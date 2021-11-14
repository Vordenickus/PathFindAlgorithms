package org.daniil.searchalgorithms.algorithms;

import lombok.Getter;
import lombok.Setter;
import org.daniil.searchalgorithms.algorithms.tree.CellNode;
import org.daniil.searchalgorithms.model.area.Cell;

public abstract class Algorithm {

    @Getter
    protected final Cell[][] area;

    @Getter
    @Setter
    protected boolean updating = false;

    @Getter
    protected CellNode found;


    public Algorithm(Cell[][] area) {
        this.area = area;
    }


    protected VirtualCell[][] getVirtualCells() {

        VirtualCell[][] cells = new VirtualCell[this.getArea().length][this.getArea().length];
        for (int i = 0, limit = this.getArea().length; i < limit; i++) {
            for (int x = 0; x < limit; x++) {
                cells[i][x] = new VirtualCell(x, i, false);
            }
        }
        return cells;
    }


    public abstract void tick();
}
