package biz.brainpowered.plane.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/24.
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
