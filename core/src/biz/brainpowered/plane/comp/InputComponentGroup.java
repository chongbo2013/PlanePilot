package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.interfaces.*;
import com.badlogic.gdx.utils.Array;

/**
 * Created by sebastian on 2014/07/28.
 * GraphicsComponentGroup Singleton
 */
public class InputComponentGroup implements InputComponentGroupInterface {

    private static InputComponentGroupInterface instance = new InputComponentGroup();
    private Array<InputComponentInterface> components;
    private InputComponentGroup()
    {
        init();
    }
    public static InputComponentGroupInterface getInstance()
    {
        return instance;
    }


    @Override
    public boolean init() {
        try
        {
            components = new Array<InputComponentInterface>();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Note: When adding InputComponents that implement the same inputs, the newer one will always override the older
     * @param componentInterface of type <t>InputComponent</t>
     */
    @Override
    public void addComponent(ComponentInterface componentInterface) {
        components.add((InputComponentInterface) componentInterface);
    }

    @Override
    public Array<LightComponentInterface> getComponents() {
        return null;
    }

    /**
     * Iterates all Active components, calling thier update() method
     * @param param Potential parameters that may be needed
     */
    public void updateGroup ( Object... param ) {
        for ( int i =0; i< components.size; i++ ) {
            components.get(i).update(null);
        }
    }
}
