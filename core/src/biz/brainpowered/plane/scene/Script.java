package biz.brainpowered.plane.scene;

import biz.brainpowered.plane.entity.EntityInterface;
import com.badlogic.gdx.utils.Array;

/**
 * Created by sebastian on 2014/07/29.
 */
public class Script {
    String id;
    Array<Command> commands; // commands that comprise this script
    Array<EntityInterface> entities; // keep entities as reference for later disposal
}
