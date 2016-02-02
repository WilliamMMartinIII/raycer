package ray_tracer.cameras.lenses;

import ray_tracer.geometry.Ray;

import java.awt.*;

/**
 * Created by William Martin III on 1/10/16.
 */
public class OrthographicLens implements Lens {
    private int halfWidth;
    private int halfHeight;
    private double scaleX;
    private double scaleY;

    public OrthographicLens(int width, int height, double scaleX, double scaleY) {
        this.halfWidth = width / 2;
        this.halfHeight = height / 2;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public Ray pointToRay(Point point) {
        double[] position = new double[] {(point.x - halfWidth) * scaleX, -(point.y - halfHeight) * scaleY, 0};
        return new Ray(position, new double[] {0, 0, 1});
    }
}
