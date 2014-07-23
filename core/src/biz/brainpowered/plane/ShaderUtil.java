package biz.brainpowered.plane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by sebastian on 2014/07/23.
 * Loads Default Shaders as string and makes a CreateShader util
 */
public class ShaderUtil {

    public static String defaultVertexShader;
    public static String getDefaultFragmentShader;

    public static boolean init () {

        try
        {
            ShaderProgram.pedantic = false;
            defaultVertexShader = Gdx.files.internal("shaders/default.vertex").readString();
            getDefaultFragmentShader = Gdx.files.internal("shaders/default.fragment").readString();
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }
    /**
     * Compiles a new instance of the default shader for this batch and returns it. If compilation
     * was unsuccessful, GdxRuntimeException will be thrown.
     * @return the default shader
     */
    public static ShaderProgram createShader(String vert, String frag) {
        ShaderProgram prog = new ShaderProgram(vert, frag);
        if (!prog.isCompiled())
            throw new GdxRuntimeException("could not compile shader: " + prog.getLog());
        if (prog.getLog().length() != 0)
            Gdx.app.log("GpuShadows", prog.getLog());
        return prog;
    }
}
