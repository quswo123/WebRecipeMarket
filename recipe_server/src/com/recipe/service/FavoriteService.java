package com.recipe.service;

import java.util.ArrayList;
import java.util.List;

import com.recipe.dao.FavoriteDAO;
import com.recipe.exception.AddException;
import com.recipe.exception.DuplicatedException;
import com.recipe.exception.FindException;
import com.recipe.exception.RemoveException;
import com.recipe.vo.Favorite;

/**
 * @author Soojeong
 *
 */
public class FavoriteService {
	private FavoriteDAO dao = new FavoriteDAO();
	
	public void add(Favorite f)	throws AddException, DuplicatedException {
		dao.insert(f);
	}
	
	public List<Favorite> findById(String customerId) throws FindException {
		List<Favorite> favoriteList = new ArrayList<>();
		favoriteList = dao.selectById(customerId);
		return favoriteList;
	}
	
	public void remove(Favorite f) throws RemoveException {
		dao.deleteByIdnCode(f);
	}
} // end class FavoriteService
