package com.example.uaim_lab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Task> tasks;
    private ArrayAdapter<Task> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks = new ArrayList<>();
        tasks.add(new Task("Zadanie 1", "Opis zadania 1", new Date(System.currentTimeMillis() + 3600000), "Niewykonane"));
        tasks.add(new Task("Zadanie 2", "Opis zadania 2", new Date(System.currentTimeMillis() - 3600), "Niewykonane"));

        for (Task task : tasks) {
            if (task.getDeadline().before(new Date()) && task.getStatus().equals("Niewykonane")) {
                task.setStatus("Przeterminowane");
            }
        }

        ListView listView = findViewById(R.id.listView);
        adapter = new TaskAdapter(this, tasks);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
                Task task = tasks.get(position);
                intent.putExtra("title", task.getTitle());
                intent.putExtra("description", task.getDescription());
                intent.putExtra("deadline", task.getDeadline().getTime());
                intent.putExtra("status", task.getStatus());
                intent.putExtra("position", position);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            String status = data.getStringExtra("status");
            if (position != -1) {
                tasks.get(position).setStatus(status);
                updateTaskStatuses();
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void updateTaskStatuses() {
        for (Task task : tasks) {
            if (task.getDeadline().before(new Date()) && task.getStatus().equals("Niewykonane")) {
                task.setStatus("Przeterminowane");
            }
        }
    }
}
