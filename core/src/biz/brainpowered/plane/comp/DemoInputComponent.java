package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.interfaces.ComponentInterface;
import biz.brainpowered.plane.comp.interfaces.EntityInterface;

/**
 * Created by sebastian on 2014/07/28.
 */
public class DemoInputComponent extends BaseComponent implements ComponentInterface {

    public DemoInputComponent(EntityInterface entity) {
        super("Input", entity);

        // todo: inject custom callbacks into InputComponents
        // and then init AI
    }

    public void update( Object... params ) {
        // detect input and update velocity on entity
        // input detection and setting Entity properties
        // AI. execute/update/smoothstep
    }
}
