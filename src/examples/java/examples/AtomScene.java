package examples;

import org.jblas.DoubleMatrix;
import ray_tracer.MatrixUtil;
import ray_tracer.background.Background;
import ray_tracer.background.GradientBackground;
import ray_tracer.cameras.Camera;
import ray_tracer.cameras.CartoonCamera;
import ray_tracer.cameras.FancyCamera;
import ray_tracer.cameras.FlatCamera;
import ray_tracer.cameras.lenses.SimpleLens;
import ray_tracer.geometry.Geometry;
import ray_tracer.geometry.Sphere;
import ray_tracer.lights.AmbientLight;
import ray_tracer.lights.Light;
import ray_tracer.lights.PointLight;
import ray_tracer.lights.SunLight;
import ray_tracer.material.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by William Martin III on 12/30/15.
 */
public class AtomScene {
    final int WIDTH = 400;
    final int HEIGHT = 400;
    final double ZOOM = 1.0;
    final int FRAME_RATE = 30;
    final double LENGTH = 5;

    private double magic = Math.sqrt(2.0) / 2.0;

    public Camera camera;
    static double[] cameraPosition = new double[] {0, 0, -10};
    static double[] cameraRotation = new double[] {0, 0, -1};

    static Material proton = new Material.Builder()
            .color(Color.WHITE)
            .reflectAmount(1.0)
            .build();
    static Material neutron = new Material.Builder()
            .color(Color.WHITE)
            .reflectAmount(1.0)
            .build();
    static Material electron = new Material.Builder()
            .color(Color.CYAN.brighter())
            .emitter(true)
            .build();

    Color darkBlue = new Color(0, 0, 16);
    Background background = new GradientBackground(Color.BLACK, darkBlue);

    public AtomScene() {
        camera = new FancyCamera();
        camera.setWidth(WIDTH);
        camera.setHeight(HEIGHT);
        camera.setBackground(background);
        camera.setLens(new SimpleLens(WIDTH, HEIGHT, 2.0, 3.0));
    }

    public BufferedImage render(double frame) {
        Collection<Geometry> geometry = new LinkedList<>();

        double position = (frame / (FRAME_RATE * LENGTH)) * (2 * Math.PI);

        DoubleMatrix yRot = new DoubleMatrix(new double[] {Math.sin(position), 0, Math.cos(position)});
        DoubleMatrix zRot = new DoubleMatrix(new double[] {Math.sin(position), Math.cos(position), 0});
        DoubleMatrix rotation = MatrixUtil.rotate(yRot, MatrixUtil.Y, MatrixUtil.Y);
        rotation = rotation.mmul(MatrixUtil.rotate(zRot, MatrixUtil.Z, MatrixUtil.Z));

        DoubleMatrix n1Pos = new DoubleMatrix(new double[] {0, -magic, 1.0});
        n1Pos = rotation.mmul(n1Pos);
        DoubleMatrix n2Pos = new DoubleMatrix(new double[] {0, -magic, -1.0});
        n2Pos = rotation.mmul(n2Pos);
        DoubleMatrix p1Pos = new DoubleMatrix(new double[] {1.0, magic, 0});
        p1Pos = rotation.mmul(p1Pos);
        DoubleMatrix p2Pos = new DoubleMatrix(new double[] {-1.0, magic, 0});
        p2Pos = rotation.mmul(p2Pos);

        rotation = rotation.neg();

        DoubleMatrix e1Pos = new DoubleMatrix(new double[] {Math.sin(position * 5), Math.cos(position * 5), 0});
        e1Pos = rotation.mmul(e1Pos);
        DoubleMatrix e2Pos = new DoubleMatrix(new double[] {Math.cos(position * 5), 0, Math.sin(position * 5)});
        e2Pos = rotation.mmul(e2Pos);

        geometry.add(new Sphere(n1Pos.toArray(), 1, neutron, null));
        geometry.add(new Sphere(n2Pos.toArray(), 1, neutron, null));
        geometry.add(new Sphere(p1Pos.toArray(), 1, proton, null));
        geometry.add(new Sphere(p2Pos.toArray(), 1, proton, null));
        geometry.add(new Sphere(e1Pos.mul(5.0).toArray(), 0.5, electron, null));
        geometry.add(new Sphere(e2Pos.mul(5.0).toArray(), 0.5, electron, null));

        Collection<Light> lights = new LinkedList<>();
        lights.add(new PointLight(Color.CYAN.brighter(), e1Pos.mul(3.5).toArray(), 0.1));
        lights.add(new PointLight(Color.CYAN.brighter(), e2Pos.mul(3.5).toArray(), 0.1));

        lights.add(new SunLight(darkBlue, new double[] {0, 1, 0}, 1));
        lights.add(new SunLight(darkBlue, new double[] {0, -1, 0}, 0.5));
//        lights.add(new PointLight(Color.white, new double[] {Math.sin(position) * 5, 2, Math.cos(position) * 5}, 0.5));
//        lights.add(new SunLight(Color.BLUE, new double[] {0, 1, 0}, 0.2));
//        lights.add(new SunLight(Color.ORANGE, new double[] {0, -1, 0}, 0.2));
//        // lights.add(new SunLight(Color.blue.brighter(), new double[] {-0.25, -0.5, 0}, 0.25));
        lights.add(new AmbientLight(Color.WHITE, 0.01));

        camera.setLights(lights);
        camera.setGeometry(geometry);
        camera.setPosition(cameraPosition);
        camera.setAngle(cameraRotation);

        return camera.render();
    }
}
