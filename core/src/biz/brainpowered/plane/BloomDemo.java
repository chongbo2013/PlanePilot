package biz.brainpowered.plane;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * LibGDX Port of Bloom Tutorial: http://web.archive.org/web/20140402071520/http://devmaster.net/posts/3100/shader-effects-glow-and-bloom
 */
public class BloomDemo implements ApplicationListener {

    Texture grass, guy;
    Sprite sprite, blurSprite; // assist in displaying the final texture flipped correctly (LibGDX bug?)
    SpriteBatch batch;
    OrthographicCamera cam;
    BitmapFont fps;

    // SHADERS
    FrameBuffer fbo;
    FrameBuffer blurFbo, blur2Fbo;
    FrameBuffer thresholdFbo;
    TextureRegion fboTexRegion, grassTextureRegion, origTextureRegion, intermediateTextureRegion;
    ShaderProgram thresholdShader;
    ShaderProgram blurShader;
    ShaderProgram glowShader;

    float mouseX = 0, mouseY = 0;
    float time = 1000;
    float playerTime = 0;
    boolean process = true;
    int blendMode = 0;

    @Override
    public void create() {
        // TEXTURES
        grass = new Texture(Gdx.files.internal("godrays2.jpg"));
        grassTextureRegion = new TextureRegion(grass);
        grassTextureRegion.flip(false, true);
        guy = new Texture(Gdx.files.internal("shaders/shockwave/guy.png"));

        // SHADERS
        ShaderProgram.pedantic = false;
        thresholdShader = new ShaderProgram(Gdx.files.internal("shaders/bloom/image.vs").readString(), Gdx.files.internal("shaders/betterbloom/threshold.fragment").readString());
        blurShader = new ShaderProgram(Gdx.files.internal("shaders/bloom/image.vs").readString(), Gdx.files.internal("shaders/betterbloom/9tapgaussian.fragment.glsl").readString());
        glowShader = new ShaderProgram(Gdx.files.internal("shaders/bloom/image.vs").readString(), Gdx.files.internal("shaders/bloom/combine.fs").readString());

        //ensure it compiled
        if (!thresholdShader.isCompiled())
            throw new GdxRuntimeException("Could not compile shader: "+thresholdShader.getLog());
        //print any warnings
        if (thresholdShader.getLog().length()!=0)
            System.out.println(thresholdShader.getLog());

        //ensure it compiled
        if (!blurShader.isCompiled())
            throw new GdxRuntimeException("Could not compile shader: "+blurShader.getLog());
        //print any warnings
        if (blurShader.getLog().length()!=0)
            System.out.println(blurShader.getLog());

        if (!glowShader.isCompiled())
            throw new GdxRuntimeException("Could not compile shader: "+glowShader.getLog());
        //print any warnings
        if (glowShader.getLog().length()!=0)
            System.out.println(glowShader.getLog());

        // FRAME BUFFERS
        blur2Fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4, true);
        blurFbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4, true);
        intermediateTextureRegion = new TextureRegion(blurFbo.getColorBufferTexture());
        intermediateTextureRegion.flip(false, true);

        thresholdFbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        fboTexRegion = new TextureRegion(thresholdFbo.getColorBufferTexture());
        fboTexRegion.flip(false, true);
        blurSprite = new Sprite(fboTexRegion);

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        origTextureRegion = new TextureRegion(fbo.getColorBufferTexture());
        origTextureRegion.flip(false, true);
        sprite = new Sprite(origTextureRegion);

        // NEEDED
        batch = new SpriteBatch(1000);
        fps = new BitmapFont();
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Set Shader Defaults
        float gamma = 0.0f;
        thresholdShader.begin();
        thresholdShader.setUniformf("threshold", gamma);
        thresholdShader.setUniformf("thresholdInvTx", 1f / (1 - gamma));
        thresholdShader.end();

        blurShader.begin();
        blurShader.setUniformf("dir", 0f, 0f); //direction of blur; nil for now
        blurShader.setUniformf("resolution", (float)fbo.getWidth()); //size of FBO texture
        blurShader.setUniformf("radius", 0.0f); //radius of blur
        blurShader.end();

        glowShader.begin();
        glowShader.setUniformi("u_texture0", 0); //bind u_texture0 to GL_TEXTURE0
        glowShader.setUniformi("u_texture1", 1); //bind u_texture1 to GL_TEXTURE1
        glowShader.setUniformi("BlendMode", 0);
        glowShader.end();

        //handle mouse wheel
        Gdx.input.setInputProcessor(new InputAdapter() {

            public boolean keyDown (int keyCode) {
                if (keyCode == Input.Keys.NUM_0){
                    blendMode = 0;
                }

                if (keyCode == Input.Keys.NUM_1){
                    blendMode = 1;
                }

                if (keyCode == Input.Keys.NUM_2){
                    blendMode = 2;
                }

                if (keyCode == Input.Keys.NUM_3){
                    blendMode = 3;
                }

                if (keyCode == Input.Keys.BACKSPACE){
                    process = false;
                    System.out.println("backspace");
                }
                return true;
            }

            public boolean keyUp (int keyCode) {
                if (keyCode == Input.Keys.BACKSPACE){
                    process = true;
                }
                return true;
            }

            public boolean mouseMoved (int screenX, int screenY) {
                time = 0;
                mouseX = (float)screenX / (float)Gdx.graphics.getWidth();
                mouseY = 1.0f - (float)screenY / (float)Gdx.graphics.getHeight();
                //System.out.println("MouseX: "+mouseX);
                //System.out.println("MouseY: "+mouseY);
                return true;
            }

            public boolean touchDragged (int screenX, int screenY, int pointer) {
                mouseX = (float)screenX / (float)Gdx.graphics.getWidth();
                mouseY = 1.0f - (float)screenY / (float)Gdx.graphics.getHeight();
                return true;
            }

        });
    }

    @Override
    public void resize(int width, int height) {
        cam.setToOrtho(false, width, height);
        batch.setProjectionMatrix(cam.combined);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        float anim = (float)Math.sin(playerTime+=0.02) * 100f;

        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Draw Initial Scene
        fbo.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setShader(null);
        batch.begin();
        batch.draw(grass, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(guy, 105, 50, guy.getWidth()*2, guy.getHeight()*2);
        batch.draw(guy, 255, 350, guy.getWidth()*2, guy.getHeight()*2);
        batch.draw(guy, 300+anim, 150, guy.getWidth()*2, guy.getHeight()*2);
        batch.end();
        fbo.end();

        // Get Brightness Threshold
        thresholdFbo.begin();
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setShader(thresholdShader);
        batch.begin();
        thresholdShader.setUniformf("threshold", mouseX);
        thresholdShader.setUniformf("thresholdInvTx", 1f / (1 - mouseX));
        batch.draw(sprite,0,0,fbo.getWidth(), fbo.getHeight());
        batch.end();
        thresholdFbo.end();

        // Horizontal Blur
        blurFbo.begin();
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setShader(blurShader);
        batch.begin();
        blurShader.setUniformf("dir", 1f, 0f); //x-axis
        blurShader.setUniformf("radius", (mouseY * 5f));
        blurShader.setUniformf("resolution", blurFbo.getWidth());
        batch.draw(thresholdFbo.getColorBufferTexture(),0,0,fbo.getWidth(), fbo.getHeight());
        batch.end();
        blurFbo.end();

        // Vertical Blur
        blur2Fbo.begin();
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setShader(blurShader);
        batch.begin();
        blurShader.setUniformf("dir", 0f, 1f); //x-axis
        blurShader.setUniformf("resolution", blurFbo.getHeight());
        batch.draw(blurFbo.getColorBufferTexture(),0,0,fbo.getWidth(), fbo.getHeight());
        batch.end();
        blur2Fbo.end();

        // Re-use old FBO and Combine Original and Final Blur
        thresholdFbo.begin();
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setShader(glowShader);
        batch.begin();
        glowShader.setUniformi("BlendMode", blendMode);
        blur2Fbo.getColorBufferTexture().bind(1);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        batch.draw(sprite,0,0,fbo.getWidth(), fbo.getHeight());
        batch.end();
        thresholdFbo.end();
//
//        // Draw Final FBO Mapped/Flipped onto Sprite
        batch.setShader(null);
        batch.begin();
        if(process == false){
            batch.draw(sprite, 0, 0);
        }else{
            batch.draw(blurSprite, 0, 0,fbo.getWidth(), fbo.getHeight());
        }

        //  draw FPS
        fps.draw(batch, String.valueOf(Gdx.graphics.getFramesPerSecond()), 5, Gdx.graphics.getHeight()-15);

        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        grass.dispose();
        guy.dispose();
        blurShader.dispose();
    }
}