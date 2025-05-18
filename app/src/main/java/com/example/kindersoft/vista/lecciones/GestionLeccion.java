package com.example.kindersoft.vista.lecciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kindersoft.R;
import com.example.kindersoft.modelo.database.Conexion;
import com.example.kindersoft.modelo.entidades.Leccion;
import com.example.kindersoft.vista.menu.VistaMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GestionLeccion extends AppCompatActivity {

    String idLocal;
    private List<Leccion> listaLecciones;
    private Leccion leccionSeleccionada;
    private ListView listViewLecciones;
    ArrayAdapter<Leccion> arrayAdapterLecciones;
    private EditText txtNombreL, txtDescripcionL, txtPuntosL;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_leccion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarElementos();
        Conexion.inicializarFirebase(this);
        database = Conexion.getDatabase();
        databaseReference = Conexion.getDatabaseReference();

        listarLecciones();

        listViewLecciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leccionSeleccionada = (Leccion) parent.getItemAtPosition(position);
                idLocal = leccionSeleccionada.getId();
                txtNombreL.setText(leccionSeleccionada.getNombre());
                txtDescripcionL.setText(leccionSeleccionada.getDescripcion());
                txtPuntosL.setText(String.valueOf(leccionSeleccionada.getPuntos()));

            }
        });

    }

    private void listarLecciones() {
        databaseReference.child("lecciones").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaLecciones = new ArrayList<Leccion>();
                listaLecciones.clear();
                for(DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Leccion l = objSnaptshot.getValue(Leccion.class);
                    listaLecciones.add(l);

                    arrayAdapterLecciones = new ArrayAdapter<Leccion>(GestionLeccion.this, android.R.layout.simple_list_item_1, listaLecciones);

                    listViewLecciones.setAdapter(arrayAdapterLecciones);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarElementos() {
        txtNombreL = findViewById(R.id.txtNombre);
        txtDescripcionL = findViewById(R.id.txtDescripcion);
        txtPuntosL = findViewById(R.id.txtPuntuacion);
        listViewLecciones = findViewById(R.id.listViewGestion);
    }

    private void limpiarCampos(){
        txtNombreL.setText("");
        txtDescripcionL.setText("");
        txtPuntosL.setText("");
    }

    private boolean validarCampos(){

        if (txtNombreL.getText().toString().isEmpty()){
            txtNombreL.setError("Requerido");
            return false;
        }
        if (txtDescripcionL.getText().toString().isEmpty()){
            txtDescripcionL.setError("Requerido");
            return false;
        }
        if (txtPuntosL.getText().toString().isEmpty()){
            txtPuntosL.setError("Requerido");
            return false;
        }
        return true;

    }

    public void registrarLeccion(View v){
        if (!validarCampos()) return;

        String nuevoNombreLeccion = txtNombreL.getText().toString().trim();

        databaseReference.child("lecciones").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean existe = false;
                for (DataSnapshot usuarioSnapshot : snapshot.getChildren()) {
                    Leccion leccionExistente = usuarioSnapshot.getValue(Leccion.class);
                    if (leccionExistente != null && leccionExistente.getNombre().trim().equalsIgnoreCase(nuevoNombreLeccion)) {
                        existe = true;
                        break;
                    }
                }

                if (existe) {
                    Toast.makeText(GestionLeccion.this, "Ese nombre de leccion ya existe. Elija otro.", Toast.LENGTH_SHORT).show();
                    txtNombreL.setError("Nombre de leccion ya registrado");
                } else {
                    // Crear nueva leccion si el nombre no está en uso
                    Leccion leccion = new Leccion();
                    leccion.setId(databaseReference.push().getKey());
                    leccion.setNombre(nuevoNombreLeccion);
                    leccion.setDescripcion(txtDescripcionL.getText().toString().trim());
                    leccion.setPuntos(Integer.parseInt(txtPuntosL.getText().toString().trim()));
                    databaseReference.child("lecciones").child(leccion.getId()).setValue(leccion);
                    Toast.makeText(GestionLeccion.this, "leccion registrada con éxito", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GestionLeccion.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void actualizarLeccion(View v) {
        String id = idLocal; // Obtener el ID almacenado
        if (id == null) {
            Toast.makeText(this, "Primero seleeccione una leccion para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        if (validarCampos()) {
            Leccion leccion = new Leccion();
            leccion.setId(id);
            leccion.setNombre(txtNombreL.getText().toString());
            leccion.setDescripcion(txtDescripcionL.getText().toString());
            leccion.setPuntos(Integer.parseInt(txtPuntosL.getText().toString()));

            databaseReference.child("lecciones").child(id).setValue(leccion);
            Toast.makeText(this, "Leccion actualizada", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        }
    }

    public void eliminarLeccion(View v) {
        String id = idLocal;
        if (id == null) {
            Toast.makeText(this, "Primero selecciones una leccion para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child("lecciones").child(id).removeValue();
        Toast.makeText(this, "leccion eliminada", Toast.LENGTH_SHORT).show();
        limpiarCampos();
    }

    public void regresarMenu(View v){
        Intent menuP = new Intent(this, VistaMenu.class);
        startActivity(menuP);
    }

}