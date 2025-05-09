package com.example.myproject;

import static com.example.myproject.FBref.refAuth;
import static com.example.myproject.FBref.refFamily;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lvBebi, lvLebi;
    Button btnLogOut, btnNewTask;
    TextView tvBebi, tvLebi;

    //I need it?
    SharedPreferences settings;
    String uId;
    String fId;
    boolean parent = false;
    int points = 0;

    private FirebaseUser fbUser;

    public static User user;
    public static Family family;
    //ArrayList<String> uIdsThis = new ArrayList<String>();//uid of all the menbers of the family
    //ArrayList<String> taskTypes = new ArrayList<String>();
    //ArrayList<String> familiesFoundList;
    //ArrayList<Family> familiesValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fbUser = refAuth.getCurrentUser();
        btnLogOut = findViewById(R.id.btnLogOut);
        btnNewTask = findViewById(R.id.btnNewTask);
        tvBebi = findViewById(R.id.tvBebi);
        tvLebi = findViewById(R.id.tvLebi);
        lvBebi = findViewById(R.id.lvBebi);
        lvLebi = findViewById(R.id.lvLebi);

        if (fbUser == null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }

        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //familiesFoundList = new ArrayList<String>();//both together
        //familiesValue = new ArrayList<Family>();//ids of the families found?
    }

    //@Override
    /*public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener(this);
        family = familiesValue.get(i);
        family.addMember(user.getuId());
        refFamily.child(family.getFId()).setValue(family);
    }*/

    public void logOut(View view) {
        refAuth.signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

}