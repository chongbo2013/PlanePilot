package biz.brainpowered.plane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

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

    float scale;

    public Bullet(Texture texture, float scale, float xPos, float yPos, float angle, float velocity)
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
    }

    public void setDispose()
    {
        _dispose = true;
    }

    public void dispose()
    {
        // none
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
    }

    // collision detection
    public Rectangle getRectangle()
    {
        return sprite.getBoundingRectangle();
    }
}
