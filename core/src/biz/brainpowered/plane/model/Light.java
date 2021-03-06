package biz.brainpowered.plane.model;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by sebastian on 2014/07/21.
 * Lights is a Model, used for data purposes in the Renderers
 */
public class Light {
    public static Color YELLOW = new Color(0.5f, 0.5f, 0.1f, 0.5f);
    public float x, y, scale;
    public Color color;

    // TODO: scale should be replaced by width/height

    // TODO: use lights were specified below
    public boolean lightBumps = false;
    public boolean castShadows = false;

    public Light(float x, float y, Color color, float scale)
    {
        new Light(x, y, color, scale, false, false);
    }

    public Light(float x, float y, Color color, float scale, boolean lightBumps, boolean castShadows) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.color = color;
        this.lightBumps = lightBumps;
        this.castShadows = castShadows;
    }

    public static Color randomColor() {
        float intensity = (float)Math.random() * 0.5f + 0.5f;
        return new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), intensity);
    }
}
