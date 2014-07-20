/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectedComponentLabeling.processingImageChunk;

import connectedComponentLabeling.util.ImageChunk;
import java.awt.Rectangle;

/**
 *
 * @author nick
 */
public class ProcessingImageChunk {

    ImageChunk[] chunks;

    public ProcessingImageChunk(ImageChunk[] chunks) {
        this.chunks = chunks;
    }


    public Rectangle[] unionChunks(int maksLabel) {
        Rectangle[] rectangles = new Rectangle[maksLabel];

        int tempRecNumber = 0;  //pomocna za numeraciju polja

        for (int r = 1; r <= maksLabel; r++) {   //prolazi kroz labele
            int x1 = -1;
            int y1 = -1;
            int x2 = -1;
            int y2 = -1;
            boolean first = false;  //prvi chunk iz neke labele
            for (ImageChunk chunk : chunks) {
                if (chunk.getLabelNumber() == r && first == false) {
                    x1 = (int) chunk.getMinX();
                    y1 = (int) chunk.getMinY();
                    x2 = (int) chunk.getMaxX();
                    y2 = (int) chunk.getMaxY();

                    first = true;
                    continue;
                }
                if (chunk.getLabelNumber() == r) {
                    x1 = (int) Math.min(x1, chunk.getMinX());
                    y1 = (int) Math.min(y1, chunk.getMinY());
                    x2 = (int) Math.max(x2, chunk.getMaxX());
                    y2 = (int) Math.max(y2, chunk.getMaxY());
                }
            }
            rectangles[tempRecNumber] = new Rectangle(x1, y1, x2 - x1, y2 - y1);
            tempRecNumber++;
        }

        return rectangles;
    }
}
