package biz.brainpowered.plane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by sebastian on 2014/07/18.
 */
public class BulletFactory //implements GenericFactory
{
    Texture texture;
    String texturePath;
    float scale;

    public BulletFactory (String texturePath, float scale)
    {
        this.texturePath = texturePath;
        this.scale = scale;
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
        return new Bullet(texture, scale, xPos, yPos, angle, velocity );
    }
}
