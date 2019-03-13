package com.example.myfirebase.Application;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
