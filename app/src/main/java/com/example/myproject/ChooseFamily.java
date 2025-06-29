package com.example.myproject;

import static com.example.myproject.FBref.refAuth;
import static com.example.myproject.FBref.refFamily;
import static com.example.myproject.FBref.refUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseFamily extends AppCompatActivity {
    TextView tvTitle, tvFamilyName, tvFamilyAddress, tvJoin, tvCreate;
    EditText etEmailHead, etFamilyName, etFamilyAddress;
    Button btnJoin, btnCreate;

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    FirebaseUser fbUser;
    String uId;
    String email;
    String fId;
    String address;
    String name;

    public static User user;
    public static Family family;
    ArrayList<String> familiesFoundList;
    ArrayList<Family> familiesValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_family);
        btnJoin = findViewById(R.id.btnJoin);
        btnCreate = findViewById(R.id.btnCreate);
        tvTitle = findViewById(R.id.tvTitle);
        tvFamilyName = findViewById(R.id.tvFamilyName);
        tvFamilyAddress = findViewById(R.id.tvFamilyAddress);
        tvJoin = findViewById(R.id.tvJoin);
        tvCreate = findViewById(R.id.tvCreate);
        etEmailHead = findViewById(R.id.etEmailHead);
        etFamilyName = findViewById(R.id.etFamilyName);
        etFamilyAddress = findViewById(R.id.etFamilyAddress);

        fbUser = refAuth.getCurrentUser();
        uId = fbUser.getUid();
        email = fbUser.getEmail();
        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.i("Choose family", "choose family");

    }

    public void createFamily(View view){
        address = etFamilyAddress.getText().toString();
        name = etFamilyName.getText().toString();
        if (fbUser != null) {
            fId = refFamily.push().getKey();
            Family family = new Family(fId, address, uId, email, name);
            refFamily.child(fId).setValue(family);
            refUser.child(uId).child("fId").setValue(fId);
            refUser.child(uId).child("parent").setValue(true);
            // update user fid and parent = true
            Toast.makeText(this, "Family created successfully by" + email, Toast.LENGTH_SHORT).show();
            Log.i("FamilyId", fId);

            editor = settings.edit();
            editor.putString("fId", fId);
            editor.commit();

            Intent intent = new Intent(ChooseFamily.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void JoinFamily(View view) {
        String headEmail = etEmailHead.getText().toString();
        FirebaseUser fbUser = refAuth.getCurrentUser();
        Query query = refFamily.orderByChild("createdBy").equalTo(headEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<String> members;
                    for (DataSnapshot child : snapshot.getChildren()) {
                        fId = child.getKey();
                    }
                } else {
                    Toast.makeText(ChooseFamily.this, "No family found with that email.", Toast.LENGTH_SHORT).show();
                }
                if (fId != null){
                    DatabaseReference ref = refFamily.child(fId).child("uids");
                    ref.push().setValue(uId, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            if (error != null) {
                                Toast.makeText(ChooseFamily.this, "Data could not be saved: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                refUser.child(uId).child("fId").setValue(fId);

                                editor = settings.edit();
                                editor.putString("fId", fId);
                                editor.commit();

                                Intent intent = new Intent(ChooseFamily.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}