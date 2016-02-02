package ray_tracer.cameras.lenses;

import ray_tracer.geometry.Ray;

import java.awt.*;

/**
 * An template for describing how to take a point in 2D image-space to 3D camera-space.
 *
 * Created by William Martin III on 1/9/16.
 */
public interface Lens {

    /**
     * Is overridden by Lens implementations to describe the transformation from a 2D {@link Point} in image-space to a
     * 3D {@link Ray} in camera-space.
     *
     * @param point The point in 2D image-space
     * @return The corresponding Ray in 3D world-space
     */
    Ray pointToRay(Point point);
}
