package com.example.stanisaw.minesweeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.stanisaw.minesweeper.util.Generator;
import com.example.stanisaw.minesweeper.views.grid.Cell;

/**
 * Created by Stanisław on 2018-01-11.
 */

public class GameEngine extends Activity {
    private static GameEngine instance;

    public int BOMB_NUMBER = 10;
    public int WIDTH = 10;
    public int HEIGHT = 10;
    int gameState = 0;
    int canStart = 1;
    int bombNotFound;
    int notRevealed;

    private Context context;

    private Cell[][] MinesweeperGrid;

    public static GameEngine getInstance() {

        if(instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    private GameEngine() {
    }

    private void sendLocalBroadcast(String string) {
        Intent intent = new Intent(string);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void createGrid(Context context) {
        MinesweeperGrid  = new Cell[WIDTH][HEIGHT];
        this.context = context;
        int[][] generatedGrid = Generator.generate(BOMB_NUMBER, WIDTH, HEIGHT);
        setGrid(context, generatedGrid);
    }

    private void setGrid(Context context, int[][] grid) {
        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {

                if(MinesweeperGrid[x][y] == null) {
                    MinesweeperGrid[x][y] = new Cell(context, x, y);
                }
                MinesweeperGrid[x][y].setValue(grid[x][y]);
                MinesweeperGrid[x][y].invalidate();
            }
        }
    }

    public Cell getCellAt(int position) {
        int x = position % HEIGHT;
        int y = position / HEIGHT;

        return MinesweeperGrid[x][y];
    }

    public Cell getCellAt(int x, int y) {
        return MinesweeperGrid[x][y];
    }

    public void changeWidthHeight(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
    }

    public void click(int posX, int posY) {
        if(posX >= 0 && posY >= 0 && posX < WIDTH && posY < HEIGHT && !getCellAt(posX, posY).isClicked() && !getCellAt(posX, posY).isFlagged() && gameState == 0) {
            getCellAt(posX, posY).setClicked();
            timerStart();
            if(getCellAt(posX, posY).getValue() == 0) {
                for(int x = -1; x <= 1; x++) {
                    for(int y = -1; y <= 1; y++) {
                        if(x != y) {
                            click(posX + x, posY + y);
                        }
                    }
                }
            }
            if(getCellAt(posX, posY).isBomb() && gameState == 0) {
                onGameLost();
            }
            checkEnd();
        }
    }

    public void timerStart() {
        if (canStart == 1) {
            sendLocalBroadcast("startTimer");
            canStart = 0;
        }
    }

    private boolean checkEnd() {
        bombNotFound = BOMB_NUMBER;
        notRevealed = WIDTH * HEIGHT;
        sendLocalBroadcast("+1");
        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                if(getCellAt(x, y).isRevealed()) {
                    notRevealed--;
                }

                if(getCellAt(x, y).isFlagged() && getCellAt(x, y).isBomb()) {
                    notRevealed--;
                    bombNotFound--;
                }
            }
        }
        if(bombNotFound == 0 && notRevealed == 0 && gameState == 0) {
            for(int x = 0; x < WIDTH; x++) {
                for(int y = 0; y < HEIGHT; y++) {
                    if(getCellAt(x, y).isFlagged() && getCellAt(x, y).isBomb()) {
                        sendLocalBroadcast("secret");
                    }
                }
            }
            Toast.makeText(context, "Game Won", Toast.LENGTH_SHORT).show();
            sendLocalBroadcast("won");
            gameState = 1;
            canStart = 1;
        }
        return false;
    }

    public void flag(int x, int y) {
        if(gameState == 0 && !getCellAt(x, y).isClicked()) {
            if(getCellAt(x, y).isFlagged()) {
                if(getCellAt(x, y).isBomb()) {
                    bombNotFound++;
                }
                getCellAt(x, y).setFlagged(false);
                notRevealed++;
                sendLocalBroadcast("-1");
            }
            else if (!getCellAt(x, y).isFlagged()) {
                getCellAt(x, y).setFlagged(true);
                checkEnd();
            }
            getCellAt(x, y).invalidate();
        }
    }

    private void onGameLost() {
        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                getCellAt(x, y).setRevealed();
                if(getCellAt(x, y).isFlagged() && getCellAt(x, y).isBomb()) {
                    sendLocalBroadcast("secret");
                }
                else if (getCellAt(x, y).isFlagged()) {
                    sendLocalBroadcast("-secret");
                }

                if(getCellAt(x, y).isFlagged() && getCellAt(x, y).isBomb()){
                    getCellAt(x, y).setFlagged(false);
                }
            }
        }
        Toast.makeText(context, "Game Lost", Toast.LENGTH_SHORT).show();
        sendLocalBroadcast("lost");
        gameState = 1;
        canStart = 1;
    }
}



