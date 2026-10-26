package com.example.smartpantry.ui.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom adapter for the suggested-recipes list. Each row shows the recipe
 * name, its ingredient count, and (for the "Almost There" section) how many
 * ingredients are still missing.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.VH> {

    public interface Listener {
        void onOpen(Recipe recipe);
    }

    private final List<Recipe> recipes = new ArrayList<>();
    private final Listener listener;

    public RecipeAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setRecipes(List<Recipe> newRecipes) {
        recipes.clear();
        recipes.addAll(newRecipes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Recipe r = recipes.get(position);
        h.name.setText(r.getName());
        h.info.setText(r.getIngredients().size() + " ingredients");

        if (r.getMissingCount() == 1) {
            h.badge.setVisibility(View.VISIBLE);
            h.badge.setText("Missing 1 ingredient");
        } else {
            h.badge.setVisibility(View.GONE);
        }

        h.itemView.setOnClickListener(v -> listener.onOpen(r));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView name, info, badge;

        VH(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.recipe_name);
            info = v.findViewById(R.id.recipe_info);
            badge = v.findViewById(R.id.recipe_badge);
        }
    }
}
