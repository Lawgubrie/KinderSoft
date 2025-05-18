package com.example.kindersoft.vista.trofeos;

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
import com.example.kindersoft.modelo.entidades.Trofeo;
import com.example.kindersoft.vista.menu.VistaMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GestionTrofeos extends AppCompatActivity {
    String idLocal;
    private Trofeo trofeoSeleccionado;
    private ListView listViewTrofeo;
    ArrayAdapter<Trofeo> arrayAdapterTrofeo;
    private List<Trofeo> listaTrofeo;
    private EditText txtDescripcionTrofeo, txtNombreTrofeo;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_trofeos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarElementos();
        Conexion.inicializarFirebase(this);
        database = Conexion.getDatabase();
        databaseReference = Conexion.getDatabaseReference();

        listarTrofeos();

        listViewTrofeo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                trofeoSeleccionado = (Trofeo) parent.getItemAtPosition(position);
                idLocal = trofeoSeleccionado.getId();
                txtDescripcionTrofeo.setText(trofeoSeleccionado.getNombre());
                txtNombreTrofeo.setText(trofeoSeleccionado.getDescripcion());
            }
        });
    }

    private void inicializarElementos() {
        txtDescripcionTrofeo = findViewById(R.id.txtDescipcionTrofeo);
        txtNombreTrofeo = findViewById(R.id.txtNombreTrof);
        listViewTrofeo = findViewById(R.id.listViewGestion);

    }
    private void listarTrofeos() {
        databaseReference.child("trofeos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaTrofeo = new ArrayList<Trofeo>();
                listaTrofeo.clear();
                for(DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Trofeo t = objSnaptshot.getValue(Trofeo.class);
                    listaTrofeo.add(t);
                    arrayAdapterTrofeo = new ArrayAdapter<Trofeo>(GestionTrofeos.this, android.R.layout.simple_list_item_1, listaTrofeo);
                    listViewTrofeo.setAdapter(arrayAdapterTrofeo);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void limpiarCampos(){
        txtDescripcionTrofeo.setText("");
        txtNombreTrofeo.setText("");
    }

    private boolean validarCampos(){
        if (txtDescripcionTrofeo.getText().toString().isEmpty()){
            txtDescripcionTrofeo.setError("Requerido");
            return false;
        }
        if (txtNombreTrofeo.getText().toString().isEmpty()){
            txtNombreTrofeo.setError("Requerido");
            return false;
        }

        return true;

    }

    public void registrarTrofeo(View v){
        if (!validarCampos()) return;

        String nuevoTrofeo = txtNombreTrofeo.getText().toString().trim();

        databaseReference.child("trofeos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean existe = false;
                for (DataSnapshot trofeoSnapshot : snapshot.getChildren()) {
                    Trofeo trofeoExistente = trofeoSnapshot.getValue(Trofeo.class);
                    if (trofeoExistente != null && trofeoExistente.getNombre().trim().equalsIgnoreCase(nuevoTrofeo)) {
                        existe = true;
                        break;
                    }
                }

                if (existe) {
                    Toast.makeText(GestionTrofeos.this, "Ese nombre de trofeo ya existe. Elija otro.", Toast.LENGTH_SHORT).show();
                    txtNombreTrofeo.setError("Nombre de trofeo ya existente");
                } else {
                    // Crear nuevo usuario si el username no está en uso
                    Trofeo trofeo = new Trofeo();
                    trofeo.setId(databaseReference.push().getKey());
                    trofeo.setDescripcion(txtDescripcionTrofeo.getText().toString());
                    trofeo.setNombre(nuevoTrofeo);


                    databaseReference.child("trofeos").child(trofeo.getId()).setValue(trofeo);
                    Toast.makeText(GestionTrofeos.this, "Trofeo registrado con éxito", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GestionTrofeos.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void actualizarTrofeo(View v) {
        String id = idLocal; // Obtener el ID almacenado
        if (id == null) {
            Toast.makeText(this, "Primero seleeccione un trofeo para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        if (validarCampos()) {
            Trofeo trofeo = new Trofeo();
            trofeo.setId(id);
            trofeo.setDescripcion(txtDescripcionTrofeo.getText().toString());
            trofeo.setNombre(txtNombreTrofeo.getText().toString());


            databaseReference.child("trofeos").child(id).setValue(trofeo);
            Toast.makeText(this, "Trofeo actualizado", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        }
    }

    public void eliminarTrofeo(View v) {
        String id = idLocal;
        if (id == null) {
            Toast.makeText(this, "Primero selecciones una trofeo para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child("trofeos").child(id).removeValue();
        Toast.makeText(this, "Trofeo eliminado", Toast.LENGTH_SHORT).show();
        limpiarCampos();
    }

    public void regresarMenu(View v){
        Intent menuP = new Intent(this, VistaMenu.class);
        startActivity(menuP);
    }


}