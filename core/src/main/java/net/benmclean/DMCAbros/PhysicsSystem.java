package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Some code was copied from https://github.com/RoaringCatGames/libgdx-ashley-box2d-example
 */
public class PhysicsSystem extends IteratingSystem {
    private static final float MAX_STEP_TIME = 1 / 45f;
    private static float accumulator = 0f;

    private World world;

    public PhysicsSystem(World world) {
        super(Family.all(Components.BodyC.class, Components.TransformC.class).get());

        this.world = world;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        if (accumulator >= MAX_STEP_TIME) {
            world.step(MAX_STEP_TIME, 6, 2);
            accumulator -= MAX_STEP_TIME;
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {}
}
