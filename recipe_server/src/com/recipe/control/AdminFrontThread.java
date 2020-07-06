package com.recipe.control;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.recipe.exception.AddException;
import com.recipe.exception.DuplicatedException;
import com.recipe.exception.FindException;
import com.recipe.exception.ModifyException;
import com.recipe.exception.RemoveException;
import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.AdminShare;
import com.recipe.vo.Point;
import com.recipe.vo.RecipeInfo;
import com.recipe.vo.Review;

public class AdminFrontThread implements Runnable{
	private Socket client;
	private DataIO dio;
	private RecipeMarketControl control;
	
	public AdminFrontThread(Socket s) {
		client = s;
		try {
			dio = new DataIO(new DataOutputStream(client.getOutputStream()), new DataInputStream(client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		control = RecipeMarketControl.getInstance();
	}
	
	/**
	 * 전달받은 메뉴 번호에 해당하는 절차를 수행한다.
	 */
	@Override
	public void run() {
		int menu = -1;
		try {
			do {
				menu = dio.receiveMenu();
				switch (menu) {
				case Menu.ADMIN_LOGIN: // 로그인
					loginFront();
					break;
				case Menu.ADMIN_LOGOUT:
					logoutFront();
					break;
				case Menu.RD_ALL: //R&D계정 전체 조회
					viewAllRdFront();
					break;
				case Menu.RD_ADD: //R&D계정 추가
					addRdFront();
					break;
				case Menu.RD_MODIFY: //R&D계정 수정
					modifyRdFront();
					break;
				case Menu.RD_REMOVE: //R&D계정 삭제
					removeRdFront();
					break;
				case Menu.RECOMMENDED_RECIPE: // 추천 레시피
					recommendRecipeFront();
					break;
				case Menu.RECIPE_PROCESS: //레시피 과정 정보
					recipeProcessFront();
					break;
				case Menu.LIKE: //좋아요
					likeRecipeFront();
					break;
				case Menu.DISLIKE: //싫어요
					disLikeRecipeFront();
					break;	
				case Menu.SEARCH_REVIEW_BY_RECIPECODE: //사용자 즐겨찾기 목록 보기
					reviewByRecipeCodeFront();
					break;
				case Menu.SEARCH_RECIPE_INGREDIENTS: // 레시피 코드 검색
					selectByIngFront();
					break;
				case Menu.SEARCH_RECIPE_NAME: // 레시피 제목 검색
					selectByNameFront();
					break;
				default:
					break;
				}
			} while (menu != -1);
		} catch (EOFException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 로그인에 필요한 ID, 패스워드를 Client로부터 전달받아 로그인 절차를 수행한다
	 * @throws IOException
	 * @author 최종국
	 */
	public void loginFront() throws IOException {
		String id = dio.receiveId();
		String pwd = dio.receivePwd();
		try {
			control.loginToAdmin(id, pwd);
			dio.sendSuccess();
		} catch (FindException e) {
			dio.sendFail(e.getMessage());
		}
	}

	/**
	 * 로그아웃할 아이디를 전달받아 로그아웃 절차를 수행한다.
	 * @throws IOException 
	 * @author 최종국
	 */
	private void logoutFront() throws IOException {
		String adminId = dio.receiveId();
		AdminShare.removeSession(adminId);
		
		dio.sendSuccess();
	}
	
	/**
	 * 클라이언트에게 추천 레시피를 탐색하여 전송한다
	 * @throws IOException
	 * @author 최종국
	 */
	public void recommendRecipeFront() throws IOException {
		RecipeInfo info = null;
		try {
			info = control.searchRecommended();
			dio.sendSuccess();
			dio.send(info);
		} catch (FindException e) {
			e.printStackTrace();
			dio.sendFail(e.getMessage());
		}
	}
	
	/**
	 * 레시피 과정 정보를 클라이언트에 전송한다
	 * @throws IOException
	 * @author 최종국
	 */
	public void recipeProcessFront() throws IOException {
		String filePath = dio.receive();
		String result = "";
		String process = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			while((process = br.readLine()) != null) result += process + "\n";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		dio.send(result);
	}
	
	/**
	 * 레시피 코드를 전달받아 해당하는 레시피의 좋아요 개수를 증가시킨다
	 * @throws IOException
	 * @author 최종국
	 */
	public void likeRecipeFront() throws IOException {
		Point p = dio.receivePoint();
		p.like();
		try {
			control.modifyPoint(p);
			dio.sendSuccess();
		} catch (ModifyException e) {
			e.printStackTrace();
			dio.sendFail(e.getMessage());
		}
	}
	
	/**
	 * 레시피 코드를 전달받아 해당하는 레시피의 싫어요 개수를 증가시킨다
	 * @throws IOException
	 * @author 최종국
	 */
	public void disLikeRecipeFront() throws IOException {
		Point p = dio.receivePoint();
		p.disLike();
		try {
			control.modifyPoint(p);
			dio.sendSuccess();
		} catch (ModifyException e) {
			e.printStackTrace();
			dio.sendFail(e.getMessage());
		}
	}
	/**
	 * 모든 R&D 계정에 대한 RD 객체 리스트를 클라이언트에게 전송한다
	 * @throws IOException
	 */
	public void viewAllRdFront() throws IOException {
		try {
			dio.sendSuccess();
			dio.sendRDList(control.viewAllRd());
		} catch (FindException e) {
			dio.sendFail(e.getMessage());
		}
	}
	
	/**
	 * R&D 계정 추가에 필요한 RD 객체를 클라이언트로부터 수신하여 rd 테이블에 새로운 R&D 계정 정보를 추가한다 
	 * @throws IOException
	 * @author 최종국
	 */
	public void addRdFront() throws IOException {
		try {
			control.addRd(dio.receiveRd());
			dio.sendSuccess();
		} catch (DuplicatedException e) {
			e.printStackTrace();
			dio.sendFail(e.getMessage());
		} catch (AddException e) {
			e.printStackTrace();
			dio.sendFail(e.getMessage());
		}
	}
	
	/**
	 * R&D 계정 수정에 필요한 RD 객체를 클라이언트로부터 수신하여 rd 테이블의 R&D 계정 정보를 수정한다 
	 * @throws IOException
	 * @author 최종국
	 */
	public void modifyRdFront() throws IOException {
		try {
			control.modifyRd(dio.receiveRd());
			dio.sendSuccess();
		} catch (ModifyException e) {
			e.printStackTrace();
			dio.sendFail(e.getMessage());
		}
	}
	
	/**
	 * R&D 계정 삭제에 필요한 아이디를 클라이언트로 수신하여 rd 테이블의 R&D 계정 정보를 삭제한다
	 * @throws IOException
	 * @author 최종국
	 */
	public void removeRdFront() throws IOException {
		try {
			control.removeRd(dio.receiveId());
			dio.sendSuccess();
		} catch (RemoveException e) {
			e.printStackTrace();
			dio.sendFail(e.getMessage());
		}
	}

	
	/**
	 * recipeCode에 해당하는 후기 목록을 조회한 후 반환한다.
	 * @throws IOException
	 * @author 고수정
	 */
	public void reviewByRecipeCodeFront() throws IOException {
		int recipeCode = dio.receiveInt();
		List<Review> list = new ArrayList<>();
		
		try {
			list = control.viewRecipeReview(recipeCode);
			dio.sendReviews(list);
		} catch (FindException e) {
			e.printStackTrace();
		}
	}

	public void selectByIngFront() throws IOException {
		List<String> recipeInfo = dio.receiveListString();
		List<RecipeInfo> searchedRecipeInfo = null;		
		try {
			
			searchedRecipeInfo = control.searchByIngName(recipeInfo);
			dio.sendSuccess();
			dio.send(searchedRecipeInfo);
			
		} catch (FindException e) {
			dio.sendFail(e.getMessage());
		}
	}
	/**
	 * 레시피이름에 해당하는 레시피목록을 클라이언트부터 전달받음
	 * @throws IOException
	 */
	public void selectByNameFront() throws IOException {
		List<RecipeInfo> recipeInfo = null;
		String recipeName = dio.receive();
		try {
			
			recipeInfo = control.searchByName(recipeName);

			dio.sendSuccess();
			dio.send(recipeInfo);
		} catch (FindException e) {
			dio.sendFail(e.getMessage());
		}
	}
}
