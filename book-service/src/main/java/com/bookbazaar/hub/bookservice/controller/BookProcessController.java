package com.bookbazaar.hub.bookservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookbazaar.hub.bookservice.dto.AddBookRequestDto;
import com.bookbazaar.hub.bookservice.service.impl.BookProcessService;
import com.bookbazaar.hub.bookservice.utils.CommonsUtils;
import com.bookbazaar.hub.bookservice.utils.JacksonUtil;
import com.bookbazaar.hub.utils.constants.AppConstants;
import com.bookbazaar.hub.utils.response.ApiHttpResponse;
import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.ApiOperation;

@RestController
public class BookProcessController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookProcessController.class);
	
	@Autowired
	BookProcessService bookProcessService;
	
	@Autowired
	CommonsUtils commonsUtils;
	
	@PostMapping("/AddBook")
    @ApiOperation(value = "Add Book To Sell")
    public ResponseEntity<ApiHttpResponse> addBookToSell(@RequestBody AddBookRequestDto bookDto) {
    	
    	JsonNode resultNode = JacksonUtil.mapper.createArrayNode();
    	
    	try {
    		logger.info("Request from user for registration : {}" , JacksonUtil.mapper.writeValueAsString(bookDto));
    		resultNode = bookProcessService.addBook(bookDto);
    		logger.debug("resultNode " + resultNode);
    		
    		return new ResponseEntity<>(new ApiHttpResponse(commonsUtils.getStatusCode(resultNode), 
    				resultNode.get(AppConstants.ERROR_OBJECT), 
					resultNode.get(AppConstants.DATA_OBJECT)), HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiHttpResponse(AppConstants.INTERNAL_SERVER_ERROR, 
					resultNode.get(AppConstants.ERROR_OBJECT)), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	
    }
	
	@GetMapping("/BookBycategory")
    @ApiOperation(value = "Books By Category")
    public ResponseEntity<ApiHttpResponse> getBookByCategory(@RequestParam(name = "category") String category) {
    	
    	JsonNode resultNode = JacksonUtil.mapper.createArrayNode();
    	
    	try {
    		resultNode = bookProcessService.getBookByCat(category);
    		logger.debug("resultNode " + resultNode);
    		
    		return new ResponseEntity<>(new ApiHttpResponse(commonsUtils.getStatusCode(resultNode), 
    				resultNode.get(AppConstants.ERROR_OBJECT), 
					resultNode.get(AppConstants.DATA_OBJECT)), HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiHttpResponse(AppConstants.INTERNAL_SERVER_ERROR, 
					resultNode.get(AppConstants.ERROR_OBJECT)), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	
    }
	
	@GetMapping("/Book")
    @ApiOperation(value = "Books By id")
    public ResponseEntity<ApiHttpResponse> getBookById(@RequestParam(name = "bookId") Long bookId, @RequestParam(name = "userId") Long userId) {
    	
    	JsonNode resultNode = JacksonUtil.mapper.createArrayNode();
    	
    	try {
    		resultNode = bookProcessService.getBookForUser(bookId, userId);
    		logger.debug("resultNode " + resultNode);
    		
    		return new ResponseEntity<>(new ApiHttpResponse(commonsUtils.getStatusCode(resultNode), 
    				resultNode.get(AppConstants.ERROR_OBJECT), 
					resultNode.get(AppConstants.DATA_OBJECT)), HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiHttpResponse(AppConstants.INTERNAL_SERVER_ERROR, 
					resultNode.get(AppConstants.ERROR_OBJECT)), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	
    }
	
	@GetMapping("/showFilteredBook")
    @ApiOperation(value = "Books By filter")
    public ResponseEntity<ApiHttpResponse> getBookByFiltering(@RequestParam(name = "category") String category, @RequestParam(name = "minPrice") Long minPrice,
    		@RequestParam(name = "maxPrice") Long maxPrice, @RequestParam(name = "pageNo") int pageNo) {
    	
    	JsonNode resultNode = JacksonUtil.mapper.createArrayNode();
    	
    	try {
    		resultNode = bookProcessService.getBookByFiltering(category, minPrice, maxPrice, pageNo);
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
