package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Some code was copied from https://github.com/RoaringCatGames/libgdx-ashley-box2d-example
 */
public class PhysicsSystem extends IteratingSystem {
    private static final float MAX_STEP_TIME = 1 / 45f;
    private static float accumulator = 0f;

    private World world;
    private Array<Entity> bodiesQueue;

    private ComponentMapper<Components.BodyC> bm = ComponentMapper.getFor(Components.BodyC.class);
    private ComponentMapper<Components.TransformC> tm = ComponentMapper.getFor(Components.TransformC.class);

    public PhysicsSystem(World world) {
        super(Family.all(Components.BodyC.class, Components.TransformC.class).get());

        this.world = world;
        this.bodiesQueue = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        if (accumulator >= MAX_STEP_TIME) {
            world.step(MAX_STEP_TIME, 6, 2);
            accumulator -= MAX_STEP_TIME;

            //Entity Queue
            for (Entity entity : bodiesQueue) {
                Components.TransformC tfm = tm.get(entity);
                Components.BodyC bodyComp = bm.get(entity);
//                Vector2 position = bodyComp.body.getPosition();
//                tfm.position.x = position.x;
//                tfm.position.y = position.y;
//                tfm.rotation = bodyComp.body.getAngle() * MathUtils.radiansToDegrees;
            }
        }
        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}
