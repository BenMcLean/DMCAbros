package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Comparator;

/**
 * Some code was copied from https://github.com/RoaringCatGames/libgdx-ashley-box2d-example
 */
public class RenderingSystem extends SortedIteratingSystem {
    static final float PPM = 16.0f;
    public static final float PIXELS_TO_METRES = 1.0f / PPM;
    private static Vector2 meterDimensions = new Vector2();
    private static Vector2 pixelDimensions = new Vector2();
    public Assets assets;
    private SpriteBatch batch;
    private Viewport view;
    private Array<Entity> renderQueue;
    private Comparator<Entity> comparator;
    private ComponentMapper<Components.TextureRegionComponent> textureM;
    private ComponentMapper<Components.TransformComponent> transformM;

    public static Vector2 getScreenSizeInMeters() {
        meterDimensions.set(Gdx.graphics.getWidth() * PIXELS_TO_METRES,
                Gdx.graphics.getHeight() * PIXELS_TO_METRES);
        return meterDimensions;
    }

    public static Vector2 getScreenSizeInPixesl() {
        pixelDimensions.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return pixelDimensions;
    }

    public static float PixelsToMeters(float pixelValue) {
        return pixelValue * PIXELS_TO_METRES;
    }

    public RenderingSystem(SpriteBatch batch, Viewport view, Assets assets) {
        super(Family.all(Components.TransformComponent.class, Components.TextureRegionComponent.class).get(), new ZComparator());

        textureM = ComponentMapper.getFor(Components.TextureRegionComponent.class);
        transformM = ComponentMapper.getFor(Components.TransformComponent.class);

        renderQueue = new Array<Entity>();

        this.batch = batch;
        this.view = view;
        this.assets = assets;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        renderQueue.sort(comparator);
        batch.draw(assets.atlas.findRegion("bricks/brick00"), 0, 0);

        for (Entity entity : renderQueue) {
            Components.TextureRegionComponent tex = textureM.get(entity);
            Components.TransformComponent t = transformM.get(entity);

            if (tex.region == null || t.isHidden) {
                continue;
            }

            float width = tex.region.getRegionWidth();
            float height = tex.region.getRegionHeight();

            float originX = width / 2f;
            float originY = height / 2f;

            batch.draw(tex.region,
                    t.position.x - originX, t.position.y - originY,
                    originX, originY,
                    width, height,
                    PixelsToMeters(t.scale.x), PixelsToMeters(t.scale.y),
                    t.rotation);
        }
        renderQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    public Viewport getView() {
        return view;
    }
}
