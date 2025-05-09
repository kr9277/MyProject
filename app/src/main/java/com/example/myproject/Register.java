package com.example.myproject;

import static com.example.myproject.FBref.refAuth;
import static com.example.myproject.FBref.refFamily;
import static com.example.myproject.FBref.refUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Register extends AppCompatActivity implements AdapterView.OnItemClickListener {
    Button btnLogin, btnRegister;
    TextView tvT1, tvP, tvC, tvPass, tvMsg, tvS, tvDS, tvCFM;
    EditText etN, etAd, etP, etE;
    CheckBox swSave;
    ListView lv;

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
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvMsg = findViewById(R.id.tvMsg);
        tvT1 = findViewById(R.id.tvTitle1);
        etN = findViewById(R.id.etN);
        etE = findViewById(R.id.etE);
        etP = findViewById(R.id.etP);
        swSave = findViewById(R.id.swSave);
        tvPass = findViewById(R.id.tvPass);
        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        familiesFoundList = new ArrayList<String>();//both together
        familiesValue = new ArrayList<Family>();//ids of the families found?
    }
    public void createUser(View view){
        String email = etE.getText().toString();
        String password = etP.getText().toString();
        if(email.isEmpty()|| password.isEmpty()){
            tvMsg.setText("Please fill all fields");
        } else {
            ProgressDialog pd = new ProgressDialog(this);
            pd. setTitle("Connecting");
            pd.setMessage("Creating user...");
            pd.show();
            refAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if (task.isSuccessful()) {
                                Log.i("Register", "createUserWithEmailAndPassword:success");
                                FirebaseUser fbUser = refAuth.getCurrentUser();
                                if (fbUser != null) {
                                    String uid = fbUser.getUid();
                                    User user = new User(etN.getText().toString(), uid);

                                    refUser.child(uid).setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        tvMsg.setText("User created successfully in the database.");
                                                    } else {
                                                        tvMsg.setText("Failed to save user in the database.");
                                                        Log.e("Register", "Database error: ", task.getException());
                                                    }
                                                }
                                            });
                                }

                                // Navigate back to MainActivity
                                Intent intent = new Intent(Register.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Close the Login activity

                            } else {
                                Log.i("regfail", "regfail");
                                Log.i("MainActivity", "createUserWithEmailAndPassword:failure", task.getException());
                                Exception exp = task.getException();
                                if (exp instanceof FirebaseAuthInvalidUserException){
                                    tvMsg.setText("Invalid email address.");
                                } else if (exp instanceof FirebaseAuthWeakPasswordException) {
                                    tvMsg.setText("Password too weak.");
                                } else if (exp instanceof FirebaseAuthUserCollisionException) {
                                    tvMsg.setText("User already exists.");
                                } else if (exp instanceof FirebaseAuthInvalidCredentialsException) {
                                    tvMsg.setText("General authentication failure.");
                                } else if (exp instanceof FirebaseNetworkException) {
                                    tvMsg.setText("Network error. Please check your connection and try again.");
                                } else {
                                    tvMsg.setText("An error occurred. Please try again later.");
                                }
                            }
                        }
                    });
        }
    }

    public void loginUser(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener(this);
        family = familiesValue.get(i);
        family.addMember(user.getuId());
        refFamily.child(family.getFId()).setValue(family);

    }
}