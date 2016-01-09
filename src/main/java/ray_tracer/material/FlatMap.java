package ray_tracer.material;

import org.jblas.DoubleMatrix;
import ray_tracer.geometry.RayIntersect;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Created by William Martin on 12/27/15.
 */
public class FlatMap extends TextureMapping {
    private final DoubleMatrix x;
    private final DoubleMatrix y;
    private final DoubleMatrix origin;

    public FlatMap(DoubleMatrix x, DoubleMatrix y, DoubleMatrix origin, Texture texture) {
        super(texture);
        this.x = x;
        this.y = y;
        this.origin = origin;
    }

    @Override
    public Point2D.Double map(RayIntersect intersect) {
        DoubleMatrix point = intersect.getReflection().getPosition();
        point = point.sub(origin);
        double x = this.x.project(point) * texture.getWidth();
        double y = this.y.project(point) * texture.getHeight();
        return new Point2D.Double(x, y);
    }

    @Override
    public DoubleMatrix getUp() {
        return y;
    }
}
