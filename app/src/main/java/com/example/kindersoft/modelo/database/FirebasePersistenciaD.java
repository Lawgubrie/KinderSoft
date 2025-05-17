package com.example.kindersoft.modelo.database;

import com.google.firebase.database.FirebaseDatabase;

public class FirebasePersistenciaD extends android.app.Application{

    @Override
    public void onCreate(){

        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

}
