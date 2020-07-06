package com.recipe.view;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.vo.RD;

public class AllRdListView {
	private Scanner sc;
	private DataIO dio;
	
	public AllRdListView(DataIO dio) {
		sc = new Scanner(System.in);
		this.dio = dio;
	}
	
	public void showAllRdListView() {
		try {
			dio.sendMenu(Menu.RD_ALL);
			
			if(dio.receiveStatus().equals("success")) {
				List<RD> result = dio.receiveRDList();
				printRdList(result);
			} else {
				FailView fail = new FailView();
				fail.allRdView(dio.receive());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printRdList(List<RD> list) {
		String menu;
		int size = list.size();
		int start_index = 0; //화면에 다섯개씩 보여줄때 사용할 시작 인덱스
		int end_index = size <= 5 ? size : 5;//화면에 다섯개씩 보여줄때 사용할 끝 인덱스
											//ListView를 최초로 구성할때, list의 size가 5 이하이면 size만큼 화면에 출력하고, 5를 초과하면 5만큼만 화면에 출력

		do {			
			for(int i = start_index; i < end_index; i++) {
				System.out.println((i+1) + ". 아이디 : " + list.get(i).getRdId() + ", 비밀번호 : " + list.get(i).getRdPwd());
			}
			if(size < 5) {
				System.out.println("0.뒤로가기");
				menu = sc.nextLine();
			}
			else {
				System.out.println("-:이전페이지 +:다음페이지 0.뒤로가기");
				menu = sc.nextLine();
				if(menu.equals("-")) {
					start_index = (start_index - 5) >= 0 ? (start_index - 5) : 0; //이전 페이지를 누르면 시작 인덱스 값을 5 감소시킨다. 이떄, 0보다 작아지면 0으로 설정한다
					end_index = start_index + 5; //시작 인덱스부터 다섯개를 출력하기 위해 끝 인덱스는 시작 인덱스에서 5 증가한 값을 갖는다
				} else if(menu.equals("+")) {
					end_index = (end_index + 5) <= size ? (end_index + 5) : size; //다음 페이지를 누르면 end_index 값을 5 증가시킨다. 이때, list의 size보다 커지면 size와 같은 값으로 설정한다
					start_index = (end_index % 5) == 0 ? end_index - 5 : end_index-(end_index%5); //시작 인덱스부터 다섯개를 출력하기 위해 시작 인덱스는 끝 인덱스에서 5 감소한 값을 갖는다
				}
			}
		} while (!menu.equals("0"));
	}
}
