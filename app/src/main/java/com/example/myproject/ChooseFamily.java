package com.example.myproject;

import static com.example.myproject.FBref.refAuth;
import static com.example.myproject.FBref.refFamily;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class ChooseFamily extends AppCompatActivity {
    TextView tvMsg, tvTitle, tvFamilyName, tvFamilyAddress, tvJoin, tvCreate;
    EditText etEmailHead, etFamilyName, etFamilyAddress;
    Button btnJoin, btnCreate;

    SharedPreferences settings;
    String uId;
    String fId;
    String address;
    String name;

    public static User user;
    public static Family family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnJoin = findViewById(R.id.btnJoin);
        btnCreate = findViewById(R.id.btnCreate);
        tvMsg = findViewById(R.id.tvMsg);
        tvTitle = findViewById(R.id.tvTitle);
        tvFamilyName = findViewById(R.id.tvFamilyName);
        tvFamilyAddress = findViewById(R.id.tvFamilyAddress);
        tvJoin = findViewById(R.id.tvJoin);
        tvCreate = findViewById(R.id.tvCreate);
        etEmailHead = findViewById(R.id.etEmailHead);
        etFamilyName = findViewById(R.id.etFamilyName);
        etFamilyAddress = findViewById(R.id.etFamilyAddress);
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