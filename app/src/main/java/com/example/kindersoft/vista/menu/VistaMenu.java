package com.example.kindersoft.vista.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kindersoft.R;
import com.example.kindersoft.vista.autenticacion.GestionRegistro;
import com.example.kindersoft.vista.autenticacion.Login;

public class VistaMenu extends AppCompatActivity {

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

        if (item.getItemId() == R.id.itemSalir) {
            Intent login = new Intent(this, Login.class);
            startActivity(login);
        }
        return true;

    }

    public void abiriUsuario(View v){
        Intent abiriUsuario = new Intent(this, GestionRegistro.class);
        startActivity(abiriUsuario);
    }

    public void abrirLecciones(View v){
        Intent abrirLecciones = new Intent(this, Login.class);
        startActivity(abrirLecciones);
    }

    public void abrirTrofeos(View v){
        Intent abrirTrofeos = new Intent(this, Login.class);
        startActivity(abrirTrofeos);
    }

    public void abrirReporte(View v){
        Intent abrirReporte = new Intent(this, Login.class);
        startActivity(abrirReporte);
    }

    public void salir(View v){

        Intent salir = new Intent(this, Login.class);
        startActivity(salir);

    }

}