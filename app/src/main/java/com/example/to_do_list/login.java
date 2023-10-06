package com.example.to_do_list;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.to_do_list.ModelUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {
    SharedPref mypref; // Declare SharedPref
    FirebaseFirestore firestore;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signup;
    private TextView forgetpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginbtn);
        signup = findViewById(R.id.anker);
        forgetpass = findViewById(R.id.fPass);
        firestore = FirebaseFirestore.getInstance();

        // Initialize SharedPref
        mypref = new SharedPref(getSharedPreferences("my_prefs", MODE_PRIVATE));

        signup.setOnClickListener(view -> {
            Intent intent = new Intent(login.this, signup.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            loginUser(email, password);
        });
    }

    private void loginUser(String email, String password) {
        firestore.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Valid login
                        Toast.makeText(login.this, "Logged in", Toast.LENGTH_LONG).show();

                        // Get the first document ID (assuming there's only one match)
                        String userId = task.getResult().getDocuments().get(0).getId();

                        // Save user data to SharedPref
                        firestore.collection("users").document(userId).get().addOnCompleteListener(userTask -> {
                            if (userTask.isSuccessful() && userTask.getResult() != null) {
                                DocumentSnapshot document = userTask.getResult();
                                if (document.exists()) {
                                    ModelUser user = document.toObject(ModelUser.class);
                                    mypref.setUser(user);

                                    // Start MainActivity
                                    startActivity(new Intent(login.this, MainActivity.class));
                                    finish(); // Finish the current login activity
                                }
                            }
                        });

                    } else {
                        Toast.makeText(login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
