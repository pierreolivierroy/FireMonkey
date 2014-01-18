package game.firemonkey;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;

import com.bag.lib.math.Circle;
import com.bag.lib.math.OverlapTester;
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
    
    public Monkey monkey;
    public ArrayList<Banana> activeBananas;
    public ArrayList<Explosion> activeExplosions;
    
    public int state;
    public static final Vector2 gravity = new Vector2(0, -10);
    
    private float nextGenerationHeight;
    public float maxHeight = 0.0f; // Current max height reached by player.
    public int score;
    
    private Random rand;
    
    public World(WorldListener listener, GameUI gUI) {
    	
    	this.state = WORLD_STATE_RUNNING;
    	this.listener = listener;
    	this.gameUI = gUI;
    	
    	this.monkey = new Monkey(WORLD_WIDTH/2, 4);
    	this.activeBananas = new ArrayList<Banana>();
    	this.activeExplosions = new ArrayList<Explosion>();
    	
    	this.nextGenerationHeight = WORLD_HEIGHT/2;
    }

	public void update(float deltaTime, float accelX) {
		updatePlayer(deltaTime, accelX);
		updateLevel(deltaTime);
		updateExplosions(deltaTime);
		
		checkMonkeyBananaCollision();
		checkGameOver();
	}

	private void updatePlayer(float deltaTime, float accelX) {

		if (monkey.state == Monkey.PLAYER_STATE_FLYING || monkey.state == Monkey.PLAYER_STATE_STARTING) // Starting is DEBUG
			monkey.velocity.x = -accelX / 10 * Monkey.MOVE_VELOCITY;
		
	    if(monkey.state == Monkey.PLAYER_STATE_HIT) {
//	    	explosion = new Explosion(50, (int)player.position.x, (int)player.position.y);
//	    	player.state = player.previousState;
	    }
	    
	    monkey.update(deltaTime);
	    maxHeight = Math.max(monkey.position.y, maxHeight);
	}

	private void updateExplosions(float deltaTime) 
	{
		try{	
			for (Explosion e: activeExplosions) {
				e.update(deltaTime);
			}
		} catch(Exception e){}
	}
	
	private void updateLevel(float deltaTime)
	{
		// Generation if player is exiting an already filled zone
		if(monkey.position.y > nextGenerationHeight) {
			nextGenerationHeight += WORLD_HEIGHT;
			rand = new Random();
			
			for (int i = 0; i < 4; i++) {
				float xValue = rand.nextFloat() * WORLD_WIDTH;
				float yValue = (rand.nextFloat() * WORLD_HEIGHT) + nextGenerationHeight;
				Banana b = new Banana(xValue, yValue, 1, 1, 30.0f);
				activeBananas.add(b);
			}
		}
		
		// Remove clouds if out of view
		for (int i = 0; i < activeBananas.size(); i++) {
			Banana b = activeBananas.get(i);
			if(b.position.y <= monkey.position.y - WORLD_HEIGHT/2)
				activeBananas.remove(b);
		}
	}
	
	private void checkMonkeyBananaCollision()
	{
		for (int i = 0; i < activeBananas.size(); i++) {
			Banana b = activeBananas.get(i);
			
			// Collision
			if(OverlapTester.overlapRectangles(monkey.bounds, b.bounds)) {
				
				// BANANA EXPLOSION YO
				Explosion e = new Explosion(20, b.position.x, b.position.y);
				activeExplosions.add(e);
				
				score += b.points;
				monkey.bananaCollision(b.boostValue);
				
				activeBananas.remove(b);
			}
		}
	}
	
	private void checkGameOver()
	{
		if(monkey.position.y < maxHeight - WORLD_HEIGHT)
			state = WORLD_STATE_GAME_OVER;
	}
}

