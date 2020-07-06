package com.recipe.view;

import java.io.IOException;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.RDShare;

public class LoginView {
	private String id;
	private String pwd;
	private DataIO dio;
	
	public LoginView(DataIO dio) {
		this.dio = dio;
	}
	
	/**
	 * 로그인에 필요한 아이디와 패스워드를 입력받는다
	 */
	public void showLoginView() {
		Scanner sc = new Scanner(System.in);
		System.out.print("1.아이디를 입력하세요 : "); id = sc.nextLine();
		System.out.print("2.비밀번호를 입력하세요 : "); pwd = sc.nextLine();
		
		login(id, pwd);
	}
	
	/**
	 * 사용자에게 입력받은 아이디와 패스워드를 서버로 전송하고, 로그인 결과를 전달받는 로그인 절차를 수행한다
	 * @param id 사용자가 입력한 아이디
	 * @param pwd 사용자가 입력한 패스워드
	 */
	public void login(String id, String pwd) {
		try {
			//로그인 절차 수행을 위한 로그인 번호와 아이디, 패스워드를 전송
			dio.sendMenu(Menu.RD_LOGIN);
			dio.sendId(id);
			dio.sendPwd(pwd);
			
			//로그인 성공 여부를 전달받는다
			if(dio.receiveStatus().equals("success")) { //로그인에 성공했다면
				SuccessView success = new SuccessView();
				success.loginRdView(id); //로그인 성공화면 출력
				RDShare.loginedId = id;
				
				RdMainView rdMainView = new RdMainView(dio);
				rdMainView.mainMenu();
			} else { //로그인에 실패했다면
				String msg = dio.receive();
				FailView fail = new FailView();
				fail.loginRdView(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
