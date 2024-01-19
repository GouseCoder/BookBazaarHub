package com.bookbazaar.hub.userservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {
	
	public static ObjectMapper mapper;
	
	static {
		
		mapper = new ObjectMapper();
	}

}
