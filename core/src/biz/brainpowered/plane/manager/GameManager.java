package biz.brainpowered.plane.manager;

import biz.brainpowered.plane.comp.EntityManager;
import com.badlogic.gdx.Game;

/**
 * Created by sebastian on 2014/07/31.
 */
public class GameManager {
    public static String things;
    public static Game activeGame;
    public static EntityManager entityManager;
    //etc..
    public static AssetLoader assetLoader;

    public static void init () {
        assetLoader = new AssetLoader();
        entityManager = new EntityManager();
    }
}
