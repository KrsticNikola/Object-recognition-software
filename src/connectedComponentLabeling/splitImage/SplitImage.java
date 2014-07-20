/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectedComponentLabeling.splitImage;

/**
 *
 * @author nik
 */
import java.awt.*;
import java.awt.image.BufferedImage;

public class SplitImage {

    BufferedImage img;
    BufferedImage parts[];
    int rows;
    int cols;

    public SplitImage() {
    }

    public SplitImage(BufferedImage img) {
        this.img = img;
    }

    public void setImg(BufferedImage bimage) {
        img = bimage;
    }

    public void split() {
        BufferedImage image = img;
        int chunks = rows * cols;
        //odredjivanje dimenzija
        int chunkWidth = image.getWidth() / cols;
        int chunkHeight = image.getHeight() / rows;
        int count = 0;
        //niz koji ce cuvati delove slike
        BufferedImage imgs[] = new BufferedImage[chunks];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                //Niz slika array sa image chunks  
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

                // crta image chunk  
                Graphics2D gr = imgs[count++].createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                gr.dispose();
            }
            parts = imgs;
        }
//        System.out.println("Splitting done");

    }

    public BufferedImage[] getPhotoParts() {
        return parts;
    }

    public void setNumberOfRowsAndColumns(Dimension d) {
        this.rows = (int) d.getHeight();
        this.cols = (int) d.getWidth();
    }

}
