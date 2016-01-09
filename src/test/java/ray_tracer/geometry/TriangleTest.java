package ray_tracer.geometry;

import org.jblas.DoubleMatrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import ray_tracer.TestUtil;

/**
 * Created by William Martin on 12/24/15.
 */
public class TriangleTest {

    @Test
    public void shouldIntersectRay() {
        Triangle triangle = new Triangle(
                new double[]{1, -1, 0},
                new double[]{-1, -1, 0},
                new double[]{0, 1, 0},
                null,
                null);
        Ray ray = new Ray(new double[]{0, 0, -10}, new double[]{0, 0, 1});

        RayIntersect intersect = triangle.getIntersect(ray);

        assertNotNull(intersect);
        assertEquals(new DoubleMatrix(new double[]{0, 0, 0}), intersect.getReflection().getPosition());
        assertEquals(new DoubleMatrix(new double[]{0.0, 0.0, -1}), intersect.getReflection().getAngle());
    }

    @Test
    public void shouldNotIntersectRayIfTriangleFacingAway() {
        Triangle triangle = new Triangle(
                new double[]{-1, -1, 0},
                new double[]{1, -1, 0},
                new double[]{0, 1, 0},
                null,
                null);
        Ray ray = new Ray(new double[]{0, 0, -10}, new double[]{0, 0, 1});

        RayIntersect intersect = triangle.getIntersect(ray);

        assertNull(intersect);
    }

    @Test
    public void shouldIntersectOnSurface() {
        Triangle triangle = new Triangle(
                new double[]{1, -1, 0},
                new double[]{-1, -1, 0},
                new double[]{0, 1, 0},
                null,
                null);
        Ray ray = new Ray(new double[]{0, 0, 0}, new double[]{0, 0, 1});

        RayIntersect intersect = triangle.getIntersect(ray);

        assertNotNull(intersect);
        assertEquals(new DoubleMatrix(new double[]{0, 0, 0}), intersect.getReflection().getPosition());
        assertEquals(new DoubleMatrix(new double[]{0, 0, -1}), intersect.getReflection().getAngle());
    }

    @Test
    public void shouldReflectAtAngle() {
        Triangle triangle = new Triangle(
                new double[]{1, -1, -1},
                new double[]{-1, -1, -1},
                new double[]{0, 1, 1},
                null,
                null);
        Ray ray = new Ray(new double[]{0, 0, -10}, new double[]{0, 0, 1});

        RayIntersect intersect = triangle.getIntersect(ray);

        assertNotNull(intersect);
        TestUtil.assertEquals(new DoubleMatrix(new double[]{0, 0, 0}), intersect.getReflection().getPosition());
        TestUtil.assertEquals(new DoubleMatrix(new double[]{0, 1, 0}), intersect.getReflection().getAngle());
    }
}
