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
import com.recipe.exception.ModifyException;
import com.recipe.exception.RemoveException;
import com.recipe.jdbc.MyConnection;
import com.recipe.vo.RD;

public class RDDAO {
	/**
	 * 새로 추가할 R&D 계정의 정보를 가진 RD 객체를 이용하여 rd 테이블에 새로운 R&D 정보를 추가한다
	 * @param r 추가할 R&D 계정의 정보를 가진 RD
	 * @throws AddException
	 * @throws DuplicatedException 아이디가 이미 존재하는 경우
	 */
	public void insert(RD r) throws AddException, DuplicatedException {
		try {
			selectById(r.getRdId());
			throw new DuplicatedException("이미 해당 아이디가 존재합니다");
		} catch (FindException e) {
			
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		String insertSQL = "INSERT INTO rd (rd_id, rd_pwd, rd_manager_name, rd_team_name, rd_phone, rd_status) VALUES (?, ?, ?, ?, ?, '1')";
		try {
			pstmt = con.prepareStatement(insertSQL);
			pstmt.setString(1, r.getRdId());
			pstmt.setString(2, r.getRdPwd());
			pstmt.setString(3, r.getRdManagerName());
			pstmt.setString(4, r.getRdTeamName());
			pstmt.setString(5, r.getRdPhone());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			if(e.getErrorCode() == 1)
				throw new AddException("해당 아이디로 계정을 생성할 수 없습니다");
		} finally {
			MyConnection.close(pstmt, con);
		}
	}
	/**
	 * 모든 R&D계정을 조회한다
	 * @return 모든 R&D계정의 정보를 가진 RD객체의 리스트
	 * @throws FindException R&D계정이 하나도 존재하지 않는 경우
	 * @author 최종국
	 */
	public List<RD> selectAll() throws FindException{
		List<RD> result = new ArrayList<RD>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		String selectAllSQL = "SELECT rd_id, rd_pwd, rd_manager_name, rd_team_name, rd_phone FROM rd WHERE rd_status = '1'";
		try {
			pstmt = con.prepareStatement(selectAllSQL);
			rs = pstmt.executeQuery();
			
			while(rs.next()) result.add(new RD(rs.getString("rd_id"), rs.getString("rd_pwd"), rs.getString("rd_manager_name"), rs.getString("rd_team_name"), rs.getString("rd_phone")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(result.isEmpty()) throw new FindException("RD 계정이 하나도 없습니다");
		return result;
	}
	
	/**
	 * 전달받은 아이디를 포함한 R&D 계정의 정보를 가진 RD객체를 반환한다
	 * @param rdId 검색할 아이디
	 * @return R&D계정의 정보를 가진 RD 객체
	 * @throws FindException 매개변수로 전달받은 아이디를 포함한 R&D계정이 존재하지 않는 경우
	 * @author 최종국
	 */
	public RD selectById(String rdId) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		String selectByIdSQL = "SELECT rd_pwd, rd_manager_name, rd_team_name, rd_phone FROM rd WHERE rd_id = ? and rd_status = '1'";
		try {
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setString(1, rdId);
			rs = pstmt.executeQuery();
			if(rs.next()) return new RD(rdId, rs.getString("rd_pwd"), rs.getString("rd_manager_name"), rs.getString("rd_team_name"), rs.getString("rd_phone"));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
		throw new FindException("아이디가 존재하지 않습니다");
	}
	
	/**
	 * 수정할 R&D 계정의 정보를 가진 RD 객체를 통해 DB의 rd 테이블의 정보를 수정한다
	 * @param r 수정할 R&D 계정의 정보를 가진 RD
	 * @throws ModifyException 수정하려는 R&D 계정이 존재하지 않는 경우
	 * @author 최종국
	 */
	public void update(RD r) throws ModifyException {
		try {
			selectById(r.getRdId());
		} catch (FindException e) {
			throw new ModifyException(e.getMessage());
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		String insertSQL = "UPDATE rd SET rd_pwd = ?, rd_manager_name = ?, rd_team_name = ?, rd_phone = ? WHERE rd_id = ?";
		try {
			pstmt = con.prepareStatement(insertSQL);
			pstmt.setString(1, r.getRdPwd());
			pstmt.setString(2, r.getRdManagerName());
			pstmt.setString(3, r.getRdTeamName());
			pstmt.setString(4, r.getRdPhone());
			pstmt.setString(5, r.getRdId());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(pstmt, con);
		}
	}
	
	/**
	 * 비활성화할 아이디를 전달받아 테이블의 status 값을 0으로 바꾸어 해당 R&D 계정을 비활성화 상태로 전환한다
	 * @param rdId 비활성화할 R&D 계정 아이디
	 * @throws RemoveException 비활성화하려는 R&D 계정이 존재하지 않는 경우
	 */
	public void disableRD(String rdId) throws RemoveException {
		try {
			selectById(rdId);
		} catch (FindException e) {
			throw new RemoveException(e.getMessage());
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		String insertSQL = "UPDATE rd SET rd_status = '0' WHERE rd_id = ?";
		try {
			pstmt = con.prepareStatement(insertSQL);
			pstmt.setString(1, rdId);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(pstmt, con);
		}
	}
}
