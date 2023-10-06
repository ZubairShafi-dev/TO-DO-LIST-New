package com.example.to_do_list;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class detailed_PopupActivity extends AppCompatActivity {

    TextView detailTV, contentTV;
    Button closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_popup);

        detailTV = findViewById(R.id.detailTextView);
        contentTV = findViewById(R.id.contentTextView);
        closeBtn=findViewById(R.id.closeBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(detailed_PopupActivity.this,MainActivity.class));
            }
        });

        Modeltask modeltask = new Gson().fromJson(getIntent().getStringExtra("tasks"), Modeltask.class);

        detailTV.setText(modeltask.getTitle());
        contentTV.setText(modeltask.getNotes());
    }
    }
