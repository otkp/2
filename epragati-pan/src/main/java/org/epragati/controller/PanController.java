package org.epragati.controller;

import java.io.FileNotFoundException;
import java.security.KeyStoreException;

import org.apache.commons.lang3.StringUtils;
import org.epragati.exception.BadRequestException;
import org.epragati.model.PanDetailsModel;
import org.epragati.service.PanService;
import org.epragati.util.GateWayResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PanController {

	private static final Logger logger = LoggerFactory.getLogger(PanController.class);

	@Autowired
	private PanService panService;
	
	@Value("${rta.pan.token}")
	private String token;
	

	@RequestMapping(method = { RequestMethod.GET } , path ="/getPandetails")
	public GateWayResponse<?> getUserFromToken(@RequestParam(name = "panNumber") String panNumber, @RequestHeader("Authorization") String authToken)
			throws KeyStoreException, FileNotFoundException, Exception {
		
		if(StringUtils.isEmpty(authToken) && !token.equals(authToken)){
			return new GateWayResponse<String>(HttpStatus.UNAUTHORIZED, "unauthorized", "unauthorized");
		}
		
		logger.debug("Pan Controller request getting pan number as Parameter: {}",panNumber);
		try {
			return new GateWayResponse<>(panService.getPanDetailsNewServ(panNumber));
			
		} catch (BadRequestException e) {
			logger.error("BadRequestException while call getPandetails , pannumber: {}, {}",panNumber,e.getMessage());
			return new GateWayResponse<String>(HttpStatus.BAD_REQUEST,  e.getMessage());
		}
		catch (Exception e) {
			logger.error("Excpetion while call getPandetails , pannumber: {}, {}",panNumber,e);
			return new GateWayResponse<String>(HttpStatus.INTERNAL_SERVER_ERROR,  e.getMessage());
		}
	}
	
	@GetMapping(value = "panSearchTest", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public GateWayResponse<?> getWarUpStatus(@RequestHeader("Authorization") String authString) {
		
		if(StringUtils.isNoneEmpty(authString)){
			if(authString.equals("Abcdf77878RhhghJyghjgUhsdhgshfgN")){
				return new GateWayResponse<>(HttpStatus.OK, "Success");
			}
		}
		return new GateWayResponse<>(HttpStatus.OK, "UnAuthorized Requset");
	}

}
