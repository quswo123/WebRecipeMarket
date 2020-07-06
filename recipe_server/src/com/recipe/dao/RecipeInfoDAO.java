package com.recipe.dao;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.recipe.exception.DuplicatedException;
import com.recipe.exception.FindException;
import com.recipe.exception.ModifyException;
import com.recipe.jdbc.MyConnection;
import com.recipe.vo.Ingredient;
import com.recipe.vo.Point;
import com.recipe.vo.RecipeInfo;
import com.recipe.vo.RecipeIngredient;

public class RecipeInfoDAO {
	public RecipeInfo selectByCode(int recipeCode) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();

		} catch (ClassNotFoundException | SQLException e) {

		}
		String selectByCodeSQL = "SELECT RI.RECIPE_CODE, RIN.RECIPE_NAME, RIN.RECIPE_SUMM, RIN.RECIPE_PRICE, RI.ing_code, ING.ING_NAME, RIN.recipe_process, PT.LIKE_COUNT, PT.DISLIKE_COUNT\r\n" + 
				"FROM RECIPE_INGREDIENT RI \r\n" + 
				"LEFT JOIN RECIPE_INFO RIN ON RI.recipe_code = RIN.recipe_code\r\n" + 
				"JOIN INGREDIENT ING ON RI.ing_code = ING.ing_code\r\n" + 
				"left JOIN POINT PT ON RI.RECIPE_CODE = PT.RECIPE_CODE\r\n" + 
				"WHERE RIN.recipe_Code IN\r\n" + 
				"(select recipe_code FROM recipe_info WHERE recipe_code = ? AND recipe_status = 1)";
		List<RecipeIngredient> ingList = new ArrayList<>();
		RecipeInfo recipeInfo = new RecipeInfo();
		int prevCode = 0; //
		try {
			pstmt = con.prepareStatement(selectByCodeSQL);
			pstmt.setInt(1, recipeCode);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int rCode = rs.getInt("recipe_code");
				int ingCode = rs.getInt("ing_code");
				String ingName = rs.getString("ing_name");
				Ingredient ingredient = new Ingredient(ingCode, ingName);
				RecipeIngredient recipeIng = new RecipeIngredient(ingredient);
				//코드랑 이름 값 (Ingredient) recipeIng 에 넣어주고 리스트에 애드해줌
				ingList.add(recipeIng);
				//코드값이 바뀔떄 recipeInfo 에 값 넣어주기
				if (prevCode != rCode) {
					recipeInfo.setRecipeCode(rCode);
					recipeInfo.setRecipeName(rs.getString("recipe_name"));
					recipeInfo.setRecipePrice(rs.getInt("recipe_price"));
					recipeInfo.setRecipeSumm(rs.getString("recipe_summ"));
					recipeInfo.setRecipeProcess(rs.getString("recipe_process"));
					recipeInfo.setIngredients(ingList);
					Point pt = new Point(rCode, rs.getInt("like_count"), rs.getInt("dislike_count"));
					recipeInfo.setPoint(pt);
				}
			}
			if (recipeInfo.getRecipeName() == null) {
				throw new FindException("찾은 레시피가 없습니다");
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			MyConnection.close(rs, pstmt, con);
		}

		return recipeInfo;
	}
	public List<RecipeInfo> selectByName(String recipeName) throws FindException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<RecipeInfo> recipeInfo = new ArrayList<>();
		try {
			con = MyConnection.getConnection();

		} catch (ClassNotFoundException | SQLException e) {
			throw new FindException(e.getMessage());
		}
		String selectByNameSQL = "SELECT RIN.Recipe_code,RIN.RECIPE_NAME, RIN.RECIPE_SUMM, RIN.RECIPE_PRICE, RI.ing_code, ING.ING_NAME, RIN.recipe_process,PT.LIKE_COUNT, PT.DISLIKE_COUNT\r\n" + 
				"FROM RECIPE_INGREDIENT RI \r\n" + 
				"LEFT JOIN RECIPE_INFO RIN ON RI.recipe_code = RIN.recipe_code\r\n" + 
				"LEFT JOIN INGREDIENT ING ON RI.ing_code = ING.ing_code\r\n" + 
				"LEFT JOIN POINT PT ON RIN.RECIPE_CODE = PT.RECIPE_CODE\r\n" + 
				"WHERE rin.recipe_name LIKE ? AND RIN.RECIPE_STATUS = 1";
		try {
			pstmt = con.prepareStatement(selectByNameSQL);
			pstmt.setString(1, "%" + recipeName + "%");
			rs = pstmt.executeQuery();
			List<RecipeIngredient> ingList = null;	
			int prevCode = 0;
			while(rs.next()) {				
				int rCode = rs.getInt("recipe_code");
				//코드값이 바뀔떄 recipeInfo 객체 생성하고 값넣어줌		
				if (prevCode != rCode) {					
					RecipeInfo recipeInfo2 = new RecipeInfo();
					ingList = new ArrayList<>();
					recipeInfo2.setIngredients(ingList);
					recipeInfo2.setRecipeCode(rCode);
					recipeInfo2.setRecipeName(rs.getString("recipe_name"));
					recipeInfo2.setRecipePrice(rs.getInt("recipe_price"));
					recipeInfo2.setRecipeSumm(rs.getString("recipe_summ"));
					recipeInfo2.setRecipeProcess(rs.getString("recipe_process"));
					recipeInfo2.setIngredients(ingList);					
					Point pt = new Point(rCode, rs.getInt("like_count"), rs.getInt("dislike_count"));
					recipeInfo2.setPoint(pt);
					recipeInfo.add(recipeInfo2);

					prevCode = rCode;
				}

				int ingCode = rs.getInt("ing_code");
				String ingName = rs.getString("ing_name");
				Ingredient ingredient = new Ingredient(ingCode, ingName);
				RecipeIngredient recipeIng = new RecipeIngredient(ingredient);				
				ingList.add(recipeIng);

			}
			if (recipeInfo.size() == 0) {
				throw new FindException("찾은 레시피가 없습니다");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(rs, pstmt, con);
		}

		return recipeInfo;
	}
	public void insert(String rdId, RecipeInfo recipe_InfoVo,String ingInfo ,List<Ingredient> ingList, String process) throws DuplicatedException{
		//입력받아온 recipe_InfoVo,ingList
		Connection con = null; // DB연결된 상태(세션)을 담은 객체
		PreparedStatement pstmt = null;  // SQL 문을 나타내는 객체
		ResultSet rs = null;  // 쿼리문을 날린것에 대한 반환값을 담을 객체

		String ing_name = "";
		for(Ingredient ingredientVO : ingList) {		//ingList에 있는 객체들을 ingredientVO에 넣으면서 반복문 실행
			ing_name +=", '" + ingredientVO.getIngName() + "'";			//", '재료1', '재료2', '재료3', ....식으로 문자넣음. // 사과1개
		}
		String quary = "SELECT COUNT(1) AS CNT FROM RECIPE_INFO WHERE RECIPE_NAME = ?";		//레시피명이 존재하는것이라면 1이나옴. 없다면 0.
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(quary);
			pstmt.setString(1,  recipe_InfoVo.getRecipeName());

			rs = pstmt.executeQuery();

			int countFlag = 0;
			while(rs.next()) {		//쿼리문을 돌렸을때 받아온 컬럼의 값이 있을때 true
				countFlag = rs.getInt(1);		//있다면 1을 countFlag에 넣는다.
			}
			if(0 < countFlag) {
				throw new DuplicatedException("이미 존재하는 레시피입니다.");
			}
			rs.close();

			for(Ingredient ingredientVO : ingList) {		//ingList에 입력받아놨던 재료명(ing_name)을 하나씩 검사하면서 테이블에 재료명이 있으면 무시하고 없으면 생성하는 부분
				quary = "MERGE INTO INGREDIENT " + 
						"USING DUAL " + 
						"   ON (ING_NAME = ?) " + 
						"WHEN NOT MATCHED THEN " + 
						"    INSERT (ING_CODE, ING_NAME) " + 
						"VALUES ((SELECT MAX(ING_CODE) + 1 FROM INGREDIENT), ?)";
				pstmt = con.prepareStatement(quary);
				pstmt.setString(1, ingredientVO.getIngName());
				pstmt.setString(2, ingredientVO.getIngName());
				pstmt.executeUpdate();
				pstmt.close();
			}

			List<Ingredient> ing_codeList = new ArrayList<Ingredient>();
			Ingredient ingredientVo = new Ingredient();

			//,빼고 ING_CODE와 ING_NAME값을 셀렉트 해오는 쿼리
			quary = "SELECT ING_CODE, ING_NAME FROM INGREDIENT WHERE ING_NAME IN ( "
					+ ing_name.substring(1, ing_name.length()) + ")";	//", '재료1', '재료2', '재료3' "식으로 문자넣음.

			pstmt = con.prepareStatement(quary);
			rs = pstmt.executeQuery();		//쿼리 실행시 재료코드값과 재료이름을 받아옴

			while(rs.next()) {
				ingredientVo = new Ingredient();
				ingredientVo.setIngCode(rs.getInt(1));		//첫번째 값은 재료코드값으로
				ingredientVo.setIngName(rs.getString(2));		//두번째값은 재료이름값으로
				ing_codeList.add(ingredientVo);		//인덱스 하나하나 ing_codeList에 넣어준다.
			}
			rs.close();
			pstmt.close();

			quary = "SELECT RECIPE_CODE_SEQ.NEXTVAL FROM DUAL";		//레시피코드의시퀀스넘버 값을 구해온다.
			pstmt = con.prepareStatement(quary);
			rs = pstmt.executeQuery();

			while(rs.next()){
				recipe_InfoVo.setRecipeCode(rs.getInt(1));//setRecipe_code메소드를 이용해서 recipe_InfoVo의 Recipe_code에 넣어준다
			}
			recipe_InfoVo.setRecipeProcess("c:/project/recipe_server/resource/recipeProcess/" + recipe_InfoVo.getRecipeCode() + ".txt");		//recipeprocess에 레시피코드를 파일명으로 한 파일생성경로를 넣어준다.
			//Mac전용 경로
//			recipe_InfoVo.setRecipeProcess("/Users/elannien/project/recipe_server/resource/recipeProcess/" + recipe_InfoVo.getRecipeCode() + ".txt");		//recipeprocess에 레시피코드를 파일명으로 한 파일생성경로를 넣어준다.
			rs.close();
			pstmt.close();
			quary = "INSERT INTO RECIPE_INFO VALUES(?, ?, ?, ?, ?, ?, ?)";		//RECIPE_INFO 에 값들을 넣어주는 쿼리문
			pstmt = con.prepareStatement(quary);
			pstmt.setInt(1, recipe_InfoVo.getRecipeCode());
			pstmt.setString(2, recipe_InfoVo.getRecipeName());
			pstmt.setString(3, recipe_InfoVo.getRecipeSumm());
			pstmt.setDouble(4, recipe_InfoVo.getRecipePrice());
			pstmt.setString(5, recipe_InfoVo.getRecipeProcess());
			pstmt.setString(6, ("1"));		//status는 1로 고정
			pstmt.setString(7, rdId);		//일단 rd아이디는 id9로 고정

			pstmt.executeUpdate();
			pstmt.close();

			fileOutput(recipe_InfoVo.getRecipeProcess(), ingInfo + "\n" + process);

			quary = "INSERT INTO POINT VALUES(?, 0, 0)";		//좋아요싫어요 초기값설정해주는 쿼리문
			pstmt = con.prepareStatement(quary);
			pstmt.setInt(1, recipe_InfoVo.getRecipeCode());

			pstmt.executeUpdate();
			pstmt.close();

			for(Ingredient ingredientVO : ing_codeList) {			//ing_codeList에 있는것들을 ingredientVO에 넣으면서 반복문 돌림.
				quary = "INSERT INTO RECIPE_INGREDIENT VALUES (?, ?)";		//리세피코드, 재료코드, 용량 insert 해주는 쿼리
				pstmt = con.prepareStatement(quary);
				pstmt.setInt(1, recipe_InfoVo.getRecipeCode());
				pstmt.setInt(2,  ingredientVO.getIngCode());
				pstmt.executeUpdate();
				pstmt.close();
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally{
			// DB 연결을 종료한다.
			try{
				MyConnection.close(rs, pstmt, con);
			}catch(Exception e){
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	public void update(String rdId, RecipeInfo recipe_InfoVo,String ingInfo ,List<Ingredient> ingList, String process) throws ModifyException {
		//입력받아온 recipe_InfoVo,ingList
		Connection con = null; // DB연결된 상태(세션)을 담은 객체
		PreparedStatement pstmt = null;  // SQL 문을 나타내는 객체
		ResultSet rs = null;  // 쿼리문을 날린것에 대한 반환값을 담을 객체

		//-----------------------ingList에서 IngName,IngCpcty를 나눠줘야 함.
		String ing_name = "";
		for(Ingredient ingredientVO : ingList) {		//ingList에 있는 객체들을 ingredientVO에 넣으면서 반복문 실행
			ing_name +=", '" + ingredientVO.getIngName() + "'";			//", '재료1', '재료2', '재료3', ....식으로 문자넣음. // 사과1개
		}
		//-----------------------
		String quary = "SELECT COUNT(1) AS CNT FROM RECIPE_INFO WHERE RECIPE_NAME = ?";		//레시피명이 존재하는것이라면 1이나옴. 없다면 0.
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(quary);
			pstmt.setString(1,  recipe_InfoVo.getRecipeName());

			rs = pstmt.executeQuery();

			int countFlag = 0;
			while(rs.next()) {		//쿼리문을 돌렸을때 받아온 컬럼의 값이 있을때 true
				countFlag = rs.getInt(1);		//있다면 1을 countFlag에 넣는다.
			}
			System.out.println(countFlag);
			if(0 < countFlag) {
				throw new ModifyException("해당 레시피 이름이 이미 존재합니다");
			}
			rs.close();
			pstmt.close();

			quary = "SELECT RD_ID FROM RECIPE_INFO WHERE RECIPE_CODE = ?";
			pstmt = con.prepareStatement(quary);
			pstmt.setInt(1, recipe_InfoVo.getRecipeCode());

			rs = pstmt.executeQuery();
			if(rs.next()) {
//				String selectedRdId = rs.getString("rd_Id");
				String selectedRdId = rs.getString(1);
				if(!selectedRdId.equals(rdId)) {
					throw new ModifyException("이 레시피의 작성자가 아닙니다"); 
				}
			}
			pstmt.close();

			//레시피코드로 연결된 RECIPE_INGREDIENT테이블의 재료코드값을 삭제한다.
			quary = "DELETE FROM RECIPE_INGREDIENT WHERE RECIPE_CODE = ?";
			pstmt = con.prepareStatement(quary);
			pstmt.setInt(1, recipe_InfoVo.getRecipeCode());

			pstmt.executeUpdate();
			pstmt.close();

			for(Ingredient ingredientVO : ingList) {		//ingList에 입력받아놨던 재료명(ing_name)을 하나씩 검사하면서 테이블에 재료명이 있으면 무시하고 없으면 생성하는 부분
				quary = "MERGE INTO INGREDIENT " + 
						"USING DUAL " + 
						"   ON (ING_NAME = ?) " + 
						"WHEN NOT MATCHED THEN " + 
						"    INSERT (ING_CODE, ING_NAME) " + 
						"VALUES ((SELECT MAX(ING_CODE) + 1 FROM INGREDIENT), ?)";
				pstmt = con.prepareStatement(quary);
				pstmt.setString(1, ingredientVO.getIngName());
				pstmt.setString(2, ingredientVO.getIngName());
				pstmt.executeUpdate();
				pstmt.close();
			}

			List<Ingredient> ing_codeList = new ArrayList<Ingredient>();
			Ingredient ingredientVo = new Ingredient();

			//,빼고 ING_CODE와 ING_NAME값을 셀렉트 해오는 쿼리
			quary = "SELECT ING_CODE, ING_NAME FROM INGREDIENT WHERE ING_NAME IN ( "
					+ ing_name.substring(1, ing_name.length()) + ")";	//", '재료1', '재료2', '재료3' "식으로 문자넣음.

			pstmt = con.prepareStatement(quary);
			rs = pstmt.executeQuery();		//쿼리 실행시 재료코드값과 재료이름을 받아옴

			while(rs.next()) {
				ingredientVo = new Ingredient();
				ingredientVo.setIngCode(rs.getInt(1));		//첫번째 값은 재료코드값으로
				ingredientVo.setIngName(rs.getString(2));		//두번째값은 재료이름값으로
				ing_codeList.add(ingredientVo);		//인덱스 하나하나 ing_codeList에 넣어준다.
			}
			rs.close();
			pstmt.close();

			quary = "UPDATE RECIPE_INFO SET RECIPE_NAME=?,RECIPE_SUMM=?,RECIPE_PRICE=? WHERE RECIPE_CODE=?";		//RECIPE_INFO 에 값들을 넣어주는 쿼리문
			pstmt = con.prepareStatement(quary);

			pstmt.setString(1, recipe_InfoVo.getRecipeName());
			pstmt.setString(2, recipe_InfoVo.getRecipeSumm());
			pstmt.setDouble(3, recipe_InfoVo.getRecipePrice());
			pstmt.setInt(4, recipe_InfoVo.getRecipeCode());

			pstmt.executeUpdate();
			pstmt.close();

			fileOutput(recipe_InfoVo.getRecipeProcess(), ingInfo + "\n" + process);

			for(Ingredient ingredientVO : ing_codeList) {			//ing_codeList에 있는것들을 ingredientVO에 넣으면서 반복문 돌림.
				quary = "INSERT INTO RECIPE_INGREDIENT VALUES (?, ?)";		//리세피코드, 재료코드, 용량 insert 해주는 쿼리
				pstmt = con.prepareStatement(quary);
				pstmt.setInt(1, recipe_InfoVo.getRecipeCode());
				pstmt.setInt(2,  ingredientVO.getIngCode());
				pstmt.executeUpdate();
				pstmt.close();
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				MyConnection.close(rs, pstmt, con);
			}catch(Exception e){
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	public void remove(String rdId, RecipeInfo recipeInfo) throws ModifyException {
		Connection con = null; // DB연결된 상태(세션)을 담은 객체
		PreparedStatement pstmt = null;  // SQL 문을 나타내는 객체
		ResultSet rs = null;  // 쿼리문을 날린것에 대한 반환값을 담을 객체

		//선택한 레시피의 rd_id값이 로그인한 rd_id값과 같은지 확인
		String quary = "SELECT RD_ID FROM RECIPE_INFO WHERE RECIPE_CODE = ?";
		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(quary);
			pstmt.setInt(1, recipeInfo.getRecipeCode());

			rs = pstmt.executeQuery();
			if(rs.next()) {
				String selectedRdId = rs.getString("rd_Id");
				if(!selectedRdId.equals(rdId)) {
					throw new ModifyException("이 레시피의 작성자가 아닙니다"); 
				}
			}
			pstmt.close();

			//레시피코드를 기준으로 레시피 활성화여부값을 0으로 수정
			quary ="UPDATE RECIPE_INFO SET RECIPE_STATUS = '0' WHERE RECIPE_CODE = ?";

			pstmt = con.prepareStatement(quary);
			pstmt.setInt(1, recipeInfo.getRecipeCode());

			pstmt.executeUpdate();
			pstmt.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				MyConnection.close(rs, pstmt, con);
			}catch(Exception e){
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	public List<RecipeInfo> selectAll() throws FindException {
		Connection con = null; // DB연결된 상태(세션)을 담은 객체
		PreparedStatement pstmt = null;  // SQL 문을 나타내는 객체
		ResultSet rs = null;  // 쿼리문을 날린것에 대한 반환값을 담을 객체


		List<RecipeInfo> recipeInfoList = new ArrayList<>();

		String quary = "SELECT i.recipe_code, i.recipe_name, i.recipe_summ, i.recipe_price, i.recipe_process, p.like_count, p.dislike_count FROM recipe_info i JOIN POINT p ON i.recipe_code = p.recipe_code where i.recipe_status=1";

		try {
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(quary);

			rs = pstmt.executeQuery();

			while(rs.next()) {
				RecipeInfo recipeInfo = new RecipeInfo();
				Point point = new Point();
				recipeInfo.setRecipeCode(rs.getInt(1));		//첫번째 값은 레시피코드값으로
				recipeInfo.setRecipeName(rs.getString(2));		//두번째값은 레시피이름값으로
				recipeInfo.setRecipeSumm(rs.getString(3));		//세번째값은 레시피요약값으로
				recipeInfo.setRecipePrice(rs.getInt(4));		//네번째값은 레시피가격값으로
				recipeInfo.setRecipeProcess(rs.getString(5));		//다섯번째값은 경로값으로
				point.setLikeCount(rs.getInt(6));		//여섯번째값은 좋아요값으로
				point.setDisLikeCount(rs.getInt(7));		//일곱번째값은 싫어요값으로
				recipeInfo.setPoint(point);					//좋아요,싫어요 값을 Point에 넣는다.

				recipeInfoList.add(recipeInfo);		//인덱스 하나하나 recipeInfoList에 넣어준다.
			}
			if(recipeInfoList.isEmpty()) {
				throw new FindException("레시피가 하나도 없습니다");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				MyConnection.close(rs, pstmt, con);
			}catch(Exception e){
				throw new RuntimeException(e.getMessage());
			}
		}
		return recipeInfoList;
	}

	/**
	 * 좋아요 개수(내림차순), 싫어요 개수(오름차순), 작성된 후기 개수(내림차순)를 기준으로 추천 레시피를 선정하여 반환한다 
	 * @return 추천 레시피 정보를 포함한 RecipeInfo 객체
	 * @throws FindException
	 */
	public RecipeInfo selectByRank() throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}

		String selectByRankSQL = "SELECT ri.recipe_code, ri.recipe_name, ri.recipe_summ, ri.recipe_price, ri.recipe_process, po.like_count, po.dislike_count\r\n" + 
				"FROM recipe_info ri JOIN point po ON (ri.recipe_code = po.recipe_code)\r\n" + 
				"WHERE\r\n" + 
				"    ri.recipe_code = (\r\n" + 
				"        SELECT\r\n" + 
				"            recipe_code\r\n" + 
				"        FROM\r\n" + 
				"            (\r\n" + 
				"                SELECT\r\n" + 
				"                    p.recipe_code\r\n" + 
				"                FROM\r\n" + 
				"                    point p\r\n" + 
				"                    JOIN recipe_info i ON (p.recipe_code = i.recipe_code)\r\n" + 
				"                WHERE\r\n" + 
				"                    i.recipe_status = '1'\r\n" + 
				"                ORDER BY\r\n" + 
				"                    like_count DESC,\r\n" + 
				"                    dislike_count ASC,\r\n" + 
				"                    (\r\n" + 
				"                        SELECT\r\n" + 
				"                            COUNT(*)\r\n" + 
				"                        FROM\r\n" + 
				"                            review\r\n" + 
				"                        WHERE\r\n" + 
				"                            recipe_code = p.recipe_code\r\n" + 
				"                    ) DESC\r\n" + 
				"            )\r\n" + 
				"        WHERE\r\n" + 
				"            ROWNUM = 1\r\n" + 
				"    )";

		try {
			pstmt = con.prepareStatement(selectByRankSQL);
			rs = pstmt.executeQuery();

			if(rs.next()) return new RecipeInfo(rs.getInt("recipe_code"), rs.getString("recipe_name"), rs.getString("recipe_summ"), rs.getDouble("recipe_price"), rs.getString("recipe_process"), new Point(rs.getInt("recipe_code"), rs.getInt("like_count"), rs.getInt("dislike_count")), null);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		throw new FindException("추천 레시피 탐색 오류");
	}
	private boolean fileOutput(String fileFullPath, String message) {
		try {

			@SuppressWarnings("resource")
			OutputStream output = new FileOutputStream(fileFullPath);		//파일경로명을  output에 넣는다.
			String str = message;
			byte[] by=str.getBytes();			//메시지들을 바이트배열에 넣는다
			output.write(by);			//그것들을 쓴다.

		} catch (Exception e) {
			System.out.println("FileOutput Failure");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static void main(String[] args) {
		RecipeInfoDAO dao = new RecipeInfoDAO();
		int code = 10;
		System.out.println(10%5);
//		
//		try {
//			RecipeInfo info = dao.selectByCode(code);
//			
//			System.out.println(info.getRecipeName());
//		} catch (FindException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
	}

}
