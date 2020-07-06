package com.recipe.view;

import java.io.IOException;

import com.recipe.io.DataIO;
import com.recipe.io.Menu;

public class RecommendedRecipeView {
	private DataIO dio;
	
	public RecommendedRecipeView(DataIO dio) {
		this.dio = dio;
	}
	
	public void showRecommendedRecipeView() throws IOException {
		dio.sendMenu(Menu.RECOMMENDED_RECIPE);
		
		if(dio.receiveStatus().equals("success")) {
			RecipeInfoView recipeInfoView = new RecipeInfoView(dio);
			recipeInfoView.showRecipeInfoView(dio.receiveRecipeInfo());
		} else {
			FailView fail = new FailView();
			fail.recommendedRecipe(dio.receive());
		}
	}
}
