package biz.brainpowered.plane.desktop;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Sebnic on 2014/12/26.
 */
public class Box2DLightsDemo implements ApplicationListener {
    OrthographicCamera orthographicCamera;
    World world;
    Box2DDebugRenderer renderer;
    float width, height;
    FPSLogger fpsLogger;
    Body circleBody;
    Body groundBody;
    RayHandler rayHandler;

    public static void main(String[] args) {

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "PlanePilot";
        config.width = 1280;
        config.height = 720;
        new LwjglApplication(new Box2DLightsDemo(), config);
    }

    @Override
    public void create() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        orthographicCamera = new OrthographicCamera(width, height);
        orthographicCamera.position.set(width/2f, height/2f, 0);
        orthographicCamera.update();

        world = new World(new Vector2(0, -9.8f), false);
        renderer = new Box2DDebugRenderer();

        fpsLogger = new FPSLogger();

        BodyDef circleDef = new BodyDef();
        circleDef.type = BodyDef.BodyType.DynamicBody;
        circleDef.position.set(width/2f, height/2f);

        circleBody = world.createBody(circleDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(3f); // tennis bass size is perfect

        FixtureDef circleFixture = new FixtureDef();
        circleFixture.shape = circleShape;
        circleFixture.density = 0.4f;
        circleFixture.friction = 0.2f;
        circleFixture.restitution = 0.8f;

        circleBody.createFixture(circleFixture);

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0f, 3f);

        groundBody = world.createBody(groundBodyDef);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(orthographicCamera.viewportWidth * 2, 3f);

        groundBody.createFixture(groundShape, 0f);

        rayHandler = new RayHandler(world);
        rayHandler.setCombinedMatrix(orthographicCamera.combined);

        new PointLight(rayHandler, 2000, Color.BLUE, 1000, width/2f, height/2f);
        new PointLight(rayHandler, 2000, Color.RED, 1000, width/2f+250, height/2f-250);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render(world, orthographicCamera.combined);
        rayHandler.updateAndRender();
        world.step(1/60f, 6, 2);

        fpsLogger.log();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
