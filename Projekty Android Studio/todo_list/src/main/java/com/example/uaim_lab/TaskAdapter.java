package com.example.uaim_lab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }

        TextView titleView = convertView.findViewById(R.id.taskTitle);
        TextView deadlineView = convertView.findViewById(R.id.taskDeadline);
        ImageView statusView = convertView.findViewById(R.id.taskStatus);

        titleView.setText(task.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        deadlineView.setText(sdf.format(task.getDeadline()));

        if ("Wykonane".equals(task.getStatus())) {
            statusView.setImageResource(R.drawable.green_circle);
        } else if ("Przeterminowane".equals(task.getStatus())) {
            statusView.setImageResource(R.drawable.red_cross);
        } else {
            statusView.setImageResource(R.drawable.gray_circle);
        }

        return convertView;
    }
}