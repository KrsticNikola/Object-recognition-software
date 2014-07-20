/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectedComponentLabeling.labeling;

import connectedComponentLabeling.util.ImageChunk;
import java.awt.Dimension;

/**
 *
 * @author nick
 */
public class BinaryComponentGrouping {

    private int MAX_LABELS = 50;
    int next_label = 1;
    private ImageChunk[] chunks;
    private int[] data;
    private Dimension d;

    public BinaryComponentGrouping() {
    }

    public BinaryComponentGrouping(ImageChunk[] data, Dimension d) {

        this.chunks = data;
        this.d = d;
    }

    /**
     * Label the connect components If label 0 is background, then label 0 is
     * untouched; If not, label 0 may be reassigned [Requires] 0 is treated as
     * background
     *
     * @param image data
     * @param d dimension of the data
     * @param zeroAsBg label 0 is treated as background, so be ignored
     */
    public void compactLabeling() {
        //label first
        setDataIntArray(chunks);
        int[] label = labeling(data, d);    // label -ne sredjene labele
        int[] stat = new int[next_label + 1];   //stat -sredjene labele
        for (int i = 0; i < data.length; i++) {
            if (label[i] > next_label) {
                System.err.println("bigger label than next_label found!");
            }
            stat[label[i]]++;
        }

        stat[0] = 0;              // label 0 will be mapped to 0
        // whether 0 is background or not
        int j = 1;
        for (int i = 1; i < stat.length; i++) {
            if (stat[i] != 0) {

                stat[i] = j++;
            }
        }

//        System.out.println("From " + next_label + " to " + (j - 1) + " regions");
        next_label = j - 1;
        for (int i = 0; i < data.length; i++) {
            label[i] = stat[label[i]];
        }
        setLabelsNmbrChunksFromIntArray(label);
    }

    /**
     * Label the connect components If label 0 is background, then label 0 is
     * untouched; If not, label 0 may be reassigned [Requires] 0 is treated as
     * background
     *
     * @param image data
     * @param d dimension of the data
     * @param zeroAsBg label 0 is treated as background, so be ignored
     */
    private int[] labeling(int[] data, Dimension d) {
        boolean zeroAsBg = true;
        int w = d.width, h = d.height;
        int[] rst = new int[w * h];
        int[] parent = new int[MAX_LABELS];
        int[] labels = new int[MAX_LABELS];
        // region label starts from 1;
        // this is required as union-find data structure
        int next_region = 1;
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                if (data[y * w + x] == 0 && zeroAsBg) {
                    continue;
                }
                int k = 0;
                boolean connected = false;
                // if connected to the left
                if (x > 0 && data[y * w + x - 1] == data[y * w + x]) {
                    k = rst[y * w + x - 1];
                    connected = true;
                }
                // if connected to the up
                if (y > 0 && data[(y - 1) * w + x] == data[y * w + x]
                        && (connected = false || data[(y - 1) * w + x] < k)) {
                    k = rst[(y - 1) * w + x];
                    connected = true;
                }
                if (!connected) {
                    k = next_region;
                    next_region++;
                }

                if (k >= MAX_LABELS) {
                    System.out.println("greska maks labels!");
                    continue;
//                    throw new RuntimeException("maximum number of " + MAX_LABELS + " labels reached. "
//                            + "increase MAX_LABELS and recompile.");
//                    System.exit(1);
                }
                rst[y * w + x] = k;
                // if connected, but with different label, then do union
                if (x > 0 && data[y * w + x - 1] == data[y * w + x] && rst[y * w + x - 1] != k) {
                    uf_union(k, rst[y * w + x - 1], parent);
                }
                if (y > 0 && data[(y - 1) * w + x] == data[y * w + x] && rst[(y - 1) * w + x] != k) {
                    uf_union(k, rst[(y - 1) * w + x], parent);
                }
            }
        }

        // Begin the second pass.  Assign the new labels
        // if 0 is reserved for background, then the first available label is 1
        next_label = 1;
        for (int i = 0; i < w * h; i++) {
            if (data[i] != 0 || !zeroAsBg) {
                rst[i] = uf_find(rst[i], parent, labels);
                // The labels are from 1, if label 0 should be considered, then
                // all the label should minus 1
                if (!zeroAsBg) {
                    rst[i]--;
                }
            }
        }
        next_label--;   // next_label records the max label
        if (!zeroAsBg) {
            next_label--;
        }

//        System.out.println(next_label + " regions");

        return rst;
    }

    public int getMaxLabel() {
        return next_label;
    }

    private void uf_union(int x, int y, int[] parent) {
        while (parent[x] > 0) {
            x = parent[x];
        }
        while (parent[y] > 0) {
            y = parent[y];
        }
        if (x != y) {
            if (x < y) {
                parent[x] = y;
            } else {
                parent[y] = x;
            }
        }

    }

    private int uf_find(int x, int[] parent, int[] label) {
        while (parent[x] > 0) {
            x = parent[x];
        }
        if (label[x] == 0) {
            label[x] = next_label++;
        }
        return label[x];
    }

    /**
     * Metoda koja puni chunks vrednosti labele iz niza int
     *
     * @param label niz int
     */
    private void setLabelsNmbrChunksFromIntArray(int[] label) {
        int tmp = 0;
        for (int i : label) {
            chunks[tmp].setLabelNumber(i);
            tmp++;
        }
    }

    /**
     * Metoda koja prebacuje chunks u int array
     *
     * @param chunks podaci chunks za prebacivanje u int array
     */
    private void setDataIntArray(ImageChunk[] chunks) {
        data = new int[chunks.length];
        int tmp = 0;
        for (ImageChunk imageChunk : chunks) {
            if (imageChunk.isMarked()) {
                data[tmp] = 1;
            } else {
                data[tmp] = 0;
            }
            tmp++;
        }
    }

    public ImageChunk[] getResoults() {
        return chunks;
    }

    public int getMAX_LABELS() {
        return MAX_LABELS;
    }

    public void setMAX_LABELS(int MAX_LABELS) {
        this.MAX_LABELS = MAX_LABELS;
    }

}
