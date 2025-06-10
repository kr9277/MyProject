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
import android.widget.Toast;
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
    String uId;

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
        FirebaseUser fbUser = refAuth.getCurrentUser();
        uId = fbUser.getUid();
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
        boolean isParent = settings.getBoolean("isParent", false);
        Log.i("isParent", String.valueOf(isParent));
        if(!isParent){
            tvMsg1.setText("Only parent can open tasks");
        }
        else if(etDisc.getText()==null || etPoints.getText()==null || time==null){
            tvMsg1.setText("Please fill all fields");
        }
        else{
            String disc = etDisc.getText().toString();
            int points = Integer.parseInt(etPoints.getText().toString().trim());
            String fId = settings.getString("fId", null);
            String tId = refFamily.child(fId).child("currentFamilyTasks").push().getKey();
            Task task = new Task(tId, time, disc, points, fId, uId);
            refFamily.child(fId).child("currentFamilyTasks").child(tId).setValue(task);
            tvMsg1.setText("Task created successfully");
            Log.i("tId", tId);
            Toast.makeText(this, "tId = " + tId, Toast.LENGTH_SHORT).show();

            //------
            long endMillis = task.getEndTime().getTime();
            Log.i("CreateTask", "tId before intent: " + tId);
            Log.i("CreateTask", "fId before intent: " + fId);
            Intent intentApp = new Intent(getApplicationContext(), AlarmReciever.class);
            intentApp.putExtra("tId", tId);
            intentApp.putExtra("fId", fId);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), tId.hashCode(),
                    intentApp,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    endMillis,
                    pendingIntent
            );
            //Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);
            }
        }
    }