package org.daniil.searchalgorithms.model.area;

import lombok.Getter;
import lombok.Setter;
import org.daniil.searchalgorithms.algorithms.Algorithm;
import org.daniil.searchalgorithms.algorithms.tree.CellNode;
import org.daniil.searchalgorithms.model.MainState;
import org.daniil.searchalgorithms.model.Panel;


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

    @Setter @Getter
    private Algorithm algorithm;

    private final MainState mainState;

    @Setter
    private boolean cleared = false;

    @Getter @Setter
    private boolean needUpdate = false;

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
            if (algorithm.isUpdating()) {
                needUpdate = true;
                algorithm.tick();
            }

            if (algorithm.getFound() != null && !cleared) {

                clearPathRelated();

                CellNode cellNode = algorithm.getFound().getParent();

                while (cellNode != null) {
                    area[cellNode.getY()][cellNode.getX()].setPartOfThePath(true);
                    if (cellNode.getParent() == null)
                        area[cellNode.getY()][cellNode.getX()].setPartOfThePath(false);
                    cellNode = cellNode.getParent();
                }

                Panel.FPS=100;
            }
        }


    }

    public void stopAlgorithm() {

        algorithm.setUpdating(false);
        algorithm = null;

        clearPathRelated();

        Panel.FPS = 100;

    }


    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0, size, size);
        for (Cell[] cells : area) {
            for (Cell cell : cells) {
                cell.draw(g);
            }
        }
        needUpdate = false;
    }

    public void clearArea() {
        for (Cell[] cells : area) {
            for (Cell cell : cells) {
                if (cell.getCellValue() == CellValue.TARGET || cell.getCellValue() == CellValue.START) {
                    cell.dropWalls();
                    continue;
                }
                cell.setCellValue(CellValue.GRASS);
                cell.setPassThrough(false);
                cell.setCurr(false);
                cell.setPartOfThePath(false);
                cell.dropWalls();
            }
        }
        if (algorithm != null) algorithm.setUpdating(false);
        algorithm = null;
        mainState.getUi().setStarted(false);
        cleared = false;
        needUpdate = true;
    }

    public void clearPathRelated() {
        for (Cell[] cells : area) {
            for (Cell cell : cells) {
                cell.setPassThrough(false);
                cell.setCurr(false);
                cell.setPartOfThePath(false);
            }
        }
        cleared = true;
        needUpdate = true;
    }

    public void changeCellValue(int x, int y, CellValue cellValue) {
        area[y][x].setCellValue(cellValue);
        needUpdate = true;
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
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
