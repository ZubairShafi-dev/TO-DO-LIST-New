package com.example.to_do_list;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
//        detailTxtview=findViewById(R.id.deatailTV);



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
        ModelUser user = sharedPref.getUser();
        if (user != null) {
            String userId = user.getId();
            Toast.makeText(this, "User ID: " + userId, Toast.LENGTH_SHORT).show();
            //Log.d("MainActivity", "User ID: " + userId); // Log the user ID for debugging

            firestore.collection("tasks")
                    .whereEqualTo("id", userId) // Use "userId" as the field name
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                modeltaskList.clear(); // Clear the list before adding data
                                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                    Modeltask tasks = document.toObject(Modeltask.class);
                                    String name=document.getString("id");
                                    Toast.makeText(MainActivity.this, ""+name, Toast.LENGTH_SHORT).show();
                                    modeltaskList.add(tasks);

                                }

                                // Reverse the order of tasks to show the latest on top
                                Collections.reverse(modeltaskList);

                                adapterUser.notifyDataSetChanged(); // Notify the adapter of data change

                                Toast.makeText(MainActivity.this, "Tasks retrieved: " + modeltaskList.size(), Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle task failure
                                Log.e("MainActivity", "Error getting task documents: ", task.getException());
                                Toast.makeText(MainActivity.this, "Error getting tasks", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // Handle the case when the user is null (not logged in)
            // For example, you can redirect the user to the login screen
            startActivity(new Intent(MainActivity.this, login.class));
            finish(); // Finish the current activity to prevent going back to it
        }
    }





}