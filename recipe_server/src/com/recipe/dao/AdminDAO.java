package com.recipe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.recipe.exception.FindException;
import com.recipe.jdbc.MyConnection;
import com.recipe.vo.Admin;

public class AdminDAO {
	public Admin selectById(String adminId) throws FindException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		String selectByIdSQL = "SELECT * FROM admin WHERE admin_id = ?";
		try {
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setString(1, adminId);
			
			rs = pstmt.executeQuery();
			if(rs.next()) return new Admin(rs.getString("admin_id"), rs.getString("admin_pwd"));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(rs, pstmt, con);
		}

		throw new FindException("아이디가 존재하지 않습니다");
	}
}
