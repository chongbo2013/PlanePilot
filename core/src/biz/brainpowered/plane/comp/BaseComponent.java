package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.interfaces.ComponentInterface;
import biz.brainpowered.plane.comp.interfaces.EntityInterface;

/**
 * Created by sebastian on 2014/07/29.
 */
public class BaseComponent implements ComponentInterface {
    public final String _name;
    public final EntityInterface _entity;

    public BaseComponent(String name, EntityInterface entity) {
        _name = name;
        _entity = entity;
    }

    public String getName () {
       return _name;
    }

    public void registerComponent() {
        ComponentGroupManager.getInstance().getComponentGroup(_name).addComponent(this);
    }

    /**
     * One-sided De-referencing
     */
    public void dispose(){
        _entity.setDispose(true);
    }

    public boolean checkDispose(){
        return _entity.getDispose();
    }
}
