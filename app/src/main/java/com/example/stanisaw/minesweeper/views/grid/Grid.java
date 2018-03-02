package com.example.stanisaw.minesweeper.views.grid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.stanisaw.minesweeper.GameEngine;

/**
 * Created by Stanisław on 2018-01-11.
 */

public class Grid extends GridView {

    public Grid(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        GameEngine.getInstance().createGrid(context);

        setNumColumns(GameEngine.getInstance().WIDTH);

        setAdapter(new GridAdapter());
    }

    public static class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return GameEngine.getInstance().WIDTH * GameEngine.getInstance().HEIGHT;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return GameEngine.getInstance().getCellAt(position);
        }
    }
}

