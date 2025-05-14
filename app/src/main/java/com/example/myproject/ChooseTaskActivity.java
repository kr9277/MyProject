package com.example.myproject;

import static com.example.myproject.FBref.refFamily;
import static com.example.myproject.FBref.refUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ChooseTaskActivity extends AppCompatActivity {
    CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10;
    TextView tvTitel3;
    EditText etAddTask;
    Button btnAddTask, btnSaveTasks;
    ArrayList<String> taskTypes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_task);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
        cb6 = findViewById(R.id.cb6);
        cb7 = findViewById(R.id.cb7);
        cb8 = findViewById(R.id.cb8);
        cb9 = findViewById(R.id.cb9);
        cb10 = findViewById(R.id.cb10);
        tvTitel3 = findViewById(R.id.tvTitel3);
        etAddTask = findViewById(R.id.etAddTask);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnSaveTasks = findViewById(R.id.btnSaveTasks);
    }
    public void addTask(View view){
        String taskType = etAddTask.getText().toString();
        taskTypes.add(taskType);
        //refFamily.child(taskTypes).child("fId").setValue(fId);
    }
    public void saveTasks(View view){
        if(cb1.isChecked()){
            taskTypes.add("לתלות כביסה");
        }
        if(cb2.isChecked()){
            taskTypes.add("לשים מכונת כביסה");
        }
        if(cb3.isChecked()){
            taskTypes.add("שואב אבק");
        }
        if(cb4.isChecked()){
            taskTypes.add("קניות בסופר");
        }
        if(cb5.isChecked()){
            taskTypes.add("סידור מצרכים");
        }
        if(cb6.isChecked()){
            taskTypes.add("לשים מדיח");
        }
        if(cb7.isChecked()){
            taskTypes.add("לפנות מדיח");
        }
        if(cb8.isChecked()){
            taskTypes.add("ספונג'ה");
        }
        if(cb9.isChecked()) {
            taskTypes.add("לזרוק זבל");
        }
        if(cb10.isChecked()){
            taskTypes.add("לפנות שולחן");
        }
        Intent intent = new Intent(ChooseTaskActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}