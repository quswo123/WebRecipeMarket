package com.recipe.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.RDShare;
import com.recipe.vo.RecipeInfo;

public class RecipeSearchView {
	private DataIO dio;

	public RecipeSearchView(DataIO dio) {
		this.dio = dio;
	}

	public void showRecipeInfoView() {
		Scanner sc = new Scanner(System.in);
		if (RDShare.loginedId.equals("")) {
			System.out.println("레시피 검색");
			System.out.println("1. 재료로 레시피 검색");
			System.out.println("2. 이름으로 레시피 검색");
			int num = Integer.parseInt(sc.nextLine());
			if (num == 1) {
				showFindbyIngNameView();
			} else if (num == 2) {
				showFindbyName();
			} else {
				showRecipeInfoView();
			}
		} else {
			System.out.println("레시피 검색");
			System.out.println("1. 재료로 레시피 검색");
			System.out.println("2. 이름으로 레시피 검색");
			System.out.println("3. 코드로 레시피 검색");
			int num = Integer.parseInt(sc.nextLine());
			if (num == 1) {
				showFindbyIngNameView();
			} else if (num == 2) {
				showFindbyName();
			} else if (num == 3) {
				showFindbyCodeView();
			} else {
				showRecipeInfoView();
			}
		}
		
	}

	private void showFindbyIngNameView() {
		Scanner sc = new Scanner(System.in);
		List<String> list = new ArrayList<>();
		System.out.println("재료로 검색");
		System.out.print("재료를 입력하세요(하나 이상의 재료는  " + "," + " 로 분리해주세요):");

		String[] s = sc.nextLine().split(",");
		for (String c : s) {
			list.add(c);
		}

		findByIngName(list);

	}

	public void findByIngName(List<String> ingName) {
		List<RecipeInfo> recipeInfo = null;
		try {
			dio.sendMenu(Menu.SEARCH_RECIPE_INGREDIENTS);
			dio.sendListString(ingName);
			if (dio.receiveStatus().equals("fail")) {
				FailView fail = new FailView();
				fail.recipeList(dio.receive());
			} else {
				recipeInfo = dio.receiveRecipeInfos();
				RecipeListView listView = new RecipeListView(dio);
				listView.showAllRecipeListView(recipeInfo);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showFindbyName() {
		Scanner sc = new Scanner(System.in);
		System.out.println("이름으로 검색");
		System.out.print("이름을 입력하세요: ");
		String ingName = sc.nextLine();
		findByName(ingName);
	}

	public void findByName(String recipeName) {
		List<RecipeInfo> recipeInfo = null;
		try {

			dio.sendMenu(Menu.SEARCH_RECIPE_NAME);
			dio.send(recipeName);
			// dio.receiveStatus();
			RecipeListView listView = new RecipeListView(dio);
			if (dio.receiveStatus().equals("fail")) {
				FailView fail = new FailView();
				fail.recipeList("해당되는 레시피 없음");
			} else {
				recipeInfo = dio.receiveRecipeInfos();
				listView.showAllRecipeListView(recipeInfo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void showFindbyCodeView() {
		Scanner sc = new Scanner(System.in);
		System.out.println("코드로 검색");
		System.out.print("코드를 입력하세요:");
		int s = Integer.parseInt(sc.nextLine());
		findByCode(s);
	}

	public void findByCode(int recipeCode) {
		RecipeInfo recipeInfo = null;
		try {
			dio.sendMenu(Menu.SEARCH_RECIPE_CODE);
			dio.sendMenu(recipeCode);
			if (dio.receiveStatus().equals("fail")) {
				FailView fail = new FailView();
				fail.recipeList(dio.receive());
			} else {
				recipeInfo = dio.receiveRecipeInfo();
				RecipeInfoView infoView = new RecipeInfoView(dio);
				infoView.showRecipeInfoView(recipeInfo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
