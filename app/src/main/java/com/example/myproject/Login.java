package com.example.myproject;

import static com.example.myproject.FBref.refAuth;
import static com.example.myproject.FBref.refUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
/**
 * Activity for handling user login.
 * Allows users to sign in with their email and password, or navigate to the registration screen.
 * It also handles remembering the user's session if they choose to be saved.
 */
public class Login extends AppCompatActivity {
    Button btnRegisterBack, btnLogin;
    TextView tvTitle, tvPass, tvEmail;
    EditText etPass, etEmail;
    CheckBox cbSave;

    SharedPreferences settings;
    String uId;
    public static User user;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnRegisterBack = findViewById(R.id.btnRegisterBack);
        btnLogin = findViewById(R.id.btnLogin);
        tvTitle = findViewById(R.id.tvTitle);
        etEmail = findViewById(R.id.etEmailHead);
        etPass = findViewById(R.id.etPass);
        cbSave = findViewById(R.id.cbSave);
        tvPass = findViewById(R.id.tvPass);
        tvEmail = findViewById(R.id.tvEmail);
        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean isChecked = settings.getBoolean("save", false);
        FirebaseUser fbUser = refAuth.getCurrentUser();
        if (fbUser!= null && isChecked){
            Log.i("User logged in successfully", "User logged in successfully");
            loadUserFamily(fbUser);
        }
        else{
            Log.i("user is not in the firebase", "user is not in the firebase");
        }
    }

    /**
     * Handles the login button click event.
     * Retrieves email and password from EditText fields, validates them,
     * and attempts to sign in the user with Firebase Authentication.
     * Displays appropriate messages based on login success or failure.
     * Saves the "save" preference based on the CheckBox state.
     *
     * @param view The view that was clicked (the login button).
     */
    public void loginUser(View view) {
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();
        editor = settings.edit();
        editor.putBoolean("save", cbSave.isChecked());
        editor.commit();
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
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
                                FirebaseUser fbUser = refAuth.getCurrentUser();
                                if (fbUser != null) {
                                    Toast.makeText(Login.this, "User logged in successfully, Uid: " + fbUser.getUid(), Toast.LENGTH_SHORT).show();
                                    loadUserFamily(fbUser);
                                }
                            }
                            else {
                                Log.i("regfail", "regfail");
                                Log.i("MainActivity", "signInWithEmail:failure", task.getException());
                                Exception exp = task.getException();
                                if (exp instanceof FirebaseAuthInvalidUserException){
                                    Toast.makeText(Login.this, "Invalid email address.", Toast.LENGTH_SHORT).show();
                                } else if (exp instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(Login.this, "General authentication failure.", Toast.LENGTH_SHORT).show();
                                } else if (exp instanceof FirebaseNetworkException) {
                                    Toast.makeText(Login.this, "Network error. Please check your connection and try again.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Login.this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    }
    /**
     * Handles the "Create User" or "Register" button click event.
     * Navigates the user to the {@link Register} activity.
     *
     * @param view The view that was clicked (the register button).
     */
    public void createUser(View view){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        finish();
    }

    /**
     * Loads the family ID (fId) for the given FirebaseUser.
     * First, it checks SharedPreferences for an existing fId.
     * If not found, it queries the Firebase Realtime Database for the user's fId.
     * If fId is found (either in SharedPreferences or Database), it navigates to {@link MainActivity}.
     * If fId is not found in either location, it navigates to {@link ChooseFamily} activity.
     *
     * @param fbUser The currently authenticated FirebaseUser.
     */
    private void loadUserFamily(FirebaseUser fbUser) {
        String uId = fbUser.getUid();
        String fId = settings.getString("fId", null);
        if (fId == null) {
            Log.i("User missing a family", "Trying to get fId from DB...");
            refUser.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean isParent = snapshot.child("parent").getValue(Boolean.class);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("isParent", isParent);
                    editor.apply();

                    Log.i("User is parent: " + isParent, "User is parent: " + isParent);
                    String fIdFromDb = snapshot.child("fId").getValue(String.class);
                    if (fIdFromDb == null || fIdFromDb.isEmpty()) {
                        Log.i("User missing a family in db", "Redirecting to ChooseFamily...");
                        Intent intent = new Intent(Login.this, ChooseFamily.class);
                        startActivity(intent);
                        finish();
                    } else {
                        editor = settings.edit();
                        editor.putString("fId", fIdFromDb);
                        editor.apply();

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase Error", error.getMessage());
                    Toast.makeText(Login.this, "שגיאה בגישה לנתוני המשתמש", Toast.LENGTH_SHORT).show();
                }
            });
        } else {//fId is already in the shared preferences
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}