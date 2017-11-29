package net.benmclean.DMCAbros.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import net.benmclean.DMCAbros.DMCAbrosGame;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        GwtApplicationConfiguration configuration = new GwtApplicationConfiguration(854, 480);
        return configuration;
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new DMCAbrosGame();
    }
}