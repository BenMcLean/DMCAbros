package net.benmclean.DMCAbros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class DMCAbrosGame extends Game {
    public Assets assets;
    protected SpriteBatch batch;
    protected ScreenDispatcher screenDispatcher;

    @Override
    public void create() {
        assets = new Assets();
        batch = new SpriteBatch();
        screenDispatcher = new ScreenDispatcher();
        Screen gameScreen = new GameScreen(assets, batch, screenDispatcher);
        screenDispatcher.AddScreen(gameScreen);
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Screen nextScreen = screenDispatcher.getNextScreen();
        if (nextScreen != getScreen()) setScreen(nextScreen);
        super.render();
    }
}
