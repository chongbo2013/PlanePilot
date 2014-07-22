package biz.brainpowered.plane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;


/**
 * Created by sebastian on 2014/07/18.
 */
public class BulletFactory //implements GenericFactory
{
    Texture texture;
    String texturePath;
    float scale;
    Array<Light> lights;

    public BulletFactory (String texturePath, float scale, Array<Light> lights)
    {
        this.texturePath = texturePath;
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

    public void dispose()
    {
        texture.dispose();
    }

    public Bullet create (float xPos, float yPos, float angle, float velocity)
    {
        return new Bullet(texture, scale, xPos, yPos, angle, velocity, lights);
    }
}
