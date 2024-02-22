package com.bookbazaar.hub.cartservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookbazaar.hub.cartservice.entity.Book;
import com.bookbazaar.hub.cartservice.entity.MyCart;
import com.bookbazaar.hub.cartservice.entity.UserInfo;

public interface CartRepository extends JpaRepository<MyCart, Long>{

	MyCart findByUserAndBook(UserInfo user, Book book);

	List<MyCart> findByUser(UserInfo user);

}
