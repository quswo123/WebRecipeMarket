package com.recipe.service;

import java.util.List;

import com.recipe.dao.PointDAO;
import com.recipe.dao.RecipeInfoDAO;
import com.recipe.dao.RecipeIngredientDAO;
import com.recipe.exception.DuplicatedException;
import com.recipe.exception.FindException;
import com.recipe.exception.ModifyException;
import com.recipe.vo.Point;
import com.recipe.vo.Ingredient;
import com.recipe.vo.RecipeInfo;

public class RecipeService {
	private RecipeInfoDAO dao = new RecipeInfoDAO();	
	private RecipeIngredientDAO idao = new RecipeIngredientDAO();
	private PointDAO pointDAO = new PointDAO();
	
	public List<RecipeInfo> findByName(String recipeName) throws FindException{
		return dao.selectByName(recipeName);		
	}
	public RecipeInfo findByCode(int recipeCode) throws FindException {
		return dao.selectByCode(recipeCode);
	}
	public List<RecipeInfo> findByIngName(List<String> ingName) throws FindException{
		return idao.selectByIngName(ingName);
	}
	/**
	 * 추천 레시피 절차를 위한 메소드
	 * @return 추천 레시피 정보를 가진 RecipeInfo
	 * @throws FindException
	 * @author 최종국
	 */
	public RecipeInfo findRecommended() throws FindException {
		return dao.selectByRank();
	}
	public void addRecipe(String rdId, RecipeInfo recipeInfo, String ingInfo, List<Ingredient> ingList, String process) throws DuplicatedException {
		dao.insert(rdId, recipeInfo, ingInfo, ingList, process);
	}
	public void modifyRecipe(String rdId, RecipeInfo recipeInfo, String ingInfo, List<Ingredient> ingList, String process) throws ModifyException {
		dao.update(rdId, recipeInfo, ingInfo, ingList, process);
	}
	public void removeRecipe(String rdId, RecipeInfo recipeInfo) throws ModifyException {
		dao.remove(rdId, recipeInfo);
	}
	public List<RecipeInfo> findAll() throws FindException {
		return dao.selectAll();
	}
	/**
	 * 포인트 수정 절차를 위한 메소드
	 * @param p 수정할 레시피 코드와 좋아요, 싫어요 개수를 포함한 Point 객체
	 * @throws ModifyException
	 * @author 최종국
	 */
	public void modifyPoint(Point p) throws ModifyException {
		pointDAO.update(p);
	}

	
}
