package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.entity.Plane;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by sebastian on 2014/07/28.
 */
public class EntityManager {
    public EntityManager () {

    }

    // do the test
    public Entity createEntityEntity ( Object... params ) {

        // todo: make a one line here :P
        Entity entity = new Entity ( params ); // get reference to entity - then pass it into the Components
        entity.addComponent(new GenericGraphicsComponent(new Texture(Gdx.files.internal(params[1].toString())), entity)); // todo: add AssetManager
        entity.addComponent(new GenericInputComponent(entity));
        return entity;
    }

    // does not exist yet
    public Entity createPlayerEntity ( Object... params ) {
        //Plane plane = new Plane( params );
        Entity entity = new Entity ( params ); // get reference to entity - then pass it into the Components
        // can poke around with entity here
        entity.addComponent(new PlayerGraphicsComponent(new Texture(Gdx.files.internal(params[1].toString())), entity)); // todo: add AssetManager
        entity.addComponent(new GenericInputComponent(entity));
        return entity;
    }

    // todo: define complete list of entity creation functions
    // they should ony need to differ by type, the data being passed in can have significant effects on the entity
}
