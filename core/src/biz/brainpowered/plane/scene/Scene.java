package biz.brainpowered.plane.scene;

import com.badlogic.gdx.utils.Array;

/**
 * Created by sebastian on 2014/07/29.
 */
public class Scene {
    String id;
    Array<String> assetList; // to be loaded via asset manager
    Array<Script> scripts; // See the Command Pattern
    Array<Command> history; // a log?

    public Scene (String id, Array<String>assetList, Array<Script> scripts) {
        // assign
    }

    public void start () {
        // load assets
        // then proceed with command executiuon queue
    }

    public void executeScriptCommand (String scriptId, Command command) {
        history.add(command);
        command.execute();
    }
}
