package com.recipe.view;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.share.CustomerShare;
import com.recipe.vo.Purchase;
import com.recipe.vo.RecipeInfo;
import com.recipe.vo.Review;

public class PurchaseInfoView {
	private DataIO dio;
	private Scanner sc;

	public PurchaseInfoView(DataIO dio) {
		sc = new Scanner(System.in);
		this.dio = dio;
	}

	public void searchPurchaseInfoView(Purchase p, boolean b) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
		NumberFormat nf = NumberFormat.getInstance();
		String menu = null;

		do {
			System.out.println("레시피명 : " + p.getPurchaseDetail().getRecipeInfo().getRecipeName());
			System.out.println("구매일자 : " + sdf.format(p.getPurchaseDate()));
			// BooleanList를 받은 b를 비교
			String temp = b == true ? "YES" : "NO";
			System.out.println("후기등록여부 : " + temp);
			System.out.println("구매수량 : " + p.getPurchaseDetail().getPurchaseDetailQuantity() + "개");
			System.out.println("총 결제금액 : " + nf.format(p.getPurchaseDetail().getPurchaseDetailQuantity()
					* p.getPurchaseDetail().getRecipeInfo().getRecipePrice()) + "원");

			if (b == true) {
				System.out.println("1.레시피로가기  | 0.이전화면");
				System.out.println("메뉴 번호를 입력해주세요 : ");
				menu = sc.nextLine();
				if (menu.equals("1")) {
					int n = Integer.parseInt(menu);
					System.out.println(menu);
					RecipeInfo param = p.getPurchaseDetail().getRecipeInfo();
					RecipeInfoView view = new RecipeInfoView(dio);
					view.showRecipeInfoView(param);
				}
			} else {
				System.out.println("1.상세레시피로가기  | 2.후기등록하기 | 0.이전화면");
				System.out.println("메뉴 번호를 입력해주세요 : ");
				menu = sc.nextLine();
				if (menu.equals("2")) {
					AddReviewView addView = new AddReviewView(dio);
					RecipeInfo info = p.getPurchaseDetail().getRecipeInfo();
					try {
						addView.insertReviewView(info);
					} catch (IOException e) {
						e.printStackTrace();
					}
					menu = "0";
				} else if (menu.equals("1")) {
					int n = Integer.parseInt(menu);
					System.out.println(menu);
					RecipeInfo info = p.getPurchaseDetail().getRecipeInfo();
					RecipeInfoView view = new RecipeInfoView(dio);
					view.showRecipeInfoView(info);
					menu = "0";
				}
			}

		} while (!menu.equals("0"));
	}

}
