package game.firemonkey;

import java.util.ArrayList;
import java.util.Random;

import com.bag.lib.math.OverlapTester;
import com.bag.lib.math.Vector2;

public class BarrelSequence {

	public final static int DIFF_EASY 	= 0;
	public final static int DIFF_MEDIUM = 1;
	public final static int DIFF_HARD 	= 2;
	
	public ArrayList<BarrelToken> tokens;
	public ArrayList<Vector2> anchorPoints;
	
	public float velocityBonus;
	
	public BarrelSequence(int difficulty)
	{
		tokens = new ArrayList<BarrelToken>();
		anchorPoints = new ArrayList<Vector2>();
		
		generateSequence(difficulty);
	}
	
	public void update(float deltaTime)
	{
		for (int i = 0; i < tokens.size(); i++) {
			BarrelToken bt = tokens.get(i);
			
			if(bt.lifeTime <= 0)
				tokens.remove(bt);
		}
	}
	
	private void generateSequence(int difficulty)
	{
		int nbTokens = 0;
		float size = 1.0f;
		float bonus = 1.0f;
		float time = 1.0f;
		
		if (difficulty == DIFF_EASY) {
			nbTokens = 3;
			anchorPoints.add(new Vector2(100, 1000));
			anchorPoints.add(new Vector2(660, 1000));
			anchorPoints.add(new Vector2(760/2, 300));
			size = 1.5f;
			bonus = 30.0f;
			time = 2.0f;
			
		} else if (difficulty == DIFF_MEDIUM) {
			nbTokens = 4;
			anchorPoints.add(new Vector2(200, 1000));
			anchorPoints.add(new Vector2(560, 1000));
			anchorPoints.add(new Vector2(200, 200));
			anchorPoints.add(new Vector2(560, 200));
			size = 1.0f;
			bonus = 40.0f;
			time = 1.5f;
			
		} else if (difficulty == DIFF_HARD) {
			nbTokens = 5;
			
		}
		
		Random rand = new Random();
		for (int i = 0; i < nbTokens; i++) {
			int nextValue = rand.nextInt(anchorPoints.size() - i);
			Vector2 pos = anchorPoints.get(nextValue);
			
			BarrelToken bt = new BarrelToken(pos.x, pos.y, size, size, i, time, bonus);
			tokens.add(bt);
		}
	}
	
	public void finalizeSequence()
	{
		
	}
}
