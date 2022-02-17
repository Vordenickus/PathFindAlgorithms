package org.daniil.searchalgorithms.model.area;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.awt.*;

@EqualsAndHashCode
public class Cell {

    @Getter
    private CellValue cellValue;

    @Getter
    private final int rawX, rawY;

    private final int width, height;

    //Показывает что эта клетка ожидает оценки в алгоритме поиска пути
    @Getter
    private boolean passThrough;

    // Показывает что данная клетка прошла оценку в алгоритмке поиска пути
    @Getter
    private boolean curr;

    // шансы на появление стен в алгоритме формирования лабиринта, максимальные рекомендуемые числа
    // 40 - HORIZONTAL_CHANCE, 20 - VERTICAL_CHANCE, или соответственно наоборот,
    // 40 - VERTICAL_CHANCE, 20 - HORIZONTAL_CHANCE, стоит учитывать что из-за особенностей алгоритма
    // даные шансы в реальности получаются примерно умноженными на 2, поэтому настоятельно не рекомендуется
    // ставить шанс от 50 и выше, алгоритм может перестать работать
    private static final int HORIZONTAL_CHANCE = 40, VERTICAL_CHANCE = 20;

    @Getter
    protected boolean leftWall=false, rightWall=false, topWall=false, bottomWall=false;

    // Показывает что данная клетка является частью пути который выдал алгоритм
    private boolean partOfThePath = false;

    private final static int LINE_THICKNESS = 2;

    @Getter
    private boolean needUpdate = false;

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
        needUpdate = true;
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

            g.fillRect(getRawX(), getRawY(), width, height);

            g.setColor(Color.WHITE);


            if (this.isTopWall())
                g.fillRect(getRawX(), getRawY(), width, LINE_THICKNESS);

            if (this.isBottomWall())
                g.fillRect(getRawX(), getRawY() + height - LINE_THICKNESS, width, LINE_THICKNESS);

            if (this.isLeftWall())
                g.fillRect(getRawX(), getRawY(), LINE_THICKNESS, height);

            if (this.isRightWall())
                g.fillRect(getRawX() + width - LINE_THICKNESS, getRawY(), LINE_THICKNESS, height);

            needUpdate = false;
    }


    private boolean passCheck(int chance) {
        return (int) (Math.random()*100) < chance;
    }

    public void dropWalls() {
        leftWall=false;
        rightWall=false;
        topWall=false;
        bottomWall=false;
        needUpdate = true;
    }

    public void setCellValue(CellValue cellValue) {
        this.cellValue = cellValue;
        needUpdate = true;
    }

    public void setPassThrough(boolean passThrough) {
        this.passThrough = passThrough;
        needUpdate = true;
    }

    public void setCurr(boolean curr) {
        this.curr = curr;
        needUpdate = true;
    }

    public void setLeftWall(boolean leftWall) {
        this.leftWall = leftWall;
        needUpdate = true;
    }

    public void setRightWall(boolean rightWall) {
        this.rightWall = rightWall;
        needUpdate = true;
    }

    public void setTopWall(boolean topWall) {
        this.topWall = topWall;
        needUpdate = true;
    }

    public void setBottomWall(boolean bottomWall) {
        this.bottomWall = bottomWall;
        needUpdate = true;
    }

    public void setPartOfThePath(boolean partOfThePath) {
        this.partOfThePath = partOfThePath;
        needUpdate = true;
    }
}
