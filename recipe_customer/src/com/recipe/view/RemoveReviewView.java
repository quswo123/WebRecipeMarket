package com.recipe.view;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.CustomerShare;
import com.recipe.vo.Review;

/**
 * @author Soojeong 나의 후기 삭제
 */
public class RemoveReviewView {
	private DataIO dio;
	private Scanner sc;

	public RemoveReviewView(DataIO dio) {
		this.dio = dio;
	}
	
	public void removeReview(List<Review> reviewList) throws IOException {
		System.out.print("후기 삭제를 원하는 번호를 입력해주세요 :");
		sc = new Scanner(System.in);
		int selectNum = sc.nextInt();
		Review r = reviewList.get(selectNum-1);
		r.setReviewDate(null);
		dio.sendMenu(Menu.REMOVE_REVIEW);
		dio.send(r);
		
		if ( dio.receive().equals("success") ) {
			SuccessView success = new SuccessView();
			String msg = "삭제 되었습니다.";
			success.reviewRemoveView(msg);
		} else {
			FailView fail = new FailView();
			fail.reviewRemoveView(dio.receive());
		}

	}//end method removeReview
	
} //end class RemoveReviewView
