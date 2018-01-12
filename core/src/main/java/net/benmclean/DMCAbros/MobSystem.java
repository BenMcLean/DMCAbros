package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class MobSystem extends IteratingSystem {
    @SuppressWarnings("unchecked")
    public MobSystem () {
        super(Family.all(Components.PacingBehavior.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        if (entity.getComponent(Components.PlayerC.class) != null) return; // PlayerControlSystem takes care of this.

        Components.PacingBehavior pacing = Components.pacingBehavior.get(entity);
        if (pacing != null && !pacing.active) {
            Components.BodyC bodyC = Components.bodyC.get(entity);
            if (pacing.facingRight)
                bodyC.body.applyForceToCenter(pacing.speed, 0f, true);
            else
                bodyC.body.applyForceToCenter(pacing.speed * -1f, 0f, true);
        }
    }
}
