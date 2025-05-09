package com.example.myproject;

import static com.example.myproject.FBref.refAuth;

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
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Button btnRegisterBack, btnLogin;
    TextView tvTitle, tvPass, tvEmail, tvMsg;
    EditText etPass, etEmail;
    CheckBox cbSave;

    SharedPreferences settings;
    String uId;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnRegisterBack = findViewById(R.id.btnRegisterBack);
        btnLogin = findViewById(R.id.btnLogin);
        tvMsg = findViewById(R.id.tvMsg);
        tvTitle = findViewById(R.id.tvTitle);
        etEmail = findViewById(R.id.etEmailHead);
        etPass = findViewById(R.id.etPass);
        cbSave = findViewById(R.id.cbSave);
        tvPass = findViewById(R.id.tvPass);
        tvEmail = findViewById(R.id.tvEmail);
        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }
    public void loginUser(View view) {
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();
        if(email.isEmpty() || password.isEmpty()){
            tvMsg.setText("Please fill all fields");
        } else {
            ProgressDialog pd = new ProgressDialog(this);
            pd. setTitle("Connecting");
            pd.setMessage("Logging in user...");
            pd.show();
            refAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if (task.isSuccessful()) {
                                Log.i("user is in the firebase", "user is in the firebase");
                                Log.i("MainActivity", "signInWithEmailAndPassword:success");
                                FirebaseUser user = refAuth.getCurrentUser();
                                tvMsg.setText("User logged in successfully\nUid: " + user.getUid());
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Log.i("regfail", "regfail");
                                Log.i("MainActivity", "signInWithEmail:failure", task.getException());
                                Exception exp = task.getException();
                                if (exp instanceof FirebaseAuthInvalidUserException){
                                    tvMsg.setText("Invalid email address.");
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
    public void createUser(View view){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}