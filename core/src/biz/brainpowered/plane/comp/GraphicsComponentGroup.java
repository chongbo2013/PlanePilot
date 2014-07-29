package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.interfaces.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by sebastian on 2014/07/28.
 * GraphicsComponentGroup Singleton
 */
public class GraphicsComponentGroup implements GraphicsComponentGroupInterface {

    private static GraphicsComponentGroupInterface instance = new GraphicsComponentGroup();
    private Array<GraphicsComponentInterface> components;
    private GraphicsComponentGroup()
    {
        init();
    }
    public static GraphicsComponentGroupInterface getInstance()
    {
        return instance;
    }

    @Override
    public boolean init() {
        try
        {
            components = new Array<GraphicsComponentInterface>();
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
        components.add((GraphicsComponentInterface)component);
    }

    public void addComponent(GraphicsComponentInterface componentInterface) {
        // add component to fast collection
    }

    // Custom
    public void updateGroup ( SpriteBatch batch ) {
        for ( int i =0; i<components.size; i++ ) {
            components.get(i).update(batch);
        }
    }
}
