package com.example.myproject;

import static com.example.myproject.FBref.refAuth;
import static com.example.myproject.FBref.refFamily;
import static com.example.myproject.FBref.refUser;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lvTasks;
    Button btnLogOut, btnNewTask, btnPoints;
    Switch swWhichList;
    TextView tvBebi, tvLebi, tvTitle5, tvHiName;

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String uId;
    String fId;
    SQLiteDatabase db;

    private FirebaseUser fbUser;
    DatabaseReference tasksRef;

    AlertDialog.Builder adb;

    public static User user;
    public static Family family;
    //ArrayList<String> uIdsThis = new ArrayList<String>();//uid of all the members of the family
    ArrayList<String> taskTypes;
    ArrayList<Task> toDoTasks;
    ArrayList<Task> inProgressTasks;
    TaskAdapter toDoAdapter;
    TaskAdapter inProgressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "task_channel", // ID
                    "משימות חדשות", // שם הערוץ
                    NotificationManager.IMPORTANCE_HIGH // רמת חשיבות
            );
            channel.setDescription("התראות על פתיחת משימות חדשות");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        fId = settings.getString("fId", null);

        btnLogOut = findViewById(R.id.btnLogOut);
        btnNewTask = findViewById(R.id.btnNewTask);
        btnPoints = findViewById(R.id.btnPoints);
        tvBebi = findViewById(R.id.tvBebi);
        tvLebi = findViewById(R.id.tvLebi);
        lvTasks = findViewById(R.id.lvTasks);
        tvTitle5 = findViewById(R.id.tvTitle5);
        tvHiName = findViewById(R.id.tvHiName);
        swWhichList  = findViewById(R.id.swWhichList);
        taskTypes = new ArrayList<String>();
        toDoTasks = new ArrayList<>();
        inProgressTasks = new ArrayList<>();
        toDoAdapter = new TaskAdapter(this, toDoTasks);
        inProgressAdapter = new TaskAdapter(this, inProgressTasks);

        lvTasks.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvTasks.setOnItemClickListener(this);

        FirebaseUser fbUser = refAuth.getCurrentUser();
        uId = fbUser.getUid();
        refUser.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null && user.getName() != null) {
                    tvHiName.setText("שלום " + user.getName() + "!");
                } else {
                    tvHiName.setText("שלום משתמש!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvHiName.setText("שלום משתמש (שגיאה בטעינה)");
            }
        });
        tasksRef = refFamily.child(fId).child("currentFamilyTasks");
        if(tasksRef!=null){
            tasksRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    toDoTasks.clear();
                    inProgressTasks.clear();
                    for (DataSnapshot taskSnap : snapshot.getChildren()) {
                        Task task = taskSnap.getValue(Task.class);
                        if (task == null)
                            continue;
                        Log.d("TaskDebug", "Task loaded: " + task.getDisc()+ ", taken: " + task.isTaken());
                        if (!task.isUserNotified(uId)) {
                            showNotification(task); // צריך לממש את הפונקציה הזאת
                            task.setNotifiedForUser(uId, true); // פונקציה שתעדכן את המאפ ב-Task

                            refFamily.child(fId).child("currentFamilyTasks").child(taskSnap.getKey()).setValue(task);
                        }
                        if (task.isTaken()) {
                            inProgressTasks.add(task);
                        } else {
                            toDoTasks.add(task);
                        }
                        scheduleTaskDeadlineAlarm(task);
                    }
                    toDoAdapter.notifyDataSetChanged();
                    inProgressAdapter.notifyDataSetChanged();
                    updateTaskListView();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        swWhichList.setOnCheckedChangeListener((buttonView, isChecked) -> updateTaskListView());
        updateTaskListView();
        Log.i("TasksFirebase", "number of tasks to do: " + inProgressTasks.size() + ", to do: " + toDoTasks.size());
    }

    private void scheduleTaskDeadlineAlarm(Task task) {
        long currentTime = System.currentTimeMillis();
        long endTime = task.getEndTime().getTime();

        if (endTime <= currentTime) return;

        Intent intent = new Intent(this, AlarmReciever.class);
        intent.putExtra("tId", task.getTId());
        intent.putExtra("fId", fId); // אפשר גם לא לשלוח אם שמור ב-SharedPreferences
        intent.putExtra("msg", "המשימה \"" + task.getDisc() + "\" לא הושלמה בזמן!");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                task.getTId().hashCode(), // מזהה ייחודי
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTime, pendingIntent);
    }

    //@Override
    /*public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener(this);
        family = familiesValue.get(i);
        family.addMember(user.getuId());
        refFamily.child(family.getFId()).setValue(family);
    }*/

    private void showNotification(Task task) {
        Notification.Builder builder;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, "task_channel");
        } else {
            builder = new Notification.Builder(this);
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("נפתחה משימה חדשה!")
                .setContentText(task.getDisc())
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        Task chosenTask = (Task) lvTasks.getItemAtPosition(i);
        if(!chosenTask.getIsTaken()){
            adb = new AlertDialog.Builder(this);
            adb.setTitle("רוצה לתפוס את המטלה?");

            adb.setPositiveButton("כן", (dialog, which) -> {
                toDoTasks.remove(chosenTask);
                inProgressTasks.add(chosenTask);
                toDoAdapter.notifyDataSetChanged();
                inProgressAdapter.notifyDataSetChanged();
                updateTaskListView();
                chosenTask.setTaken(true);
                chosenTask.setResponsible(MainActivity.user.getName());
                refFamily.child(fId).child("currentFamilyTasks")
                        .child(chosenTask.getTId()).setValue(chosenTask);
            });

            adb.setNegativeButton("ביטול", (dialog, which) -> {
                dialog.dismiss();
            });

            AlertDialog ad = adb.create();
            ad.show();
        }
        else{
            adb = new AlertDialog.Builder(this);
            adb.setTitle("רוצה לסגור את המטלה?");
            adb.setPositiveButton("כן", (dialog, which) -> {
                int currentPoints = MainActivity.user.getPoints(); // ודא שיש getPoints()
                int taskPoints = chosenTask.getPoints();
                MainActivity.user.setPoints(currentPoints + taskPoints); //static variable
                refUser.child(MainActivity.user.getuId()).setValue(MainActivity.user);
                refFamily.child(fId).child("currentFamilyTasks")
                        .child(chosenTask.getTId()).removeValue();
                inProgressTasks.remove(chosenTask);
                inProgressAdapter.notifyDataSetChanged();
                updateTaskListView();
            });
            adb.setNegativeButton("ביטול", (dialog, which) -> {
                dialog.dismiss();
            });

            AlertDialog ad = adb.create();
            ad.show();
        }
    }

    private void updateTaskListView() {
        if (swWhichList.isChecked()) {
            lvTasks.setAdapter(inProgressAdapter);
        } else {
            lvTasks.setAdapter(toDoAdapter);
        }
    }

    public void logOut(View view) {
        refAuth.signOut();
        editor = settings.edit();
        editor.putString("fId", null);
        editor.putBoolean("save", false);
        editor.commit();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void openNewTask(View view){
        Intent intent = new Intent(this, OpenTaskActivity.class);
        startActivity(intent);
        finish();
    }

    public void openPoints(View view){
        Intent intent = new Intent(this, PointsActivity.class);
        startActivity(intent);
    }


}