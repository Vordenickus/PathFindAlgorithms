package org.daniil.searchalgorithms.model;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Panel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    private final MainState mainState;

    @Getter
    private final int width, height;

    private Thread mainThread;

    @Getter @Setter
    private boolean running = true;

    private static final int FPS = 1000;



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

        long targetTime = 1000 / FPS;

        while (running) {

            start = System.nanoTime();

            tick();
            repaint();

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
