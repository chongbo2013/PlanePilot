package biz.brainpowered.plane.comp.entities;

import biz.brainpowered.plane.comp.EntityManager;
import com.badlogic.gdx.utils.Array;

public class PlaneEntity extends SpriteEntity {
    private int shotsFired = 0;
    private Array<BulletEntity> bullets = new Array<BulletEntity>();
    public PlaneEntity(Object params) {
        super(params);
    }

    public void fireBullet () {
        // should check last time bullet was fired -- could invest in a TimerSystem for time based triggers/flags
        EntityManager.getInstance().createPlayerBulletEntity("PlayerBullet"+shotsFired, "airplane/B_2.png", this);
        shotsFired++;
    }
}
