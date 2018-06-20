package com.example.ralph.tictactoeclass;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.security.PublicKey;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public final static int PLAYER_X = 1;
    public final static int PLAYER_O = 2;
    public final static int NO_PLAYER = -1;


    int size = 3;
    LinearLayout rootLayout;
    int currentPlayer;
    TTTButton board[][];
    boolean gameOver;
    BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.rootLayout);

        initGame();

    }

    @Override
    protected void onStart() {
        super.onStart();
        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("rec","hello");

            }
        };
        IntentFilter intentFilter=new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    public void initGame(){
        currentPlayer = PLAYER_O;
        board = new TTTButton[size][size];
        gameOver = false;
        setUpBoard();
    }

    private void setUpBoard() {

        rootLayout.removeAllViews();
        for(int i = 0;i<size;i++){

            LinearLayout rowLayout = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
            rowLayout.setLayoutParams(params);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            for(int j = 0;j<size;j++){
                TTTButton button = new TTTButton(this);
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
                button.setLayoutParams(buttonParams);
                button.setOnClickListener(this);
                rowLayout.addView(button);
                board[i][j] = button;
            }
            rootLayout.addView(rowLayout);

        }

    }

    @Override
    public void onClick(View v) {

        TTTButton button = (TTTButton)v;
        if(!gameOver){
            button.setPlayer(currentPlayer);
            checkGameOver();
            togglePlayer();
        }
        //TODO
    }

    private void checkGameOver() {
        //ROWS
        for(int row = 0;row < size;row ++){
            boolean rowSame = true;
            for(int col = 0;col<size;col++){
                TTTButton currentButton = board[row][col];
                TTTButton rowFirstButton = board[row][0];
                if(currentButton.isEmpty() || currentButton.getPlayer() != rowFirstButton.getPlayer()){
                    rowSame = false;
                    break;
                }
            }
            if(rowSame){
                TTTButton rowFirstButton = board[row][0];
                int player = rowFirstButton.getPlayer();
                onGameOver(player);
                for(int j = 0;j<size;j++){
                    board[row][j].setBackgroundResource(R.drawable.won_bg);
                }
                return;
            }

        }

        //COLS
        for(int col = 0;col< size;col ++){
            boolean colSame = true;
            for(int row = 0;row<size;row++){
                TTTButton currentButton = board[row][col];
                TTTButton colFirstButton = board[0][col];
                if(currentButton.isEmpty() || currentButton.getPlayer() != colFirstButton.getPlayer()){
                    colSame = false;
                    break;
                }
            }
            if(colSame){
                TTTButton colFirstButton = board[0][col];
                int player = colFirstButton.getPlayer();
                onGameOver(player);
                for(int row = 0;row<size;row++){
                    board[row][col].setBackgroundResource(R.drawable.won_bg);
                }
                return;
            }
        }

        //FIRST
        boolean firstSame = true;
        for(int i = 0;i<size;i++){
            TTTButton currentButton = board[i][i];
            TTTButton firstButton = board[0][0];
            if(currentButton.isEmpty() || currentButton.getPlayer() != firstButton.getPlayer()){
                firstSame = false;
                break;
            }
        }
        if(firstSame){
            TTTButton firstButton = board[0][0];
            int player = firstButton.getPlayer();
            onGameOver(player);
            for(int row = 0;row<size;row++){
                board[row][row].setBackgroundResource(R.drawable.won_bg);
            }
            return;
        }

        //Second diag
        boolean secondSame = true;
        for(int i = 0;i<size;i++){
            TTTButton currentButton = board[i][size - i - 1];
            TTTButton firstButton = board[0][size - 1];
            if(currentButton.isEmpty() || currentButton.getPlayer() != firstButton.getPlayer()){
                secondSame = false;
                break;
            }
        }
        if(secondSame){
            TTTButton firstButton = board[0][size - 1];
            int player = firstButton.getPlayer();
            onGameOver(player);
            for(int row = 0;row<size;row++){
                board[row][size - 1 -row].setBackgroundResource(R.drawable.won_bg);
            }
            return;
        }

        //DRAW
        boolean full = true;
        for(int i = 0;i<size;i++){
            for(int j = 0;j<size;j++){
                if(board[i][j].isEmpty()){
                    full = false;
                    break;
                }
            }
        }
        if(full){
            onGameOver(NO_PLAYER);
        }

    }

    private void onGameOver(int player) {
        if(player == PLAYER_X){
            Toast.makeText(this,"X Won",Toast.LENGTH_SHORT).show();
        }else if(player == PLAYER_O){
            Toast.makeText(this,"O Won",Toast.LENGTH_SHORT).show();
        }
        else if(player == NO_PLAYER){
            Toast.makeText(this,"Draw",Toast.LENGTH_SHORT).show();
        }
        gameOver = true;
        for(int i = 0;i<size;i++){
            for(int j = 0;j<size;j++){
                board[i][j].setEnabled(false);
            }
        }
    }

    private void togglePlayer() {
        currentPlayer = currentPlayer == PLAYER_O? PLAYER_X: PLAYER_O;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();
        if(id == R.id.reset){
            initGame();
        }
        else if(id == R.id.three){
            size = 3;
            initGame();
            item.setChecked(true);
        }else if(id == R.id.four){
            size = 4;
            initGame();
            item.setChecked(true);
        }
        else if(id == R.id.five){
            size = 5;
            initGame();
            item.setChecked(true);
        }
        return super.onOptionsItemSelected(item);
    }
}
