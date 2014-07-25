package biz.brainpowered.plane.render;

import biz.brainpowered.plane.entity.Enemy;
import biz.brainpowered.plane.model.Light;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.lwjgl.Sys;

import java.util.ArrayList;

import static biz.brainpowered.plane.render.ShaderUtil.createShader;

/**
 * Created by sebastian on 2014/07/24.
 */
public class LightMapRenderer {
    //read our shader files
    private String shadowMapPixelShaderPath;
    private String shadowRenderPixelShaderPath;

    private String finalPixelShaderPath;
    private String finalPixelShader;
    private ShaderProgram finalShader;

    //values passed to the shader
    public static final float ambientIntensity = .99f;
    public static final Vector3 ambientColor = new Vector3(0.99f, 0.99f, 0.99f);

    // Shadow Casting
    private int lightSize = 256;
    private float upScale = 1.0f; //for example; try lightSize=128, upScale=1.5f

    TextureRegion shadowMap1D; //1 dimensional shadow map
    TextureRegion occluders;   //occluder map
    TextureRegion finalLightMap;   //occluder map

    FrameBuffer shadowMapFBO;
    FrameBuffer occludersFBO;

    FrameBuffer finalLightMapFBO;
    Texture finalLightMapTex;

    Texture casterSprites;
    Texture light;

    ShaderProgram shadowMapShader, shadowRenderShader;

    boolean additive = true;
    boolean softShadows = true;

    Integer appWidth;
    Integer appHeight;

    private Light tmpLight;

    OrthographicCamera cam;

    public LightMapRenderer (String fragmentShaderPath, String shadowMapFragmentShaderPath, String shadowRenderFragmentShaderPath, Array<Light> lights) {
        finalPixelShaderPath = fragmentShaderPath;
        shadowMapPixelShaderPath = shadowMapFragmentShaderPath;
        shadowRenderPixelShaderPath = shadowRenderFragmentShaderPath;

        // viewport
        appWidth = Gdx.graphics.getWidth();
        appHeight = Gdx.graphics.getHeight();
        // todo: load light texture and vars
    }

    public boolean init () {
        try
        {


            cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            cam.setToOrtho(false);

            finalPixelShader =  Gdx.files.internal(finalPixelShaderPath).readString();

            // TODO: rename var to something un-ambiguous (ie finalShadowMapShader / blend FBO with Diffuse)
            finalShader = createShader(ShaderUtil.defaultVertexShader, finalPixelShader);
            finalShader.begin();
            finalShader.setUniformi("u_lightmap", 1); //  1 value refers to the FBO-to-ShaderProgramTextureUnit  binding
            finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y, ambientColor.z, ambientIntensity);
            finalShader.end();

            // renders occluders to 1D shadow map
            shadowMapShader = createShader(ShaderUtil.defaultVertexShader, Gdx.files.internal(shadowMapPixelShaderPath).readString());
            // samples 1D shadow map to create the blurred soft shadow
            shadowRenderShader = createShader(ShaderUtil.defaultVertexShader, Gdx.files.internal(shadowRenderPixelShaderPath).readString());

            //the occluders
            casterSprites = new Texture("explosion19.png");
            //the light sprite
            light = new Texture("light.png");

            //build frame buffers
            occludersFBO = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, lightSize, false);
            occluders = new TextureRegion(occludersFBO.getColorBufferTexture());
            occluders.flip(false, true);

            //our 1D shadow map, lightSize x 1 pixels, no depth
            shadowMapFBO = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, 1, false);
            Texture shadowMapTex = shadowMapFBO.getColorBufferTexture();

            //use linear filtering and repeat wrap mode when sampling
            shadowMapTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            shadowMapTex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

            //for debugging only; in order to render the 1D shadow map FBO to screen
            shadowMap1D = new TextureRegion(shadowMapTex);
            shadowMap1D.flip(false, true);

            finalLightMapFBO = new FrameBuffer(Pixmap.Format.RGBA8888, appWidth, appHeight, false);
            finalLightMapTex = finalLightMapFBO.getColorBufferTexture();

            finalLightMap = new TextureRegion(finalLightMapTex);
            finalLightMap.flip(false, true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void resize (int width, int height, SpriteBatch batch) {
        //Shading (2d)
        cam.setToOrtho(false, width, height);
        batch.setProjectionMatrix(cam.combined);

        finalShader.begin();
        finalShader.setUniformf("resolution", width, height);
        finalShader.end();
    }

    /**
     * Renders the LightMaps with Projected Shadows
     * Note: .end() should be called at end of render to cleanup Lightmap FBO
     * @param lights
     */
    public void renderLights (SpriteBatch batch, Array<Light> lights, ArrayList<Enemy> occlusionEntityCollection) {
        /**
         /* Note on Shadow Rendering
         /* Step 1: Render all Lights/Shadows into finalLightMapFBO (via a set of shaders)
         /* Step 2: Blend Diffuse and FBO textures (via third and 'final' shader)
         /**/

        // TODO: Move Shadow Rendering into Separate Class (or Sprite Batcher)
        // Step 1: Shadow Rendering
        // all stored in the "finalLightMapFBO"
        //clear frame
        Gdx.gl.glClearColor(0.25f,0.25f,0.25f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (additive)
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (int i=0; i<lights.size; i++)
        {
            tmpLight = lights.get(i);
            // only render lights that cast shadows
            if(tmpLight.castShadows)
                renderLight(tmpLight, batch, occlusionEntityCollection); // todo: write two or three separate sprite batchers to prevent program switching within a single batch
        }
        if (additive)
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        // End Shadow Rendering
    }

    public void render (SpriteBatch batch) {
        final float dt = Gdx.graphics.getRawDeltaTime();

        // TODO: Move final Rendering into Final Rendering Class (takes in several LightMap textures, then blends and outputs)
        // Step 2: Blending
        // Render 'Diffuse' Everything Else (and Put Shadow Maps ontop)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setShader(finalShader);
        batch.begin();

        // TODO: if lightmap was generated - then bind to unit and use final renderer - otherwise switch to original pipeline
        finalLightMapFBO.getColorBufferTexture().bind(1); //this is important! bind the FBO to the 2nd texture unit
        light.bind(0); //we force the binding of a texture on first texture unit to avoid artefacts
        //this is because our default and ambiant shader dont use multi texturing...
        //you can basically bind anything, it doesnt matter
    }

    public void end () {
        // Empty out the Light Map once per frame (thanks!)
        finalLightMapFBO.begin();
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        finalLightMapFBO.end();
    }

    // Renders Lights that cast shadows - todo: Specify a Separate Shadow Cast Rendering Class
    private void renderLight(Light o, SpriteBatch batch, ArrayList<Enemy> occlusionEntityCollection)
    {
        float mx = o.x;
        float my = o.y;
        float lightSize = this.lightSize;//o.scale *

        //STEP 1. render light region to occluder FBO

        //bind the occluder FBO
        occludersFBO.begin();

        //clear the FBO to WHITE/ TRANSPARENT
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set the orthographic camera to the size of our FBO
        cam.setToOrtho(false, occludersFBO.getWidth(), occludersFBO.getHeight());

        //translate camera so that light is in the center
        cam.translate(mx - lightSize/2f, my - lightSize/2f);

        //update camera matrices
        cam.update();

        //set up our batch for the occluder pass
        batch.setProjectionMatrix(cam.combined);
        batch.setShader(null); //use default shader
        batch.begin();

        // ... draw any sprites that will cast shadows here ... //
        // TODO: [Optimise] Only Render Entities that Intersect with Current CAMera Projection/Position/LightSize
        for (int x = 0; x < occlusionEntityCollection.size(); x++) {
            occlusionEntityCollection.get(x).render(batch);
        }

        //end the batch before unbinding the FBO
        batch.end();

        //unbind the FBO
        occludersFBO.end();

        //STEP 2. build a 1D shadow map from occlude FBO

        //bind shadow map
        shadowMapFBO.begin();

        //clear it
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set our shadow map shader
        batch.setShader(shadowMapShader);
        batch.begin();
        shadowMapShader.setUniformf("resolution", lightSize, lightSize);
        shadowMapShader.setUniformf("upScale", upScale);

        //reset our projection matrix to the FBO size
        cam.setToOrtho(false, shadowMapFBO.getWidth(), shadowMapFBO.getHeight());
        batch.setProjectionMatrix(cam.combined);

        //draw the occluders texture to our 1D shadow map FBO
        batch.draw(occluders.getTexture(), 0, 0, lightSize, shadowMapFBO.getHeight());

        //flush batch
        batch.end();

        //unbind shadow map FBO
        shadowMapFBO.end();

        //STEP 3. render the blurred shadows

        finalLightMapFBO.begin();
        // DONT CLEAR HERE - only after each frame
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //reset projection matrix to screen !!!
        cam.setToOrtho(false);
        batch.setProjectionMatrix(cam.combined);

        //set the shader which actually draws the light/shadow
        batch.setShader(shadowRenderShader);
        batch.begin();

        shadowRenderShader.setUniformf("resolution", lightSize, lightSize);
        shadowRenderShader.setUniformf("softShadows", softShadows ? 1f : 0f);

        //set color to light
        batch.setColor(o.color);
        float finalSize = lightSize * o.scale;

        //draw centered on light position
        batch.draw(shadowMap1D.getTexture(), mx-finalSize/2f, my-finalSize/2f, finalSize, finalSize);

        //flush the batch before swapping shaders into the FrameBuffer
        batch.end();
        finalLightMapFBO.end();

        //reset color
        batch.setColor(Color.WHITE);
    }
}
