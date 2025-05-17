package com.example.kindersoft.vista.autenticacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kindersoft.R;
import com.example.kindersoft.modelo.entidades.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VistaRegistro extends AppCompatActivity {

    private ArrayList<String> roles;
    private EditText txtUsuarioR, txtClaveR, txtNombres, txtEdad, txtPuntosT;
    private Spinner spinner_rol;
    private CheckBox cbTerminosC;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vista_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarFirebase();
        inicializarElementos();

        roles = new ArrayList<>();
        roles.add("Seleccione un rol: ");
        roles.add("estudiante");

        ArrayAdapter<String> rol = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        spinner_rol.setAdapter(rol);
    }

    private void inicializarElementos() {

        txtUsuarioR = findViewById(R.id.txtUsuarioG);
        txtClaveR = findViewById(R.id.txtClaveG);
        txtNombres = findViewById(R.id.txtNombresG);
        txtEdad = findViewById(R.id.txtEdadG);
        spinner_rol = findViewById(R.id.spinner_rol_G);
        cbTerminosC = findViewById(R.id.cbTerminosC);
        txtPuntosT = findViewById(R.id.txtPuntosT_G);

    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public void registrarUsuario(View v){
        if (!validarCampos()) return;

        String nuevoUsername = txtUsuarioR.getText().toString().trim();

        databaseReference.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean existe = false;
                for (DataSnapshot usuarioSnapshot : snapshot.getChildren()) {
                    Usuario usuarioExistente = usuarioSnapshot.getValue(Usuario.class);
                    if (usuarioExistente != null && usuarioExistente.getUsername().trim().equalsIgnoreCase(nuevoUsername)) {
                        existe = true;
                        break;
                    }
                }

                if (existe) {
                    Toast.makeText(VistaRegistro.this, "Ese nombre de usuario ya existe. Elija otro.", Toast.LENGTH_SHORT).show();
                    txtUsuarioR.setError("Usuario ya registrado");
                } else {
                    // Crear nuevo usuario si el username no está en uso
                    Usuario usuario = new Usuario();
                    usuario.setId(databaseReference.push().getKey());
                    usuario.setUsername(nuevoUsername);
                    usuario.setPassword(txtClaveR.getText().toString().trim());
                    usuario.setNombres(txtNombres.getText().toString().trim());
                    usuario.setEdad(Integer.parseInt(txtEdad.getText().toString()));
                    usuario.setRol(spinner_rol.getSelectedItem().toString().trim());
                    usuario.setPuntos_totales(Integer.parseInt(txtPuntosT.getText().toString().trim()));
                    databaseReference.child("usuarios").child(usuario.getId()).setValue(usuario);
                    Toast.makeText(VistaRegistro.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VistaRegistro.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos(){
        txtUsuarioR.setText("");
        txtClaveR.setText("");
        txtNombres.setText("");
        txtEdad.setText("");
        spinner_rol.setSelection(0);
        cbTerminosC.setChecked(false);
        txtPuntosT.setText("");
    }

    private boolean validarCampos(){
        if (txtUsuarioR.getText().toString().isEmpty()){
            txtUsuarioR.setError("Requerido");
            return false;
        }
        if (txtClaveR.getText().toString().isEmpty()){
            txtClaveR.setError("Requerido");
            return false;
        }
        if (txtNombres.getText().toString().isEmpty()){
            txtNombres.setError("Requerido");
            return false;
        }
        if (txtEdad.getText().toString().isEmpty()){
            txtEdad.setError("Requerido");
            return false;
        }
        if (spinner_rol.getSelectedItem().toString().equals("Seleccione un rol: ")){
            Toast.makeText(this, "Debe seleccionar un rol", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtPuntosT.getText().toString().isEmpty()){
            txtPuntosT.setError("Requerido");
            return false;
        }
        if (cbTerminosC.isChecked() == false){
            cbTerminosC.setError("Requerido");
            return false;
        }

        return true;

    }

    public void regresarLogin(View v){
        Intent login = new Intent(this, Login.class);
        startActivity(login);
    }


}