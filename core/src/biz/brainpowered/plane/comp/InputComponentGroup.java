package biz.brainpowered.plane.comp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
        components = new Array<InputComponentInterface>();
    }
    public static InputComponentGroupInterface getInstance()
    {
        return instance;
    }

    /**
     * Future Proofing
     */
    @Override
    public void init() {

    }

    /**
     * Note: When adding InputComponents that implement the same inputs, the newer one will always override the older
     * @param componentInterface of type <t>InputComponent</t>
     */
    @Override
    public void addComponent(ComponentInterface componentInterface) {
        // add component to fast collection
        components.add((InputComponentInterface) componentInterface);
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
