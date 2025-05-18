package com.example.kindersoft.vista.juegos;

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
import com.example.kindersoft.modelo.entidades.Juego;
import com.example.kindersoft.modelo.entidades.Leccion;
import com.example.kindersoft.vista.menu.VistaMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GestionJuego extends AppCompatActivity {
    String idLocal;
    private ArrayList<String> tiposJuego;
    private List<Juego> listaJuegos;
    private Juego juegoSeleccionado;
    private ListView listViewJuegos;
    ArrayAdapter<Juego> arrayAdapterJuegos;
    ArrayAdapter<Leccion> arrayAdapterLecciones;
    private List<Leccion> listaLecciones;
    private EditText txtNombreJ, txtPuntajeMaximo;
    private Spinner spinerTipoJuego, spinnerIdLeccion;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_juego);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarElementos();
        Conexion.inicializarFirebase(this);
        database = Conexion.getDatabase();
        databaseReference = Conexion.getDatabaseReference();

        tiposJuego = new ArrayList<>();
        tiposJuego.add("Seleccione un tipo de juego: ");
        tiposJuego.add("Quizz");
        tiposJuego.add("Retro");

        ArrayAdapter<String> tipo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tiposJuego);
        spinerTipoJuego.setAdapter(tipo);

        listarLecciones();

        listarJuegos();

        listViewJuegos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                juegoSeleccionado = (Juego) parent.getItemAtPosition(position);
                idLocal = juegoSeleccionado.getId();
                txtNombreJ.setText(juegoSeleccionado.getNombre());
                setSpinnerValue(spinerTipoJuego, juegoSeleccionado.getTipo());
                txtPuntajeMaximo.setText(String.valueOf(juegoSeleccionado.getPuntaje_maximos()));
                setLeccionSpinnerValue(juegoSeleccionado.getId_leccion());

            }
        });
    }
    private void listarJuegos() {
        databaseReference.child("juegos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaJuegos = new ArrayList<Juego>();
                listaJuegos.clear();
                for(DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    Juego j = objSnaptshot.getValue(Juego.class);
                    listaJuegos.add(j);
                    arrayAdapterJuegos = new ArrayAdapter<Juego>(GestionJuego.this, android.R.layout.simple_list_item_1, listaJuegos);
                    listViewJuegos.setAdapter(arrayAdapterJuegos);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void listarLecciones() {
        listaLecciones = new ArrayList<>();

        databaseReference.child("lecciones").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaLecciones.clear();
                for (DataSnapshot leccionSnap : snapshot.getChildren()) {
                    Leccion leccion = leccionSnap.getValue(Leccion.class);
                    listaLecciones.add(leccion);
                }

                arrayAdapterLecciones = new ArrayAdapter<>(GestionJuego.this, android.R.layout.simple_spinner_item, listaLecciones);
                arrayAdapterLecciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerIdLeccion.setAdapter(arrayAdapterLecciones);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GestionJuego.this, "Error al cargar lecciones", Toast.LENGTH_SHORT).show();
            }
        });
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
    private void setLeccionSpinnerValue(String idLeccion) {
        for (int i = 0; i < listaLecciones.size(); i++) {
            if (listaLecciones.get(i).getId().equals(idLeccion)) {
                spinnerIdLeccion.setSelection(i);
                break;
            }
        }
    }

    private void inicializarElementos() {
        txtNombreJ = findViewById(R.id.txtNombre);
        txtPuntajeMaximo = findViewById(R.id.txtPuntuacion);
        listViewJuegos = findViewById(R.id.listViewGestion);
        spinerTipoJuego = findViewById(R.id.spinnerUsuarioR);
        spinnerIdLeccion = findViewById(R.id.spinnerIdLeccion);
        listViewJuegos = findViewById(R.id.listViewGestion);

    }
    private void limpiarCampos(){
        txtNombreJ.setText("");
        txtPuntajeMaximo.setText("");
        spinnerIdLeccion.setSelection(0);
        spinerTipoJuego.setSelection(0);
    }



    private boolean validarCampos(){
        if (txtNombreJ.getText().toString().isEmpty()){
            txtNombreJ.setError("Requerido");
            return false;
        }
        if (spinerTipoJuego.getSelectedItem().toString().equals("Seleccione un tipo de juego: ")){
            Toast.makeText(this, "Debe seleccionar un tipo de juego", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtPuntajeMaximo.getText().toString().isEmpty()){
            txtPuntajeMaximo.setError("Requerido");
            return false;
        }

        return true;

    }

    public void registrarJuego(View v){
        if (!validarCampos()) return;

        String nuevoJuego = txtNombreJ.getText().toString().trim();

        databaseReference.child("juegos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean existe = false;
                for (DataSnapshot juegoSnapshot : snapshot.getChildren()) {
                    Juego juegoExistente = juegoSnapshot.getValue(Juego.class);
                    if (juegoExistente != null && juegoExistente.getNombre().trim().equalsIgnoreCase(nuevoJuego)) {
                        existe = true;
                        break;
                    }
                }

                if (existe) {
                    Toast.makeText(GestionJuego.this, "Ese nombre de juego ya existe. Elija otro.", Toast.LENGTH_SHORT).show();
                    txtNombreJ.setError("Nombre de juego ya existente");
                } else {
                    // Crear nuevo usuario si el username no está en uso
                    Juego juego = new Juego();
                    juego.setId(databaseReference.push().getKey());
                    juego.setNombre(nuevoJuego);
                    juego.setTipo(spinerTipoJuego.getSelectedItem().toString().trim());
                    juego.setPuntaje_maximos(Integer.parseInt(txtPuntajeMaximo.getText().toString().trim()));
                    juego.setId_leccion(((Leccion) spinnerIdLeccion.getSelectedItem()).getId());


                    databaseReference.child("juegos").child(juego.getId()).setValue(juego);
                    Toast.makeText(GestionJuego.this, "juegos registrado con éxito", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GestionJuego.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void actualizarJuego(View v) {
        String id = idLocal; // Obtener el ID almacenado
        if (id == null) {
            Toast.makeText(this, "Primero seleeccione un juego para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        if (validarCampos()) {
            Juego game = new Juego();
            game.setId(id);
            game.setNombre(txtNombreJ.getText().toString());
            game.setTipo(spinerTipoJuego.getSelectedItem().toString());
            game.setPuntaje_maximos(Integer.parseInt(txtPuntajeMaximo.getText().toString()));
            game.setId_leccion(((Leccion) spinnerIdLeccion.getSelectedItem()).getId());


            databaseReference.child("juegos").child(id).setValue(game);
            Toast.makeText(this, "Juego actualizado", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        }
    }

    public void eliminarJuego(View v) {
        String id = idLocal;
        if (id == null) {
            Toast.makeText(this, "Primero selecciones una juego para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child("juegos").child(id).removeValue();
        Toast.makeText(this, "Juego eliminado", Toast.LENGTH_SHORT).show();
        limpiarCampos();
    }

    public void regresarMenu(View v){
        Intent menuP = new Intent(this, VistaMenu.class);
        startActivity(menuP);
    }
}