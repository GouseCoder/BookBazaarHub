package com.bookbazaar.hub.userservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookbazaar.hub.userservice.entity.UserInfo;
import com.bookbazaar.hub.userservice.entity.UserRole;
import com.bookbazaar.hub.userservice.repo.UserRepository;
import com.bookbazaar.hub.userservice.repo.UserRoleRepository;
import com.bookbazaar.hub.userservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void registerUser(UserInfo user) {
		
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			logger.info("Email already registered");
			throw new RuntimeException("Email already registered");
		} else {
			UserRole userRole = user.getRoles().stream()
		            .findFirst()
		            .orElseThrow(() -> new RuntimeException("UserRole not found"));
			
			if (userRole.getId() == 0 || userRole.getName()==null) {
				userRoleRepository.save(userRole);
			}
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		logger.info("Registration Successful");
	}

}
