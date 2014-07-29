package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import biz.brainpowered.plane.comp.interfaces.InputComponentInterface;
import biz.brainpowered.plane.comp.interfaces.SpriteEntityInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by sebastian on 2014/07/28.
 */
public class GenericInputComponent extends BaseComponent implements InputComponentInterface {

    public GenericInputComponent ( EntityInterface entity ) {
        super(ComponentGroupManager.INPUT, entity);

        // todo: inject custom callbacks into InputComponents
        Gdx.input.setInputProcessor(new InputAdapter() {

            public boolean touchDown(int x, int y, int pointer, int button) {
                float mx = x;
                float my = Gdx.graphics.getHeight() - y;

                /**
                 * Larger Consideration:
                 * Could implement "Spacial" Component that wraps x,y,z,velocity, angle, scale, etc
                 * then simply, example: ((SpacialComponent)_entity.getComponent("Spacial")).getY()/.setX(x)
                 * Considerations in this are, Performance trade-off vs Flexibility (consistency)
                 * and then finally alternative Fast mapping techniques acheiving the same outcome
                 */

                _entity.setX(mx);
                _entity.setY(my);
                return true;
            }

        });
    }

    public void update( Object... params ) {
        // detect input and update velocity on entity
        // input detection and setting Entity properties
    }
}
