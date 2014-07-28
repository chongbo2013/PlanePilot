package biz.brainpowered.plane.comp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by sebastian on 2014/07/28.
 * GraphicsComponentGroup Singleton
 */
public class InputComponentGroup implements ComponentGroupInterface {

    private static ComponentGroupInterface instance = new InputComponentGroup();
    private Array<InputComponent> components;
    private InputComponentGroup()
    {
        components = new Array<InputComponent>();
    }
    public static ComponentGroupInterface getInstance()
    {
        return instance;
    }

    @Override
    public void init() {

    }

    @Override
    public void addComponent(ComponentInterface componentInterface) {
        // add component to fast collection
        components.add((InputComponent) componentInterface);
    }

    public void updateGroup ( SpriteBatch batch ) {
        for ( int i =0; i< components.size; i++ ) {
            components.get(i).update(null);
        }
    }
}
