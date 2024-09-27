package bg.sofia.uni.fmi.mjt.photoalbum;

public class MainPhotoAlbum {
    public static void main(String[] args) {
        ParallelMonochromeAlbumCreator albumCreator = new ParallelMonochromeAlbumCreator(6);
        albumCreator.processImages("src/bg/sofia/uni/fmi/mjt/photoalbum/photoscolorful",
                "src/bg/sofia/uni/fmi/mjt/photoalbum/photosblackandwhite");
    }
}
