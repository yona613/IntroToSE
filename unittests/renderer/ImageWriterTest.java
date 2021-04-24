package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;


class ImageWriterTest {

    @Test
    void writeToImage() {
        ImageWriter image = new ImageWriter("Test", 800, 500);
        for (int i = 0; i < 800; i++) {
            for (int j = 0; j < 500; j++) {
                image.writePixel(i, j, new Color(Color.CYAN));
            }

        }
        for (int i = 0; i < 800; i += 50) {
            for (int j = 0; j < 500; j++) {
                image.writePixel(i, j, new Color(Color.BLACK));
            }
        }

        for (int i = 0; i < 500; i += 50) {
            for (int j = 0; j < 800; j++) {
                image.writePixel(j, i, new Color(java.awt.Color.RED));
            }
        }

        image.writeToImage();
    }


}