package biz.brainpowered.plane.render;

import biz.brainpowered.plane.entity.BumpLitSpriteEntity;
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
    public static final float DEFAULT_LIGHT_Z = 0.0125f;
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

    BumpLitSpriteEntity blse;

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

            // Shadow Map Setup
            planeTex = new Texture(Gdx.files.internal("airplane/PLANE_8_N.png"));

            // DEMO Code:
            blse = new BumpLitSpriteEntity(planeTex, planeNormals, true);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // TODO: what this needs is: the lights data - the iteratable items with normal maps -? this should all be passed back to an FBO and blended downstream
    public void render ( SpriteBatch batch, Array<Light> lights, BumpLitSpriteEntity blse ) {

        //BUMP
        // TODO: Lots of Space for Optimisation
        // Construct Array of Vector3 Floats to pass into GLSL Program
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

        shaderProgram.begin();
        shaderProgram.setUniformi("lightCount", bumpLights );
        shaderProgram.setUniform3fv("LightsPos", myIntArray, 0, ex);
        shaderProgram.end();

        //reset light Z
//        if (Gdx.input.isTouched()) {
//            LIGHT_POS.z = DEFAULT_LIGHT_Z;
//            System.out.println("New light Z: "+LIGHT_POS.z);
//        }
        batch.setShader(shaderProgram);
        batch.begin();

        //shader will now be in use...
//        //bind normal map to texture unit 1
//        planeNormals.bind(1);
//
//        //bind diffuse color to texture unit 0
//        //important that we specify 0 otherwise we'll still be bound to glActiveTexture(GL_TEXTURE1)
//        planeTex.bind(0);

        // TODO: Entity System to Kick in soon... stay tuned
        // Note: the BumpLitSpriteEntity knows how to assign textures to the Texture Units for the Shader to Operate
        blse.render(batch);

        //draw the texture unit 0 with our shader effect applied
        //batch.draw(planeTex, Display.getWidth()/2, Display.getHeight()/2);

        batch.end();
    }

    public void resize (int width, int height)
    {
        shaderProgram.begin();
        shaderProgram.setUniformf("Resolution", width, height);
        shaderProgram.end();
    }
}
