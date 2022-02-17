package org.daniil.searchalgorithms.model.ui;

import lombok.Getter;
import lombok.Setter;
import org.daniil.searchalgorithms.algorithms.*;
import org.daniil.searchalgorithms.model.GameObject;
import org.daniil.searchalgorithms.model.MainState;
import org.daniil.searchalgorithms.model.MazeForm;
import org.daniil.searchalgorithms.model.Panel;
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

    @Getter @Setter
    private boolean needUpdate = false;

    @Getter
    private boolean isMaze = false;

    public ArrayList<ButtonObj> objects;

    public ArrayList<ButtonObj> algorithms;

    public ArrayList<ButtonObj> mazeRelated;

    @Getter
    private CellValue currSelected = CellValue.GRASS;

    @Getter
    private Algorithms currAlgorithm = BFS.ALGORITHM_NAME;

    @Getter @Setter
    private boolean needReset = false;

    private ButtonObj startButton, stopButton;

    @Setter
    private boolean started = false;

    private final MainState mainState;

    private static final String[] DOTS = {".","..","..."};


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
        algorithms = new ArrayList<>();

        ButtonObj grass = new ButtonObj(20,100,50,50,Color.GREEN, "grass",
                        CellValue.GRASS,this);
        grass.setSelected(true);
        ButtonObj wall = new ButtonObj(80, 100, 50, 50, Color.WHITE, "Wall",
                        CellValue.WALL, this);
        ButtonObj sand = new ButtonObj(140, 100, 50, 50, Color.YELLOW, "Sand",
                        CellValue.SAND, this);
        ButtonObj water = new ButtonObj(20, 160, 50, 50, Color.BLUE, "Water",
                        CellValue.WATER, this);
        ButtonObj stone = new ButtonObj(80, 160, 50, 50, Color.GRAY.darker(), "Stone",
                CellValue.STONE, this);
        ButtonObj start = new ButtonObj(20, 300, 50, 50, Color.MAGENTA, "Start",
                        CellValue.START, this);
        ButtonObj target = new ButtonObj(80, 300, 50, 50, Color.CYAN, "Target",
                        CellValue.TARGET, this);

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
        initMazeRelated();
    }

    private void setOnClicks(ArrayList<ButtonObj> buttonObjs) {
        for (ButtonObj obj : buttonObjs) {
            obj.setOnClick(()->{
                this.needUpdate = true;
                obj.setSelected(true);
            });
        }
    }

    private void initClearButton() {

        ButtonObj clear = new ButtonObj(20,220, 170, 50, Color.RED,"Clear Area", this);
        objects.add(clear);

        clear.setOnClick(()-> {
            this.setNeedReset(true);

            startButton.setColor(Color.RED);
            stopButton.setColor(Color.RED);

            isMaze=false;
            needUpdate=true;
        });

    }

    private void initAlgorithms() {
        initSelectAlgorithms();
        ButtonObj start = new ButtonObj(20,600,170,50,Color.GREEN, "Start", this);
        ButtonObj stop = new ButtonObj(20, 700, 170, 50, Color.RED, "Stop and clear", this);
        start.setOnClick(()->{
            if (mainState.getGameArea().containStart() && mainState.getGameArea().containTarget()) {

                if (!started) {

                    started = true;

                    Cell startCell = mainState.getGameArea().getStart();
                    Cell targetCell = mainState.getGameArea().getTarget();


                    Algorithm algorithm = AlgorithmLoader.loadAlgorithm(mainState.getGameArea().getArea(),
                            startCell,targetCell, currAlgorithm);

                    mainState.getGameArea().setAlgorithm(algorithm);

                    if (algorithm != null)
                        algorithm.setUpdating(true);

                    stop.setColor(Color.GREEN);
                    start.setColor(Color.RED);

                    if (isMaze) Panel.FPS=600;

                    mainState.getGameArea().setCleared(false);
                }
            }
        });

        stop.setOnClick(()->{
            if (started) {
                started = false;
                mainState.getGameArea().stopAlgorithm();
                stop.setColor(Color.RED);
                start.setColor(Color.GREEN);
            }
        });
        startButton=start;
        stopButton=stop;
        algorithms.add(start);
        algorithms.add(stop);
    }

    private void initSelectAlgorithms() {

        ButtonObj bfs = new ButtonObj(5,450,60,50,Color.BLUE, "BFS",
                BFS.ALGORITHM_NAME, this);
        ButtonObj djikstra = new ButtonObj(75, 450, 60,50, Color.BLUE,"Dijkstra",
                Dijkstra.ALGORITHM_NAME,this);
        ButtonObj aStart = new ButtonObj(145, 450, 60, 50, Color.BLUE, "A*",
                AStar.ALGORITHM_NAME, this);

        algorithms.add(bfs);
        algorithms.add(djikstra);
        algorithms.add(aStart);

        bfs.setSelected(true);

        for (ButtonObj buttonObj : algorithms) {
            buttonObj.setOnClick(()->{
                this.currAlgorithm = buttonObj.getAlgorithm();
                this.needUpdate=true;
                buttonObj.setSelected(true);
            });
        }

    }

    private void initMazeRelated() {

        mazeRelated = new ArrayList<>();

        ButtonObj maze = new ButtonObj(20, 800, 170, 50, Color.GREEN, "Create Maze", this);
        maze.setOnClick(()->{

            if (mainState.getGameArea().containStart() && mainState.getGameArea().containTarget() &&
                    (mainState.getGameArea().getAlgorithm() == null || !mainState.getGameArea().getAlgorithm().isUpdating())) {

                mainState.getGameArea().clearPathRelated();
                startButton.setColor(Color.GREEN);
                stopButton.setColor(Color.RED);
                started=false;
                needUpdate=true;

                maze.setText("Creating"+ DOTS[0]);

                Thread thread = new Thread(()->{
                    int index = 0;
                    MazeForm mazeForm = new MazeForm(mainState.getGameArea().getArea());

                    mazeForm.createMaze();

                    BFS bfs = new BFS(mainState.getGameArea().getArea(), mainState.getGameArea().getStart(),
                            mainState.getGameArea().getTarget());

                    while (!bfs.pathExist()) {
                        index++;
                        if (index == DOTS.length)
                            index = 0;
                        maze.setText("Creating"+ DOTS[index]);
                        needUpdate=true;
                        mazeForm.createMaze();
                    }

                    isMaze = true;

                    mainState.getGameArea().setNeedUpdate(true);

                    Panel.FPS = 600;

                    maze.setText("Create maze");
                    needUpdate = true;


                    for (int i = 0; i < 3; i++) {
                        mainState.getGameArea().setNeedUpdate(true);
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();

            }

        });

        mazeRelated.add(maze);

    }



    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(x,y,width,height);
        for (ButtonObj object : objects) {
            object.draw(g);
        }
        for (ButtonObj obj : algorithms) {
            obj.draw(g);
        }
        for (ButtonObj obj : mazeRelated) {
            obj.draw(g);
        }
        needUpdate = false;
    }

    @Override
    public void tick() {

        if (mainState.getGameArea().containStart()&&mainState.getGameArea().containTarget() &&
                (mainState.getGameArea().getAlgorithm() == null || !mainState.getGameArea().getAlgorithm().isUpdating())) {
            if (mazeRelated.get(0).getColor() != Color.GREEN) {
                mazeRelated.get(0).setColor(Color.GREEN);
                needUpdate=true;
            }
        } else {
            if (mazeRelated.get(0).getColor() != Color.RED) {
                mazeRelated.get(0).setColor(Color.RED);
                needUpdate = true;
            }
        }

        if (mainState.getGameArea().containStart()&&mainState.getGameArea().containTarget() && stopButton.getColor() != Color.GREEN) {
            if (startButton.getColor() != Color.GREEN) {
                startButton.setColor(Color.GREEN);
                needUpdate = true;
            }
        } else {
            if (startButton.getColor() != Color.RED) {
                startButton.setColor(Color.RED);
                needUpdate = true;
            }
        }

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
                this.needUpdate=true;
                return;
            }
        }

        for (ButtonObj obj : algorithms) {
            if (mRect.intersects(obj.getRect())) {
                if (obj.getAlgorithm() != null) {
                    algorithms.forEach(algorithm -> algorithm.setSelected(false));
                    currAlgorithm = obj.getAlgorithm();
                }
                obj.click();
                this.needUpdate=true;
                return;
            }
        }

        for (ButtonObj obj : mazeRelated) {
            if (mRect.intersects(obj.getRect())) {
                obj.click();
                return;
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
