package biz.brainpowered.plane;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by sebastian on 2014/07/21.
 */

class Light {

    static Color YELLOW = new Color(0.5f, 0.5f, 0.1f, 0.5f);
    float x, y, scale;
    Color color;

    public Light(float x, float y, Color color, float scale) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.color = color;
    }

    static Color randomColor() {
        float intensity = (float)Math.random() * 0.5f + 0.5f;
        return new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), intensity);
    }

}
