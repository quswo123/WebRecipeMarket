package com.recipe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.recipe.exception.FindException;
import com.recipe.jdbc.MyConnection;
import com.recipe.vo.Ingredient;
import com.recipe.vo.Point;
import com.recipe.vo.RecipeInfo;
import com.recipe.vo.RecipeIngredient;

public class RecipeIngredientDAO {
	public List<RecipeInfo> selectByIngName(List<String> ingName) throws FindException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<RecipeInfo> recipeInfo = new ArrayList<>();
		try {
			con = MyConnection.getConnection();

		} catch (ClassNotFoundException | SQLException e) {
			throw new FindException(e.getMessage());
		}
		//보내질 쿼리
		String newSQL =""; 
		//더해질 쿼리
		String selectByIngNameSQL ="SELECT RI.RECIPE_CODE, RIN.RECIPE_NAME, RIN.RECIPE_SUMM, RIN.RECIPE_PRICE, RI.ing_code, ING.ING_NAME, RIN.recipe_process, PT.LIKE_COUNT, PT.DISLIKE_COUNT\r\n" + 
				"FROM RECIPE_INGREDIENT RI \r\n" + 
				"LEFT JOIN RECIPE_INFO RIN ON RI.recipe_code = RIN.recipe_code\r\n" + 
				"JOIN INGREDIENT ING ON RI.ing_code = ING.ing_code\r\n" + 
				"LEFT JOIN POINT PT ON RIN.RECIPE_CODE = PT.RECIPE_CODE\r\n" +
				"WHERE RI.recipe_code IN \r\n" + 
				"(select ring.recipe_code FROM Ingredient ig JOIN recipe_ingredient ring on ig.ing_code = ring.ing_code Where ig.ing_name LIKE ?) AND RIN.RECIPE_Status = 1 INTERSECT  ";
		
		for (int i = 1; i < ingName.size(); i++ ) {
			newSQL += selectByIngNameSQL;	
			//리스트 사이즈만큼 돌면서 쿼리가 더해지고, 마지막줄은 INTERSECT 를 서브스트링해 더해짐
			if (i == ingName.size() -1) {
				newSQL += selectByIngNameSQL.substring(0, selectByIngNameSQL.length() -11);
			}			
		}
		if (ingName.size() == 1) {
			newSQL = selectByIngNameSQL.substring(0, selectByIngNameSQL.length() -11);
		}
		try {
			pstmt = con.prepareStatement(newSQL);
			//리스트 사이즈만큼 돌면서 값세팅해줌
			for (int i = 1; i < ingName.size()+1; i++) {
				pstmt.setString(i, "%" +ingName.get(i-1) + "%");
			}
			rs = pstmt.executeQuery();
			int prevCode = 0;
			List<RecipeIngredient> ingList = null;	
			while(rs.next()) {				
				int rCode = rs.getInt("recipe_code");
				//코드값이 같지 않을때 RecipeInfo 객체 생성해주고 값 넣어주기			
				if (prevCode != rCode) {					
					RecipeInfo recipeInfo2 = new RecipeInfo();
					ingList = new ArrayList<>();
					recipeInfo2.setIngredients(ingList);
					recipeInfo2.setRecipeCode(rCode);
					recipeInfo2.setRecipeName(rs.getString("recipe_name"));
					recipeInfo2.setRecipePrice(rs.getInt("recipe_price"));
					recipeInfo2.setRecipeSumm(rs.getString("recipe_summ"));
					recipeInfo2.setRecipeProcess(rs.getString("recipe_process"));
					recipeInfo2.setIngredients(ingList);					
					Point pt = new Point(rCode, rs.getInt("like_count"), rs.getInt("dislike_count"));
					recipeInfo2.setPoint(pt);
					recipeInfo.add(recipeInfo2);
					//prevCode에 새로운값 대입해주기
					prevCode = rCode;
				}
				//와일문 돌면서 값넣어주고 리스트에 애드해줌
				int ingCode = rs.getInt("ing_code");
				String name = rs.getString("ing_name");
				Ingredient ingredient = new Ingredient(ingCode, name);
				RecipeIngredient recipeIng = new RecipeIngredient(ingredient);				
				ingList.add(recipeIng);
				
			}
			if (recipeInfo.size() == 0) {
				throw new FindException("찾은 레시피가 없습니다");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			MyConnection.close(rs, pstmt, con);
		}		
		return recipeInfo;
	}
	public static void main(String[] args) {
		RecipeIngredientDAO dao = new RecipeIngredientDAO();
//		int code = 134;
//		try {
//			RecipeInfo list = dao.selectByCode(code);
//			System.out.println("code:" + list.getRecipeCode() + "  name:" + list.getRecipeName() + "  summ:"+ list.getRecipeSumm() +"  price:"+ list.getRecipePrice());
//			List<RecipeIngredient> lines = list.getIngredients();
//			Point pt = list.getPoint();
//			System.out.println("like" + pt.getLikeCount() + ":"+ "dislike" + pt.getDisLikeCount());
//			for(RecipeIngredient line : lines) {
//				System.out.print(line.getIngredient().getIngCode() + ":");
//				System.out.print(line.getIngredient().getIngName()+ ", ");
//			}
//		} catch (FindException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		List<String> ingrList = new ArrayList<>();
		ingrList.add("김치");
		ingrList.add("호박");	
		try {
			List<RecipeInfo> list2 = dao.selectByIngName(ingrList);
			for(RecipeInfo ri : list2) {
				System.out.println(ri.getRecipeCode() + ri.getRecipeName() + ri.getRecipePrice() + ri.getRecipeProcess() + ri.getRecipeSumm());
				List<RecipeIngredient> lines = ri.getIngredients();
				for(RecipeIngredient ing : lines) {
					System.out.println(ing.getIngredient().getIngName());
				}
			}
		} catch (FindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}

