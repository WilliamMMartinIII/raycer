package ray_tracer.material;

import org.jblas.DoubleMatrix;
import org.jblas.Geometry;
import ray_tracer.ColorUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by William Martin on 12/30/15.
 */
public class Texture {
    private final BufferedImage diffuse;
    private final BufferedImage normal;
    private final BufferedImage specular;
    private final BufferedImage reflection;
    private final int width;
    private final int height;

    private Texture(Builder builder) {
        diffuse = builder.diffuse;
        normal = builder.normal;
        specular = builder.specular;
        reflection = builder.reflection;

        if (builder.width == null) {
            width = diffuse.getWidth();
        } else {
            width = builder.width;
        }
        if (builder.height == null) {
            height = diffuse.getHeight();
        } else {
            height = builder.height;
        }
    }

    public Color getDiffuse(double x, double y) {
        return getPixel(diffuse, x, y);
    }

    public DoubleMatrix getNormal(double x, double y) {
        if (normal == null) {
            return new DoubleMatrix(new double[] {0, 1, 0});
        }

        Color result = getPixel(normal, x, y);

        double scale = ColorUtil.MAX;
        DoubleMatrix normal = new DoubleMatrix(new double[] {
                -(result.getRed() / scale - 0.5) * 2,
                -(result.getGreen() / scale - 0.5) * 2,
                (result.getBlue() / scale - 0.5)
        });
        Geometry.normalize(normal);
        return normal;
    }

    public double getSpecular(double x, double y) {
        if (specular == null) {
            return 0;
        }
        Color result = getPixel(specular, x, y);
        return (double) (result.getRed() + result.getGreen() + result.getBlue()) / (ColorUtil.MAX * 3.0);
    }

    public double getReflection(double x, double y) {
        if (reflection == null) {
            return 0;
        }
        Color result = getPixel(reflection, x, y);
        return (double) (result.getRed() + result.getGreen() + result.getBlue()) / (ColorUtil.MAX * 3);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getDiffuse() {
        return diffuse;
    }

    public BufferedImage getNormal() {
        return normal;
    }

    public BufferedImage getSpecular() {
        return specular;
    }

    public BufferedImage getReflection() {
        return reflection;
    }

    public static class Builder {
        private BufferedImage diffuse = null;
        private BufferedImage normal = null;
        private BufferedImage specular = null;
        private BufferedImage reflection = null;

        Integer width = null;
        Integer height = null;

        public Builder(BufferedImage diffuse) {
            if (diffuse == null) {
                throw new IllegalArgumentException("Texture must have diffuse color.");
            }
            this.diffuse = diffuse;
        }

        public Builder size(Integer width, Integer height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder diffuse(BufferedImage diffuse) {
            this.diffuse = diffuse;
            return this;
        }

        public Builder normal(BufferedImage normal) {
            this.normal = normal;
            return this;
        }

        public Builder specular(BufferedImage specular) {
            this.specular = specular;
            return this;
        }

        public Builder reflection(BufferedImage reflection) {
            this.reflection = reflection;
            return this;
        }

        public Texture build() {
            return new Texture(this);
        }
    }

    protected Color getPixel(BufferedImage image, double x, double y) {
        x = x % image.getWidth();
        y = y % image.getHeight();

        x = (x < 0.0) ? x + image.getWidth() : x;
        y = (y < 0.0) ? y + image.getHeight() : y;
        if (x == image.getWidth()) {
            x -= 1;
        }
        if (y == image.getHeight()) {
            y -= 1;
        }
        int left = (int) Math.floor(x);
        int lower = (int) Math.floor(y);
        int right = (left + 1) % image.getWidth();
        int upper = (lower + 1) % image.getHeight();

        Color lowerLeft = new Color(image.getRGB(left, lower));
        Color lowerRight = new Color(image.getRGB(right, lower));
        Color upperLeft = new Color(image.getRGB(left, upper));
        Color upperRight = new Color(image.getRGB(right, upper));


        Color down = ColorUtil.add(
                ColorUtil.multiply(lowerLeft, x - left),
                ColorUtil.multiply(lowerRight, left + 1 - x)
        );

        Color up = ColorUtil.add(
                ColorUtil.multiply(upperLeft, x - left),
                ColorUtil.multiply(upperRight, left + 1 - x)
        );

        return ColorUtil.add(
                ColorUtil.multiply(down, y - lower),
                ColorUtil.multiply(up, lower + 1 - y)
        );
    }
}
