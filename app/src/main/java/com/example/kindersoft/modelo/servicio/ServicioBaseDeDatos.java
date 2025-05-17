package com.example.kindersoft.modelo.servicio;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ServicioBaseDeDatos {

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    private static ServicioBaseDeDatos instance;

    private ServicioBaseDeDatos() {}

    public static ServicioBaseDeDatos getInstance(){
        if (instance == null){
            instance = new ServicioBaseDeDatos();
        }
        return instance;
    }

    public void inicializarFirebase(Context a) {
        FirebaseApp.initializeApp(a);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

}
