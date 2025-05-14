package com.example.myproject;

import static com.example.myproject.FBref.refAuth;
import static com.example.myproject.FBref.refFamily;
import static com.example.myproject.FBref.refUser;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lvBebi, lvLebi;
    Button btnLogOut, btnNewTask;
    TextView tvBebi, tvLebi;

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String uId;
    String fId;
    SQLiteDatabase db;

    private FirebaseUser fbUser;
    DatabaseReference tasksRef;

    //AlertDialog.Builder adb;

    public static User user;
    public static Family family;
    //ArrayList<String> uIdsThis = new ArrayList<String>();//uid of all the members of the family
    ArrayList<String> taskTypes = new ArrayList<String>();
    ArrayList<Task> toDoTasks = new ArrayList<>();
    ArrayList<Task> inProgressTasks = new ArrayList<>();
    TaskAdapter toDoAdapter = new TaskAdapter(this, toDoTasks);
    TaskAdapter inProgressAdapter = new TaskAdapter(this, inProgressTasks);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        fId = settings.getString("fId", null);

        btnLogOut = findViewById(R.id.btnLogOut);
        btnNewTask = findViewById(R.id.btnNewTask);
        tvBebi = findViewById(R.id.tvBebi);
        tvLebi = findViewById(R.id.tvLebi);
        lvBebi = findViewById(R.id.lvBebi);
        lvLebi = findViewById(R.id.lvLebi);

        lvBebi.setAdapter(toDoAdapter);
        lvLebi.setAdapter(inProgressAdapter);
        tasksRef = refFamily.child(fId).child("currentFamilyTasks");
        tasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                toDoTasks.clear();
                inProgressTasks.clear();
                for (DataSnapshot taskSnap : snapshot.getChildren()) {
                    Task task = taskSnap.getValue(Task.class);
                    if (task == null) continue;
                    if (task.getIsCompleted()) {
                        inProgressTasks.add(task); // משימות שהושלמו
                    } else {
                        toDoTasks.add(task); // משימות לביצו ע
                    }
                }
                toDoAdapter.notifyDataSetChanged();
                inProgressAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //טיפול בשגיאה
            }
        });
    }

    //@Override
    /*public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener(this);
        family = familiesValue.get(i);
        family.addMember(user.getuId());
        refFamily.child(family.getFId()).setValue(family);
    }*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        lvLebi.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvLebi.setOnItemClickListener(this);
    }

    public void logOut(View view) {
        refAuth.signOut();
        editor = settings.edit();
        editor.putString("fId", null);
        editor.putBoolean("save", false);
        editor.commit();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void openNewTask(View view){
        //adb = new AlertDialog.Builder(this);
        //adb.setTitle("פתח מטלה:");

        //final EditText etDisc = new EditText(this);
        //etDisc.setHint("תיאור המטלה");
        //adb.setView(etDisc);

        //final EditText etPoints = new EditText(this);
        //etPoints.setHint("כמה נקודות המטלה שווה?");
        //adb.setView(etPoints);

       // final EditText etTimeEnd = new EditText(this);
        //etTimeEnd.setHint("תיאור המטלה");
        //adb.setView(etTimeEnd);
        //AlertDialog ad = adb.create();
        //ad.show();


        Intent intent = new Intent(this, OpenTaskActivity.class);
        startActivity(intent);
        finish();
    }


}