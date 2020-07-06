package com.recipe.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.RDShare;
import com.recipe.vo.Ingredient;
import com.recipe.vo.RecipeInfo;

public class AllRecipeListView {
	private Scanner sc;
	private DataIO dio;

	public AllRecipeListView(DataIO dio) {
		this.dio = dio;
		sc = new Scanner(System.in);
	}
	public void showAllRecipeListView() {
		String menu;
		try {
			dio.sendMenu(Menu.RECIPE_ALL);
			List<RecipeInfo> list = dio.receiveRecipeInfos();

			if(dio.receiveStatus().equals("success")) {
				int size = list.size();
				int start_index = 0; //화면에 다섯개씩 보여줄때 사용할 시작 인덱스
				int end_index = size <= 5 ? size : 5;//화면에 다섯개씩 보여줄때 사용할 끝 인덱스
				//ListView를 최초로 구성할때, list의 size가 5 이하이면 size만큼 화면에 출력하고, 5를 초과하면 5만큼만 화면에 출력
				System.out.println("[" + size + "개의 건이 조회되었습니다]");
				do {			
					for(int i = start_index; i < end_index; i++) {
						System.out.println(list.get(i).getRecipeCode() + " / " + list.get(i).getRecipeName());
					}
					if(size < 5) {
						System.out.println("0.뒤로가기");
						menu = sc.nextLine();
						if (!menu.equals("0")) {
							RecipeInfo param = findByCode(list, Integer.parseInt(menu));
							RecipeInfoView infoView = new RecipeInfoView(dio);
							infoView.showRecipeInfoView(param);
						}
					}
					else {
						System.out.println("-:이전페이지 +:다음페이지 0.뒤로가기");
						System.out.println("상세레시피를 보기원하시면 코드번호를 입력하세요: ");
						menu = sc.nextLine();
						if(menu.equals("-")) {
							start_index = (start_index - 5) >= 0 ? (start_index - 5) : 0; //이전 페이지를 누르면 시작 인덱스 값을 5 감소시킨다. 이떄, 0보다 작아지면 0으로 설정한다
							end_index = start_index + 5; //시작 인덱스부터 다섯개를 출력하기 위해 끝 인덱스는 시작 인덱스에서 5 증가한 값을 갖는다
						} else if(menu.equals("+")) {
							end_index = (end_index + 5) <= size ? (end_index + 5) : size; //다음 페이지를 누르면 end_index 값을 5 증가시킨다. 이때, list의 size보다 커지면 size와 같은 값으로 설정한다
							start_index = end_index - 5; //시작 인덱스부터 다섯개를 출력하기 위해 시작 인덱스는 끝 인덱스에서 5 감소한 값을 갖는다
						} else if(menu.equals("0")) {

						} else {
							RecipeInfo param = findByCode(list, Integer.parseInt(menu));
							RecipeInfoView infoView = new RecipeInfoView(dio);
							infoView.showRecipeInfoView(param);
						}
					}
				} while (!menu.equals("0"));
			}else {
				FailView failView = new FailView();
				failView.viewAllRecipeView(dio.receive());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private RecipeInfo findByCode(List<RecipeInfo> list ,int code ) {
		for(RecipeInfo r : list) {
			if(r.getRecipeCode() == code) {
				return r;
			}
		}
		return null;
	}
}