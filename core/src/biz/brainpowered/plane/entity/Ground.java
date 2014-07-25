package biz.brainpowered.plane.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author sebastian
 * @since 2014/05/30
 * Represents Two Large Sprites that Scroll Down the Screen representing the Ground
 */
public class Ground
{
    Texture texture;
    Sprite ground;
    Sprite ground2;
    float _width;
    float _height;
    float _xVelocity;   // required for horizontal scolling
    float _yVelocity;

    /**
     * Constructor
     * @param groundTexturePath the path to the texture
     * @param initalXVelocity base velocity
     */
    public Ground(String groundTexturePath, float initalXVelocity)
    {
        _width = Gdx.graphics.getWidth();
        _height = Gdx.graphics.getHeight();

        _yVelocity = initalXVelocity;

        texture = new Texture(groundTexturePath);

        ground = new Sprite(texture);
        ground.setSize(_width, _height);

        ground2 = new Sprite(texture);
        ground2.setSize(_width, _height);
        ground2.setPosition(0.0f, ground.getHeight()+ground.getY());
    }

    /**
     * Update Sprite States and Position on Screen
     * @param deltaT frame time
     * @param velocityModifier modify initial velocity
     */
    public void update (float deltaT, float velocityModifier) {
        float deltaYV = -(deltaT * (_yVelocity * velocityModifier));
        if (ground.getY() <= -_height){
            // off screen - move back up
            ground.setPosition(0, ground2.getY() + ground2.getHeight());
        }
        if (ground2.getY() <= -_height){
            // off screen - move back up
            ground2.setPosition(0, ground.getY() + ground.getHeight());
        }
        ground.translateY(Math.round(deltaYV)); // no sub-pixels (that otherwise could lead to artifacts)
        ground2.translateY(Math.round(deltaYV));
    }

    /**
     * Draw Sprites
     * @param batch the sprite batch to draw to
     */
    public void render(SpriteBatch batch)
    {
        ground.draw(batch);
        ground2.draw(batch);
    }

    /**
     * Dispose
     */
    public void dispose()
    {
        texture.dispose();
    }
}
