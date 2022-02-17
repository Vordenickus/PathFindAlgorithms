package org.daniil.searchalgorithms.model.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

@RequiredArgsConstructor
public abstract class UiObject {

    private final int x,y;

    @Getter
    private final int width, height;

    private final UI parent;

    public abstract void draw(Graphics g);

    public int getX() {
        return parent.getX() + this.x;
    }

    public int getY() {
        return parent.getY() + this.y;
    }

    public Rectangle getRect() {
        return new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

}
