/*
 * 내 정보 보기
 * @author 영민
 */
package com.recipe.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.share.CustomerShare;
import com.recipe.vo.Customer;

public class CustomerInfoView {
	private DataIO dataio;
	private Scanner sc;

	/*
	 * IO연결
	 */
	public CustomerInfoView(DataIO dio) throws UnknownHostException, IOException {
		sc = new Scanner(System.in);
		dataio = dio;
	}

	/*
	 * 입력받은 ID를 통해 고객정보 보여주기
	 */
	public void viewMyAccount() {
		
		try {
			// 내정보보기를 요청
			dataio.sendMenu(Menu.CUSTOMER_INFO);
			// Customer_status를 송신
			dataio.sendId(CustomerShare.loginedId);
			// 응답
			if ("success".equals(dataio.receiveStatus())) {
				Customer receiveCustomer = dataio.receiveCustomer();
				System.out.println("이름 :" + receiveCustomer.getCustomerName());
				System.out.println("ID :" + receiveCustomer.getCustomerId());
				System.out.println("비밀번호 : " + receiveCustomer.getCustomerPwd());
				System.out.println("e-mail :" + receiveCustomer.getCustomerEmail());
				System.out.println(
						"주소 :" + receiveCustomer.getPostal().getCity() + receiveCustomer.getPostal().getDoro()+receiveCustomer.getCustomerAddr());
				System.out.println("핸드폰 번호 :" + receiveCustomer.getCustomerPhone());
			} else {
				String failMsg = dataio.receive();
				System.out.println(failMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 메뉴 화면 구현, 1번 선택 시 modifyinfoview 연결, 2번 선택 시 deleteinfoview 연결, 3."-" 선택 시
	 * 이전화면
	 *
	 */
	public void customerInfoMenu() {
		String menu = null;
		
		try {
			do {
				viewMyAccount();
				System.out.println("1.내 정보 수정하기  2.탈퇴하기 0.이전화면 ");
				System.out.print("메뉴 번호를 입력하세요 : ");
				menu = sc.nextLine();
				if (menu.equals("1")) {
					ModifyCustomerInfoView modifyinfoview = new ModifyCustomerInfoView(dataio);
					modifyinfoview.modifyMyAccount();

				} else if (menu.equals("2")) {
					DeleteCustomerInfoView deleteinfoview = new DeleteCustomerInfoView(dataio);
					deleteinfoview.deleteMyAccount();
					menu = "0";
				}
			} while (!menu.equals("0"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//		CustomerInfoView view = null;
//		try {
//			view = new CustomerInfoView();
//			view.viewMyAccount();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (view != null)
//				try {
//					view.s.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//		}
//	}
}
