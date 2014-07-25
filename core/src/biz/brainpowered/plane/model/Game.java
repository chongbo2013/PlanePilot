package biz.brainpowered.plane.model;

/**
 * Created by sebastian on 2014/07/24.
 */
public class Game {

    private static PlayerState playerState;

    public static float planeScale = 1.0f;

    // todo: implement config loader

    // todo: decide what player states exist (inactive input, scripted, ai, normal)
    public enum PlayerState {
        NORMAL
    };

    public static void setPlayerState (PlayerState state) {
        playerState = state;
    }

    public static PlayerState getPlayerState () {
        return playerState;
    }
}
