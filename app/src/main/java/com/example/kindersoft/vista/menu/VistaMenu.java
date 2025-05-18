package com.example.kindersoft.vista.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kindersoft.vista.juegos.GestionJuego;
import com.example.kindersoft.R;
import com.example.kindersoft.modelo.entidades.Usuario;
import com.example.kindersoft.vista.autenticacion.GestionRegistro;
import com.example.kindersoft.vista.autenticacion.Login;
import com.example.kindersoft.vista.lecciones.GestionLeccion;
import com.example.kindersoft.vista.reportes.GestionReporte;
import com.example.kindersoft.vista.trofeos.GestionTrofeos;

import java.util.Map;

public class VistaMenu extends AppCompatActivity {
    private Usuario usuarioLeer;
    private TextView txtSaludoUser;
    private LinearLayout menu;
    private String[] menuEstudiantes = { "lecciones", "trofeos", "reporte", "salir" };
    private String[] menuAdmin = { "usuarios", "lecciones", "trofeos", "reporte","", "salir" };
    private Map<String , Class<?>> actividades = Map.of(
            "usuarios", GestionRegistro.class,
            "lecciones", GestionLeccion.class,
            "trofeos", GestionTrofeos.class,
            "reporte", GestionReporte.class,
            "", GestionJuego.class,
            "salir", Login.class
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vista_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usuarioLeer = obtenerUsuarioDePreferencias();
        Log.i("Leer usuario", usuarioLeer.toString());
        txtSaludoUser = findViewById(R.id.txtvSaludoMenu);
        txtSaludoUser.setText("Bienvenido " + usuarioLeer.getNombres() + "\n" + "Tu rol es: " + usuarioLeer.getRol());

        crearMenu();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuPrincipal = getMenuInflater();
        menuPrincipal.inflate(R.menu.menu_p, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.itemAcercaDe)
            Toast.makeText(this, "presiono sobre configuracion", Toast.LENGTH_SHORT).show();

        return true;

    }

    private Usuario obtenerUsuarioDePreferencias() {
        SharedPreferences sharedPref = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        Usuario usuario = new Usuario();
        usuario.setId(sharedPref.getString("id", null));
        usuario.setUsername(sharedPref.getString("username", null));
        usuario.setNombres(sharedPref.getString("nombres", null));
        usuario.setEdad(sharedPref.getInt("edad", 0));
        usuario.setRol(sharedPref.getString("rol", null));
        usuario.setPuntos_totales(sharedPref.getInt("puntos_totales", 0));
        return usuario;
    }

    private void crearMenu(){
        menu = findViewById(R.id.linearLayoutMenu);
        menu.removeAllViews();
        if (usuarioLeer.getRol().equals("estudiante")) {
            seleccionarMenu(menuEstudiantes);
        } else if (usuarioLeer.getRol().equals("admin")) {
            seleccionarMenu(menuAdmin);
        }
    }

    private void seleccionarMenu(String[] itemMenu){
        for (String item : itemMenu) {
            ImageButton boton = new ImageButton(this);
            boton.setBackground(null);
            boton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            int resId = getResources().getIdentifier(item, "drawable", getPackageName());
            if (resId != 0) {
                boton.setImageResource(resId);
            }else{
                boton.setImageResource(R.drawable.ic_launcher_foreground);
            }
            boton.setTag(item);
            boton.setOnClickListener(v -> {
                String opcion = (String) v.getTag();
                if (opcion != null) {
                    if (opcion.equals("salir")) {
                        borrarDatosUsuario();
                        borrarCredenciales();
                        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VistaMenu.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // limpiar pila de actividades
                        startActivity(intent);
                        finish();
                    } else {
                        Class<?> actividadDestino = actividades.get(opcion);
                        if (actividadDestino != null) {
                            Intent intent = new Intent(VistaMenu.this, actividadDestino);
                            startActivity(intent);
                        }
                    }
                }
            });
            menu.addView(boton);
        }
    }

    public void borrarDatosUsuario() {
        SharedPreferences splogin = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = splogin.edit();
        editor.clear();
        editor.apply();
    }
    public void borrarCredenciales() {
        SharedPreferences splogin = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = splogin.edit();
        editor.clear();
        editor.apply();
    }


}