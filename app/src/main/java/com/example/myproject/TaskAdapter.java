package com.example.myproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    private Context context;
    private List<Task> tasks;

    public TaskAdapter(@NonNull Context context, @NonNull List<Task> tasks) {
        super(context, 0, tasks);
        this.context = context;
        this.tasks = tasks;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_task_adapter, parent, false);
        }
        Task task = tasks.get(position);
        TextView tvDesc = convertView.findViewById(R.id.tvDesc);
        TextView tvResponsible = convertView.findViewById(R.id.tvResponsible);
        TextView tvPoints = convertView.findViewById(R.id.tvPoints);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);
        tvDesc.setText(task.getDisc());
        tvResponsible.setText(task.getResponsible());
        tvPoints.setText(task.getPoints() + "נקודות");
        if(task.getIsCompleted()) {
            tvStatus.setText("הושלם");
        } else {
            tvStatus.setText("בביצוע");
        }
        return convertView;
    }
}