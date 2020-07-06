package com.recipe.view;

import java.io.IOException;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.AdminShare;


public class AdminMainView {
	private Scanner sc;
	private DataIO dio;
	
	public AdminMainView(DataIO dio) {
		sc = new Scanner(System.in);
		this.dio = dio;
	}
	
	public void mainMenu() {
		int menu = -1;
		try {
			do {
				System.out.println("1.R&D계정추가 2.R&D계정수정 3.R&D계정삭제 4.R&D계정조회 5.로그아웃 6.프로그램 종료");
				menu = Integer.parseInt(sc.nextLine());
				switch (menu) {
				case 1:
					AddRdView addRdView = new AddRdView(dio);
					addRdView.showAddRdView();
					break;
				case 2:
					ModifyRdView modifyRdView = new ModifyRdView(dio);
					modifyRdView.showModifyRdView();
					break;
				case 3:
					RemoveRdView removeRdView = new RemoveRdView(dio);
					removeRdView.showRemoveRdView();
					break;
				case 4:
					AllRdListView allRdListView = new AllRdListView(dio);
					allRdListView.showAllRdListView();
					break;
				case 5:
					menu = -1;
					logout();
					break;
				case 6:
					System.exit(0);
					break;
				}
			} while (menu != -1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 현재 로그인중인 아이디를 서버로 전송하고 로그아웃 절차를 수행한다
	 * @throws IOException
	 */
	public void logout() throws IOException {
		dio.sendMenu(Menu.ADMIN_LOGOUT);
		dio.sendId(AdminShare.loginedId);
		
		if(dio.receiveStatus().equals("success")) {
			AdminShare.loginedId = "";
			SuccessView success = new SuccessView();
			success.logoutAdminView();
		} else {
			FailView fail = new FailView();
			fail.logoutAdminView();
		}
	}
}
