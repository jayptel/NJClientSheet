package com.rhythm.njclienttsheet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {
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
                // Start the AddItemActivity
               Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
               startActivity(intent);

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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        /*Fragment addItemFragment = new AddItemFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, addItemFragment)
                .addToBackStack(null)
                .commit();*/
        // Create a new instance of the AddItemFragment and add it to the container
       AddItemFragment addItemFragment = new AddItemFragment();
       fragmentTransaction.add(R.id.fragmentContainer, addItemFragment);

        //Commit the transaction
       fragmentTransaction.commit();
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