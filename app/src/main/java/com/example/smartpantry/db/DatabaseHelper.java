package com.example.smartpantry.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.example.smartpantry.logic.IngredientNormalizer;
import com.example.smartpantry.model.Ingredient;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite persistence for the whole app, using SQLiteOpenHelper (the approach
 * taught in the module's persistent-data chapter).
 *
 * Three tables:
 *   pantry_items        - what the user has (full CRUD).
 *   recipes             - seeded recipe collection (read-only in the UI).
 *   recipe_ingredients  - the ingredients each recipe needs.
 *
 * Recipes are seeded ONCE on database creation so they survive app restarts.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "smart_pantry.db";
    public static final int DB_VERSION = 1;

    // --- pantry_items ---
    public static final String T_PANTRY = "pantry_items";
    public static final String P_ID = "_id";
    public static final String P_NAME = "name";
    public static final String P_NAME_NORM = "name_norm";
    public static final String P_QTY = "quantity";
    public static final String P_UNIT = "unit";
    public static final String P_EXPIRY = "expiry_date";

    // --- recipes ---
    public static final String T_RECIPES = "recipes";
    public static final String R_ID = "_id";
    public static final String R_NAME = "name";
    public static final String R_STEPS = "steps";

    // --- recipe_ingredients ---
    public static final String T_RING = "recipe_ingredients";
    public static final String RI_ID = "_id";
    public static final String RI_RECIPE = "recipe_id";
    public static final String RI_NAME = "name";
    public static final String RI_QTY = "quantity";
    public static final String RI_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T_PANTRY + " (" +
                P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                P_NAME + " TEXT NOT NULL, " +
                P_NAME_NORM + " TEXT NOT NULL, " +
                P_QTY + " REAL NOT NULL DEFAULT 0, " +
                P_UNIT + " TEXT, " +
                P_EXPIRY + " TEXT)");

        db.execSQL("CREATE TABLE " + T_RECIPES + " (" +
                R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                R_NAME + " TEXT NOT NULL, " +
                R_STEPS + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + T_RING + " (" +
                RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RI_RECIPE + " INTEGER NOT NULL, " +
                RI_NAME + " TEXT NOT NULL, " +
                RI_QTY + " REAL NOT NULL DEFAULT 0, " +
                RI_UNIT + " TEXT, " +
                "FOREIGN KEY(" + RI_RECIPE + ") REFERENCES " + T_RECIPES + "(" + R_ID + "))");

        // Pre-load the recipe collection on first run.
        RecipeSeed.seed(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_RING);
        db.execSQL("DROP TABLE IF EXISTS " + T_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + T_PANTRY);
        onCreate(db);
    }

    // ------------------------------------------------------------------
    // PANTRY CRUD
    // ------------------------------------------------------------------

    /** CREATE / UPDATE. If item.id > 0 we update, otherwise insert. */
    public long savePantryItem(@NonNull PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(P_NAME, item.getName());
        cv.put(P_NAME_NORM, IngredientNormalizer.name(item.getName()));
        cv.put(P_QTY, item.getQuantity());
        cv.put(P_UNIT, item.getUnit());
        cv.put(P_EXPIRY, item.getExpiryDate());

        if (item.getId() > 0) {
            db.update(T_PANTRY, cv, P_ID + "=?",
                    new String[]{String.valueOf(item.getId())});
            return item.getId();
        } else {
            return db.insert(T_PANTRY, null, cv);
        }
    }

    /** DELETE a pantry item by id. */
    public void deletePantryItem(long id) {
        getWritableDatabase().delete(T_PANTRY, P_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    /** READ a single pantry item (used by the edit screen). */
    public PantryItem getPantryItem(long id) {
        Cursor c = getReadableDatabase().query(T_PANTRY, null, P_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        PantryItem item = null;
        if (c.moveToFirst()) {
            item = readPantry(c);
        }
        c.close();
        return item;
    }

    /** READ all pantry items, alphabetically. */
    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query(T_PANTRY, null, null, null,
                null, null, P_NAME + " COLLATE NOCASE ASC");
        while (c.moveToNext()) {
            list.add(readPantry(c));
        }
        c.close();
        return list;
    }

    private PantryItem readPantry(Cursor c) {
        PantryItem p = new PantryItem();
        p.setId(c.getLong(c.getColumnIndexOrThrow(P_ID)));
        p.setName(c.getString(c.getColumnIndexOrThrow(P_NAME)));
        p.setQuantity(c.getDouble(c.getColumnIndexOrThrow(P_QTY)));
        p.setUnit(c.getString(c.getColumnIndexOrThrow(P_UNIT)));
        p.setExpiryDate(c.getString(c.getColumnIndexOrThrow(P_EXPIRY)));
        return p;
    }

    // ------------------------------------------------------------------
    // RECIPE READS
    // ------------------------------------------------------------------

    /** READ all recipes together with their ingredients. */
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor rc = db.query(T_RECIPES, null, null, null, null, null,
                R_NAME + " COLLATE NOCASE ASC");
        while (rc.moveToNext()) {
            Recipe r = new Recipe(
                    rc.getLong(rc.getColumnIndexOrThrow(R_ID)),
                    rc.getString(rc.getColumnIndexOrThrow(R_NAME)),
                    rc.getString(rc.getColumnIndexOrThrow(R_STEPS)));
            loadIngredients(db, r);
            recipes.add(r);
        }
        rc.close();
        return recipes;
    }

    /** READ a single recipe by id (used by the detail screen). */
    public Recipe getRecipe(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor rc = db.query(T_RECIPES, null, R_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        Recipe r = null;
        if (rc.moveToFirst()) {
            r = new Recipe(
                    rc.getLong(rc.getColumnIndexOrThrow(R_ID)),
                    rc.getString(rc.getColumnIndexOrThrow(R_NAME)),
                    rc.getString(rc.getColumnIndexOrThrow(R_STEPS)));
            loadIngredients(db, r);
        }
        rc.close();
        return r;
    }

    private void loadIngredients(SQLiteDatabase db, Recipe r) {
        Cursor ic = db.query(T_RING, null, RI_RECIPE + "=?",
                new String[]{String.valueOf(r.getId())}, null, null, RI_NAME + " ASC");
        while (ic.moveToNext()) {
            Ingredient ing = new Ingredient();
            ing.setId(ic.getLong(ic.getColumnIndexOrThrow(RI_ID)));
            ing.setRecipeId(r.getId());
            ing.setName(ic.getString(ic.getColumnIndexOrThrow(RI_NAME)));
            ing.setQuantity(ic.getDouble(ic.getColumnIndexOrThrow(RI_QTY)));
            ing.setUnit(ic.getString(ic.getColumnIndexOrThrow(RI_UNIT)));
            r.addIngredient(ing);
        }
        ic.close();
    }
}
