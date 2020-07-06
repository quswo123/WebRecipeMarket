package com.recipe.service;

import java.util.List;

import com.recipe.dao.PurchaseDAO;
import com.recipe.dao.ReviewDAO;
import com.recipe.exception.AddException;
import com.recipe.exception.FindException;
import com.recipe.vo.Purchase;

public class PurchaseService {
	PurchaseDAO purchasedao = new PurchaseDAO();

	public void buy(Purchase p)  throws AddException{
		purchasedao.insert(p);
	}
	
	
	public List<Purchase> findById(String customerId) throws FindException{
		return purchasedao.selectById(customerId);
	}

}
