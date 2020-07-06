package com.recipe.view;

import java.io.IOException;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.vo.RD;

public class AddRdView {
	private Scanner sc;
	private DataIO dio;
	
	public AddRdView(DataIO dio) {
		sc = new Scanner(System.in);
		this.dio = dio;
	}
	
	public void showAddRdView() {
		System.out.println("R&D 계정 추가하기");
		System.out.print("아이디 : "); String rdId = sc.nextLine();
		System.out.print("비밀번호 : "); String rdPwd = sc.nextLine();
		System.out.print("관리자 이름 : "); String rdManagerName = sc.nextLine();
		System.out.print("부서 이름 : "); String rdTeamName = sc.nextLine();
		System.out.print("전화번호 : "); String rdPhone = sc.nextLine();
		
		try {
			dio.sendMenu(Menu.RD_ADD);
			dio.send(new RD(rdId, rdPwd, rdManagerName, rdTeamName, rdPhone));
			
			if(dio.receiveStatus().equals("success")) {
				SuccessView success = new SuccessView();
				success.addRdView(rdId);
			} else {
				FailView fail = new FailView();
				fail.addRdView(dio.receive());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
