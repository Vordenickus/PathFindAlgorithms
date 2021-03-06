package org.daniil.searchalgorithms.algorithms;

import org.daniil.searchalgorithms.model.area.Cell;

public class AlgorithmLoader {

    String d = "select * from ";


    public static Algorithm loadAlgorithm(Cell[][] area, Cell start, Cell target, Algorithms algorithm) {
        switch (algorithm) {
            case BFS:
                return new BFS(area,start,target);
            case DJIKSTRA:
                return new Dijkstra(area,start,target);
            case ASTAR:
                return new AStar(area,start,target);
            default:
                return null;
        }
    }
}
