package com.example.to_do_list;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
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

        // Null checks before setting text
        if (modeltask != null) {
            if (holder.titleTextView != null) {
                holder.titleTextView.setText(modeltask.getTitle());

                holder.et.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "debug", Toast.LENGTH_SHORT).show();
//                        Log.d("AdapterUser", "EditText clicked");
//                        showDetailedPopup(holder.itemView.getContext(), modeltask);
                    }
                });

            }
            if (holder.notesTextView != null) {
                holder.notesTextView.setText(modeltask.getNotes());
            }
            if (holder.whenTextView != null) {
                holder.whenTextView.setText(modeltask.getWhen());
            }
            if (holder.whoTextView != null) {
                holder.whoTextView.setText(modeltask.getWho());
            }
        }
    }


    @Override
    public int getItemCount() {
        return userTask.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Define the views you want to display in each item
        public TextView titleTextView;
        public TextView notesTextView;
        public TextView whenTextView;
        public TextView whoTextView;
        public TextView et;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize your TextViews here
            titleTextView = itemView.findViewById(R.id.tasktitle);
            notesTextView = itemView.findViewById(R.id.taskNotes);
            whenTextView = itemView.findViewById(R.id.whenbtn);
            whoTextView = itemView.findViewById(R.id.whobtn);
            et=itemView.findViewById(R.id.tvTasks);
        }
    }

    private void showDetailedPopup(Context context, Modeltask modeltask) {
        Intent intent = new Intent(context, detailed_PopupActivity.class);
        intent.putExtra("tasks", (CharSequence) modeltask); // Pass the modeltask data if needed
        context.startActivity(intent);
    }
}
