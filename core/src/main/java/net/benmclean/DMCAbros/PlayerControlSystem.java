package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

/**
 * Some code was copied from https://www.gamedevelopment.blog/ashley-and-box2d-tutorial/
 */
public class PlayerControlSystem extends IteratingSystem {

    ComponentMapper<Components.PlayerC> pm; // gets player component of entity
    ComponentMapper<Components.BodyC> bodm; // gets body component
    ComponentMapper<Components.StateC> sm; // gets state component
    KeyboardController controller;  // our input controller


    @SuppressWarnings("unchecked")
    public PlayerControlSystem(KeyboardController keyCon) {
        // Set this system to process all entities with the PlayerComponent
        super(Family.all(Components.PlayerC.class).get());
        controller = keyCon; // assogn our reference to the controller
        // create our component mappers
        pm = ComponentMapper.getFor(Components.PlayerC.class);
        bodm = ComponentMapper.getFor(Components.BodyC.class);
        sm = ComponentMapper.getFor(Components.StateC.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //get the entity body
        Components.BodyC b2body = bodm.get(entity);
        // get the entity state
        Components.StateC state = sm.get(entity);
        // if body is going down set state falling
        if (b2body.body.getLinearVelocity().y > 0) {
            state.set(Components.StateC.STATE_FALLING);
        }
        // if body stationary on y axis
        if (b2body.body.getLinearVelocity().y == 0) {
            // only change to normal if previous state was falling(no mid air jump)
            if (state.get() == Components.StateC.STATE_FALLING) {
                state.set(Components.StateC.STATE_NORMAL);
            }
            // set state moving if not falling and moving on x axis
            if (b2body.body.getLinearVelocity().x != 0) {
                state.set(Components.StateC.STATE_MOVING);
            }
        }
        // apply forces depending on controller input
        if (controller.left) {
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, -5f, 0.2f), b2body.body.getLinearVelocity().y);
        }
        if (controller.right) {
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, 5f, 0.2f), b2body.body.getLinearVelocity().y);
        }

        if (!controller.left && !controller.right) {
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, 0, 0.1f), b2body.body.getLinearVelocity().y);
        }

        if (controller.up &&
                (state.get() == Components.StateC.STATE_NORMAL || state.get() == Components.StateC.STATE_MOVING)) {
            //b2body.body.applyForceToCenter(0, 3000,true);
            b2body.body.applyLinearImpulse(0, 75f, b2body.body.getWorldCenter().x, b2body.body.getWorldCenter().y, true);
            state.set(Components.StateC.STATE_JUMPING);
        }
    }
}