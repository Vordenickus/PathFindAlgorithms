package org.daniil.searchalgorithms.algorithms;

import lombok.*;
import org.daniil.searchalgorithms.algorithms.tree.CellNode;
import org.daniil.searchalgorithms.model.Panel;
import org.daniil.searchalgorithms.model.area.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Djikstra extends Algorithm{

    public static final Algorithms ALGORITHM_NAME = Algorithms.DJISTRA;

    private final Cell start;
    private final Cell target;

    private final ArrayList<CellNode> path = new ArrayList<>();
    private final HashMap<HashIndex, CellNode> doneWith = new HashMap<>();
    // Индекс по сути не нужен, просто заглушка от других алгоритмов
    private int index = 0;
    private final VirtualCell[][] virtualCells;



    public Djikstra(Cell[][] area, Cell start, Cell target) {
        super(area);
        this.start=start;
        this.target=target;
        this.virtualCells = this.getVirtualCells();
    }


    @Override
    public void tick() {

        if (updating) {

            if (path.isEmpty()) path.add(new CellNode(start.getX(), start.getY(), 0, null));

            CellNode temp = path.get(getShortest());
            //System.out.println(temp.getValue());

            this.getArea()[temp.getY()][temp.getX()].setCurr(true);
            this.getArea()[temp.getY()][temp.getX()].setPassThrough(true);

            virtualCells[temp.getY()][temp.getX()].setVisited(true);

            if (index==0) {
                this.getArea()[start.getY()][start.getX()].setPassThrough(false);
                this.getArea()[start.getY()][start.getX()].setCurr(false);
                index++;
            }

            if (temp.getX() == target.getX() && temp.getY() == target.getY()) {
                this.getArea()[target.getY()][target.getX()].setPassThrough(false);
                this.getArea()[target.getY()][target.getX()].setCurr(false);
                updating=false;
                Panel.FPS=100;
                found = temp;
            }

            if (updating) {

                List<CellNode> children = temp.getChildrenDjistra(this.getArea());

                for (CellNode child : children) {

                    if (isDoneWith(child)) continue;

                    if (!containsSame(child)) {
                        path.add(child);
                        this.getArea()[child.getY()][child.getX()].setPassThrough(true);
                    } else {

                        CellNode sameNode = this.getSame(child);

                        if (child.getValue()<sameNode.getValue()) {
                            int desiredIndex = path.indexOf(sameNode);
                            path.remove(desiredIndex);
                            path.add(desiredIndex, child);
                        }
                    }
                }
            }

            path.remove(temp);
            path.trimToSize();
            if (path.size() > 50) {
                if (Panel.FPS < 500) {
                    Panel.FPS = 500;
                }
            }
            if (path.size() > 100) {
                if (Panel.FPS < 1000)
                    Panel.FPS = 1000;
            }
            doneWith.put(new HashIndex(temp.getX(),temp.getY()), temp);

            if (path.isEmpty()) {
                Panel.FPS = 100;
                updating=false;
            }

        }

    }


    public boolean isDoneWith(CellNode cellNode) {
        return doneWith.containsKey(new HashIndex(cellNode.getX(), cellNode.getY()));
    }


    // Возвращает индекс нода с самым коротким вэлью
    public int getShortest() {

        int value = Integer.MAX_VALUE;

        int target = 0;

        for (int i = 0, limit = path.size(); i<limit; i++) {
            if (path.get(i).getValue() < value) {
                value = path.get(i).getValue();
                target = i;
            }
        }

        return target;
    }

    public boolean containsSame(CellNode cell) {
        return path.stream().anyMatch(cellNode -> cellNode.getX() == cell.getX() && cellNode.getY() == cell.getY());
    }

    public CellNode getSame(CellNode cell) {
        for (CellNode cellNode : path) {

            if (cellNode.getX()==cell.getX() && cellNode.getY()==cell.getY())
                return cellNode;

        }
        return null;
    }

    @Data @AllArgsConstructor @EqualsAndHashCode
    private static class HashIndex {
        private final int x;
        private final int y;
    }

}
