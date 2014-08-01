package biz.brainpowered.plane.render;

import biz.brainpowered.plane.comp.LightComponent;
import biz.brainpowered.plane.comp.interfaces.LightComponentInterface;
import biz.brainpowered.plane.manager.GameManager;
import biz.brainpowered.plane.model.Light;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Created by sebastian on 2014/07/23.
 * todo: get fbo reference - dont return the FBO...
 */
public class SimpleLightsRenderer {
    private FrameBuffer frameBuffer;
    private int width;
    private int height;
    private Array<LightComponentInterface> lights;
    private LightComponentInterface tmpLight;
    private Texture lightTex;

    // pass in light size somwhere
    //private float lightSize = 150f;

    //used to make the light flicker
    public float zAngle;
    public static final float zSpeed = 15.0f;
    public static final float PI2 = 3.1415926535897932384626433832795f * 2.0f;

    private boolean	lightMove = false;
    private boolean lightOscillate = false;

    public SimpleLightsRenderer (String lightTexturePath, Array<LightComponentInterface> lights) {
        // todo: load light texture and vars
        lightTex = GameManager.assetLoader.get(lightTexturePath, "Texture");
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        this.lights = lights;
    }

    public boolean init () {
        try
        {
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    public void resize (int width, int height) {
        // Lighting
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
    }

    public FrameBuffer render (SpriteBatch batch) {
        final float dt = Gdx.graphics.getRawDeltaTime();
        // Occilation angle for Simple Lights
        zAngle += dt * zSpeed;
        while(zAngle > PI2)
            zAngle -= PI2;

        //frameBuffer.begin();
        batch.setShader(null); // passthrough
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f); // clear with white opaque
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        // TODO:  Explosion Class to implement light source
        boolean lightOscillate = true;
        float lightSize = lightOscillate? (100.00f + (25.0f * (float)Math.sin(zAngle)) + 25.0f* MathUtils.random()): 150.0f;
        //lightSize = 150f;
        for (int x = 0; x<lights.size; x++)
        {
            tmpLight = lights.get(x);
            // TODO:/NOTE
            // this method is invalid as all lights now are contained in a single array,
            // also only simple lights need be rendered here
            batch.draw(lightTex,
                    ((LightComponent)tmpLight).x,
                    ((LightComponent)tmpLight).y,
                    lightSize,
                    lightSize);
            // todo: light size to change over time
        }
        batch.end();
        //frameBuffer.end();
        return frameBuffer;
    }
}
