package com.bookbazaar.hub.cartservice.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.bookbazaar.hub.cartservice.entity.Book;
import com.bookbazaar.hub.cartservice.entity.MyCart;

public class CartItemDTO {
	
	private Book book;
    private int quantity;
    private double totalPrice;
    
    public CartItemDTO(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }
    
 // Convert Cart entities to CartItemDTOs
    public static List<CartItemDTO> convertToDTO(List<MyCart> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new CartItemDTO(cartItem.getBook(), cartItem.getQuantity()))
                .collect(Collectors.toList());
    }
    
    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
