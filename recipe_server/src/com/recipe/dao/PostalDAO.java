/*
 * 수업 시간에 배운 PostalDAO 그대로 사용했습니다
 * @author 영민
 */

package com.recipe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.recipe.exception.FindException;
import com.recipe.jdbc.MyConnection;
import com.recipe.vo.Postal;

public class PostalDAO {
	public List<Postal> selectByDoro(String doro) throws FindException{
		List<Postal> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		
		String selectByDoroSQL = 
				"SELECT zipcode\r\n" + 
				"      ,buildingno\r\n" + 
				"      ,sido ||' ' || NVL(sigungu, ' ') ||' ' || NVL(eupmyun, ' ')  city    \r\n" + 
				"      ,doro || ' ' || DECODE(building2, '0' , building1, building1 ||'-' || building2) doro\r\n" + 
				"      ,building      \r\n" + 
				"FROM postal\r\n" + 
				"WHERE building LIKE ? \r\n" + 
				"OR DORO ||' '|| building1 || building2 LIKE ?";
		try {
			pstmt = con.prepareStatement(selectByDoroSQL);
			pstmt.setString(1, "%" +doro+ "%");
			pstmt.setString(2, "%" +doro+ "%");
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String zipcode = rs.getString("zipcode");
				String buildingno = rs.getString("buildingno");
				String city = rs.getString("city");
				String doro1 = rs.getString("doro");
				String building = rs.getString("building");
				Postal p = new Postal(buildingno, zipcode, city, doro1, building);
				list.add(p);
			}
			if(list.size() == 0) {
				throw new FindException("해당 도로명주소는 없습니다");				
			}			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
		return list;		
	}
	public static void main(String[] args) {
		PostalDAO dao = new PostalDAO();
		String doro = "홍익길";
		try {
			List<Postal>list = dao.selectByDoro(doro);
			for(Postal p: list) {
				System.out.println(p.getZipcode() 
						+ ":" + p.getCity() 
						+ ":" + p.getDoro()
						+ ":" + p.getBuilding());
			}
		} catch (FindException e) {
			e.printStackTrace();
		}
	}
	
	
}

