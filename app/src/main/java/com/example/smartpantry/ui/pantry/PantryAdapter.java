package com.example.smartpantry.ui.pantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.model.PantryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom RecyclerView.Adapter that binds pantry items to item_pantry.xml.
 * Exposes click and delete callbacks so the Fragment stays in control.
 */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.VH> {

    public interface Listener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private final List<PantryItem> items = new ArrayList<>();
    private final Listener listener;

    public PantryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<PantryItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        PantryItem item = items.get(position);
        h.name.setText(item.getName());

        String qty = trimNumber(item.getQuantity()) + " " +
                (item.getUnit() == null ? "" : item.getUnit());
        h.qty.setText(qty.trim());

        String expiry = item.getExpiryDate();
        if (expiry != null && !expiry.isEmpty()) {
            h.expiry.setVisibility(View.VISIBLE);
            h.expiry.setText("Expires: " + expiry);
        } else {
            h.expiry.setVisibility(View.GONE);
        }

        h.itemView.setOnClickListener(v -> listener.onEdit(item));
        h.delete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String trimNumber(double d) {
        if (d == Math.floor(d)) return String.valueOf((long) d);
        return String.valueOf(d);
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView name, qty, expiry;
        final View delete;

        VH(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.item_name);
            qty = v.findViewById(R.id.item_qty);
            expiry = v.findViewById(R.id.item_expiry);
            delete = v.findViewById(R.id.item_delete);
        }
    }
}
