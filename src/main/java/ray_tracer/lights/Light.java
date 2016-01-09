package ray_tracer.lights;

import org.jblas.DoubleMatrix;
import ray_tracer.geometry.Ray;

import java.awt.*;

/**
 * Created by William Martin on 12/25/15.
 */
public abstract class Light {
    protected final LightType type;
    protected final Color color;
    protected final double power;
    protected final Ray ray;

    protected Light(LightType type, Color color, double[] position, double[] angle, double power) {
        this(type, color, new DoubleMatrix(position), new DoubleMatrix(angle), power);
    }

    protected Light(LightType type, Color color, DoubleMatrix position, DoubleMatrix angle, double power) {
        this.type = type;
        this.color = color;
        this.ray = new Ray(position, angle);
        this.power = power;
    }

    public LightType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public DoubleMatrix getPosition() {
        return ray.getPosition();
    }

    public DoubleMatrix getAngle() {
        return ray.getAngle();
    }

    public double getPower() {
        return power;
    }
}
