package com.bookbazaar.hub.cartservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookbazaar.hub.cartservice.service.impl.CartService;
import com.bookbazaar.hub.cartservice.utils.CommonsUtils;
import com.bookbazaar.hub.cartservice.utils.JacksonUtil;
import com.bookbazaar.hub.utils.constants.AppConstants;
import com.bookbazaar.hub.utils.response.ApiHttpResponse;
import com.fasterxml.jackson.databind.JsonNode;


@RestController
public class CartOperationController {
	
	private static final Logger logger = LoggerFactory.getLogger(CartOperationController.class);
	
	@Autowired
	CartService cartService;
	
	@Autowired
	CommonsUtils commonsUtils;
	
	@PostMapping("/add")
    public ResponseEntity<ApiHttpResponse> addToCart(@RequestParam Long userId, @RequestParam Long bookId) {
		
		JsonNode resultNode = JacksonUtil.mapper.createArrayNode();
		try {
			
			resultNode = cartService.addToCart(userId, bookId);
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
			
			resultNode = cartService.showCart(userId);
			logger.debug("resultNode " + resultNode);
			
			return new ResponseEntity<>(new ApiHttpResponse(commonsUtils.getStatusCode(resultNode), 
    				resultNode.get(AppConstants.ERROR_OBJECT), 
					resultNode.get(AppConstants.DATA_OBJECT)), HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiHttpResponse(AppConstants.INTERNAL_SERVER_ERROR, 
					resultNode.get(AppConstants.ERROR_OBJECT)), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@PutMapping("/increase")
    public ResponseEntity<ApiHttpResponse> increaseQty(@RequestParam Long userId, @RequestParam Long bookId) {
		
		JsonNode resultNode = JacksonUtil.mapper.createArrayNode();
		try {
			
			resultNode = cartService.increaseQuantity(userId, bookId);
			logger.debug("resultNode " + resultNode);
			
			return new ResponseEntity<>(new ApiHttpResponse(commonsUtils.getStatusCode(resultNode), 
    				resultNode.get(AppConstants.ERROR_OBJECT), 
					resultNode.get(AppConstants.DATA_OBJECT)), HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiHttpResponse(AppConstants.INTERNAL_SERVER_ERROR, 
					resultNode.get(AppConstants.ERROR_OBJECT)), HttpStatus.INTERNAL_SERVER_ERROR);
		}
       
       
    }
	
	@PutMapping("/decrease")
    public ResponseEntity<ApiHttpResponse> decreaseQty(@RequestParam Long userId, @RequestParam Long bookId) {
		
		JsonNode resultNode = JacksonUtil.mapper.createArrayNode();
		try {
			
			resultNode = cartService.decreaseQuantity(userId, bookId);
			logger.debug("resultNode " + resultNode);
			
			return new ResponseEntity<>(new ApiHttpResponse(commonsUtils.getStatusCode(resultNode), 
    				resultNode.get(AppConstants.ERROR_OBJECT), 
					resultNode.get(AppConstants.DATA_OBJECT)), HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiHttpResponse(AppConstants.INTERNAL_SERVER_ERROR, 
					resultNode.get(AppConstants.ERROR_OBJECT)), HttpStatus.INTERNAL_SERVER_ERROR);
		}
       
       
    }
	
	@DeleteMapping("/delete")
    public ResponseEntity<ApiHttpResponse> removeFomCart(@RequestParam Long userId, @RequestParam Long bookId) {
		
		JsonNode resultNode = JacksonUtil.mapper.createArrayNode();
		try {
			
			resultNode = cartService.removeFromCart(userId, bookId);
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
