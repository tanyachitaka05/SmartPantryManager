package com.example.smartpantry.ui.pantry;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;

/**
 * Add/Edit Ingredient screen. Reached from the pantry list via an Intent.
 * Handles both Create (no id extra) and Update (id extra present), and
 * performs input validation before saving.
 */
public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "extra_item_id";

    private DatabaseHelper db;
    private long editingId = 0; // 0 = creating a new item

    private EditText nameInput, qtyInput, expiryInput;
    private Spinner unitSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = new DatabaseHelper(this);
        nameInput = findViewById(R.id.input_name);
        qtyInput = findViewById(R.id.input_qty);
        expiryInput = findViewById(R.id.input_expiry);
        unitSpinner = findViewById(R.id.spinner_unit);

        ArrayAdapter<CharSequence> units = ArrayAdapter.createFromResource(
                this, R.array.units, android.R.layout.simple_spinner_item);
        units.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(units);

        // If an id was passed in, we are editing - pre-fill the form.
        editingId = getIntent().getLongExtra(EXTRA_ID, 0);
        if (editingId > 0) {
            setTitle("Edit Ingredient");
            prefill(db.getPantryItem(editingId));
        } else {
            setTitle("Add Ingredient");
        }

        Button save = findViewById(R.id.btn_save);
        save.setOnClickListener(v -> save());
    }

    private void prefill(PantryItem item) {
        if (item == null) return;
        nameInput.setText(item.getName());
        qtyInput.setText(trimNumber(item.getQuantity()));
        expiryInput.setText(item.getExpiryDate());
        setUnitSelection(item.getUnit());
    }

    private void setUnitSelection(String unit) {
        if (unit == null) return;
        for (int i = 0; i < unitSpinner.getCount(); i++) {
            if (unit.equalsIgnoreCase(unitSpinner.getItemAtPosition(i).toString())) {
                unitSpinner.setSelection(i);
                return;
            }
        }
    }

    private void save() {
        String name = nameInput.getText().toString().trim();
        String qtyStr = qtyInput.getText().toString().trim();

        // --- input validation ---
        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Please enter an ingredient name");
            nameInput.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(qtyStr)) {
            qtyInput.setError("Please enter a quantity");
            qtyInput.requestFocus();
            return;
        }
        double qty;
        try {
            qty = Double.parseDouble(qtyStr);
        } catch (NumberFormatException e) {
            qtyInput.setError("Quantity must be a number");
            qtyInput.requestFocus();
            return;
        }
        if (qty <= 0) {
            qtyInput.setError("Quantity must be greater than zero");
            qtyInput.requestFocus();
            return;
        }

        PantryItem item = new PantryItem();
        item.setId(editingId);
        item.setName(name);
        item.setQuantity(qty);
        item.setUnit(unitSpinner.getSelectedItem().toString());
        item.setExpiryDate(expiryInput.getText().toString().trim());

        db.savePantryItem(item);
        Toast.makeText(this, editingId > 0 ? "Item updated" : "Item added",
                Toast.LENGTH_SHORT).show();
        finish(); // back to the pantry list, which reloads in onResume
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private String trimNumber(double d) {
        if (d == Math.floor(d)) return String.valueOf((long) d);
        return String.valueOf(d);
    }
}
