package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

public class MobSystem extends IteratingSystem {
    ComponentMapper<Components.BodyC> bodyM;
    ComponentMapper<Components.BehaviorI> behaviorM;
    ComponentMapper<Components.PacingBehavior> pacingM;

    @SuppressWarnings("unchecked")
    public MobSystem () {
        super(Family.all(Components.BehaviorI.class, Components.TransformC.class).get());
        bodyM = ComponentMapper.getFor(Components.BodyC.class);
        behaviorM = ComponentMapper.getFor(Components.BehaviorI.class);
        pacingM = ComponentMapper.getFor(Components.PacingBehavior.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        if (entity.getComponent(Components.PlayerC.class) != null) return; // PlayerControlSystem takes care of this.

        Components.PacingBehavior pacing = pacingM.get(entity);
        if (pacing != null && !pacing.active) {
            Components.BodyC bodyC = bodyM.get(entity);
            if (pacing.facingRight)
                bodyC.body.setLinearVelocity(MathUtils.lerp(bodyC.body.getLinearVelocity().x, 5f, 0.2f), bodyC.body.getLinearVelocity().y);
            else
                bodyC.body.setLinearVelocity(MathUtils.lerp(bodyC.body.getLinearVelocity().x, 0, 0.1f), bodyC.body.getLinearVelocity().y);
        }
    }
}
