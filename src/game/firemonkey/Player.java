package game.firemonkey;

import com.bag.lib.DynamicGameObject;

public class Player extends DynamicGameObject {

    public static final float PLAYER_WIDTH 			= 1.4f;
    public static final float PLAYER_HEIGHT 		= 1.4f;
    public static final float PLAYER_FLOOR_POSITION = 0.5f + PLAYER_HEIGHT/2;
    public static final float PLAYER_MAX_VELOCITY	= 12.0f;
    
    public static final int PLAYER_STATE_IDLE 		= 0;
    public static final int PLAYER_STATE_RUNNING 	= 1;
    public static final int PLAYER_STATE_FLYING 	= 2;
    public static final int PLAYER_STATE_FALLING 	= 3;
    public static final int PLAYER_STATE_HIT_WALL 	= 9;

    
    public static final float JETPACK_ACCELERATION	= 5.0f;

    float moveX = 15.0f; // Test value that makes the pacman move in X
    public int state;
    public int previousState;

    
	public Player(float x, float y) {
		super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		this.state = PLAYER_STATE_IDLE;
	}

}
