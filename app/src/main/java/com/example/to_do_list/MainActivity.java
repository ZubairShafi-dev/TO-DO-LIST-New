package com.example.to_do_list;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections; // Import Collections class
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    SharedPref sharedPref;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button addbtn;
    Button saveButton;  // You may use this button for saving tasks
    Button removeButton;  // You may use this button for removing tasks
    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    List<Modeltask> modeltaskList;
    TextView mytask,detailTxtview;
    AdapterUser adapterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawer);
        addbtn = findViewById(R.id.AddBtn);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.RecyclerView);
        mytask = findViewById(R.id.tasks);
        setSupportActionBar(toolbar);
        detailTxtview=findViewById(R.id.deatailTV);

        // Initialize SharedPref   // to check data is stored in sharedpref
        sharedPref = new SharedPref(getSharedPreferences("my_prefs", MODE_PRIVATE));

        ModelUser user = sharedPref.getUser();

        if (user != null) {
            // A user was found in SharedPref, display their username
            Toast.makeText(MainActivity.this, "Username: " + user.getUserName(), Toast.LENGTH_SHORT).show();
        } else {
            // No user found in SharedPref, handle this case (e.g., show a login screen or take appropriate action)
            // For example, you can start the login activity if no user is found.
            startActivity(new Intent(MainActivity.this, login.class));
            finish(); // Finish the current activity to prevent going back to it
        }
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the PopUpActivity when the "Add" button is clicked
                startActivity(new Intent(MainActivity.this, PopUpActivity1.class));
            }
        });
//        ed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////               Intent intent;
////               intent.setContentView(R.layout.detail_popup);
//                startActivity(new Intent(MainActivity.this,R.layout.detail_popup));
//            }
//        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close
        ) {
            private boolean isDrawerOpened = false;

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                // Calculate the new margin for the Toolbar based on the slideOffset
                int maxMargin = drawerView.getWidth();
                int newMargin = (int) (maxMargin * slideOffset);

                // Adjust the Toolbar's margin to move it from under the drawer
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
                layoutParams.setMargins(newMargin, 0, 0, 0);
                toolbar.setLayoutParams(layoutParams);
                // Set the custom hamburger icon drawable when the drawer is opened
                if (slideOffset > 0.0f && !isDrawerOpened) {
                    toolbar.setNavigationIcon(R.drawable.baseline_menu_24);
                    isDrawerOpened = true;
                } else if (slideOffset == 0.0f && isDrawerOpened) {
                    // Set the hamburger icon back when the drawer is closed
                    toolbar.setNavigationIcon(R.drawable.baseline_menu_24);
                    isDrawerOpened = false;
                }
            }
        };

        drawerLayout.addDrawerListener(toggle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toggle.syncState();

        mytask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click on the "Add" button, e.g., open a dialog to add a task
                // You can implement this functionality as per your requirements.
                // After adding or saving a task, you can update the RecyclerView.

                // Show or hide the RecyclerView based on your requirements
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE); // Hide the RecyclerView
                } else {
                    recyclerView.setVisibility(View.VISIBLE); // Show the RecyclerView
                }

                // Update the RecyclerView with the latest task data
                adapterUser.notifyDataSetChanged();
            }
        });

        // Initialize RecyclerView and adapter
        firestore = FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        modeltaskList = new ArrayList<>();
        adapterUser = new AdapterUser(modeltaskList, MainActivity.this);
        recyclerView.setAdapter(adapterUser);

        // Load tasks initially when the activity starts
        getTasks();
    }

    private void getTasks() {
        firestore.collection("tasks").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        modeltaskList.clear(); // Clear the list before adding data
                        for (DocumentSnapshot document : task.getResult()) {
                            Modeltask tasks = document.toObject(Modeltask.class);
                            modeltaskList.add(tasks);
                        }

                        // Reverse the order of tasks to show the latest on top
                        Collections.reverse(modeltaskList);

                        adapterUser.notifyDataSetChanged(); // Notify the adapter of data change
                        Log.d("MainActivity", "Data retrieved successfully. Number of items: " + modeltaskList.size());
                    } else {
                        // Handle task failure
                        Log.e("MainActivity", "Error getting task documents: ", task.getException());
                    }
                });
    }
}