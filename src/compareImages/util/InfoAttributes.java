/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compareImages.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author nik
 */
public class InfoAttributes {

    int blocksx;
    int blocksy;
    int factorA;
    int factorD;
    Graphics2D gc; // nekada postojalo original slika + konture
    Graphics2D gShape;  //konture samo
    BufferedImage img1;
    BufferedImage img2;

    private int[][] arrayPic1;
    private int[][] arrayPic2;

    public InfoAttributes(int[][] array1, int[][] array2, int blocksx, int blocksy, int factorA, int factorD, Graphics2D gShape) {
        this.arrayPic1 = array1;
        this.arrayPic2 = array2;
        this.blocksx = blocksx;
        this.blocksy = blocksy;
        this.factorA = factorA;
        this.factorD = factorD;
        this.gShape = gShape;
    }

    public InfoAttributes(int blocksx, int blocksy, int factorA, int factorD, Graphics2D gc, Graphics2D gShape) {
        this.blocksx = blocksx;
        this.blocksy = blocksy;
        this.factorA = factorA;
        this.factorD = factorD;
        this.gc = gc;
        this.gShape = gShape;
    }

    public InfoAttributes(BufferedImage img1, BufferedImage img2, int blocksx, int blocksy, int factorA, int factorD, Graphics2D gc, Graphics2D gShape) {
        this.img1 = img1;
        this.img2 = img2;
        this.blocksx = blocksx;
        this.blocksy = blocksy;
        this.factorA = factorA;
        this.factorD = factorD;
        this.gc = gc;
        this.gShape = gShape;
    }

    public InfoAttributes(int[][] array1, int[][] array2, int blocksx, int blocksy, int factorA, int factorD, Graphics2D gc, Graphics2D gShape) {
        this.arrayPic1 = array1;
        this.arrayPic2 = array2;
        this.blocksx = blocksx;
        this.blocksy = blocksy;
        this.factorA = factorA;
        this.factorD = factorD;
        this.gc = gc;
        this.gShape = gShape;
    }

    public BufferedImage getImg1() {
        return img1;
    }

    public BufferedImage getImg2() {
        return img2;
    }

    public int getBlocksx() {
        return blocksx;
    }

    public int getBlocksy() {
        return blocksy;
    }

    public int getFactorA() {
        return factorA;
    }

    public int getFactorD() {
        return factorD;
    }

    public Graphics2D getGc() {
        return gc;
    }

    public Graphics2D getgShape() {
        return gShape;
    }

    public void setGc(Graphics2D gc) {
        this.gc = gc;
    }

    public void setgShape(Graphics2D gShape) {
        this.gShape = gShape;
    }

    public int[][] getArrayPic1() {
        return arrayPic1;
    }

    public int[][] getArrayPic2() {
        return arrayPic2;
    }

}
