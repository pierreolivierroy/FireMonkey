package game.firemonkey;

import java.util.ArrayList;

import android.util.Log;

import com.bag.lib.math.Vector2;


public class World {
	
	// Interface, mostly used to access sound effects
    public interface WorldListener {
          //public void sound();
		  int getTime();
    }

    // World's size
    public static final float WORLD_WIDTH 			= 9;
    public static final float WORLD_HEIGHT 			= 16;
    
    // World's states
    public static final int WORLD_STATE_RUNNING 	= 0;
    public static final int WORLD_STATE_NEXT_LEVEL 	= 1;
    public static final int WORLD_STATE_GAME_OVER 	= 2;
    
    public final WorldListener listener;
    public GameUI gameUI;
    
    public Player player;
    
    public int state;

    public World(WorldListener listener, GameUI gUI) {
    	
    	this.state = WORLD_STATE_RUNNING;
    	this.listener = listener;
    	this.gameUI = gUI;
    	
    	this.player = new Player(WORLD_WIDTH/2, 1);

    }

	public void update(float deltaTime, float accelX) {
		updatePlayer(deltaTime, accelX);
		updateExplosions(deltaTime);
	}

	private void updatePlayer(float deltaTime, float accelX) {

		if (player.state == Player.PLAYER_STATE_FLYING || player.state == Player.PLAYER_STATE_STARTING) // Starting is DEBUG
			player.velocity.x = -accelX / 10 * Player.MOVE_VELOCITY;
		
	    if(player.state == Player.PLAYER_STATE_HIT) {
//	    	explosion = new Explosion(50, (int)player.position.x, (int)player.position.y);
//	    	player.state = player.previousState;
	    }
	    
	    player.update(deltaTime);
	}
	


	private void updateExplosions(float deltaTime) 
	{
//		try{	
//			explosion.update(deltaTime);
//		} catch(Exception e){}
	}
}

