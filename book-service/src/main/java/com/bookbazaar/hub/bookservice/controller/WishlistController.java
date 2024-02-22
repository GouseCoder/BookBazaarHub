package com.bookbazaar.hub.bookservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookbazaar.hub.bookservice.service.impl.BookProcessService;
import com.bookbazaar.hub.bookservice.utils.CommonsUtils;
import com.bookbazaar.hub.bookservice.utils.JacksonUtil;
import com.bookbazaar.hub.utils.constants.AppConstants;
import com.bookbazaar.hub.utils.response.ApiHttpResponse;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {
	
	private static final Logger logger = LoggerFactory.getLogger(WishlistController.class);
	
	@Autowired
	BookProcessService bookService;
	
	@Autowired
	CommonsUtils commonsUtils;
	
	@PostMapping("/add")
    public ResponseEntity<ApiHttpResponse> addToCart(@RequestParam Long userId, @RequestParam Long bookId) {
		
		JsonNode resultNode = JacksonUtil.mapper.createArrayNode();
		try {
			
			resultNode = bookService.addToWishList(userId, bookId);
			logger.debug("resultNode " + resultNode);
			
			return new ResponseEntity<>(new ApiHttpResponse(commonsUtils.getStatusCode(resultNode), 
    				resultNode.get(AppConstants.ERROR_OBJECT), 
					resultNode.get(AppConstants.DATA_OBJECT)), HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiHttpResponse(AppConstants.INTERNAL_SERVER_ERROR, 
					resultNode.get(AppConstants.ERROR_OBJECT)), HttpStatus.INTERNAL_SERVER_ERROR);
		} 
    }
	
	@GetMapping("/show")
    public ResponseEntity<ApiHttpResponse> showCart(@RequestParam Long userId) {
		
		JsonNode resultNode = JacksonUtil.mapper.createArrayNode();
		try {
			
			resultNode = bookService.showWishList(userId);
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
