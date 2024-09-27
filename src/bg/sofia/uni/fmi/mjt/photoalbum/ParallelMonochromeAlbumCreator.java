package bg.sofia.uni.fmi.mjt.photoalbum;

import javax.imageio.ImageIO;
import javax.swing.plaf.TableHeaderUI;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelMonochromeAlbumCreator implements MonochromeAlbumCreator {

    int imageProcessorsCount;
    private Queue<Image> images;

    public ParallelMonochromeAlbumCreator(int imageProcessorsCount) {
        this.imageProcessorsCount = imageProcessorsCount;
        this.images = new ArrayDeque<>();
    }

    @Override
    public void processImages(String sourceDirectory, String outputDirectory) {
        AtomicInteger loadedImagesCount = new AtomicInteger(0);
        AtomicBoolean areAllAvailableImagesLoaded = new AtomicBoolean(false);

        Path directory = Paths.get(sourceDirectory);

        try {
            List<Path> paths = getImagePaths(directory);
            AtomicInteger allImagesCount = new AtomicInteger(paths.size());

            paths.forEach(p -> {
                Thread imageProducerThread = new Thread(new ImageProducerThread(p, this.images, loadedImagesCount, allImagesCount, areAllAvailableImagesLoaded));
                imageProducerThread.start();
            });
        } catch (IOException e) {
            throw new UncheckedIOException("A problem occurred!", e);
        }

        for(int i = 0; i < this.imageProcessorsCount; i++) {
            Thread imageConsumerThread = new Thread(new ImageConsumerThread(outputDirectory, images, areAllAvailableImagesLoaded));
            imageConsumerThread.start();
        }
    }

    private List<Path> getImagePaths(Path sourcePath) throws IOException {
        try (Stream<Path> files = Files.walk(sourcePath)) {
            return files.skip(1)
                    .filter(p -> p.toString().toLowerCase().endsWith(".jpg")
                            || p.toString().toLowerCase().endsWith(".jpeg")
                            || p.toString().toLowerCase().endsWith(".png"))
                    .collect(Collectors.toList());
        }
    }

}
