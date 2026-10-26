package com.example.smartpantry.ui.recipes;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.Ingredient;
import com.example.smartpantry.model.Recipe;

/**
 * Recipe Detail screen. Receives a recipe id through an Intent extra and
 * shows the full ingredient list plus the preparation method.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView title = findViewById(R.id.detail_name);
        TextView ingredients = findViewById(R.id.detail_ingredients);
        TextView steps = findViewById(R.id.detail_steps);

        long id = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        DatabaseHelper db = new DatabaseHelper(this);
        Recipe recipe = db.getRecipe(id);

        if (recipe == null) {
            title.setText("Recipe not found");
            return;
        }

        setTitle(recipe.getName());
        title.setText(recipe.getName());

        StringBuilder sb = new StringBuilder();
        for (Ingredient ing : recipe.getIngredients()) {
            sb.append("\u2022 ")
              .append(trimNumber(ing.getQuantity())).append(" ")
              .append(ing.getUnit() == null ? "" : ing.getUnit()).append("  ")
              .append(ing.getName()).append("\n");
        }
        ingredients.setText(sb.toString().trim());
        steps.setText(recipe.getSteps());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private String trimNumber(double d) {
        if (d == Math.floor(d)) return String.valueOf((long) d);
        return String.valueOf(d);
    }
}
