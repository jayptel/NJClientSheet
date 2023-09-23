package com.rhythm.njclienttsheet;

import android.app.Activity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
public class AddItemActivity extends Activity {
    private Button buttonSend;
    private EditText editTextName, editTextDescription, editTextAge;
    //private boolean isAddItemVisible = false; // To track the visibility of the Add Item fragment

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        // Initialize UI elements
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextAge = findViewById(R.id.editTextAge);
        buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("hello world", "JSON Data: add Button Click Log ");
                // Get data from EditText fields
                String name = editTextName.getText().toString();
                String description = editTextDescription.getText().toString();
                String age = editTextAge.getText().toString();
                // Create a JSON object with the data
                JSONObject postData = new JSONObject();
                try {
                    postData.put("action", "addItem");
                    postData.put("name", name);
                    postData.put("description", description);
                    postData.put("age", age);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("hello world", "JSON Data: add Button Click Log " + postData.toString());
                // Send the data to Google Sheets API
                sendDataToGoogleSheets(postData);

            }
        });

        // TODO: Implement OnClickListener for other buttons (Update, View, Delete).
    }



    private void sendDataToGoogleSheets(JSONObject postData) {
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
                                Toast.makeText(AddItemActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                                // Clear the EditText fields
                                editTextName.setText("");
                                editTextDescription.setText("");
                                editTextAge.setText("");
                            } else {
                                // Handle other cases, e.g., show an error message
                                Toast.makeText(AddItemActivity.this, "Server returned an unexpected response", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                            Toast.makeText(AddItemActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
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

                                    Toast.makeText(AddItemActivity.this, "Error parsing error response", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle non-JSON response
                                Log.e("Error", "Non-JSON response type: " + errorResponse.substring(0, 20));

                                Toast.makeText(AddItemActivity.this, "Received a non-JSON response", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            // Network error
                            Log.e("Error", "Network error: " + error.getMessage());

                            Toast.makeText(AddItemActivity.this, "Network error - please check your internet connection.", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(AddItemActivity.this, "Error sending data", Toast.LENGTH_SHORT).show();
                    }
                });

// Add the request to the Volley request queue

        requestQueue.add(jsonObjectRequest);

    }


    // TODO: Implement methods for Update, View, and Delete operations.

}
