package com.recipe.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;
import com.recipe.vo.Customer;
import com.recipe.vo.Postal;

public class RegisterView {
	private DataIO dataio;
	private Scanner sc;

	/*
	 * IO연결
	 */
	public RegisterView(DataIO dio) throws UnknownHostException, IOException {
		sc = new Scanner(System.in);
		dataio = dio;
	}

	public void addMyAccount() {
		Customer c = addInfoMenu();
		if (c != null) {
			try {
				// 회원가입
				dataio.sendMenu(Menu.CUSTOMER_REGISTER);
				// Customer 정보를 송신
				dataio.send(c);
				if ("success".equals(dataio.receiveStatus())) {
					System.out.println("회원가입을 축하합니다!");
				} else {
					String failMsg = dataio.receive();
					System.out.println(failMsg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Customer addInfoMenu() {
		Customer c = new Customer();
		System.out.println("회원가입");
		System.out.print("1.아이디를 입력해주세요  : ");
		String customerId = sc.nextLine();
		if (!"".equals(customerId)) {
			c.setCustomerId(customerId);
		}
		System.out.print("2.이름을 입력해주세요  : ");
		String customerName = sc.nextLine();
		if (!"".equals(customerName)) {
			c.setCustomerName(customerName);
		}
		System.out.print("3.비밀번호를 입력해주세요  : ");
		String customerPwd = sc.nextLine();
		if (!"".equals(customerPwd)) {
			c.setCustomerPwd(customerPwd);
		}
		System.out.print("4.이메일을 입력해주세요  : ");
		String customerEmail = sc.nextLine();
		if (!"".equals(customerEmail)) {
			c.setCustomerEmail(customerEmail);
		}
		System.out.print("5. 도로명을 입력해주세요 : ");
		String doro = sc.nextLine();
		if (!"".equals(doro)) {
			try {
				dataio.sendMenu(Menu.SEARCH_POSTAL);
				dataio.send(doro);
				if ("success".equals(dataio.receiveStatus())) {
					int size = Integer.parseInt(dataio.receive());
					List<Postal> list = new ArrayList<>();
					for (int i = 0; i < size; i++) {
						Postal p = dataio.receivePostal();
						list.add(p);
						System.out.println((i + 1) + ": " +p.getZipcode() + ", " + p.getCity() + "" + p.getDoro()+ p.getBuilding());
					}
					System.out.print("번호를 선택하세요 : ");
					int index = Integer.parseInt(sc.nextLine()) - 1;
					if (index < 0 || index >= list.size()) {
						System.out.println("잘못입력하셨습니다");
						return null;
					}
					Postal modifyPostal = list.get(index);
					c.setPostal(modifyPostal);
				} else {
					String failMsg = dataio.receive();
					System.out.println(failMsg);
					return null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		System.out.print("6.상세주소를 입력해주세요  : ");
		String customerAddr = sc.nextLine();
		if (!"".equals(customerAddr)) {
			c.setCustomerAddr(customerAddr);
		}
		
		System.out.print("7.핸드폰 번호를 입력해주세요  : ");
		String customerPhone = sc.nextLine();
		if (!"".equals(customerPhone)) {
			c.setCustomerPhone(customerPhone);
		}

		return c;
	}
}
