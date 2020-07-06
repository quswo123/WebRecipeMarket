package com.recipe.io;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.recipe.vo.Customer;
import com.recipe.vo.Favorite;
import com.recipe.vo.Ingredient;
import com.recipe.vo.Point;
import com.recipe.vo.Postal;
import com.recipe.vo.Purchase;
import com.recipe.vo.PurchaseDetail;
import com.recipe.vo.RD;
import com.recipe.vo.RecipeInfo;
import com.recipe.vo.RecipeIngredient;
import com.recipe.vo.Review;

public class DataIO {
   private DataOutputStream dos;
   private DataInputStream dis;   
   
   public DataIO(DataOutputStream dos, DataInputStream dis) {
      super();
      this.dos = dos;
      this.dis = dis;
   }
   
   /**
    * "success" 메시지를 전송한다 (Server -> Client)
    * @throws IOException
    */
   public void sendSuccess() throws IOException{
      dos.writeUTF("success");
   }
   /**
    * "success" 메시지를 전송한다 (Server -> Client)
    * @param msg "success" 메시지 전송 후 보낼 메시지 내용
    * @throws IOException
    */
   public void sendSuccess(String msg) throws IOException {
      dos.writeUTF("success");
      dos.writeUTF(msg);
   }
   /**
    * "fail" 메시지를 전송한다 (Server -> Client)
    * @param msg "fail" 메시지 전송 후 보낼 메시지 내용
    * @throws IOException
    */
   public void sendFail(String msg) throws IOException {
      dos.writeUTF("fail");
      dos.writeUTF(msg);
   }
   /**
    * 메시지를 전송한다
    * @param str 보낼 메시지 내용
    * @throws IOException
    */
   public void send(String str) throws IOException {
      dos.writeUTF(str);
   }
   /**
    * 숫자를 전송한다
    * @param num 보낼 숫자
    * @throws IOException
    */
   public void send(int num) throws IOException {
	  dos.writeInt(num);
   }
   /**
    * 메뉴 번호를 전송한다
    * @param menuNo 보낼 메뉴 번호
    * @throws IOException
    */
   public void sendMenu(int menuNo) throws IOException {
      dos.writeInt(menuNo);
   }
   /**
    * ID를 전송한다
    * @param id 전송할 ID
    * @throws IOException
    */
   public void sendId(String id) throws IOException {
      dos.writeUTF(id);
   }
   /**
    * 패스워드를 전송한다
    * @param pwd 전송할 패스워드
    * @throws IOException 
    */
   public void sendPwd(String pwd) throws IOException {
      dos.writeUTF(pwd);
   }
   
   public void sendListString(List<String> str) throws IOException {
		dos.writeInt(str.size());
		for(String s : str) {
			send(s);
		}
	}
   
   /**
    * VO 객체 Customer의 내용들을 전송한다
    * @param c 정보를 전송할 Customer
    * @throws IOException
    */
   public void send(Customer c) throws IOException {
      dos.writeUTF(strNullCheck(c.getCustomerId()));
      dos.writeUTF(strNullCheck(c.getCustomerPwd()));
      dos.writeUTF(strNullCheck(c.getCustomerName()));
      dos.writeUTF(strNullCheck(c.getCustomerEmail()));
      dos.writeUTF(strNullCheck(c.getCustomerPhone()));
      send(c.getPostal()); 
      dos.writeUTF(strNullCheck(c.getCustomerAddr()));
   }
   /**
    * Customer List를 전송한다
    * @param list 보낼 Customer들을 가진 List
    * @throws IOException
    */
   public void sendCustomers(List<Customer> list) throws IOException {
      dos.writeInt(list.size());
      for(Customer c: list) send(c);
   }
   /**
    * VO 객체 Postal의 내용들을 전송한다
    * @param p 정보를 전송할 Postal
    * @throws IOException
    */
   public void send(Postal p) throws IOException {
      if(p == null) p = new Postal();
      dos.writeUTF(strNullCheck(p.getBuildingno()));
      dos.writeUTF(strNullCheck(p.getZipcode()));
      dos.writeUTF(strNullCheck(p.getCity()));
      dos.writeUTF(strNullCheck(p.getDoro()));
      dos.writeUTF(strNullCheck(p.getBuilding()));
   }
   /**
    * VO 객체 RD의 내용들을 전송한다
    * @param r 정보를 전송할 RD
    * @throws IOException
    */
   public void send(RD r) throws IOException {
      dos.writeUTF(strNullCheck(r.getRdId()));
      dos.writeUTF(strNullCheck(r.getRdPwd()));
      dos.writeUTF(strNullCheck(r.getRdManagerName()));
      dos.writeUTF(strNullCheck(r.getRdTeamName()));
      dos.writeUTF(strNullCheck(r.getRdPhone()));
   }
   /**
    * RD List를 전송한다
    * @param list 보낼 RD들을 가진 List
    * @throws IOException
    */
   public void sendRDList(List<RD> list) throws IOException {
	   dos.writeInt(list.size());
	   for(RD r : list) send(r);
   }
   /**
    * VO 객체 Purchase의 내용들을 전송한다
    * @param p 정보를 전송할 Purchase
    * @throws IOException
    */
   public void send(Purchase p) throws IOException {
      dos.writeInt(p.getPurchaseCode());
      dos.writeUTF(strNullCheck(p.getCustomerId()));
      if(p.getPurchaseDate() == null) dos.writeUTF("1900-00-00");
      else dos.writeUTF(strNullCheck(p.getPurchaseDate().toString()));
      send(p.getPurchaseDetail());
   }
   
   /**
    * int 전달받는다
    * @return 전달받은 메뉴 번호
    * @throws IOException
    */
   public int receiveInt() throws IOException{
      return dis.readInt();
   }
   
   /**
    * VO 객체 PurchaseDetail의 내용들을 전송한다
    * @param pd 정보를 전송할 PurchaseDetail
    * @throws IOException
    */
   public void send(PurchaseDetail pd) throws IOException {
      dos.writeInt(pd.getPurchaseCode());
      dos.writeInt(pd.getPurchaseDetailQuantity());
      send(pd.getRecipeInfo());
   }
   
   public void sendPurchase(List<Purchase> list) throws IOException{
      dos.writeInt(list.size());
      for(Purchase p : list) send(p);
   }
   /**
    * VO 객체 RecipeInfo의 내용들을 전송한다
    * @param ri 정보를 전송할 RecipeInfo
    * @throws IOException
    */
   public void send(RecipeInfo r) throws IOException {
      dos.writeInt(r.getRecipeCode());
      dos.writeUTF(strNullCheck(r.getRecipeName()));
      dos.writeUTF(strNullCheck(r.getRecipeSumm()));
      dos.writeDouble(r.getRecipePrice());
      dos.writeUTF(strNullCheck(r.getRecipeProcess()));
      send(r.getPoint());
      sendRecipeIngredients(r.getIngredients());
   }
   /**
    * VO 객체 RecipeInfo의 내용들을 전송한다
    * @param ri
    * @throws IOException
    */
   public void send(List<RecipeInfo> list) throws IOException {
      dos.writeInt(list.size());
      for(RecipeInfo i : list) send(i);
   }
   /**
    * VO 객체 RecipeIngredient의 내용들을 전송한다
    * @param ri
    * @throws IOException
    */
   public void send(RecipeIngredient ri) throws IOException {
      send(ri.getIngredient());
   }
   /**
    * VO 객체 Ingredient의 내용들을 전송한다
    * @param i 정보를 전송할 Ingredient
    * @throws IOException
    */
   public void send(Ingredient i) throws IOException {
      dos.writeInt(i.getIngCode());
      dos.writeUTF(strNullCheck(i.getIngName()));
   }
   /**
    * RecipeIngredient List를 전송한다
    * @param list 보낼 RecipeIngredient들을 가진 List
    * @throws IOException
    */
   public void sendRecipeIngredients(List<RecipeIngredient> list) throws IOException {
      if(list == null) { //RecipeIngredient 리스트가 null이면 size로 0을 보내 receiveReceipeInfo에서 list의 receive를 수행하지 않도록 한다.
         dos.writeInt(0);
         return;
      }
      dos.writeInt(list.size());
      for(RecipeIngredient i : list) send(i);
   }
   /**
    * VO 객체 Point의 내용들을 전송한다
    * @param p 정보를 전송할 Point
    * @throws IOException
    */
   public void send(Point p) throws IOException {
      dos.writeInt(p.getRecipeCode());
      dos.writeInt(p.getLikeCount());
      dos.writeInt(p.getDisLikeCount());
   }
   /**
    * VO 객체 Favorite의 내용들을 전송한다
    * @param f 정보를 전송할 Favorite
    * @throws IOException
    */
   public void send(Favorite f) throws IOException {
      dos.writeUTF(strNullCheck(f.getCustomerId()));
      send(f.getRecipeInfo());
   }
   
   /**
    * Favorite List를 전송한다
    * @param list 보낼 Favorite들을 가진 List
    * @throws IOException
    */
   public void sendFavorites(List<Favorite> list) throws IOException {
      dos.writeInt(list.size());
      for(Favorite f: list) send(f);
   }
   
   /**
    * VO 객체 Review의 내용들을 전송한다
    * @param r 정보를 전송할 Review
    * @throws IOException
    */
   public void send(Review r) throws IOException {
      dos.writeUTF(strNullCheck(r.getCustomerId()));
      dos.writeUTF(strNullCheck(r.getReviewComment()));
      dos.writeUTF(strNullCheck(r.getReviewDate().toString()));
      send(r.getRecipeInfo());
   }
   
   public void sendIngredientList(List<Ingredient> list) throws IOException {
	   dos.writeInt(list.size());
	   for(Ingredient i: list) send(i);
   }
   
   /**
    * Review List를 전송한다
    * @param list 보낼 Review들을 가진 List
    * @throws IOException
    */
   public void sendReviews(List<Review> list) throws IOException {
      dos.writeInt(list.size());
      for(Review r: list) send(r);
   }
   
   public String receiveStatus() throws IOException {
      return dis.readUTF();
   }
   /**
    * 메시지를 전달받는다
    * @return 전달된 메시지
    * @throws IOException
    */
   public String receive() throws IOException {
      return dis.readUTF();
   }
   /**
    * Client로부터 메뉴 번호를 전달받는다
    * @return 전달받은 메뉴 번호
    * @throws IOException
    */
   public int receiveMenu() throws IOException{
      return dis.readInt();
   }
   /**
    * Client로부터 ID를 전달받는다
    * @return 전달받은 ID
    * @throws IOException
    */
   public String receiveId() throws IOException {
      return dis.readUTF();
   }
   /**
    * Client로부터 패스워드를 전달받는다
    * @return 전달받은 패스워드
    * @throws IOException
    */
   public String receivePwd() throws IOException {
      return dis.readUTF();
   }
   /**
	 * VO 객체 Customer의 내용들을 전달받는다
	 * @return 전달받은 내용들로 구성한 Customer
	 * @throws IOException
	 */
	public Customer receiveCustomer() throws IOException {
		String id = dis.readUTF();
		String pwd = dis.readUTF();
		String name = dis.readUTF();
		String email = dis.readUTF();
		String phone = dis.readUTF();
		Postal postal = receivePostal();
		String addr = dis.readUTF();
		
		return new Customer(id, pwd, name, email, phone, postal, addr);
//		return new Customer(id, pwd, name, email, phone, null, addr);
	}
   /**
    * Customer List를 전달받는다
    * @return 전달받은 Customer들의 List
    * @throws IOException
    */
   public List<Customer> receiveCustomers() throws IOException {
      int size = dis.readInt();
      List<Customer> list = new ArrayList<>();
      for(int i=0; i<size; i++) list.add(receiveCustomer());
      
      return list;
   }
   /**
    * VO 객체 Postal의 내용들을 전달받는다
    * @return 전달받은 내용들로 구성한 Postal
    * @throws IOException
    */
   public Postal receivePostal() throws IOException {
      String buildingno = dis.readUTF();
      String zipcode = dis.readUTF();
      String city = dis.readUTF();
      String doro = dis.readUTF();
      String building = dis.readUTF();
      
      return new Postal(buildingno, zipcode, city, doro, building);
   }
   /**
    * VO 객체 RD의 내용들을 전달받는다
    * @return 전달받은 내용들로 구성한 RD
    * @throws IOException
    */
   public RD receiveRd() throws IOException {
      String rdId = dis.readUTF();
      String rdPwd = dis.readUTF();
      String rdManagerName = dis.readUTF();
      String rdTeamName = dis.readUTF();
      String rdPhone = dis.readUTF();
      
      return new RD(rdId, rdPwd, rdManagerName, rdTeamName, rdPhone);
   }
   /**
    * RD List를 전달받는다
    * @return 전달받은 RD들의 List
    * @throws IOException
    */
   public List<RD> receiveRDList() throws IOException {
	   List<RD> result = new ArrayList<RD>();
	   int size = dis.readInt();
	   for(int i = 0; i < size; i++) result.add(receiveRd());
	   
	   return result;
   }
   /**
    * VO 객체 Purchase의 내용들을 전달받는다
    * @return 전달받은 내용들로 구성한 Purchase
    * @throws IOException
    */
   public Purchase receivePurchase() throws IOException, ParseException {
      int purchaseCode = dis.readInt();
      String customerId = dis.readUTF();
      Date purchaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(dis.readUTF());
      PurchaseDetail purchaseDetail = receivePurchaseDetail();
      
      return new Purchase(purchaseCode, customerId, purchaseDate, purchaseDetail);
   }
   /**
    * VO 객체 PurchaseDetail의 내용들을 전달받는다
    * @return 전달받은 내용들로 구성한 PurchaseDetail
    * @throws IOException
    */
   public PurchaseDetail receivePurchaseDetail() throws IOException {
      int purchaseCode = dis.readInt();
      int purchaseDetailQuantity = dis.readInt();
      RecipeInfo recipeInfo = receiveRecipeInfo();
      
      return new PurchaseDetail(purchaseCode, purchaseDetailQuantity, recipeInfo);
   }
   
   /**
    * Purchase객체를 전달받는다
    * @return
    * @throws IOException
    * @throws ParseException
    */
   public List<Purchase> receivePurchaseList() throws IOException, ParseException{
      int size = dis.readInt();
      List<Purchase> list = new ArrayList<>();
      for(int i=0; i<size; i++) list.add(receivePurchase());
      
      return list;
   }
   
   /**
    * RecipeInfo List를 전달받는다
    * @return 전달받은 RecipeIngredient들의 List
    * @throws IOException
    */
   public List<RecipeInfo> receiveRecipeInfos() throws IOException {
      int size = dis.readInt();
      List<RecipeInfo> list = new ArrayList<RecipeInfo>();
      for(int i = 0; i < size; i++) list.add(receiveRecipeInfo());
      
      return list;
   }
   
   
   /**
    * VO 객체 RecipeInfo의 내용들을 전달받는다
    * @return 전달받은 내용들로 구성한 RecipeInfo
    * @throws IOException
    */
   public RecipeInfo receiveRecipeInfo() throws IOException {
      int recipeCode = dis.readInt();
      String recipeName = dis.readUTF();
      String recipeSumm = dis.readUTF();
      double recipePrice = dis.readDouble();
      String recipeProcess = dis.readUTF();
      Point point = receivePoint();
      List<RecipeIngredient> ingredients = receiveRecipeIngredients(); 
      
      return new RecipeInfo(recipeCode, recipeName, recipeSumm, recipePrice, recipeProcess, point, ingredients);
   }
   /**
    * VO 객체 RecipeIngredient의 내용들을 전달받는다
    * @return 전달받은 내용들로 구성한 RecipeIngredient
    * @throws IOException
    */
   public RecipeIngredient receiveRecipeIngredient() throws IOException {
      return new RecipeIngredient(receiveIngredient());
   }
   /**
    * VO 객체 Ingredient의 내용들을 전달받는다
    * @return 전달받은 내용들로 구성한 Ingredient
    * @throws IOException
    */
   public Ingredient receiveIngredient() throws IOException {
      int ingCode = dis.readInt();
      String ingName = dis.readUTF();
      
      return new Ingredient(ingCode, ingName);
   }
   /**
    * RecipeIngredient List를 전달받는다
    * @return 전달받은 RecipeIngredient들의 List
    * @throws IOException
    */
   public List<RecipeIngredient> receiveRecipeIngredients() throws IOException {
      int size = dis.readInt();
      List<RecipeIngredient> list = new ArrayList<RecipeIngredient>();
      for(int i = 0; i < size; i++) list.add(receiveRecipeIngredient());
      
      return list;
   }
   /**
    * VO 객체 Point의 내용들을 전달받는다
    * @return 전달받은 내용들로 구성한 Point
    * @throws IOException
    */
   public Point receivePoint() throws IOException {
      int recipeCode = dis.readInt();
      int likeCount = dis.readInt();
      int disLikeCount = dis.readInt();
      
      return new Point(recipeCode, likeCount, disLikeCount);
   }
   
   /**
    * String 타입의 멤버 변수가 null이면 빈 문자열을 보낼수 있도록 처리하기 위한 메소드
    * @param str null인지 확인할 문자열
    * @return null이면 빈 문자열, null이 아니면 str을 그대로 return
    */
   public String strNullCheck(String str) {
      return str != null ? str : "";
   }
   
   public void close() {
      if(dos != null)
      try {
         dos.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
      
      if(dis != null)
      try {
         dis.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   
   /**
    * VO 객체 Review의 내용들을 전달받는다
    * @return 전달받은 내용들로 구성한 Review
    * @throws IOException, ParseException
    */
   public Review receiveReview() throws IOException, ParseException {
      String customerId = dis.readUTF();
      String reviewComment = dis.readUTF();
      Date reviewDate = new SimpleDateFormat("yyyy-MM-dd").parse(dis.readUTF());
      RecipeInfo recipeInfo = receiveRecipeInfo();
      
      return new Review(customerId, reviewComment, reviewDate, recipeInfo);
   }
   
   /**
    * Review List를 전달받는다
    * @return 전달받은 Review들의 List
    * @throws IOException, ParseException
    */
   public List<Review> receiveReviews() throws IOException, ParseException {
      int size = dis.readInt();
      List<Review> list = new ArrayList<>();
      for(int i=0; i<size; i++) list.add(receiveReview());
      
      return list;
   }

   /**
    * VO 객체 Favorite의 내용들을 전달받는다
    * @return 전달받은 내용들로 구성한 Favorite
    * @throws IOException, ParseException
    */
   public Favorite receiveFavorite() throws IOException {
      String customerId = dis.readUTF();
      RecipeInfo recipeInfo = receiveRecipeInfo();
      
      return new Favorite (customerId, recipeInfo);
   }
   
   /**
    * Favorite List를 전달받는다
    * @return 전달받은 Favorite들의 List
    * @throws IOException
    */
   public List<Favorite> receiveFavorites() throws IOException {
      int size = dis.readInt();
      List<Favorite> list = new ArrayList<>();
      for(int i=0; i<size; i++) list.add(receiveFavorite());
      
      return list;
   }
   
   public List<String> receiveListString() throws IOException {
		int size = dis.readInt();
		List<String> list = new ArrayList<>();
		for(int i=0; i<size; i++) list.add(receive());
		
		return list;
	}

   public List<Ingredient> receiveIngredientList() throws IOException {
	   int size = dis.readInt();
	   List<Ingredient> list = new ArrayList<>();
	   for(int i=0; i<size; i++) list.add(receiveIngredient());

	   return list;
   }
   
}

