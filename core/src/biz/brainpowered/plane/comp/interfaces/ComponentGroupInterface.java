package biz.brainpowered.plane.comp.interfaces;

import com.badlogic.gdx.utils.Array;

/**
 * Created by sebastian on 2014/07/29.
 */
public interface ComponentGroupInterface {
    // initialise this type of Component Group
    public boolean init();

    // add component instance (which has reference to the 'owning' Entity)
    public void addComponent(ComponentInterface componentInterface);

    public Array<LightComponentInterface> getComponents();
}
