package biz.brainpowered.plane;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by sebastian on 2014/07/21.
 */
class Light {
    static Color YELLOW = new Color(0.5f, 0.5f, 0.1f, 0.5f);
    float x, y, scale;
    Color color;

    // TODO: scale should be replaced by width/height

    // TODO: use lights were specified below
    boolean lightBumps = false;
    boolean castShadows = false;

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

    static Color randomColor() {
        float intensity = (float)Math.random() * 0.5f + 0.5f;
        return new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), intensity);
    }
}
