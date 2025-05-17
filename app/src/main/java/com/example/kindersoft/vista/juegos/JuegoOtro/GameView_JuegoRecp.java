package com.example.kindersoft.vista.juegos.JuegoOtro;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.kindersoft.R;

public class GameView_JuegoRecp extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private Thread gameThread;
    private SurfaceHolder holder;
    private boolean isPlaying;
    private Canvas canvas;
    private Paint paint;

    // Jugador
    private Rect player;
    private int playerWidth = 200;
    private int playerHeight = 30;

    // Tipos de objetos
    private static final int OBJECT_TYPE_GOOD = 0;
    private static final int OBJECT_TYPE_BAD = 1;

    // Objeto que cae
    private Rect fallingObject;
    private int objectType;
    private int objectSize = 60;
    private float objectSpeed = 30;
    private int score = 0;
    private int lives = 3;

    private MediaPlayer catchSound;
    private MediaPlayer gameOverSound;

    // Colores fijos
    private final int GOOD_OBJECT_COLOR = Color.GREEN;
    private final int BAD_OBJECT_COLOR = Color.RED;
    private final int PLAYER_COLOR = Color.BLUE;


    public GameView_JuegoRecp(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint();
        player = new Rect();
        fallingObject = new Rect();
        lives = 3;

        // Carga los sonidos
        catchSound = MediaPlayer.create(context, R.raw.arcade_beat);
        catchSound.setVolume(1f, 1f);
        catchSound.setLooping(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        initGame();
        resume();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        pause();
        releaseMediaPlayers();
    }


    private void releaseMediaPlayers() {
        if (catchSound != null) catchSound.release();
    }

    private void initGame() {
        score = 0;
        lives = 3;
        playCatchSound();

        // Posición inicial del jugador (centro abajo)
        int startX = getWidth() / 2 - playerWidth / 2;
        int startY = getHeight() - 200;

        player.set(startX, startY, startX + playerWidth, startY + playerHeight);

        spawnFallingObject();
    }


    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            controlFPS();
        }
    }

    private void update() {
        // Mueve el objeto hacia abajo
        fallingObject.top += objectSpeed;
        fallingObject.bottom += objectSpeed;

        // Si el objeto sale de la pantalla
        if (fallingObject.top > getHeight()) {
            if (objectType == OBJECT_TYPE_GOOD) {
                // Si era un objeto bueno y no lo atrapaste, pierdes una vida
                loseLife();
                resetearVelocidad();
            }
            spawnFallingObject();
        }

        // Detecta colisiones
        if (Rect.intersects(player, fallingObject)) {
            if (objectType == OBJECT_TYPE_GOOD) {
                score += 10;
            } else if (objectType == OBJECT_TYPE_BAD){
                quitarPuntaje();
                disminuirVelocidad();
            }
            spawnFallingObject();
        }

        aumentarVelocidad();
    }

    private void loseLife() {
        lives--;
        disminuirVelocidad();
        if (lives <= 0) {
            lives = 0; // Asegurar que no sea negativo
            gameOver();
            resetearVelocidad();
        }
    }

    private void draw() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            // Dibuja jugador
            paint.setColor(PLAYER_COLOR);
            canvas.drawRect(player, paint);

            // Dibuja objeto según su tipo
            if (objectType == OBJECT_TYPE_GOOD) {
                // Objeto bueno - Círculo verde
                paint.setColor(GOOD_OBJECT_COLOR);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(fallingObject.centerX(), fallingObject.centerY(), objectSize/2, paint);

                // Borde blanco
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3f);
                canvas.drawCircle(fallingObject.centerX(), fallingObject.centerY(), objectSize/2, paint);
            }
            else {
                // Objeto malo - Cuadrado rojo con X
                paint.setColor(BAD_OBJECT_COLOR);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(fallingObject, paint);

                // Dibuja una "X" blanca
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(5f);
                canvas.drawLine(fallingObject.left, fallingObject.top, fallingObject.right, fallingObject.bottom, paint);
                canvas.drawLine(fallingObject.right, fallingObject.top, fallingObject.left, fallingObject.bottom, paint);
            }

            // Dibuja puntuación y vidas
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(0);
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            canvas.drawText("Puntos: " + score, 50, 450, paint);
            canvas.drawText("Vidas: " + lives, getWidth() - 250, 450, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void spawnFallingObject() {
        int randomX = (int) (Math.random() * (getWidth() - objectSize));
        fallingObject.set(randomX, 0, randomX + objectSize, objectSize);

        // Decide aleatoriamente si es un objeto bueno o malo (70% bueno, 30% malo)
        objectType = Math.random() < 0.55 ? OBJECT_TYPE_GOOD : OBJECT_TYPE_BAD;
    }

    private void controlFPS() {
        try {
            Thread.sleep(16); // 60fps
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        player.offsetTo((int) event.getX() - playerWidth / 2, player.top);
        return true;
    }

    public void pause() {
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void gameOver() {
        isPlaying = false;

        // Obtener la actividad y mostrar el diálogo
        Context context = getContext();
        if (context instanceof Juego_Reco) {
            ((Juego_Reco) context).showGameOverDialog(score);
        }
    }

    private void playCatchSound() {
        if (catchSound != null) {
            catchSound.seekTo(0);
            catchSound.start();
        }
    }

    private void aumentarVelocidad(){
        objectSpeed += .005f;
    }

    private void disminuirVelocidad(){
        objectSpeed -= .010f;
    }

    private void resetearVelocidad(){
        objectSpeed = 30;
    }

    private void quitarPuntaje(){
        score -= 30;
        if(score < 0){
            score = 0;
        }
    }

}
