package com.example.smartpantry;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.smartpantry.ui.pantry.PantryFragment;
import com.example.smartpantry.ui.recipes.SuggestedRecipesFragment;
import com.example.smartpantry.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Single host Activity. It swaps between the three main Fragments
 * (Pantry, Suggested Recipes, Settings) using the bottom navigation bar,
 * satisfying the "working navigation element" requirement.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView nav = findViewById(R.id.bottom_nav);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_pantry) {
                show(new PantryFragment());
                return true;
            } else if (id == R.id.nav_suggested) {
                show(new SuggestedRecipesFragment());
                return true;
            } else if (id == R.id.nav_settings) {
                show(new SettingsFragment());
                return true;
            }
            return false;
        });

        // Show the pantry on first launch.
        if (savedInstanceState == null) {
            nav.setSelectedItemId(R.id.nav_pantry);
        }
    }

    private void show(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
