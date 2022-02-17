package org.daniil.searchalgorithms.algorithms;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.daniil.searchalgorithms.algorithms.tree.CellNode;
import org.daniil.searchalgorithms.model.Panel;
import org.daniil.searchalgorithms.model.area.Cell;

import java.util.*;

public class AStar extends Algorithm{

    public static final Algorithms ALGORITHM_NAME = Algorithms.ASTAR;

    private final Cell start;
    private final Cell target;


    private final  PriorityQueue<CellNode> path = new PriorityQueue<>(Comparator.comparingDouble(CellNode::getValueAStar));
    // Сюда кладем исследованые ноды, вэлью мапы не важно, можно пихать что угодно
    private final HashMap<HashIndex, CellNode> doneWith = new HashMap<>();

    private int index = 0;
    private final VirtualCell[][] virtualCells;

    public AStar(Cell[][] area, Cell start, Cell target) {
        super(area);
        this.start=start;
        this.target=target;
        this.virtualCells = this.getVirtualCells();
    }


    @Override
    public void tick() {

        if (this.isUpdating()) {

            if (path.isEmpty()) {
                path.add(new CellNode(start.getX(),start.getY(), 0, null));
                path.peek().setValueAStar(0);
            }

            CellNode temp = path.poll();

            this.getArea()[temp.getY()][temp.getX()].setCurr(true);
            this.getArea()[temp.getY()][temp.getX()].setPassThrough(true);

            virtualCells[temp.getY()][temp.getX()].setVisited(true);

            if (index==0) {
                this.getArea()[start.getY()][start.getX()].setPassThrough(false);
                this.getArea()[start.getY()][start.getX()].setCurr(false);
                System.out.println(temp.getValueAStar());
                index++;
            }

            if (temp.getX() == target.getX() && temp.getY() == target.getY()) {
                this.getArea()[target.getY()][target.getX()].setPassThrough(false);
                this.getArea()[target.getY()][target.getX()].setCurr(false);
                this.setUpdating(false);
                this.found = temp;
            }

            if (this.isUpdating()) {

                List<CellNode> children = temp.getChildrenAStart(this.getArea(), this.target.getX(),this.target.getY());

                for (CellNode child : children) {

                    if (isDoneWith(child)) continue;

                    if (!containsSame(child)) {
                        path.add(child);
                        this.getArea()[child.getY()][child.getX()].setPassThrough(true);
                    } else {

                        CellNode sameNode = this.getSame(child);

                        if (child.getValueAStar() < sameNode.getValueAStar()) {
                            path.remove(child);
                            path.add(child);
                        }
                    }
                }
            }

            doneWith.put(new HashIndex(temp.getX(),temp.getY()),temp);

            if (path.isEmpty()) this.setUpdating(false);

            if (path.size()>40 && Panel.FPS < 500)
                Panel.FPS = 500;

            if (path.size()>100 && Panel.FPS < 1000)
                Panel.FPS = 1000;

        }


    }


    public boolean isDoneWith(CellNode cellNode) {
        return doneWith.containsKey(new HashIndex(cellNode.getX(),cellNode.getY()));
    }

    public boolean containsSame(CellNode cell) {
        return path.stream()
                .anyMatch(cellNode -> cellNode.getX() == cell.getX() && cellNode.getY() == cell.getY());
    }

    public CellNode getSame(CellNode cell) {
        for (CellNode cellNode : path) {
            if (cellNode.getX()==cell.getX()&&cellNode.getY()==cell.getY())
                return cellNode;
        }
        return null;
    }

    @Data
    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static class HashIndex {
        private final int x;
        private final int y;
    }
}
