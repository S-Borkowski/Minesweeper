package com.example.stanisaw.minesweeper.util;

import java.util.Random;

/**
 * Created by Stanisław on 2018-01-11.
 */

public class Generator {

    public static int[][] generate(int bombNumber, final int width, final int height) {
        Random r = new Random();

        int[][] grid = new int[width][height];
        for(int x = 0; x < width; x++) {
            grid[x] = new int[height];
        }
        while(bombNumber > 0) {
            int x = r.nextInt(width);
            int y = r.nextInt(height);

            if(grid[x][y] != -1) {
                grid[x][y] = -1;
                bombNumber--;
            }
        }
        grid = calculateNeighbours(grid, width, height);

        return grid;
    }

    private static int[][] calculateNeighbours(int[][] grid, final int width, final int height) {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                grid[x][y] = getNeighbourNumber(grid, x, y, width, height);
            }
        }
        return grid;
    }

    private static int getNeighbourNumber(final int[][] grid, final int x, final int y, final int width, final int height) {
        if(grid[x][y] == -1) {
            return -1;
        }

        int count = 0;

        if(isMineAt(grid, x - 1, y + 1, width, height)) count++;
        if(isMineAt(grid, x - 1, y, width, height)) count++;
        if(isMineAt(grid, x - 1, y - 1, width, height)) count++;
        if(isMineAt(grid, x, y + 1, width, height)) count++;
        if(isMineAt(grid, x, y - 1, width, height)) count++;
        if(isMineAt(grid, x + 1, y + 1, width, height)) count++;
        if(isMineAt(grid, x + 1, y, width, height)) count++;
        if(isMineAt(grid, x + 1, y - 1, width, height)) count++;

        return count;
    }

    private static boolean isMineAt(final int[][] grid, final int x, final int y, final int width, final int height) {
        if(x >= 0 && y>= 0 && x < width && y < height) {
            if(grid[x][y] == -1) {
                return true;
            }
        }
        return false;
    }
}

