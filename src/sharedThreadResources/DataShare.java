package sharedThreadResources;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author nick
 */
import java.awt.Image;
import java.util.concurrent.*;

public class DataShare {

    private final ArrayBlockingQueue<Image> listImage;
// ovde ima prostora za neko bolje resenje velicine bafera
    private final int BUFFER_SIZE = 3;
    public DataShare() {
        listImage = new ArrayBlockingQueue(BUFFER_SIZE);
    }

    public ArrayBlockingQueue<Image> getListImage() {
        return listImage;
    }

}
