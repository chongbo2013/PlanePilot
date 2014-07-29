package biz.brainpowered.plane;

import biz.brainpowered.plane.comp.ComponentGroupManager;
import biz.brainpowered.plane.comp.EntityManager;
import biz.brainpowered.plane.comp.GraphicsComponentGroup;
import biz.brainpowered.plane.comp.InputComponentGroup;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/29.
 */
public class ecsEntryPoint implements ApplicationListener {

    // todo: move to GameManager

    // GameManager/Model Singleton Properties here
    int appWidth;
    int appHeight;
    Camera camera;
    SpriteBatch batch;
    EntityManager entityManager;

    @Override
    public void create() {
        // init game manager
        // Init Globals/GameManager Props
        appWidth = Gdx.graphics.getWidth();
        appHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(appWidth, appHeight);
        camera.update();
        batch = new SpriteBatch( 1000, null);
        batch.setShader(null);
        entityManager = new EntityManager();
        // entity manager
        // scene manager
        // asset manager

        // TODO: begin loading assets from splash scene, when done, execute default script


        /**
         * each ComponentGroups manage their own Type of Components (Graphics, Input, etc.)
         * Each Component also Keeps a Reference to their owning/parent Entity
         * Note: each Entity ALSO keeps a reference to the Component (this is for convenience at first, however GC is one Step farther away)
         */

        ComponentGroupManager.getInstance().registerComponentGroup(ComponentGroupManager.GRAPHICS, GraphicsComponentGroup.getInstance());
        ComponentGroupManager.getInstance().registerComponentGroup(ComponentGroupManager.INPUT, InputComponentGroup.getInstance());

        // todo: will use SceneManagement and Scripting to Control Entities
        entityManager.createGroundEntity( null, "airplane/airPlanesBackground.png", 80.0f);
        //entityManager.createEntityEntity( null, "airplane/PLANE_8_N.png"); // returns a Reference-value
        entityManager.createPlaneEntity( null, "airplane/PLANE_8_N.png"); // returns a Reference-value


        /**
         * A Few Notes:
         * Entities are simply Component Collections
         * Components are proccessed in batches through the ComponentGroups (Graphics, Input, Audio, AI, Physics, etc)
         */
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        // Component Group Manager tasks

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f); // clear with white opaque
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        GraphicsComponentGroup.getInstance().updateGroup(batch); // Benefit of ECS is here
        batch.end();

        InputComponentGroup.getInstance().updateGroup( null ); // Benefit of ECS is here

        /**
         * One major benefit illustrated here is that much of the boilerplate and micromanagement is abstracted away
         */

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
