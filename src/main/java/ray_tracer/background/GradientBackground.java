package ray_tracer.background;

import ray_tracer.ColorUtil;
import ray_tracer.geometry.Ray;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by William Martin on 12/25/15.
 */
public class GradientBackground implements Background {
    private final List<Stop> stops = new ArrayList<>();
    private List<Stop> reversed;

    public GradientBackground(Color top, Color bottom) {
        addStop(1.0, top);
        addStop(0.0, bottom);
    }

    public Color getColor(Ray ray) {
        double amount = ray.getAngle().get(1) / 2.0 + 0.5;
        Stop lower = null;
        Stop upper = null;

        for (Stop stop : stops) {
            if (stop.position <= amount) {
                lower = stop;
                break;
            }
        }

        for (Stop stop : reversed) {
            if (stop.position >= amount) {
                upper = stop;
                break;
            }
        }

        double total = upper.position - lower.position;

        return ColorUtil.add(
                ColorUtil.multiply(upper.color, (amount - lower.position) / total),
                ColorUtil.multiply(lower.color, (upper.position - amount) / total)
        );
    }

    public void addStop(double position, Color color) {
        if (position < 0 || position > 1.0) {
            throw new IllegalArgumentException();
        }

        stops.add(new Stop(position, color));
        Collections.sort(stops);
        reversed = new ArrayList<>(stops);
        Collections.reverse(stops);
    }

    private class Stop implements Comparable<Stop> {
        double position;
        Color color;

        Stop(double position, Color color) {
            this.position = position;
            this.color = color;
        }

        @Override
        public int compareTo(Stop o) {
            return (o.position > this.position) ? -1 : 1;
        }
    }
}
