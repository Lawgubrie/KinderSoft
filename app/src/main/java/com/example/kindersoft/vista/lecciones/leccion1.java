package com.example.kindersoft.vista.lecciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kindersoft.R;
import com.example.kindersoft.vista.juegos.JuegoOtro.Juego_Reco;
import com.example.kindersoft.vista.juegos.Quizz.QuizzGeometria;

public class leccion1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leccion1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void regresarMenuLeccion(View v){
        Intent regresarMenuLeccion = new Intent(this, MenuLeccion.class);
        startActivity(regresarMenuLeccion);
    }

    public void iniciarJuego1(View v){
        Intent iniciarJuego1 = new Intent(this, Juego_Reco.class);
        startActivity(iniciarJuego1);
    }

    public void iniciarQuizz1(View v){
        Intent iniciarQuizz = new Intent(this, QuizzGeometria.class);
        startActivity(iniciarQuizz);
    }



}