package com.recipe.service;

import java.util.ArrayList;
import java.util.List;

import com.recipe.dao.ReviewDAO;
import com.recipe.exception.AddException;
import com.recipe.exception.DuplicatedException;
import com.recipe.exception.FindException;
import com.recipe.exception.RemoveException;
import com.recipe.vo.Review;

/**
 * @author Soojeong
 *
 */
public class ReviewService {
	private ReviewDAO dao;
	
	public ReviewService() {
		dao = new ReviewDAO();
	}
	
	public List<Review> findByCode(int recipeCode) throws FindException {
		List<Review> reviewList = new ArrayList<>();
		reviewList = dao.selectByCode(recipeCode); 
		return reviewList;
	}
	
	public void add(Review r ) throws AddException, DuplicatedException {
		dao.insert(r);
	}
	
	public void remove(Review r ) throws RemoveException {
		dao.deleteByIdnCode(r);
	}
	
	public List<Review> findById(String customerId) throws FindException {
		List<Review> reviewList = new ArrayList<>();
		reviewList = dao.selectById(customerId);
		return reviewList;
	}

} // end class ReviewService
