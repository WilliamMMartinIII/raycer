package ray_tracer.cameras;

import javafx.util.Pair;
import org.jblas.DoubleMatrix;
import ray_tracer.MatrixUtil;
import ray_tracer.background.Background;
import ray_tracer.cameras.lenses.Lens;
import ray_tracer.geometry.Ray;
import ray_tracer.geometry.RayIntersect;
import ray_tracer.geometry.Geometry;
import ray_tracer.lights.Light;

import java.awt.*;
import java.awt.font.NumericShaper;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The base for all cameras which describe how rays interact with their environment to generate an image.
 *
 * <p>The typical life-cycle of how to color an image pixel consists of
 * <ol>
 *     <li>Determine the location and angle of the initial ray to interact with the scene</li>
 *     <li>Find the closest object that the ray intersects with</li>
 *     <li>Get details about the intersection point such as surface normal and reflection angle</li>
 *     <li>Determine what color the object is at the intersection point using techniques such as
 *          <ul>
 *              <li>Diffuse (lambertion) color</li>
 *              <li>Specular color</li>
 *              <li>Ambient color</li>
 *          </ul>
 *          utilizing material properties and textures in conjunction with lighting conditions
 *     </li>
 *     <li>Recursively reflect more rays given the surfaces reflection properties</li>
 * </ol>
 * Note that this is the traditional process and stylized cameras can be created by modifying the behavior.
 *
 * <p>The only required action that must be taken before a camera may render anything is that the size of the resulting
 * image must be specified and a lens must be specified.
 *
 * Created by William Martin III on 12/24/15.
 */
public abstract class Camera {
    protected int height;
    protected int width;

    protected Background background;
    protected Collection<Geometry> geometry;
    protected Collection<Light> lights;

    protected BufferedImage image;

    private DoubleMatrix position;
    private DoubleMatrix angle;
    private DoubleMatrix rotate;
    private DoubleMatrix up = new DoubleMatrix(new double[] {0, 1, 0});

    private Lens lens;

    private int samples = 1;
    private double blur = 0;

    /**
     * Copies all internal parameters of the other camera that petain to the output image.
     *
     * @param other The camera whose properties are to be copied
     */
    public void copy(Camera other) {
        this.width = other.width;
        this.height = other.height;
        this.background = other.background;
        this.geometry = other.geometry;
        this.lights = other.lights;
        this.position = other.position;
        this.angle = other.angle;
        this.rotate = other.rotate;
        this.up = other.up;
        this.lens = other.lens;
    }

    /**
     * Sets the height of the image to be rendered.
     *
     * @param height The height of the output image
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Sets the width of the image to be rendered.
     *
     * @param width The width of the output image
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets the location of the camera in space using a {@link DoubleMatrix}.
     *
     * @param position A vector describing the position of the camera
     */
    public void setPosition(DoubleMatrix position) {
        this.position = position;
    }

    /**
     * Sets the location of a camera in space using a double array.
     *
     * @param position A vector describing the position of the camera
     */
    public void setPosition(double[] position) {
        setPosition(new DoubleMatrix(position));
    }

    /**
     * Sets the direction the camera is pointing using a {@link DoubleMatrix}.
     *
     * @param angle A vector describing the angle of the camera
     */
    public void setAngle(DoubleMatrix angle) {
        this.angle = angle.neg();
        org.jblas.Geometry.normalize(this.angle);

        calcRotate();
    }

    /**
     * Sets the direction the camera is pointing using a double array.
     *
     * @param angle A vector describing the angle of the camera
     */
    public void setAngle(double[] angle) {
        setAngle(new DoubleMatrix(angle));
    }

    /**
     * Points the camera at a specific point in space.
     *
     * @param point A {@link DoubleMatrix} vector describing the point for the camera to look at
     */
    public void lookAt(DoubleMatrix point)
    {
        setAngle(point.sub(position));
    }

    /**
     * Specifies the angle which the camera will orient as "upwards".
     *
     * @param up The angle pointing up
     */
    public void setUpDirection(DoubleMatrix up) {
        this.up = up;
    }

    /**
     * Sets the lens for the camera to use to determine the formation of rays shot from it.
     *
     * @param lens The lens to use
     */
    public void setLens(Lens lens) {
        this.lens = lens;
    }

    /**
     * Sets the background of the scene to render.
     *
     * @param background The background to use
     */
    public void setBackground(Background background) {
        this.background = background;
    }

    /**
     * Sets all geometry to be considered while rendering a scene.
     *
     * @param geometry A collection of geometry to be used
     */
    public void setGeometry(Collection<Geometry> geometry) {
        this.geometry = geometry;
    }

    /**
     * Specifies all light sources to be considered while rendering a scene.
     *
     * @param lights A collection of lights to be used
     */
    public void setLights(Collection<Light> lights) {
        this.lights = lights;
    }

    /**
     * Specifies the number of rays to shoot per pixel.
     *
     * @param samples The number of rays
     * @throws IllegalArgumentException if samples is not greater than zero
     */
    public void setSamples(int samples) {
        if (samples <= 0) {
            throw new IllegalArgumentException("The number of samples must be greater than zero.");
        }
        this.samples = samples;
    }

    /**
     * The amount to randomize the direction of the rays when shot.
     *
     * @param blur A double describing the blur amount
     * @throws IllegalArgumentException if the amount is less than zero
     */
    public void setBlur(double blur) {
        if (blur < 0) {
            throw new IllegalArgumentException("The blur amount must not be less than zero.");
        }
        this.blur = blur;
    }

    /**
     * Renders an image of the scene specified.
     *
     * <p>It is recommended that {@link #setWidth(int)}, {@link #setHeight(int)}, {@link #setPosition(DoubleMatrix)},
     * {@link #setAngle(DoubleMatrix)} (or {@link #lookAt(DoubleMatrix)}), {@link #setBackground(Background)},
     * {@link #setGeometry(Collection)}, and {@link #setLights(Collection)} are all called before {@link #render()} is
     * called to ensure that all details of the scene are properly specified. Note that specific implementations of
     * cameras may require more details to be set.
     *
     * @return An image of the scene described by the camera's position and angle and the scene's background, geometry,
     * and lights
     */
    public final BufferedImage render() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        if (samples == 1) {
            StreamSupport.stream(Spliterators.spliterator(new PointIterator(width, height), height * width,
                    Spliterator.NONNULL), true)
                    .forEach(point -> image.setRGB(point.x, point.y, getColor(pointToRay(point)).getRGB()));
        } else {
            StreamSupport.stream(Spliterators.spliterator(new PointIterator(width, height), height * width,
                    Spliterator.NONNULL), true)
                    .forEach(point -> {
                        Ray ray = pointToRay(point);
                        int[] rgb = {0, 0, 0};
                        for (int i = 0; i < samples; i++) {
                            DoubleMatrix angle = MatrixUtil.cheapRandomRotate(ray.getAngle(), blur);
                            Color color = getColor(new Ray(ray.getPosition(), angle));
                            rgb[0] += color.getRed();
                            rgb[1] += color.getGreen();
                            rgb[2] += color.getBlue();
                        }

                        Color color = new Color(rgb[0] / samples, rgb[1] / samples, rgb[2] / samples);
                        image.setRGB(point.x, point.y, color.getRGB());
                    });
        }

        return image;
    }

    /**
     * Describes the color returned by the given ray.
     *
     * <p>This is the main method that camera implementations must override and describes how all the elements described
     * in {@link Camera} should be used to determine a color. The {@link Camera} itself provides the position and angle
     * of the rays which will be shot into the scene and it is the job of implementations only to describe how the rays
     * interact with the scene.
     *
     * @param ray The ray which will be shot into the scene.
     * @return The color that that ray encounters
     */
    public abstract Color getColor(Ray ray);

    /**
     * Returns the closest point of geometry which intersects the ray as described by a {@link RayIntersect}.
     *
     * <p>The geometry used is specified by the {@link #setGeometry(Collection)} call.
     *
     * <p>This will return only the closest intersection in the "forward" direction of the ray as described by its
     * angle.
     *
     * @param ray The ray to find the closest intersection
     * @return The closest intersection of the ray or null if the ray does not intersect any geometry
     */
    protected final RayIntersect findClosest(Ray ray) {
        Geometry closest = null;
        double distance = Double.MAX_VALUE;
        for (Geometry renderable : geometry) {
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

    private class PointIterator implements Iterator<Point> {
        int width;
        int height;
        int currentX;
        int currentY;

        public PointIterator(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public boolean hasNext() {
            return currentY < height;
        }

        @Override
        public Point next() {
            Point point = new Point(currentX++, currentY);
            if (currentX >= width) {
                currentX = 0;
                ++currentY;
            }

            return point;
        }
    }

    protected Ray pointToRay(Point point) {
        Ray cameraRay = lens.pointToRay(point);

        DoubleMatrix angle = rotate.mmul(cameraRay.getAngle());

        return new Ray(cameraRay.getPosition().add(position), angle);
    }

    private void calcRotate() {
        rotate = MatrixUtil.rotate(angle, up, up.add(new DoubleMatrix(new double[] {0, 0, 1})));
    }
}
