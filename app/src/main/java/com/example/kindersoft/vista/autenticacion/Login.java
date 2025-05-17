package com.example.kindersoft.vista.autenticacion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kindersoft.R;
import com.example.kindersoft.vista.menu.VistaMenu;


public class Login extends AppCompatActivity {

    private EditText txtUser;
    private EditText txtContrasena;
    private CheckBox recordar;
    private Intent ventanaPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Verificar si ya hay credenciales guardadas
//        if(credencialExistente()) {
//            startActivity(new Intent(this, VistaMenu.class));
//            finish(); // Cierra el LoginActivity para que no se pueda volver atrás
//            return;
//        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtUser = findViewById(R.id.txtUsuarioL);
        txtContrasena = findViewById(R.id.txtClaveL);
        recordar = findViewById(R.id.cbRecordarL);
        //ventanaPrincipal = new Intent(Login.this, VistaMenu.class);
        //leerCredenciales();
    }

    public void ingresar(View v){
        Intent menu_principal = new Intent(this, VistaMenu.class);
        startActivity(menu_principal);
    }

    public void registrar(View v){
        Intent registroU = new Intent(this, VistaRegistro.class);
        startActivity(registroU);
    }

    private boolean credencialExistente() {
        SharedPreferences splogin = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String usuario = splogin.getString("spUsuario", "");
        String clave = splogin.getString("spClave", "");
        return !usuario.isEmpty() && !clave.isEmpty();
    }

    private void autenticar(){
        String usuario = txtUser.getText().toString().trim();
        String clave = txtContrasena.getText().toString().trim();
        if (usuario.isEmpty() || clave.isEmpty()){
            return;
        }
        if (recordar.isChecked()) {
            guardarContrasenia(usuario, clave);
        } else {
            // Si no se marca el checkbox, borra las credenciales
            borrarCredenciales();
        }
    }

    private void guardarContrasenia(String usuario, String clave){
        SharedPreferences splogin = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor speditlogin = splogin.edit();
        speditlogin.putString("spUsuario",usuario);
        speditlogin.putString("spClave", clave);
        speditlogin.apply();
    }
    private void leerCredenciales(){
        SharedPreferences splogin = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String usuario = splogin.getString("spUsuario", "");
        String clave = splogin.getString("spClave", "");

        if(!usuario.isEmpty() && !clave.isEmpty()) {
            txtUser.setText(usuario);
            txtContrasena.setText(clave);
            recordar.setChecked(true); // Marcamos el checkbox si hay credenciales
        }
    }

    public void borrarCredenciales() {
        SharedPreferences splogin = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = splogin.edit();
        editor.clear();
        editor.apply();
    }



}