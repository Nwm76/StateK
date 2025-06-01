package com.example.statek;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.statek.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    GridLayout gridPlayer, gridEnemy;
    TextView textViewStatus;
    Button[][] buttonsPlayer = new Button[5][5];
    Button[][] buttonsEnemy = new Button[5][5];
    boolean[][] enemyShips = new boolean[5][5];
    boolean[][] playerShips = new boolean[5][5];
    Random random = new Random();
    int playerHits = 0, botHits = 0;
    final int maxHits = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridPlayer = findViewById(R.id.gridPlayer);
        gridEnemy = findViewById(R.id.gridEnemy);
        textViewStatus = findViewById(R.id.textViewStatus);

        setupGrid(buttonsPlayer, gridPlayer, false);
        setupGrid(buttonsEnemy, gridEnemy, true);
        placeShips(enemyShips);
        placeShips(playerShips);
    }

    void setupGrid(Button[][] buttons, GridLayout grid, boolean isEnemy) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Button btn = new Button(this);
                btn.setLayoutParams(new GridLayout.LayoutParams());
                btn.setText("");
                btn.setMinHeight(120);
                btn.setMinWidth(120);
                int finalI = i;
                int finalJ = j;

                if (isEnemy) {
                    btn.setOnClickListener(v -> playerMove(finalI, finalJ));
                }

                buttons[i][j] = btn;
                grid.addView(btn);
            }
        }
    }

    void placeShips(boolean[][] board) {
        int placed = 0;
        while (placed < maxHits) {
            int x = random.nextInt(5);
            int y = random.nextInt(5);
            if (!board[x][y]) {
                board[x][y] = true;
                placed++;
            }
        }
    }

    void playerMove(int x, int y) {
        Button btn = buttonsEnemy[x][y];
        if (!btn.getText().toString().equals("")) return;

        if (enemyShips[x][y]) {
            btn.setText("X");
            playerHits++;
            textViewStatus.setText("Trafiłeś!");
        } else {
            btn.setText("O");
            textViewStatus.setText("Pudło!");
            botMove();
        }

        checkWinner();
    }

    void botMove() {
        int x, y;
        do {
            x = random.nextInt(5);
            y = random.nextInt(5);
        } while (!buttonsPlayer[x][y].getText().toString().equals(""));

        if (playerShips[x][y]) {
            buttonsPlayer[x][y].setText("X");
            botHits++;
            textViewStatus.setText("Bot trafił!");
        } else {
            buttonsPlayer[x][y].setText("O");
            textViewStatus.setText("Bot chybił!");
        }

        checkWinner();
    }

    void checkWinner() {
        if (playerHits >= maxHits) {
            textViewStatus.setText("Wygrałeś!");
            disableAll();
        } else if (botHits >= maxHits) {
            textViewStatus.setText("Przegrałeś!");
            disableAll();
        }
    }

    void disableAll() {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                buttonsEnemy[i][j].setEnabled(false);
    }
}
