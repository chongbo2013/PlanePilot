package biz.brainpowered.plane.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/24.
 * SpriteEntity implies renderable graphics item;
 * @todo implement Component SpriteEntity System...
 * @deprecated old
 */
public class SpriteEntity { // todo: re-define as SpriteEntity
    protected boolean _dispose;
    protected Sprite _sprite;
    protected Texture _texture;
    protected float _x;
    protected float _y;

    public SpriteEntity(Texture texture) {
        _dispose = false;
        _texture = texture;
        if (_texture != null)
            _sprite = new Sprite( _texture );
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
