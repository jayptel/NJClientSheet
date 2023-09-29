package com.rhythm.njclienttsheet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

public class AddItemFragment extends Fragment {

    private EditText editTextId, editTextName, editTextDescription, editTextAge;
    private Button buttonSend;

    public AddItemFragment() {
        // Required empty public constructor

    }
    public void clearEditTextId() {
        if (editTextId != null) {
            editTextId.getText().clear();
        }
    }

    // Public method to clear the text of editTextName
    public void clearEditTextName() {
        if (editTextName != null) {
            editTextName.getText().clear();
        }
    }

    // Public method to set focus on the name EditText
    public void requestFocusOnNameEditText() {
        if (editTextName != null) {
            editTextName.requestFocus();
        }
    }

    // Public method to clear the text of editTextDescription
    public void clearEditTextDescription() {
        if (editTextDescription != null) {
            editTextDescription.getText().clear();
        }
    }

    // Public method to clear the text of editTextAge
    public void clearEditTextAge() {
        if (editTextAge != null) {
            editTextAge.getText().clear();
        }
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.add_item, container, false);
        View rootView = inflater.inflate(R.layout.add_item, container, false);
// Initialize UI elements
        //editTextId = rootView.findViewById(R.id.editTextId);
        editTextName = rootView.findViewById(R.id.editTextName);
        editTextDescription = rootView.findViewById(R.id.editTextDescription);
        editTextAge = rootView.findViewById(R.id.editTextAge);
        buttonSend = rootView.findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get data from EditText fields
                //String id = editTextId.getText().toString();
                String name = editTextName.getText().toString();
                String description = editTextDescription.getText().toString();
                String age = editTextAge.getText().toString();

                // Create a JSON object with the data
                JSONObject postData = new JSONObject();
                try {
                    postData.put("action", "addItem");
                   // postData.put("id", id);
                    postData.put("name", name);
                    postData.put("description", description);
                    postData.put("age", age);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Send the data to Google Sheets API (you can call a method in MainActivity to do this)
                ((MainActivity) requireActivity()).sendDataToGoogleSheets(postData);
            }
        });

        return rootView;
    }


}
