package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.interfaces.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by sebastian on 2014/07/28.
 * GraphicsComponentGroup Singleton
 */
public class LightComponentGroup implements LightComponentGroupInterface {

    private static LightComponentGroupInterface instance = new LightComponentGroup();
    private Array<LightComponentInterface> components;
    private LightComponentGroup()
    {
        init();
    }
    public static LightComponentGroupInterface getInstance()
    {
        return instance;
    }

    @Override
    public boolean init() {
        try
        {
            components = new Array<LightComponentInterface>();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void addComponent(ComponentInterface component) {
        components.add((LightComponentInterface)component);
    }

    @Override
    public Array<LightComponentInterface> getComponents() {
        return components;
    }

    public void addComponent(GraphicsComponentInterface componentInterface) {
        // add component to fast collection
    }

    // Custom
    public void updateGroup ( SpriteBatch batch ) {
        for ( int i =0; i<components.size; i++ ) {
            components.get(i).update(batch);
            if (components.get(i).checkDispose()){
                components.get(i).dispose();
                components.removeIndex(i);

            }
        }
    }

    /**
     * todo: Decisions to be made here
     */
    // Get Lights, or Run Renderer within this (injection)
}
