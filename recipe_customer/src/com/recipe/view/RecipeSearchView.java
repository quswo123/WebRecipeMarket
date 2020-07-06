package com.recipe.view;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.vo.RecipeInfo;

public class RecipeSearchView {	
	private DataIO dio;	
	public RecipeSearchView(DataIO dio) {
		this.dio = dio;
	}
	public void showRecipeInfoView() {
		Scanner sc = new Scanner(System.in);
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
			if (dio.receiveStatus().equals("fail")) {
				FailView fail = new FailView();
				fail.recipeList(dio.receive());
			} else {
				recipeInfo = dio.receiveRecipeInfos();
				RecipeListView listView = new RecipeListView(dio);
				listView.showAllRecipeListView(recipeInfo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
