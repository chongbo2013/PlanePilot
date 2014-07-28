package biz.brainpowered.plane.comp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by sebastian on 2014/07/28.
 */
public class PlayerGraphicsComponent extends GenericGraphicsComponent {

    // TODO: fill out the class variables from the Plane class
    Texture leftTexture;
    Sprite leftSprite;

    Texture rightTexture;
    Sprite rightSprite;

    // pass a complete parameter list in here, thanks
    public PlayerGraphicsComponent(Texture texture, Entity entity) {
        super(texture, entity);

        // more assignments..
    }
}
