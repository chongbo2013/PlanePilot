package biz.brainpowered.plane.comp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by sebastian on 2014/07/28.
 */
public class DemoInputComponent implements ComponentInterface {
    private Entity _entity;

    public DemoInputComponent(Entity entity) {
        _entity = entity;

        // todo: inject custom callbacks into InputComponents
        // and then init AI
    }

    public void update( Object... params ) {
        // detect input and update velocity on entity
        // input detection and setting Entity properties
        // AI. execute/update/smoothstep
    }

    @Override
    public void registerComponent() {
        InputComponentGroup.getInstance().addComponent(this);
    }
}
