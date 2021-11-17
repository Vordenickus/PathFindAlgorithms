package org.daniil.searchalgorithms.model.ui;

import lombok.Getter;
import lombok.Setter;
import org.daniil.searchalgorithms.algorithms.Algorithms;
import org.daniil.searchalgorithms.model.area.CellValue;
import org.daniil.searchalgorithms.model.ui.button.OnClick;

import java.awt.*;

public class ButtonObj extends UiObject {

    @Getter
    private CellValue cellValue;

    @Getter
    private Algorithms algorithm;

    @Getter @Setter
    private Color color;

    @Getter @Setter
    private String text;

    private final Font font;

    @Getter @Setter
    private boolean selected = false;

    @Setter
    private OnClick onClick;


    public ButtonObj(int x, int y, int width, int height, Color color, String text, CellValue cellValue, UI parent) {
        super(x, y, width, height, parent);
        this.color=color;
        this.text=text;
        this.cellValue = cellValue;
        font = new Font("TimesRoman",Font.PLAIN, 20);
    }

    public ButtonObj(int x, int y, int width, int height, Color color, String text, Algorithms algorithm, UI parent) {
        super(x, y, width, height, parent);
        this.color=color;
        this.text=text;
        this.algorithm=algorithm;
        font = new Font("TimesRoman",Font.PLAIN, 20);
    }

    public ButtonObj(int x, int y, int width, int height, Color color, String text, UI parent) {
        super(x, y, width, height, parent);
        this.color=color;
        this.text=text;
        font = new Font("TimesRoman",Font.PLAIN, 20);
    }



    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(this.getX(),this.getY(),this.getWidth(),this.getHeight());
        g.setColor(Color.black);
        g.setFont(font);
        FontMetrics fontMetrics = g.getFontMetrics(font);
        int fWidth = fontMetrics.stringWidth(text);
        int fHeight = fontMetrics.getHeight();
        g.drawString(text, this.getX()+(this.getWidth()-fWidth)/2, this.getY() + fHeight);

        if (selected) {
            g.setColor(Color.ORANGE);
            g.drawRect(this.getX(),this.getY(),this.getWidth(),this.getHeight());
        }

    }

    public void click(OnClick onClick) {
        onClick.click();
    }

    public void click() {
        onClick.click();
    }
}
