package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

/**
 * Some code was copied from https://github.com/RoaringCatGames/libgdx-ashley-box2d-example
 */
public class RenderingSystem extends SortedIteratingSystem {
    public Assets assets;
    private SpriteBatch batch;
    private Array<Entity> renderQueue;
    private Comparator<Entity> comparator;
    public Camera cam;
    public Body camTarget;
    public Color backgroundColor = Color.BLACK;

    public RenderingSystem(SpriteBatch batch, Camera cam, Assets assets) {
        super(Family.all(Components.TransformC.class, Components.TextureRegionC.class).get(), new ZComparator());
        comparator = new ZComparator();
        renderQueue = new Array<Entity>();

        this.batch = batch;
        this.cam = cam;
        this.assets = assets;
    }

    public void setCamTarget(Body body) {
        camTarget = body;
    }

    public void setBackgroundColor (Color color) {
        backgroundColor = color;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (camTarget != null) {
            cam.position.set(camTarget.getPosition().x, camTarget.getPosition().y, 0);
            cam.update();
            batch.setProjectionMatrix(cam.combined);
        }
        renderQueue.sort(comparator);
        batch.begin();
        for (Entity entity : renderQueue) {
            Components.TextureRegionC tex = Components.textureC.get(entity);
            Components.TransformC t = Components.transformC.get(entity);
            Components.BodyC body = Components.bodyC.get(entity);

            if (tex.region == null || t.isHidden) {
                continue;
            }

            batch.draw(tex.region,
                    body.body.getPosition().x - 0.5f, body.body.getPosition().y - 0.5f,
//                    0, 0,
                    1, 1); //,
//                    t.scale.x, t.scale.y,
//                    body.body.getAngle() * MathUtils.radiansToDegrees);
        }
        batch.end();
        renderQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }
}
