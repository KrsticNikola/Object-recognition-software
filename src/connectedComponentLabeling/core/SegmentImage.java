/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectedComponentLabeling.core;

import connectedComponentLabeling.splitImage.SplitImage;
import connectedComponentLabeling.processingImageChunk.ProcessingImageChunk;
import connectedComponentLabeling.labeling.Labeling;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author nick
 */
public class SegmentImage {

    private BufferedImage buffImage;
    private Dimension dimension;
    private Rectangle[] segments;

    public SegmentImage() {
    }

    public SegmentImage(BufferedImage buffImage, Dimension dimension) {
        this.buffImage = buffImage;
        this.dimension = dimension;
    }

    public void startSegmenting() {

        SplitImage splitClass = new SplitImage();   //klasa za split slike

        splitClass.setNumberOfRowsAndColumns(dimension);    //postavljamo dimenzije za izdeljivanje
        splitClass.setImg(buffImage); //postavljamo sliku
        splitClass.split(); //vrsi se izdeljivanje

        BufferedImage[] parts = splitClass.getPhotoParts(); //uzima se podeljeno
        Labeling labeling = new Labeling(dimension, parts);
        labeling.startLabeling();
        ProcessingImageChunk test = new ProcessingImageChunk(labeling.getChunks0_n());
        segments = test.unionChunks(labeling.getMaxLabel());    //spaja zajednicke chunks

    }

    public Rectangle[] getSegments() {
        return segments;
    }

}
