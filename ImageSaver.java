import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageSaver {
    public static void saveImage(BufferedImage image, String path) {
        try {
            ImageIO.write(image, "png", new File(path));
            System.out.println("Imagem salva em: " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
