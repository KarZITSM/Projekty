package com.example.uaim_lab;


// TaskDetailActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        long deadlineMillis = intent.getLongExtra("deadline", 0);
        String status = intent.getStringExtra("status");
        int position = intent.getIntExtra("position", -1);

        TextView titleView = findViewById(R.id.taskDetailTitle);
        TextView descriptionView = findViewById(R.id.taskDetailDescription);
        TextView deadlineView = findViewById(R.id.taskDetailDeadline);
        TextView statusView = findViewById(R.id.taskDetailStatus);
        Button doneButton = findViewById(R.id.doneButton);
        Button notDoneButton = findViewById(R.id.notDoneButton);
        Button backButton = findViewById(R.id.backButton);

        titleView.setText(title);
        descriptionView.setText(description);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        deadlineView.setText(sdf.format(new Date(deadlineMillis)));
        statusView.setText(status);

        if (status.equals("Przeterminowane")) {
            doneButton.setEnabled(false); // Uniemożliwienie oznaczenia jako wykonane
        }

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult("Wykonane", position);
            }
        });

        notDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult("Niewykonane", position);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(TaskDetailActivity.this, MainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
               finish();
           }
        });
    }

    private void sendResult(String status, int position) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("status", status);
        resultIntent.putExtra("position", position);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
