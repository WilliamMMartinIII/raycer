package ray_tracer.geometry;

import org.jblas.DoubleMatrix;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by William Martin on 12/24/15.
 */
public class SphereTest {

    @Test
    public void shouldIntersectRay() {
        Sphere sphere = new Sphere(0, 0, 0, 10, null, null);
        Ray ray = new Ray(new double[]{0, 0, 20}, new double[]{0, 0, -1});

        RayIntersect intersect = sphere.getIntersect(ray);

        assertNotNull(intersect);
        assertEquals(new DoubleMatrix(new double[]{0, 0, 10}), intersect.getReflection().getPosition());
        assertEquals(new DoubleMatrix(new double[]{0, 0, 1}), intersect.getReflection().getAngle());
    }

    @Test
    public void shouldIntersectRayStartingOnSurface() {
        Sphere sphere = new Sphere(0, 0, 0, 10, null, null);
        Ray ray = new Ray(new double[]{0, 0, 10}, new double[]{0, 0, -1});

        RayIntersect intersect = sphere.getIntersect(ray);

        assertNotNull(intersect);
        assertEquals(new DoubleMatrix(new double[]{0, 0, 10}), intersect.getReflection().getPosition());
        assertEquals(new DoubleMatrix(new double[]{0, 0, 1}), intersect.getReflection().getAngle());
    }

    @Test
    @Ignore
    public void shouldIntersectRayStartingInSphere() {
        Sphere sphere = new Sphere(0, 0, 0, 10, null, null);
        Ray ray = new Ray(new double[]{0, 0, 0}, new double[]{0, 0, 1});

        RayIntersect intersect = sphere.getIntersect(ray);

        assertNotNull(intersect);
        assertEquals(new DoubleMatrix(new double[]{0, 0, 10}), intersect.getReflection().getPosition());
        assertEquals(new DoubleMatrix(new double[]{0, 0, -1}), intersect.getReflection().getAngle());
    }

    @Test
    public void shouldNotIntersectRay() {
        Sphere sphere = new Sphere(0, 0, 0, 10, null, null);
        Ray ray = new Ray(new double[]{0, 0, 0}, new double[]{0, 0, 1});

        RayIntersect intersect = sphere.getIntersect(ray);

        assertNull(intersect);
    }
}
