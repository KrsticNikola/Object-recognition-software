/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectedComponentLabeling.labeling;

import connectedComponentLabeling.util.ImageChunk;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 *
 * @author nick
 */
public class Labeling {

    private Dimension dimension;    //dimenzije- broj pravougaonih delova x,y
    private int SENSITIVITY_NUMBER_OF_PIXELS = 100; //osetljivost prilikom provere pravougaonih delova
    private final int MAX_NUMBER_LABELS_0_n = 200;  //maksimalan broj labela
    private BufferedImage[] parts;  //delovi za obradu
    private ImageChunk[] chunks0_n; //rezultat labelovanja
    private int maxLabel;

    public Labeling() {
    }

    public Labeling(Dimension dimension, BufferedImage[] parts) {
        this.dimension = dimension;
        this.parts = parts;
    }

    public void startLabeling() {
        BinaryComponentLabeling label0_1 = new BinaryComponentLabeling(parts, dimension);         //osetljivost kvadrata po broju piksela, 
        label0_1.setSENSITIVITY_NUMBER_OF_PIXELS(parts[0].getHeight()*parts[0].getWidth()/4); //4 dakle ako je preko 25%ispunjenosti registrovace ga

//        label0_1.setSENSITIVITY_NUMBER_OF_PIXELS(SENSITIVITY_NUMBER_OF_PIXELS); //osetljivost kvadrata po broju piksela
        label0_1.markParts();

        ImageChunk[] data0_1 = label0_1.getResoults();  //resultat 0-1
        BinaryComponentGrouping label0_n = new BinaryComponentGrouping(data0_1, dimension);           label0_n.setMAX_LABELS(MAX_NUMBER_LABELS_0_n); //maksimalan broj labela u procesu labelovanja, vise objekata potrebno vise labela
        label0_n.compactLabeling();

        ImageChunk[] data0_n = label0_n.getResoults();  //rezultat 0-n
        setMaksLabelNumber(label0_n.getMaxLabel()); //najveca labela
        chunks0_n = data0_n;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setParts(BufferedImage[] parts) {
        this.parts = parts;
    }

    public ImageChunk[] getChunks0_n() {
        return chunks0_n;
    }

    public int getSENSITIVITY_NUMBER_OF_PIXELS() {
        return SENSITIVITY_NUMBER_OF_PIXELS;
    }

    public void setSENSITIVITY_NUMBER_OF_PIXELS(int SENSITIVITY_NUMBER_OF_PIXELS) {
        this.SENSITIVITY_NUMBER_OF_PIXELS = SENSITIVITY_NUMBER_OF_PIXELS;
    }

    private void setMaksLabelNumber(int maxLabel) {
        this.maxLabel = maxLabel;
    }

    public int getMaxLabel() {
        return maxLabel;
    }

}
