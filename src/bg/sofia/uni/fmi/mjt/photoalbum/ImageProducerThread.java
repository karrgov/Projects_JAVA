package bg.sofia.uni.fmi.mjt.photoalbum;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageProducerThread implements Runnable {

    private final Path imagePath;
    private final Queue<Image> imageQueue;
    private final AtomicInteger loadedImagesCount;
    private final AtomicInteger allImagesCount;
    private final AtomicBoolean areAllAvailableImagesLoaded;

    public ImageProducerThread(Path imagePath, Queue<Image> imageQueue, AtomicInteger loadedImagesCount, AtomicInteger allImagesCount, AtomicBoolean areAllAvailableImagesLoaded) {
        this.imagePath = imagePath;
        this.imageQueue = imageQueue;
        this.loadedImagesCount = loadedImagesCount;
        this.allImagesCount = allImagesCount;
        this.areAllAvailableImagesLoaded = areAllAvailableImagesLoaded;
    }

    @Override
    public void run() {
        Image image = loadImage();

        synchronized (this.imageQueue) {
            this.imageQueue.offer(image);
            this.loadedImagesCount.incrementAndGet();
            if(this.loadedImagesCount.get() == this.allImagesCount.get()) {
                this.areAllAvailableImagesLoaded.set(true);
            }
            this.imageQueue.notifyAll();
        }
    }

    private Image loadImage() {
        try {
            BufferedImage imageData = ImageIO.read(this.imagePath.toFile());
            return new Image(this.imagePath.getFileName().toString(), imageData);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Failed to load image %s", this.imagePath), e);
        }
    }
}
