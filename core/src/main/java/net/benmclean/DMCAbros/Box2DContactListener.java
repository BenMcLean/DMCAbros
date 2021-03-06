package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Some code copied from https://www.gamedevelopment.blog/ashley-and-box2d-tutorial/
 */
public class Box2DContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        System.out.println("Contact");
        // get fixtures
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        System.out.println(fa.getBody().getType() + " has hit " + fb.getBody().getType());
        // check if either fixture has an Entity object stored in the body's userData
        if (fa.getBody().getUserData() instanceof Entity) {
            Entity ent = (Entity) fa.getBody().getUserData();
            entityCollision(ent, fb);
            return;
        } else if (fb.getBody().getUserData() instanceof Entity) {
            Entity ent = (Entity) fb.getBody().getUserData();
            entityCollision(ent, fa);
            return;
        }
    }

    private void entityCollision(Entity ent, Fixture fb) {
        // check the collided Entity is also an Entity
        if (fb.getBody().getUserData() instanceof Entity) {
            Entity colEnt = (Entity) fb.getBody().getUserData();
            // get the components for this entity
            Components.CollisionC col = ent.getComponent(Components.CollisionC.class);
            Components.CollisionC colb = colEnt.getComponent(Components.CollisionC.class);

            // set the CollisionEntity of the component
            if (col != null) {
                col.collisionEntity = colEnt;
            } else if (colb != null) {
                colb.collisionEntity = ent;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        System.out.println("Contact end");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
