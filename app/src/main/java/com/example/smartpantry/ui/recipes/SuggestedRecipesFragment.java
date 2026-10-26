package com.example.smartpantry.ui.recipes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.logic.RecipeMatcher;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;

import java.util.List;

/**
 * The "Suggested Recipes" screen. Runs the strict-matching rule against the
 * current pantry and shows ONLY recipes the user can make right now.
 * Optionally shows a clearly separated "Almost There" section (missing 1
 * ingredient), controlled from Settings.
 */
public class SuggestedRecipesFragment extends Fragment implements RecipeAdapter.Listener {

    private DatabaseHelper db;
    private RecipeAdapter suggestedAdapter;
    private RecipeAdapter almostAdapter;
    private TextView emptyView;
    private View almostSection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suggested, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        db = new DatabaseHelper(requireContext());
        emptyView = view.findViewById(R.id.suggested_empty);
        almostSection = view.findViewById(R.id.almost_section);

        RecyclerView suggested = view.findViewById(R.id.suggested_list);
        suggested.setLayoutManager(new LinearLayoutManager(requireContext()));
        suggestedAdapter = new RecipeAdapter(this);
        suggested.setAdapter(suggestedAdapter);

        RecyclerView almost = view.findViewById(R.id.almost_list);
        almost.setLayoutManager(new LinearLayoutManager(requireContext()));
        almostAdapter = new RecipeAdapter(this);
        almost.setAdapter(almostAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        recompute();
    }

    private void recompute() {
        List<Recipe> allRecipes = db.getAllRecipes();
        List<PantryItem> pantry = db.getAllPantryItems();

        // STRICT suggestions - the core feature.
        List<Recipe> strict = RecipeMatcher.suggest(allRecipes, pantry);
        suggestedAdapter.setRecipes(strict);
        emptyView.setVisibility(strict.isEmpty() ? View.VISIBLE : View.GONE);

        // Optional "Almost There" section (stretch feature, toggled in Settings).
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        boolean showAlmost = prefs.getBoolean("show_almost_there", false);
        if (showAlmost) {
            List<Recipe> almost = RecipeMatcher.almostThere(allRecipes, pantry);
            almostAdapter.setRecipes(almost);
            almostSection.setVisibility(almost.isEmpty() ? View.GONE : View.VISIBLE);
        } else {
            almostSection.setVisibility(View.GONE);
        }
    }

    @Override
    public void onOpen(Recipe recipe) {
        Intent i = new Intent(requireContext(), RecipeDetailActivity.class);
        i.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(i);
    }
}
