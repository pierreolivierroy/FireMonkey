package game.firemonkey;

import java.util.ArrayList;
import java.util.Random;

import com.bag.lib.math.OverlapTester;
import com.bag.lib.math.Vector2;

public class World {

	// Interface, mostly used to access sound effects
	public interface WorldListener {
		//public void sound();
		int getTime();
		void playBananaHit();
	}

	// World's size
	public static final float WORLD_WIDTH 			= 9;
	public static final float WORLD_HEIGHT 			= 16;

	// World's states
	public static final int WORLD_STATE_RUNNING 	= 0;
	public static final int WORLD_STATE_NEXT_LEVEL 	= 1;
	public static final int WORLD_STATE_GAME_OVER 	= 2;

	// Level
	public static int currentLevel = 1;

	//Banana patterns
	public static final int BANANA_PATTERN_BLANK_MIN		= 3;//5%
	public static final int BANANA_PATTERN_RANDOM_MIN		= 45;//60%
	public static final int BANANA_PATTERN_STACK_MIN 		= 75;//15%
	public static final int BANANA_PATTERN_RECTANGLE_MIN 	= 80;//10%
	public static final int BANANA_PATTERN_DIAGONAL_MIN 	= 95;//10%
	public static final int BANANA_PATTERN_BANANA			= 98;//10%

	float minWidth = 0.5f;
	float maxWidth = WORLD_WIDTH - 0.5f;
	boolean showBanana = false;

	public final WorldListener listener;
	public GameUI gameUI;

	public Monkey monkey;
	public Barrel activeBarrel;
	public ArrayList<Banana> activeBananas;
	public ArrayList<Explosion> activeExplosions;

	public int state;
	public static Vector2 gravity;

	private float nextGenerationHeight;
	private float lastBarrelHeight;

	public float maxHeight = 0.0f; 
	public float levelTargetHeight = 10000.0f; // DEBUG VALUE
	public float lastHeight;
	public float screenIncrement;
	public float repeatIncrement_1;
	public float repeatIncrement_2;

	public int score; 			// Overall final score
	private int bananaScore; 		// Score based on nb of bananas consumed

	private Random rand;

	public float debug = 0;
	
	public World(WorldListener listener, GameUI gUI) {

		this.state = WORLD_STATE_RUNNING;
		this.listener = listener;
		this.gameUI = gUI;

		this.monkey = new Monkey(WORLD_WIDTH/2, Monkey.PLAYER_HEIGHT/2, Monkey.PLAYER_DEFAULT_JUMPS);
		this.activeBananas = new ArrayList<Banana>();
		this.activeExplosions = new ArrayList<Explosion>();

		this.nextGenerationHeight = 0;
		this.lastBarrelHeight = WORLD_HEIGHT * 4; // DEBUG

		this.score = 0;
		this.bananaScore = 0;

		World.gravity = new Vector2(0, -11 - currentLevel);
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

	float threshold_1 = 16;
	float threshold_2 = 16;
	
	private void updatePlayer(float deltaTime, float accelX) {

		if (monkey.state == Monkey.PLAYER_STATE_FLYING || monkey.state == Monkey.PLAYER_STATE_FALLING) // Starting is DEBUG
			monkey.velocity.x = -accelX / 10 * Monkey.MOVE_VELOCITY;
		
		lastHeight = monkey.position.y;
		monkey.update(deltaTime);
		
		if(monkey.position.y > lastHeight && lastHeight >= 32) {
			screenIncrement += (monkey.position.y - lastHeight)/100;
		}
		
		if(screenIncrement >= threshold_1) {
			repeatIncrement_1 ++;
			threshold_1 += WORLD_HEIGHT;
		}
		if(screenIncrement >= threshold_2) {
			repeatIncrement_2 ++;
			threshold_2 += WORLD_HEIGHT;
		}

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

		if(monkey.state == Monkey.PLAYER_STATE_BONUS)
			updateBarrelSequence(deltaTime);

		if(activeBarrel != null && activeBarrel.position.y <= monkey.position.y - WORLD_HEIGHT/2)
			activeBarrel = null;
	}

	private void updateBarrelSequence(float deltaTime)
	{
		activeBarrel.sequence.update(deltaTime);

		if(activeBarrel.sequence.state == BarrelSequence.STATE_DEAD) {
			endBarrel();
		}
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
			nextGenerationHeight += WORLD_HEIGHT + 4;
			rand = new Random();

			Random r = new Random();
			int min = 0;
			int max = 100;
			int pattern = r.nextInt(max-min) + min;		

			if(levelTargetHeight < monkey.position.y && !showBanana){
				generateBananaPattern();
				showBanana = true;
			} else {
				//Generate banana petterns randomly according to their stats
				if(BANANA_PATTERN_BLANK_MIN < pattern && pattern <= BANANA_PATTERN_RANDOM_MIN)
					generateRandomBananaPattern(5 - currentLevel);
				else if(BANANA_PATTERN_RANDOM_MIN < pattern && pattern <= BANANA_PATTERN_STACK_MIN)
					generateStackBananaPattern();
				else if(BANANA_PATTERN_RECTANGLE_MIN < pattern && pattern <= BANANA_PATTERN_DIAGONAL_MIN)
					generateRectangleBananaPattern();
				else if(BANANA_PATTERN_DIAGONAL_MIN < pattern && pattern <= BANANA_PATTERN_BANANA)
					generateDiagonalBananaPattern();
				else if(BANANA_PATTERN_BANANA < pattern && pattern <= max)
					generateBananaPattern();
			}
			//generateRandomBananaPattern(4 - currentLevel);	
		}		
		removeBananas();
	}

	private void generateBananaPattern(){

		float incrementX = 0.0f;
		float incrementY = 1.5f;

		float xValue = 1.5f;
		float yValue = (rand.nextFloat() * WORLD_HEIGHT) + nextGenerationHeight;

		for(int i = 0; i < 8; i++){

			float x = xValue + incrementX;
			float y = yValue + incrementY;			
			Banana bb = new Banana(x, y, 1, 1, Banana.BOOST_HIGH, Banana.POINTS_MED);

			activeBananas.add(bb);
			if(i > 3)
				incrementX += 0.4f;
			else if(i > 6)
				incrementX += 0.2f;
			else
				incrementX += 0.5f;

			incrementY += 1.5f;
		}

		incrementX = 1.5f;
		incrementY = 0.8f; 

		for (int i = 0; i < 3; i++) {
			float x = xValue + incrementX;
			float y = yValue + incrementY;			
			Banana bb = new Banana(x, y, 1, 1, Banana.BOOST_HIGH, Banana.POINTS_MED);

			activeBananas.add(bb);
			if(i == 2){
				incrementX += 1.0f;
				incrementY += 1.5f;
			}
			else {
				incrementX += 1.5f;
				incrementY += 0.8f;
			}
		}

		for (int i = 0; i < 3; i++) {
			float x = xValue + incrementX;
			float y = yValue + incrementY;			
			Banana bb = new Banana(x, y, 1, 1, Banana.BOOST_HIGH, Banana.POINTS_MED);

			activeBananas.add(bb);
			incrementX += 0.3f;
			incrementY += 1.5f;
		}

		incrementX -= 0.3f;
		incrementY += 0.3f;

		for (int i = 0; i < 4; i++) {
			float x = xValue + incrementX;
			float y = yValue + incrementY;			
			Banana bb = new Banana(x, y, 1, 1, Banana.BOOST_HIGH, Banana.POINTS_MED);

			activeBananas.add(bb);

			if(i == 4){
				incrementX -= 0.8f;
				incrementY += 0.0f;
			}
			else {
				incrementX -= 0.8f;
				incrementY += 1.5f;
			}	
		}

		incrementX -= 0.5f;
		incrementY -= 1.5f;

		float x = xValue + incrementX;
		float y = yValue + incrementY;			
		Banana bb = new Banana(x, y, 1, 1, Banana.BOOST_HIGH, Banana.POINTS_MED);
		activeBananas.add(bb);

	}

	private void generateDiagonalBananaPattern(){

		Random r = new Random();
		Random randDirection = new Random();
		int minD = 0;
		int maxD = 1;
		int direction = randDirection.nextInt(maxD-minD) + minD;	
		int minBananas = 8;
		int maxBananas = 14;
		int bananas = r.nextInt(maxBananas-minBananas) + minBananas;		

		//To the right
		if(direction == 0){
			float incrementX = WORLD_WIDTH/(float)bananas;
			float incrementY = 0f;
			float xValue = incrementX;
			float yValue = (rand.nextFloat() * WORLD_HEIGHT) + nextGenerationHeight;
			float x = xValue - 0.5f;
			float y = yValue + incrementY;	

			for (int i = 0; i < bananas; i++) {

				Banana b = new Banana(x, y, 1, 1, Banana.BOOST_MED, Banana.POINTS_MED);
				activeBananas.add(b);

				incrementY += 1.5f;
				x += incrementX;
				y = yValue + incrementY;
			}
		}

		//To the left
		else if(direction == 1){
			float incrementX = WORLD_WIDTH/(float)bananas;
			float incrementY = 0f;
			float xValue = WORLD_WIDTH - 0.5f;
			float yValue = (rand.nextFloat() * WORLD_HEIGHT) + nextGenerationHeight;
			float x = xValue;
			float y = yValue + incrementY;	

			for (int i = 0; i < bananas; i++) {

				Banana b = new Banana(x, y, 1, 1, Banana.BOOST_MED, Banana.POINTS_MED);
				activeBananas.add(b);

				incrementY += 1.5f;
				x -= incrementX;
				y = yValue + incrementY;
			}
		}
		// Remove clouds if out of view

	}

	private void generateRandomBananaPattern(int number){

		for (int i = 0; i < number; i++) {
			float xValue = rand.nextFloat() * (WORLD_WIDTH - 1f) + 0.5f;
			float yValue = (rand.nextFloat() * WORLD_HEIGHT) + nextGenerationHeight;

			//Generate random banana size/points/boost
			float bananaSize = randomizeBananaSize();
			int points = 120 - ((int)bananaSize * 10);
			float boost = bananaSize * 24;

			Banana b = new Banana(xValue, yValue, bananaSize, bananaSize, boost, points);

			//Check collisions
			boolean collision = false;

			for (int j = 0; j < activeBananas.size(); j++) {

				if(OverlapTester.overlapCircles(activeBananas.get(j).hitZone, b.hitZone)){
					collision = true;
					break;
				}
			}

			//If no collisions
			if(collision == false)
				activeBananas.add(b);

		}
	}

	private void generateRectangleBananaPattern(){

		Random r = new Random();
		int minWidth = 2;
		int maxWidth = 5;
		int minHeight = 2;
		int maxHeight = 8;
		int rectangleWidth = r.nextInt(maxWidth-minWidth) + minWidth;
		int rectangleHeight = r.nextInt(maxHeight-minHeight) + minHeight;

		float incrementX = 0f;	
		float incrementY = 0f;

		float xValue = (WORLD_WIDTH/(float)rectangleWidth);

		float yValue = (rand.nextFloat() * WORLD_HEIGHT) + nextGenerationHeight;		
		float x = xValue + incrementX;
		float y = yValue + incrementY;			

		for(int i = 0; i < rectangleHeight; i++){

			incrementX = 0f;
			x = xValue + incrementX;

			for(int j = 0; j < rectangleWidth; j++){

				x = xValue + incrementX;						
				Banana bb = new Banana(x, y, 1, 1, Banana.BOOST_MED, Banana.POINTS_MED);
				activeBananas.add(bb);
				incrementX += 1.5f;
			}

			incrementY += 1.5f;
			y = yValue + incrementY;	
		}
	}

	private void generateStackBananaPattern(){

		Random r = new Random();
		int minBananas = 4;
		int maxbananas = 10	;
		int nbBananas = r.nextInt(maxbananas-minBananas) + minBananas;

		float increment = 1.5f;
		float minSpacer = 1.0f;
		float maxSpacer = 3.0f;

		float xValue = rand.nextFloat() * (maxWidth - minWidth) + minWidth;
		float yValue = (rand.nextFloat() * WORLD_HEIGHT) + nextGenerationHeight;

		Banana b = new Banana(xValue, yValue, 1, 1, Banana.BOOST_HIGH, Banana.POINTS_MED);

		activeBananas.add(b);

		for(int i = 0; i < nbBananas; i++){

			float y = yValue + ((float)increment);			
			Banana bb = new Banana(xValue, y, 1, 1, Banana.BOOST_HIGH, Banana.POINTS_MED);

			activeBananas.add(bb);
			increment += 1.5f;
		}
	}

	private float randomizeBananaSize(){
		float minSize = 0.8f;
		float maxSize = 1.5f;

		Random r = new Random();		

		return r.nextFloat() * (maxSize - minSize) + minSize;
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
				activeExplosions.add(new Explosion(10, (int)b.position.x, (int)b.position.y, 0.2f));

				bananaScore += b.points;
				monkey.bananaCollision(b.boostValue);
				listener.playBananaHit();

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

			if(activeBarrel.sequence == null) {
				activeBarrel.generateSequence(World.currentLevel, monkey.position.y);
			}
		}
	}

	private void generateBarrel()
	{
		if(activeBarrel != null || maxHeight < 8*WORLD_HEIGHT)	// Only 1 barrel at a time
			return;

		rand = new Random();
		float odds = rand.nextFloat();

		if(odds > 0.985f && odds < 0.989f) {
			float xValue = (rand.nextFloat() * WORLD_WIDTH - 1) + 1;
			float yValue = (rand.nextFloat() * WORLD_HEIGHT) + nextGenerationHeight;

			if(maxHeight > 3000)
				activeBarrel = new Barrel(xValue, yValue, 1.3f, 1.6f);
			else 
				activeBarrel = new Barrel(xValue, yValue, 1.3f, 1.6f);
		}
	}

	private void checkGameOver()
	{
		if(monkey.position.y < maxHeight - WORLD_HEIGHT)

			// WRITE SCORE TO HIGHSCORE FOR LEVEL X

			state = WORLD_STATE_GAME_OVER;
	}

	public void shootMonkey()
	{
		activeExplosions.add(new Explosion(30, (int)activeBarrel.position.x, (int)activeBarrel.position.y, 0.4f));
		activeBarrel = null;

		monkey.state = Monkey.PLAYER_STATE_FLYING;
		monkey.velocity.y = 60.0f;
	}

	public void touchToken(Vector2 touch)
	{
		for (int i = 0; i < activeBarrel.sequence.tokens.size(); i++) {
			BarrelToken bt = activeBarrel.sequence.tokens.get(i);

			if(OverlapTester.pointInRectangle(bt.bounds, touch)) {
				activeExplosions.add(new Explosion(20, (int)bt.position.x, (int)bt.position.y, 1.5f));
				activeBarrel.sequence.inputSequence(bt);				
			}
		}

		if(activeBarrel.sequence.state == BarrelSequence.STATE_DEAD) {
			endBarrel();
		}		
	}

	public void endBarrel(){
		activeExplosions.add(new Explosion(30, (int)activeBarrel.position.x, (int)activeBarrel.position.y, 1.8f));

		monkey.state = Monkey.PLAYER_STATE_FLYING;
		if(activeBarrel.sequence.completionBonus <= 15) {
			activeBarrel.sequence.completionBonus = 15.0f;
		}

		monkey.velocity.y = activeBarrel.sequence.completionBonus;
        if(activeBarrel.sequence.totalSuccess) {
            monkey.jump++;
        }

		activeBarrel = null;
	}
}

