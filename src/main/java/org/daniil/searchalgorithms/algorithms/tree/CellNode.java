package org.daniil.searchalgorithms.algorithms.tree;

import lombok.Getter;
import lombok.Setter;
import org.daniil.searchalgorithms.algorithms.Algorithm;
import org.daniil.searchalgorithms.algorithms.VirtualCell;
import org.daniil.searchalgorithms.model.area.Cell;
import org.daniil.searchalgorithms.model.area.CellValue;

import java.util.ArrayList;
import java.util.List;

public class CellNode {

    @Getter
    private final int x, y;

    @Getter
    private int value = Integer.MAX_VALUE;

    @Getter
    private int valueAStar = Integer.MAX_VALUE;

    @Getter
    private final CellNode parent;

    //private Algorithm algorithm;

    public CellNode(int x, int y, CellNode parent) {
        this.x=x;
        this.y=y;
        this.parent=parent;
    }

    public CellNode(int x, int y, int value, CellNode parent) {
        this.x=x;
        this.y=y;
        this.parent=parent;
        this.value=value;
    }

    public CellNode(int x, int y, int value, int valueAStar, CellNode parent) {
        this.x=x;
        this.y=y;
        this.value=value;
        this.valueAStar=valueAStar;
        this.parent=parent;
    }


    public List<CellNode> getChildren(Cell[][] area) {

        ArrayList<CellNode> children = new ArrayList<>();

        int maxPos = area.length-1;

        if (x>0) {
            if (area[y][x-1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x-1,y,this));
        }
        if (x<maxPos) {
            if (area[y][x+1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x+1,y,this));
        }
        if (y>0) {
            if (area[y-1][x].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x,y-1,this));
        }
        if (y<maxPos) {
            if (area[y+1][x].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x,y+1,this));
        }
        if (x>0&&y>0) {
            if (area[y-1][x-1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x-1,y-1,this));
        }
        if (x>0&&y<maxPos) {
            if (area[y+1][x-1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x-1,y+1,this));
        }
        // #TODO: ПРОЕБАНЫ КООРДИНАТЫ
        if (x<maxPos&&y>0) {
            if (area[y-1][x+1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x+1,y-1,this));
        }
        // #TODO: ПРОЕБАНЫ КООРДИНАТЫ
        if (x<maxPos&&y<maxPos){
            if (area[y+1][x+1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x+1,y+1,this));
        }

        //children.forEach(o-> System.out.println(o.getX()+"/"+o.getY()));

        return children;
    }

    public List<CellNode> getChildrenDjistra(Cell[][] area) {

        ArrayList<CellNode> children = new ArrayList<>();

        int maxPos = area.length-1;

        if (x>0) {
            if (area[y][x-1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x-1,y, getDistance(x-1,y,area), this));
        }
        if (x<maxPos) {
            if (area[y][x+1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x+1,y, getDistance(x+1,y,area),this));
        }
        if (y>0) {
            if (area[y-1][x].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x,y-1, getDistance(x,y-1,area),this));
        }
        if (y<maxPos) {
            if (area[y+1][x].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x,y+1, getDistance(x,y+1,area),this));
        }
        if (x>0&&y>0) {
            if (area[y-1][x-1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x-1,y-1, getDistance(x-1,y-1,area),this));
        }
        if (x>0&&y<maxPos) {
            if (area[y+1][x-1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x-1,y+1, getDistance(x-1,y+1,area),this));
        }
        if (x<maxPos&&y>0) {
            if (area[y-1][x+1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x+1,y-1, getDistance(x+1,y-1,area),this));
        }
        if (x<maxPos&&y<maxPos){
            if (area[y+1][x+1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x+1,y+1, getDistance(x+1,y+1,area),this));
        }

        //children.forEach(o-> System.out.println(o.getX()+"/"+o.getY()));

        return children;
    }

    public List<CellNode> getChildrenAStart(Cell[][] area, int targetX, int targetY) {

        ArrayList<CellNode> children = new ArrayList<>();

        int maxPos = area.length-1;

        if (x>0) {
            if (area[y][x-1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x-1,y,0, computeValueAStar(area,targetX,targetY,x-1,y), this));
        }
        if (x<maxPos) {
            if (area[y][x+1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x+1,y,0, computeValueAStar(area,targetX,targetY,x+1,y),this));
        }
        if (y>0) {
            if (area[y-1][x].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x,y-1,0, computeValueAStar(area,targetX,targetY,x,y-1),this));
        }
        if (y<maxPos) {
            if (area[y+1][x].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x,y+1,0, computeValueAStar(area,targetX,targetY,x,y+1),this));
        }
        if (x>0&&y>0) {
            if (area[y-1][x-1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x-1,y-1,0, computeValueAStar(area,targetX,targetY,x-1,y-1),this));
        }
        if (x>0&&y<maxPos) {
            if (area[y+1][x-1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x-1,y+1,0, computeValueAStar(area,targetX,targetY,x-1,y+1),this));
        }
        if (x<maxPos&&y>0) {
            if (area[y-1][x+1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x+1,y-1,0, computeValueAStar(area,targetX,targetY,x+1,y-1),this));
        }
        if (x<maxPos&&y<maxPos){
            if (area[y+1][x+1].getCellValue() != CellValue.WALL)
                children.add(new CellNode(x+1,y+1,0, computeValueAStar(area,targetX,targetY,x+1,y+1),this));
        }

        //children.forEach(o-> System.out.println(o.getX()+"/"+o.getY()));

        return children;
    }

    private int getHeiuristic(int targetX, int targetY, int x, int y) {

        return (int) (Math.sqrt((targetX-x)*(targetX-x) + (targetY-y)*(targetY-y))) * 100;
    }

    public void setValueAStar(int valueAStar) {
        this.valueAStar = valueAStar;
    }

    public int computeValueAStar(Cell[][] area, int targetX, int targetY, int x, int y) {
        return getDistance(x,y,area) + getHeiuristic(targetX,targetY,x,y);
    }




    private int getDistance(int x, int y, Cell[][] area) {

        Cell temp = area[y][x];

        int addDistance=0;

        switch (temp.getCellValue()) {
            case GRASS:
                addDistance = 2;
                break;
            case STONE:
                addDistance = 1;
                break;
            case SAND:
                addDistance = 10;
                break;
            case WATER:
                addDistance = 20;
                break;
            default:
                break;
        }
        return (this.getValue() + addDistance);
    }


}
