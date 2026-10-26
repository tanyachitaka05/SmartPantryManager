package com.example.smartpantry.ui.pantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * The Pantry List screen - the home tab. Shows every ingredient the user
 * owns in a RecyclerView, lets them add (via Intent to AddEditIngredientActivity),
 * edit (tap a row) and delete (trash icon) - the full CRUD surface.
 */
public class PantryFragment extends Fragment implements PantryAdapter.Listener {

    private DatabaseHelper db;
    private PantryAdapter adapter;
    private TextView empty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pantry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        db = new DatabaseHelper(requireContext());
        empty = view.findViewById(R.id.empty_view);

        RecyclerView list = view.findViewById(R.id.pantry_list);
        list.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PantryAdapter(this);
        list.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab_add);
        fab.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AddEditIngredientActivity.class)));
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh(); // reload after returning from add/edit so data stays in sync
    }

    private void refresh() {
        java.util.List<PantryItem> items = db.getAllPantryItems();
        adapter.setItems(items);
        empty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onEdit(PantryItem item) {
        // Use an Intent extra to pass the id of the row being edited.
        Intent i = new Intent(requireContext(), AddEditIngredientActivity.class);
        i.putExtra(AddEditIngredientActivity.EXTRA_ID, item.getId());
        startActivity(i);
    }

    @Override
    public void onDelete(PantryItem item) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete item")
                .setMessage("Remove \"" + item.getName() + "\" from your pantry?")
                .setPositiveButton("Delete", (d, w) -> {
                    db.deletePantryItem(item.getId());
                    refresh();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
