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
import android.widget.Toast;

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
    TextView tvTitle, tvPass, tvName, tvEmail;
    EditText etName, etPass, etEmail;
    CheckBox cbSave, cbParent;

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String uId;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnLoginBack = findViewById(R.id.btnLoginBack);
        btnRegister = findViewById(R.id.btnRegister);
        tvName = findViewById(R.id.tvFamilyAddress);
        tvEmail = findViewById(R.id.tvEmail);
        tvTitle = findViewById(R.id.tvTitle);
        etName = findViewById(R.id.etFamilyName);
        etEmail = findViewById(R.id.etEmailHead);
        etPass = findViewById(R.id.etPass);
        cbSave = findViewById(R.id.cbSave);
        cbParent = findViewById(R.id.cbParent);
        tvPass = findViewById(R.id.tvPass);

        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);


    }
    public void createUser(View view){
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();
        editor = settings.edit();
        editor.putBoolean("save", cbSave.isChecked());
        editor.putBoolean("parent", cbParent.isChecked());
        editor.commit();
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
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
                                    uId = fbUser.getUid();
                                    user = new User(etName.getText().toString(), uId);
                                    refUser.child(uId).setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Register.this, "User created successfully in the database.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(Register.this, "Failed to save user in the database.", Toast.LENGTH_SHORT).show();
                                                        Log.e("Register", "Database error: ", task.getException());
                                                    }
                                                }
                                            });
                                }
                                //Intent intent = new Intent(Register.this, MainActivity.class);
                                Intent intent = new Intent(Register.this, ChooseFamily.class);
                                startActivity(intent);
                                finish(); // Close the Login activity

                            } else {
                                Log.i("regfail", "regfail");
                                Log.i("MainActivity", "createUserWithEmailAndPassword:failure", task.getException());
                                Exception exp = task.getException();
                                if (exp instanceof FirebaseAuthInvalidUserException){
                                    Toast.makeText(Register.this, "Invalid email address.", Toast.LENGTH_SHORT).show();
                                } else if (exp instanceof FirebaseAuthWeakPasswordException) {
                                    Toast.makeText(Register.this, "Password too weak.", Toast.LENGTH_SHORT).show();
                                } else if (exp instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(Register.this, "User already exists, please login or use another email.", Toast.LENGTH_SHORT).show();
                                } else if (exp instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(Register.this, "General authentication failure.", Toast.LENGTH_SHORT).show();
                                } else if (exp instanceof FirebaseNetworkException) {
                                    Toast.makeText(Register.this, "Network error. Please check your connection and try again.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Register.this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    }
    public void loginUser(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}