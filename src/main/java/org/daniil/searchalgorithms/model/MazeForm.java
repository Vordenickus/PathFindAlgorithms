package org.daniil.searchalgorithms.model;

import org.daniil.searchalgorithms.model.area.Cell;

public class MazeForm {

    private final Cell[][] area;

    public MazeForm(Cell[][] area) {
        this.area=area;
    }


    public void createMaze() {
        initiateWalls();
        correctWalls();
    }

    private void initiateWalls() {
        for (Cell[] cells : area) {
            for (Cell cell : cells) {
                cell.initializeWalls();
            }
        }
    }

    private void correctWalls() {
        for (int i=0; i<area.length;i++) {
            for(int x=0;x<area[i].length;x++) {
                if (x==0) {
                    Cell leftCell = area[i][x];
                    Cell rightCell = area[i][x+1];
                    if (leftCell.isRightWall()) rightCell.setLeftWall(true);
                    if (rightCell.isLeftWall()) leftCell.setRightWall(true);
                } else if (x==area.length-1) {
                    Cell leftCell = area[i][x-1];
                    Cell rightCell = area[i][x];
                    if (leftCell.isRightWall()) rightCell.setLeftWall(true);
                    if (rightCell.isLeftWall()) leftCell.setRightWall(true);
                } else {
                    Cell leftCell = area[i][x-1];
                    Cell centreCell = area[i][x];
                    Cell rightCell = area[i][x+1];

                    if (leftCell.isRightWall()) centreCell.setLeftWall(true);
                    if (centreCell.isLeftWall()) leftCell.setRightWall(true);
                    if (centreCell.isRightWall()) rightCell.setLeftWall(true);
                    if (rightCell.isLeftWall()) centreCell.setRightWall(true);
                }

                if (i==0) {
                    Cell topCell = area[i][x];
                    Cell bottomCell = area[i+1][x];

                    if (topCell.isBottomWall()) bottomCell.setTopWall(true);
                    if (bottomCell.isTopWall()) topCell.setBottomWall(true);

                } else if (i==area.length-1) {

                    Cell bottomCell = area[i][x];
                    Cell topCell = area[i-1][x];

                    if (topCell.isBottomWall()) bottomCell.setTopWall(true);
                    if (bottomCell.isTopWall()) topCell.setBottomWall(true);

                } else {
                    Cell topCell = area[i-1][x];
                    Cell centreCell = area[i][x];
                    Cell bottomCell = area[i+1][x];

                    if (topCell.isBottomWall()) centreCell.setTopWall(true);
                    if (centreCell.isTopWall()) topCell.setBottomWall(true);
                    if (centreCell.isBottomWall()) bottomCell.setTopWall(true);
                    if (bottomCell.isTopWall()) centreCell.setBottomWall(true);

                }

            }
        }
    }



}
