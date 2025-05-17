package com.example.kindersoft.vista.juegos.JuegoOtro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import com.example.kindersoft.R;
import com.example.kindersoft.vista.lecciones.leccion1;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class GameOverDialogFragment extends DialogFragment {


    private int finalScore;
    private int pruebaValor;
    private GameOverListener listener;

    // Interfaz para comunicación con la Activity
    public interface GameOverListener {
        void onRestartGame();
    }

    public GameOverDialogFragment(int score) {
        this.finalScore = score;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (GameOverListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " debe implementar GameOverListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());

        // Inflar la vista personalizada
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.game_over, null);

        // Configurar los elementos de la vista
        TextView scoreText = view.findViewById(R.id.tv_final_score);
        scoreText.setText(String.format("Puntuación: %d", finalScore));

        Button restartButton = view.findViewById(R.id.btn_restart);
        restartButton.setOnClickListener(v -> {
            listener.onRestartGame();
            dismiss();
        });

        Button regre = view.findViewById(R.id.btnRegresar);
        regre.setOnClickListener(v -> {
            Intent regresar = new Intent(getActivity(), leccion1.class);
            startActivity(regresar);
        });

        builder.setView(view)
                .setCancelable(false);

        return builder.create();
    }


}