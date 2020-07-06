package com.recipe.view;

public class FailView {
	/**
	 * 로그인 실패 메시지를 출력한다
	 * @param msg 발생한 오류 메시지
	 * @author 최종국
	 */
	public void loginAdminView(String msg) {
		System.out.println(msg);
	}
	/**
	 * 로그아웃 실패 메시지를 출력한다
	 * @author 최종국
	 */
	public void logoutAdminView() {
		System.out.println("로그아웃 실패");
	}
	
	/**
	 * 추천 레시피 실패 메시지를 출력한다
	 * @author 최종국
	 */
	public void recommendedRecipe(String msg) {
		System.out.println("추천 레시피 실패 : " + msg);
	}
	 /* 추천 레시피 실패 메시지를 출력한다
	 * @author 최종국
	 */
public void recipeList(String msg) {
	System.out.println(msg);
}
	
	/**
	 * 좋아요 실패 메시지를 출력한다
	 * @author 최종국
	 */
	public void likeRecipe(String msg) {
		System.out.println("좋아요 실패 : " + msg);
	}
	
	/**
	 * R&D 계정 추가 실패 메시지를 출력한다
	 * @author 최종국
	 */
	public void addRdView(String msg) {
		System.out.println("R&D 계정 추가 실패 : " + msg);
	}
	
	/**
	 * R&D 계정 수정 실패 메시지를 출력한다
	 * @author 최종국
	 */
	public void modifyRdView(String msg) {
		System.out.println("R&D 계정 수정 실패 : " + msg);
	}
	
	/**
	 * R&D 계정 삭제 실패 메시지를 출력한다
	 * @author 최종국
	 */
	public void removeRdView(String msg) {
		System.out.println("R&D 계정 삭제 실패 : " + msg);
	}
	
	/**
	 * R&D 계정 전체 조회 실패 메시지를 출력한다
	 * @param msg
	 */
	public void allRdView(String msg) {
		System.out.println("계정 전체 조회 실패 : " + msg);
	}
}
