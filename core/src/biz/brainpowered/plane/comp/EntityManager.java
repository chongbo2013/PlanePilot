package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.entities.BaseEntity;
import biz.brainpowered.plane.comp.entities.GroundEntity;
import biz.brainpowered.plane.comp.entities.PlaneEntity;
import biz.brainpowered.plane.comp.entities.SpriteEntity;
import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by sebastian on 2014/07/28.
 */
public class EntityManager {

    /**
     * Entity References are not Stored, Except in the Components that they Compose (trick!)
     */
    public EntityManager () {

    }

    // do the test
    public BaseEntity createEntityEntity ( Object... params ) {
        BaseEntity entity = new BaseEntity ( params );
        entity.addComponent(new GenericGraphicsComponent(new Texture(Gdx.files.internal(params[1].toString())), entity)); // todo: add AssetManager
        entity.addComponent(new GenericInputComponent(entity));
        return entity;
    }

    // does not exist yet
    public BaseEntity createPlayerEntity ( Object... params ) {
        BaseEntity entity = new BaseEntity ( params );
        entity.addComponent(new PlayerGraphicsComponent(new Texture(Gdx.files.internal(params[1].toString())), entity)); // todo: add AssetManager
        entity.addComponent(new GenericInputComponent(entity));
        return entity;
    }

    // todo: define complete list of entity creation functions
    // they should ony need to differ by type, the data being passed in can have significant effects on the entity

    // todo: define params according to Map from Text Config.. (central)
    public EntityInterface createGroundEntity ( Object... params ) {
        GroundEntity entity = new GroundEntity( params[0] ); // passing in null (for now)
        entity.addComponent(new GroundGraphicsComponent(new Texture(Gdx.files.internal(params[1].toString())), entity, new Float(params[2].toString())));
        return entity;
    }

    public EntityInterface createPlaneEntity (Object... params) {
        PlaneEntity entity = new PlaneEntity( params );
        entity.addComponent(new PlayerGraphicsComponent(new Texture(Gdx.files.internal(params[1].toString())), entity));
        entity.addComponent(new PlayerInputComponent(entity));
        //entity.a
        return entity;
    }
}
