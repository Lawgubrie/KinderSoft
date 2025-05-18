package com.example.kindersoft.vista.juegos.Quizz;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kindersoft.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizzGeometria extends AppCompatActivity {


    int totalQuestions = 5;
    int currentQuestionIndex = 0;
    int correct = 0;
    int wrong = 0;


    ImageView questionImage;
    Button option1, option2, option3, option4;
    TextView questionNumber;

    int[] images = {
            R.drawable.circle,
            R.drawable.square,
            R.drawable.triangle,
            R.drawable.rectangle,
            R.drawable.pentagon
    };

    String[][] options = {
            {"Circle", "Square", "Triangle", "Pentagon"},
            {"Square", "Circle", "Triangle", "Rectangle"},
            {"Triangle", "Circle", "Square", "Pentagon"},
            {"Rectangle", "Circle", "Triangle", "Square"},
            {"Pentagon", "Circle", "Square", "Rectangle"}
    };

    String[] answers = {"Circle", "Square", "Triangle", "Rectangle", "Pentagon"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz_geometria);

        //INICIALIZA LOS ELEMENTOS DE LA INTERFAZ
        questionImage = findViewById(R.id.question_image);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        questionNumber = findViewById(R.id.question_number);

        loadNextQuestion();

        //MANEJO DE CLICS EN OPCIONES
        View.OnClickListener listener = v -> {
            Button clicked = (Button) v;
            String selectedAnswer = clicked.getText().toString();

            if (selectedAnswer.equals(answers[currentQuestionIndex])) {
                correct++;
            } else {
                wrong++;
            }

            currentQuestionIndex++;

            if (currentQuestionIndex == totalQuestions) {
                showScoreDialog(); //MUESTRA VENTANA DE RESULTADO
            } else {
                loadNextQuestion(); //CARGA LA SIGUIENTE PREGUNTA
            }
        };

        option1.setOnClickListener(listener);
        option2.setOnClickListener(listener);
        option3.setOnClickListener(listener);
        option4.setOnClickListener(listener);
    }

    // FUNCIÓN PARA CARGAR LA SIGUIENTE PREGUNTA
    private void loadNextQuestion() {
        questionImage.setImageResource(images[currentQuestionIndex]);
        questionNumber.setText("Pregunta: " + (currentQuestionIndex + 1));

        // Obtiene las opciones de la pregunta actual
        String[] currentOptions = options[currentQuestionIndex];

        // Convertir a lista para poder desordenar
        List<String> shuffledOptions = new ArrayList<>(Arrays.asList(currentOptions));
        Collections.shuffle(shuffledOptions); //Aquí se mezcla

        // Asignar a botones
        option1.setText(shuffledOptions.get(0));
        option2.setText(shuffledOptions.get(1));
        option3.setText(shuffledOptions.get(2));
        option4.setText(shuffledOptions.get(3));
    }

    // FUNCIÓN PARA MOSTRAR LA VENTANA EMERGENTE DE RESULTADO
    private void showScoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resultado Final");
        builder.setMessage("✅ Correctas: " + correct + "\n❌ Incorrectas: " + wrong);
        builder.setPositiveButton("Jugar de nuevo", (dialog, which) -> {
            currentQuestionIndex = 0;
            correct = 0;
            wrong = 0;
            loadNextQuestion();
        });
        builder.setNegativeButton("Salir", (dialog, which) -> {
            finish();
        });
        builder.setCancelable(false);
        builder.show();
    }
}