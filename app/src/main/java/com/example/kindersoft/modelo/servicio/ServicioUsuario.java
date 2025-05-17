package com.example.kindersoft.modelo.servicio;



import android.widget.Toast;

import com.example.kindersoft.modelo.entidades.Usuario;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class ServicioUsuario {
    private static  ServicioUsuario instance;
    private List<Usuario> userList;
    FirebaseDatabase database;
    DatabaseReference databaseReference;


    private ServicioUsuario() {}

    public static ServicioUsuario getInstance(){
        if (instance == null){
            instance = new ServicioUsuario();
        }
        return instance;
    }






}
