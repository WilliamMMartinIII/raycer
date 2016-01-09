package ray_tracer.material;

import org.jblas.DoubleMatrix;
import ray_tracer.geometry.RayIntersect;

import java.awt.geom.Point2D;

/**
 * Created by William Martin on 12/27/15.
 */
public class SphereMap extends TextureMapping {
    private static final DoubleMatrix UP = new DoubleMatrix(new double[] {0, 0, 1});

    public SphereMap(Texture texture) {
        super(texture);
    }

    @Override
    public Point2D.Double map(RayIntersect intersect) {
        DoubleMatrix normal = intersect.getNormal();

        double x = Math.asin(normal.get(0)) / Math.PI + 0.5;
        double y = Math.asin(normal.get(1)) / Math.PI + 0.5;
        x *= texture.getWidth() * 2.0;
        y *= texture.getHeight() * 2.0;

        return new Point2D.Double(x, y);
    }

    @Override
    public DoubleMatrix getUp() {
        return UP;
    }
}
