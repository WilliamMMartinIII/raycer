package ray_tracer.cameras;

import ray_tracer.geometry.Ray;
import ray_tracer.geometry.RayIntersect;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by William Martin on 12/25/15.
 */
public class FlatCamera extends Camera{

    @Override
    public Color getColor(Ray ray) {
        RayIntersect intersect = findClosest(ray);

        if (intersect != null) {
            return intersect.getGeometry().getMaterial().getDiffuse();
        } else {
            return background.getColor(ray);
        }
    }
}
