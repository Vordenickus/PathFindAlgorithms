package org.daniil.searchalgorithms.model.area;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class Cell {

    @Getter @Setter
    private CellValue cellValue;

    private int rawX, rawY;

    private final int width, height;

    @Getter @Setter
    private boolean passThrough;

    @Getter @Setter
    private boolean curr;

    @Setter
    private boolean partOfThePath = false;

    @Getter
    private final int x, y;


    public Cell(int x, int y, CellValue cellValue, int size) {
        this.x = x;
        this.y = y;
        this.cellValue = cellValue;
        this.width = size;
        this.height = size;
    }

    public void draw(Graphics g) {
        switch (cellValue) {
            case GRASS:
                g.setColor(Color.BLACK);
                break;
            case SAND:
                g.setColor(Color.YELLOW);
                break;
            case WALL:
                g.setColor(Color.WHITE);
                break;
            case STONE:
                g.setColor(Color.GRAY);
                break;
            case WATER:
                g.setColor(Color.BLUE);
                break;
            case START:
                g.setColor(Color.MAGENTA);
                break;
            case TARGET:
                g.setColor(Color.CYAN);
                break;
            case PATH:
                g.setColor(Color.RED);
                break;
        }
        if (passThrough) g.setColor(Color.RED);
        if (curr) g.setColor(Color.GREEN);
        if (partOfThePath) g.setColor(Color.GREEN.darker().darker());

        g.fillRect(getRawX(),getRawY(),width,height);
    }

    public int getRawX() {
        return width * x;
    }

    public int getRawY() {
        return height * y;
    }

}
