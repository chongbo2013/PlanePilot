package biz.brainpowered.plane.render;

import biz.brainpowered.plane.model.Light;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector4f;

import static biz.brainpowered.plane.render.ShaderUtil.createShader;

/**
 * Created by sebastian on 2014/07/23.
 * BumpRenderer: compose all aspects of Running a Bump Shading Renderer
 * TODO: refactor into a Custom SpriteBatcher
 * status: current non functional
 * RnD: passing data (float vec3 arrays) into the shader programs
 */
public class BumpRenderer {

    //our constants...
    public static final float DEFAULT_LIGHT_Z = 0.025f;
    public static final float AMBIENT_INTENSITY = 1.0f;
    public static final float LIGHT_INTENSITY = 0.5f;

    public static final Vector3 LIGHT_POS = new Vector3(0f, 0f, DEFAULT_LIGHT_Z);

    //Light RGB and intensity (alpha)
    public static final Vector3 LIGHT_COLOR = new Vector3(1f, 1f, 1f);

    //Ambient RGB and intensity (alpha)
    public static final Vector3 AMBIENT_COLOR = new Vector3(0.6f, 0.6f, 1f);

    //Attenuation coefficients for light falloff
    public static final Vector3 FALLOFF = new Vector3(.6f, 4f, 30f);

    private ShaderProgram shaderProgram;
    // End Bump Shader

    // Instance Vars
    String fragmentShaderString;
    String fragmentShaderPath;

    Texture planeNormals; // to be implemented
    Texture planeTex;

    Light tmpLight;


    //SpriteBatch batch;
    //OrthographicCamera cam;// = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    //cam.setToOrtho(false);

    public BumpRenderer(String path, OrthographicCamera cam)
    {
        //this.cam = cam;
        fragmentShaderPath = path;
        fragmentShaderString = "";

    }

    public boolean init () {
        try
        {

            planeNormals = new Texture(Gdx.files.internal("airplane/PLANE_8_N_NRM.png"));

            fragmentShaderString = Gdx.files.internal(fragmentShaderPath).readString();
            shaderProgram = createShader(ShaderUtil.defaultVertexShader, fragmentShaderString);

            //setup default uniforms
            shaderProgram.begin();

            //our normal map
            shaderProgram.setUniformi("u_normals", 1); //GL_TEXTURE1

            //light/ambient colors
            //LibGDX doesn't have Vector4 class at the moment, so we pass them individually...
            shaderProgram.setUniformf("LightColor", LIGHT_COLOR.x, LIGHT_COLOR.y, LIGHT_COLOR.z, LIGHT_INTENSITY);
            shaderProgram.setUniformf("AmbientColor", AMBIENT_COLOR.x, AMBIENT_COLOR.y, AMBIENT_COLOR.z, AMBIENT_INTENSITY);
            shaderProgram.setUniformf("Falloff", FALLOFF);


            //LibGDX likes us to end the shader program
            shaderProgram.end();

//            batch = new SpriteBatch(1000, shaderProgram);
//            batch.setShader(shaderProgram);

//            cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//            cam.setToOrtho(false);

            //handle mouse wheel
//            Gdx.input.setInputProcessor(new InputAdapter() {
//                public boolean scrolled(int delta) {
//                    //LibGDX mouse wheel is inverted compared to lwjgl-basics
//                    LIGHT_POS.z = Math.max(0f, LIGHT_POS.z - (delta * 0.005f));
//                    System.out.println("New light Z: "+LIGHT_POS.z);
//                    return true;
//                }
//            });


            // Shadow Map Setup
            planeTex = new Texture(Gdx.files.internal("airplane/PLANE_8_N.png"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // TODO: what this needs is: the lights data - the iteratable items with normal maps -? this should all be passed back to an FBO and blended downstream
    public void render ( SpriteBatch batch, Array<Light> lights ) {

        //BUMP
        //update light position, normalized to screen resolution

        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Gdx.gl.glClearColor(0.25f,0.25f,0.25f,1f);

//        //reset light Z
//        if (Gdx.input.isTouched()) {
//            LIGHT_POS.z = DEFAULT_LIGHT_Z;
//            System.out.println("New light Z: "+LIGHT_POS.z);
//        }

        //shader will now be in use...
//System.out.println("zangle: "+(((zAngle * appWidth )/ appWidth) * 40));
//System.out.println("zangle: "+(100.00f + (25.0f * (float)Math.sin(zAngle)) + 25.0f* MathUtils.random()));
        //update light position, normalized to screen resolution
//        float gx = plane.currentSprite.getX();
//        float gy = plane.currentSprite.getY();

//        LIGHT_POS.x = gx;
//        LIGHT_POS.y = gy;
//
        //float[] numbers = new float[]();
        float[] myIntArray = new float[(lights.size +1) * 3];
        int ex = 0;
        int bumpLights = 0;
        for(int i =0; i<lights.size; i++)
        {
            tmpLight = lights.get(i);
            if (tmpLight.lightBumps) {

                myIntArray[ex] = tmpLight.x  / (float)Display.getWidth();
                ex++;
                myIntArray[ex] = tmpLight.y  / (float)Display.getHeight();
                ex++;
                myIntArray[ex] = DEFAULT_LIGHT_Z;
                ex++;
                bumpLights++;
            }
        }

//        myIntArray[ex] = 0.5f ;
//        ex++;
//        myIntArray[ex] = 0.5f;
//        ex++;
//        myIntArray[ex] = DEFAULT_LIGHT_Z;
//        ex++;

        shaderProgram.begin();
//
//        //send a Vector4f to GLSL
        shaderProgram.setUniformi("lightCount", bumpLights );
//        shaderProgram.setUniformf("LightPos", LIGHT_POS);
//        //bumpRenderer.setUniform3fv("LightPoss", myIntArray, 0, 1);
        shaderProgram.setUniform3fv("LightPoss", myIntArray, 0, ex);

        shaderProgram.end();
//       // bumpRenderer.setUniform3fv("LightPoss", new float[] {gx, gy, DEFAULT_LIGHT_Z}, 0, 1);
//
//        batch.setShader(shaderProgram);
//        batch.begin();
//        //bind normal map to texture unit 1
//        // or render normals to texture, then bind
//        planeNormals.bind(1);
//
//        //bind diffuse color to texture unit 0
//        //important that we specify 0 otherwise we'll still be bound to glActiveTexture(GL_TEXTURE1)
//        planeTex.bind(0);
//
//        //draw the texture unit 0 with our shader effect applied
//        batch.draw(planeTex, 50, 50);
//        //batch.draw(planeTex, gx, gy);
//
//        batch.end();
        // END BUMP
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.gl.glClearColor(0.25f,0.25f,0.25f,1f);

        //reset light Z
        if (Gdx.input.isTouched()) {
            LIGHT_POS.z = DEFAULT_LIGHT_Z;
            System.out.println("New light Z: "+LIGHT_POS.z);
        }
        batch.setShader(shaderProgram);
        batch.begin();

        //shader will now be in use...

        //update light position, normalized to screen resolution
        float x = Mouse.getX() / (float)Display.getWidth();
        float y = Mouse.getY() / (float)Display.getHeight();

        LIGHT_POS.x = x;
        LIGHT_POS.y = y;

        //send a Vector4f to GLSL
        shaderProgram.setUniformf("LightPos", LIGHT_POS);

        //bind normal map to texture unit 1
        planeNormals.bind(1);

        //bind diffuse color to texture unit 0
        //important that we specify 0 otherwise we'll still be bound to glActiveTexture(GL_TEXTURE1)
        planeTex.bind(0);

        //draw the texture unit 0 with our shader effect applied
        batch.draw(planeTex, Display.getWidth()/2, Display.getHeight()/2);

        batch.end();
    }

    public void resize (int width, int height)
    {

//        cam.setToOrtho(false, width, height);
//        batch.setProjectionMatrix(cam.combined);

        shaderProgram.begin();
        shaderProgram.setUniformf("Resolution", width, height);
        shaderProgram.end();
    }
}
