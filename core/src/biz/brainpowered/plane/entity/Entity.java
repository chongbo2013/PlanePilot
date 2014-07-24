package biz.brainpowered.plane.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/24.
 * Entity implies renderable graphics item;
 * @todo implement Component Entity System...
 */
public class Entity {
    protected boolean _dispose;
    protected Sprite _sprite;
    protected float _x;
    protected float _y;

    public Entity () {
        _dispose = false;
        _sprite = null;
    }

    public void setDispose ( boolean dispose ) {
        _dispose = dispose;
    }

    public boolean getDispose () {
        return _dispose;
    }

    public void setSprite ( Sprite sprite ) {
        _sprite = sprite;
    }

    public Sprite getSprite () {
        return _sprite;
    }

    public void setX ( float x ) {
        _x = x;
    }

    public float getX () {
        return _x;
    }

    public void setY ( float y ) {
        _y = y;
    }

    public float getY () {
        return _y;
    }

    public void render (SpriteBatch batch) {
        // override me
        _sprite.draw(batch);
    }
}
