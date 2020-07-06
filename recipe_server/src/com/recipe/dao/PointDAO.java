package com.recipe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.recipe.exception.ModifyException;
import com.recipe.jdbc.MyConnection;
import com.recipe.vo.Point;

public class PointDAO {
	
	/**
	 * UPDATE할 값을 가진 Point 객체를 전달받아 DB의 POINT 테이블을 수정한다
	 * @param p 수정된 값을 가진 Point 객체
	 * @throws ModifyException
	 * @author 최종국
	 */
	public void update(Point p) throws ModifyException {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new ModifyException(e.getMessage());
		}
		
		String updatePointSQL = "UPDATE point SET like_count = ?, dislike_count = ? WHERE recipe_code = ?";
		try {
			pstmt = con.prepareStatement(updatePointSQL);
			pstmt.setInt(1, p.getLikeCount());
			pstmt.setInt(2, p.getDisLikeCount());
			pstmt.setInt(3, p.getRecipeCode());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
