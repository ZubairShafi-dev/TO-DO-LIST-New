package com.example.to_do_list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

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
                Modeltask modeltask = new Modeltask(TaskTitle, TaskNotes, ButtonWhen, ButtonWho);
                firestore.collection("tasks").add(modeltask)
                        .addOnCompleteListener(registerTask -> {
                            if (registerTask.isSuccessful()) {
                                Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show();
                                // Optionally, navigate to another screen or perform other actions
                            } else {
                                Toast.makeText(this, "Failed to save task", Toast.LENGTH_SHORT).show();
                            }
                        });
            }


        }