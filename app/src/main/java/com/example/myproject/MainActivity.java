package com.example.myproject;

import static com.example.myproject.FBref.refAuth;
import static com.example.myproject.FBref.refUser;
import static com.example.myproject.FBref.refFamily;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lv;

    SharedPreferences settings;
    String uId;
    String fId;
    boolean parent = false;
    int points = 0;

    private FirebaseUser fbUser;

    public static User user;
    public static Family family;
    ArrayList<String> uIdsThis = new ArrayList<String>();//uid of all the menbers of the family
    ArrayList<String> taskTypes = new ArrayList<String>();
    ArrayList<String> familiesFoundList;
    ArrayList<Family> familiesValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbUser = refAuth.getCurrentUser();
        setContentView(R.layout.activity_main);
        if (fbUser == null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }

        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        familiesFoundList = new ArrayList<String>();//both together
        familiesValue = new ArrayList<Family>();//ids of the families found?
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener(this);
        family = familiesValue.get(i);
        family.addMember(user.getuId());
        refFamily.child(family.getFId()).setValue(family);

    }


    //password check, javadoc
    //public void next(View view){
        //ויוזר ליצור שורש של המשפחה אם זה הורה
    //}
}