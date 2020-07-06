package com.recipe.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.recipe.io.DataIO;

public class MainView {
	private static final int RD_PORT = 1026;
	private Scanner sc;
	private Socket s;
	private DataIO dio;
	
	public MainView() {
		sc = new Scanner(System.in);
		try {
			s = new Socket("192.168.0.114", RD_PORT);
			dio = new DataIO(new DataOutputStream(s.getOutputStream()), new DataInputStream(s.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void mainMenu() {
		int menu = -1;
		try {
			do {
				System.out.println("1.레시피검색 2.추천레시피 3.로그인 0.프로그램 종료");
				menu = Integer.parseInt(sc.nextLine());
				switch (menu) {
				case 1:
					RecipeSearchView recipeSearchView = new RecipeSearchView(dio);
					recipeSearchView.showRecipeInfoView();
					break;
				case 2:
					RecommendedRecipeView recommendedRecipeView = new RecommendedRecipeView(dio);
					recommendedRecipeView.showRecommendedRecipeView();
					break;
				case 3:
					LoginView loginView = new LoginView(dio);
					loginView.showLoginView();
					break;
				case 0:
					System.exit(0);
				default:
					break;
				}
			} while (menu != -1);
		} catch (IOException e) {

		}
	}
	
	public static void main(String[] args) {
		MainView mainView = new MainView();
		mainView.mainMenu();
	}

}
