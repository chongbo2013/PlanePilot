package biz.brainpowered.plane.comp.entities;

import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import com.badlogic.gdx.graphics.Color;

/**
 * @deprecated not to be used
 */
public class LightEntity extends BaseEntity{
    public static Color YELLOW = new Color(0.5f, 0.5f, 0.1f, 0.5f);
    public float x, y, scale;
    public Color color;

    // TODO: scale should be replaced by width/height

    // TODO: use lights were specified below
    public boolean lightBumps = false;
    public boolean castShadows = false;

    public LightEntity(float x, float y, Color color, float scale)
    {
        super(null);
        new LightEntity(x, y, color, scale, false, false);
    }

    public LightEntity(float x, float y, Color color, float scale, boolean lightBumps, boolean castShadows) {
        super(null);
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