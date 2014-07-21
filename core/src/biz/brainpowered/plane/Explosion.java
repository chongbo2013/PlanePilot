package biz.brainpowered.plane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

    // pass in Texture Region from Factory
    public Explosion(Animation animation, float xPos, float yPos)
    {
        _x = xPos;
        _y = yPos;

        this.animation = animation;
    }

    public void dispose()
    {

    }

    public void render(SpriteBatch batch)
    {
        batch.draw(animation.getKeyFrame(elapsedTime, false), _x, _y);
        elapsedTime += Gdx.graphics.getDeltaTime();
    }
}
