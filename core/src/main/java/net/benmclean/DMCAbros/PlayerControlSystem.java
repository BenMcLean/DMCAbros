package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

/**
 * Some code was copied from https://www.gamedevelopment.blog/ashley-and-box2d-tutorial/
 */
public class PlayerControlSystem extends IteratingSystem {
    KeyboardController controller;  // our input controller
    Vector2 walk = new Vector2();

    @SuppressWarnings("unchecked")
    public PlayerControlSystem(KeyboardController keyCon) {
        // Set this system to process all entities with the PlayerComponent
        super(Family.all(Components.PlayerC.class).get());
        controller = keyCon; // assogn our reference to the controller
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //get the entity body
        Components.BodyC b2body = Components.bodyC.get(entity);
        // get the entity state
        Components.StateC state = Components.stateC.get(entity);
        // get the Player component
        Components.PlayerC player = Components.playerC.get(entity);

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
//            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, -5f, 0.2f), b2body.body.getLinearVelocity().y);
            b2body.body.applyForceToCenter(player.speed * -1, 0f, true);
        }
        if (controller.right) {
            b2body.body.applyForceToCenter(player.speed, 0f, true);
        }

        if (controller.up &&
                (state.get() == Components.StateC.STATE_NORMAL || state.get() == Components.StateC.STATE_MOVING)) {
            //b2body.body.applyForceToCenter(0, 3000,true);
            b2body.body.applyForceToCenter(0, player.jump, true);
            state.set(Components.StateC.STATE_JUMPING);
        }

        // falling down pits
        if (b2body.body.getPosition().y < 0) b2body.body.setTransform(b2body.body.getPosition().x, 18f, 0f);
    }
}
