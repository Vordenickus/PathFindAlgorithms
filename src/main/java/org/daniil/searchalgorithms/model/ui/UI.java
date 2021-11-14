package org.daniil.searchalgorithms.model.ui;

import lombok.Getter;
import lombok.Setter;
import org.daniil.searchalgorithms.algorithms.AStar;
import org.daniil.searchalgorithms.algorithms.Dijkstra;
import org.daniil.searchalgorithms.model.GameObject;
import org.daniil.searchalgorithms.model.MainState;
import org.daniil.searchalgorithms.model.area.Cell;
import org.daniil.searchalgorithms.model.area.CellValue;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class UI implements KeyListener, MouseListener, GameObject {

    @Getter
    private final int x, y, width, height;

    public ArrayList<ButtonObj> objects;

    @Getter
    private CellValue currSelected = CellValue.GRASS;

    @Getter @Setter
    private boolean needReset = false;

    @Setter
    private boolean started = false;

    private final MainState mainState;


    public UI(int x, int y, int width, int height, MainState mainState) {
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.mainState=mainState;
        init();
    }

    public void init() {

        objects = new ArrayList<>();

        ButtonObj grass = new ButtonObj(20,100,50,50,Color.GREEN, "grass", CellValue.GRASS,this);
        grass.setSelected(true);
        ButtonObj wall = new ButtonObj(80, 100, 50, 50, Color.WHITE, "Wall", CellValue.WALL, this);
        ButtonObj sand = new ButtonObj(140, 100, 50, 50, Color.YELLOW, "Sand", CellValue.SAND, this);
        ButtonObj water = new ButtonObj(20, 160, 50, 50, Color.BLUE, "Water", CellValue.WATER, this);
        ButtonObj stone = new ButtonObj(80, 160, 50, 50, Color.GRAY.darker(), "Stone", CellValue.STONE, this);
        ButtonObj start = new ButtonObj(20, 300, 50, 50, Color.MAGENTA, "Start", CellValue.START, this);
        ButtonObj target = new ButtonObj(80, 300, 50, 50, Color.CYAN, "Target", CellValue.TARGET, this);

        objects.add(grass);
        objects.add(wall);
        objects.add(sand);
        objects.add(water);
        objects.add(stone);
        objects.add(start);
        objects.add(target);

        setOnClicks(objects);
        initClearButton();
        initAlgorithms();
    }

    private void setOnClicks(ArrayList<ButtonObj> buttonObjs) {
        for (ButtonObj obj : buttonObjs) {
            obj.setOnClick(()->{
                obj.setSelected(true);
            });
        }
    }

    private void initClearButton() {

        ButtonObj clear = new ButtonObj(20,220, 170, 50, Color.RED,"Clear Area", null, this);
        objects.add(clear);

        clear.setOnClick(()->this.setNeedReset(true));

    }

    private void initAlgorithms() {
        ButtonObj start = new ButtonObj(20,400,170,50,Color.RED, "Start", null, this);
        start.setOnClick(()->{
            if (mainState.getGameArea().containStart()&&mainState.getGameArea().containTarget()) {

                if (!started) {

                    started = true;

                    Cell startCell = mainState.getGameArea().getStart();
                    Cell targetCell = mainState.getGameArea().getTarget();

                    AStar bfs = new AStar(mainState.getGameArea().getArea(), startCell, targetCell);

                    mainState.getGameArea().setAlgorithm(bfs);

                    bfs.setUpdating(true);


                }

            }
        });

        objects.add(start);
    }



    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(x,y,width,height);
        for (ButtonObj object : objects) {
            object.draw(g);
        }
    }

    @Override
    public void tick() {

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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        int mX = e.getX();
        int mY = e.getY();
        int mSize = 10;

        Rectangle mRect = new Rectangle(mX-mSize/2, mY-mSize/2, mSize, mSize);

        for (ButtonObj object : objects) {
            if (mRect.intersects(object.getRect())) {
                if (object.getCellValue() != null) {
                    objects.forEach(buttonObj -> buttonObj.setSelected(false));
                    currSelected = object.getCellValue();
                }
                object.click();
                break;
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
