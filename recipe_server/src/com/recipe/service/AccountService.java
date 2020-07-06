package com.recipe.service;

import com.recipe.dao.CustomerDAO;
import com.recipe.exception.AddException;
import com.recipe.exception.DuplicatedException;
import com.recipe.exception.FindException;
import com.recipe.exception.ModifyException;
import com.recipe.exception.RemoveException;
import com.recipe.share.CustomerShare;
import com.recipe.vo.Customer;

public class AccountService {
	CustomerDAO customerDAO;

	public AccountService() {
		customerDAO = new CustomerDAO();
	}

	/**
	 * 로그인 절차를 수행하는 메소드
	 * 아이디와 패스워드를 가진 Customer가 존재하는지 여부를 확인하고, 존재하면 세션에 아이디를 저장한뒤 클라이언트에게 로그인 성공 메시지를 전송한다
	 * @param customerId 로그인 절차를 수행할 아이디
	 * @param customerPwd 로그인 절차를 수행할 패스워드
	 * @throws FindException
	 * @author 최종국
	 */
	public void login(String customerId, String customerPwd) throws FindException {
		Customer c;
		try {
			c = customerDAO.selectById(customerId);
		} catch (FindException e) {
			throw new FindException("로그인 실패");
		}
		if (!c.getCustomerPwd().equals(customerPwd))
			throw new FindException("로그인 실패");

		CustomerShare.addSession(customerId);
	}

	/*
	 * Customer 회원가입 호출
	 * @영민
	 */
	public void add(Customer c) throws AddException, DuplicatedException {
		customerDAO.insert(c);

	}

	/*
	 * Customer 내 정보 보기 호출
	 * @author 영민
	 */
	public Customer findById(String id) throws FindException {
		return customerDAO.selectById(id);
	}

	/*
	 * Customer 내 정보 수정 호출
	 * @author 영민
	 */
	public void modify(Customer c) throws ModifyException {
		customerDAO.update(c);
	}

	/*
	 * Customer 회원 탈퇴 호출
	 * @author영민
	 */
	public void remove(Customer c) throws RemoveException {
		customerDAO.update1(c);
	}
}
