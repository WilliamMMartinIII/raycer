package examples;


import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import org.jblas.DoubleMatrix;
import ray_tracer.cameras.Camera;
import ray_tracer.cameras.FancyCamera;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * Created by William Martin III on 12/25/15.
 */
public class Video {
    static AtomScene scene = new AtomScene();

    public static void main(String[] args) {

        IMediaWriter videoWriter = ToolFactory.makeWriter("compositeTest.mp4");
        videoWriter.addVideoStream(0, 0, scene.WIDTH, scene.HEIGHT);

        int lengthOfFrame = (int) (1.0 / scene.FRAME_RATE * 1000);
        System.out.println("Rendering " + (int) (scene.FRAME_RATE * scene.LENGTH) + " frames...");

        for (int i = 0; i < scene.LENGTH * scene.FRAME_RATE; i++) {
            BufferedImage frame = scene.render(i * 2);

            BufferedImage convertedImg = new BufferedImage(frame.getWidth(), frame.getHeight(),
                    BufferedImage.TYPE_3BYTE_BGR);
            convertedImg.getGraphics().drawImage(frame, 0, 0, null);

            videoWriter.encodeVideo(0, convertedImg, i * lengthOfFrame, TimeUnit.MILLISECONDS);
            System.out.println("Frame " + i + " complete.");
        }
        System.out.println("Rendering complete.");

        videoWriter.close();
    }
}
