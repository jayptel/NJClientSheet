package com.rhythm.njclienttsheet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private Button buttonAdd;
   // private Button buttonSend;
    //private EditText editTextName, editTextDescription, editTextAge;
   private AddItemFragment addItemFragment;
    private boolean isAddItemVisible = false; // To track the visibility of the Add Item fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

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
    // Define the sendDataToGoogleSheets method here, similar to how it's done in AddItemActivity
    public void sendDataToGoogleSheets(JSONObject postData) {
        // Your implementation for sending data to Google Sheets
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Replace with your Google Sheets API endpoint URL
        String apiUrl = "/exec";

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
    // TODO: Implement methods for Update, View, and Delete operations.
/*
    private Button buttonAdd;
    private boolean isAddItemVisible = false; // To track the visibility of the Add Item fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate add_item.xml layout as a fragment when "Add" button is clicked
                Fragment addItemFragment = new AddItemFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, addItemFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // TODO: Implement OnClickListener for other buttons (Update, View, Delete).
    }

    // TODO: Implement methods for Update, View, and Delete operations.*/
}
