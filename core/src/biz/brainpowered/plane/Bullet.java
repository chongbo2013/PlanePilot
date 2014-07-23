package biz.brainpowered.plane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created with IntelliJ IDEA.
 * User: sebastian
 * Date: 2014/05/30
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class Bullet
{
    Sprite sprite;

    float _x;
    float _y;
    float _a;
    float _v;

    boolean _dispose = false;

    Light light;
    Array<Light> lights;

    // TODO: Lights to be managed independently (as a singleton)
    float scale;

    public Bullet(Texture texture, float scale, float xPos, float yPos, float angle, float velocity, Array<Light>lights)
    {
        sprite = new Sprite(texture);
        sprite.scale(scale);
        sprite.setOriginCenter();

        _x = xPos;
        _y = yPos;
        _a = angle;
        _v = velocity;
        
        sprite.setRotation(_a - 90f);
        sprite.setPosition(_x, _y);

        this.lights = lights;
        light = new Light(_x, _y, Light.randomColor(), 0.5f, false, true);
        lights.add(light);
        // lights.removeValue(light, true);
    }

    public void setDispose()
    {
        _dispose = true;
    }

    public void dispose(){
        lights.removeValue(light, true);
    }

    // TODO; possibly implement sub-render updates for higher accuracy under low framerates

    public void render(SpriteBatch batch)
    {
        // fuck you angles for now
//        _x = (_v * Gdx.graphics.getDeltaTime()) * (float)Math.cos(_a);
//        _y =  (_v * Gdx.graphics.getDeltaTime())  * (float)Math.sin(_a);

        //_x += (_v * Gdx.graphics.getDeltaTime());
        _y += (_v * Gdx.graphics.getDeltaTime());

        sprite.setPosition(_x, _y);
        sprite.draw(batch);

        light.x = _x;
        light.y = _y;
    }

    // collision detection
    public Rectangle getRectangle()
    {
        return sprite.getBoundingRectangle();
    }
}
