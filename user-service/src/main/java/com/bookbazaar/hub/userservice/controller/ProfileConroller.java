package com.bookbazaar.hub.userservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookbazaar.hub.userservice.service.UserService;
import com.bookbazaar.hub.userservice.utils.CommonsUtils;
import com.bookbazaar.hub.userservice.utils.JacksonUtil;
import com.bookbazaar.hub.utils.constants.AppConstants;
import com.bookbazaar.hub.utils.response.ApiHttpResponse;
import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.ApiOperation;

@RestController
public class ProfileConroller {
	
private static final Logger logger = LoggerFactory.getLogger(ProfileConroller.class);
	
	@Autowired
    private UserService userService;
	
	@Autowired
	CommonsUtils commonsUtils;
	
	@PutMapping("/v1/changePassword")
    @ApiOperation(value = "change you password")
    public ResponseEntity<ApiHttpResponse> changePassword(@RequestParam Long userId, @RequestParam String newPassword) {
    	
    	JsonNode resultNode = JacksonUtil.mapper.createArrayNode();
    	
    	try {
    		resultNode = userService.updatePassword(userId, newPassword);
    		logger.debug("resultNode " + resultNode);
    		
    		return new ResponseEntity<>(new ApiHttpResponse(commonsUtils.getStatusCode(resultNode), 
    				resultNode.get(AppConstants.ERROR_OBJECT), 
					resultNode.get(AppConstants.DATA_OBJECT)), HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiHttpResponse(AppConstants.INTERNAL_SERVER_ERROR, 
					resultNode.get(AppConstants.ERROR_OBJECT)), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@PutMapping("/v1/changeAddress")
    @ApiOperation(value = "update new address")
    public ResponseEntity<ApiHttpResponse> changeAddress(@RequestParam Long userId, @RequestParam String newAddress) {
    	
    	JsonNode resultNode = JacksonUtil.mapper.createArrayNode();
    	
    	try {
    		resultNode = userService.updateAddress(userId, newAddress);
    		logger.debug("resultNode " + resultNode);
    		
    		return new ResponseEntity<>(new ApiHttpResponse(commonsUtils.getStatusCode(resultNode), 
    				resultNode.get(AppConstants.ERROR_OBJECT), 
					resultNode.get(AppConstants.DATA_OBJECT)), HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiHttpResponse(AppConstants.INTERNAL_SERVER_ERROR, 
					resultNode.get(AppConstants.ERROR_OBJECT)), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@PutMapping("/v1/changePhone")
    @ApiOperation(value = "update new phone No")
    public ResponseEntity<ApiHttpResponse> changePhone(@RequestParam Long userId, @RequestParam Long newPhone) {
    	
    	JsonNode resultNode = JacksonUtil.mapper.createArrayNode();
    	
    	try {
    		resultNode = userService.updatePhone(userId, newPhone);
    		logger.debug("resultNode " + resultNode);
    		
    		return new ResponseEntity<>(new ApiHttpResponse(commonsUtils.getStatusCode(resultNode), 
    				resultNode.get(AppConstants.ERROR_OBJECT), 
					resultNode.get(AppConstants.DATA_OBJECT)), HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiHttpResponse(AppConstants.INTERNAL_SERVER_ERROR, 
					resultNode.get(AppConstants.ERROR_OBJECT)), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

}
