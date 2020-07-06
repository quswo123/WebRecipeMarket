package com.recipe.view;

import java.io.IOException;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.CustomerShare;

public class CustomerMainView {
	private Scanner sc;
	private DataIO dio;

	public CustomerMainView(DataIO dio) {
		sc = new Scanner(System.in);
		this.dio = dio;
	}

	public void mainMenu() {
		int menu = -1;
		try {
			do {
				System.out.println("1.레시피검색 2.추천레시피 3.구매내역 4.즐겨찾기보기 5.나의후기목록 6.내 정보보기 7.로그아웃 8.프로그램 종료");
				menu = Integer.parseInt(sc.nextLine());
				switch (menu) {
				case 1:
					RecipeSearchView searchView = new RecipeSearchView(dio);
					searchView.showRecipeInfoView();
					break;
				case 2:
					RecommendedRecipeView recommendedView = new RecommendedRecipeView(dio);
					recommendedView.showRecommendedRecipeView();
					break;
				case 3:
					PurchaseListVIew purchaseListView = new PurchaseListVIew(dio);
					purchaseListView.purchaseView();
					break;
				case 4:
					FavoriteListView favoriteListView = new FavoriteListView(dio);
					favoriteListView.showFavoriteListView(CustomerShare.loginedId);
					break;

				case 5:
					MyReviewListView myReviewListView = new MyReviewListView(dio);
		             myReviewListView.showMyReviewListView(CustomerShare.loginedId);
					break;

				case 6:
					CustomerInfoView customerInfoView = new CustomerInfoView(dio);
					customerInfoView.customerInfoMenu();
					break;
				case 7:
					menu = -1;
					logout();
					break;
				case 8:
					System.exit(0);
					break;
				}
			} while (menu != -1 && !CustomerShare.loginedId.equals(""));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 현재 로그인중인 아이디를 서버로 전송하고 로그아웃 절차를 수행한다
	 * 
	 * @throws IOException
	 */
	private void logout() throws IOException {
		dio.sendMenu(Menu.CUSTOMER_LOGOUT);
		dio.sendId(CustomerShare.loginedId);

		if (dio.receiveStatus().equals("success")) {
			CustomerShare.loginedId = "";
			SuccessView success = new SuccessView();
			success.logoutCustomerView();
		} else {
			FailView fail = new FailView();
			fail.logoutCustomerView();
		}
	}
}
