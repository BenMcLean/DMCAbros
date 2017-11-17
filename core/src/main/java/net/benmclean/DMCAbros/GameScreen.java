package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;

/**
 * Some code was copied from https://github.com/RoaringCatGames/libgdx-ashley-box2d-example
 */
public class GameScreen extends ScreenAdapter implements Disposable {
    protected final float VIRTUAL_HEIGHT = 12f;

    public Assets assets;
    protected boolean isInitialized = false;
    protected float elapsedTime = 0f;
    protected int secondsToSplash = 10;
    protected World world;

    protected PooledEngine engine;

    protected SpriteBatch batch;
    protected OrthographicCamera cam;
    //    protected FrameBuffer frameBuffer;
    protected IScreenDispatcher dispatcher;
    //    protected Texture screenTexture;
//    protected TextureRegion screenRegion;
//    protected Viewport worldView;
//    protected Viewport screenView;
    protected Entity e;

    public GameScreen(Assets assets, SpriteBatch batch, FrameBuffer frameBuffer, IScreenDispatcher dispatcher) {
        super();
        this.batch = batch;
        this.dispatcher = dispatcher;
        this.assets = assets;
        cam = new OrthographicCamera();
//        this.frameBuffer = frameBuffer;
//        worldView = new FitViewport(Assets.VIRTUAL_WIDTH, Assets.VIRTUAL_HEIGHT);
//        screenView = new FitViewport(Assets.VIRTUAL_WIDTH, Assets.VIRTUAL_HEIGHT);
//        screenView.getCamera().position.set(Assets.VIRTUAL_WIDTH / 2, Assets.VIRTUAL_HEIGHT / 2, 0);
//        screenView.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        screenRegion = new TextureRegion();
        batch.enableBlending();
    }

    private void init() {
        Gdx.app.log("GameScreen", "Initializing");
        isInitialized = true;

        world = new World(new Vector2(0f, -9.8f), true);
        engine = new PooledEngine();
        engine.addSystem(new RenderingSystem(batch, assets));
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new PhysicsDebugSystem(world, cam));

        engine.addEntity(brick(-1, 0));
        engine.addEntity(brick(0, 0));
        engine.addEntity(brick(1, 0));

        engine.addEntity(avatar(0, 2));

        isInitialized = true;
    }

    public Entity avatar(int x, int y) {
        Entity e = new Entity();
        e.add(Components.TypeC.Mob);
        TextureRegion region = assets.atlas.findRegion("characters/AstronautE0");
//        int sizeX = region.getRegionWidth(), sizeY = region.getRegionHeight();
        Components.TextureRegionC tc = engine.createComponent(Components.TextureRegionC.class);
        tc.region = region;
        e.add(tc);
        Components.TransformC tfc = engine.createComponent(Components.TransformC.class);
        tfc.z = 1;
        tfc.scale.set(1, 1);
        e.add(tfc);
        Components.BodyC bc = engine.createComponent(Components.BodyC.class);
        bc.body = createBox(x, y, 1f, 1f, true, world);
        e.add(bc);
        return e;
    }

    public Entity brick(int x, int y) {
        Entity e = new Entity();
        e.add(Components.TypeC.Brick);
        TextureRegion region = assets.atlas.findRegion("bricks/brick00");
        int sizeX = region.getRegionWidth(), sizeY = region.getRegionHeight();
        Components.TextureRegionC tc = engine.createComponent(Components.TextureRegionC.class);
        tc.region = region;
        e.add(tc);
        Components.TransformC tfc = engine.createComponent(Components.TransformC.class);
        tfc.z = 1;
        tfc.scale.set(1, 1);
        e.add(tfc);
        Components.BodyC bc = engine.createComponent(Components.BodyC.class);
        bc.body = createBox(x, y, 1f, 1f, false, world);
        e.add(bc);
        return e;
    }

    public static Body createBox(float x, float y, float w, float h, boolean dynamic, World world) {
        // create a definition
        BodyDef boxBodyDef = new BodyDef();
        if (dynamic) {
            boxBodyDef.type = BodyDef.BodyType.DynamicBody;
        } else {
            boxBodyDef.type = BodyDef.BodyType.StaticBody;
        }

        boxBodyDef.position.x = x;
        boxBodyDef.position.y = y;
        boxBodyDef.fixedRotation = true;

        //create the body to attach said definition
        Body boxBody = world.createBody(boxBodyDef);
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(w / 2, h / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.density = 10f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0f;

        boxBody.createFixture(fixtureDef);
        poly.dispose();

        return boxBody;
    }

    private void update(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.position.set(0, 5, 0);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        engine.update(delta); // This is where the magic happens.
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
    public void resize(int width, int height) {
        cam.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
        batch.setProjectionMatrix(cam.combined);
    }

    @Override
    public void dispose() {
    }
}