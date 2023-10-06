package com.example.to_do_list;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_do_list.Modeltask;
import com.example.to_do_list.R;
import com.example.to_do_list.detailed_PopupActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder> {
    private List<Modeltask> userTask;
    private Context mContext;

    public AdapterUser(List<Modeltask> userTask, Context context) {
        this.mContext = context;
        this.userTask = userTask;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclic_items, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Modeltask modeltask = userTask.get(position);

        // Set the task title
        holder.et.setText(modeltask.getTitle());

        // Set the checkbox state based on the task's completion status
        holder.checkBox.setChecked(modeltask.isCompleted());

        // Handle checkbox click
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the completion status of the task
                modeltask.setCompleted(isChecked);
                if (isChecked) {
                    deleteTaskFromFirebase(modeltask);
                } else {
                    updateTaskInFirebase(modeltask);
                }
            }
        });

        // Handle click to show detailed task information
        holder.et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailedPopup(modeltask);
            }
        });
    }
    @Override
    public int getItemCount() {
        return userTask.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView et;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            et = itemView.findViewById(R.id.tvTasks);
            checkBox = itemView.findViewById(R.id.myCheckBox);
        }
    }
    private void deleteTaskFromFirebase(Modeltask modeltask) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String Id = modeltask.getId(); // Get the task ID using your getId method

        if (Id != null) {
            Log.d("FirebaseDelete", "Attempting to delete task with ID: " + Id);

            firestore.collection("tasks") // Replace with your Firebase collection name
                    .document(Id) // Use the task ID to identify the document to delete
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Document successfully deleted
                            Toast.makeText(mContext, "Task deleted", Toast.LENGTH_LONG).show();
                            Log.d("FirebaseDelete", "Task with ID " + Id + " deleted successfully");

                            // Remove the task from the RecyclerView
                            int position = userTask.indexOf(modeltask);
                            if (position != -1) {
                                userTask.remove(position);
                                notifyItemRemoved(position);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle the error
                            Toast.makeText(mContext, "Error deleting task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FirebaseDelete", "Error deleting task with ID " + Id + ": " + e.getMessage());
                        }
                    });
        }
    }



    private void updateTaskInFirebase(Modeltask modeltask) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String Id = modeltask.getId(); // Get the task ID using your getId method

        if (Id != null) {
            firestore.collection("tasks")
                    .document(Id)
                    .set(modeltask) // Update the task in Firestore
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Task successfully updated
                            Toast.makeText(mContext, "Task updated", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle the error
                            Toast.makeText(mContext, "Error updating task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FirebaseUpdate", "Error updating task with ID " + Id + ": " + e.getMessage());
                        }
                    });
        }
    }


    private void showDetailedPopup(Modeltask modeltask) {
        Intent intent = new Intent(mContext, detailed_PopupActivity.class);
        intent.putExtra("tasks", new Gson().toJson(modeltask));
        mContext.startActivity(intent);
    }
}
