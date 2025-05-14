package com.example.myproject;

import static com.example.myproject.FBref.refAuth;
import static com.example.myproject.FBref.refFamily;
import static com.example.myproject.FBref.refUser;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class OpenTaskActivity extends AppCompatActivity {
    TextView tvTitleTime, tvTitle4, tvMsg1;
    EditText etDisc, etPoints;
    Button btnTime, btnOpenTask, btnClean, btnBack;

    Date time;
    SharedPreferences settings;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private int ALARM_RQST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_task);
        tvTitleTime = findViewById(R.id.tvTitleTime);
        tvTitle4 = findViewById(R.id.tvTitle4);
        tvMsg1 = findViewById(R.id.tvMsg1);
        etDisc = findViewById(R.id.etDisc);
        etPoints = findViewById(R.id.etPoints);
        btnTime = findViewById(R.id.btnTime);
        btnOpenTask = findViewById(R.id.btnOpenTask);
        btnClean = findViewById(R.id.btnClean);
        btnBack = findViewById(R.id.btnBack);
        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }
    public void clean(View view) {
        etDisc.setText("");
        etPoints.setText("");
        time = new Date();
    }
    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void setTime(View view) {
        openTimePickerDialog(true);
    }
    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), is24r);
        timePickerDialog.setTitle("Choose time");
        timePickerDialog.show();
    }
    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Log.i("onTimeSet", "onTimeSet");

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if (calSet.compareTo(calNow) <= 0) {
                calSet.add(Calendar.DATE, 1);
            }
            Log.i("onTimeSet", "Cal set " + calSet.getTime());
            time = calSet.getTime();
        }
    };

    public void createTask(View view) {
        if(etDisc.getText()==null || etPoints.getText()==null || time==null){
            tvMsg1.setText("Please fill all fields");
        }
        else{
            String disc = etDisc.getText().toString();
            int points = Integer.parseInt(etPoints.getText().toString().trim());
            String fId = settings.getString("fId", null);
            String tId = refFamily.child(fId).child("currentFamilyTasks").push().getKey();
            Task task = new Task(tId, time, disc, points, fId);
            refFamily.child(fId).child("currentFamilyTasks").child(tId).setValue(task);
            tvMsg1.setText("Task created successfully");
            Log.i("TaskId", tId);

            //ך מקפיצים התראה לשאר המשפחה? הקפצתי התראה רק למשתמש הזה שפתח את המטלה החדשה, אי
            Log.i("setAlarm", "setAlarm");
            ALARM_RQST_CODE++;
            Intent intent = new Intent(this, AlarmReciever.class);
            intent.putExtra("msg",String.valueOf(ALARM_RQST_CODE)+" TOD");
            alarmIntent = PendingIntent.getBroadcast(this,
                    ALARM_RQST_CODE, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
            alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmIntent);

//            String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uId).child("shouldNotify");
//
//            ref.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    Boolean shouldNotify = snapshot.getValue(Boolean.class);
//                    if (shouldNotify != null && shouldNotify) {
//                        showInstantNotification("יש לך הודעה חדשה!", "בדוק את האפליקציה");
//                        ref.setValue(false); // לאפס כדי שהתראה לא תופיע שוב
//                    }
//                }
//                @Override
//                public void onCancelled(DatabaseError error) { }
//            });

            //לא בטוח שהגעתי לפה
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            }
        }
    }