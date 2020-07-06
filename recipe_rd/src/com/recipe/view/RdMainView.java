package com.recipe.view;

import java.io.IOException;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.RDShare;

public class RdMainView {
	private Scanner sc;
	private DataIO dio;
	
	public RdMainView(DataIO dio) {
		sc = new Scanner(System.in);
		this.dio = dio;
	}
	
	public void mainMenu() {
		int menu = -1;
		try {
			do {
				System.out.println("1.레시피등록 2.레시피검색 3.레시피전체보기 4.추천레시피 5.내정보보기 6.로그아웃 7.프로그램 종료");
				menu = Integer.parseInt(sc.nextLine());
				switch (menu) {
				case 1:
					AddRecipeView addRecipeView = new AddRecipeView(dio);
					addRecipeView.AddRecipeFormView();
					break;
				case 2:
					RecipeSearchView recipeSearchView = new RecipeSearchView(dio);
					recipeSearchView.showRecipeInfoView();
					break;
				case 3:
					AllRecipeListView allRecipeListVew = new AllRecipeListView(dio);
					allRecipeListVew.showAllRecipeListView();
					break;
				case 4:
					RecommendedRecipeView recommendedRecipeView = new RecommendedRecipeView(dio);
					recommendedRecipeView.showRecommendedRecipeView();
					break;
				case 5:
					RdInfoView rdInfoView = new RdInfoView(dio);
					rdInfoView.showRdInfoView();
					break;
				case 6:
					menu = -1;
					logout();
					break;
				case 7:
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
		dio.sendMenu(Menu.RD_LOGOUT);
		dio.sendId(RDShare.loginedId);
		
		if(dio.receiveStatus().equals("success")) {
			RDShare.loginedId = "";
			SuccessView success = new SuccessView();
			success.logoutRdView();
		} else {
			FailView fail = new FailView();
			fail.logoutRdView();
		}
	}
}
