package com.recipe.view;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.CustomerShare;
import com.recipe.vo.Purchase;
import com.recipe.vo.RecipeInfo;
import com.recipe.vo.Review;

public class PurchaseListVIew {
	private DataIO dio;
	private Scanner sc;

	public PurchaseListVIew(DataIO dio) {
		sc = new Scanner(System.in);
		this.dio = dio;
	}
	
	
	/**
	 * 나의 구매목록 보기 View
	 */
	public void purchaseView() {
		List<Purchase> list = null;
		List<Review> rlist = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
		String menu;

		int start_index = 0;
		int end_index = 0;
		try {
			do {
				// 현재 아이디전송
				dio.sendMenu(Menu.PURCHASE_LIST);
				dio.sendId(CustomerShare.loginedId);

				if (dio.receiveStatus().equals("fail")) {
					FailView fview = new FailView();
					fview.myPurchaseView(dio.receive());
					break;
				}
				// 구매리스트와 후기등록여부를 위해 후기리스트도 가져온다
				list = dio.receivePurchaseList();
				rlist = dio.receiveReviews();
				
				int size = list.size();
				if (start_index == 0) end_index = size <= 5 ? size : 5;// 화면에 다섯개씩 보여줄때 사용할 끝 인덱스
				//ListView를 최초로 구성할때, list의 size가 5 이하이면 size만큼 화면에 출력하고, 5를초과하면 5만큼만화면에 출력
				
				System.out.println("나의 구매내역");
				System.out.println("[" + list.size() + "건의 구매내역이 조회되었습니다 ]");
				System.out.println("레시피상품명/구매일자/후기등록여부");
				for (int i = start_index; i < end_index; i++) {
					// 레시피 이름
					System.out.print(i+1 + ". " + list.get(i).getPurchaseDetail().getRecipeInfo().getRecipeName() + " / ");
					// 레시피 날짜
					System.out.print(sdf.format(list.get(i).getPurchaseDate()) + " / ");
					int j;
					for (j = 0; j < rlist.size(); j++) {
						// 각 구매레시피코드와 리뷰레시피코드를 비교
						if (list.get(i).getPurchaseDetail().getRecipeInfo().getRecipeCode() == rlist.get(j)
								.getRecipeInfo().getRecipeCode()) {
							System.out.println("Yes");
							break;
						}
					}
					if (j == rlist.size()) {
						System.out.println("No");
					}
				} // for
				if (size < 5) {
					System.out.println("상세페이지번호 : | 0.이전화면");
					System.out.print("상세내역을 보기원하시면 번호를 입력해주세요 : ");
					menu = sc.nextLine();
					int n = Integer.parseInt(menu);
					if (n != 0) {
						PurchaseInfoView infoView = new PurchaseInfoView(dio);
						int i;
						for(i = 0; i < rlist.size(); i++) {
							if(list.get(n-1).getPurchaseDetail().getRecipeInfo().getRecipeCode()==rlist.get(i).getRecipeInfo().getRecipeCode()) {
								infoView.searchPurchaseInfoView(list.get(n - 1), true);
								break;
							}
						}
						if(i == rlist.size()) {
							infoView.searchPurchaseInfoView(list.get(n - 1), false);
						}
					}
				} else {
					System.out.println("-:이전페이지 | +:다음페이지  | 0.이전화면 ");
					System.out.print("상세내역을 보기원하시면 번호를 입력해주세요 : ");
					menu = sc.nextLine();
					if (menu.equals("-")) {
						start_index = (start_index - 5) >= 0 ? (start_index - 5) : 0; //이전 페이지를 누르면 시작 인덱스 값을 5 감소시킨다. 이떄, 0보다 작아지면 0으로 설정한다
						end_index = start_index + 5; //시작 인덱스부터 다섯개를 출력하기 위해 끝 인덱스는 시작 인덱스에서 5 증가한 값을 갖는다
					} else if (menu.equals("+")) {
						end_index = (end_index + 5) <= size ? (end_index + 5) : size; //다음 페이지를 누르면 end_index 값을 5 증가시킨다. 이때, list의 size보다 커지면 size와 같은 값으로 설정한다
						start_index = (end_index % 5) == 0 ? end_index - 5 : end_index-(end_index%5); //시작 인덱스부터 다섯개를 출력하기 위해 시작 인덱스는 끝 인덱스에서 5 감소한 값을 갖는다
					} else {
						int n = Integer.parseInt(menu);
						if (n != 0) {
							PurchaseInfoView infoView = new PurchaseInfoView(dio);
							int i;
							for(i = 0; i < rlist.size(); i++) {
								if(list.get(n-1).getPurchaseDetail().getRecipeInfo().getRecipeCode()==rlist.get(i).getRecipeInfo().getRecipeCode()) {
									infoView.searchPurchaseInfoView(list.get(n - 1), true);
									break;
								}
							}
							if(i == rlist.size()) {
								infoView.searchPurchaseInfoView(list.get(n - 1), false);
							}
						}
					}
				}
				
			} while (!menu.equals("0"));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

}
