package com.bookbazaar.hub.userservice.utils;

import com.fasterxml.jackson.databind.JsonNode;

public class ApiHttpResponse {
	
	private int errCode;
	private JsonNode dataObject;
	
	public ApiHttpResponse(int errCode, JsonNode dataObject) {
		super();
		this.errCode = errCode;
		this.dataObject = dataObject;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public JsonNode getDataObject() {
		return dataObject;
	}

	public void setDataObject(JsonNode dataObject) {
		this.dataObject = dataObject;
	}
	
	
	
}
