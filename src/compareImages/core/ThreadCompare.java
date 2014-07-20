/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compareImages.core;

import compareImages.util.InfoAttributes;
import compareImages.util.Matrix;
import compareImages.util.ImageProccessingAndOther;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author nik
 */
public class ThreadCompare implements Runnable {

    private ImageProccessingAndOther imgProcess = null;
    private final int blocksx;
    private final int blocksy;
    private int factorA;

    private final int factorD;
    private BufferedImage img1 = null;
    private BufferedImage img2 = null;

//    private Graphics2D gc = null;
    private Graphics2D gShape = null;

    private final int x;
    private final int y;

    private int[][] arrayPic1;
    private int[][] arrayPic2;

    public ThreadCompare(BufferedImage img1, BufferedImage img2, int blocksx, int blocksy, int factorA, int factorD,
            Graphics2D gc, Graphics2D gShape, int x, int y) {
        imgProcess = new ImageProccessingAndOther();
        this.blocksx = blocksx;
        this.blocksy = blocksy;
        this.factorA = factorA;
        this.factorD = factorD;
        this.img1 = img1;
        this.img2 = img2;
//        this.gc = gc;
        this.gShape = gShape;
        this.x = x;
        this.y = y;
    }

    public ThreadCompare(int x, int y, InfoAttributes ia) {
        imgProcess = new ImageProccessingAndOther();
        this.blocksx = ia.getBlocksx();
        this.blocksy = ia.getBlocksy();
        this.factorA = ia.getFactorA();
        this.factorD = ia.getFactorD();

//        this.gc = ia.getGc();
        this.gShape = ia.getgShape();
        this.x = x;
        this.y = y;
        this.arrayPic1 = ia.getArrayPic1();
        this.arrayPic2 = ia.getArrayPic2();
    }

    @Override
    public void run() {
        compare();
    }

    void compare() {

        int[][] array1 = arrayPic1;
        int[][] array2 = arrayPic2;
        Matrix m = new Matrix(array1);
        Matrix m2 = new Matrix(array2);
        m.setSubMatrix(x * blocksx, y * blocksy, blocksy, blocksx);
        m2.setSubMatrix(x * blocksx, y * blocksy, blocksy, blocksx);
        int[][] array11 = m.getData();
        int[][] array22 = m2.getData();
        int b1 = imgProcess.getAverageBrightnessNew(array11, factorD);
        int b2 = imgProcess.getAverageBrightnessNew(array22, factorD);
        int diff = Math.abs(b1 - b2);
        if (diff > factorA) { // the difference in a certain region has passed the threshold value of factorA
            // draw an indicator on the change image to show where change was detected.
            gShape.fillRect(x * blocksx, y * blocksy, blocksx, blocksy);
//            gc.drawRect(x * blocksx, y * blocksy, blocksx, blocksy);
//            gShape.dispose(); // ne sme ovo
//            gc.dispose(); // ne sme ovo
        }
    }

}
