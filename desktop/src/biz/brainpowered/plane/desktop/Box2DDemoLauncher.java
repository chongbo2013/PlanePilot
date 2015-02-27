package biz.brainpowered.plane.desktop;

import biz.brainpowered.plane.Box2dDemo;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Box2dDemoLauncher{
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "PlanePilot";
        //config.useGL20 = true;
        config.width = (int) (Box2dDemo.V_WIDTH * Box2dDemo.SCALE);
        config.height = (int) (Box2dDemo.V_HEIGHT * Box2dDemo.SCALE);
        new LwjglApplication(new Box2dDemo(), config);
    }

}
