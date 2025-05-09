package com.example.myproject;

import static com.example.myproject.FBref.refAuth;
import static com.example.myproject.FBref.refFamily;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChooseFamily extends AppCompatActivity {


    SharedPreferences settings;
    String uId;
    String fId;
    boolean parent = false;
    int points = 0;
    public static User user;
    public static Family family;
    ArrayList<String> uIdsThis = new ArrayList<String>();//uid of all the menbers of the family
    ArrayList<String> taskTypes = new ArrayList<String>();
    ArrayList<String> familiesFoundList;
    ArrayList<Family> familiesValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnLogin = findViewById(R.id.btnLoginBack);
        btnRegister = findViewById(R.id.btnRegister);
        tvMsg = findViewById(R.id.tvMsg);
        tvT1 = findViewById(R.id.tvTitle);
        etN = findViewById(R.id.etName);
        etE = findViewById(R.id.etEmail);
        etP = findViewById(R.id.etPass);
        swSave = findViewById(R.id.cbSave);
        tvPass = findViewById(R.id.tvPass);

        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        familiesFoundList = new ArrayList<String>();//both together
        familiesValue = new ArrayList<Family>();//ids of the families found?


    }
    public void createFamily(View view){
        FirebaseUser fbUser = refAuth.getCurrentUser();
        if (fbUser != null) {
            String uid = fbUser.getUid();
            fId = refFamily.push().getKey();
            Family family = new Family(fId, address, uId, fbUser.getEmail(), name);
            refFamily.child(fId).setValue(family);
            // update user fid and parent = true
            tvMsg.setText("Family created successfully\nFid: "+fId);
        }
    }

}