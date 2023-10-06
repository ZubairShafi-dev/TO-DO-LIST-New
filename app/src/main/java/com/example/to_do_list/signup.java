package com.example.to_do_list;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

public class signup extends AppCompatActivity {
    FirebaseFirestore firestore;
    EditText username,email,password,confirmpassword;
    Button signupbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username=findViewById(R.id.UserName);
        email=findViewById(R.id.emailSignup);
        password=findViewById(R.id.password);
        confirmpassword=findViewById(R.id.confirmPassword);
        signupbtn=findViewById(R.id.signUpButton);
        firestore=FirebaseFirestore.getInstance();


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = username.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String ConfirmPassword = confirmpassword.getText().toString();
                registration(ConfirmPassword, Email, Password,Username );
            }
        });



    }

    private void registration( String ConfirmPassword, String Email, String Password,String Username) {
        ModelUser modelUser = new ModelUser(ConfirmPassword, Email,null, Password,Username );

        firestore.collection("users").whereEqualTo("email", Email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Email already exists
                            Toast.makeText(signup.this, "Email is already in use", Toast.LENGTH_SHORT).show();
                        } else {
                            // Email is not in use, proceed with registration
                            firestore.collection("users").add(modelUser)
                                    .addOnCompleteListener(registerTask -> {
                                        if (registerTask.isSuccessful()) {
                                            String documentId = registerTask.getResult().getId();
                                            // Set the document ID in your ModelUser object
                                            modelUser.setId(documentId);

                                            // Now, update the user's document with the ID
                                            firestore.collection("users").document(documentId)
                                                    .set(modelUser)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(signup.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(signup.this, login.class));
                                                            // Optionally, navigate to another screen or perform other actions
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(signup.this, "Failed to update user data", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(signup.this, "Failed to register", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(signup.this, "Failed to check email existence", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}