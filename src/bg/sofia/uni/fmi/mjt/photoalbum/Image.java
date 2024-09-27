package bg.sofia.uni.fmi.mjt.photoalbum;

import java.awt.image.BufferedImage;

public class Image {

    private String name;
    private BufferedImage data;

    public Image(String name, BufferedImage data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return this.name;
    }

    public BufferedImage getData() {
        return this.data;
    }

    public String getImageFormat() {
        int index = this.name.lastIndexOf('.');
        if(index != -1 && index < this.name.length() - 1) {
            return this.name.substring(index + 1).toLowerCase();
        }
        return "png";
    }

}
