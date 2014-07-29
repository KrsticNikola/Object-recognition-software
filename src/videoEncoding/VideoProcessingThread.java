/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoEncoding;

import java.awt.image.BufferedImage;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;
import sharedThreadResources.DataShare;

public class VideoProcessingThread implements IDecoder {

    private final DataShare dataStore;
//    private DataShareProcessing testDataShare;
    private volatile boolean stop = false; //stop flag

    public VideoProcessingThread(DataShare dataStore, String inputFilename) {
        this.dataStore = dataStore;
        this.inputFilename = inputFilename;

    }

//    public VideoProcessingThread(DataShare testDataShare, int a) {
////        this.dataStore = dataStore;
//        dataStore = null;
//        this.testDataShare = new DataShareProcessing(testDataShare);
//    }
    @Override
    public void run() {
        runIt();
    }

//    public final double SECONDS_BETWEEN_FRAMES = 0.001;
//    public final double SECONDS_BETWEEN_FRAMES = 0.00001;
//    public final double SECONDS_BETWEEN_FRAMES = 0.00001;
    public final double SECONDS_BETWEEN_FRAMES = 0.000001;
//    public final double SECONDS_BETWEEN_FRAMES = 0.003;
    private final String inputFilename;

    long start = System.currentTimeMillis();
    long last = start;
    // The video stream index, used to ensure we display frames from one and
    // only one video stream from the media container.
    private int mVideoStreamIndex = -1;

    // Time of last frame write
    private long mLastPtsWrite = Global.NO_PTS;

    public final long MICRO_SECONDS_BETWEEN_FRAMES
            = (long) (Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);
    IMediaReader mediaReader;

    private void runIt() {
        mediaReader = ToolFactory.makeReader(inputFilename);

        // stipulate that we want BufferedImages created in BGR 24bit color space
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

        mediaReader.addListener(new ImageSnapListener());

        // read out the contents of the media file and
        // dispatch events to the attached listener
        while (mediaReader != null && (mediaReader.readPacket() == null)) ;
//        while ((mediaReader.readPacket() == null) && (!stop)) ;
        System.out.println("Zavrseno dekodiranje");

    }

    //stop metoda
    @Override
    public void requestStop() {
        mediaReader.close();
        mediaReader = null;

    }

    private class ImageSnapListener extends MediaListenerAdapter {

        @Override
        public void onVideoPicture(IVideoPictureEvent event) {

            if (event.getStreamIndex() != mVideoStreamIndex) {
                // if the selected video stream id is not yet set, go ahead an
                // select this lucky video stream
                if (mVideoStreamIndex == -1) {
                    mVideoStreamIndex = event.getStreamIndex();
                } // no need to show frames from this video stream
                else {
                    return;
                }
            }

            // if uninitialized, back date mLastPtsWrite to get the very first frame
            if (mLastPtsWrite == Global.NO_PTS) {
                mLastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;
            }

            // if it's time to write the next frame
            if (event.getTimeStamp() - mLastPtsWrite
                    >= MICRO_SECONDS_BETWEEN_FRAMES) {
//                dataStore.addToArrayBlockingQueue(event.getImage());
                putdataStore(event.getImage());
//                testDataShare.addImage(event.getImage());
                mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
            }

        }

        private void putdataStore(BufferedImage image) {
            try {

                dataStore.getListImage().put(image);

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }

    }

}
