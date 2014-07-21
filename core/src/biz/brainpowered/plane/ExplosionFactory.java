package biz.brainpowered.plane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by sebastian on 2014/07/18.
 */
public class ExplosionFactory {

    String texturePath;
    Texture texture;
    TextureRegion[][] initialTextureSplit;
    TextureRegion[] ordered;
    Animation animation;
    int columns;
    int rows;
    float frameInterval;

    public ExplosionFactory(String texturePath, int columns, int rows, float interval)
    {
        this.texturePath = texturePath;
        this.columns = columns;
        this.rows = rows;
        frameInterval = interval;
    }

    public boolean init()
    {
        try
        {
            texture = new Texture(Gdx.files.internal(texturePath));
            initialTextureSplit = TextureRegion.split(texture, texture.getWidth() / 5, texture.getHeight() / 5);
            ordered = new TextureRegion[columns * rows];
            int index = 0;
            for (int c = 0; c < columns; c++) {
                for (int r = 0; r < rows; r++) {
                    ordered[index++] = initialTextureSplit[c][r];
                }
            }
            animation = new Animation(frameInterval, ordered);
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

    public Explosion create(float x, float y)
    {
        return new Explosion(animation, x, y);
    }
}
