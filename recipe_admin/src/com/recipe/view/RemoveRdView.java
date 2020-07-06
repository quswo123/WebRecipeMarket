package com.recipe.view;

import java.io.IOException;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;

public class RemoveRdView {
	private Scanner sc;
	private DataIO dio;
	
	public RemoveRdView(DataIO dio) {
		sc = new Scanner(System.in);
		this.dio = dio;
	}
	
	public void showRemoveRdView() {
		System.out.print("삭제할 아이디를 입력해주세요 : "); String rdId = sc.nextLine();
		
		try {
			dio.sendMenu(Menu.RD_REMOVE);
			dio.sendId(rdId);
			
			if(dio.receiveStatus().equals("success")) {
				SuccessView success = new SuccessView();
				success.removeRdView(rdId);
			} else {
				FailView fail = new FailView();
				fail.removeRdView(dio.receive());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
