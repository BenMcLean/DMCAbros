package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import net.benmclean.utils.AtlasRepacker;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidmath.LightRNG;
import squidpony.squidmath.RNG;

import java.io.FileNotFoundException;

/**
 * Some code was copied from https://github.com/RoaringCatGames/libgdx-ashley-box2d-example
 */
public class GameScreen extends ScreenAdapter implements Disposable {
    protected final float VIRTUAL_HEIGHT = 12f;

    public Assets assets;
    public TextureAtlas atlas;
    protected boolean isInitialized = false;
    protected float elapsedTime = 0f;
    protected int secondsToSplash = 10;
    protected World world;

    protected PooledEngine engine;
    protected KeyboardController controller;
    protected SpriteBatch batch;
    protected OrthographicCamera cam;
    //    protected FrameBuffer frameBuffer;
    protected IScreenDispatcher dispatcher;
    //    protected Texture screenTexture;
//    protected TextureRegion screenRegion;
//    protected Viewport worldView;
//    protected Viewport screenView;
    long SEED;
    Assets.Brick brick;
    Color backgroundColor;

    public GameScreen(Assets assets, SpriteBatch batch, FrameBuffer frameBuffer, IScreenDispatcher dispatcher) {
        this(0, assets, batch, frameBuffer, dispatcher);
    }

    public GameScreen(long SEED, Assets assets, SpriteBatch batch, FrameBuffer frameBuffer, IScreenDispatcher dispatcher) {
        super();
        this.SEED = SEED;
        brick = Assets.Brick.values()[LightRNG.determineBounded(SEED, Assets.Brick.values().length)];
        RNG rng = new RNG(SEED);
        backgroundColor = SColor.randomColorWheel(rng, 1, 2);

        Color brickColor = SColor.randomColorWheel(rng, 2, 2);
        Color[] brickColors = new Color[]{
                Color.BLACK,
                new Color(brickColor.r / 2f, brickColor.g / 2f, brickColor.b / 2f, 1f),
                brickColor,
                new Color(brickColor.r * 1.5f, brickColor.g * 1.5f, brickColor.b * 1.5f, 1f)
        };

        AtlasRepacker packer = new AtlasRepacker().pack("brick", assets.atlas.findRegion("bricks/" + brick.toString()), brickColors);
        atlas = packer.generateTextureAtlas();
        packer.dispose();

        this.batch = batch;
        this.dispatcher = dispatcher;
        this.assets = assets;
        cam = new OrthographicCamera();
        controller = new KeyboardController();
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
        engine.addSystem(new PlayerControlSystem(controller));
        engine.addSystem(new MobSystem());
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new PhysicsDebugSystem(world, cam));
        engine.addSystem(new RenderingSystem(batch, cam, assets));
        engine.getSystem(RenderingSystem.class).setBackgroundColor(backgroundColor);

//        engine.addEntity(brick(-1, 0));
//        engine.addEntity(brick(0, 0));
//        engine.addEntity(brick(1, 0));

        try {
            loadLevel("maps/smb/1-1.txt");
        } catch (FileNotFoundException e) {
            Gdx.app.log("GameScreen", "FileNotFoundException!");
            Gdx.app.exit();
        }

        Entity player = avatar(8, 8);
        engine.getSystem(RenderingSystem.class).setCamTarget(
                player.getComponent(Components.BodyC.class).body
        );
        engine.addEntity(player);

        Gdx.input.setInputProcessor(controller);

        isInitialized = true;
    }

    public void loadLevel(String filename) throws FileNotFoundException {
        FileHandle file = Gdx.files.internal(filename);
        if (!file.exists()) throw new FileNotFoundException();
        String[] stuff = file.readString().split(";");
        String[] blocks = stuff[0].split(",");
        int mapWidth = blocks.length / 15;
        for (int x = 0; x < mapWidth; x++)
            for (int y = 1; y < 15; y++) {
                String block = blocks[(y - 1) * (mapWidth) + x];
                if (block.contains("-"))
                    engine.addEntity(goober(x, 15 - y));
                else if (!block.equals("1"))
                    engine.addEntity(brick(x, 15 - y, brick));
            }
    }

    public Entity avatar(int x, int y) {
        Entity e = new Entity();
        e.add(Components.TypeC.Mob);
        Components.TextureRegionC tc = engine.createComponent(Components.TextureRegionC.class);
        tc.region = assets.atlas.findRegion("characters/AstronautE0");
        e.add(tc);
        Components.TransformC tfc = engine.createComponent(Components.TransformC.class);
        tfc.z = 1;
        tfc.scale.set(1, 1);
        e.add(tfc);
        Components.BodyC playerBody = engine.createComponent(Components.BodyC.class);
        playerBody.body = createBox(x, y, true, world);
        e.add(playerBody);
        Components.StateC stateCom = engine.createComponent(Components.StateC.class);
        stateCom.set(Components.StateC.STATE_NORMAL);
        e.add(stateCom);
        Components.PlayerC pc = engine.createComponent(Components.PlayerC.class);
        e.add(pc);
        return e;
    }

    public Entity brick(int x, int y, Assets.Brick brick) {
        Entity e = new Entity();
        e.add(Components.TypeC.Brick);
        Components.TextureRegionC tc = engine.createComponent(Components.TextureRegionC.class);
        tc.region = atlas.findRegion("brick");
        e.add(tc);
        Components.TransformC tfc = engine.createComponent(Components.TransformC.class);
        tfc.z = 1;
        tfc.scale.set(1, 1);
        e.add(tfc);
        Components.BodyC bc = engine.createComponent(Components.BodyC.class);
        bc.body = createBox(x, y, false, world);
        e.add(bc);
        return e;
    }

    public Entity goober(int x, int y) {
        Entity e = new Entity();
        e.add(Components.TypeC.Mob);
        Components.TextureRegionC tc = engine.createComponent(Components.TextureRegionC.class);
        tc.region = assets.atlas.findRegion("characters/BlobS0");
        e.add(tc);
        Components.TransformC tfc = engine.createComponent(Components.TransformC.class);
        tfc.z = 1;
        tfc.scale.set(1, 1);
        e.add(tfc);
        Components.BodyC bodyC = engine.createComponent(Components.BodyC.class);
        bodyC.body = createBox(x, y, true, world);
        e.add(bodyC);
        e.add(engine.createComponent(Components.PacingBehavior.class));
        return e;
    }

    public static Body createBox(float x, float y, boolean dynamic, World world) {
        return createBox(x, y, 1f, 1f, dynamic, world);
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
        engine.update(delta); // This is where the magic happens.

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