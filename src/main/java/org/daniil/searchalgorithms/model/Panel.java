package org.daniil.searchalgorithms.model;

import lombok.Getter;
import lombok.Setter;
import org.daniil.searchalgorithms.model.area.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Panel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    private final MainState mainState;

    @Getter
    private final int width, height;

    private final Thread mainThread;

    @Getter @Setter
    private boolean running = true;

    public static int FPS = 100;

    private static final long HALF_A_SECOND = 500_000_000;




    public Panel(int width, int height) {
        this.width=width;
        this.height=height;
        mainState = new MainState(this.getWidth(), this.getHeight());

        setPreferredSize(new Dimension(width,height));

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);

        mainThread = new Thread(this);
        mainThread.start();
    }

    @Override
    public void run() {

        long start, elapsed, wait;

        long safe = System.nanoTime();

        while (running) {

            long targetTime = 1000 / FPS;

            start = System.nanoTime();

            tick();

            for (Cell[] cells : mainState.getGameArea().getArea()) {
                for (Cell cell : cells) {
                    if (cell.isNeedUpdate()) {
                        repaint(cell.getRawX(), cell.getRawY(), 10, 10);
                    }
                }
            }

            if (start - safe >= HALF_A_SECOND) {
                repaint(0, 0, 1000, 1000);
                safe = System.nanoTime();
            }

            if (mainState.getUi().isNeedUpdate())
                repaint(1000,0,200,1000);

            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed / 1_000_000;

            if (wait < 0) {
                wait = 5;
            }

            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                setRunning(false);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        mainState.draw(g);
        g.setColor(Color.WHITE);
        g.drawLine(1000,0,1000,1000);
    }

    public void tick() {
        mainState.tick();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        mainState.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        mainState.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        mainState.keyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mainState.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mainState.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mainState.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mainState.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mainState.mouseExited(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mainState.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mainState.mouseMoved(e);
    }
}
