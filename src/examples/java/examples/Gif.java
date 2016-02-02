package examples;

import examples.util.GifSequenceWriter;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by William Martin III on 12/25/15.
 */
public class Gif {
    static AtomScene scene = new AtomScene();

    public static void main(String[] args) {
        try {
            scene.camera.setSamples(64);
            scene.camera.setBlur(0.01);
            int lengthOfFrame = (int) (1.0 / scene.FRAME_RATE * 1000);
            System.out.println(lengthOfFrame + " milliseconds per frame.");

            ImageOutputStream stream = new FileImageOutputStream(new File("atomBlurred.gif"));
            GifSequenceWriter writer = new GifSequenceWriter(stream, BufferedImage.TYPE_INT_RGB, lengthOfFrame, true);

            System.out.println("Rendering " + (int) (scene.FRAME_RATE * scene.LENGTH) + " frames...");

            for (int i = 0; i < scene.LENGTH * scene.FRAME_RATE; i++) {
                BufferedImage original = scene.render(i);

                int w = (int) (original.getWidth() * scene.ZOOM);
                int h = (int) (original.getHeight() * scene.ZOOM);
                BufferedImage finished = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                AffineTransform at = new AffineTransform();
                at.scale(scene.ZOOM, scene.ZOOM);
                AffineTransformOp scaleOp =
                        new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                finished = scaleOp.filter(original, finished);

                writer.writeToSequence(finished);

                System.out.println("Frame " + i + " complete.");
            }

            writer.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
