package com.example.smartpantry.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartpantry.logic.IngredientNormalizer;

/**
 * Seeds 18 starter recipes into the database on first run.
 * Quantities are kept small and in simple units so the strict-matching
 * rule is easy to demonstrate in the video.
 */
final class RecipeSeed {

    private RecipeSeed() { }

    static void seed(SQLiteDatabase db) {
        // --- 1 ---
        long id = recipe(db, "Tomato & Egg Scramble",
                "1. Beat the eggs with a pinch of salt.\n" +
                "2. Fry the chopped tomatoes until soft.\n" +
                "3. Pour in the eggs and stir until just set.\n" +
                "4. Finish with a drizzle of oil and serve.");
        ing(db, id, "Eggs", 3, "pcs");
        ing(db, id, "Tomatoes", 2, "pcs");
        ing(db, id, "Salt", 2, "g");
        ing(db, id, "Oil", 15, "ml");

        // --- 2 ---
        id = recipe(db, "Garlic Butter Pasta",
                "1. Boil the pasta until al dente, then drain.\n" +
                "2. Melt butter and gently fry the minced garlic.\n" +
                "3. Toss the pasta in the garlic butter.\n" +
                "4. Season with salt and serve.");
        ing(db, id, "Pasta", 200, "g");
        ing(db, id, "Butter", 30, "g");
        ing(db, id, "Garlic", 3, "pcs");
        ing(db, id, "Salt", 2, "g");

        // --- 3 ---
        id = recipe(db, "Classic Pancakes",
                "1. Whisk flour, milk, egg and sugar into a smooth batter.\n" +
                "2. Pour ladles of batter onto a hot greased pan.\n" +
                "3. Flip when bubbles form on top.\n" +
                "4. Serve warm.");
        ing(db, id, "Flour", 150, "g");
        ing(db, id, "Milk", 250, "ml");
        ing(db, id, "Eggs", 1, "pcs");
        ing(db, id, "Sugar", 20, "g");

        seedPart2(db);
        seedPart3(db);
    }

    private static void seedPart2(SQLiteDatabase db) {
        // --- 4 ---
        long id = recipe(db, "Vegetable Fried Rice",
                "1. Heat oil and scramble the egg.\n" +
                "2. Add peas, carrot and cooked rice.\n" +
                "3. Stir-fry with soy sauce until hot.\n" +
                "4. Serve immediately.");
        ing(db, id, "Rice", 300, "g");
        ing(db, id, "Eggs", 2, "pcs");
        ing(db, id, "Peas", 60, "g");
        ing(db, id, "Carrot", 1, "pcs");
        ing(db, id, "Soy Sauce", 15, "ml");
        ing(db, id, "Oil", 15, "ml");

        // --- 5 ---
        id = recipe(db, "Grilled Cheese Sandwich",
                "1. Butter the outside of two bread slices.\n" +
                "2. Place cheese between them.\n" +
                "3. Grill in a pan until golden on both sides.");
        ing(db, id, "Bread", 2, "pcs");
        ing(db, id, "Cheese", 60, "g");
        ing(db, id, "Butter", 20, "g");

        // --- 6 ---
        id = recipe(db, "Chicken Stir Fry",
                "1. Slice and brown the chicken.\n" +
                "2. Add pepper and onion, stir-fry briefly.\n" +
                "3. Pour in soy sauce and cook through.\n" +
                "4. Serve hot.");
        ing(db, id, "Chicken Breast", 250, "g");
        ing(db, id, "Bell Pepper", 1, "pcs");
        ing(db, id, "Onion", 1, "pcs");
        ing(db, id, "Soy Sauce", 20, "ml");
        ing(db, id, "Oil", 15, "ml");

        // --- 7 ---
        id = recipe(db, "Mashed Potatoes",
                "1. Boil the potatoes until tender.\n" +
                "2. Mash with butter and milk.\n" +
                "3. Season with salt.");
        ing(db, id, "Potatoes", 500, "g");
        ing(db, id, "Butter", 40, "g");
        ing(db, id, "Milk", 80, "ml");
        ing(db, id, "Salt", 3, "g");

        // --- 8 ---
        id = recipe(db, "Greek Salad",
                "1. Chop tomatoes, cucumber and onion.\n" +
                "2. Add crumbled feta and olives.\n" +
                "3. Dress with olive oil and salt.");
        ing(db, id, "Tomatoes", 2, "pcs");
        ing(db, id, "Cucumber", 1, "pcs");
        ing(db, id, "Onion", 1, "pcs");
        ing(db, id, "Feta Cheese", 80, "g");
        ing(db, id, "Olive Oil", 20, "ml");

        // --- 9 ---
        id = recipe(db, "Banana Smoothie",
                "1. Peel the bananas.\n" +
                "2. Blend with milk and honey until smooth.\n" +
                "3. Pour and serve chilled.");
        ing(db, id, "Bananas", 2, "pcs");
        ing(db, id, "Milk", 200, "ml");
        ing(db, id, "Honey", 15, "ml");

        // --- 10 ---
        id = recipe(db, "Omelette",
                "1. Beat the eggs with salt.\n" +
                "2. Pour into a buttered hot pan.\n" +
                "3. Add cheese, fold and serve.");
        ing(db, id, "Eggs", 3, "pcs");
        ing(db, id, "Cheese", 40, "g");
        ing(db, id, "Butter", 15, "g");
        ing(db, id, "Salt", 2, "g");

        // --- 11 ---
        id = recipe(db, "Tomato Soup",
                "1. Soften onion and garlic in oil.\n" +
                "2. Add tomatoes and water, simmer.\n" +
                "3. Blend smooth and season with salt.");
        ing(db, id, "Tomatoes", 4, "pcs");
        ing(db, id, "Onion", 1, "pcs");
        ing(db, id, "Garlic", 2, "pcs");
        ing(db, id, "Oil", 15, "ml");
        ing(db, id, "Salt", 3, "g");
    }

    private static void seedPart3(SQLiteDatabase db) {
        // --- 12 ---
        long id = recipe(db, "French Toast",
                "1. Whisk egg, milk and sugar.\n" +
                "2. Soak bread slices in the mixture.\n" +
                "3. Fry in butter until golden.");
        ing(db, id, "Bread", 3, "pcs");
        ing(db, id, "Eggs", 2, "pcs");
        ing(db, id, "Milk", 100, "ml");
        ing(db, id, "Sugar", 15, "g");
        ing(db, id, "Butter", 20, "g");

        // --- 13 ---
        id = recipe(db, "Guacamole",
                "1. Mash the avocados.\n" +
                "2. Mix in chopped tomato, onion and lime juice.\n" +
                "3. Season with salt.");
        ing(db, id, "Avocados", 2, "pcs");
        ing(db, id, "Tomatoes", 1, "pcs");
        ing(db, id, "Onion", 1, "pcs");
        ing(db, id, "Lime", 1, "pcs");
        ing(db, id, "Salt", 2, "g");

        // --- 14 ---
        id = recipe(db, "Spaghetti Aglio e Olio",
                "1. Boil spaghetti until al dente.\n" +
                "2. Warm olive oil with sliced garlic and chilli.\n" +
                "3. Toss pasta through the oil and season.");
        ing(db, id, "Spaghetti", 200, "g");
        ing(db, id, "Garlic", 4, "pcs");
        ing(db, id, "Olive Oil", 40, "ml");
        ing(db, id, "Chilli Flakes", 2, "g");
        ing(db, id, "Salt", 3, "g");

        // --- 15 ---
        id = recipe(db, "Scrambled Eggs on Toast",
                "1. Toast the bread.\n" +
                "2. Scramble eggs gently in butter.\n" +
                "3. Spoon onto toast and season.");
        ing(db, id, "Eggs", 2, "pcs");
        ing(db, id, "Bread", 2, "pcs");
        ing(db, id, "Butter", 15, "g");
        ing(db, id, "Salt", 1, "g");

        // --- 16 ---
        id = recipe(db, "Carrot & Pea Rice",
                "1. Saute carrot and peas in oil.\n" +
                "2. Stir in cooked rice.\n" +
                "3. Season with salt and serve.");
        ing(db, id, "Rice", 250, "g");
        ing(db, id, "Carrot", 1, "pcs");
        ing(db, id, "Peas", 80, "g");
        ing(db, id, "Oil", 15, "ml");
        ing(db, id, "Salt", 2, "g");

        // --- 17 ---
        id = recipe(db, "Cheesy Baked Potato",
                "1. Bake the potato until soft.\n" +
                "2. Split and top with butter and cheese.\n" +
                "3. Return to the oven until the cheese melts.");
        ing(db, id, "Potatoes", 2, "pcs");
        ing(db, id, "Cheese", 60, "g");
        ing(db, id, "Butter", 20, "g");
        ing(db, id, "Salt", 2, "g");

        // --- 18 ---
        id = recipe(db, "Honey Yogurt Bowl",
                "1. Spoon yogurt into a bowl.\n" +
                "2. Drizzle with honey.\n" +
                "3. Top with sliced banana.");
        ing(db, id, "Yogurt", 200, "g");
        ing(db, id, "Honey", 20, "ml");
        ing(db, id, "Bananas", 1, "pcs");
    }

    // ------------------------------------------------------------------
    // small helpers
    // ------------------------------------------------------------------

    static long recipe(SQLiteDatabase db, String name, String steps) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.R_NAME, name);
        cv.put(DatabaseHelper.R_STEPS, steps);
        return db.insert(DatabaseHelper.T_RECIPES, null, cv);
    }

    static void ing(SQLiteDatabase db, long recipeId, String name, double qty, String unit) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.RI_RECIPE, recipeId);
        cv.put(DatabaseHelper.RI_NAME, name);
        cv.put(DatabaseHelper.RI_QTY, qty);
        cv.put(DatabaseHelper.RI_UNIT, IngredientNormalizer.unit(unit));
        db.insert(DatabaseHelper.T_RING, null, cv);
    }
}
