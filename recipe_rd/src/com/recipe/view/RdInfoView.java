package com.recipe.view;

import java.io.IOException;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.RDShare;

public class RdInfoView {
	private DataIO dio;
	private Scanner sc;
	
	public RdInfoView(DataIO dio) {
		sc = new Scanner(System.in);
		this.dio = dio;
	}
	
	public void showRdInfoView() {
		int menu = -1;
		do {
			printRdInfo();
			System.out.println("0.뒤로가기");
			menu = Integer.parseInt(sc.nextLine());
		} while(menu != 0);
	}
	
	private void printRdInfo() {
		try {
			dio.sendMenu(Menu.RD_INFO);
			dio.sendId(RDShare.loginedId);
			
			if(dio.receiveStatus().equals("success")) {
				System.out.println(dio.receiveRd());
			} else {
				FailView fail = new FailView();
				fail.rdInfoView(dio.receive());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
