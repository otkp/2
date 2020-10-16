package org.epragati.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;

import org.epragati.model.PanDetailsModel;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


public interface PanService {
	
	public PanDetailsModel getPanDetails(String PanNumber)throws KeyStoreException, FileNotFoundException, Exception;
	
	PanDetailsModel getPanDetailsNewServ(String panNo) throws JsonParseException, JsonMappingException, IOException;	  

}
