package com.bookbazaar.hub.userservice.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookbazaar.hub.userservice.entity.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Long>{
	
	 UserInfo findByUsername(String username);
	 Optional<UserInfo> findByEmail(String email);

}
