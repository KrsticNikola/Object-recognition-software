/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectedComponentLabeling.labeling;

import connectedComponentLabeling.util.ImageChunk;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author nick
 */
public class BinaryComponentLabeling {

    private Dimension dimensionS;   //dimenzije, broj pravougaonika
    private BufferedImage[] parts;  //delovi slike

    private int SENSITIVITY_NUMBER_OF_PIXELS = 250;   //osetljivost svakog od pravougaonika
    private ImageChunk[] resoults;  //delovi slike konvertovani u prosiren oblik

    public BinaryComponentLabeling() {
    }

    public BinaryComponentLabeling(BufferedImage[] parts, Dimension d) {
        this.dimensionS = d;
        this.parts = parts;
    }

    public void markParts() {
        ImageChunk[] resoults = new ImageChunk[parts.length];
        int tmpChunkNumber = 0;
        BufferedImage[] parts = this.parts;

        int height = parts[0].getHeight();
        int width = parts[0].getWidth();
        Dimension d = new Dimension(width, height);

        for (BufferedImage imgPart : parts) {

            int counter = 0;
            resoults[tmpChunkNumber] = new ImageChunk(d);    //inicijalizacija

            int[] pixel;
            out:
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (counter > SENSITIVITY_NUMBER_OF_PIXELS) {
//                        resoults[tmp] = 1;
                        resoults[tmpChunkNumber].setMarked(true);
                        break out;
                    }
                    pixel = imgPart.getRaster().getPixel(x, y, new int[3]);
                    if (pixel[0] != 255 || pixel[1] != 255 || pixel[2] != 255) {
                        counter++;
                    }
                }

            }
            tmpChunkNumber++;
        }
        int chunkCounter = 0;
        //postavlja pointere
        for (int i = 0; i < dimensionS.height; i++) {
            for (int j = 0; j < dimensionS.width; j++) {
                Point p = new Point(d.width * j, (d.height * i));
//                System.out.println(p);
                resoults[chunkCounter].setPointer(p);
                chunkCounter++;
            }
        }

        this.resoults = resoults;
    }

    public ImageChunk[] getResoults() {
        return resoults;
    }

    public void setResoults(ImageChunk[] resoults) {
        this.resoults = resoults;
    }

    public int getSENSITIVITY_NUMBER_OF_PIXELS() {
        return SENSITIVITY_NUMBER_OF_PIXELS;
    }

    public void setSENSITIVITY_NUMBER_OF_PIXELS(int SENSITIVITY_NUMBER_OF_PIXELS) {
        this.SENSITIVITY_NUMBER_OF_PIXELS = SENSITIVITY_NUMBER_OF_PIXELS;
    }
}
