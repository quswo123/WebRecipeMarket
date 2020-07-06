package com.recipe.view;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.vo.Review;

/**
 * @author Soojeong
 *
 */
public class ReviewListView {
	private DataIO dio;
	private Scanner sc;

	public ReviewListView(DataIO dio) {
		this.dio = dio;
		sc = new Scanner(System.in);
	}
	/**
	 *  로그인 한 recipeCode 로 목록조회
	 */
	public void showReviewListByRecipeCodeView(int recipeCode) {
		System.out.println("===== 레시피 후기 목록 보기 =====");
        String menu;
        int start_index = 0;
        int end_index = 0;
        do {        
        	List<Review> reviewList = searchByRecipeCodeReviewList(recipeCode);
    		/*목록 출력*/
        	int size = reviewList.size();
	        if (start_index == 0) end_index = size <= 5 ? size : 5;
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
	        
	        System.out.println("== ["+size+"]건의 후기 목록이 조회되었습니다 ==");
        	if ( size != 0 ) {
            	System.out.println("후기작성일자 | 작성자  | 후기내용 ");
        	}
            for (int i = start_index; i<end_index; i++) {
                Review r = reviewList.get(i);
                System.out.println( i+1 + ". "
                		+ sdf.format(r.getReviewDate()) +" | "
                        + r.getCustomerId() +" | "
                		+ r.getReviewComment()
                );
            }
	                
	        if(size < 5) {
	        	System.out.print("0:뒤로가기 ");
	            menu = sc.nextLine();
	        } else {
	            System.out.println("---------------------------------------------");
	            System.out.println("-:이전페이지 +:다음페이지 0:뒤로가기  : ");
	            menu = sc.nextLine();
	            
	            if(menu.equals("-")) {
	                start_index = (start_index - 5) >= 0 ? (start_index - 5) : 0; //이전 페이지를 누르면 시작 인덱스 값을 5 감소시킨다. 이떄, 0보다 작아지면 0으로 설정한다
	                end_index = start_index + 5; //시작 인덱스부터 다섯개를 출력하기 위해 끝 인덱스는 시작 인덱스에서 5 증가한 값을 갖는다
				} else if(menu.equals("+")) {
					end_index = (end_index + 5) <= size ? (end_index + 5) : size; //다음 페이지를 누르면 end_index 값을 5 증가시킨다. 이때, list의 size보다 커지면 size와 같은 값으로 설정한다
					start_index = (end_index % 5) == 0 ? end_index - 5 : end_index-(end_index%5); //시작 인덱스부터 다섯개를 출력하기 위해 시작 인덱스는 끝 인덱스에서 5 감소한 값을 갖는다
				}
			}
	    } while (!menu.equals("0"));
	}
	
	/**
	 * Review 목록 보기 수행시, recipeCode로 조회한 결과값 반환한다.
	 * @param recipeCode
	 */
	public List<Review> searchByRecipeCodeReviewList(int recipeCode) {
		List<Review> reviewList = null;
		try {
			//favorite 목록을 조회 절차 수행을 위한 customerId와 RecipeInfo객체 전송
			dio.sendMenu(Menu.SEARCH_REVIEW_BY_RECIPECODE);
			dio.send(recipeCode);
			reviewList = dio.receiveReviews();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return reviewList;
	}
}// end class ReviewListView
