package biz.brainpowered.plane.comp.entities;

/**
 * Created by sebastian on 2014/08/01.
 */
public class ProgressBarEntity extends SpriteEntity {
    public float currentProgress;

    public ProgressBarEntity(Object params) {
        super(params);
        currentProgress = 0f;
    }

    public void setCurrentProgress(float progress){
        currentProgress = progress;
    }
}
