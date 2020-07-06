package com.recipe.service;

import java.util.List;

import com.recipe.dao.PostalDAO;
import com.recipe.exception.FindException;
import com.recipe.vo.Postal;
/*
 * PostalDAO의 selectByDoro 호출
 * @param String doro
 * @author 영민
 */
public class PostService {
	private PostalDAO dao= new PostalDAO();
	
	public List<Postal> findByDoro(String doro) throws FindException{
		return dao.selectByDoro(doro);
		
	}
}
