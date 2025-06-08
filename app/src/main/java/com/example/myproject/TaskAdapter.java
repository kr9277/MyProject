package com.example.myproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_task_adapter, parent, false);
        }
        Task task = tasks.get(position);
        TextView tvDesc = view.findViewById(R.id.tvDesc);
        TextView tvResponsible = view.findViewById(R.id.tvResponsible);
        TextView tvPoints = view.findViewById(R.id.tvPoints);
        TextView tvTime = view.findViewById(R.id.tvTime);
        tvDesc.setText(task.getDisc());
        tvResponsible.setText(task.getResponsible());
        tvPoints.setText(task.getPoints() + " נקודות ");
        tvTime.setText(task.getEndTime().toString());
        return view;
    }
}