package com.recipe.view;

import java.io.IOException;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.vo.RecipeInfo;

public class RecipeInfoView {
	private Scanner sc;
	private DataIO dio;
	
	public RecipeInfoView(DataIO dio) {
		sc = new Scanner(System.in);
		this.dio = dio;
	}
	
	/**
	 * 레시피 정보를 화면에 출력하는 메소드. 이때 레시피 과정 정보를 서버에서 전달받아 화면에 출력한다.
	 * @param info 화면에 보여줄 레시피 정보
	 * @author 최종국
	 */
	public void showRecipeInfoView(RecipeInfo info) {
		basicMenu(info); //Admin은 로그인하지 않은 경우에만 레시피 검색을 하기때문에 로그인 상태를 체크할 필요가 없다.
	}
	
	/**
	 * 로그인하지 않은 상태에서 보여줄 메뉴를 출력
	 * @author 최종국
	 */
	private void basicMenu(RecipeInfo info) {
		String menu = null;
		try {
			do {
				printRecipeInfo(info);
				System.out.println("1.좋아요 2.싫어요 3.후기목록보기 0.이전화면");
				menu = sc.nextLine();
				if (menu.equals("1")) {
					likeThisRecipe(info);
				} else if (menu.equals("2")) {
					disLikeThisRecipe(info);
				} else if (menu.equals("3")) {
					//후기목록보기
					ReviewListView reviewListView = new ReviewListView(dio);
					reviewListView.showReviewListByRecipeCodeView(info.getRecipeCode());
				}
			} while (!menu.equals("0"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 현재 레시피의 좋아요 개수를 하나 증가한다
	 * @param info 현재 레시피 정보를 가진 RecipeInfo 객체
	 * @author 최종국
	 * @throws IOException 
	 */
	private void likeThisRecipe(RecipeInfo info) throws IOException {
		dio.sendMenu(Menu.LIKE);
		dio.send(info.getPoint());
		
		if(dio.receiveStatus().equals("fail")) {
			FailView fail = new FailView();
			fail.likeRecipe(dio.receive());
		}
		info.getPoint().setLikeCount(info.getPoint().getLikeCount() + 1);
	}
	
	/**
	 * 현재 레시피의 싫어요 개수를 하나 증가한다
	 * @param info 현재 레시피 정보를 가진 RecipeInfo 객체
	 * @author 최종국
	 * @throws IOException 
	 */
	private void disLikeThisRecipe(RecipeInfo info) throws IOException {
		dio.sendMenu(Menu.DISLIKE);
		dio.send(info.getPoint());
		
		if(dio.receiveStatus().equals("fail")) {
			FailView fail = new FailView();
			fail.likeRecipe(dio.receive());
		}
		info.getPoint().setDisLikeCount(info.getPoint().getDisLikeCount() + 1);
	}
	
	/**
	 * 레시피 정보를 출력한다
	 * @param info 레시피의 정보를 가진 RecipeInfo VO객체
	 */
	private void printRecipeInfo(RecipeInfo info) {
		System.out.println(info); //레시피 정보 출력
		try {
			dio.sendMenu(Menu.RECIPE_PROCESS); //레시피 과정 정보를 서버에 요청
			dio.send(info.getRecipeProcess()); //레시피 과정 경로를 서버에 전송 (이렇게 하는게 맞을지... recipeCode를 보내면 그에 대한 파일 경로를 검색해서 과정 정보를 보내주도록 하는게 맞는지...)
			System.out.println(dio.receive()); //레시피 과정 정보 출력
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
