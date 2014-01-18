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
    
    //Banana patterns
    public static final int BANANA_PATTERN_BLANK_MIN = 5;
    public static final int BANANA_PATTERN_RANDOM_MIN = 70;//70%
    public static final int BANANA_PATTERN_STACK_MIN = 85;//15%
    public static final int BANANA_PATTERN_RECTANGLE_MIN = 95;//10%
    public static final int BANANA_PATTERN_DIAGONAL_MIN = 100;//5%
    float minWidth = 0.5f;
	float maxWidth = WORLD_WIDTH - 0.5f;
    
    public final WorldListener listener;
    public GameUI gameUI;
    
    public Monkey monkey;
    public Barrel activeBarrel;
    public ArrayList<Banana> activeBananas;
    public ArrayList<Explosion> activeExplosions;
    
    public int state;
    public static final Vector2 gravity = new Vector2(0, -10);
      
    private float nextGenerationHeight;
    private float lastBarrelHeight;
    
    public float maxHeight = 0.0f; 
    public float levelTargetHeight = 1000.0f; // DEBUG VALUE
    
    public int score; 			// Overall final score
    private int bananaScore; 		// Score based on nb of bananas consumed
    
    private Random rand;
    
    public World(WorldListener listener, GameUI gUI) {
    	
    	this.state = WORLD_STATE_RUNNING;
    	this.listener = listener;
    	this.gameUI = gUI;
    	
    	this.monkey = new Monkey(WORLD_WIDTH/2, Monkey.PLAYER_HEIGHT/2);
    	this.activeBananas = new ArrayList<Banana>();
    	this.activeExplosions = new ArrayList<Explosion>();
    	
    	this.nextGenerationHeight = WORLD_HEIGHT/2;
    	this.lastBarrelHeight = WORLD_HEIGHT * 4; // DEBUG
    	
    	this.score = 0;
    	this.bananaScore = 0;
    }

	public void update(float deltaTime, float accelX) {
		generateBarrel();
		
		updatePlayer(deltaTime, accelX);
		updateBananas(deltaTime);
		updateBarrel(deltaTime);
		updateLevel(deltaTime);
		updateExplosions(deltaTime);
		
		checkMonkeyBananaCollision();
		checkMonkeyBarrelCollision();
		
		updateScore();
		checkGameOver();
	}

	private void updatePlayer(float deltaTime, float accelX) {

		if (monkey.state == Monkey.PLAYER_STATE_FLYING || monkey.state == Monkey.PLAYER_STATE_FALLING) // Starting is DEBUG
			monkey.velocity.x = -accelX / 10 * Monkey.MOVE_VELOCITY;
		
	    if(monkey.state == Monkey.PLAYER_STATE_HIT) {
//	    	explosion = new Explosion(50, (int)player.position.x, (int)player.position.y);
//	    	player.state = player.previousState;
	    }
	    
	    monkey.update(deltaTime);
	    
	    maxHeight = Math.max(monkey.position.y, maxHeight);
	    if(maxHeight >= levelTargetHeight)
	    {
	    	// UNLOCK LEVEL X
	    }
	}
	
	private void updateBananas(float deltaTime)
	{
		try{	
			for (int i = 0; i < activeBananas.size(); i++) {
				Banana b = activeBananas.get(i);
				b.update(deltaTime);
			}
		} catch(Exception e){}
	}
	
	private void updateBarrel(float deltaTime)
	{
		if(activeBarrel == null)
			return;
		
		activeBarrel.update(deltaTime);
		if(activeBarrel.position.y <= monkey.position.y - WORLD_HEIGHT/2)
			activeBarrel = null;
	}

	private void updateExplosions(float deltaTime) 
	{
		try{	
			for (int i = 0; i < activeExplosions.size(); i++) { 
				Explosion e = activeExplosions.get(i);
				e.update(deltaTime);
				
				 if(e.state == Explosion.STATE_DEAD)
					 activeExplosions.remove(i);
			}
		} catch(Exception e){}
	}
	
	private void updateLevel(float deltaTime)
	{
		// Generation if player is exiting an already filled zone
		if(monkey.position.y > nextGenerationHeight) {
			nextGenerationHeight += WORLD_HEIGHT;
			rand = new Random();
			
			Random r = new Random();
			int min = 0;
			int max = 100;
			int pattern = r.nextInt(max-min) + min;			

			if(min <= pattern && pattern <= BANANA_PATTERN_RANDOM_MIN)
				generateRandomBananaPattern();
			else if(BANANA_PATTERN_RANDOM_MIN <= pattern && pattern <= BANANA_PATTERN_STACK_MIN)
				generateStackBananaPattern();
			else if(BANANA_PATTERN_RECTANGLE_MIN <= pattern && pattern <= BANANA_PATTERN_DIAGONAL_MIN)
				generateRectangleBananaPattern();
			else if(BANANA_PATTERN_DIAGONAL_MIN <= pattern)
				generateDiagonalBananaPattern();		
			
			removeBananas();
		}

	}
	
private void generateDiagonalBananaPattern(){
		
		for (int i = 0; i < 25; i++) {
			float xValue = rand.nextFloat() * WORLD_WIDTH;
			float yValue = (rand.nextFloat() * WORLD_HEIGHT) + nextGenerationHeight;
			Banana b = new Banana(xValue, yValue, 1, 1, Math.max(Banana.BOOST_MED, monkey.velocity.y));
			activeBananas.add(b);
		}
	}
	
	private void generateRandomBananaPattern(){
		
		for (int i = 0; i < 4; i++) {
			float xValue = rand.nextFloat() * (WORLD_WIDTH - 1f) + 0.5f;
			float yValue = (rand.nextFloat() * WORLD_HEIGHT) + nextGenerationHeight;
			Banana b = new Banana(xValue, yValue, 1, 1, Math.max(Banana.BOOST_MED, monkey.velocity.y));
			activeBananas.add(b);
		}
	}
	
	private void generateRectangleBananaPattern(){
						
		nextGenerationHeight += 25f;
		
		Random r = new Random();
		int minWidth = 2;
		int maxWidth = 6;
		int minHeight = 2;
		int maxHeight = 10;
		int rectangleWidth = r.nextInt(maxWidth-minWidth) + minWidth;
		int rectangleHeight = r.nextInt(maxHeight-minHeight) + minHeight;
		/*rectangleWidth = 6;
		rectangleHeight = 10;*/
		
		float incrementX = 0f;	
		float incrementY = 0f;
					
		float xValue = (WORLD_WIDTH/(float)rectangleWidth);

		float yValue = (rand.nextFloat() * WORLD_HEIGHT) + nextGenerationHeight;		
		float x = xValue + incrementX;
		float y = yValue + incrementY;			
		
		Banana b = new Banana(xValue, yValue, 1, 1, Math.max(Banana.BOOST_MED, monkey.velocity.y));
		activeBananas.add(b);
		
		for(int i = 0; i < rectangleHeight; i++){
			
			incrementX = 0f;
			x = xValue + incrementX;
			
			for(int j = 0; j < rectangleWidth; j++){
				
				x = xValue + incrementX;						
				Banana bb = new Banana(x, y, 1, 1, Math.max(Banana.BOOST_MED, monkey.velocity.y));
				activeBananas.add(bb);
				incrementX += 1.5f;
			}
					
			incrementY += 1.5f;
			y = yValue + incrementY;	
		}
	}
	
	private void generateStackBananaPattern(){
		
		Random r = new Random();
		int minBananas = 2;
		int maxbananas = 10;
		int nbBananas = r.nextInt(maxbananas-minBananas) + minBananas;
		
		float increment = 1.5f;
		float minSpacer = 1.5f;
		float maxSpacer = 5.0f;
					
		float xValue = rand.nextFloat() * (maxWidth - minWidth) + minWidth;
		float yValue = (rand.nextFloat() * WORLD_HEIGHT) + nextGenerationHeight;
		
		Banana b = new Banana(xValue, yValue, 1, 1, Math.max(Banana.BOOST_HIGH, monkey.velocity.y));
		activeBananas.add(b);
		
		for(int i = 0; i < nbBananas; i++){
			
			float y = yValue + ((float)increment);			
			Banana bb = new Banana(xValue, y, 1, 1, Math.max(Banana.BOOST_HIGH, monkey.velocity.y));
			activeBananas.add(bb);
			increment += 1.5f;
		}
	}
	
	private void removeBananas(){
		
		// Remove bananas if out of view
		for (int i = 0; i < activeBananas.size(); i++) {
			Banana b = activeBananas.get(i);
			if(b.position.y <= monkey.position.y - WORLD_HEIGHT/2)
				activeBananas.remove(b);
		}
	}
	
	private void updateScore()
	{
		score = (int) (bananaScore + maxHeight);
	}
	
	private void checkMonkeyBananaCollision()
	{
		for (int i = 0; i < activeBananas.size(); i++) {
			Banana b = activeBananas.get(i);
			
			// Collision
			if(OverlapTester.overlapCircles(monkey.hitZone, b.hitZone)) {
				
				// BANANA EXPLOSION YO
				activeExplosions.add(new Explosion(10, (int)b.position.x, (int)b.position.y, 0.5f));
				
				bananaScore += b.points;
				monkey.bananaCollision(b.boostValue);
				
				activeBananas.remove(b);
			}
		}
	}
	
	private void checkMonkeyBarrelCollision()
	{
		if(activeBarrel == null)
			return;
		
		if(OverlapTester.overlapCircles(monkey.hitZone, activeBarrel.hitZone)) {
						
			monkey.barrelCollision(activeBarrel.position);
			activeBarrel.state = Barrel.STATE_MONKEY_IN;
		}
	}
	
	private void generateBarrel()
	{
		if(activeBarrel != null || maxHeight < 8*WORLD_HEIGHT)	// Only 1 barrel at a time
			return;
		
		rand = new Random();
		float odds = rand.nextFloat();
		
		if(odds > 0.05f && odds < 0.15f) {
			float xValue = rand.nextFloat() * WORLD_WIDTH;
			float yValue = (rand.nextFloat() * WORLD_HEIGHT) + nextGenerationHeight;
			activeBarrel = new Barrel(xValue, yValue, 1.3f, 1.6f, BarrelSequence.DIFF_EASY);
		}
	}
	
	private void checkGameOver()
	{
		if(monkey.position.y < maxHeight - WORLD_HEIGHT)
			
        	// WRITE SCORE TO HIGHSCORE FOR LEVEL X
			
			state = WORLD_STATE_GAME_OVER;
	}
	
	 /******************************************
	  * 
	  * 	UI Touch interactions
	  * 
	  ******************************************/
	
	public void shootMonkey()
	{
		activeExplosions.add(new Explosion(30, (int)activeBarrel.position.x, (int)activeBarrel.position.y, 0.5f));
		activeBarrel = null;
		
		monkey.state = Monkey.PLAYER_STATE_FLYING;
		monkey.velocity.y = 60.0f;
	}
	
	public void touchToken(Vector2 touch)
	{
		for (int i = 0; i < activeBarrel.sequence.tokens.size(); i++) {
			BarrelToken bt = activeBarrel.sequence.tokens.get(i);
			
			if(OverlapTester.pointInRectangle(bt.bounds, touch)) {
				activeExplosions.add(new Explosion(20, (int)bt.position.x, (int)bt.position.y, 0.5f));
				
			}
		}
	}
}

