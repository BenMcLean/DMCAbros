package net.benmclean.DMCAbros;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Some code was copied from https://github.com/RoaringCatGames/libgdx-ashley-box2d-example
 */
public class Components {
    public enum TypeC implements Component {
        Mob("Mob"),
        Brick("Brick");
        private String string;

        TypeC(String string) {
            this.string = string;
        }

        public String toString() {
            return string;
        }
    }

    public static class TransformC implements Component {
        public final Vector2 scale = new Vector2(1.0f, 1.0f);
        public float z = 0;
        public boolean isHidden = false;
    }

    public static class BodyC implements Component {
        public Body body;
    }

    public static class TextureRegionC implements Component {
        public TextureRegion region;
    }

    public static class CollisionC implements Component {
        public Entity collisionEntity;
    }

    public static class PlayerC implements Component {

    }

    public static class StateC implements Component {
        public static final int STATE_NORMAL = 0;
        public static final int STATE_JUMPING = 1;
        public static final int STATE_FALLING = 2;
        public static final int STATE_MOVING = 3;
        public static final int STATE_HIT = 4;

        private int state = 0;
        public float time = 0.0f;
        public boolean isLooping = false;

        public void set(int newState) {
            state = newState;
            time = 0.0f;
        }

        public int get() {
            return state;
        }
    }
}
