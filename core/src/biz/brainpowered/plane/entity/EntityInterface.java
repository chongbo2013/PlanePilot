package biz.brainpowered.plane.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @deprecated a basic sprite entity
 */
public interface EntityInterface {

    public void setDispose ( boolean dispose );

    public boolean getDispose ();

    public void setSprite ( Sprite sprite );

    public Sprite getSprite ();

    public void setX ( float x );

    public float getX ();

    public void setY ( float y );

    public float getY ();

    public void render (SpriteBatch batch);
}
