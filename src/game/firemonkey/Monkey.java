package game.firemonkey;

import android.util.Log;

import com.bag.lib.DynamicGameObject;

public class Monkey extends DynamicGameObject {

    public static final float PLAYER_WIDTH 			= 1.4f;
    public static final float PLAYER_HEIGHT 		= 1.4f;
    public static final float PLAYER_FLOOR_POSITION = 0.5f + PLAYER_HEIGHT/2;
    public static final float PLAYER_MAX_VELOCITY	= 12.0f;
    
    public static final int PLAYER_STATE_STARTING 	= 0;
    public static final int PLAYER_STATE_FLYING 	= 1;
    public static final int PLAYER_STATE_FALLING 	= 2;
    public static final int PLAYER_STATE_HIT 		= 3;
    public static final int PLAYER_STATE_BONUS 		= 4;
    
    public static final float ROCKET_ACCELERATION	= 5.0f;
    public static final float MOVE_VELOCITY = 25.0f;
    
    public int state;   
    public int previousState;
    
    private float stateTime;   
        
	public Monkey(float x, float y) {
		super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		this.state = PLAYER_STATE_STARTING;
	}

    public void update(float deltaTime) {  
    	
        velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
        
        // Sides stuff
        if(position.x < 0)
            position.x = World.WORLD_WIDTH;
        if(position.x > World.WORLD_WIDTH)
            position.x = 0;
        
        // Flying / falling stuff
        if(velocity.y > 0 && state != PLAYER_STATE_FLYING) {
            state = PLAYER_STATE_FLYING;
            stateTime = 0;
        }
        
        if(velocity.y < 0 && state != PLAYER_STATE_FALLING) {
            state = PLAYER_STATE_FALLING;
            stateTime = 0;
        }
        
        // Ground collision (initial)
        if(position.y < PLAYER_HEIGHT/2)
        	position.y = PLAYER_HEIGHT/2;
        
        stateTime += deltaTime;        
    }
    
    public void bananaCollision(float boost)
    {
    	this.velocity.y = boost;
    }
}
