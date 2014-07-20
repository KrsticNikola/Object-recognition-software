/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectedComponentLabeling.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author nick
 */
public class ImageChunk extends Rectangle {

    private boolean marked = false;
    private int labelNumber;

    public ImageChunk() {
    }

    public ImageChunk(Dimension dimension) {
        super(dimension);
    }

    public ImageChunk(Dimension dimension, Point pointer) {
        super(pointer, dimension);
    }

    public ImageChunk(boolean marked, Dimension dimension, Point pointer) {
        super(pointer, dimension);
        this.marked = marked;
    }

    public ImageChunk(boolean marked, Dimension dimension, Point pointer, int labelNumber) {
        super(pointer, dimension);
        this.marked = marked;
        this.labelNumber = labelNumber;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public int getLabelNumber() {
        return labelNumber;
    }

    public void setLabelNumber(int labelNumber) {
        this.labelNumber = labelNumber;
    }

    public void setPointer(Point p) {
        super.setLocation(p);
    }

    public void setDimension(Dimension d) {
        super.setSize(d);
    }

}
