package com.example.smartpantry.logic;

import com.example.smartpantry.model.Ingredient;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * THE CORE BUSINESS LOGIC of the app (brief Section 2.3).
 *
 * Strict-matching rule: a recipe is "suggested" only when EVERY ingredient
 * it requires is present in the pantry in at least the required quantity.
 * Miss even one ingredient (or not enough of it) and the recipe is excluded.
 */
public final class RecipeMatcher {

    private RecipeMatcher() { }

    /**
     * Build a quick lookup of what the pantry contains, keyed by the
     * NORMALISED ingredient name so "Tomatoes" matches "tomato".
     */
    private static Map<String, PantryItem> indexPantry(List<PantryItem> pantry) {
        Map<String, PantryItem> map = new HashMap<>();
        for (PantryItem p : pantry) {
            String key = IngredientNormalizer.name(p.getName());
            // If the same ingredient appears twice, keep the larger quantity.
            PantryItem existing = map.get(key);
            if (existing == null || p.getQuantity() > existing.getQuantity()) {
                map.put(key, p);
            }
        }
        return map;
    }

    /**
     * @return how many of the recipe's ingredients the pantry CANNOT satisfy.
     *         0 means the recipe passes the strict rule.
     */
    public static int missingIngredientCount(Recipe recipe, Map<String, PantryItem> pantryIndex) {
        int missing = 0;
        for (Ingredient need : recipe.getIngredients()) {
            if (!pantryHasEnough(need, pantryIndex)) {
                missing++;
            }
        }
        return missing;
    }

    /**
     * True only if the pantry has this ingredient by name AND in at least
     * the required quantity (when the units can be compared).
     */
    private static boolean pantryHasEnough(Ingredient need, Map<String, PantryItem> pantryIndex) {
        String key = IngredientNormalizer.name(need.getName());
        PantryItem have = pantryIndex.get(key);
        if (have == null) {
            return false; // ingredient not in pantry at all
        }
        double needQty = UnitConverter.toBase(need.getQuantity(), need.getUnit());
        double haveQty = UnitConverter.toBase(have.getQuantity(), have.getUnit());

        if (UnitConverter.comparable(need.getUnit(), have.getUnit())) {
            // Units are compatible - compare real amounts (small tolerance for doubles).
            return haveQty + 1e-6 >= needQty;
        }
        // Units are not convertible (e.g. "cup" vs "g"): fall back to presence.
        // Being present counts as enough, since we cannot reliably compare.
        return true;
    }

    /** Recipes where NOTHING is missing - the strict suggestions list. */
    public static List<Recipe> suggest(List<Recipe> allRecipes, List<PantryItem> pantry) {
        Map<String, PantryItem> idx = indexPantry(pantry);
        List<Recipe> result = new ArrayList<>();
        for (Recipe r : allRecipes) {
            if (r.getIngredients().isEmpty()) continue;
            int missing = missingIngredientCount(r, idx);
            r.setMissingCount(missing);
            if (missing == 0) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * STRETCH (brief Section 8): recipes missing exactly ONE ingredient.
     * Kept strictly separate from the main suggestions list.
     */
    public static List<Recipe> almostThere(List<Recipe> allRecipes, List<PantryItem> pantry) {
        Map<String, PantryItem> idx = indexPantry(pantry);
        List<Recipe> result = new ArrayList<>();
        for (Recipe r : allRecipes) {
            if (r.getIngredients().isEmpty()) continue;
            int missing = missingIngredientCount(r, idx);
            r.setMissingCount(missing);
            if (missing == 1) {
                result.add(r);
            }
        }
        return result;
    }
}
