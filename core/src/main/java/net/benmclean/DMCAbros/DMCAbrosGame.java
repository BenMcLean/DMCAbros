package net.benmclean.DMCAbros;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class DMCAbrosGame extends ApplicationAdapter {
    public static final int sky = Color.rgba8888(93 / 255f,148 / 255f,251 / 255f, 1f);
    public static final int brickDark = Color.rgba8888(204 / 255f,75 / 255f,9 / 255f, 1f);
    public static final int brickLight = Color.rgba8888(1f,186 / 255f,170 / 255f, 1f);
    public static final int bushes = Color.rgba8888(18 / 255f,124 / 255f,34 / 255f, 1f);
    public static final int clouds = Color.rgba8888(219 / 255f,252 / 255f,1f,1f);
    public Assets assets;

    @Override
    public void create() {
        assets = new Assets();
    }

    @Override
    public void render() {

    }
}