package net.benmclean.DMCAbros;

import com.badlogic.gdx.Screen;

/**
 * Some code was copied from https://github.com/RoaringCatGames/libgdx-ashley-box2d-example
 */
public interface IScreenDispatcher {
    void endCurrentScreen();
    Screen getNextScreen();
}
