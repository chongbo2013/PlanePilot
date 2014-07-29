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
 * LibGDX port of ShaderLesson6, i.e. normal mapping in 2D games.
 * @author davedes
 */
public class ShockwaveDemo implements ApplicationListener {


    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.width = 640;
        cfg.height = 480;
        cfg.resizable = false;
        new LwjglApplication(new ShockwaveDemo(), cfg);
    }

    Texture grass, guy;
    SpriteBatch batch;
    OrthographicCamera cam;
    ShaderProgram shader;

    // Shockwave
    FrameBuffer fbo;
    TextureRegion fboTexRegion;
    float mouseX = 0, mouseY = 0;
            float time = 1000;
    float
            playerTime = 0;

    @Override
    public void create() {
        grass = new Texture(Gdx.files.internal("shaders/shockwave/grass.png"));
        guy = new Texture(Gdx.files.internal("shaders/shockwave/guy.png"));

        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(Gdx.files.internal("shaders/shockwave/shockVert.glsl").readString(), Gdx.files.internal("shaders/shockwave/shockFrag.glsl").readString());
        //ensure it compiled
        if (!shader.isCompiled())
            throw new GdxRuntimeException("Could not compile shader: "+shader.getLog());
        //print any warnings
        if (shader.getLog().length()!=0)
            System.out.println(shader.getLog());

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
//        //clear the screen
        //batch.setShader(null);
        fbo.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setShader(null);
        //batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.draw(grass, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 2, 2);

        float anim = (float)Math.sin(playerTime+=0.02) * 100f;

        //draw some player sprites ...
        batch.draw(guy, 105, 50, guy.getWidth()*2, guy.getHeight()*2);
        batch.draw(guy, 255, 350, guy.getWidth()*2, guy.getHeight()*2);
        batch.draw(guy, 300+anim, 150, guy.getWidth()*2, guy.getHeight()*2);
        batch.end();
        fbo.end();



        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setShader(shader);// = shockwaveShader;
        batch.begin();
        shader.setUniformf("shockParams", 10.0f, 0.7f, 0.1f);
        shader.setUniformf("center", mouseX, mouseY);
        //System.out.println("center: "+mouseX+" - "+mouseY);
        shader.setUniformf("time", time += dt);
        batch.draw(fboTexRegion, 0, 0);
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
        shader.dispose();
    }
}