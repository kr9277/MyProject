package com.example.myproject;

import static com.example.myproject.FBref.refAuth;
import static com.example.myproject.FBref.refUser;
import static com.example.myproject.FBref.refFamily;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    Button btn1;
    TextView tvT1, tvP, tvC, tvPass, tvMsg, tvS, tvDS, tvCFM;
    EditText etN, etAd, etP, etE;
    Switch sw1, sw2;
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
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.btn1);
        tvMsg = findViewById(R.id.tvMsg);
        tvC = findViewById(R.id.tvC);
        tvP = findViewById(R.id.tvP);
        tvS = findViewById(R.id.tvS);
        tvDS = findViewById(R.id.tvDS);
        tvT1 = findViewById(R.id.tvT1);
        etN = findViewById(R.id.etN);
        etAd = findViewById(R.id.etAd);
        etE = findViewById(R.id.etE);
        etP = findViewById(R.id.etP);
        sw1 = findViewById(R.id.sw1);
        sw2 = findViewById(R.id.sw2);
        tvPass = findViewById(R.id.tvPass);
        lv = findViewById(R.id.lv);
        tvCFM = findViewById(R.id.tvC);
        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        familiesFoundList = new ArrayList<String>();//both together
        familiesValue = new ArrayList<Family>();//ids of the families found?
    }
    public void next(View view){
        Log.i("nextStart", "nextStart");
        if(sw2.isChecked()){ //shared preference

        }
        String name = etN.getText().toString();
        String email = etE.getText().toString();
        String address = etAd.getText().toString();
        String password = etP.getText().toString();
        if(name.isEmpty()|| password.isEmpty()|| address.isEmpty()){
            tvMsg.setText("Please fill all fields");
        }
        else{
            Log.i("got the fields", "got the fields");
            ProgressDialog pd = new ProgressDialog(this);//show message to the user
            pd.setTitle("Connecting");
            pd.setMessage("Creating user...");
            pd.show();
            refAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if (task.isSuccessful()) {
                                Log.i("user is in the firebase", "user is in the firebase");
                                Log.i("MainActivity", "createUserWithEmailAndPassword:success");
                                uId = refAuth.getCurrentUser().getUid();
                                if(!sw1.isChecked()) {
                                    parent = true;
                                }
                                user = new User(name, password, parent, points, uId);
                                refUser.child(uId).setValue(user);//user is in the users tree in the firebase
                                tvMsg.setText("User created successfully\nUid: "+uId);
                                if(parent){
                                    Family family = new Family(address, uId, password, name);
                                    fId = refFamily.push().getKey();
                                    refFamily.child(fId).setValue(family);
                                    tvMsg.setText("Family created successfully\nFid: "+fId);
                                }
                                else {
                                    Query query = refFamily.orderByChild("pass").equalTo(password); //
                                    query.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<DataSnapshot> task) {
                                            if(task.isSuccessful()){
                                                Log.i("taskSuccess","taskSuccess");
                                                DataSnapshot dS = task.getResult();
                                                //read in a loop all the families found
                                                familiesFoundList.clear();
                                                familiesValue.clear();
                                                String stName = "";
                                                String stAddress = "";
                                                for(DataSnapshot data : dS.getChildren()) {
                                                    Family family = data.getValue(Family.class);
                                                    familiesValue.add(family);
                                                    stName = family.getName();
                                                    stAddress = family.getAddress();
                                                    familiesFoundList.add(stName + ", " + stAddress);
                                                }
                                                ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, familiesFoundList);
                                                lv.setAdapter(adp);
                                            } else {
                                                Log.i("regfail", "regfail");
                                                Log.e("firebase", "Error getting data", task.getException());
                                            }
                                        }
                                    });

                                }
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
        Log.i("regend", "regend");

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