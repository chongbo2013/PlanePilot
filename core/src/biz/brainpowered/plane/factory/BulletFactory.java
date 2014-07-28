package biz.brainpowered.plane.factory;

import biz.brainpowered.plane.entity.Bullet;
import biz.brainpowered.plane.model.Light;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;


/**
 * Created by sebastian on 2014/07/18.
 */
public class BulletFactory extends EntityFactory
{
    float scale;
    Array<Light> lights; // todo: Light Singleton

    public BulletFactory (String texturePath, float scale, Array<Light> lights)
    {
        super(texturePath);
        this.scale = scale;
        this.lights = lights;
    }

    public boolean init ()
    {
        try
        {
            texture = new Texture(Gdx.files.internal(texturePath));
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    public Bullet create (float xPos, float yPos, float angle, float velocity)
    {
        // need to center the entity by its texture dimensions offset back
        xPos = xPos - (texture.getWidth()/2);
        yPos = yPos - (texture.getHeight()/2);
        return new Bullet(texture, scale, xPos, yPos, angle, velocity, lights);
    }

    public void dispose()
    {
        texture.dispose();
    }
}
