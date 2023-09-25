package com.rhythm.njclienttsheet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rhythm.njclienttsheet.adapters.ItemAdapter;
import com.rhythm.njclienttsheet.models.Item;

import java.util.List;

public class ItemListFragment extends Fragment {
    private List<Item> itemList;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_item, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(adapter);

        // Populate the adapter with data (retrieve data from Google Sheets)

        return rootView;
    }
    // Method to set data for the adapter
    /*public void setItemList(List<Item> itemList) {
        this.itemList = itemList;

        // Check if the adapter has been initialized
        if (adapter != null) {
            adapter.setItemList(itemList);
            adapter.notifyDataSetChanged(); // Notify the adapter of data changes
        }
    }*/

    // Add methods to populate the adapter with data (e.g., retrieve data from Google Sheets)
    public void updateItemList(List<Item> itemList) {
        // Update the UI with the new itemList
        this.itemList = itemList;

        if (adapter != null) {
            adapter.setItemList(itemList);
        }
        // Assuming you have a RecyclerView and its adapter set up in your fragment
       /* if (recyclerView != null && adapter != null) {
            Log.d("ItemListFragment", "updateItemList: " + itemList.toString());
            // Clear the existing data in the adapter
            adapter.clear();

            // Add the new items to the adapter
            adapter.addAll(itemList);

            // Notify the adapter that the dataset has changed
            adapter.notifyDataSetChanged();
        }*/
    }
}
