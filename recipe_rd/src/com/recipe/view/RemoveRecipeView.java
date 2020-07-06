package com.recipe.view;

import java.io.IOException;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.RDShare;
import com.recipe.vo.RecipeInfo;

public class RemoveRecipeView {

	private Scanner sc;
	private DataIO dio;

	public RemoveRecipeView(DataIO dio) {
		this.dio = dio;
		sc = new Scanner(System.in);
	}
	public void showRemoveRecipeView(RecipeInfo info) {
		System.out.println("레시피삭제");
		System.out.println("삭제하시겠습니까?( y / n ) ");
		String result = (sc.nextLine());		//입력받은 레시피명을 setRecipe_name에 넣는다.
		if("y".equals(result.toLowerCase())) {		//입력받은 값을 소문자로 변환후 exit라면 break;
			try {
				dio.sendMenu(Menu.REMOVE_RECIPE);
				dio.sendId(RDShare.loginedId);
				dio.send(info);

				if(dio.receiveStatus().equals("success")) {		//rd로 로그인이 잘됬는지?
					SuccessView success = new SuccessView();
					success.removeRecipeView();
				}else {
					FailView fail = new FailView();
					fail.removeRecipeView(dio.receive());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
