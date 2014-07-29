package biz.brainpowered.plane.desktop;

import biz.brainpowered.plane.comp.ComponentGroupManager;
import biz.brainpowered.plane.comp.EntityManager;
import biz.brainpowered.plane.comp.GraphicsComponentGroup;
import biz.brainpowered.plane.comp.InputComponentGroup;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/28.
 */
public class ECSDemo implements ApplicationListener {

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.width = 640;
        cfg.height = 480;
        cfg.resizable = false;
        new LwjglApplication(new ECSDemo(), cfg);
    }

    Camera camera;
    SpriteBatch batch;
    int appWidth;
    int appHeight;
    EntityManager entityManager;

    @Override
    public void create() {
        appWidth = Gdx.graphics.getWidth();
        appHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(appWidth, appHeight);
        camera.update();
        batch = new SpriteBatch( 1000, null);
        batch.setShader(null); // passthrough
        entityManager = new EntityManager();

        // Register Component Groups
        ComponentGroupManager.getInstance().registerComponentGroup(ComponentGroupManager.GRAPHICS, GraphicsComponentGroup.getInstance());
        ComponentGroupManager.getInstance().registerComponentGroup(ComponentGroupManager.INPUT, InputComponentGroup.getInstance());

        // todo: will use SceneManagement and Scripting to do basic workflows and scene progression;
        entityManager.createEntityEntity( null, "airplane/PLANE_8_N.png"); // returns a Reference-value
        entityManager.createPlayerEntity( null, "airplane/PLANE_8_N.png"); // returns a Reference-value
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f); // clear with white opaque
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        GraphicsComponentGroup.getInstance().updateGroup(batch);
        batch.end();

        InputComponentGroup.getInstance().updateGroup( null );
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
