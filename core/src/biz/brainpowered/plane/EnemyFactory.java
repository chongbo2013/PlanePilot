package biz.brainpowered.plane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by sebastian on 2014/07/18.
 */
public class EnemyFactory {

    Texture texture;
    String texturePath;
    float scale;

    public EnemyFactory (String texturePath, float scale)
    {
        this.scale = scale;
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

    public Enemy create (int xPos, int yPos)
    {
        return new Enemy(texture, scale, xPos, yPos);
    }
}
