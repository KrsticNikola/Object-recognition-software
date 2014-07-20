/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compareImages.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.GrayFilter;

/**
 *
 * @author nik
 */
public class ImageProccessingAndOther {

    public ImageProccessingAndOther() {
    }

    // write a buffered image to a jpeg file.
    public boolean saveJPG(Image img, String filename) {
        BufferedImage bi = imageToBufferedImage(img);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            ImageIO.write(bi, "jpeg", out);
        } catch (java.io.FileNotFoundException io) {

            System.out.println("File Not Found");
            io.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    // read a jpeg file into a buffered image
    public BufferedImage loadJPG(String filename) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(filename);
        } catch (java.io.FileNotFoundException io) {
            System.out.println("File Not Found");
        }

        BufferedImage bi = null;
        try {
            bi = ImageIO.read(in);
        } catch (java.io.IOException io) {
            System.out.println("IOException");
        }
        return bi;
    }

    // buffered images 
    public BufferedImage imageToBufferedImage(Image img) {
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2 = bi.createGraphics();
        g2.drawImage(img, null, null);
        g2.dispose();
        return bi;
    }

    public BufferedImage imageToBufferedImage(Image createDisabledImage, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2 = bi.createGraphics();
        g2.drawImage(createDisabledImage, 0, 0, null);
        g2.dispose();
        return bi;
    }

    // neki vid prosecne osvetljenosti odredjenog kvadrata slike
    public int getAverageBrightness(BufferedImage img, int factorD) {

        Raster r = img.getData();
//        DataBuffer r = img.getRaster().getDataBuffer().get;
//        img.getRaster().getDataBuffer().getData().
        int total = 0;
        for (int y = 0; y < r.getHeight(); y++) {
            for (int x = 0; x < r.getWidth(); x++) {

                // r.getSample uzima uzorke (piksele predtavljene u obliku int)
                total += r.getSample(r.getMinX() + x, r.getMinY() + y, 0);
            }

        }
        return (int) (total / ((r.getWidth() / factorD) * (r.getHeight() / factorD)));
    }

    public int getAverageBrightness(Raster imgRaster, int factorD) {

        Raster r = imgRaster;
//        DataBuffer r = img.getRaster().getDataBuffer().get;
//        img.getRaster().getDataBuffer().getData().
        int total = 0;
        for (int y = 0; y < r.getHeight(); y++) {
            for (int x = 0; x < r.getWidth(); x++) {

                // r.getSample uzima uzorke (piksele predtavljene u obliku int)
                total += r.getSample(r.getMinX() + x, r.getMinY() + y, 0);
            }

        }

        int resoult = 0;
        try {
            resoult = (int) (total / ((r.getWidth() / factorD) * (r.getHeight() / factorD)));
        } catch (Exception e) {
            new Exception("Povecaj vrednost factorD");
        }
        return resoult;

    }

    public int getAverageBrightness(int[][] imgDataArray, int factorD) {
        //x osa
        int sizeX = imgDataArray.length;

        //y osa
        int sizeY = imgDataArray[0].length;
        int total = 0;
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {

                total += imgDataArray[x][y];
            }
        }

        return (int) (total / ((sizeX / factorD) * (sizeY / factorD)));
    }

    public int getAverageBrightnessNew(int[][] imgDataArray, int factorD) {
        int resoult = 0;
        int rows = imgDataArray.length;
        int columns = imgDataArray[0].length;
        int total = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                total += imgDataArray[i][j];
            }

        }

        double tmp = (rows) * (columns);
        resoult = (int) (total / (tmp));

        return resoult;
    }
//konvertuje buufered image sliku u int[][] matricu

    public int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;
//        System.out.println("Width unutra: " + width);
//        System.out.println("Height unutra: " + height);
        int[][] result = new int[height][width];
//        int[][] result = new int[width][height];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {

                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
//                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }
//        System.out.println(result.length);
//        System.out.println(result[0].length);
        return result;
    }

    public int[][] convertTo2DUsingGetRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[row][col] = image.getRGB(col, row);
            }
        }

        return result;
    }

    public int[] convertTo1DUsingGetRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] result = new int[height * width];
        int tmp = 0;
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                result[tmp] = image.getRGB(row, col);
                tmp++;
            }
        }

        return result;
    }

    public BufferedImage bufferedImageFromMatrix(int PixelArray[][]) {
        BufferedImage bufferImage2 = null;
        try {

            int height = PixelArray.length;
            int width = PixelArray[0].length;

            ///////create Image from this PixelArray
            bufferImage2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            Graphics g = bufferImage2.getGraphics();
//            g.drawImage(g, 0, 0, null);
//            g.dispose();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
//                    int Pixel = PixelArray[y][x] << 16 | PixelArray[y][x] << 8 | PixelArray[y][x];
                    bufferImage2.setRGB(x, y, PixelArray[y][x]);
                }
            }
            File outputfile = new File("resources//check.jpg");
            ImageIO.write(bufferImage2, "jpg", outputfile);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return bufferImage2;
    }

    public BufferedImage bufferedImageToGray(BufferedImage img) {

//            BufferedImage img = ImageIO.read(new File("http://i.stack.imgur.com/yhCnH.png") );
//            BufferedImage gray = new BufferedImage(img.getWidth(), img.getHeight(),
//                    BufferedImage.TYPE_BYTE_GRAY);
//            Graphics2D g = gray.createGraphics();
//            g.drawImage(img, 0, 0, null);
//        BufferedImage grayImage = new BufferedImage(
//                img.getWidth(),
//                img.getHeight(),
//                BufferedImage.TYPE_BYTE_GRAY);
//        Graphics2D g = grayImage.createGraphics();
//        g.drawImage(img, 0, 0, null);
//        g.dispose();
//        return grayImage;
        ImageFilter filter = new GrayFilter(true, 50);
        ImageProducer producer = new FilteredImageSource(img.getSource(), filter);
        Image mage = Toolkit.getDefaultToolkit().createImage(producer);
        return imageToBufferedImage(mage);

    }

    public BufferedImage convertToGrayScale(BufferedImage image) {
        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = result.createGraphics();
//        Graphics g = result.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return result;
    }

    public BufferedImage convertToGrayScale(BufferedImage image, int width, int height) {
//        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = result.createGraphics();
//        Graphics g = result.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return result;
    }

    public void makeGray(BufferedImage img) {
        for (int x = 0; x < img.getWidth(); ++x) {
            for (int y = 0; y < img.getHeight(); ++y) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                int grayLevel = (r + g + b) / 3;
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                img.setRGB(x, y, gray);
            }
        }
    }

    private int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

    }

    public BufferedImage desaturate(BufferedImage source) {
        ColorConvertOp colorConvert
                = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        colorConvert.filter(source, source);

        return source;
    }

    public BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double) width / imageWidth;
        double scaleY = (double) height / imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(
                image,
                new BufferedImage(width, height, image.getType()));
    }

}
