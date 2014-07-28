package biz.brainpowered.plane.comp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by sebastian on 2014/07/28.
 */
public class InputComponent implements ComponentInterface {
    private Entity _entity;

    public InputComponent ( Entity entity ) {
        _entity = entity;

        // todo: inject custom callbacks into InputComponents
        Gdx.input.setInputProcessor(new InputAdapter() {

            public boolean touchDown(int x, int y, int pointer, int button) {
                float mx = x;
                float my = Gdx.graphics.getHeight() - y;
                _entity._x = mx;
                _entity._y = my;
                return true;
            }

        });
    }

    public void update( Object... params ) {
        // detect input and update velocity on entity
        // input detection and setting Entity properties
    }

    @Override
    public void registerComponent() {
        InputComponentGroup.getInstance().addComponent(this);
    }
}
