package com.example.kindersoft.vista.autenticacion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kindersoft.R;
import com.example.kindersoft.modelo.entidades.Usuario;
import com.example.kindersoft.modelo.database.Conexion;
import com.example.kindersoft.vista.menu.VistaMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private EditText txtUser;
    private EditText txtContrasena;
    private CheckBox recordar;
    private List<Usuario> listaUsuarios;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Conexion.inicializarFirebase(this);
        database = Conexion.getDatabase();
        databaseReference = Conexion.getDatabaseReference();

        inicializarElementos();
        listarUsuarios();
    }
    private void inicializarElementos() {
        txtUser = findViewById(R.id.txtUsuarioL);
        txtContrasena = findViewById(R.id.txtClaveL);
        recordar = findViewById(R.id.cbRecordarL);
    }
    public void ingresar(View v){
        buscarUsuarioPorNombre(txtUser.getText().toString().trim(), txtContrasena.getText().toString().trim());
    }
    public void registrar(View v){
        Intent registroU = new Intent(this, VistaRegistro.class);
        startActivity(registroU);
    }
    private void listarUsuarios() {
        databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaUsuarios = new ArrayList<Usuario>();
                listaUsuarios.clear();
                for(DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Usuario u = objSnaptshot.getValue(Usuario.class);
                    listaUsuarios.add(u);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void buscarUsuarioPorNombre(String usernameInput, String passwordInput) {
        Query query = databaseReference.child("usuarios").orderByChild("username").equalTo(usernameInput);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Usuario usuario = userSnapshot.getValue(Usuario.class);
                        if (usuario != null && usuario.getPassword().equals(passwordInput)) {
                            // Usuario autenticado exitosamente
                            Toast.makeText(Login.this, "Bienvenido, " + usuario.getUsername(), Toast.LENGTH_SHORT).show();

                            Log.i("Usuario Login", usuario.toString());
                            guardarUsuarioEnPreferencias(usuario);

                            Intent intent = new Intent(Login.this, VistaMenu.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                    // Usuario encontrado pero la contraseña no coincide
                    Toast.makeText(Login.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                } else {
                    // No se encontró el usuario
                    Toast.makeText(Login.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Login.this, "Error al buscar el usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarUsuarioEnPreferencias(Usuario usuario) {
        SharedPreferences sharedPref = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("id", usuario.getId());
        editor.putString("username", usuario.getUsername());
        editor.putString("nombres", usuario.getNombres());
        editor.putInt("edad", usuario.getEdad() != null ? usuario.getEdad() : 0);
        editor.putString("rol", usuario.getRol());
        editor.putInt("puntos_totales", usuario.getPuntos_totales() != null ? usuario.getPuntos_totales() : 0);

        editor.apply();
    }


}