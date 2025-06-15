package com.example.myproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PointsActivity extends AppCompatActivity {
    TextView tvUsersPoints, tvTitle6;
    Button btnMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        tvUsersPoints = findViewById(R.id.tvUsersPoints);
        tvTitle6 = findViewById(R.id.tvTitle6);
        btnMain = findViewById(R.id.btnMain);
        tvUsersPoints.setText(MainActivity.user.getName() + " צברת " + MainActivity.user.getPoints() + " נקודות ");

        String fId = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("fId", null);
        Log.d("PointsActivity", "fId = " + fId);

//        if (fId != null) {
//            FirebaseDatabase.getInstance().getReference("Family").child(fId).child("uIds")
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            StringBuilder pointsDisplay = new StringBuilder();
//
//                            for (DataSnapshot userSnap : snapshot.getChildren()) {
//                                String name = userSnap.child("name").getValue(String.class);
//                                Long points = userSnap.child("points").getValue(Long.class);
//
//                                if (name != null && points != null) {
//                                    pointsDisplay.append(name).append(" - ").append(points).append(" נקודות").append("\n");
//                                }
//                            }
//                            if (pointsDisplay.length() > 0) {
//                                tvUsersPoints.setText(pointsDisplay.toString());
//                            } else {
//                                tvUsersPoints.setText("לא נמצאו משתמשים במשפחה");
//                            }
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            tvUsersPoints.setText("שגיאה בטעינת הנתונים");
//                            Log.e("PointsActivity", "Database error: " + error.getMessage());
//                        }
//                    });
//        } else {
//            tvUsersPoints.setText("לא נמצא מזהה משפחה");
//        }
    }

    public void backMain(View view){
        finish();
    }

}