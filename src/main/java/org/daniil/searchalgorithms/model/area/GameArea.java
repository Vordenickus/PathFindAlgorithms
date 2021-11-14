package org.daniil.searchalgorithms.model.area;

import lombok.Getter;
import lombok.Setter;
import org.daniil.searchalgorithms.algorithms.Algorithm;
import org.daniil.searchalgorithms.algorithms.BFS;
import org.daniil.searchalgorithms.algorithms.tree.CellNode;
import org.daniil.searchalgorithms.model.MainState;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


public class GameArea implements MouseMotionListener, KeyListener {

    @Getter
    private final Cell[][] area;

    @Getter
    private final int size;

    @Setter
    private CellValue cellValue = CellValue.GRASS;

    @Setter
    private Algorithm algorithm;

    private final MainState mainState;

    private boolean cleared = false;

    public GameArea(int size, MainState mainState) {
        this.size = size;
        this.mainState=mainState;
        area = initialize();
    }

    public Cell[][] initialize() {
        Cell[][] target = new Cell[size/10][size/10];

        for (int i = 0, limit = size/10; i < limit; i++) {
            for (int x = 0; x < limit; x++) {
                target[i][x] = new Cell(x,i,CellValue.GRASS, size/100);
            }
        }

        return target;
    }

    public void tick() {

        if (algorithm != null) {
            if (algorithm.isUpdating()) algorithm.tick();

            if (algorithm.getFound() != null && !cleared) {

                clearPathRelated();
                cleared = true;

                CellNode cellNode = algorithm.getFound().getParent();

                while (cellNode != null) {
                    area[cellNode.getY()][cellNode.getX()].setPartOfThePath(true);
                    cellNode = cellNode.getParent();
                }


            }
        }


    }

    public boolean containsWall() {
        for (Cell[] cells : area) {
            for (Cell cell : cells) {
                if (cell.getCellValue() == CellValue.WALL) return true;
            }
        }
        return false;
    }


    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0, size, size);

        for (Cell[] cells : area) {
            for (Cell cell : cells) {
                cell.draw(g);
            }
        }

    }

    public void clearArea() {
        for (Cell[] cells : area) {
            for (Cell cell : cells) {
                cell.setCellValue(CellValue.GRASS);
                cell.setPassThrough(false);
                cell.setCurr(false);
                cell.setPartOfThePath(false);
            }
        }
        if (algorithm != null) algorithm.setUpdating(false);
        algorithm = null;
        mainState.getUi().setStarted(false);
        cleared = false;
    }

    public void clearPathRelated() {
        for (Cell[] cells : area) {
            for (Cell cell : cells) {
                cell.setPassThrough(false);
                cell.setCurr(false);
            }
        }
    }

    public void changeCellValue(int x, int y, CellValue cellValue) {
        area[y][x].setCellValue(cellValue);
    }


    public void mousePressed(MouseEvent e) {
        mouseDragged(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getX()<= this.getSize()) {
            try {
                if (cellValue != CellValue.START && cellValue != CellValue.TARGET) {
                    if (SwingUtilities.isLeftMouseButton(e))
                        this.changeCellValue(e.getX() / 10, e.getY() / 10, cellValue);
                    if (SwingUtilities.isRightMouseButton(e))
                        this.changeCellValue(e.getX() / 10, e.getY() / 10, CellValue.GRASS);
                } else if (cellValue == CellValue.START){
                    if (SwingUtilities.isLeftMouseButton(e) && !containStart())
                        this.changeCellValue(e.getX() / 10, e.getY() / 10, cellValue);
                    if (SwingUtilities.isRightMouseButton(e))
                        this.changeCellValue(e.getX() / 10, e.getY() / 10, CellValue.GRASS);
                } else {
                    if (SwingUtilities.isLeftMouseButton(e) && !containTarget())
                        this.changeCellValue(e.getX() / 10, e.getY() / 10, cellValue);
                    if (SwingUtilities.isRightMouseButton(e))
                        this.changeCellValue(e.getX() / 10, e.getY() / 10, CellValue.GRASS);
                }
                //System.out.println(area[e.getX()/10][e.getY()/10].getCellValue().toString() + " " + e.getX()/10 + " " + e.getY()/10);
            } catch (ArrayIndexOutOfBoundsException ignored) {

            }
        }
    }

    public boolean containStart() {
        for (Cell[] cells : area) {
            for (Cell cell : cells) {
                if (cell.getCellValue() == CellValue.START)
                    return true;
            }
        }
        return false;
    }

    public boolean containTarget() {
        for (Cell[] cells : area) {
            for (Cell cell : cells) {
                if (cell.getCellValue() == CellValue.TARGET)
                    return true;
            }
        }
        return false;
    }

    public Cell getStart() {

        for (Cell[] cells : area) {
            for (Cell cell : cells) {
                if (cell.getCellValue() == CellValue.START)
                    return cell;
            }
        }
        return null;

    }

    public Cell getTarget() {
        for (Cell[] cells : area) {
            for (Cell cell : cells) {
                if (cell.getCellValue() == CellValue.TARGET)
                    return cell;
            }
        }
        return null;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        CellNode cellNode = new CellNode(57,76,null);

        switch (key) {
            case KeyEvent.VK_SPACE:
                break;
            case KeyEvent.VK_1:
                area[76][57].setPassThrough(true);
                break;
            case KeyEvent.VK_2:
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
