package com.example.myproject;

import static com.example.myproject.FBref.refAuth;
import static com.example.myproject.FBref.refFamily;
import static com.example.myproject.FBref.refUser;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private FirebaseUser fbUser;

    //AlertDialog.Builder adb;

    public static User user;
    public static Family family;
    //ArrayList<String> uIdsThis = new ArrayList<String>();//uid of all the menbers of the family
    ArrayList<String> taskTypes = new ArrayList<String>();
    //ArrayList<String> familiesFoundList;
    //ArrayList<Family> familiesValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        fbUser = refAuth.getCurrentUser();
        if (fbUser != null && (!settings.getBoolean("save", false)))
        {
            refAuth.signOut();
            editor = settings.edit();
            editor.putString("fId", null);
            editor.commit();
            fbUser = null;
        }

        if (fbUser == null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }

        uId = fbUser.getUid();
        fId = settings.getString("fId", null);
        if (fId == null){
            refUser.child(uId).child("fId").addListenerForSingleValueEvent(new ValueEventListener(){
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       fId = snapshot.getValue(String.class);
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {
                   }
               }
            );
            if (fId == null){ // User missing a family
                Intent intent = new Intent(this, ChooseFamily.class);
                startActivity(intent);
                finish();
            }
            editor = settings.edit();
            editor.putString("fId", fId);
            editor.commit();
        }

        btnLogOut = findViewById(R.id.btnLogOut);
        btnNewTask = findViewById(R.id.btnNewTask);
        tvBebi = findViewById(R.id.tvBebi);
        tvLebi = findViewById(R.id.tvLebi);
        lvBebi = findViewById(R.id.lvBebi);
        lvLebi = findViewById(R.id.lvLebi);

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