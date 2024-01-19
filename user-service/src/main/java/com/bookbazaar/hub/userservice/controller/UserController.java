package com.bookbazaar.hub.userservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookbazaar.hub.userservice.dto.JwtResponse;
import com.bookbazaar.hub.userservice.dto.LoginRequest;
import com.bookbazaar.hub.userservice.entity.UserInfo;
import com.bookbazaar.hub.userservice.exceptions.CustomUsernameNotFoundException;
import com.bookbazaar.hub.userservice.service.UserService;
import com.bookbazaar.hub.userservice.utils.ApiHttpResponse;
import com.bookbazaar.hub.userservice.utils.JacksonUtil;
import com.bookbazaar.hub.userservice.utils.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
    private UserService userService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtService jwtService;

    @PostMapping("/v1/register")
    @ApiOperation(value = "Register account")
    public ResponseEntity<ApiHttpResponse> registerUser(@RequestBody UserInfo user) throws JsonProcessingException {
    	logger.info("Request from user for registration : {}" , JacksonUtil.mapper.writeValueAsString(user));
    	userService.registerUser(user);
        return new ResponseEntity<>(new ApiHttpResponse(200, null), HttpStatus.CREATED);
    }
    
    @PostMapping("/v1/login")
    public JwtResponse AuthenticateAndGetToken(@RequestBody LoginRequest authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
           return new JwtResponse(jwtService.GenerateToken(authRequestDTO.getUsername()));
        } else {
            throw new CustomUsernameNotFoundException("invalid user request..!!");
        }
    }

}
