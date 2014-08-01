package biz.brainpowered.plane.comp;

import biz.brainpowered.plane.comp.entities.*;
import biz.brainpowered.plane.comp.interfaces.EntityInterface;
import biz.brainpowered.plane.comp.interfaces.GraphicsComponentGroupInterface;
import biz.brainpowered.plane.comp.interfaces.GraphicsComponentInterface;
import biz.brainpowered.plane.comp.interfaces.SpriteEntityInterface;
import biz.brainpowered.plane.manager.GameManager;
import biz.brainpowered.plane.model.Light;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import org.lwjgl.Sys;

import java.util.HashMap;
import java.util.Map;

/**
 * Manage and Store Entity References
 * Singleton
 */
public class EntityManager {

    private static EntityManager instance = new EntityManager();
    public static EntityManager getInstance()
    {
        return instance;
    }
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
            entity = new GroundEntity( 80f ); // passing in null (for now)
            entity.addComponent(new GroundGraphicsComponent((Texture)GameManager.assetLoader.get(params[1].toString(), "Texture"), entity));
            addEntity(entityId, entity);
        }
        return entity;
    }

    public EntityInterface createPlaneEntity (Object... params) {
        String entityId = (String) params[0];
        EntityInterface entity = getEntity(entityId);
        if (entity == null) {
            entity = new PlaneEntity(params);
            entity.addComponent(new PlayerGraphicsComponent((Texture)GameManager.assetLoader.get(params[1].toString(), "Texture"), (SpriteEntityInterface)entity));
            entity.addComponent(new PlayerInputComponent(entity));
            addEntity(entityId, entity);
        }
        return entity;
    }

    public EntityInterface createPlayerBulletEntity(Object... params) {
        //todo: keep texture reference
        String entityId = (String) params[0];
        EntityInterface entity = getEntity(entityId);
        PlaneEntity planeEntity = (PlaneEntity) params[2];
        if (entity == null) {
            entity = new BulletEntity(params);
            float bulletX = planeEntity.getX() + (planeEntity.getWidth() / 2);
            float bulletY = planeEntity.getY() + (planeEntity.getHeight());
            // set initial, then Moves automatically (keep entity updated)
            entity.addComponent(new BulletGraphicsComponent((Texture)GameManager.assetLoader.get(params[1].toString(), "Texture"), entity, bulletX, bulletY, 90f, 1000f)); // todo: globals
            // Grab updated values from Entity -> keep Light Updated
            // Pseudo: this entity, controls the Component
            //entity.addComponent(new LightComponent(entity,  _x + _sprite.getWidth()/2, _y + _sprite.getHeight()/2, Light.randomColor(), 0.5f, true, true));
            entity.addComponent(new LightComponent(entity,  bulletX, bulletY, Light.randomColor(), 0.5f, true, true));
            addEntity(entityId, entity);
        }
        return entity;
    }

    public EntityInterface createProgressBarEntity (Object... params) {
        String entityId = (String) params[0];
        EntityInterface entity = getEntity(entityId);
        if (entity == null) {
            entity = new ProgressBarEntity(params);
            entity.addComponent(new ProgressBarGraphicsComponent((Texture)GameManager.assetLoader.get(params[1].toString(), "Texture"), (SpriteEntityInterface)entity, new Float(params[2].toString()),  new Float(params[3].toString()), 100, 100));
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
