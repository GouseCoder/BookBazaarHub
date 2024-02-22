package com.bookbazaar.hub.cartservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookbazaar.hub.cartservice.dto.CartItemDTO;
import com.bookbazaar.hub.cartservice.entity.Book;
import com.bookbazaar.hub.cartservice.entity.MyCart;
import com.bookbazaar.hub.cartservice.entity.UserInfo;
import com.bookbazaar.hub.cartservice.repo.BooksRepository;
import com.bookbazaar.hub.cartservice.repo.CartRepository;
import com.bookbazaar.hub.cartservice.repo.UserRepository;
import com.bookbazaar.hub.cartservice.utils.CommonsUtils;
import com.bookbazaar.hub.cartservice.utils.JacksonUtil;
import com.bookbazaar.hub.utils.constants.AppConstants;
import com.bookbazaar.hub.utils.constants.ResponseConstants;
import com.bookbazaar.hub.utils.constants.ResponseKeyConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class CartService {
	
	private static final Logger logger = LoggerFactory.getLogger(CartService.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BooksRepository booksRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	CommonsUtils commonsUtils;
	
	public JsonNode addToCart(Long userId, Long bookId) {
		
		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);
		ObjectNode errorObject = (ObjectNode) resultNode.get(AppConstants.ERROR_OBJECT);
		
		try {
			
			UserInfo user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
	        Book book = booksRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
			
			// Check if the book is already in the user's cart
	        MyCart existingCartItem = cartRepository.findByUserAndBook(user, book);
	        
	        if(existingCartItem != null) {
	        	 // If the book is already in the cart, update the quantity
	        	errorObject.put(AppConstants.ERROR_CODE, ResponseConstants.PRODUCT_ALREADY_IN_CART);
	        	errorObject.put(AppConstants.ERROR_REASON, "Product Already in cart");
	        }
	        else {
	        	
	        	MyCart newCartItem = new MyCart();
	        	newCartItem.setUser(user);
	            newCartItem.setBook(book);
	            newCartItem.setQuantity(1);
	            cartRepository.save(newCartItem);
	            
	            dataObject.put(AppConstants.ERROR_CODE, ResponseConstants.ADDED_TO_CART);
	            dataObject.put(AppConstants.ERROR_REASON, "Added to cart");
	        }
			
		} catch (Exception e) {
			logger.error("Exception in addToCart " , e);
		}
		
		return resultNode;
		
    }
	public JsonNode showCart(Long userId) {
		
		ArrayNode cartArray = JacksonUtil.mapper.createArrayNode();
		
		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);
		
		try {
			
			UserInfo user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
			
			List<MyCart> cartItems = cartRepository.findByUser(user);
	        List<CartItemDTO> cartItemDTOs = CartItemDTO.convertToDTO(cartItems);
	        
	        for (CartItemDTO cartItem : cartItemDTOs) {
	            double totalPrice = cartItem.getBook().getPrice() * cartItem.getQuantity();
	            cartItem.setTotalPrice(totalPrice);
	            
	            ObjectNode carItemsObject = JacksonUtil.mapper.createObjectNode();
	            JsonNode book = convertEntityToObject(cartItem.getBook());
	            carItemsObject.set(ResponseKeyConstants.BOOK, book);
	            carItemsObject.put(ResponseKeyConstants.QTY, cartItem.getQuantity());
	            carItemsObject.put(ResponseKeyConstants.TOTAL_PRICE, cartItem.getTotalPrice());
	            
	            cartArray.add(carItemsObject);
	            
	        }
	        
	        dataObject.set("cartItems", cartArray);
	       
		} catch (Exception e) {
			logger.error("Exception in showCart " , e);
		}
		
		return resultNode;
	}
    
    private JsonNode convertEntityToObject(Book book) {
		
		ObjectNode bookObject = JacksonUtil.mapper.createObjectNode();
		try {
			
			bookObject.put(ResponseKeyConstants.BOOK_ID, book.getId());
			bookObject.put(ResponseKeyConstants.BOOK_NAME, book.getName());
			bookObject.put(ResponseKeyConstants.BOOK_DESC, book.getDescription());
			bookObject.put(ResponseKeyConstants.PRICE, book.getPrice());
			bookObject.put(ResponseKeyConstants.BOOK_AUTHOR, book.getAuthorName());
			bookObject.put(ResponseKeyConstants.BOOK_CAT, book.getCategory());
			bookObject.put(ResponseKeyConstants.BOOK_RATING_STAR, book.getRatinginStar());
			
		} catch (Exception e) {
			logger.error("Exception in convertEntityToObject ", e);
		}
	
		return bookObject;
	}
    
    
	public JsonNode increaseQuantity(Long userId, Long bookId) {
		
		try {
			
			UserInfo user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
	        Book book = booksRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
			
	        MyCart cartItem = cartRepository.findByUserAndBook(user, book);

	        cartItem.setQuantity(cartItem.getQuantity() + 1);
	        cartRepository.save(cartItem);
			
		} catch (Exception e) {
			logger.error("Exception in increaseQuantity ", e);
		}
		
		return showCart(userId);
	}
	
	public JsonNode decreaseQuantity(Long userId, Long bookId) {
		
		try {
			
			UserInfo user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
	        Book book = booksRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
			
	        MyCart cartItem = cartRepository.findByUserAndBook(user, book);
	        if(cartItem.getQuantity()==1) {
	        	removeFromCart(userId, bookId);
	        }
	        else {
	        	cartItem.setQuantity(cartItem.getQuantity() - 1);
		        cartRepository.save(cartItem);
	        }
			
		} catch (Exception e) {
			logger.error("Exception in decreaseQuantity ", e);
		}
		
		return showCart(userId);
	}
	
	public JsonNode removeFromCart(Long userId, Long bookId) {
		
		try {
			
			UserInfo user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
	        Book book = booksRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
			
	        MyCart cartItem = cartRepository.findByUserAndBook(user, book);
	        cartRepository.delete(cartItem);
	        
			
		} catch (Exception e) {
			logger.error("Exception in removeFromCart ", e);
		}
		
		return showCart(userId);
	}

}
