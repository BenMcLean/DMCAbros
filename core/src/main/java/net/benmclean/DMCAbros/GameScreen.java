package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Some code was copied from https://github.com/RoaringCatGames/libgdx-ashley-box2d-example
 */
public class GameScreen extends ScreenAdapter implements Disposable {

    public Assets assets;
    protected boolean isInitialized = false;
    protected float elapsedTime = 0f;
    protected int secondsToSplash = 10;
    protected World world;

    protected PooledEngine engine;

    protected SpriteBatch batch;
    protected FrameBuffer frameBuffer;
    protected IScreenDispatcher dispatcher;
    protected Texture screenTexture;
    protected TextureRegion screenRegion;
    protected Viewport worldView;
    protected Viewport screenView;
    protected Entity e;

    public GameScreen(Assets assets, SpriteBatch batch, FrameBuffer frameBuffer, IScreenDispatcher dispatcher) {
        super();
        this.batch = batch;
        this.frameBuffer = frameBuffer;
        this.dispatcher = dispatcher;
        this.assets = assets;
        worldView = new FitViewport(Assets.VIRTUAL_WIDTH, Assets.VIRTUAL_HEIGHT);
        screenView = new FitViewport(Assets.VIRTUAL_WIDTH, Assets.VIRTUAL_HEIGHT);
        screenView.getCamera().position.set(Assets.VIRTUAL_WIDTH / 2, Assets.VIRTUAL_HEIGHT / 2, 0);
        screenView.update(Assets.VIRTUAL_WIDTH, Assets.VIRTUAL_HEIGHT);
        screenRegion = new TextureRegion();
        batch.enableBlending();
    }

    private void init() {
        Gdx.app.log("GameScreen", "Initializing");
        isInitialized = true;

        world = new World(new Vector2(0f, -9.8f), true);
        engine = new PooledEngine();

        RenderingSystem renderingSystem = new RenderingSystem(batch, worldView, assets);
        engine.addSystem(renderingSystem);
        engine.addSystem(new PhysicsSystem(world));

        engine.addEntity(brick());

        isInitialized = true;
    }

    public Entity brick() {
        Entity e = new Entity();
        Components.TransformComponent tfc = new Components.TransformComponent();
        tfc.position.set(0, 0, 1);
        tfc.rotation = 0;
        tfc.scale.set(1, 1);
        e.add(tfc);
        Components.BodyComponent bc = new Components.BodyComponent();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);
        bc.body = world.createBody(bodyDef);
        e.add(bc);
        Components.TextureRegionComponent tc = new Components.TextureRegionComponent();
        tc.region = assets.atlas.findRegion("bricks/brick00");
        e.add(tc);
        return e;
    }

    private void update(float delta) {
        frameBuffer.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        worldView.getCamera().position.set(0,0,0);
        worldView.update(Assets.VIRTUAL_WIDTH, Assets.VIRTUAL_HEIGHT);
        batch.setProjectionMatrix(worldView.getCamera().combined);
        worldView.apply();
        batch.begin();
        engine.update(delta);
        batch.end();
        frameBuffer.end();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        screenView.apply();
        batch.setProjectionMatrix(screenView.getCamera().combined);
        batch.begin();
        screenTexture = frameBuffer.getColorBufferTexture();
        screenTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        screenRegion.setRegion(screenTexture);
        screenRegion.flip(false, true);
        batch.draw(screenRegion, 0, 0);
        batch.end();

        elapsedTime += delta;
        if (elapsedTime / 1000f > secondsToSplash) {
            world.dispose();
            dispatcher.endCurrentScreen();
        }
    }

    @Override
    public void render(float delta) {
        if (isInitialized) {
            update(delta);
        } else {
            init();
        }
    }

    @Override
    public void dispose() {
        screenTexture.dispose();
    }
}