package biz.brainpowered.plane.comp.entities;

/**
 * Composes a Specialised Graphics Component
 *
 */
public class GroundEntity extends SpriteEntity {

    public float groundSpeed;

    public GroundEntity(float groundSpeed) {
        super( null );
        this.groundSpeed = groundSpeed;
    }
}
