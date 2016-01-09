package ray_tracer.background;

import ray_tracer.geometry.Ray;

import java.awt.*;

/**
 * Created by William Martin on 12/25/15.
 */
public interface Background {
    Color getColor(Ray ray);
}
