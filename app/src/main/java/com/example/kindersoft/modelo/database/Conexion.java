package com.example.kindersoft.modelo.database;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Conexion {

    private static FirebaseDatabase database;
    private static DatabaseReference databaseReference;

    // Llamar esto solo una vez (por ejemplo, en la primera actividad que se lanza)
    public static void inicializarFirebase(Context context) {
        if (database == null) {
            FirebaseApp.initializeApp(context);
            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference();
        }
    }

    public static FirebaseDatabase getDatabase() {
        return database;
    }

    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

}
