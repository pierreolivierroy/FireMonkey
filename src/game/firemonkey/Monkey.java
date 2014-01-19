package game.firemonkey;

import com.bag.lib.DynamicGameObject;
import com.bag.lib.math.Circle;
import com.bag.lib.math.Vector2;

public class Monkey extends DynamicGameObject {

    public static final float PLAYER_WIDTH 			= 1.8f;
    public static final float PLAYER_HEIGHT 		= 1.8f;
    public static final float PLAYER_FLOOR_POSITION = 0.5f + PLAYER_HEIGHT/2;
    public static final float PLAYER_MAX_VELOCITY	= 12.0f;
    
    public static final int PLAYER_STATE_STARTING 	= 0;
    public static final int PLAYER_STATE_FLYING 	= 1;
    public static final int PLAYER_STATE_FALLING 	= 2;
    public static final int PLAYER_STATE_HIT 		= 3;
    public static final int PLAYER_STATE_BONUS 		= 4;
    
    public static final float ROCKET_ACCELERATION	= 5.0f;
    public static final float MOVE_VELOCITY = 25.0f;
    
    public static final int PLAYER_DEFAULT_JUMPS 	= 5;
    public static final float PLAYER_BOOST_MAX_HEIGHT = 40f;
    
    public int state;   
    public int previousState;
    public Circle hitZone;
    public int jump;
    public float jumpHeight = 0f;
    public boolean firstJump = true;
    public boolean jumpBoostActive = false;
    public boolean hasFellSinceBarrel = true;
    
    //private float stateTime;   
    public float immuneTime;
        
	public Monkey(float x, float y, int jump) {
		super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		this.state = PLAYER_STATE_STARTING;
		this.hitZone = new Circle(x, y, PLAYER_HEIGHT/3);
		this.jump = jump;
	}

    public void update(float deltaTime) {  
    	
    	if(state != PLAYER_STATE_BONUS) {
	        velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
	        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
    	}
    	
        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
        hitZone.center.set(position);
        
        // Sides stuff
        if(position.x < 0)
            position.x = World.WORLD_WIDTH;
        if(position.x > World.WORLD_WIDTH)
            position.x = 0;
        
        // Flying / falling stuff
        if(velocity.y > 0 && state != PLAYER_STATE_FLYING) {
            state = PLAYER_STATE_FLYING;
            //stateTime = 0;
        }
        
        if(velocity.y < 0 && state != PLAYER_STATE_FALLING) {
            state = PLAYER_STATE_FALLING;
            hasFellSinceBarrel = true;
            //stateTime = 0;
        }
        
        if(immuneTime > 0)
        	immuneTime -= deltaTime;
        
        // Ground collision (initial)
        if(position.y < PLAYER_HEIGHT/2)
        	position.y = PLAYER_HEIGHT/2;
        
        //stateTime += deltaTime;        
    }
    
    public void bananaCollision(float boost) 
    {
    	this.velocity.y = Math.max(boost, this.velocity.y);
    }
    
    public void barrelCollision(Vector2 barrelPos) 
    {
    	if(state != PLAYER_STATE_BONUS) {
	    	this.state = PLAYER_STATE_BONUS;
	    	this.position.set(barrelPos);
	    	this.velocity.set(0,0);
	    	this.hasFellSinceBarrel = false;
    	}
    }
}
