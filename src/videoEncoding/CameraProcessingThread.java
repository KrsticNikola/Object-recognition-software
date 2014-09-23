/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoEncoding;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IError;
import com.xuggle.xuggler.IMetaData;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;
import java.awt.image.BufferedImage;
import sharedThreadResources.DataShare;

/**
 *
 * @author nick
 */
public class CameraProcessingThread implements IDecoder {

    private final DataShare dataStore;
//    private DataShareProcessing testDataShare;
    private volatile boolean stop = false; //stop flag
    String driverName = "";
    String deviceName = "";
    IContainer container;

    public CameraProcessingThread(DataShare dataStore) {
        this.dataStore = dataStore;
        if (isUnix()) {
            driverName = "video4linux2";
            deviceName = "/dev/video0";
        }
        if (isWindows()) {
            driverName = "vfwcap";
            deviceName = "0";
        }
        if (driverName.isEmpty()) {
            throw new RuntimeException("Osx nije prepoznat!");
        }

    }

    @Override
    public void run() {
        runIt();
    }

    private void runIt() {

        // Let's make sure that we can actually convert video pixel formats.
        if (!IVideoResampler.isSupported(IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION)) {
            throw new RuntimeException("you must install the GPL version of Xuggler (with IVideoResampler support) for this demo to work");
        }

        // Create a Xuggler container object
        container = IContainer.make();

        // Tell Xuggler about the device format
        IContainerFormat format = IContainerFormat.make();
        if (format.setInputFormat(driverName) < 0) {
            throw new IllegalArgumentException("couldn't open webcam device: " + driverName);
        }

        // devices, unlike most files, need to have parameters set in order
        // for Xuggler to know how to configure them, for a webcam, these
        // parameters make sense
        IMetaData params = IMetaData.make();

        params.setValue("framerate", "30/1");
        params.setValue("video_size", "640x480");

        // Open up the container
        int retval = container.open(deviceName, IContainer.Type.READ, format,
                false, true, params, null);
        if (retval < 0) {
            // This little trick converts the non friendly integer return value into
            // a slightly more friendly object to get a human-readable error name
            IError error = IError.make(retval);
            throw new IllegalArgumentException("could not open file: " + deviceName + "; Error: " + error.getDescription());
        }

        // query how many streams the call to open found
        int numStreams = container.getNumStreams();

        // and iterate through the streams to find the first video stream
        int videoStreamId = -1;
        IStreamCoder videoCoder = null;
        for (int i = 0; i < numStreams; i++) {
            // Find the stream object
            IStream stream = container.getStream(i);
            // Get the pre-configured decoder that can decode this stream;
            IStreamCoder coder = stream.getStreamCoder();

            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                videoStreamId = i;
                videoCoder = coder;
                break;
            }
        }
        if (videoStreamId == -1) {
            throw new RuntimeException("could not find video stream in container: " + deviceName);
        }

        /*
         * Now we have found the video stream in this file.  Let's open up our decoder so it can
         * do work.
         */
        if (videoCoder.open() < 0) {
            throw new RuntimeException("could not open video decoder for container: " + deviceName);
        }

        IVideoResampler resampler = null;
        if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24) {
            // if this stream is not in BGR24, we're going to need to
            // convert it.  The VideoResampler does that for us.
            resampler = IVideoResampler.make(videoCoder.getWidth(), videoCoder.getHeight(), IPixelFormat.Type.BGR24,
                    videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
            if (resampler == null) {
                throw new RuntimeException("could not create color space resampler for: " + deviceName);
            }
        }
        /*
         * And once we have that, we draw a window on screen
         */
//        openJavaWindow();

        /*
         * Now, we start walking through the container looking at each packet.
         */
//        ArrayBlockingQueue arrayList = dataStore.getListImage();
        IPacket packet = IPacket.make();
        while (!stop) {
            container.readNextPacket(packet);
//        while ((container != null) && (container.readNextPacket(packet) >= 0)) {
            /*
             * Now we have a packet, let's see if it belongs to our video stream
             */
            if (packet.getStreamIndex() == videoStreamId) {
                /*
                 * We allocate a new picture to get the data out of Xuggler
                 */
                IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),
                        videoCoder.getWidth(), videoCoder.getHeight());

                int offset = 0;
                while (offset < packet.getSize()) {
                    /*
                     * Now, we decode the video, checking for any errors.
                     * 
                     */
                    int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
                    if (bytesDecoded < 0) {
                        throw new RuntimeException("got error decoding video in: " + deviceName);
                    }
                    offset += bytesDecoded;

                    /*
                     * Some decoders will consume data in a packet, but will not be able to construct
                     * a full video picture yet.  Therefore you should always check if you
                     * got a complete picture from the decoder
                     */
                    if (picture.isComplete()) {
                        IVideoPicture newPic = picture;
                        if (resampler != null) {
                            // we must resample
                            newPic = IVideoPicture.make(resampler.getOutputPixelFormat(), picture.getWidth(), picture.getHeight());
                            if (resampler.resample(newPic, picture) < 0) {
                                throw new RuntimeException("could not resample video from: " + deviceName);
                            }
                        }
                        if (newPic.getPixelType() != IPixelFormat.Type.BGR24) {
                            throw new RuntimeException("could not decode video as BGR 24 bit data in: " + deviceName);
                        }
                        BufferedImage javaImage = Utils.videoPictureToImage(newPic);
                        putdataStore(javaImage);
                    }
                }
            } else {
                /*
                 * This packet isn't part of our video stream, so we just silently drop it.
                 */
                do {
                } while (false);
            }

        }
        /*
         * Technically since we're exiting anyway, these will be cleaned up by 
         * the garbage collector... but because we're nice people and want
         * to be invited places for Christmas, we're going to show how to clean up.
         */
        if (videoCoder != null) {
            videoCoder.close();
            videoCoder = null;
        }
        if (container != null) {
            container.close();
            container = null;
        }
        System.out.println("Zavrseno procesiranje video kamere");

    }

    //stop metoda
    @Override
    public void requestStop() {
//        container.close();
//        container = null;
        stop = true;

    }

    private void putdataStore(BufferedImage image) {
        try {

            dataStore.getListImage().put(image);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    private boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);
    }

    private boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0);
    }
}
