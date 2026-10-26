package com.example.smartpantry.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A recipe: a name, the ingredients it needs, and the preparation steps.
 */
public class Recipe {

    private long id;
    private String name;
    private String steps;
    private final List<Ingredient> ingredients = new ArrayList<>();

    // Populated at match time only (not stored in DB) - how many ingredients
    // are still missing for the "Almost There" stretch feature.
    private int missingCount = 0;

    public Recipe() { }

    public Recipe(long id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void addIngredient(Ingredient i) { ingredients.add(i); }

    public int getMissingCount() { return missingCount; }
    public void setMissingCount(int missingCount) { this.missingCount = missingCount; }
}
