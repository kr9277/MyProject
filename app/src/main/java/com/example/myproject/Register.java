package com.example.myproject;

import static com.example.myproject.FBref.refAuth;
import static com.example.myproject.FBref.refUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class Register extends AppCompatActivity {
    Button btnLoginBack, btnRegister;
    TextView tvTitle1, tvPass, tvMsg, tvName, tvEmail;
    EditText etName, etPass, etEmail;
    CheckBox swSave;

    SharedPreferences settings;
    String uId;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnLoginBack = findViewById(R.id.btnLoginBack);
        btnRegister = findViewById(R.id.btnRegister);
        tvMsg = findViewById(R.id.tvMsg);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvTitle1 = findViewById(R.id.tvTitle1);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        swSave = findViewById(R.id.swSave);
        tvPass = findViewById(R.id.tvPass);
        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }
    public void createUser(View view){
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();
        if(email.isEmpty() || password.isEmpty()){
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
                                    User user = new User(etName.getText().toString(), uid);
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
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}