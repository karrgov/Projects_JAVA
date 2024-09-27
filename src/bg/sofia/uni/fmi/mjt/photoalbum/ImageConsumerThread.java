package bg.sofia.uni.fmi.mjt.photoalbum;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageConsumerThread implements Runnable {

    private final String outputDirectory;
    private final Queue<Image> imageQueue;
    private final AtomicBoolean areAllAvailableImagesLoaded;

    public ImageConsumerThread(String outputDirectory, Queue<Image> imageQueue, AtomicBoolean areAllAvailableImagesLoaded) {
        this.outputDirectory = outputDirectory;
        this.imageQueue = imageQueue;
        this.areAllAvailableImagesLoaded = areAllAvailableImagesLoaded;
    }

    @Override
    public void run() {
        while(true) {
            Image image = null;

            try {
                image = getImageFromQueue();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e.getMessage(), e);
            }

            if(image == null) {
                break;
            }

            saveImage(convertToBlackAndWhite(image));
        }
    }

    private Image getImageFromQueue() throws InterruptedException {
        synchronized (this.imageQueue) {
            while(this.imageQueue.isEmpty()) {
                if(this.areAllAvailableImagesLoaded.get() && this.imageQueue.isEmpty()) {
                    return null;
                }

                this.imageQueue.wait();
            }
        }
        return this.imageQueue.poll();
    }

    private Image convertToBlackAndWhite(Image image) {
        BufferedImage processedData = new BufferedImage(image.getData().getWidth(), image.getData().getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        processedData.getGraphics().drawImage(image.getData(), 0, 0, null);

        return new Image(image.getName(), processedData);
    }

    private void saveImage(Image image) {
        try {
            ImageIO.write(image.getData(), image.getImageFormat(), new File(this.outputDirectory, image.getName()));
            System.out.println("Saved " + image.getName() + " to " + this.outputDirectory);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("While saving image %s", image.getName()), e);
        }
    }
}
