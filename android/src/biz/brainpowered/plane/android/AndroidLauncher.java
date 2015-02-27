package biz.brainpowered.plane.android;

import android.os.Bundle;

import biz.brainpowered.plane.Shockwave;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import biz.brainpowered.plane.Pilot;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = true;
        config.useCompass = true;
        //config.useGL20 = true; use GL30 instead - GL20 is default
        //config.resolutionStrategy = new RatioResolutionStrategy(1.3f);
		initialize(new Shockwave(), config);
	}
}
