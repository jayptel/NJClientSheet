package com.rhythm.njclienttsheet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rhythm.njclienttsheet.adapters.ItemAdapter;
import com.rhythm.njclienttsheet.models.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private Button buttonAdd;
   // private Button buttonSend;
    //private EditText editTextName, editTextDescription, editTextAge;
   private AddItemFragment addItemFragment;
   private ItemListFragment itemListFragment;
    private RequestQueue requestQueue;
    private boolean isAddItemVisible = false; // To track the visibility of the Add Item fragment
    private boolean isViewItemVisible = false;
    private List<Item> itemList = new ArrayList<>();
    private static final int REQUEST_EDIT_ITEM = 1;
    private ItemAdapter itemAdapter; // Declare itemAdapter as a class-level variable
    private Button buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        Log.d("hello wolrd", "mainactive lunch next loaddataanddisplay");
// Initialize the itemAdapter and set it to the RecyclerView
        itemAdapter = new ItemAdapter(itemList); // itemList should be initialized here
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(itemAdapter);
        showViewItemListFragment();
        // Initialize UI elements
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AddItemActivity
              // Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
              // startActivity(intent);

                if (isAddItemVisible) {
                    // Remove the Add Item fragment
                    removeAddItemFragment();
                    //Show View Data  showViewItemListFragment();
                    showViewItemListFragment();
                    Log.d("hello wolrd", "checking add view and hide");
                } else {
                    // Show the Add Item fragment
                    showAddItemFragment();
                    Log.d("hello wolrd", "checking add view and visible ");
                }
                isAddItemVisible = !isAddItemVisible;

            }
        });

        // TODO: Implement OnClickListener for other buttons (Update, View, Delete).
        // TODO:Implement OnClickListener for View buttons
        // Insert the OnClickListener for the "View" button here
        Button buttonView = findViewById(R.id.buttonView);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment with ItemListFragment when "View" button is clicked

                if (isViewItemVisible) {
                    // Remove the Add Item fragment
                    removeViewItemListFragment();
                    Log.d("hello wolrd", "checking Add view List  and hide");
                } else {
                    // Show the Add Item fragment
                    showViewItemListFragment();
                    Log.d("hello wolrd", "checking add view List and visible ");
                }
                isViewItemVisible = !isViewItemVisible;
            }
        });

        buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete selected items from the Google Sheet here
                Log.d("MainActivity", "Delete button clicked");

                // Call getSelectedItems to retrieve the selected items
                List<Item> selectedItems = itemAdapter.getSelectedItems();

                // Check if there are any selected items
                if (!selectedItems.isEmpty()) {
                    // Remove selected items from the itemList
                    itemList.removeAll(selectedItems);

                    // Update the adapter
                    itemAdapter.notifyDataSetChanged();

                    // Perform deletion in Google Sheets for the selected items
                    for (Item itemToDelete : selectedItems) {
                        Log.d("MainActivity", "Deleting item: " + itemToDelete.getName());
                        deleteItemFromSheet(itemToDelete.getName()); // Pass the item's name or identifier
                    }

                    // Clear the list of selected items after deletion
                    selectedItems.clear();
                } else {
                    // No items selected, show a message or handle as needed
                    Toast.makeText(MainActivity.this, "No items selected for deletion", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteItemFromSheet(String itemName) {
        // Create a request URL with the item name to delete
        String url = "https://script.google.com/macros/s/AKfycbzejRLzXfkpJPgs1hF6vAsLqo31CrVz2vq-J-bhDask-0YSNuQ3HGlw-ScE9JRH1yj-Fg/exec?itemName=" + itemName; // Replace with your web app URL

        // Log the URL before making the request
        Log.d("MainActivity", "Delete request URL: " + url);


        // Send a GET request to your Google Apps Script web app to delete the item
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response, e.g., show a success message
                        Log.d("MainActivity", "Delete response: " + response);
                        // Handle the response, e.g., show a success message
                        if ("Success".equals(response)) {
                            // Handle success, e.g., show a success message to the user
                            Toast.makeText(MainActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle other cases, e.g., show an error message
                            Toast.makeText(MainActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors, including network errors and response errors
                        Toast.makeText(MainActivity.this, "Error sending delete request", Toast.LENGTH_SHORT).show();
                        Log.e("MainActivity", "Error sending delete request", error);
                    }
                });

        // Add the request to the Volley request queue
        requestQueue.add(stringRequest);
    }

    // TODO: Below  For Add item Fragmment Show and Hide layout mehtod
    private void showAddItemFragment() {
       // FragmentManager fragmentManager = getSupportFragmentManager();
       // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment addItemFragment = new AddItemFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, addItemFragment)
                .addToBackStack(null)
                .commit();
        // Assign the fragment instance to addItemFragment
        this.addItemFragment = (AddItemFragment) addItemFragment;
            // Create a new instance of the AddItemFragment and add it to the container
            // AddItemFragment addItemFragment = new AddItemFragment();
            // fragmentTransaction.add(R.id.fragmentContainer, addItemFragment);

        //Commit the transaction
      // fragmentTransaction.commit();
        Log.d("hello wolrd", "add add view ");

    }

    private void removeAddItemFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Remove the AddItemFragment from the container
            fragmentTransaction.remove(fragment);

            // Commit the transaction
            fragmentTransaction.commit();
            Log.d("hello wolrd", "remove add view");
        }
    }
    // TODO: Above  For Add item Fragmment Show and Hide layout mehtod

//TODO: Below  For VIew Item Fragment SHow And Hide when Click Buttton View
    private void showViewItemListFragment() {
        // Create a new instance of ItemListFragment
        Fragment itemListFragment = new ItemListFragment();
        // Create a dummy list of items
        /*List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("Item 1", "Description 1", "Age 1"));
        itemList.add(new Item("Item 2", "Description 2", "Age 2"));
// Set the data for the ItemListFragment
        if (itemListFragment instanceof ItemListFragment) {
            ((ItemListFragment) itemListFragment).setItemList(itemList);
        }*/

        // Retrieve data from Google Sheets using an HTTP request
        //String url = "https://script.google.com/macros/s/AKfycbyLiHAb1XdRI1FSOyGTNROOo6oAnenNHc18XRF7IfzOs7TSWMYIE8G4GgRaN8wEkuna/exec?action=getData";
        String url = "https://script.google.com/macros/s/AKfycbyPx6b7h9XMur9krNwYmqqucKqziMHj0rRS7hXU1HLZZZc8Lj1m4qBIiPHHIu9sjDOBBg/exec";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse the JSON response and update the itemListFragment
                        // Here, you'll need to implement code to populate your itemListFragment with the data.
                        // You can parse the JSON response and create Item objects from it.
                        // Clear the itemList to remove old data
                        itemList.clear();
                        // Example parsing:
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject itemObject = jsonArray.getJSONObject(i);
                                String name = itemObject.getString("name");
                                String description = itemObject.getString("description");
                                String age = itemObject.getString("age");

                                // Create Item objects and add them to your list
                                Item item = new Item(name, description, age);
                                itemList.add(item);
                            }
                            // Log data retrieval
                            Log.d("DataRetrieval", "Data retrieved successfully");

                            // Update the itemListFragment with the data
                            if (itemListFragment != null) {
                                ((ItemListFragment) itemListFragment).updateItemList(itemList);
                                Log.d("MainActivity", "updateItemList called in showViewItemListFragment");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Log error in JSON parsing
                            Log.e("DataRetrieval", "Error parsing JSON response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(MainActivity.this, "Error retrieving data", Toast.LENGTH_SHORT).show();

                        Log.e("DataRetrieval", "Error retrieving data");
                    }
                });

        // Add the request to the Volley request queue
        requestQueue.add(jsonObjectRequest);

        // Replace the current fragment with ItemListFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, itemListFragment)
                .addToBackStack(null) // If you want to allow back navigation
                .commit();
        this.itemListFragment = (ItemListFragment) itemListFragment;
    }
    private void removeViewItemListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Remove the AddItemFragment from the container
            fragmentTransaction.remove(fragment);

            // Commit the transaction
            fragmentTransaction.commit();
            Log.d("MainActivity", "removeViewItemListFragment");
        }
    }
    //TODO: Above For VIew Item Fragment SHow And Hide when Click Buttton View


    // Define the sendDataToGoogleSheets method here, similar to how it's done in AddItemActivity
    // TODO: SendDataToGoogleSheets method here, similar to how it's done in AddItemActivity Send Data to Google Sheet
    public void sendDataToGoogleSheets(JSONObject postData) {
        // Your implementation for sending data to Google Sheets
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Replace with your Google Sheets API endpoint URL
        String apiUrl = "https://script.google.com/macros/s/AKfycbyLiHAb1XdRI1FSOyGTNROOo6oAnenNHc18XRF7IfzOs7TSWMYIE8G4GgRaN8wEkuna/exec";

        // Create a Volley request to send data to the API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                apiUrl,
                postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response from Google Sheets (e.g., success message)
                        try {
                            String message = response.getString("message");
                            if ("Success".equals(message)) {
                                // Handle success, e.g., show a success message to the user
                                if (addItemFragment != null) {
                                    Toast.makeText(MainActivity.this, "Text Box clear ", Toast.LENGTH_SHORT).show();
                                    addItemFragment.clearEditTextName();
                                    addItemFragment.clearEditTextDescription();
                                    addItemFragment.clearEditTextAge();
                                    addItemFragment.requestFocusOnNameEditText();
                                }
                                Toast.makeText(MainActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();

                                // Clear the EditText fields
                                // Clear the EditText fields in the AddItemFragment

                            } else {
                                // Handle other cases, e.g., show an error message
                                Toast.makeText(MainActivity.this, "Server returned an unexpected response", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                            Toast.makeText(MainActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors, including network errors and response errors
                        Log.e("hello world", "Error sending data", error);
                        if (error.networkResponse != null) {
                            String errorResponse = new String(error.networkResponse.data);
                            Log.e("hello world", "Error response: " + errorResponse);

                            // Check response is JSON before parsing
                            if (errorResponse.startsWith("{")) {
                                try {
                                    JSONObject json = new JSONObject(errorResponse);
                                    // Handle the JSON response here if needed
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    // Handle JSON parsing error
                                    Log.e("Error", "Non-JSON response type: " + errorResponse.substring(0, 20));

                                    Toast.makeText(MainActivity.this, "Error parsing error response", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle non-JSON response
                                Log.e("Error", "Non-JSON response type: " + errorResponse.substring(0, 20));

                                Toast.makeText(MainActivity.this, "Received a non-JSON response", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            // Network error
                            Log.e("Error", "Network error: " + error.getMessage());

                            Toast.makeText(MainActivity.this, "Network error - please check your internet connection.", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(MainActivity.this, "Error sending data", Toast.LENGTH_SHORT).show();
                    }
                });

// Add the request to the Volley request queue

        requestQueue.add(jsonObjectRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainActivity", "onActivityResult: requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (requestCode == REQUEST_EDIT_ITEM && resultCode == RESULT_OK) {
            Item updatedItem = (Item) data.getSerializableExtra("updatedItem");

            // Find the position of the updated item in the list
            int position = findItemPositionById(updatedItem.getName());

            if (position != -1) {
                // Replace the old item with the updated item
                itemList.set(position, updatedItem);

                // Notify the adapter of the change at the specific position
                itemAdapter.notifyItemChanged(position);

                // Notify the ItemListFragment with the updated data
                if (itemListFragment != null) {
                    itemListFragment.updateItemList(itemList);
                    Log.d("MainActivity", "updateItemList called in onActivityResult");
                }
            }
        }
    }
    private int findItemPositionById(String itemId) {
        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            if (item.getName().equals(itemId)) {
                return i;
            }
        }
        return -1; // Item not found in the list
    }

}