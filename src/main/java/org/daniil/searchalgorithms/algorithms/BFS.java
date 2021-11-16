package org.daniil.searchalgorithms.algorithms;

import org.daniil.searchalgorithms.algorithms.tree.CellNode;
import org.daniil.searchalgorithms.model.Panel;
import org.daniil.searchalgorithms.model.area.Cell;

import java.util.ArrayList;
import java.util.List;

public class BFS extends Algorithm{

    public static final Algorithms ALGORITHM_NAME = Algorithms.BFS;

    private final Cell start;
    private final Cell target;

    private final ArrayList<CellNode> path = new ArrayList<>();
    private int index = 0;


    private final VirtualCell[][] virtualCells;

    public BFS(Cell[][] area, Cell start, Cell target) {
        super(area);
        this.start=start;
        this.target=target;
        virtualCells = getVirtualCells();
        //System.out.println(target.getX() + " "  + target.getY());
    }

    public boolean pathExist() {

        ArrayList<CellNode> path = new ArrayList<>();

        VirtualCell[][] cells = getVirtualCells();

        int startX = start.getX();
        int startY = start.getY();

        int toX = target.getX();
        int toY = target.getY();

        CellNode startNode = new CellNode(startX,startY,null);
        cells[startY][startX].setVisited(true);

        int ind = 0;

        path.add(startNode);

        while (ind != path.size()) {

            CellNode temp = path.get(ind);

            if (temp.getX() == toX && temp.getY() == toY)
                return true;

            List<CellNode> children = temp.getChildren(this.getArea());

            for (CellNode child : children) {
                if (!cells[child.getY()][child.getX()].isVisited()) {
                    path.add(child);
                    cells[child.getY()][child.getX()].setVisited(true);
                }
            }
            ind++;
        }
        return false;
    }

    @Override
    public void tick() {
        if (updating) {
            if (path.isEmpty()) path.add(new CellNode(start.getX(), start.getY(), null));
            CellNode temp = path.get(0);
            this.getArea()[temp.getY()][temp.getX()].setCurr(true);

            this.getArea()[temp.getY()][temp.getX()].setPassThrough(true);
            virtualCells[temp.getY()][temp.getX()].setVisited(true);

            if (index==0) {
                this.getArea()[start.getY()][start.getX()].setCurr(false);
                this.getArea()[start.getY()][start.getX()].setPassThrough(false);
                index++;
            }

            if (temp.getX() == target.getX() && temp.getY() == target.getY()) {
                this.getArea()[target.getY()][target.getX()].setPassThrough(false);
                this.found = temp;
                updating = false;
            }

            if (updating) {
                List<CellNode> children = temp.getChildren(this.getArea());

                for (CellNode child : children) {
                    if (!virtualCells[child.getY()][child.getX()].isVisited()) {
                        path.add(child);
                        virtualCells[child.getY()][child.getX()].setVisited(true);
                        this.getArea()[child.getY()][child.getX()].setPassThrough(true);
                    }
                }
            }
            path.remove(temp);
            path.trimToSize();

            if (path.size() >= 30) {
                if (Panel.FPS < 500) {
                    Panel.FPS = 500;
                }
            }
            if (path.size()>=100) {
                if (Panel.FPS < 1000) {
                    Panel.FPS = 1000;
                }
            }

            if (path.isEmpty()) {
                Panel.FPS=100;
                updating=false;
            }
        }
    }


}
