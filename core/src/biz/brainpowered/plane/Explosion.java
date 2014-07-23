package biz.brainpowered.plane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created with IntelliJ IDEA.
 * User: sebastian
 * Date: 2014/05/29
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class Explosion {

    Animation animation;
    float _x;
    float _y;
    float elapsedTime = 0.0f;
    Light light;

    // TODO: LightManager Singleton
    Array<Light> lights;

    // pass in Texture Region from Factory
    public Explosion(Animation animation, float xPos, float yPos, Array<Light> lights)
    {
        _x = xPos;
        _y = yPos;

        float centerOriginX = animation.getKeyFrame(0).getRegionWidth() / 2;
        float centerOriginY = animation.getKeyFrame(0).getRegionHeight() / 2;

        this.lights = lights;
        light = new Light(_x + centerOriginX, _y + centerOriginY, Light.YELLOW, 1.5f, false, true);
        lights.add(light);
        this.animation = animation;
    }

    public void dispose()
    {
        lights.removeValue(light, true);
    }

    public void render(SpriteBatch batch)
    {
        light.scale = (float)Math.sin(elapsedTime * (4.2 - elapsedTime)) * 1.0f; //Pulse Scale Value
        batch.draw(animation.getKeyFrame(elapsedTime, false), _x, _y);
        elapsedTime += Gdx.graphics.getDeltaTime();
    }
}
