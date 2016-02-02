package ray_tracer.cameras.lenses;

import org.jblas.DoubleMatrix;
import org.jblas.Geometry;
import ray_tracer.geometry.Ray;

import java.awt.*;

/**
 * Created by William Martin III on 1/9/16.
 */
public class FisheyeLens implements Lens {
    private int width;
    private int height;
    private double amount;

    public FisheyeLens(int width, int height, double amount) {
        this.width = width;
        this.height = height;
        this.amount = amount * Math.PI;
    }

    @Override
    public Ray pointToRay(Point point) {
        double x = ((double) point.x) / width * 2.0 - 1.0;
        double y = ((double) point.y) / height * 2.0 - 1.0;

        DoubleMatrix angle = new DoubleMatrix(new double[] {Math.sin(x) * amount, -Math.sin(y) * amount, 1});
        Geometry.normalize(angle);
        DoubleMatrix position = new DoubleMatrix(angle.toArray()).mul(0.25);

        return new Ray(position, angle);

    }
}
