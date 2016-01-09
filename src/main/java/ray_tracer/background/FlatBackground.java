package ray_tracer.background;

import ray_tracer.geometry.Ray;

import java.awt.*;

/**
 * Created by William Martin on 12/25/15.
 */
public class FlatBackground implements Background {
    Color color;

    public FlatBackground(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor(Ray ray) {
        return color;
    }
}
