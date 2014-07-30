package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.entities.BaseEntity;
import biz.brainpowered.plane.comp.entities.GroundEntity;
import biz.brainpowered.plane.comp.entities.PlaneEntity;
import biz.brainpowered.plane.comp.entities.SpriteEntity;
import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.lwjgl.Sys;

import java.util.HashMap;
import java.util.Map;

/**
 * Manage and Store Entity References
 */
public class EntityManager {

    private Map<String, EntityInterface> entities;

    public EntityManager () {
        init();
    }

    public boolean init () {
        try {
            entities = new HashMap<String, EntityInterface>();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public EntityInterface getEntity (String entityId) {
        try {
            return entities.get(entityId);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addEntity (String entityId, EntityInterface entity) {
        if (getEntity(entityId) == null) {
            entities.put(entityId, entity);
        }else {
            System.out.println("Something Broke, UP the Chain! - EntityManager:addEntity:"+entityId+" resulted in a duplicate and did not 'addEntity'");
        }
    }

    // todo: define complete list of entity creation functions
    // they should ony need to differ by type, the data being passed in can have significant effects on the entity

    // todo: define params according to Map from Text Config.. (central)
    public EntityInterface createGroundEntity ( Object... params ) {
        String entityId = (String) params[0];
        EntityInterface entity = getEntity(entityId);
        if (entity == null) {
            entity = new GroundEntity( null ); // passing in null (for now)
            entity.addComponent(new GroundGraphicsComponent(new Texture(Gdx.files.internal(params[1].toString())), entity, new Float(params[2].toString())));
            addEntity(entityId, entity);
        }
        return entity;
    }

    public EntityInterface createPlaneEntity (Object... params) {
        String entityId = (String) params[0];
        EntityInterface entity = getEntity(entityId);
        if (entity == null) {
            entity = new PlaneEntity(params);
            entity.addComponent(new PlayerGraphicsComponent(new Texture(Gdx.files.internal(params[1].toString())), entity));
            entity.addComponent(new PlayerInputComponent(entity));
            addEntity(entityId, entity);
        }
        return entity;
    }

//    public EntityInterface createEnemyEntity (Object... params) {
//        String entityId = (String) params[0];
//        EntityInterface entity = getEntity(entityId);
//        if (entity != null) {
//            entity = new PlaneEntity(params);
//            entity.addComponent(new EnemyGraphicsComponent(new Texture(Gdx.files.internal(params[1].toString())), entity));
//            entity.addComponent(new EnemyInputComponent(entity));
//            addEntity(entityId, entity);
//        }
//        return entity;
//    }
}
