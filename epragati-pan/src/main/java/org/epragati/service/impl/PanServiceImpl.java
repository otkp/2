package org.epragati.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.epragati.exception.BadRequestException;
import org.epragati.model.EPragathiPANRequestModel;
import org.epragati.model.PanDetailsModel;
import org.epragati.service.APIBased.DummyHostnameVerifier;
import org.epragati.service.APIBased.DummyTrustManager;
import org.epragati.utill.Constant;
import org.epragati.service.PanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PanServiceImpl implements PanService{


	@Value("${rta.pan.userid}")
	private String userName;


	@Value("${rta.pan.password}")
	private String Password;

	@Value("${rta.pan.url}")
	private String panUrl;
	
	@Value("${ePragati.pan.token}")
	private String ePragatiPanToken;
	
	@Value("${ePragati.pan.url}")
	private String ePragatiPanURL;
	
	@Autowired
	private RestTemplate restTemplate;

	private static final Logger logger = LoggerFactory.getLogger(PanServiceImpl.class);

	@Override
	public PanDetailsModel getPanDetails(String PanNumber) throws KeyStoreException, FileNotFoundException, Exception {

		PanDetailsModel pdModel = new PanDetailsModel();
		KeyStore keystore = KeyStore.getInstance("jks");
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream input = new FileInputStream(classLoader.getResource("files/oupt.jks").getFile());
		try {
			char[] password=Password.toCharArray();
			keystore.load(input, password);
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (NoSuchAlgorithmException e1) {
			logger.error(e1.getMessage());
		} catch (CertificateException e1) {
			logger.error(e1.getMessage());
		} 
		
		IOUtils.closeQuietly(input);

		Enumeration e = keystore.aliases();
		String alias = "";
		if(e!=null)
		{
			while (e.hasMoreElements())
			{
				String  n = (String)e.nextElement();
				if (keystore.isKeyEntry(n))
				{
					alias = n;
				}
			}
		}
		PrivateKey privateKey = null;
		try {
			privateKey = (PrivateKey) keystore.getKey(alias, Password.toCharArray());
		} catch (UnrecoverableKeyException | NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		String userId = userName;

		String data=userId.concat(PanNumber);


		X509Certificate myPubCert=(X509Certificate) keystore.getCertificate(alias);
		byte[] dataToSign=data.getBytes();
		CMSSignedDataGenerator sgen = new CMSSignedDataGenerator();
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider ());
		sgen.addSigner(privateKey, myPubCert,CMSSignedDataGenerator.DIGEST_SHA1);
		Certificate[] certChain =keystore.getCertificateChain(alias);
		ArrayList certList = new ArrayList();
		CertStore certs = null;
		for (int i=0; i < certChain.length; i++)
			certList.add(certChain[i]); 
		try {
			sgen.addCertificatesAndCRLs(CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC"));
		} catch (CertStoreException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
				| NoSuchProviderException | CMSException e1) {
			e1.printStackTrace();
		}
		CMSSignedData csd = null;
		try {
			csd = sgen.generate(new CMSProcessableByteArray(dataToSign),true, "BC");
		} catch (NoSuchAlgorithmException | NoSuchProviderException | CMSException e1) {
			e1.printStackTrace();
		}
		byte[] signedData = null;
		try {
			signedData = csd.getEncoded();
		} catch (IOException e1) {
			logger.error(e1.getMessage());
			e1.printStackTrace();
		}
		byte[] signedData64 = Base64.encodeBase64(signedData); 
		String signature1 = new String(signedData64);
		String signature=signature1;
		Date startTime=null;
		Calendar c1=Calendar.getInstance();
		startTime=c1.getTime();

		Date connectionStartTime=null;
		String logMsg="\n-";
		BufferedWriter out=null;
		FileWriter fstream=null;
		Calendar c=Calendar.getInstance();
		long nonce=c.getTimeInMillis();


		try{
			fstream= new FileWriter("API_PAN_verification.logs",true);
			out = new BufferedWriter(fstream);
		}
		catch(Exception e1){
			logMsg+="::Exception: "+e1.getMessage()+" ::Program Start Time:"+startTime+"::nonce= "+nonce;
			out.write(logMsg);
			out.close();
		}


		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContext.getInstance("SSL");

			sslcontext.init(new KeyManager[0],
					new TrustManager[] { new DummyTrustManager() },
					new SecureRandom());
		} catch (NoSuchAlgorithmException e2) {
			logMsg+="::Exception: "+e2.getMessage()+" ::Program Start Time:"+startTime+"::nonce= "+nonce;
			e2.printStackTrace(System.err);
			out.write(logMsg);
			out.close();
		} catch (KeyManagementException e3) {
			logMsg+="::Exception: "+e3.getMessage()+" ::Program Start Time:"+startTime+"::nonce= "+nonce;
			e3.printStackTrace(System.err);
			out.write(logMsg);
			out.close();
		}

		SSLSocketFactory factory = sslcontext.getSocketFactory();


		String urlParameters="data=";
		try{
			urlParameters =urlParameters + URLEncoder.encode(data, "UTF-8") +"&signature=" + URLEncoder.encode(signature, "UTF-8");
		}catch(Exception e1){
			logMsg+="::Exception: "+e1.getMessage()+" ::Program Start Time:"+startTime+"::nonce= "+nonce;
			e1.printStackTrace();
			out.write(logMsg);
			out.close();
		}

		try{

			URL url;
			HttpsURLConnection connection;
			InputStream is = null;

			url = new URL(panUrl);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setSSLSocketFactory(factory);
			connection.setHostnameVerifier(new DummyHostnameVerifier());

			OutputStream os = connection.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			osw.write(urlParameters);
			osw.flush();
			connectionStartTime=new Date();
			logMsg+="::Request Sent At: " + connectionStartTime;
			logMsg+="::Request Data: "+ data;
			osw.close();
			is =connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String line =null;
			line = in.readLine();
			String[] strs = line.split("[\\^]");
			pdModel.setStatus(Integer.parseInt(strs[0]));
			pdModel.setPanNumber(strs[1]);
			pdModel.setIsValid(strs[2]);
			pdModel.setLastName(strs[3]);
			pdModel.setFirstName(strs[4]);
			pdModel.setMiddleName(strs[5]);
			pdModel.setSalutation(strs[6]);
			pdModel.setPanUpdatedDateAtITDEnd(strs[7]);
			is.close();
			in.close();
		}
		catch(ConnectException e4){
			logger.error(e4.getMessage());
			logMsg+="::Exception: "+e4.getMessage() + "::Program Start Time:"+startTime+"::nonce= "+nonce;
			out.write(logMsg);
			out.close();
		}
		catch(Exception e1){
			logger.error(e1.getMessage());
			logMsg+="::Exception: "+e1.getMessage()+ "::Program Start Time:"+startTime+"::nonce= "+nonce;
			out.write(logMsg);
			out.close();
			e1.printStackTrace();
		}

		out.write(logMsg);
		out.close();

		return pdModel;
	}

	@Override
	public PanDetailsModel getPanDetailsNewServ(String panNo) throws JsonParseException, JsonMappingException, IOException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", ePragatiPanToken);
		EPragathiPANRequestModel ePragathiPANRequestModel=new EPragathiPANRequestModel();
		ePragathiPANRequestModel.setPan(panNo);
		HttpEntity<EPragathiPANRequestModel> httpEntity = new HttpEntity<>(ePragathiPANRequestModel, headers);
		//RestTemplate restTemplateL = new RestTemplate();
		
		long startTime= System.currentTimeMillis();
		logger.info("Request sending to PAN service, endpount:{} & PAN No: {}",ePragatiPanURL,panNo);
		@SuppressWarnings("rawtypes")
		ResponseEntity<String> response =restTemplate.exchange(ePragatiPanURL, HttpMethod.POST, httpEntity,String.class);
		logger.info("Responce recived from PAN  endpount. PAN No: {}, Total time to consume the service: {}ms",panNo,(System.currentTimeMillis()-startTime));
		logger.debug("No Response from PAN service. panNo:{}",panNo);
		if (response == null || StringUtils.isEmpty(response.getBody())) {
			logger.debug("No Response from PAN service. panNo:{}",panNo);
			throw new BadRequestException("No response from PAN service.");
		}
		ObjectMapper mapper = new ObjectMapper();
		List<PanDetailsModel> panDetailsModels =
			    mapper.readValue(response.getBody(), new TypeReference<List<PanDetailsModel>>(){});
		logger.debug("Response from PAN service is {}.",panDetailsModels);
		if(panDetailsModels.isEmpty() || (!Constant.Status.E.getDesc().equals(panDetailsModels.get(0))
				&& StringUtils.isEmpty(panDetailsModels.get(0).getLastName()))) {
			logger.debug("Invalid PAN number: {}",panNo);
			throw new BadRequestException("Invalid PAN number. ");
		}
		return panDetailsModels.get(0);
	}
}
