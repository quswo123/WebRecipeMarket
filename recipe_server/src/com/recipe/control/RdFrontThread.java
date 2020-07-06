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

import com.recipe.exception.DuplicatedException;
import com.recipe.exception.FindException;
import com.recipe.exception.ModifyException;
import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.RDShare;
import com.recipe.vo.Point;
import com.recipe.vo.Ingredient;
import com.recipe.vo.RecipeInfo;
import com.recipe.vo.Review;

public class RdFrontThread implements Runnable{
	private Socket client;
	private DataIO dio;
	private RecipeMarketControl control;

	public RdFrontThread(Socket s) {
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
				case Menu.RD_LOGIN: // 로그인
					loginFront();
					break;
				case Menu.RD_INFO: //R&D 내 정보 보기
					viewRdInfo();
					break;
				case Menu.ADD_RECIPE: // 레시피 등록
					addRecipeFront();
					break;
				case Menu.MODIFY_RECIPE: // 레시피 등록
					modifyRecipeFront();
					break;
				case Menu.REMOVE_RECIPE: // 레시피 삭제
					removeRecipeFront();
					break;				
				case Menu.RECIPE_ALL: // 레시피 전체조회
					viewAllRecipeFront();
					break;
				case Menu.RECOMMENDED_RECIPE: // 추천 레시피
					recommendRecipeFront();
					break;
				case Menu.SEARCH_RECIPE_CODE: // 레시피 코드 검색
					selectByRecipeCode();
					break;
				case Menu.SEARCH_RECIPE_NAME: // 레시피 제목 검색
					selectByNameFront();
					break;
				case Menu.SEARCH_RECIPE_INGREDIENTS: // 레시피 재료 검색
					selectByIngFront();
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
				case Menu.RD_LOGOUT: //로그아웃
					logoutFront();
					break;
				case Menu.SEARCH_REVIEW_BY_RECIPECODE: //사용자 즐겨찾기 목록 보기
					reviewByRecipeCodeFront();
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
			control.loginToRd(id, pwd);
			dio.sendSuccess();
		} catch (FindException e) {
			dio.sendFail(e.getMessage());
		}
	}

	/**
	 * 로그아웃에 필요한 아이디를 전달받아 로그아웃 절차를 수행한다.
	 * @throws IOException
	 * @author 최종국
	 */
	public void logoutFront() throws IOException {
		String rdId = dio.receiveId();
		RDShare.removeSession(rdId);

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

	/**
	 * 레시피코드에 해당하는 레시피목록을 클라이언트부터 전달받음
	 * @throws IOException
	 */
	public void selectByRecipeCode() throws IOException {
		RecipeInfo recipeInfo = null;
		int recipeCode = dio.receiveMenu();
		try {
			
			recipeInfo = control.searchByCode(recipeCode);
			dio.sendSuccess();
			dio.send(recipeInfo);
		}catch (FindException e) {
			dio.sendFail(e.getMessage());
		}
	}

	public void viewAllRdFront() throws IOException {
		try {
			dio.sendSuccess();
			control.viewAllRd();
		} catch (FindException e) {
			dio.sendFail(e.getMessage());
		}
	}

	public void addRecipeFront()throws IOException{
		String rdId = dio.receiveId();
		RecipeInfo recipeInfo = dio.receiveRecipeInfo();
		String ingInfo = dio.receive();
		List<Ingredient> ingList = dio.receiveIngredientList();
		String process = dio.receive();
		try {
			control.addRecipe(rdId, recipeInfo, ingInfo, ingList, process);
			dio.sendSuccess();
		} catch (DuplicatedException e) {
			e.printStackTrace();
			dio.sendFail(e.getMessage());
		}
	}

	public void modifyRecipeFront()throws IOException{
		String rdId = dio.receiveId();
		RecipeInfo recipeInfo = dio.receiveRecipeInfo();
		String ingInfo = dio.receive();
		List<Ingredient> ingList = dio.receiveIngredientList();
		String process = dio.receive();
		try {
			control.modifyRecipe(rdId, recipeInfo, ingInfo, ingList, process);
			dio.sendSuccess();
		} catch (ModifyException e) {
			e.printStackTrace();
			dio.sendFail(e.getMessage());
		}
	}

	public void removeRecipeFront() throws IOException{
		String rdId = dio.receiveId();
		RecipeInfo recipeInfo = dio.receiveRecipeInfo();
		try {
			control.removeRecipe(rdId, recipeInfo);
			dio.sendSuccess();
		} catch (ModifyException e) {
			e.printStackTrace();
			dio.sendFail(e.getMessage());
		}
	}
	public void viewAllRecipeFront() throws IOException{
		try {
			dio.send(control.viewAllRecipe());
			dio.sendSuccess();
		} catch (FindException e) {
			e.printStackTrace();
			dio.sendFail(e.getMessage());
		}
	}

	public void viewRdInfo() throws IOException{
		String rdId = dio.receiveId();
		try {
			dio.sendSuccess();
			dio.send(control.viewRdAccount(rdId));
		} catch (FindException e) {
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
}
