package ray_tracer.background;

import ray_tracer.geometry.Ray;

import java.awt.*;

/**
 * Created by William Martin on 12/25/15.
 */
public class Horizon implements Background {
    private final Color sky;
    private final Color ground;

    public Horizon(Color sky, Color ground) {
        this.sky = sky;
        this.ground = ground;

    }
    public Color getColor(Ray ray) {
        if (ray.getAngle().get(1) > 0) {
            return sky;
        } else {
            return ground;
        }
    }
}
