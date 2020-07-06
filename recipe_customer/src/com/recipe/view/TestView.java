package com.recipe.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.recipe.io.DataIO;
import com.recipe.share.CustomerShare;
import com.recipe.vo.Favorite;
import com.recipe.vo.Point;
import com.recipe.vo.RecipeInfo;
import com.recipe.vo.Review;

/**
 * @author Soojeong 테스트 파일 입니다. upload 금지
 */
public class TestView {

	public static void main(String[] args) {
		Socket s;
		DataIO dio;

		try {
			s = new Socket("127.0.0.1", 1025);
			dio = new DataIO(new DataOutputStream(s.getOutputStream()), new DataInputStream(s.getInputStream()));
			
			CustomerShare.loginedId = "tester";

			//test_MyReviewView(dio);
			//test_MyReviewViewList(dio);
			//test_recipeCodeReviewViewList(dio);
			//test_removeReview(dio);
			//test_FavoriteListView(dio);
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void test_MyReviewView (DataIO dio) throws IOException {
		MyReviewListView view = new MyReviewListView(dio);
		CustomerShare.loginedId = "kosj";
		view.showMyReviewListView(CustomerShare.loginedId);
	}
	
	private static void test_MyReviewViewList (DataIO dio) {
		MyReviewListView view = new MyReviewListView(dio);
		CustomerShare.loginedId = "kosj";
		List<Review> list = view.searchReviewList(CustomerShare.loginedId);
		
		for (Review r : list ) {
			System.out.println(r.getCustomerId());
			System.out.println(r.getReviewComment());
			System.out.println(r.getReviewDate());
		}
	}
	
	private static void test_recipeCodeReviewViewList (DataIO dio) {
		ReviewListView view = new ReviewListView(dio);
		int recipeCode = 134;
		view.showReviewListByRecipeCodeView(recipeCode);
	}
	private static void test_removeReview (DataIO dio) throws IOException {
		RemoveReviewView view = new RemoveReviewView(dio);
		MyReviewListView myView = new MyReviewListView(dio);
		myView.showMyReviewListView(CustomerShare.loginedId);
		List<Review> list = myView.searchReviewList(CustomerShare.loginedId);
		view.removeReview(list);
		System.out.println("삭제 완료!!!");
	}
	
	private static void test_FavoriteListView(DataIO dio) throws IOException {
		FavoriteListView listView = new FavoriteListView(dio);
		List<Favorite> list = new ArrayList<>();
		listView.showFavoriteListView(CustomerShare.loginedId);
	}

} // end class TestView()
