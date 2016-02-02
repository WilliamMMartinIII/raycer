package ray_tracer.cameras;

import ray_tracer.geometry.Ray;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

/**
 * Created by William Martin III on 1/10/16.
 */
public class CompositeCamera extends Camera {
    private List<Camera> cameras;
    private BiFunction<List<Camera>, Point, Camera> rule;

    public CompositeCamera(List<Camera> cameras, BiFunction<java.util.List<Camera>, Point, Camera> rule) {
        this.cameras = cameras;
        this.rule = rule;
    }

    @Override
    public Color getColor(Ray ray) {
        throw new NotImplementedException();
    }
}
