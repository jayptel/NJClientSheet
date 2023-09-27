package com.rhythm.njclienttsheet.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.rhythm.njclienttsheet.models.Item;
import com.rhythm.njclienttsheet.R; // Replace with your actual resource import


import android.widget.TextView;
import com.rhythm.njclienttsheet.EditItemActivity;
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> itemList;

    // Constructor to initialize your data source (itemList)
    public ItemAdapter(List<Item> itemList) {
        if (itemList == null) {
            this.itemList = new ArrayList<>(); // Initialize with an empty list if itemList is null
        } else {
            this.itemList = itemList;
        }
    }

    // Method to set the itemList data
    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged(); // Notify the adapter of data changes
    }
    // Create and return a new ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout XML here
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false); // Replace with your actual item layout

        return new ViewHolder(view);
    }

    // Bind data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the item at the current position
        Item item = itemList.get(position);

        // Set data to your ViewHolder's views
        holder.nameTextView.setText(item.getName());
        holder.descriptionTextView.setText(item.getDescription());
        holder.ageTextView.setText(item.getAge());
// Update the checkbox state based on the isSelected field
        holder.checkBox.setChecked(item.isSelected());
      // Add an OnClickListener to the checkbox
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setSelected(holder.checkBox.isChecked()); // Update the selected state

                Log.d("ItemAdapter", "Checkbox clicked for item: " + item.getName() + ", isChecked: " + holder.checkBox.isChecked());
            }
        });
        // Set an OnClickListener for the "Edit" button
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Edit" button click here
                // You can navigate to the EditItemFragment or a new activity and pass the item data.
                // For example, start an EditItemActivity and pass the item's data.
                // Create an Intent to start the EditItemActivity
                Intent intent = new Intent(v.getContext(), EditItemActivity.class);
                // Pass the selected item's data to the EditItemActivity
                intent.putExtra("selectedItem", item);
                v.getContext().startActivity(intent);
            }
        });
    }
    // Implement a method to get selected items for deletion
    public List<Item> getSelectedItems() {
        List<Item> selectedItems = new ArrayList<>();
        for (Item item : itemList) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }
    // Return the number of items in your data source
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Item> itemList) {
        itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    // ViewHolder class to hold your item's views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView ageTextView;
        public View editButton;
        public MaterialCheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView); // Replace with your actual view IDs
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            ageTextView = itemView.findViewById(R.id.ageTextView);
            editButton = itemView.findViewById(R.id.buttonEdit);
            checkBox=itemView.findViewById(R.id.checkBox);
        }
    }
}
