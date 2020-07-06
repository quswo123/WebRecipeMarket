package com.recipe.view;

public class SuccessView {
	/**
	 * 로그인 성공 메시지를 출력한다
	 * @param id 로그인한 아이디
	 * @author 최종국
	 */
	public void loginAdminView(String id) {
		System.out.println(id + " 로그인 성공");
	}
	
	/**
	 * 로그아웃 성공 메시지를 출력한다
	 * @author 최종국
	 */
	public void logoutAdminView() {
		System.out.println("로그아웃 되었습니다");
	}
	
	/**
	 * R&D 계정 추가 성공 메시지를 출력한다
	 * @author 최종국
	 */
	public void addRdView(String rdId) {
		System.out.println(rdId + " 계정 추가되었습니다");
	}
	
	/**
	 * R&D 계정 수정 성공 메시지를 출력한다
	 * @author 최종국
	 */
	public void modifyRdView(String rdId) {
		System.out.println(rdId + " 계정 수정되었습니다");
	}
	
	/**
	 * R&D 계정 삭제 성공 메시지를 출력한다
	 * @author 최종국
	 */
	public void removeRdView(String rdId) {
		System.out.println(rdId + " 계정 삭제되었습니다");
	}
}
