package com.recipe.control;

import java.util.ArrayList;
import java.util.List;

import com.recipe.exception.AddException;
import com.recipe.exception.DuplicatedException;
import com.recipe.exception.FindException;
import com.recipe.exception.ModifyException;
import com.recipe.exception.RemoveException;
import com.recipe.service.AccountService;
import com.recipe.service.AdminAccountService;
import com.recipe.service.FavoriteService;
import com.recipe.service.PostService;
import com.recipe.service.PurchaseService;
import com.recipe.service.RDAccountService;
import com.recipe.service.RecipeService;
import com.recipe.service.ReviewService;
import com.recipe.vo.Customer;
import com.recipe.vo.Favorite;
import com.recipe.vo.Ingredient;
import com.recipe.vo.Point;
import com.recipe.vo.Postal;
import com.recipe.vo.Purchase;
import com.recipe.vo.RD;
import com.recipe.vo.RecipeInfo;
import com.recipe.vo.Review;

public class RecipeMarketControl {
	private static RecipeMarketControl control = new RecipeMarketControl();

	private AccountService accountService;
	private FavoriteService favoriteService;
	private PostService postService;
	private PurchaseService purchaseService;
	private RDAccountService rdAccountService;
	private RecipeService recipeService;
	private ReviewService reviewService;
	private AdminAccountService adminAccountService;

	private RecipeMarketControl() {
		accountService = new AccountService();
		favoriteService = new FavoriteService();
		postService = new PostService();
		purchaseService = new PurchaseService();
		rdAccountService = new RDAccountService();
		recipeService = new RecipeService();
		reviewService = new ReviewService();
		adminAccountService = new AdminAccountService();
	}

	public static RecipeMarketControl getInstance() {
		return control;
	}

	/**
	 * Customer 클라이언트의 로그인 절차를 위한 Control 메소드
	 * 
	 * @param customerId  Customer 클라이언트에서 전달받은 아이디
	 * @param customerPwd Customer 클라이언트에서 전달받은 패스워드
	 * @throws FindException
	 * @author 최종국
	 */
	public void loginToAccount(String customerId, String customerPwd) throws FindException {
		accountService.login(customerId, customerPwd);
	}

	/**
	 * R&D 클라이언트의 로그인 절차를 위한 Control 메소드
	 * 
	 * @param rdId  R&D 클라이언트에서 전달받은 아이디
	 * @param rdPwd R&D 클라이언트에서 전달받은 패스워드
	 * @throws FindException
	 * @author 최종국
	 */
	public void loginToRd(String rdId, String rdPwd) throws FindException {
		rdAccountService.login(rdId, rdPwd);
	}

	/**
	 * Admin 클라이언트의 로그인 절차를 위한 Control 메소드
	 * 
	 * @param adminId  Admin 클라이언트에서 전달받은 아이디
	 * @param adminPwd Admin 클라이언트에서 전달받은 패스워드
	 * @throws FindException
	 * @author 최종국
	 */
	public void loginToAdmin(String adminId, String adminPwd) throws FindException {
		adminAccountService.login(adminId, adminPwd);
	}

	/**
	 * 구매하기 메소드
	 * 
	 * @param p
	 * @throws AddException
	 * @author 변재원
	 */
	public void buyRecipe(Purchase p) throws AddException {
		purchaseService.buy(p);
	}

	/**
	 * 구매목록 메소드
	 * 
	 * @param List<Purchase>
	 * @throws AddException
	 * @author 변재원
	 */
	public List<Purchase> viewMyPurchase(String customerId) throws FindException {
		return purchaseService.findById(customerId);
	}

	/**
	 * 
	 * @param f
	 * @throws AddException
	 * @throws DuplicatedException
	 * @author 고수정
	 */
	public void addFavorite(Favorite f) throws AddException, DuplicatedException {
		favoriteService.add(f);
	}

	/**
	 * 
	 * @param customerId
	 * @throws FindException
	 * @author 고수정
	 */
	public List<Favorite> viewFavorite(String customerId) throws FindException {
		List<Favorite> list = new ArrayList<>();
		list = favoriteService.findById(customerId);
		return list;
	}

	/**
	 * 
	 * @param f
	 * @throws RemoveException
	 * @author 고수정
	 */
	public void removeFavorite(Favorite f) throws RemoveException {
		favoriteService.remove(f);
	}

	/**
	 * 
	 * @param recipeCode
	 * @throws FindException
	 * @author 고수정
	 */
	public List<Review> viewRecipeReview(int recipeCode) throws FindException {
		List<Review> list = new ArrayList<>();
		list = reviewService.findByCode(recipeCode);
		return list;
	}

	/**
	 * 
	 * @param r
	 * @throws AddException
	 * @throws DuplicatedException
	 * @author 고수정
	 */
	public void addReview(Review r) throws AddException, DuplicatedException {
		reviewService.add(r);
	}

	/**
	 * 
	 * @param customerId
	 * @throws FindException
	 * @author 고수정
	 */
	public List<Review> viewMyReview(String customerId) throws FindException {
		List<Review> list = new ArrayList<>();
		list = reviewService.findById(customerId);
		return list;
	}
	/**
	 * 
	 * @param Review
	 * @throws RemoveException
	 * @author 고수정
	 */
	public void remove(Review review) throws RemoveException {
		reviewService.remove(review);
	}

	/**
	 * 
	 * @param recipeCode
	 * @return
	 * @throws FindException
	 * @author 이혜림
	 */
	public RecipeInfo searchByCode(int recipeCode) throws FindException {
		return recipeService.findByCode(recipeCode);
	}

	/**
	 * 
	 * @param recipeName
	 * @return
	 * @throws FindException
	 * @author 이혜림
	 */
	public List<RecipeInfo> searchByName(String recipeName) throws FindException {
		return recipeService.findByName(recipeName);
	}

	/**
	 * 
	 * @param ingName
	 * @return
	 * @throws FindException
	 * @author 이혜림
	 */
	public List<RecipeInfo> searchByIngName(List<String> ingName) throws FindException {
		return recipeService.findByIngName(ingName);
	}

	/*
	 * Control에서 accountService의 add 호출
	 * 
	 * @param Customer C
	 * 
	 * @author 영민
	 */
	public void addAccount(Customer c) throws AddException, DuplicatedException {
		accountService.add(c);
	}

	/*
	 * Control에서 accountService의 findById호출
	 * 
	 * @param String id
	 * 
	 * @author 영민
	 */
	public Customer viewMyAccount(String id) throws FindException {
		return accountService.findById(id);
	}

	/*
	 * Control에서 accountService의 modify호출
	 * 
	 * @param Customer c
	 * 
	 * @author 영민
	 */
	public void modifyMyAccount(Customer c) throws ModifyException {
		accountService.modify(c);
	}

	/*
	 * Control에서 accountService의 removeMyAccount 호출
	 * 
	 * @param Cusotomer c
	 * 
	 * @author 영민
	 */
	public void removeMyAccount(Customer c) throws RemoveException {
		accountService.remove(c);
	}
	/*
	 * control에서 PostService의 findMyDoro 호출
	 * @param String doro
	 * @author 영민
	 */
	public List<Postal> searchByDoro(String doro) throws FindException{
		PostService service = new PostService();
		return service.findByDoro(doro);
	}

	/**
	 * 추천 레시피 탐색 절차를 위한 메소드
	 * @return 추천 레시피 정보를 가진 RecipeInfo
	 * @throws FindException
	 * @author 최종국
	 */
	public RecipeInfo searchRecommended() throws FindException {
		return recipeService.findRecommended();
	}
	

	/**
	 * 포인트 수정 절차를 위한 메소드
	 * @param p 수정할 레시피 코드와 좋아요, 싫어요 개수를 포함한 Point 객체
	 * @throws ModifyException
	 * @author 최종국
	 */
	public void modifyPoint(Point p) throws ModifyException{
		recipeService.modifyPoint(p);
	}
	
	/**
	 * R&D 계정 전체 조회 절차를 위한 메소드
	 * @return RD 객체 리스트
	 * @throws FindException
	 * @author 최종국
	 */
	public List<RD> viewAllRd() throws FindException {
		return rdAccountService.findAll();
	}
	
	/**
	 * R&D 계정 추가 절차를 위한 메소드
	 * @param r 추가할 R&D 계정 정보를 가진 RD
	 * @throws AddException
	 * @throws DuplicatedException 아이디가 중복되면 발생
	 * @author 최종국
	 */
	public void addRd(RD r) throws AddException, DuplicatedException {
		rdAccountService.add(r);
	}
	
	/**
	 * R&D 계정 수정 절차를 위한 메소드
	 * @param r 수정할 R&D 계정 정보를 가진 RD
	 * @throws ModifyException 수정하려는 R&D 계정이 존재하지 않으면 발생
	 * @author 최종국
	 */
	public void modifyRd(RD r) throws ModifyException {
		rdAccountService.modify(r);
	}
	
	/**
	 * R&D 계정 삭제(비활성화) 절차를 위한 메소드
	 * @param rdId 삭제할 아이디
	 * @throws RemoveException 삭제하려는 R&D 계정이 존재하지 않으면 발생
	 */
	public void removeRd(String rdId) throws RemoveException {
		rdAccountService.remove(rdId);
	}
	public void addRecipe(String rdId, RecipeInfo recipeInfo, String ingInfo, List<Ingredient> ingList, String process) throws DuplicatedException {
		recipeService.addRecipe(rdId, recipeInfo, ingInfo, ingList, process);
	}
	public void modifyRecipe(String rdId, RecipeInfo recipeInfo, String ingInfo, List<Ingredient> ingList, String process) throws ModifyException {
		recipeService.modifyRecipe(rdId, recipeInfo, ingInfo, ingList, process);
	}
	public void removeRecipe(String rdId, RecipeInfo recipeInfo) throws ModifyException {
		recipeService.removeRecipe(rdId, recipeInfo);
	}
	public List<RecipeInfo> viewAllRecipe() throws FindException{
		return recipeService.findAll();
	}
	
	public RD viewRdAccount(String rdId) throws FindException {
		return rdAccountService.findById(rdId);
	}
}
