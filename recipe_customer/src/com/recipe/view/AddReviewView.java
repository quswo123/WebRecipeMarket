package com.recipe.view;

import java.io.IOException;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.CustomerShare;
import com.recipe.vo.RecipeInfo;
import com.recipe.vo.Review;

/**
 * @author Soojeong
 *
 */
public class AddReviewView {

	private DataIO dio;
	private Scanner sc;
//	private String customerId;
//	private RecipeInfo recipeInfo;

	public AddReviewView(DataIO dio) {
		this.dio = dio;
	}

	public void insertReviewView(RecipeInfo info) throws IOException {
		System.out.println("===== 후기 등록  =====");
		System.out.print("== 구매 후기를 입력해주세요 : ");
		sc = new Scanner(System.in);
		String comment = sc.nextLine();

		Review review = new Review();
		review.setCustomerId(CustomerShare.loginedId);
		review.setReviewComment(comment);
		review.setRecipeInfo(info);

		dio.sendMenu(Menu.ADD_REVIEW);
		dio.send(review);

		if (dio.receive().equals("fail")) {
			FailView fail = new FailView();
			String msg = dio.receive();
			fail.reviewInsertView(msg);
		}

		System.out.println("====레시피 추천하시겠습니까?====");
		System.out.print("==== 1.좋아요 | 2.싫어요  : ");
		int select = Integer.parseInt(sc.nextLine());
		switch (select) {
		case 1:
			dio.send(Menu.LIKE);
			break;
		case 2:
			dio.send(Menu.DISLIKE);
			break;
		}
		dio.send(review.getRecipeInfo().getPoint());

		if (dio.receive().equals("success")) {
			SuccessView success = new SuccessView();
            String msg = "후기를 작성해주셔서 감사합니다.";
            success.reviewInsertView(msg);
		} else {
			FailView fail = new FailView();
			String msg = dio.receive();
			fail.reviewInsertView(msg);
		}
		/*
		 * PurchaseListVIew pview = new PurchaseListVIew(dio); pview.purchaseView();
		 */
	}

} // end class AddReviewView
