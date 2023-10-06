package com.example.to_do_list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

public class PopUpActivity1 extends AppCompatActivity {
    FirebaseFirestore firestore;
    EditText title,notes;
    Button whenbtn,whobtn,removebtn ,savebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up1);

                title=findViewById(R.id.tasktitle);
                notes=findViewById(R.id.taskNotes);
                whenbtn=findViewById(R.id.whenbtn);
                whobtn=findViewById(R.id.whobtn);
                removebtn=findViewById(R.id.removebtn);
                savebtn=findViewById(R.id.savebtn);
                firestore=FirebaseFirestore.getInstance();






                savebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String TaskTitle = title.getText().toString();
                        String TaskNotes = notes.getText().toString();
                        String ButtonWhen = whenbtn.getText().toString();
                        String ButtonWho = whobtn.getText().toString();
                        savetask(TaskTitle,TaskNotes,ButtonWhen,ButtonWho);
                    }
                });

            }

    private void savetask(String TaskTitle, String TaskNotes, String ButtonWhen, String ButtonWho) {
        SharedPref sharedPref = new SharedPref(getSharedPreferences("my_prefs", MODE_PRIVATE));
        ModelUser currentUser = sharedPref.getUser();
        if (currentUser != null) {

            Modeltask modeltask = new Modeltask(false, currentUser.id, TaskNotes, TaskTitle, ButtonWhen, ButtonWho);
             String Userid=currentUser.getId();
            Toast.makeText(this, "UserId in Model Task"+Userid, Toast.LENGTH_SHORT).show();

            // Save the task document to Firestore
            firestore.collection("tasks").document(currentUser.id).set(modeltask)
                    .addOnCompleteListener(registerTask -> {
                        if (registerTask.isSuccessful()) {
                            Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(PopUpActivity1.this, "Failed to save task", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Handle the case where the current user is not available
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }




}