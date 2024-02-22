package com.bookbazaar.hub.cartservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(length = 1000)
	private String description;
	private long price;
	private String authorName;
	private String category;
	private int ratinginStar;
	@Column(length = 1000)
	private String condition;
	private Long sellerId;
	
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public Book() {
		super();
		
	}

	public Book(Long id, String name, String description, long price, String authorName, String category,
			int ratinginStar) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.authorName = authorName;
		this.category = category;
		this.ratinginStar = ratinginStar;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getRatinginStar() {
		return ratinginStar;
	}

	public void setRatinginStar(int ratinginStar) {
		this.ratinginStar = ratinginStar;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}
	
	

}
