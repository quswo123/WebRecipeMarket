package com.recipe.view;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.CustomerShare;

public class DeleteCustomerInfoView {
	private DataIO dataio;
	private Scanner sc;

	/*
	 * IO 연결
	 * 
	 * @author 영민
	 */
	public DeleteCustomerInfoView(DataIO dio) throws UnknownHostException, IOException {
		sc = new Scanner(System.in);
		dataio = dio;

	}

	public void deleteMyAccount() {
		try {
			// 회원탈퇴를 요청
			dataio.sendMenu(Menu.CUSTOMER_REMOVE);
			// Customer 정보를 송신
			dataio.sendId(CustomerShare.loginedId);
			if ("success".equals(dataio.receiveStatus())) {
				SuccessView success = new SuccessView();
				success.deleteCustomerView();
				logout();
			} else {
				String failMsg = dataio.receive();
				System.out.println(failMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 현재 로그인중인 아이디를 서버로 전송하고 로그아웃 절차를 수행한다
	 * 
	 * @throws IOException
	 */
	private void logout() throws IOException {
		dataio.sendMenu(Menu.CUSTOMER_LOGOUT);
		dataio.sendId(CustomerShare.loginedId);

		if (dataio.receiveStatus().equals("success")) {
			CustomerShare.loginedId = "";
			SuccessView success = new SuccessView();
			success.logoutCustomerView();
		} else {
			FailView fail = new FailView();
			fail.logoutCustomerView();
		}
	}
}
