package com.example.stanisaw.minesweeper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stanisaw.minesweeper.views.grid.Grid;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    int points = 0;
    int secretPoints = 0;
    TextView scoreBoard;
    TextView timerTextView;
    long startTime = 0;
    GridView gv;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);

            if (seconds < 10) {
                timerTextView.setText("00"+seconds);
            }
            else if (seconds < 100) {
                timerTextView.setText("0"+seconds);
            }
            else {
                timerTextView.setText(""+seconds);
            }

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gv = findViewById(R.id.minesweeperGridView);

        setContentView(R.layout.activity_main);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, new IntentFilter("lost"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, new IntentFilter("won"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, new IntentFilter("-secret"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, new IntentFilter("secret"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, new IntentFilter("startTimer"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, new IntentFilter("+1"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, new IntentFilter("-1"));
        scoreBoard = findViewById(R.id.points);

        GameEngine.getInstance().createGrid(this);

        timerTextView = findViewById(R.id.timerTextView);
        Toast.makeText(this, "10 mines", Toast.LENGTH_SHORT).show();
    }

    private BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.toString().contains("startTimer") == true) {
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
            }
            else if(intent.toString().contains("won") == true) {
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setVisibility(VISIBLE);
                timerHandler.removeCallbacks(timerRunnable);
                addPoints(100+secretPoints);
            }
            else if (intent.toString().contains("lost") == true) {
                ImageButton button = findViewById(R.id.imageButton);
                button.setImageResource(R.drawable.lose);
                timerHandler.removeCallbacks(timerRunnable);
                addPoints(-100+secretPoints);
            }
            else if (intent.toString().contains("-secret") == true) {
                secretPoints-=5;
            }
            else if (intent.toString().contains("secret") == true) {
                secretPoints+=5;
            }
            else if (intent.toString().contains("+1") == true) {
                addPoints(1);
            }
            else if (intent.toString().contains("-1") == true) {
                addPoints(-1);
            }
        }
    };
    public void onClick(View view) {
        gv = findViewById(R.id.minesweeperGridView);
        ImageButton button = findViewById(R.id.imageButton);
        button.setImageResource(R.drawable.normal);
        gv.setAdapter(null);
        gv.setNumColumns(GameEngine.getInstance().WIDTH);
        gv.setAdapter(new Grid.GridAdapter());
        GameEngine.getInstance().createGrid(this);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setVisibility(View.GONE);
        GameEngine.getInstance().gameState = 0;
        scoreBoard.setText("000");
        points = 0;
        secretPoints = 0;
        timerHandler.removeCallbacks(timerRunnable);
        timerTextView.setText("000");
    }

    public void onClick() {
        gv = findViewById(R.id.minesweeperGridView);
        ImageButton button = findViewById(R.id.imageButton);
        button.setImageResource(R.drawable.normal);
        gv.setAdapter(null);
        gv.setNumColumns(GameEngine.getInstance().WIDTH);
        gv.setAdapter(new Grid.GridAdapter());
        GameEngine.getInstance().createGrid(this);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setVisibility(View.GONE);
        GameEngine.getInstance().gameState = 0;
        scoreBoard.setText("000");
        points = 0;
        secretPoints = 0;
        timerHandler.removeCallbacks(timerRunnable);
        timerTextView.setText("000");
    }

    public void addPoints(int x) {
        points+=x;
        if (points < 0) {
            scoreBoard.setText(""+points);
        }
        else if (points < 10) {
            scoreBoard.setText("00"+points);
        }
        else if (points < 100) {
            scoreBoard.setText("0"+points);
        }
        else {
            scoreBoard.setText(""+points);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options:
                return true;
            case R.id.help:
                Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.help2:
                Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.help3:
                Toast.makeText(this, "Hold to flag/unflag", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.grid:
                return true;
            case R.id.eightXeight:
                GameEngine.getInstance().changeWidthHeight(8, 8);
                GameEngine.getInstance().BOMB_NUMBER = 5;
                Toast.makeText(this, "5 mines", Toast.LENGTH_SHORT).show();
                onClick();
                return true;
            case R.id.tenXten:
                GameEngine.getInstance().changeWidthHeight(10, 10);
                GameEngine.getInstance().BOMB_NUMBER = 10;
                Toast.makeText(this, "10 mines", Toast.LENGTH_SHORT).show();
                onClick();
                return true;
            case R.id.twelveXtwelve:
                GameEngine.getInstance().changeWidthHeight(12, 12);
                GameEngine.getInstance().BOMB_NUMBER = 15;
                Toast.makeText(this, "15 mines", Toast.LENGTH_SHORT).show();
                onClick();
                return true;
            case R.id.fourteenXfourteen:
                GameEngine.getInstance().changeWidthHeight(14, 14);
                GameEngine.getInstance().BOMB_NUMBER = 25;
                Toast.makeText(this, "25 mines", Toast.LENGTH_SHORT).show();
                onClick();
                return true;
            case R.id.mines:
                return true;
            case R.id.FiveMines:
                GameEngine.getInstance().BOMB_NUMBER = 5;
                Toast.makeText(this, "5 mines", Toast.LENGTH_SHORT).show();
                onClick();
                return true;
            case R.id.TenMines:
                GameEngine.getInstance().BOMB_NUMBER = 10;
                Toast.makeText(this, "10 mines", Toast.LENGTH_SHORT).show();
                onClick();
                return true;
            case R.id.FifteenMines:
                GameEngine.getInstance().BOMB_NUMBER = 15;
                Toast.makeText(this, "15 mines", Toast.LENGTH_SHORT).show();
                onClick();
                return true;
            case R.id.TwentyFiveMines:
                GameEngine.getInstance().BOMB_NUMBER = 25;
                Toast.makeText(this, "25 mines", Toast.LENGTH_SHORT).show();
                onClick();
                return true;
            case R.id.FourtyMines:
                GameEngine.getInstance().BOMB_NUMBER = 40;
                Toast.makeText(this, "40 mines", Toast.LENGTH_SHORT).show();
                onClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
