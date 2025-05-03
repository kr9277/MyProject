package com.example.myproject;

import static com.example.myproject.FBref.refAuth;
import static com.example.myproject.FBref.refFamily;

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
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements AdapterView.OnItemClickListener {
    Button btnRegister, btnLogin;
    TextView tvT1, tvPass, tvMsg;
    EditText etP, etE;
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
        setContentView(R.layout.activity_login);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        tvMsg = findViewById(R.id.tvMsg);
        tvT1 = findViewById(R.id.tvT1);
        etE = findViewById(R.id.etE);
        etP = findViewById(R.id.etP);
        swSave = findViewById(R.id.swSave);
        tvPass = findViewById(R.id.tvPass);
        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }
    public void loginUser(View view) {
        String email = etE.getText().toString();
        String password = etP.getText().toString();
        if(email.isEmpty()|| password.isEmpty()){
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

                                // Navigate back to MainActivity
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Close the Login activity


                            } else {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener(this);
        family = familiesValue.get(i);
        family.addMember(user.getuId());
        refFamily.child(family.getFId()).setValue(family);

    }

    public void createUser(View view){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

}