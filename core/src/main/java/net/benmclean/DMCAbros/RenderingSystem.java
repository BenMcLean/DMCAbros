package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
    private ComponentMapper<Components.TextureRegionC> textureM;
    private ComponentMapper<Components.TransformC> transformM;
    private ComponentMapper<Components.BodyC> bodyM;

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
        super(Family.all(Components.TransformC.class, Components.TextureRegionC.class).get(), new ZComparator());
        comparator = new ZComparator();

        textureM = ComponentMapper.getFor(Components.TextureRegionC.class);
        transformM = ComponentMapper.getFor(Components.TransformC.class);
        bodyM = ComponentMapper.getFor(Components.BodyC.class);

        renderQueue = new Array<Entity>();

        this.batch = batch;
        this.view = view;
        this.assets = assets;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        renderQueue.sort(comparator);

        for (Entity entity : renderQueue) {
            Components.TextureRegionC tex = textureM.get(entity);
            Components.TransformC t = transformM.get(entity);
            Components.BodyC body = bodyM.get(entity);

            if (tex.region == null || t.isHidden) {
                continue;
            }

            float width = tex.region.getRegionWidth();
            float height = tex.region.getRegionHeight();

            float originX = width / 2f;
            float originY = height / 2f;

            batch.draw(tex.region,
                    body.body.getPosition().x - originX, body.body.getPosition().y - originY,
                    originX, originY,
                    width, height,
                    PixelsToMeters(t.scale.x), PixelsToMeters(t.scale.y),
                    body.body.getAngle() * MathUtils.radiansToDegrees);
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
