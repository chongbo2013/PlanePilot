package biz.brainpowered.plane;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * LibGDX Port of Bloom Tutorial: http://web.archive.org/web/20140402071520/http://devmaster.net/posts/3100/shader-effects-glow-and-bloom
 */
public class Box2dDemo implements ApplicationListener {

    FPSLogger fpsLogger;
    SpriteBatch batch;
    OrthographicCamera cam;


    // Physics
    World world;
    Box2DDebugRenderer debugRenderer;

    // redo
    Array<Poly> polys;
    Fixture[] balls = new Fixture[1];

    float time;
    float threshold;

    public static float SCALE = 2.0f;
    public static int V_WIDTH = 240;
    public static int V_HEIGHT = 320;
    public static int FPS = 60;
    public static int BALL_COUNT = 150;
    public static int BALL_RADIUS = 2;
    public static int BALL_DENSITY = 1;
    public static int BALL_FRICTION = 0;
    public static float BALL_RESTITUTION = 0.3f;
    public static Vector2 GRAVITY = new Vector2(0f, -10f);
    public static Vector2 NGRAVITY = new Vector2(0, -GRAVITY.y);
    public static float FLIP_RATE = 2.4f;
    public static int SPIKE_THICKNESS = 12;
    public static int SPIKE_EXTENT = 20;


    @Override
    public void create() {
        Box2D.init();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(cam.combined);
        world = new World(new Vector2(0, -9.8f), false);
        debugRenderer = new Box2DDebugRenderer();

        fpsLogger = new FPSLogger();
        time = 0;
        threshold = 0.3f;

//        this.doBlur = true;
//        this.doThreshold = true;

        world = new World(GRAVITY, false);
        polys = new Array<Poly>();
        //this.polys = [];
        this.buildOuter();
        this.addSpike(new Vector2( SPIKE_EXTENT, 0),  1);
        this.addSpike(new Vector2(-SPIKE_EXTENT, 0), -1);
        //this.balls = [];
        for (int i = 0; i < BALL_COUNT; i++) {
            addBall(null);
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugRenderer.render(world, cam.combined);
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
        batch.dispose();

    }

    public void buildOuter() {
        float thickness = 0.1f;
        PolygonShape box = new PolygonShape();
        BodyDef def = new BodyDef();

        def.position.set(new Vector2(V_WIDTH / 2, 0));
        box.setAsBox(thickness / 2, V_HEIGHT / 2);
        world.createBody(def).createFixture(box, 0);

        def.position.set(new Vector2(-V_WIDTH / 2, 0));
        box.setAsBox(thickness / 2, V_HEIGHT / 2);
        world.createBody(def).createFixture(box, 0);

        def.position.set(new Vector2(0, V_HEIGHT / 2));
        box.setAsBox(V_WIDTH / 2, thickness / 2);
        this.world.createBody(def).createFixture(box, 0);

        def.position.set(new Vector2(0, -V_HEIGHT / 2));
        box.setAsBox(V_WIDTH / 2, thickness / 2);
        this.world.createBody(def).createFixture(box, 0);
    }

    public void addSpike(Vector2 pos, float dir) {
        int thickness = SPIKE_THICKNESS;
        BodyDef def = new BodyDef();
        def.position.set(pos);
        Vector2[] verts = {new Vector2(dir * V_HEIGHT / 2 - pos.x, dir *  thickness / 2),
                new Vector2(0, 0),
                new Vector2(dir * V_HEIGHT / 2 - pos.x, dir * -thickness / 2)};
        polys.add(new Poly(pos, verts));
        FixtureDef fix = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.set(verts);
        fix.shape = shape;
        fix.density = (1.0f);
        fix.friction = (0);
        this.world.createBody(def).createFixture(fix);
    }


    public Vector2 random() {
        return new Vector2((float)Math.random() * V_HEIGHT - (V_HEIGHT / 2),
                (float)Math.random() * V_HEIGHT - (V_HEIGHT / 2));
    }

    public void addBall(Vector2 pos) {
        pos = pos!=null ? pos : this.random();
        BodyDef def = new BodyDef();
        def.position.set(pos);
        def.type = BodyDef.BodyType.DynamicBody;
        CircleShape circle = new CircleShape();
        circle.setRadius(BALL_RADIUS);
        FixtureDef mass = new FixtureDef();
        mass.shape = circle;
        mass.density = BALL_DENSITY;
        mass.friction = BALL_FRICTION;
        mass.restitution = BALL_RESTITUTION;
        balls[balls.length-1] = (world.createBody(def).createFixture(mass));
    }

    // Container for Poly objects
    class Poly{
        public final Vector2 pos;
        public final Vector2[] verts;

        Poly(Vector2 p, Vector2[] v) {
            pos = p;
            verts = v;
        }
    }
}