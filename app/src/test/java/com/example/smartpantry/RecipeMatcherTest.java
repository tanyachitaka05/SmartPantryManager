package com.example.smartpantry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.smartpantry.logic.RecipeMatcher;
import com.example.smartpantry.model.Ingredient;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Pure-Java unit tests for the strict-matching rule (brief Section 2.3).
 * These run on the JVM with `./gradlew test` - no device needed.
 */
public class RecipeMatcherTest {

    private Recipe tomatoEgg() {
        Recipe r = new Recipe(1, "Tomato & Egg", "steps");
        r.addIngredient(new Ingredient("Eggs", 2, "pcs"));
        r.addIngredient(new Ingredient("Tomatoes", 2, "pcs"));
        return r;
    }

    @Test
    public void allIngredientsPresent_recipeIsSuggested() {
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(new PantryItem(1, "Eggs", 6, "pcs", null));
        pantry.add(new PantryItem(2, "Tomatoes", 3, "pcs", null));

        List<Recipe> out = RecipeMatcher.suggest(list(tomatoEgg()), pantry);
        assertEquals(1, out.size());
    }

    @Test
    public void oneIngredientMissing_recipeExcluded() {
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(new PantryItem(1, "Eggs", 6, "pcs", null));
        // no tomatoes
        List<Recipe> out = RecipeMatcher.suggest(list(tomatoEgg()), pantry);
        assertTrue(out.isEmpty());
    }

    @Test
    public void notEnoughQuantity_recipeExcluded() {
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(new PantryItem(1, "Eggs", 1, "pcs", null)); // need 2
        pantry.add(new PantryItem(2, "Tomatoes", 3, "pcs", null));
        List<Recipe> out = RecipeMatcher.suggest(list(tomatoEgg()), pantry);
        assertTrue(out.isEmpty());
    }

    @Test
    public void pluralAndCaseDifferences_stillMatch() {
        // Recipe says "Tomatoes", pantry says "tomato" - must still match.
        Recipe r = new Recipe(2, "Salad", "steps");
        r.addIngredient(new Ingredient("Tomatoes", 1, "pcs"));
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(new PantryItem(1, "tomato", 2, "pcs", null));
        List<Recipe> out = RecipeMatcher.suggest(list(r), pantry);
        assertEquals(1, out.size());
    }

    @Test
    public void kgToGramConversion_isComparable() {
        Recipe r = new Recipe(3, "Bake", "steps");
        r.addIngredient(new Ingredient("Flour", 500, "g"));
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(new PantryItem(1, "Flour", 1, "kg", null)); // 1kg >= 500g
        List<Recipe> out = RecipeMatcher.suggest(list(r), pantry);
        assertEquals(1, out.size());
    }

    @Test
    public void almostThere_findsRecipesMissingExactlyOne() {
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(new PantryItem(1, "Eggs", 6, "pcs", null));
        // tomatoes missing -> exactly one missing
        List<Recipe> almost = RecipeMatcher.almostThere(list(tomatoEgg()), pantry);
        assertEquals(1, almost.size());
        assertFalse(RecipeMatcher.suggest(list(tomatoEgg()), pantry).size() > 0);
    }

    private List<Recipe> list(Recipe... rs) {
        List<Recipe> l = new ArrayList<>();
        for (Recipe r : rs) l.add(r);
        return l;
    }
}
