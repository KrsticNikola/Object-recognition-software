/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import connectedComponentLabeling.SegmentImage;
import compareImages.core.ImageCompare;
import java.awt.image.BufferedImage;
import java.io.IOException;
import compareImages.util.CompareParams;
import compareImages.util.ImageProccessingAndOther;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.util.HashMap;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.imgrec.ImageRecognitionPlugin;
import sharedThreadResources.DataShare;
import videoEncoding.CameraProcessingThread;
import videoEncoding.IDecoder;
import videoEncoding.VideoProcessingThread;

/**
 *
 * @author nik
 */
public class Controller {

    private static Controller controller;

    private ImageCompare ic;    //klasa koja koja vrsi poredjenje
    private CompareParams params;   //parametri prilikom poredjenja
    private BufferedImage imgShape;// samo konture
    private BufferedImage imgShapeRectangle;    //kontura sa pravougaonicima
    private BufferedImage imgNormalRectangle;    //normal sa pravougaonicima
    private ImageProccessingAndOther imageUtil;
    private int brojac;

//    private CameraProcessingThread camMediaPlayer;
//    private VideoProcessingThread videoMediaPlayer;
    private IDecoder videoDecoder;
    private DataShare dataStore; //za skladistenje buffer

    NeuralNetwork nnetwork; //neuronska mreza
    ImageRecognitionPlugin imageRecognition; //plagin za prepoznavanje
    private double neuralNetworksensitivity;// osetljivost nn mreze

    private String fileMediaPath = ""; //putanja ka video fajlu

    private boolean highLvlShape = false;
    private String nnVariable; // varijabla za pozivanje output.get(variable)

    private String outputObjectFolder;
    private boolean saveObjects;

    private Controller() {

        // get the image recognition plugin from neural network
        dataStore = new DataShare();
        neuralNetworksensitivity = 0.7;
        params = new CompareParams();
        ic = new ImageCompare();
        brojac = 0;
        imageUtil = new ImageProccessingAndOther();
//        params.setSensitivity(4000);
        params.setSensitivity(6500);

        outputObjectFolder = "pictures/pronasao/";
        saveObjects = false;
    }

    public static Controller getInstance() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public void compare(BufferedImage img1, BufferedImage img2) throws IOException, Exception {
        if (nnetwork == null) {
            throw new RuntimeException("Ucitaj neuronsku mrezu!");
        }
        if (videoDecoder == null) {
            throw new RuntimeException("Ucitaj drajver !");
        }

        ic.setImg1(img1);
        ic.setImg2(img2);

        if (highLvlShape) {
            params.setColumnsNmbr(img1.getWidth());
            params.setRowsNmbr(img1.getHeight());
        } else {
            params.setColumnsNmbr(img1.getWidth() / 2);
            params.setRowsNmbr(img1.getHeight() / 2);
        }

        ic.setParams(this.params);
        ic.compare();
        imgShapeRectangle = ic.getImgShape();  //segment+kontura
        imgNormalRectangle = img2;  //original+segment
        imgShape = ic.getImgShape();    //kontura
//        imageUtil.saveJPG(imgShape, "slicica.jpg");
        Dimension d = new Dimension(20, 20);    //broj segmenata za labelovanje
        SegmentImage segment = new SegmentImage(imgShape, d);
        segment.startSegmenting();

        Graphics2D graphicsShapeRect = imgShapeRectangle.createGraphics();
        graphicsShapeRect.setColor(Color.RED);
        graphicsShapeRect.setStroke(new BasicStroke(10));

        Graphics2D graphicsNormalRect = imgNormalRectangle.createGraphics();
        graphicsNormalRect.setColor(Color.RED);
        graphicsNormalRect.setStroke(new BasicStroke(10));

        Rectangle[] resoult = segment.getSegments();
        for (Rectangle rectangle : resoult) {
            if (rectangle.height > 110 && rectangle.width > 110) {
                BufferedImage bImage = imgShape.getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                HashMap<String, Double> output = imageRecognition.recognizeImage(bImage);
//                String variable = nnetwork.getLabel();
                if (output.get(nnVariable) > neuralNetworksensitivity) {
                    System.out.println(output.get(nnVariable));
//                    BufferedImage bImageForSave = imgNormalRectangle.getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

                    //snimace pronadjen objekte
                    if (saveObjects) {
                        imageUtil.saveJPG(bImage, outputObjectFolder + "/" + brojac + ".jpg");
                    }
                    System.out.println("Pronasao");
                    graphicsShapeRect.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                    graphicsShapeRect.dispose();
                    graphicsNormalRect.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                    graphicsNormalRect.dispose();
                }
                brojac++;
            }

        }

    }

//vraca original sliku sa pronadjenim promenama
//vraca samo konturu promena
    public void setParamsCompare(CompareParams params) {
        this.params = params;
    }

    public void setParamsCompareManually(int rowsNmbr, int columnsNmbr, int sensitivity, int stabilizer) {
        this.params = new CompareParams(rowsNmbr, columnsNmbr, sensitivity, stabilizer);
    }

    public BufferedImage getImgShapeRectangle() {
        return imgShapeRectangle;
    }

    public BufferedImage getImgNormalRectangle() {
        return imgNormalRectangle;
    }

    public void setMedia(String mediaEncoding) {
        if (mediaEncoding.equalsIgnoreCase("video")) {
            videoDecoder = new VideoProcessingThread(dataStore, fileMediaPath);
            System.out.println("video radi");
        }
        if (mediaEncoding.equalsIgnoreCase("cam")) {
            videoDecoder = new CameraProcessingThread(dataStore, "linux");
            System.out.println("kamera");
        }

    }

    public void setOutputObjectFolder(File outputFolder) {

        outputObjectFolder = outputFolder.getAbsolutePath();
        System.out.println(outputObjectFolder);
    }

    public void loadNeuralNetwork(File networkLocation) {
        nnetwork = NeuralNetwork.load(networkLocation.getAbsolutePath());
        nnVariable = networkLocation.getName().split("_")[1].split("\\.")[0];
        imageRecognition
                = (ImageRecognitionPlugin) nnetwork.getPlugin(ImageRecognitionPlugin.class
                ); // get the image recognition plugin from neural network

    }

    public void playMedia() {
        Thread vs = new Thread(videoDecoder);
        vs.start();
    }

    public void stopMedia() {
        videoDecoder.requestStop();
//        dataStore = new DataShare();
    }

    public DataShare getDataStore() {
        return dataStore;
    }

    public void setFileMediaPath(String fileMediaPath) {
        this.fileMediaPath = fileMediaPath;
    }

    public CompareParams getParams() {
        return params;
    }

    public void changeHighLvlShapeState() {
        highLvlShape = !highLvlShape;
    }

    public void setNeuralNetworksensitivity(double neuralNetworksensitivity) {
        this.neuralNetworksensitivity = neuralNetworksensitivity;
    }

    public void changeSaveObjectState() {
        saveObjects = !saveObjects;
    }

}
