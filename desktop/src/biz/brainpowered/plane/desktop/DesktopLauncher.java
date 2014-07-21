package biz.brainpowered.plane.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import biz.brainpowered.plane.Pilot;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "PlanePilot";
        //config.useGL20 = true;
        config.width = 700;
        config.height = 1024;
		new LwjglApplication(new Pilot(), config);
	}
}
