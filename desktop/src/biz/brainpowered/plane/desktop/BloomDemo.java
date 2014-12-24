package biz.brainpowered.plane.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * LibGDX Port of Bloom Tutorial: http://web.archive.org/web/20140402071520/http://devmaster.net/posts/3100/shader-effects-glow-and-bloom
 *
 * TODO: All is still to be done
 */
public class BloomDemo implements ApplicationListener {


    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.width = 640;
        cfg.height = 480;
        cfg.resizable = false;
        new LwjglApplication(new BloomDemo(), cfg);
    }

    Texture grass, guy;
    SpriteBatch batch;
    OrthographicCamera cam;
    ShaderProgram blurShader;

    // Shockwave
    FrameBuffer fbo;
    TextureRegion fboTexRegion;
    float mouseX = 0, mouseY = 0;
    float time = 1000;
    float playerTime = 0;

    @Override
    public void create() {
        grass = new Texture(Gdx.files.internal("shaders/shockwave/grass.png"));
        guy = new Texture(Gdx.files.internal("shaders/shockwave/guy.png"));

        ShaderProgram.pedantic = false;
        blurShader = new ShaderProgram(Gdx.files.internal("shaders/blur/gaussianVert.glsl").readString(), Gdx.files.internal("shaders/blur/gaussianFrag.glsl").readString());

        //ensure it compiled
        if (!blurShader.isCompiled())
            throw new GdxRuntimeException("Could not compile shader: "+blurShader.getLog());
        //print any warnings
        if (blurShader.getLog().length()!=0)
            System.out.println(blurShader.getLog());

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        fboTexRegion = new TextureRegion(fbo.getColorBufferTexture());
        fboTexRegion.flip(false, true);

        batch = new SpriteBatch(1000);

        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.setToOrtho(false);

        //handle mouse wheel
        Gdx.input.setInputProcessor(new InputAdapter() {

            public boolean touchDown (int screenX, int screenY, int pointer, int button) {
                //System.out.println("screenx: "+screenX);
                time = 0;
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

        // Render to Frame Buffer
        //fbo.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.setShader(blurShader);
        batch.begin();
        blurShader.setUniformf("dir", 1.0f, 0.0f); //direction of blur; nil for now
        blurShader.setUniformf("resolution", 1024); //size of FBO texture
        blurShader.setUniformf("radius", 2.0f); //radius of blur
        batch.draw(grass, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 2, 2);

        // shader grabs first texture - blurs that only

        //draw some player sprites ...
        batch.draw(guy, 105, 50, guy.getWidth()*2, guy.getHeight()*2);
        batch.draw(guy, 255, 350, guy.getWidth()*2, guy.getHeight()*2);
        batch.draw(guy, 300+anim, 150, guy.getWidth()*2, guy.getHeight()*2);
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