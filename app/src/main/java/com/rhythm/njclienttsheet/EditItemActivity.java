package com.rhythm.njclienttsheet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rhythm.njclienttsheet.adapters.ItemAdapter;
import com.rhythm.njclienttsheet.models.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class EditItemActivity extends AppCompatActivity {
    private EditText editNameEditText, editDescriptionEditText, editAgeEditText;
    private Button saveButton;
    private Item selectedItem; // This will hold the item data to be edited
    private static final int REQUEST_EDIT_ITEM = 1;
    private List<Item> itemList; // Declare itemList as a class-level variable
    private ItemAdapter itemAdapter; // Declare itemAdapter as a class-level variable

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Initialize UI elements
        editNameEditText = findViewById(R.id.editNameEditText);
        editDescriptionEditText = findViewById(R.id.editDescriptionEditText);
        editAgeEditText = findViewById(R.id.editAgeEditText);
        saveButton = findViewById(R.id.saveButton);

        // Retrieve the selected item data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            selectedItem = (Item) intent.getSerializableExtra("selectedItem");
        }

        // Populate the UI with the selected item's data (if available)
        if (selectedItem != null) {
            editNameEditText.setText(selectedItem.getName());
            editDescriptionEditText.setText(selectedItem.getDescription());
            editAgeEditText.setText(selectedItem.getAge());
        }

        // Set an OnClickListener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Save" button click
                // Get the edited data from the EditText fields
                String name = editNameEditText.getText().toString();
                String description = editDescriptionEditText.getText().toString();
                String age = editAgeEditText.getText().toString();
// Create a new Item object with the edited data
                Item updatedItem = new Item(name, description, age);

                // Update the selected item's data (if needed)
                if (selectedItem != null) {
                    selectedItem.setName(name);
                    selectedItem.setDescription(description);
                    selectedItem.setAge(age);
// Send a PUT request to update the data
                    //UpdateItemTask updateTask = new UpdateItemTask();
// Call async task to send update request
                    new UpdateItemTask().execute(updatedItem);

                   // updateTask.execute(selectedItem);
                    /*// You can now send the updated data back to the previous activity or update it in your data source (e.g., Google Sheets)
                    // For simplicity, I'm assuming you want to send it back to the calling activity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedItem", selectedItem);
                    // Set the result code and attach the intent with updated data
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Finish this activity and return to the calling activity*/
                } else {
                    // Handle the case when no item data is available
                    Toast.makeText(EditItemActivity.this, "No item data available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Initialize itemList and itemAdapter using data passed through the intent
        itemList = (List<Item>) intent.getSerializableExtra("itemList");
        itemAdapter = new ItemAdapter(itemList); // Use your actual adapter class

    }
    // Async task to send PUT request
    private class UpdateItemTask extends AsyncTask<Item, Void, String> {

        @Override
        protected String doInBackground(Item... items) {

            // Get updated item
            Item item = items[0];

            try {

                // Convert item to JSON
                JSONObject json = new JSONObject();
                json.put("name", item.getName());
                json.put("description", item.getDescription());
                json.put("age", item.getAge());

                // Send PUT request to Google Apps Script
                URL url = new URL("https://script.google.com/macros/s/AKfycbxvorVgeVUlFQ9ENZssMNMEfC2jHnhjpF-JoUpPDnVxwR3W_VJ_u6yjBC99igThGQ/exec");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                Log.d("hello", "doInBackground: "+os);
                os.flush();
                os.close();

                // Get response
                int code = conn.getResponseCode();
                String responseMessage = conn.getResponseMessage();

                Log.d("UpdateItemTask", "Response code: " + code);
                Log.d("UpdateItemTask", "Response message: " + responseMessage);

                return (code == 200) ? "Success" : "Error";

            } catch (Exception e) {
                Log.e("UpdateItemTask", "Error sending data", e);
                return "Error";
            }

        }

        @Override
        protected void onPostExecute(String result) {

            Log.d("UpdateItemTask", "onPostExecute: Result = " + result);
                if (result.equals("Success")) {
                    // Show success message

                    // Update the selected item's data (if needed)
                    if (selectedItem != null) {
                        selectedItem.setName(selectedItem.getName());
                        selectedItem.setDescription(selectedItem.getDescription());
                        selectedItem.setAge(selectedItem.getAge());
                    }

                    // Pass the updated item back to the previous activity
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("updatedItem", selectedItem);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    // Show error message
                    Toast.makeText(EditItemActivity.this, "Failed to update item!", Toast.LENGTH_SHORT).show();
                    Log.d("UpdateItemTask", "Error response: " + result);
                }
        }

    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_ITEM && resultCode == RESULT_OK) {
            Item updatedItem = (Item) data.getSerializableExtra("updatedItem");

            // Find the position of the updated item in the list
            int position = findItemPositionById(updatedItem.getName());

            if (position != -1) {
                // Replace the old item with the updated item
                itemList.set(position, updatedItem);

                // Notify the adapter of the change at the specific position
                itemAdapter.notifyItemChanged(position);
            }
        }
    }

    // Helper method to find the position of an item in the list by its unique identifier
    private int findItemPositionById(String itemId) {
        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            if (item.getName().equals(itemId)) {
                return i;
            }
        }
        return -1; // Item not found in the list
    }
   /* // Implement a method to convert the updated item to JSON or another suitable format
    private String createJsonPayload(Item updatedItem) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", updatedItem.getName());
            jsonObject.put("description", updatedItem.getDescription());
            jsonObject.put("age", updatedItem.getAge());

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null; // Handle the error appropriately in your code
        }
    }*/

}
