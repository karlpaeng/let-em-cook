package dev.karl.letemcook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class RecipeViewer extends AppCompatActivity {

    TextView dish, ingredients, servingSize, instructions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_viewer);

        getWindow().setStatusBarColor(ContextCompat.getColor(RecipeViewer.this, R.color.orange));
        getWindow().setNavigationBarColor(ContextCompat.getColor(RecipeViewer.this, R.color.orange));

        dish = findViewById(R.id.tvRecipeTitle);
        ingredients = findViewById(R.id.tvRecipeActIngredients);
        servingSize = findViewById(R.id.tvRecipeActServing);
        instructions = findViewById(R.id.tvRecipeActInstructions);

        Intent intent = getIntent();
        try {
            if (intent != null && intent.hasExtra("recipe")){

                RecipeModel recipe = (RecipeModel) intent.getParcelableExtra("recipe");

                dish.setText(recipe.title);
                ingredients.setText(recipe.ingredients);
                servingSize.setText(recipe.servings);
                instructions.setText(recipe.instructions);
            }
        }catch (Exception e){
            Log.d("letemcook_log", e.getMessage());
        }

        AdView mAdView = findViewById(R.id.adViewRecipe);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
}