# Smart Pantry Manager

A native **Java** Android application that helps you cut food waste by suggesting
recipes you can cook using **strictly** the ingredients you already have at home.
No shopping trip, no location features - just your pantry and your recipes.



---

## What it does

- **Pantry management (full CRUD):** add, edit and delete ingredients (name,
  quantity, unit, optional expiry date). Everything is stored on-device and
  survives app restarts.
- **Seeded recipe collection:** 18 recipes are pre-loaded into the database on
  first run, each with required ingredients and preparation steps.
- **Strict-matching suggestions (the core logic):** the *Suggested Recipes*
  screen shows **only** recipes for which you have *every* required ingredient
  in at least the required quantity. Miss one ingredient and the recipe is
  excluded.
- **Robust matching:** names are normalised so "Tomatoes" matches "tomato",
  and units are converted where sensible (e.g. 1 kg satisfies a 500 g
  requirement). No naive exact-string matching.
- **Recipe detail screen:** full ingredient list and method for any recipe.
- **Settings screen:** toggle expiring-soon alerts, toggle the optional
  "Almost There" list (recipes missing exactly one ingredient, kept clearly
  separate from the strict suggestions), and choose a preferred unit system.
- **Graceful empty state:** a friendly message when nothing matches yet.

---

## Database choice: SQLite (SQLiteOpenHelper)

This app uses **SQLite via SQLiteOpenHelper**, implemented locally on-device.

**Why SQLite?**

- The app's data (pantry items and recipes) is private, structured and
  relational a perfect fit for a local relational store.
- It needs **zero network access and zero external accounts**, which keeps the
  app fully offline and in scope with the brief (no location/cloud needed).


Schema (see DatabaseHelper.java):

| Table | Purpose |
|-------|---------|
| pantry_items | What the user owns - full CRUD |
| recipes | Seeded recipe collection |
| recipe_ingredients | Ingredients each recipe requires (FK to recipes) |

---

## Project structure


app/src/main/java/com/example/smartpantry/
  MainActivity.java                 host activity + bottom navigation
  model/   PantryItem, Ingredient, Recipe
  db/      DatabaseHelper (SQLite CRUD), RecipeSeed (initial data)
  logic/   RecipeMatcher (STRICT rule), IngredientNormalizer, UnitConverter
  ui/pantry/    PantryFragment, PantryAdapter, AddEditIngredientActivity
  ui/recipes/   SuggestedRecipesFragment, RecipeAdapter, RecipeDetailActivity
  ui/settings/  SettingsFragment
app/src/test/java/...  RecipeMatcherTest (unit tests for the core rule)


### Screens (5 total)

1. **Pantry List** - RecyclerView of all ingredients (home tab).
2. **Add / Edit Ingredient** - form with input validation (separate Activity,
   reached via an Intent).
3. **Suggested Recipes** - runs the strict-matching rule.
4. **Recipe Detail** - full ingredients + method (separate Activity, data passed
   via an Intent extra).
5. **Settings** - preferences (PreferenceFragmentCompat).

---

## Setup & run

**Requirements:** Android Studio (Giraffe/Hedgehog or newer), Android SDK 34,
JDK 17.

1. Open Android Studio -> **Open** -> select the SmartPantryManager folder.
2. Let Gradle sync (Android Studio will download the Gradle wrapper/distribution
   automatically on first sync).
3. Choose an emulator (or a connected device, API 24+) and press **Run**.
4. On first launch the 18 recipes are seeded automatically. Add a few pantry
   items and open the *Recipes* tab to see the strict matching in action.

**Run the unit tests** (no device needed):

```

```

---



---
