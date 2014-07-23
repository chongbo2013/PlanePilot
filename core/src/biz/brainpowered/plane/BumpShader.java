package biz.brainpowered.plane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import org.lwjgl.util.vector.Vector4f;

import static biz.brainpowered.plane.ShaderUtil.createShader;

/**
 * Created by sebastian on 2014/07/23.
 * BumpShader: compose all aspects of Running a Bump Shading Renderer
 * TODO: refactor into a Custom SpriteBatcher
 * status: current non functional
 * RnD: passing data (float vec3 arrays) into the shader programs
 */
public class BumpShader {

    // BUMP Shading
    public static final float DEFAULT_LIGHT_Z = 0.125f;
    public static final float AMBIENT_INTENSITY = 0.5f;
    public static final float LIGHT_INTENSITY = 2f;

    public static final Vector3 LIGHT_POS = new Vector3(0f,0f,DEFAULT_LIGHT_Z);
    //Light RGB and intensity (alpha)
    public static final Vector4f LIGHT_COLOR = new Vector4f(1f, 0.9f, 0.8f, 1f);

    //Ambient RGB and intensity (alpha)
    public static final Vector4f AMBIENT_COLOR = new Vector4f(0.9f, 0.9f, 1f, 0.2f);

    //Attenuation coefficients for light falloff
    public static final Vector3 FALLOFF = new Vector3(.4f, 3f, 20f);

    private ShaderProgram bumpShader;
    // End Bump Shader

    public BumpShader () {

    }

    public boolean init () {
        try
        {
            bumpShader = createShader(ShaderUtil.defaultVertexShader, Gdx.files.internal("shaders/bumpFrag.glsl").readString());

            //setup default uniforms
            bumpShader.begin();

            //our normal map
            bumpShader.setUniformi("u_normals", 1); //GL_TEXTURE1

            //light/ambient colors
            //LibGDX doesn't have Vector4 class at the moment, so we pass them individually...
            bumpShader.setUniformf("LightColor", LIGHT_COLOR.x, LIGHT_COLOR.y, LIGHT_COLOR.z, LIGHT_INTENSITY);
            bumpShader.setUniformf("AmbientColor", AMBIENT_COLOR.x, AMBIENT_COLOR.y, AMBIENT_COLOR.z, AMBIENT_INTENSITY);
            bumpShader.setUniformf("Falloff", FALLOFF);

            //LibGDX likes us to end the shader program
            bumpShader.end();
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    // TODO: what this needs is: the lights data - the iteratable items with normal maps -? this should all be passed back to an FBO and blended downstream
    public void render (SpriteBatch batch) {

        // BUMP
//        //update light position, normalized to screen resolution
//
//        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        //Gdx.gl.glClearColor(0.25f,0.25f,0.25f,1f);
//
////        //reset light Z
////        if (Gdx.input.isTouched()) {
////            LIGHT_POS.z = DEFAULT_LIGHT_Z;
////            System.out.println("New light Z: "+LIGHT_POS.z);
////        }
//
//        //shader will now be in use...
////System.out.println("zangle: "+(((zAngle * appWidth )/ appWidth) * 40));
////System.out.println("zangle: "+(100.00f + (25.0f * (float)Math.sin(zAngle)) + 25.0f* MathUtils.random()));
//        //update light position, normalized to screen resolution
//        float gx = plane.currentSprite.getX();
//        float gy = plane.currentSprite.getY();
//
//        LIGHT_POS.x = gx;
//        LIGHT_POS.y = gy;
//
//
//        //float[] numbers = new float[]();
//        float[] myIntArray = new float[(lights.size +1) * 3];
//        int ex = 0;
//        for(int i =0; i<lights.size; i++)
//        {
//            myIntArray[ex] = lights.get(i).x;
//            ex++;
//            myIntArray[ex] = lights.get(i).y;
//            ex++;
//            myIntArray[ex] = DEFAULT_LIGHT_Z;
//            ex++;
//        }
//
//        myIntArray[ex] = gx ;
//        ex++;
//        myIntArray[ex] = gy;
//        ex++;
//        myIntArray[ex] = DEFAULT_LIGHT_Z;
//        ex++;
//
//        //send a Vector4f to GLSL
//        bumpShader.setUniformi("lightCount", lights.size + 1);
//        bumpShader.setUniformf("LightPos", LIGHT_POS);
//        //bumpShader.setUniform3fv("LightPoss", myIntArray, 0, 1);
//        //bumpShader.setUniform3fv("LightPoss", myIntArray, 0, ex);
//       // bumpShader.setUniform3fv("LightPoss", new float[] {gx, gy, DEFAULT_LIGHT_Z}, 0, 1);
//
//        batch.setShader(bumpShader);
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
    }

    public void resize (int width, int height)
    {
        bumpShader.begin();
        bumpShader.setUniformf("Resolution", width, height);
        bumpShader.end();
    }
}
