package biz.brainpowered.plane.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by sebastian on 2014/07/24.
 */
public class EntityFactory implements FactoryInterface {
    Texture texture;
    String texturePath;

    public EntityFactory ( String texturePath ) {
        this.texturePath = texturePath;
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
}
