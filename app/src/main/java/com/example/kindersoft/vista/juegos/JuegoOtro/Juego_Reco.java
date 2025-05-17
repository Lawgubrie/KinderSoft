package com.example.kindersoft.vista.juegos.JuegoOtro;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.kindersoft.R;

public class Juego_Reco extends AppCompatActivity
        implements GameOverDialogFragment.GameOverListener{

    private GameView_JuegoRecp gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startNewGame();
    }

    private void startNewGame() {
        if (gameView != null) {
            gameView.pause();
        }
        gameView = new GameView_JuegoRecp(this);
        setContentView(gameView);
    }

    public void showGameOverDialog(int score) {
        GameOverDialogFragment dialog = new GameOverDialogFragment(score);
        FragmentManager fm = getSupportFragmentManager();
        dialog.show(fm, "game_over_dialog");
    }

    @Override
    public void onRestartGame() {
        startNewGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameView != null) {
            gameView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameView != null) {
            gameView.resume();
        }
    }
}