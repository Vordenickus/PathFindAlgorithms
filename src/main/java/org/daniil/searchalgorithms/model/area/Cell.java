package org.daniil.searchalgorithms.model.area;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@EqualsAndHashCode
public class Cell {

    @Getter @Setter
    private CellValue cellValue;

    @Getter
    private final int rawX, rawY;

    private final int width, height;

    @Getter @Setter
    private boolean passThrough;

    @Getter @Setter
    private boolean curr;

    private static final int HORIZONTAL_CHANCE = 40, VERTICAL_CHANCE = 20;

    @Getter @Setter
    protected boolean leftWall=false, rightWall=false, topWall=false, bottomWall=false;

    @Setter
    private boolean partOfThePath = false;

    private final static int lineThickness = 2;

    @Getter
    private final int x, y;


    public Cell(int x, int y, CellValue cellValue, int size) {
        this.x = x;
        this.y = y;
        this.cellValue = cellValue;
        this.width = size;
        this.height = size;
        this.rawX = this.width * this.x;
        this.rawY = this.height * this.y;
    }

    public void initializeWalls() {
        leftWall = passCheck(HORIZONTAL_CHANCE);
        rightWall = passCheck(HORIZONTAL_CHANCE);
        topWall = passCheck(VERTICAL_CHANCE);
        bottomWall = passCheck(VERTICAL_CHANCE);
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

        g.setColor(Color.WHITE);



        if (this.isTopWall())
            g.fillRect(getRawX(),getRawY(),width,lineThickness);

        if (this.isBottomWall())
            g.fillRect(getRawX(),getRawY()+height-lineThickness,width,lineThickness);

        if (this.isLeftWall())
            g.fillRect(getRawX(),getRawY(),lineThickness, height);

        if (this.isRightWall())
            g.fillRect(getRawX()+width-lineThickness,getRawY(),lineThickness,height);

    }


    private boolean passCheck(int chance) {
        return (int) (Math.random()*100) < chance;
    }

    public void dropWalls() {
        leftWall=false;
        rightWall=false;
        topWall=false;
        bottomWall=false;
    }

}
