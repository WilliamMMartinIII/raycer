package ray_tracer.material;

import java.awt.*;

/**
 * Created by William Martin on 12/24/15.
 */
public class Material {
    private final Color ambient;
    private final Color diffuse;
    private final Color specular;
    private final Color reflect;

    private final double refractIndex;
    private final double refractAmount;
    private final double transparency;
    private final double reflectAmount;

    private final boolean emitter;

    private Material(Builder builder) {
        ambient = builder.ambient;
        diffuse = builder.diffuse;
        specular = builder.specular;
        reflect = builder.reflect;

        transparency = builder.transparency;
        refractIndex = builder.refractIndex;
        refractAmount = builder.refractAmount;
        reflectAmount = builder.reflectAmount;

        emitter = builder.emitter;
    }

    public Color getAmbient() {
        return ambient;
    }

    public Color getDiffuse() {
        return diffuse;
    }

    public Color getSpecular() {
        return specular;
    }

    public Color getReflect() {
        return reflect;
    }

    public double getTransparency() {
        return transparency;
    }

    public double getRefractIndex() {
        return refractIndex;
    }

    public double getRefractAmount() {
        return refractAmount;
    }

    public double getReflectAmount() {
        return reflectAmount;
    }

    public boolean getEmitter() {
        return emitter;
    }

    public static class Builder {
        private Color ambient = Color.black;
        private Color diffuse = Color.black;
        private Color specular = Color.black;
        private Color reflect = Color.black;

        private double transparency = 1;
        private double refractIndex = 1;
        private double refractAmount = 0;
        private double reflectAmount = 0;

        private boolean emitter = false;

        public Builder() { }

        public Builder ambient(Color ambient) {
            this.ambient = ambient;
            return this;
        }

        public Builder diffuse(Color diffuse) {
            this.diffuse = diffuse;
            return this;
        }

        public Builder specular(Color specular) {
            this.specular = specular;
            return this;
        }

        public Builder reflect(Color reflect) {
            this.reflect = reflect;
            return this;
        }

        public Builder color(Color color) {
            this.ambient = color;
            this.diffuse = color;
            this.specular = color;
            this.reflect = color;
            return this;
        }

        public Builder transparency(double transparency) {
            this.transparency = transparency;
            return this;
        }

        public Builder refractIndex(double refractIndex) {
            this.refractIndex = refractIndex;
            return this;
        }

        public Builder refractAmount(double refractAmount) {
            this.refractAmount = refractAmount;
            return this;
        }

        public Builder reflectAmount(double reflectAmount) {
            this.reflectAmount = reflectAmount;
            return this;
        }

        public Builder emitter(boolean emitter) {
            this.emitter = emitter;
            return this;
        }

        public Material build() {
            return new Material(this);
        }
    }
}
