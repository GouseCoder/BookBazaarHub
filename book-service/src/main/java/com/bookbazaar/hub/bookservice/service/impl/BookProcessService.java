package com.bookbazaar.hub.bookservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookbazaar.hub.bookservice.dto.AddBookRequestDto;
import com.bookbazaar.hub.bookservice.entity.Book;
import com.bookbazaar.hub.bookservice.entity.UserInfo;
import com.bookbazaar.hub.bookservice.entity.Wishlist;
import com.bookbazaar.hub.bookservice.repo.BooksRepository;
import com.bookbazaar.hub.bookservice.repo.UserRepository;
import com.bookbazaar.hub.bookservice.repo.WishlistRepo;
import com.bookbazaar.hub.bookservice.utils.CommonsUtils;
import com.bookbazaar.hub.bookservice.utils.JacksonUtil;
import com.bookbazaar.hub.utils.constants.AppConstants;
import com.bookbazaar.hub.utils.constants.ResponseConstants;
import com.bookbazaar.hub.utils.constants.ResponseKeyConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class BookProcessService {

	private static final Logger logger = LoggerFactory.getLogger(BookProcessService.class);

	@Autowired
	BooksRepository booksRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	WishlistRepo wishlistRepo;

	@Autowired
	CommonsUtils commonsUtils;

	public JsonNode addBook(AddBookRequestDto bookdto) {

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);

		try {

			Book book = new Book();
			book.setAuthorName(bookdto.getAuthorName());
			book.setCategory(bookdto.getCategory());
			book.setDescription(bookdto.getDescription());
			book.setName(bookdto.getName());
			book.setPrice(bookdto.getPrice());
			book.setCondition(bookdto.getBookCondition());
			book.setSellerId(bookdto.getSellerId());

			booksRepository.save(book);
			dataObject.put(AppConstants.ERROR_CODE, ResponseConstants.BOOK_ADDED_SUCCESSFULLY);
			dataObject.put(AppConstants.ERROR_REASON, "Book added sucessfully!!");

		} catch (Exception e) {
			logger.error("Exception in addBook ", e);
		}

		return resultNode;

	}

	public JsonNode getBookByCat(String category) {

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);

		try {

			List<Book> booksbycat = booksRepository.findByCategory(category);
			ArrayNode booksList = JacksonUtil.mapper.createArrayNode();
			for (Book book : booksbycat) {
				JsonNode bookObject = convertEntityToObject(book);
				booksList.add(bookObject);
			}

			dataObject.set(ResponseKeyConstants.BOOKS_BY_CAT, booksList);

		} catch (Exception e) {
			logger.error("Exception in getBookByCat ", e);
		}

		return resultNode;

	}

	public JsonNode getBookForUser(Long bookId, Long userId) {

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);

		try {

			Optional<Book> bookDetails = booksRepository.findById(bookId);
			addBookInUsersLastViwed(bookId, userId);
			Book book = bookDetails.get();
			JsonNode bookObject = convertEntityToObject(book);
			dataObject.set(ResponseKeyConstants.BOOK_DETAILS, bookObject);

		} catch (Exception e) {
			logger.error("Exception in getBookForUser ", e);
		}

		return resultNode;

	}

	public JsonNode getBookById(Long bookId) {

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);

		try {

			Optional<Book> bookDetails = booksRepository.findById(bookId);
			Book book = bookDetails.get();
			JsonNode bookObject = convertEntityToObject(book);
			dataObject.set(ResponseKeyConstants.BOOK_DETAILS, bookObject);

		} catch (Exception e) {
			logger.error("Exception in getBookById ", e);
		}

		return resultNode;

	}

	private void addBookInUsersLastViwed(Long bookId, Long userId) {

		Optional<UserInfo> userOptional = userRepository.findById(userId);
		if (userOptional.isPresent()) {

			UserInfo user = userOptional.get();
			List<Long> lastViewedBooks = user.getLastViewedBooks();
			lastViewedBooks.add(bookId);
			user.setLastViewedBooks(lastViewedBooks);

			userRepository.save(user);
		}

	}

	public JsonNode getbooksForDashboard(Long userId) {

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);

		boolean isUserNewlyLoggedIn = false;
		UserInfo user = null;

		try {
			Optional<UserInfo> userOptional = userRepository.findById(userId);
			if (userOptional.isPresent()) {
				user = userOptional.get();
				isUserNewlyLoggedIn = user.isFirstLogin();
			}

			if (isUserNewlyLoggedIn) {
				createDashboardForNewUser(dataObject, user);
			} else {
				createDashboardForExistingUser(dataObject, userId);
			}
		} catch (Exception e) {
			logger.error("Exception in getbooksForDashboard ", e);
		}

		return resultNode;
	}

	private void createDashboardForNewUser(ObjectNode dataObject, UserInfo user) {

		try {
			// if user is new then from now he will not be a new user
			user.setFirstLogin(false);
			userRepository.save(user);

			dataObject.set("trendingBooks", getTrendingBooks());
			dataObject.set("categorizedBooks", getPopularBooksBycat());

		} catch (Exception e) {
			logger.error("Exception in createDashboardForNewUser ", e);
		}

	}

	private void createDashboardForExistingUser(ObjectNode dataObject, Long userId) {

		try {
			dataObject.set("lastViewdBooks", getLastViwedItems(userId));
			dataObject.set("recommendations", JacksonUtil.mapper.createArrayNode());
			dataObject.set("categorizedBooks", getPopularBooksBycat());
		} catch (Exception e) {
			logger.error("Exception in createDashboardForExistingUser ", e);
		}
	}

	private ArrayNode getPopularBooksBycat() {

		List<String> popularCtegories = new ArrayList<String>();
		popularCtegories.add("Mystery");
		popularCtegories.add("Biography");
		popularCtegories.add("Classic");
		popularCtegories.add("Productivity");

		ArrayNode outerAray = JacksonUtil.mapper.createArrayNode();

		try {
			for (String category : popularCtegories) {

				ObjectNode categoryObject = JacksonUtil.mapper.createObjectNode();
				ArrayNode categoryAray = JacksonUtil.mapper.createArrayNode();
				List<Book> booksbycat = booksRepository.getPopularBooksFromDb(category);
				for (Book book : booksbycat) {
					JsonNode bookObject = convertEntityToObject(book);
					categoryAray.add(bookObject);
				}
				categoryObject.set(category, categoryAray);
				outerAray.add(categoryObject);
			}
		} catch (Exception e) {
			logger.error("Exception in getPopularBooksBycat ", e);
		}

		return outerAray;
	}

	private ArrayNode getTrendingBooks() {

		return JacksonUtil.mapper.createArrayNode();
	}

	private ArrayNode getLastViwedItems(Long userId) {
		ArrayNode lastViwedAray = JacksonUtil.mapper.createArrayNode();
		Optional<UserInfo> userOptional = userRepository.findById(userId);
		if (userOptional.isPresent()) {

			UserInfo user = userOptional.get();
			List<Long> lastViwedItems = user.getLastViewedBooks();
			List<Book> lastViewedBooks = booksRepository.getLastViewedBooksById(lastViwedItems);
			lastViwedAray = JacksonUtil.convertListToArrayNode(lastViewedBooks, Book.class);
		}

		return lastViwedAray;
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
			bookObject.put(ResponseKeyConstants.BOOK_CONDITION, book.getCondition());
			bookObject.put(ResponseKeyConstants.SELLER_ID, book.getSellerId());

		} catch (Exception e) {
			logger.error("Exception in convertEntityToObject ", e);
		}

		return bookObject;
	}

	public JsonNode addToWishList(Long userId, Long bookId) {

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);
		ObjectNode errorObject = (ObjectNode) resultNode.get(AppConstants.ERROR_OBJECT);

		try {

			UserInfo user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
			Book book = booksRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

			// Check if the book is already in the user's cart
			Wishlist existingItem = wishlistRepo.findByUserAndBook(user, book);

			if (existingItem != null) {
				// If the book is already in the cart, update the quantity
				errorObject.put(AppConstants.ERROR_CODE, ResponseConstants.PRODUCT_ALREADY_IN_CART);
				errorObject.put(AppConstants.ERROR_REASON, "Product Already in wishlist");
			} else {

				Wishlist newWishListItem = new Wishlist();
				newWishListItem.setUser(user);
				newWishListItem.setBook(book);
				wishlistRepo.save(newWishListItem);

				dataObject.put(AppConstants.ERROR_CODE, ResponseConstants.ADDED_TO_CART);
				dataObject.put(AppConstants.ERROR_REASON, "Added to Wishlist");
			}

		} catch (Exception e) {
			logger.error("Exception in addToWishList ", e);
		}

		return resultNode;

	}

	public JsonNode showWishList(Long userId) {

		ArrayNode wishListArray = JacksonUtil.mapper.createArrayNode();

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);

		try {

			UserInfo user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

			List<Wishlist> wishListItems = wishlistRepo.findByUser(user);
			for(Wishlist wishlist : wishListItems) {
				ObjectNode obj = JacksonUtil.mapper.createObjectNode();
				JsonNode book = convertEntityToObject(wishlist.getBook());
				obj.set(ResponseKeyConstants.BOOK, book);
				wishListArray.add(obj);
			}
			dataObject.set("wishList", wishListArray);

		} catch (Exception e) {
			logger.error("Exception in showCart ", e);
		}

		return resultNode;
	}

	public JsonNode getBookByFiltering(String category, Long minPrice, Long maxPrice, int pageNo) {
		
		ArrayNode bookArray = JacksonUtil.mapper.createArrayNode();

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);
		
		try {
			
			int pageSize = 10; // Number of records per page
			int offset = pageNo * pageSize;
			
			List<Book> filteredBooks = booksRepository.getFilteredBooks(category, minPrice, maxPrice, 10, offset);
			for(Book book : filteredBooks) {
				JsonNode bookObject = convertEntityToObject(book);
				bookArray.add(bookObject);
			}
			
			dataObject.set("Books", bookArray);
			
		} catch (Exception e) {
			logger.error("Exception in getBookByFiltering ", e);
		}
		
		return resultNode;
	}

}
