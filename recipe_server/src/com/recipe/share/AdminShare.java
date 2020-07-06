package com.recipe.share;

import java.util.ArrayList;
import java.util.List;

public class AdminShare {
	private static List<String> session = new ArrayList<String>();
	/**
	 * 전달받은 id가 로그인 상태인지(id가 세션상에 존재하는지) 확인한다
	 * @param id 로그인 상태인지 확인할 id
	 * @return id가 session에 존재한다면 true, 존재하지 않으면 false를 반환한다
	 */
	public static boolean isExistInSession(String id) {
		if(session.contains(id)) return true;
		else return false;
	}
	
	/**
	 * id를 로그인 상태로 만든다(session에 id를 저장한다)
	 * @param id 세션에 저장할 id
	 */
	public static void addSession(String id) {
		session.add(id);
	}
	
	/**
	 * id를 로그아웃 상태로 전환한다(session에서 id를 삭제한다)
	 * @param id 세션에서 삭제할 id
	 */
	public static void removeSession(String id) {
		session.remove(id);
	}
}
