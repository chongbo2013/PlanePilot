package biz.brainpowered.plane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/18.
 */
public class Debug
{
    // Debug
    BitmapFont font;

    final float yOffset = 20;
    final float xOffset = 20;

    private float appWidth;
    private float appHeight;
    private float currentOffset = 0f;

    public Debug ()
    {
        appWidth = Gdx.graphics.getWidth();
        appHeight = Gdx.graphics.getHeight();

        // should init with a font
        font = new BitmapFont();
        font.setColor(Color.WHITE);
    }

    public void draw(SpriteBatch sb, String string)
    {
        currentOffset += yOffset; // increment Y-Offset each time draw is called
        font.draw(sb, string, xOffset, appHeight - currentOffset);
    }

    public void reset(){
        currentOffset = 0;
    }

    public void dispose()
    {
        font.dispose();
    }
}
