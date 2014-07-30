package biz.brainpowered.plane;

import biz.brainpowered.plane.comp.ComponentGroupManager;
import biz.brainpowered.plane.comp.EntityManager;
import biz.brainpowered.plane.comp.GraphicsComponentGroup;
import biz.brainpowered.plane.comp.InputComponentGroup;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by sebastian on 2014/07/29.
 */
public class ecsEntryPoint extends Game {


    private int rendCount; //** render count **//
    private long startTime; //** time app started **//
    private long endTime; //** time app ended **//

    // todo: move to GameManager

    // GameManager/Model Singleton Properties here
    int appWidth;
    int appHeight;
    Camera camera;
    SpriteBatch batch;
    EntityManager entityManager;

    @Override
    public void create() {

        // TODO: init this into GameManager thanks
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

        setScreen(new SplashScreen(this)); //** start SplashScreen, with Game parameter **//

        /**
         * each ComponentGroups manage their own Type of Components (Graphics, Input, etc.)
         * Each Component also Keeps a Reference to their owning/parent Entity
         * Note: each Entity ALSO keeps a reference to the Component (this is for convenience at first, however GC is one Step farther away)
         */

//        ComponentGroupManager.getInstance().registerComponentGroup(ComponentGroupManager.GRAPHICS, GraphicsComponentGroup.getInstance());
//        ComponentGroupManager.getInstance().registerComponentGroup(ComponentGroupManager.INPUT, InputComponentGroup.getInstance());
//
//        // todo: will use SceneManagement and Scripting to Control Entities
//        entityManager.createGroundEntity( "Ground1", "airplane/airPlanesBackground.png", 80.0f);
//        //entityManager.createEntityEntity( null, "airplane/PLANE_8_N.png"); // returns a Reference-value
//        entityManager.createPlaneEntity( "Player1", "airplane/PLANE_8_N.png"); // returns a Reference-value
//        entityManager.createGroundEntity( "Ground1", "airplane/airPlanesBackground.png", 80.0f);
//
//        entityManager.createGroundEntity( "Ground1", "airplane/airPlanesBackground.png", 80.0f);


        /**
         * A Few Notes:
         * Entities are simply Component Collections
         * Components are proccessed in batches through the ComponentGroups (Graphics, Input, Audio, AI, Physics, etc)
         */
    }

    @Override
    public void resize(int width, int height) {
        // when we rotate the screeen?
    }

    @Override
    public void render() {
        // Component Group Manager tasks

        super.render();
        rendCount++; //** another render - inc count **//

    }

    @Override
    public void pause() {
        // activate pause mode in Game screen
            // stop sounds, animations, etc
        // if in lobby/menu, no need for pause mode
    }

    @Override
    public void resume() {
        // would need to reactivate the shaders
    }

    @Override
    public void dispose() {
        // disposal entry point
        Gdx.app.log("my Splash Game", "App rendered " + rendCount + " times");
        Gdx.app.log("my Splash Game", "App ended");
        endTime = TimeUtils.millis();
        Gdx.app.log("my Splash Game", "App running for " + (endTime-startTime)/1000 + " seconds.");

    }
}
