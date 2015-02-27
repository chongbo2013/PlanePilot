package biz.brainpowered.plane.desktop;

import biz.brainpowered.plane.BloomDemo;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class BloomDemoLauncher{
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "PlanePilot";
        //config.useGL20 = true;
        config.width = 700;
        config.height = 1024;
        new LwjglApplication(new BloomDemo(), config);
    }

}
