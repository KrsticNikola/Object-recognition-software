package compareImages.core;

import compareImages.util.InfoAttributes;
import compareImages.util.ImageProccessingAndOther;
import java.awt.*;
import java.awt.image.*;
import java.io.IOException;

import compareImages.util.CompareParams;

public class ImageCompare {

    private BufferedImage shapeImg; //bela slika sa konturama
    private ImageProccessingAndOther imgProces;
    private BufferedImage img1;
    private BufferedImage img2;

    private CompareParams params;


    public ImageCompare() {
        this.imgProces = new ImageProccessingAndOther();
        this.params = new CompareParams();

    }
    //
    // constructor 1. use filenames
    public ImageCompare(String file1, String file2, CompareParams params) {

        this.imgProces = new ImageProccessingAndOther();
        this.params = params;
        this.img1 = imgProces.loadJPG(file1);
        this.img2 = imgProces.loadJPG(file2);

    }

    // constructor 3. use buffered images. all roads lead to the same place. this place.
    public ImageCompare(BufferedImage img1, BufferedImage img2, CompareParams params) {
        this.imgProces = new ImageProccessingAndOther();
        this.params = params;
        this.img1 = img1;
        this.img2 = img2;

    }

    // compare the two images in this object.
    public void compare() throws InterruptedException, IOException {
        if (params == null) {
            throw new RuntimeException("Parametri su prazni, unesite parametre prvo!");
        }
        if (img1 == null || img2 == null) {
            throw new RuntimeException("Slike za poredjenje su null, postavi ih!");
        }
        refreshGraphics();
        // setup change display image
        // konvertuje u crno belu sliku, jer se vrsi
        //  poredjenje intenziteta boja na slikama
        int[][] array1 = imgProces.convertTo2DWithoutUsingGetRGB(img1);
        int[][] array2 = imgProces.convertTo2DWithoutUsingGetRGB(img2);
        
        //bela slika sa konturama
        setShapeImg(img1.getWidth(), img1.getHeight());
        // gShape za rad na beloj pozadi
        Graphics2D gShape = shapeImg.createGraphics();
        gShape.setColor(Color.BLACK);

        // koliko ima sektora za poredjenje osetljivosti
        int blocksx = (int) (img1.getWidth() / params.getColumnsNmbr());
        int blocksy = (int) (img1.getHeight() / params.getRowsNmbr());

        InfoAttributes ia = new InfoAttributes(array1, array2, blocksx, blocksy,
                this.params.getSensitivity(), this.params.getStabilizer(),
                gShape);

        for (int y = 0; y < params.getRowsNmbr(); y++) {
            for (int x = 0; x < params.getColumnsNmbr(); x++) {
                new ThreadCompare(x, y, ia).run();  //ovde se vrsi poredjenje izmedju oblasti
            }
        }
    }

    //default paramet
    // set the parameters for use during change detection.
    // broj vertikalnih, horizontalnih kvadrata za poredjenje
    // factor a - apsolutno dozvoljeno odstupanje izmedju slika 
    // factor d - kod racunanja osetljivosti srednje osvetljenosti
    public void setParameters(int columns, int rows, int factorA, int factorD) {
        this.params.setColumnsNmbr(columns);
        this.params.setRowsNmbr(rows);
        this.params.setSensitivity(factorA);
        this.params.setStabilizer(factorD);

    }

    private void refreshGraphics() {
//        imgc = null;
        shapeImg = null;
    }

    private void setShapeImg(int width, int height) {
        shapeImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D tmp = shapeImg.createGraphics();
        tmp.setColor(Color.white);
        tmp.fillRect(0, 0, width, height);
    }

//     vraca original sa konturama ovo je za neka druga koriscenja!
    
//    public BufferedImage getOriginalWithShape() {
//        return imgc;
//    }

    public BufferedImage getImgShape() {
        return shapeImg;
    }


    public CompareParams getParams() {
        return params;
    }

    public void setParams(CompareParams params) {
        this.params = params;
    }

    public void setImg1(BufferedImage img1) {
        this.img1 = img1;
    }

    public void setImg2(BufferedImage img2) {
        this.img2 = img2;
    }

}
