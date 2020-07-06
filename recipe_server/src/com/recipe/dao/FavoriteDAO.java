package com.recipe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.recipe.exception.AddException;
import com.recipe.exception.DuplicatedException;
import com.recipe.exception.FindException;
import com.recipe.exception.RemoveException;
import com.recipe.jdbc.MyConnection;
import com.recipe.vo.Favorite;
import com.recipe.vo.Point;
import com.recipe.vo.RecipeInfo;

/**
 * @author Soojeong
 */
public class FavoriteDAO {

	public static void main(String[] args) {
		/*테스트 할 기능 주석해제 하세요*/
		test_favorite_selectById();
		//test_favorite_insert();
		//test_favorite_deleteByIdnCode();
		
	} //end method main();

	/**
	 * 즐겨찾기 추가 : insert()
	 * @param Favorite f
	 */
	public void insert(Favorite f) throws AddException, DuplicatedException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = MyConnection.getConnection();
			
		} catch (ClassNotFoundException | SQLException e) {
			e.getStackTrace();
		}
		
		String insertSQL = "INSERT INTO FAVORITE"
				+ "(customer_id , recipe_code) "
				+ "VALUES ( ? , ? )";


 		String customerId = f.getCustomerId();
		int recipeCode = f.getRecipeInfo().getRecipeCode();
	
		try {
			pstmt = con.prepareStatement(insertSQL);
			pstmt.setString(1, customerId);
			pstmt.setInt(2, recipeCode);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			if ( e.getErrorCode() == 1 ) { // SQLException errorCode == 1 )  
				throw new DuplicatedException("Fail : 이미 즐겨찾기 추가되어 있는 레시피 입니다.");
			} else { 
				throw new AddException("Fail : 즐겨찾기 추가 실패했습니다.");
			}
			
		} finally {
			MyConnection.close(pstmt, con);
		} 
		
	}//end method insert();
	
	
	/**
	 * 즐겨찾기 목록 전체보기 : selectById()
	 * @param String customerId
	 */
	public List<Favorite> selectById(String customerId) throws FindException {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Favorite> favoriteList = null;
		
		String selectSQL = "SELECT info.recipe_code"
				+ ", recipe_name"
				+ ", recipe_summ"
				+ ", recipe_price"
				+ ", recipe_process"
				+ ", recipe_status"
				+ ", rd_Id"
				+ ", like_count"
				+ ", dislike_count"
				+ ", f.customer_id"
				+" FROM RECIPE_INFO info "  
				+" JOIN FAVORITE f  ON info.recipe_code = f.recipe_code "
				+" LEFT JOIN POINT p  ON info.recipe_code = p.recipe_code "
				+" WHERE CUSTOMER_ID = ? AND RECIPE_STATUS = '1'"
				+" ORDER BY RECIPE_CODE";
		
		try {
			con = MyConnection.getConnection();
			
		} catch (ClassNotFoundException | SQLException e ) {
			e.printStackTrace();
		}
		
		try {
			pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, customerId);
			rs = pstmt.executeQuery();
			favoriteList = new ArrayList<>();
			
			while ( rs.next() ) {
				RecipeInfo info = new RecipeInfo();
				info.setRecipeCode(rs.getInt("recipe_code"));
				info.setRecipeName(rs.getString("recipe_name"));
				info.setRecipePrice(rs.getInt("recipe_price"));
				info.setRecipeProcess(rs.getString("recipe_process"));
				info.setRecipeSumm(rs.getString("recipe_summ"));
				info.setIngredients(null);
				
				Point p = new Point();
				p.setRecipeCode(rs.getInt("recipe_code"));
				p.setLikeCount(rs.getInt("like_count"));
				p.setDisLikeCount(rs.getInt("dislike_count"));
				info.setPoint(p);
				
				Favorite f = new Favorite();
				f.setRecipeInfo(info);
				f.setCustomerId(customerId);
				
				favoriteList.add(f);
			} //end while
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Fail : 즐겨찾기 목록 조회에 실패하셨습니다.");
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
		return favoriteList;
	} // end method selectById(); 
	
	
	/**
	 * 즐겨찾기 삭제 : deleteByIdnCode()
	 * @param Favorite f
	 */
	public void deleteByIdnCode(Favorite f) throws RemoveException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = MyConnection.getConnection();
			
		} catch (ClassNotFoundException | SQLException e) {
			e.getStackTrace();
		}
		
		String deleteSQL = "DELETE FROM FAVORITE "
						 + "WHERE CUSTOMER_ID = ? "
						 + "AND RECIPE_CODE = ?";
		
 		String customerId = f.getCustomerId();
		if (customerId == null) {
			System.out.println("customerID null!!!!");
		}
		
		int recipeCode = f.getRecipeInfo().getRecipeCode();
		
		try {
			pstmt = con.prepareStatement(deleteSQL);
			pstmt.setString(1, customerId);
			pstmt.setInt(2, recipeCode);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Fail : 즐겨찾기 삭제 실패하셨습니다.");
			
		} finally {
			MyConnection.close(pstmt, con);
		} 
		
	} //end method deleteByIdnCode();
	
	
	// test_favorite_selectById
	private static void test_favorite_selectById() {
		FavoriteDAO dao = new FavoriteDAO();
		String customerId = "tester";
		
		List <Favorite> fList = new ArrayList<>();
		
		try {
			fList = dao.selectById(customerId);
			
			if ( fList.size() == 0 ) {
				System.out.println("즐겨찾기목록 조회 결과 : 즐겨찾기 목록이 없습니다.");
			} 
			
			for ( Favorite f : fList ) {
				System.out.println("customer_ID : " + f.getCustomerId());
				System.out.println("recipe_code : " + f.getRecipeInfo().getRecipeCode());
				System.out.println("recipe_name : " + f.getRecipeInfo().getRecipeName());
				System.out.println("recipe_price: " + f.getRecipeInfo().getRecipePrice());
				System.out.println("recipe_process : " + f.getRecipeInfo().getRecipeProcess());
				System.out.println("recipe_summ : " + f.getRecipeInfo().getRecipeSumm());
			}
		} catch ( FindException e ) {
			System.out.println(e.getMessage());
		}
	}  //end test method 
	
	// test_favorite_insert 
	private static void test_favorite_insert() {
		FavoriteDAO dao = new FavoriteDAO();
		String customerId = "tester";
		int recipeCode = 101;
		RecipeInfo info = new RecipeInfo();
		info.setRecipeCode(recipeCode);
		
		Favorite f = new Favorite(customerId, info);
		try {
			dao.insert(f);
			System.out.println("Success : 즐겨찾기 목록에서 추가되었습니다.");
		} catch (DuplicatedException e) {
			System.out.println(e.getMessage());
		} catch (AddException e) { 
			System.out.println(e.getMessage());
		}
	} //end test method 
	
	// test_favorite_deleteByIdnCode
	private static void test_favorite_deleteByIdnCode() {
		FavoriteDAO dao = new FavoriteDAO();
		String customerId = "tester";
		int recipeCode = 399;
		
		Favorite f = new Favorite();
		f.setCustomerId(customerId);
		RecipeInfo info = new RecipeInfo();
		info.setRecipeCode(recipeCode);
		f.setRecipeInfo(info);
		try {
			dao.deleteByIdnCode(f);
			System.out.println("Success : 즐겨찾기 목록에서 삭제되었습니다.");

		} catch ( RemoveException e ) {
			System.out.println(e.getMessage());
		}
	} //end test method 
} //end class FavoriteDAO
