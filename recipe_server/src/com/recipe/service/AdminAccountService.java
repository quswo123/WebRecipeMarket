package com.recipe.service;

import com.recipe.dao.AdminDAO;
import com.recipe.exception.FindException;
import com.recipe.share.AdminShare;
import com.recipe.vo.Admin;

public class AdminAccountService {
	AdminDAO adminDAO;
	public AdminAccountService() {
		adminDAO = new AdminDAO();
	}
	
	/**
	 * 로그인 절차를 수행하는 메소드
	 * 아이디와 패스워드를 가진 Admin이 존재하는지 여부를 확인하고, 존재하면 세션에 아이디를 저장한뒤 클라이언트에게 로그인 성공 메시지를 전송한다
	 * @param adminId 로그인 절차를 수행할 아이디
	 * @param adminPwd 로그인 절차를 수행할 패스워드
	 * @throws FindException
	 * @author 최종국
	 */
	public void login(String adminId, String adminPwd) throws FindException{
		Admin a;
		try {
			a = adminDAO.selectById(adminId);
		} catch(FindException e) {
			throw new FindException("로그인 실패");
		}
		if(!a.getAdminPwd().equals(adminPwd)) throw new FindException("로그인 실패");
		
		AdminShare.addSession(adminId);
	}
}
