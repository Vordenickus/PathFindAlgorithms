package org.daniil.searchalgorithms.model;

import lombok.Getter;
import org.daniil.searchalgorithms.model.area.GameArea;
import org.daniil.searchalgorithms.model.ui.UI;

import java.awt.*;
import java.awt.event.*;

public class MainState implements KeyListener, MouseListener, MouseMotionListener, GameObject {

    @Getter
    private final GameArea gameArea;
    @Getter
    private final UI ui;

    public MainState(int width, int height) {
        gameArea = new GameArea(width-200, this);
        ui = new UI(gameArea.getSize(),0,200,height, this);
    }

    public void tick() {

        gameArea.setCellValue(ui.getCurrSelected());
        if (ui.isNeedReset()) {
            gameArea.clearArea();
            ui.setNeedReset(false);
        }

        gameArea.tick();

    }

    public void draw(Graphics g) {
        gameArea.draw(g);
        ui.draw(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        gameArea.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gameArea.keyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ui.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gameArea.mousePressed(e);
        ui.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        ui.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        ui.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        ui.mouseExited(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        gameArea.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        gameArea.mouseMoved(e);
    }
}
