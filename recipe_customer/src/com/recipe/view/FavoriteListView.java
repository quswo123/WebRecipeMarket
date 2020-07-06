package com.recipe.view;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.CustomerShare;
import com.recipe.vo.Favorite;
import com.recipe.vo.RecipeInfo;

/**
 * @author Soojeong
 *
 */
public class FavoriteListView {
	private DataIO dio;
	private Scanner sc;

	public FavoriteListView(DataIO dio) {
		sc = new Scanner(System.in);
		this.dio = dio;
		sc = new Scanner(System.in);
	}

	/**
	 * 즐겨찾기 로그인 한 customerId로 목록조회
	 * @throws IOException 
	 */
	public void showFavoriteListView(String customerId) throws IOException {
		System.out.println("===== 즐겨찾기 목록 보기 =====");
		String menu;
		int start_index = 0;
		int end_index = 0;
		do { 
        	List<Favorite> favoriteList = searchFavoriteList(customerId);
        	/*목록 출력*/
        	int size = favoriteList.size();
        	if (start_index == 0) end_index = size <= 5 ? size : 5;

        	SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        	
        	System.out.println("== ["+size+"]건의 등록된 즐겨찾기 목록이 조회되었습니다 ==");
        	if ( size != 0 ) {
            	System.out.println("NO. 레시피이름  ");
        	}
            for (int i = start_index; i<end_index; i++) {
                Favorite f = favoriteList.get(i);
                System.out.println( i+1 + ". "
                        + f.getRecipeInfo().getRecipeName()
                );
            }
	                
	        if(size < 5) {
	        	sc = new Scanner(System.in);
	        	System.out.println("0:뒤로가기  D:즐겨찾기해제");
	            System.out.print("상세레시피를 보기 원하시면 번호를 입력하세요 : ");
	            menu = sc.nextLine();
	            if(menu.equalsIgnoreCase("D")) {
	                try {
	                    removeFavoriteView();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    FailView fail = new FailView();
	                    String msg = dio.receive();
	                    fail.favoriteListView(msg);
	                }
                } else if (!menu.equals("0")) {
	                int n = Integer.parseInt(menu);
	                RecipeInfo param = favoriteList.get(n-1).getRecipeInfo();            
	                RecipeInfoView infoView = new RecipeInfoView(dio);
	                infoView.showRecipeInfoView(param);
	            }
	        } else {
	            System.out.println("---------------------------------------------");
	            System.out.println("-:이전페이지 +:다음페이지 0:뒤로가기 D:즐겨찾기해제 ");
	            System.out.print("상세레시피를 보기 원하시면 번호를 입력하세요 : ");
	            menu = sc.nextLine();
	            
	            if(menu.equals("-")) {
	                start_index = (start_index - 5) >= 0 ? (start_index - 5) : 0; //이전 페이지를 누르면 시작 인덱스 값을 5 감소시킨다. 이떄, 0보다 작아지면 0으로 설정한다
	                end_index = start_index + 5; //시작 인덱스부터 다섯개를 출력하기 위해 끝 인덱스는 시작 인덱스에서 5 증가한 값을 갖는다
				
	            } else if(menu.equals("+")) {
					end_index = (end_index + 5) <= size ? (end_index + 5) : size; //다음 페이지를 누르면 end_index 값을 5 증가시킨다. 이때, list의 size보다 커지면 size와 같은 값으로 설정한다
					start_index = (end_index % 5) == 0 ? end_index - 5 : end_index-(end_index%5); //시작 인덱스부터 다섯개를 출력하기 위해 시작 인덱스는 끝 인덱스에서 5 감소한 값을 갖는다
	            
				} else if(menu.equalsIgnoreCase("D")) {
	                try {
	                    removeFavoriteView();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    FailView fail = new FailView();
	                    String msg = dio.receive();
	                    fail.favoriteListView(msg);
	                } 
	                
				}  else if (!menu.equals("0")){
	                int n = Integer.parseInt(menu);
	                RecipeInfo param = favoriteList.get(n-1).getRecipeInfo();            
	                RecipeInfoView infoView = new RecipeInfoView(dio);
	                infoView.showRecipeInfoView(param);
				}
	        }
	    } while (!menu.equals("0"));
	}
	
	/**
	 * Favorite 목록에서 삭제
	 * @throws IOException 
	 */
	public void removeFavoriteView() throws IOException {
		/*삭제할 즐겨찾기 번호 입력*/
		System.out.print("즐겨찾기 해제를 원하는 번호를 입력해주세요 :");
		int selectNum = Integer.parseInt((sc.nextLine()));
		
		List<Favorite> fList = searchFavoriteList(CustomerShare.loginedId);
		Favorite f = fList.get(selectNum-1);
		
		dio.sendMenu(Menu.REMOVE_FAVORITE); //Menu.java에 추가할 것 
		dio.send(f);
		SuccessView success = new SuccessView();
		String msg = "즐겨찾기 목록에서 삭제되었습니다.";
		success.favoriteDeleteView(msg);
	}
	
	/**
	 * 즐겨찾기 목록 보기 수행시, 사용자의 customerId로 조회한 결과값 반환한다.
	 * @param customerId 로그인한 사용자의 ID
	 */
	public List<Favorite> searchFavoriteList(String customerId) {
		List<Favorite> favoriteList = null;
		try {
			//favorite 목록을 조회 절차 수행을 위한 customerId와 RecipeInfo객체 전송
			dio.sendMenu(Menu.SEARCH_FAVORITE_BY_CUSTOMERID); //Menu.java에 추가할 것 
			dio.send(customerId);
			favoriteList = dio.receiveFavorites();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return favoriteList;
	}
	
	/**
	 * 즐겨찾기 추가시 Favorite 객체 정보로 추가  조회한 결과값 status 반환한다.
	 * @param customerId 로그인한 사용자의 ID
	 */
	public void insertFavorite(Favorite f) {
		try {
			//전송받은 데이터
			dio.sendMenu(Menu.ADD_FAVORITE);
			dio.send(f);
			//client측으로 전송할 데이터
					
			if (dio.receive().equals("success")) {
				SuccessView success = new SuccessView();
	            String msg = "즐겨찾기 등록 성공했습니다.";
	            success.favoriteInsertView(msg);
	            System.out.println("------------------------------------");
	            
			} else {
				FailView fail = new FailView();
				String msg = dio.receive();
				fail.favoriteInsertView(msg);
				System.out.println("------------------------------------");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
} // end class FavoriteListView
