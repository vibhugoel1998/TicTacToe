package com.example.ralph.tictactoeclass;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.widget.Button;

/**
 * Created by ralph on 28/01/18.
 */

public class TTTButton extends AppCompatButton {

    private int player = MainActivity.NO_PLAYER;

    public TTTButton(Context context){
        super(context);
        setBackgroundResource(R.drawable.button_bg);
    }

    public void setPlayer(int player) {
        this.player = player;
        if(player == MainActivity.PLAYER_O){
            setText("O");
        }else if(player == MainActivity.PLAYER_X){
            setText("X");
        }
        setEnabled(false);
    }

    public int getPlayer() {
        return player;
    }

    public boolean isEmpty(){
        return player == MainActivity.NO_PLAYER;
    }
}
