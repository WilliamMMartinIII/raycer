package examples;

import org.jblas.DoubleMatrix;
import ray_tracer.background.Background;
import ray_tracer.background.FlatBackground;
import ray_tracer.cameras.Camera;
import ray_tracer.cameras.FancyCamera;
import ray_tracer.material.Material;
import ray_tracer.geometry.Geometry;
import ray_tracer.geometry.Sphere;
import ray_tracer.geometry.Triangle;
import ray_tracer.lights.Light;
import ray_tracer.lights.SunLight;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by William Martin III on 12/25/15.
 */
public class RayTracer {

    public static void main(String[] args) {
        Material tan = new Material.Builder()
                .color(new Color(255, 230, 153))
                .reflectAmount(0.2)
                .build();
        Material grey = new Material.Builder()
                .color(new Color(128, 128, 128))
                .build();
        Material lightGrey = new Material.Builder()
                .color(new Color(204, 204, 204))
                .build();

        Material white = new Material.Builder()
                .color(new Color(230, 230, 230))
                .transparency(0.5)
                .reflectAmount(0.8)
                .build();
        Material red = new Material.Builder()
                .color(new Color(230, 51, 51))
                .transparency(0.5)
                .reflectAmount(0.8)
                .build();
        Material blue = new Material.Builder()
                .color(new Color(102, 204, 230))
                .transparency(0.5)
                .reflectAmount(0.8)
                .build();
        Material yellow = new Material.Builder()
                .color(new Color(230, 204, 77))
                .transparency(0.5)
                .reflectAmount(0.8)
                .build();

        int far = -80;
        int near = 20;

        List<Geometry> geometry = new LinkedList<>();

        geometry.add(new Triangle(
                new DoubleMatrix(new double[]{-1.5, -5, near}),
                new DoubleMatrix(new double[]{-1.5, -5, far}),
                new DoubleMatrix(new double[]{-100, -5, near}),
                tan,
                null
        ));
        geometry.add(new Triangle(
                new DoubleMatrix(new double[]{-100, -5, near}),
                new DoubleMatrix(new double[]{-1.5, -5, far}),
                new DoubleMatrix(new double[]{-100, -5, far}),
                tan,
                null
        ));

        geometry.add(new Triangle(
                new DoubleMatrix(new double[]{-1, -5.5, far}),
                new DoubleMatrix(new double[]{-1.5, -5, far}),
                new DoubleMatrix(new double[]{-1.5, -5, near}),
                tan,
                null
        ));
        geometry.add(new Triangle(
                new DoubleMatrix(new double[]{-1.5, -5, near}),
                new DoubleMatrix(new double[]{-1, -5.5, near}),
                new DoubleMatrix(new double[]{-1, -5.5, far}),
                tan,
                null
        ));

        // surface side
        geometry.add(new Triangle(
                new DoubleMatrix(new double[]{-1, -40, far}),
                new DoubleMatrix(new double[]{-1, -5.5, far}),
                new DoubleMatrix(new double[]{-1, -5.5, 10}),
                tan,
                null
        ));
        geometry.add(new Triangle(
                new DoubleMatrix(new double[]{-1, -40, far}),
                new DoubleMatrix(new double[]{-1, -5.5, 10}),
                new DoubleMatrix(new double[]{-1, -40, 10}),
                tan,
                null
        ));

        // back wall
        geometry.add(new Triangle(
                new DoubleMatrix(new double[]{-100, 100, far}),
                new DoubleMatrix(new double[]{-100, -40, far}),
                new DoubleMatrix(new double[]{100, -40, far}),
                lightGrey,
                null
        ));
        geometry.add(new Triangle(
                new DoubleMatrix(new double[]{100, -40, far}),
                new DoubleMatrix(new double[]{100, 100, far}),
                new DoubleMatrix(new double[]{-100, 100, far}),
                lightGrey,
                null
        ));

        // lower wall
        geometry.add(new Triangle(
                new DoubleMatrix(new double[]{100, -40, far}),
                new DoubleMatrix(new double[]{-1, -40, far}),
                new DoubleMatrix(new double[]{-1, -40, near}),
                grey,
                null
        ));
        geometry.add(new Triangle(
                new DoubleMatrix(new double[]{-1, -40, near}),
                new DoubleMatrix(new double[]{100, -40, near}),
                new DoubleMatrix(new double[]{-1, -40, far}),
                grey,
                null
        ));

        geometry.add(new Sphere(new double[] {-4, -2, -10}, 3, white, null));
        geometry.add(new Sphere(new double[] {-2, -4, -6}, 1, red, null));
        geometry.add(new Sphere(new double[] {-6, -3, -4}, 2, blue, null));
        geometry.add(new Sphere(new double[] {-6, -4, 2}, 1, yellow, null));

        Background background = new FlatBackground(Color.RED);

        List<Light> lights = new LinkedList<>();

        lights.add(new SunLight(new Color(51, 102, 204), new double[] {-3, -1, -1}, 0.3));
        lights.add(new SunLight(new Color(255, 206, 122), new double[] {2, -1, -1}, 0.3));
        lights.add(new SunLight(new Color(128, 128, 128), new double[] {2, -1, -1}, 0.3));

        Camera camera = new FancyCamera();
        camera.setBackground(background);
        camera.setGeometry(geometry);
        camera.setLights(lights);

        camera.setPosition(new double[] {0, 0, 5});
        camera.setAngle(new double[] {0, 0, 1});

        camera.setHeight(512);
        camera.setWidth(512);

        try {
            // retrieve image
            BufferedImage bi = camera.render();
            File outputFile = new File("/home/will/Desktop/test.png");
            ImageIO.write(bi, "png", outputFile);
        } catch (IOException e) {
            System.out.println("oopsies");
        }

    }
}
