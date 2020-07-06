package com.recipe.view;

import java.io.IOException;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.vo.RD;

public class ModifyRdView {
	private Scanner sc;
	private DataIO dio;
	
	public ModifyRdView(DataIO dio) {
		sc = new Scanner(System.in);
		this.dio = dio;
	}
	
	public void showModifyRdView() {
		System.out.println("R&D 계정 수정하기");
		System.out.print("수정할 아이디 : "); String rdId = sc.nextLine();
		System.out.print("비밀번호 : "); String rdPwd = sc.nextLine();
		System.out.print("관리자 이름 : "); String rdManagerName = sc.nextLine();
		System.out.print("부서 이름 : "); String rdTeamName = sc.nextLine();
		System.out.print("전화번호 : "); String rdPhone = sc.nextLine();
		
		try {
			dio.sendMenu(Menu.RD_MODIFY);
			RD r = new RD(rdId, rdPwd, rdManagerName, rdTeamName, rdPhone);
			dio.send(r);
			
			if(dio.receiveStatus().equals("success")) {
				SuccessView success = new SuccessView();
				success.modifyRdView(rdId);
				System.out.println(r);
			} else {
				FailView fail = new FailView();
				fail.modifyRdView(dio.receive());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
