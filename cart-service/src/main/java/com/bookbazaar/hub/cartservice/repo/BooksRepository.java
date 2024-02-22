package com.bookbazaar.hub.cartservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookbazaar.hub.cartservice.entity.Book;

public interface BooksRepository extends JpaRepository<Book, Long>{
	
	List<Book> findByCategory(String category);
	
	@Query(value = "select * from book where category = :category order by ratingin_star", nativeQuery = true)
	List<Book> getPopularBooksFromDb(String category);
}
