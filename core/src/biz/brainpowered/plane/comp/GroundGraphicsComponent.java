package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sebastian on 2014/07/29.
 */
public class GroundGraphicsComponent extends GenericGraphicsComponent {
    Sprite _sprite2;

    float _width;
    float _height;
    float _xVelocity;   // required for horizontal scolling
    float _yVelocity;

    public GroundGraphicsComponent(Texture texture, EntityInterface entity, float initalXVelocity) {
        super(texture, entity);


        _width = Gdx.graphics.getWidth();
        _height = Gdx.graphics.getHeight();

        _yVelocity = initalXVelocity;

        // todo: investigate if this is needed
        //texture = new Texture(groundTexturePath);

        _sprite = new Sprite(texture);
        _sprite.setSize(_width, _height);

        _sprite2 = new Sprite(texture);
        _sprite2.setSize(_width, _height);
        _sprite2.setPosition(0.0f, _sprite.getHeight()+_sprite.getY());
    }



    // this case requires that entity position be applied to sprite
    public void update ( SpriteBatch batch ) {
        float deltaT = Gdx.graphics.getDeltaTime();
        float velocityModifier = 1.0f;

        float deltaYV = -(deltaT * (_yVelocity * velocityModifier));
        if (_sprite.getY() <= -_height){
            // off screen - move back up
            _sprite.setPosition(0, _sprite2.getY() + _sprite2.getHeight());
        }
        if (_sprite2.getY() <= -_height){
            // off screen - move back up
            _sprite2.setPosition(0, _sprite.getY() + _sprite.getHeight());
        }

        // WARNING: POSITION IS STORED IN SPRITE....
        _sprite.translateY(Math.round(deltaYV)); // no sub-pixels (that otherwise could lead to artifacts)
        _sprite2.translateY(Math.round(deltaYV));

        _sprite.draw(batch);
        _sprite2.draw(batch);


        // note - drawing data may be needed to be pre-calculated (j.i.c.)
//        if ( !draw(_entity.getDrawConfig(), batch) ) {
        // todo: instead of setting values, just draw in place
//        _sprite.setX(_entity.getX());
//        _sprite.setY(_entity.getY());
//        _sprite.draw(batch); // the common fallback
//        }
    }

//    private boolean draw ( DrawConfig dc, SpriteBatch batch ) {
//        // May be required to do Lights/Shadows/BumpMaps Halleluja - not now
//        return false;
//    }

//    @Override
//    public void registerComponent() {
//        GraphicsComponentGroup.getInstance().addComponent(this);
//    }
}
