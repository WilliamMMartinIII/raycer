package ray_tracer.cameras;

import javafx.concurrent.Task;
import org.jblas.DoubleMatrix;
import org.jblas.Geometry;
import ray_tracer.MatrixUtil;
import ray_tracer.background.Background;
import ray_tracer.geometry.Ray;
import ray_tracer.geometry.RayIntersect;
import ray_tracer.geometry.Renderable;
import ray_tracer.lights.Light;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by William Martin III on 12/24/15.
 */
public abstract class Camera {
    protected int height;
    protected int width;
    private DoubleMatrix position;
    private DoubleMatrix angle;
    private DoubleMatrix rotate;
    private DoubleMatrix up = new DoubleMatrix(new double[] {0, 1, 0});

    Background background;
    Collection<Renderable> geometry;
    Collection<Light> lights;

    private int currentX;
    private int currentY;
    private BufferedImage image;

    private boolean active = false;
    private ExecutorService executor;

    public void setUp() {
        startThreads();
        active = true;
    }

    public void tearDown() {
        active = false;
        stopThreads();
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setPosition(DoubleMatrix position) {
        this.position = position;
    }

    public void setPosition(double[] position) {
        setPosition(new DoubleMatrix(position));
    }

    public void setAngle(DoubleMatrix angle) {
        this.angle = angle.neg();
        Geometry.normalize(this.angle);

        calcRotate();
    }

    public void setAngle(double[] angle) {
        setAngle(new DoubleMatrix(angle));
    }

    public void lookAt(DoubleMatrix pos)
    {
        setAngle(pos.sub(position));
    }

    public void setUp(DoubleMatrix up) {
        this.up = up;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public void setGeometry(Collection<Renderable> geometry) {
        this.geometry = geometry;
    }

    public void setLights(Collection<Light> lights) {
        this.lights = lights;
    }

    public final BufferedImage render() {

        currentX = 0;
        currentY = 0;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        if (!active) {
            startThreads();
        }
        List<Callable<Void>> tasks = new LinkedList<>();
        Point current = getNext();
        while (current != null) {
            tasks.add(new Task(current));
            current = getNext();
        }
        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException ex) {
            System.err.println("Executor interrupted.");
        }
        if (!active) {
            stopThreads();
        }

        return image;
    }

    private synchronized Point getNext() {
        Point point = new Point(currentX++, currentY);
        if (currentX >= width) {
            currentX = 0;
            ++currentY;
        }

        if (currentY >= height) {
            return null;
        }

        return point;
    }

    public abstract Color getColor(Ray ray);

    protected Ray pointToRay(Point point) {
        double x = point.x;
        double y = point.y;
        double smaller = (width < height ? width : height);

        x = (x - (width / 2.0)) / smaller;
        y = (y - (height / 2.0)) / smaller;

        DoubleMatrix angle = new DoubleMatrix(new double[]{x, -y, 0.5});
        angle = rotate.mmul(angle);
        Geometry.normalize(angle);

        return new Ray(position, angle);
    }

    protected RayIntersect findClosest(Ray ray) {
        Renderable closest = null;
        double distance = Double.MAX_VALUE;
        for (Renderable renderable : geometry) {
            double newDist = renderable.getDistanceToIntersect(ray);

            if (newDist < 0) {
                continue;
            }

            if (newDist < distance) {
                closest = renderable;
                distance = newDist;
            }
        }

        return closest == null ? null : closest.getIntersect(ray);
    }

    private class Task implements Callable<Void> {
        Point point;

        Task(Point point) {
            this.point = point;
        }

        @Override
        public Void call() {
            Color color = getColor(pointToRay(point));

            image.setRGB(point.x, point.y, color.getRGB());

            return null;
        }
    }

    private void calcRotate() {
        rotate = MatrixUtil.rotate(angle, up, up.add(new DoubleMatrix(new double[] {0, 0, 1})));
    }

    private void startThreads() {
        int cores = Runtime.getRuntime().availableProcessors();
        executor = Executors.newFixedThreadPool(cores);
    }

    private void stopThreads() {
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            System.err.println("Thread pool executor interrupted while awaiting termination.");
        }
    }
}
