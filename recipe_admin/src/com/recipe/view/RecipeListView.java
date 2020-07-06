package com.recipe.view;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.vo.RD;
import com.recipe.vo.RecipeInfo;

public class RecipeListView {
	private Scanner sc;		
	private DataIO dio;	
	public RecipeListView(DataIO dio) {
		this.dio = dio;
		this.sc = new Scanner(System.in);
	}

	public void showAllRecipeListView(List<RecipeInfo> recipeInfo) {
		printRecipeList(recipeInfo);
	}

	private void printRecipeList(List<RecipeInfo> list) {
		String menu;
		int size = list.size();
		int start_index = 0; //화면에 다섯개씩 보여줄때 사용할 시작 인덱스
		int end_index = size <= 5 ? size : 5;//화면에 다섯개씩 보여줄때 사용할 끝 인덱스
											//ListView를 최초로 구성할때, list의 size가 5 이하이면 size만큼 화면에 출력하고,5를 초과하면 5만큼만 화면에 출력
		System.out.println("[" + size + "건이 조회되었습니다]");
		do {			
			for(int i = start_index; i < end_index; i++) {
				System.out.println(i+1 + ". " + list.get(i).getRecipeName());
			}
			if(size < 5) {
				System.out.println("0.뒤로가기");
				System.out.print("상세레시피를 보기 원하시면 번호를 입력하세요:");
				menu = sc.nextLine();
				if (!menu.equals("0")) {
					int n = Integer.parseInt(menu);
					System.out.println(menu);
					RecipeInfo param = list.get(n-1);			
					RecipeInfoView infoView = new RecipeInfoView(dio);
					infoView.showRecipeInfoView(param);
				}
			}
			else {
				System.out.println("-:이전페이지 +:다음페이지 0.뒤로가기");
				System.out.print("상세레시피를 보기 원하시면 번호를 입력하세요:");
				menu = sc.nextLine();
				if(menu.equals("-")) {
					start_index = (start_index - 5) >= 0 ? (start_index - 5) : 0; //이전 페이지를 누르면 시작 인덱스 값을 5 감소시킨다. 이떄, 0보다 작아지면 0으로 설정한다
					end_index = start_index + 5; //시작 인덱스부터 다섯개를 출력하기 위해 끝 인덱스는 시작 인덱스에서 5 증가한 값을 갖는다
				} else if(menu.equals("+")) {
					end_index = (end_index + 5) <= size ? (end_index + 5) : size; //다음 페이지를 누르면 end_index 값을 5 증가시킨다. 이때, list의 size보다 커지면 size와 같은 값으로 설정한다
					start_index = (end_index % 5) == 0 ? end_index - 5 : end_index-(end_index%5); //시작 인덱스부터 다섯개를 출력하기 위해 시작 인덱스는 끝 인덱스에서 5 감소한 값을 갖는다
				}  else if (!menu.equals("0")){
					int n = Integer.parseInt(menu);
					System.out.println(menu);
					RecipeInfo param = list.get(n-1);			
					RecipeInfoView infoView = new RecipeInfoView(dio);
					infoView.showRecipeInfoView(param);
				}
			}
		} while (!menu.equals("0"));
	}
}
