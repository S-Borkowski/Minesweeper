package com.example.stanisaw.minesweeper.views.grid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.stanisaw.minesweeper.GameEngine;
import com.example.stanisaw.minesweeper.R;

/**
 * Created by Stanisław on 2018-01-11.
 */

public class Cell extends BaseCell implements View.OnClickListener, View.OnLongClickListener {

    public Cell(Context context, int x, int y) {
        super(context);

        setPosition(x, y);

        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public void onClick(View v) {
        GameEngine.getInstance().click(getXPos(), getYPos());
    }

    @Override
    public boolean onLongClick(View v) {
        GameEngine.getInstance().flag(getXPos(), getYPos());
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawButton(canvas);

        if(isFlagged()) {
            drawFlag(canvas);
        }
        else if(isRevealed() && isBomb() && !isClicked()) {
            drawNormalBomb(canvas);
        }
        else {
            if(isClicked()) {
                if(getValue() == -1) {
                    drawBombExploded(canvas);
                }
                else {
                    drawNumber(canvas);
                }
            }
            else {
                drawButton(canvas);
            }
        }
    }

    public void drawBombExploded(Canvas canvas) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bomb_exploded);
        drawable.setBounds(0, 0, getWidth(), getHeight());
        drawable.draw(canvas);
    }

    private void drawNormalBomb(Canvas canvas) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bomb_normal);
        drawable.setBounds(0, 0, getWidth(), getHeight());
        drawable.draw(canvas);
    }

    private void drawFlag(Canvas canvas) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.flag);
        drawable.setBounds(0, 0, getWidth(), getHeight());
        drawable.draw(canvas);
    }

    private void drawButton(Canvas canvas) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.button);
        drawable.setBounds(0, 0, getWidth(), getHeight());
        drawable.draw(canvas);
    }

    private void drawNumber(Canvas canvas) {
        Drawable drawable = null;

        switch(getValue()) {
            case 0:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.zero);
                break;
            case 1:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.one);
                break;
            case 2:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.two);
                break;
            case 3:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.three);
                break;
            case 4:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.four);
                break;
            case 5:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.five);
                break;
            case 6:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.six);
                break;
            case 7:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.seven);
                break;
            case 8:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.eight);
                break;
        }

        drawable.setBounds(0, 0, getWidth(), getHeight());
        drawable.draw(canvas);
    }
}

