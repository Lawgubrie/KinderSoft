package com.example.kindersoft.vista.reportes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kindersoft.R;
import com.example.kindersoft.modelo.database.Conexion;
import com.example.kindersoft.modelo.entidades.Reporte;
import com.example.kindersoft.modelo.entidades.Usuario;
import com.example.kindersoft.vista.menu.VistaMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GestionReporte extends AppCompatActivity {
    String idLocal;
    private Reporte reporteSeleccionado;
    private ListView listViewReporte;
    ArrayAdapter<Reporte> arrayAdapterReporte;
    private List<Reporte> listaReporte;
    ArrayAdapter<Usuario> arrayAdapterUsuarios;
    private List<Usuario> listaUsuarios;
    private EditText txtPuntuacion;
    private Spinner spinnerIdUsuario;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_reporte);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarElementos();
        Conexion.inicializarFirebase(this);
        database = Conexion.getDatabase();
        databaseReference = Conexion.getDatabaseReference();

        listarUsuarios();
        listarReportes();

        listViewReporte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                reporteSeleccionado = (Reporte) parent.getItemAtPosition(position);
                idLocal = reporteSeleccionado.getId();
                setUsuariosSpinnerValue(reporteSeleccionado.getIdUsuario());
                txtPuntuacion.setText(String.valueOf(reporteSeleccionado.getPuntuacion()));

            }
        });

    }

    private void listarReportes() {
        databaseReference.child("reportes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaReporte = new ArrayList<Reporte>();
                listaReporte.clear();
                for(DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Reporte r = objSnaptshot.getValue(Reporte.class);
                    listaReporte.add(r);
                    arrayAdapterReporte = new ArrayAdapter<Reporte>(GestionReporte.this, android.R.layout.simple_list_item_1, listaReporte);
                    listViewReporte.setAdapter(arrayAdapterReporte);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void listarUsuarios() {
        listaUsuarios = new ArrayList<>();

        databaseReference.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaUsuarios.clear();
                for (DataSnapshot usuarioSnap : snapshot.getChildren()) {
                    Usuario usuario = usuarioSnap.getValue(Usuario.class);
                    listaUsuarios.add(usuario);
                }
                arrayAdapterUsuarios = new ArrayAdapter<>(GestionReporte.this, android.R.layout.simple_spinner_item, listaUsuarios);
                arrayAdapterUsuarios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerIdUsuario.setAdapter(arrayAdapterUsuarios);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GestionReporte.this, "Error al cargar usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUsuariosSpinnerValue(String idLeccion) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getId().equals(idLeccion)) {
                spinnerIdUsuario.setSelection(i);
                break;
            }
        }
    }

    private void inicializarElementos() {
        spinnerIdUsuario = findViewById(R.id.spinnerUsuarioR);
        txtPuntuacion = findViewById(R.id.txtPuntuacion);
        listViewReporte = findViewById(R.id.listViewGestion);
    }

    private void limpiarCampos(){
        txtPuntuacion.setText("");
        spinnerIdUsuario.setSelection(0);
    }



    private boolean validarCampos(){
        if (txtPuntuacion.getText().toString().isEmpty()){
            txtPuntuacion.setError("Requerido");
            return false;
        }

        return true;

    }

    public void registrarReporte(View v){
        if (validarCampos()){

            Reporte reporte = new Reporte();
            reporte.setId(databaseReference.push().getKey());
            reporte.setIdUsuario(((Usuario) spinnerIdUsuario.getSelectedItem()).getId());
            reporte.setPuntuacion(Integer.parseInt(txtPuntuacion.getText().toString().trim()));



            databaseReference.child("reportes").child(reporte.getId()).setValue(reporte);
            Toast.makeText(GestionReporte.this, "reporte registrado con éxito", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        }
    }

    public void actualizarReporte(View v) {
        String id = idLocal; // Obtener el ID almacenado
        if (id == null) {
            Toast.makeText(this, "Primero seleeccione un reporte para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        if (validarCampos()) {
            Reporte repo = new Reporte();
            repo.setId(id);
            repo.setIdUsuario(((Usuario) spinnerIdUsuario.getSelectedItem()).getId());
            repo.setPuntuacion(Integer.parseInt(txtPuntuacion.getText().toString()));


            databaseReference.child("reportes").child(id).setValue(repo);
            Toast.makeText(this, "Reporte actualizado", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        }
    }

    public void eliminarReporte(View v) {
        String id = idLocal;
        if (id == null) {
            Toast.makeText(this, "Primero selecciones una reporte para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child("reportes").child(id).removeValue();
        Toast.makeText(this, "Reporte eliminado", Toast.LENGTH_SHORT).show();
        limpiarCampos();
    }

    public void regresarMenu(View v){
        Intent menuP = new Intent(this, VistaMenu.class);
        startActivity(menuP);
    }
}