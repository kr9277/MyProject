package com.example.myproject;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBref {
    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static FirebaseAuth refAuth = FirebaseAuth.getInstance();
    public static DatabaseReference refUser = FBDB.getReference("User");
    public static DatabaseReference refFamily = FBDB.getReference("Family");
}
