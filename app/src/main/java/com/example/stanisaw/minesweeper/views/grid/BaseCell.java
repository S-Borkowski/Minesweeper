package com.example.stanisaw.minesweeper.views.grid;

import android.content.Context;
import android.view.View;

/**
 * Created by Stanisław on 2018-01-11.
 */

public abstract class BaseCell extends View {

    private int value;
    private boolean isBomb;
    private boolean isRevealed;
    private boolean isClicked;
    private boolean isFlagged;

    private int x, y;
    private int position;

    public BaseCell(Context context) {
        super(context);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        isBomb = false;
        isRevealed = false;
        isClicked = false;
        isFlagged = false;

        if(value == -1) {
            isBomb = true;
        }

        this.value = value;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed() {
        isRevealed = true;
        invalidate();
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked() {
        this.isClicked = true;
        this.isRevealed = true;
        invalidate();
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public int getXPos() {
        return x;
    }

    public int getYPos() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;

        this.position = x * y;

        invalidate();
    }
}
