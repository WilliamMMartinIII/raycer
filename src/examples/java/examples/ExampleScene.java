package examples;

import com.google.common.collect.ImmutableList;
import org.jblas.DoubleMatrix;
import ray_tracer.background.*;
import ray_tracer.cameras.Camera;
import ray_tracer.cameras.CartoonCamera;
import ray_tracer.cameras.CompositeCamera;
import ray_tracer.cameras.FancyCamera;
import ray_tracer.cameras.lenses.SimpleLens;
import ray_tracer.geometry.Circle;
import ray_tracer.geometry.Geometry;
import ray_tracer.geometry.Sphere;
import ray_tracer.geometry.Triangle;
import ray_tracer.lights.AmbientLight;
import ray_tracer.lights.Light;
import ray_tracer.lights.PointLight;
import ray_tracer.material.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by William Martin III on 12/30/15.
 */
public class ExampleScene {
    static final int WIDTH = 300;
    static final int HEIGHT = 200;
    static final double ZOOM = 2.0;
    static final int FRAME_RATE = 30;
    static final double LENGTH = 4;

    public Camera camera;
    public Camera fancyCamera;
    public Camera diffuseCamera;
    public Camera cartoonCamera;
    DoubleMatrix cameraPosition = new DoubleMatrix(new double[] {0, 1, -1.5});
    DoubleMatrix cameraRotation = new DoubleMatrix(new double[] {0, 0, -1});

    static Material ground = new Material.Builder()
            .color(Color.GRAY.darker())
            .reflectAmount(0.75)
            .build();
    static Material ball = new Material.Builder()
            .color(Color.WHITE.darker())
            .reflectAmount(0.5)
            .build();
    static Material pink = new Material.Builder()
            .color(Color.MAGENTA)
            // .emitter(true)
            .build();
    static Material green = new Material.Builder()
            .color(Color.GREEN.darker())
            // .emitter(true)
            .build();

    static Texture brick;
    static Texture test;

    static TextureMapping groundMap;
    static TextureMapping ballMap;

    static Background cartoonBackground = new Horizon(Color.CYAN, Color.ORANGE);
    static Background gradBackground = new GradientBackground(Color.MAGENTA, new Color(0x2A1600));
    static Background skyBackground;

    public ExampleScene() {

        ((GradientBackground) gradBackground).addStop(0.5, Color.ORANGE);
        ((GradientBackground) gradBackground).addStop(0.49, new Color(0x2A1600));

        try {
            brick = new Texture.Builder(ImageIO.read(new File("src/examples/resources/textures/brick_DIFFUSE.jpg")))
                    .normal(ImageIO.read(new File("src/examples/resources/textures/brick_NORMAL.jpg")))
                    .specular(ImageIO.read(new File("src/examples/resources/textures/brick_REFLECT.jpg")))
                    .reflection(ImageIO.read(new File("src/examples/resources/textures/brick_REFLECT.jpg")))
                    .size(1500, 1000)
                    .build();
            test = new Texture.Builder(ImageIO.read(new File("src/examples/resources/textures/test.png")))
                    .size(512, 512)
                    .build();

            skyBackground = new SkyBox(ImageIO.read(new File("src/examples/resources/skyboxes/skybox.jpg")));
        } catch (IOException e) {
            System.err.println("TextureMapping images could not be loaded! " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        groundMap = new FlatMap(
                new DoubleMatrix(new double[] {5, 0, 0}),
                new DoubleMatrix(new double[] {0, 0, 5}),
                new DoubleMatrix(new double[] {0, 0, 0}),
                brick);

        ballMap = new SphereMap(brick);

        fancyCamera = new FancyCamera();
        fancyCamera.setWidth(WIDTH);
        fancyCamera.setHeight(HEIGHT);
        fancyCamera.setLens(new SimpleLens(WIDTH, HEIGHT, 2.0, 3.0));

        diffuseCamera = new FancyCamera().enableReflection(false).enableSpecular(false);
        cartoonCamera = new CartoonCamera();

        camera = new CompositeCamera(ImmutableList.of(diffuseCamera, cartoonCamera), (cameras, point) ->
            cameras.get((int) (((double)point.x) / WIDTH * cameras.size())));

        camera.copy(fancyCamera);
    }

    public BufferedImage render(double frame) {
        Collection<Geometry> geometry = new LinkedList<>();

        geometry.add(new Triangle(
                new double[] {10, -1, -10},
                new double[] {-10, -1, -10},
                new double[] {-10, -1, 10},
                ground,
                groundMap));
        geometry.add(new Triangle(
                new double[] {10, -1, -10},
                new double[] {-10, -1, 10},
                new double[] {10, -1, 10},
                ground,
                groundMap));

        double position = frame / (FRAME_RATE * LENGTH) * (2 * Math.PI);

        geometry.add(new Sphere(new double[] {0, 0, 0}, 1, ball, null));
        geometry.add(new Sphere(new double[] {Math.sin(position) * -5, 0, Math.cos(position) * 5}, 1, pink, null));
        geometry.add(new Circle(new double[] {-3, -0.5, 0}, new double[] {-1, -0.5, 0}, new double[] {-3, -0.5, -1}, green, null));

        Collection<Light> lights = new LinkedList<>();
        lights.add(new PointLight(Color.white, new double[] {Math.sin(position) * 5, 2, Math.cos(position) * 5}, 0.5));
//        lights.add(new SunLight(Color.ORANGE.brighter(), new double[] {0.25, -0.5, 0}, 0.25));
//        lights.add(new SunLight(Color.blue.brighter(), new double[] {-0.25, -0.5, 0}, 0.25));
        lights.add(new AmbientLight(Color.WHITE, 0.25));

        fancyCamera.setLights(lights);
        fancyCamera.setGeometry(geometry);
        fancyCamera.setPosition(cameraPosition);
        fancyCamera.setAngle(cameraRotation);

        diffuseCamera.copy(fancyCamera);
        cartoonCamera.copy(fancyCamera);

        fancyCamera.setBackground(skyBackground);
        diffuseCamera.setBackground(skyBackground);
        cartoonCamera.setBackground(cartoonBackground);

        return fancyCamera.render();
    }
}
