package com.example.kindersoft.vista.autenticacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.example.kindersoft.modelo.database.Conexion;
import com.example.kindersoft.vista.menu.VistaMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GestionRegistro extends AppCompatActivity {

    private ArrayList<String> roles;
    private List<Usuario> listaUsuarios;
    ArrayAdapter<Usuario> arrayAdapterUsuarios; //para la listaView
    private EditText txtUsuarioG, txtClaveG, txtNombresG, txtEdadG, txtPuntosTG;
    private Spinner spinner_rol_G;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarElementos();
        Conexion.inicializarFirebase(this);
        database = Conexion.getDatabase();
        databaseReference = Conexion.getDatabaseReference();

        roles = new ArrayList<>();
        roles.add("Seleccione un rol: ");
        roles.add("estudiante");

        ArrayAdapter<String> rol = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        spinner_rol_G.setAdapter(rol);

        listarUsuarios();
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
                    System.out.println(listaUsuarios);
                    //arrayAdapterUsuarios = new ArrayAdapter<Usuario>(GestionRegistro.this, android.R.layout.simple_list_item_1, listaUsuarios);
                    // Se puede Guardar en una Lista de tipo Usuario visible en el telefono
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarElementos() {

        txtUsuarioG = findViewById(R.id.txtUsuarioG);
        txtClaveG = findViewById(R.id.txtClaveG);
        txtNombresG = findViewById(R.id.txtNombresG);
        txtEdadG = findViewById(R.id.txtEdadG);
        spinner_rol_G = findViewById(R.id.spinner_rol_G);
        txtPuntosTG = findViewById(R.id.txtPuntosT_G);

    }

    private void limpiarCampos(){
        txtUsuarioG.setText("");
        txtClaveG.setText("");
        txtNombresG.setText("");
        txtEdadG.setText("");
        spinner_rol_G.setSelection(0);
        txtPuntosTG.setText("");
    }

    private boolean validarCampos(){
        if (txtUsuarioG.getText().toString().isEmpty()){
            txtUsuarioG.setError("Requerido");
            return false;
        }
        if (txtClaveG.getText().toString().isEmpty()){
            txtClaveG.setError("Requerido");
            return false;
        }
        if (txtNombresG.getText().toString().isEmpty()){
            txtNombresG.setError("Requerido");
            return false;
        }
        if (txtEdadG.getText().toString().isEmpty()){
            txtEdadG.setError("Requerido");
            return false;
        }
        if (spinner_rol_G.getSelectedItem().toString().equals("Seleccione un rol: ")){
            Toast.makeText(this, "Debe seleccionar un rol", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtPuntosTG.getText().toString().isEmpty()){
            txtPuntosTG.setError("Requerido");
            return false;
        }
        return true;

    }

    private void setSpinnerValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public void buscarUsuario(View v) {
        String username = txtUsuarioG.getText().toString();
        if (username.isEmpty()) {
            txtUsuarioG.setError("Ingrese un nombre de usuario");
            return;
        }

        for (Usuario u : listaUsuarios) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                txtClaveG.setText(u.getPassword());
                txtNombresG.setText(u.getNombres());
                txtEdadG.setText(String.valueOf(u.getEdad()));
                txtPuntosTG.setText(String.valueOf(u.getPuntos_totales()));
                setSpinnerValue(spinner_rol_G, u.getRol());

                // Guarda el ID temporalmente
                txtUsuarioG.setTag(u.getId());
                Toast.makeText(this, "Usuario encontrado", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
    }

    public void actualizarUsuario(View v) {
        String id = (String) txtUsuarioG.getTag(); // Obtener el ID almacenado
        if (id == null) {
            Toast.makeText(this, "Primero busque un usuario para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        if (validarCampos()) {
            Usuario usuario = new Usuario();
            usuario.setId(id);
            usuario.setUsername(txtUsuarioG.getText().toString());
            usuario.setPassword(txtClaveG.getText().toString());
            usuario.setNombres(txtNombresG.getText().toString());
            usuario.setEdad(Integer.parseInt(txtEdadG.getText().toString()));
            usuario.setRol(spinner_rol_G.getSelectedItem().toString());
            usuario.setPuntos_totales(Integer.parseInt(txtPuntosTG.getText().toString()));

            databaseReference.child("usuarios").child(id).setValue(usuario);
            Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        }
    }

    public void eliminarUsuario(View v) {
        String id = (String) txtUsuarioG.getTag();
        if (id == null) {
            Toast.makeText(this, "Primero busque un usuario para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child("usuarios").child(id).removeValue();
        Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
        limpiarCampos();
    }

    public void regresarMenu(View v){
        Intent menuP = new Intent(this, VistaMenu.class);
        startActivity(menuP);
    }

}