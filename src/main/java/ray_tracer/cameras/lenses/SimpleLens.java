package ray_tracer.cameras.lenses;

import org.jblas.DoubleMatrix;
import ray_tracer.geometry.Ray;

import java.awt.*;

/**
 * Created by William Martin III on 1/9/16.
 */
public class SimpleLens implements Lens {
    private double halfWidth;
    private double halfHeight;
    private double distance;
    private double scale;

    public SimpleLens(int width, int height, double scale, double distance) {
        this.halfWidth = width / 2.0;
        this.halfHeight = height / 2.0;
        this.scale = scale;
        this.distance = distance;
    }

    @Override
    public Ray pointToRay(Point point) {
        double x = point.x;
        double y = point.y;
        double smaller = (halfWidth < halfHeight ? halfWidth : halfHeight);

        x = (x - halfWidth) / smaller * scale;
        y = (y - halfHeight) / smaller * scale;

        DoubleMatrix rotation = new DoubleMatrix(new double[] {x, -y, distance});
        DoubleMatrix center = new DoubleMatrix(new double[] {0, 0, 0});

        return new Ray(center, rotation);
    }
}
