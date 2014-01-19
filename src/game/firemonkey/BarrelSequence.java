package game.firemonkey;

import java.util.ArrayList;
import java.util.Random;

import com.bag.lib.math.Circle;
import com.bag.lib.math.OverlapTester;
import com.bag.lib.math.Vector2;

public class BarrelSequence {

	public final static int STATE_ACTIVE 	= 0;
	public final static int STATE_DEAD 		= 1;
	
	public final static int DIFF_EASY 	= 1;
	public final static int DIFF_MEDIUM = 2;
	public final static int DIFF_HARD 	= 3;
	
	public ArrayList<BarrelToken> tokens;
	public ArrayList<Vector2> anchorPoints;
	
	public float completionBonus;
	public float velocityBonus;
	public int nextIndex;
	
	public int state;
	public float lifeTime;
	public boolean totalSuccess;
	
	public BarrelSequence(int difficulty, float height)
	{
		state = STATE_ACTIVE;
		tokens = new ArrayList<BarrelToken>();
		anchorPoints = new ArrayList<Vector2>();
		nextIndex = 0;
		completionBonus = 0;
		totalSuccess = false;
		generate(difficulty, height);
		
		if(difficulty == DIFF_EASY)
			lifeTime = 1.8f;
		else if (difficulty == DIFF_MEDIUM)
			lifeTime = 2.0f;
		else if (difficulty == DIFF_HARD)
			lifeTime = 2.0f;
		else 
			lifeTime = 2.0f;
	}
	
	public void update(float deltaTime)
	{
		for (int i = 0; i < tokens.size(); i++) {
			BarrelToken bt = tokens.get(i);
			bt.update(deltaTime);
			
			if(bt.actualTime <= 0)
				bt.success = false;
		}
		
		lifeTime -= deltaTime;
		if(lifeTime <= 0) {
			finalizeSequence();
		}
	}
	
	private void generate(int difficulty, float height)
	{
		int nbTokens = 0;
		float size = 1.0f;
		float bonus = 1.0f;
		float time = 1.0f;
		
		if (difficulty == DIFF_EASY) {
			nbTokens = 3;
            size = 140.0f;
            bonus = 15.0f;
            time = 5.0f;

            generateAnchorList(nbTokens, height);

            boolean regenAnchors;
            do {
                regenAnchors = false;
                for(int i=0; i<anchorPoints.size(); i++) {
                    Vector2 point = anchorPoints.get(i);
                    Circle circle = new Circle(point.x, point.y, size/2);
                    for(int j=0; j<anchorPoints.size(); j++) {
                        if(i == j) continue;
                        if(OverlapTester.pointInCircle(circle, anchorPoints.get(j))) {
                            regenAnchors = true;
                        }
                    }
                }
                if(regenAnchors)
                    generateAnchorList(nbTokens, height);
            } while (regenAnchors);

		} else if (difficulty == DIFF_MEDIUM) {
			nbTokens = 4;
			size = 100.0f;
			bonus = 20.0f;
			time = 1.5f;

            generateAnchorList(nbTokens, height);

            boolean regenAnchors;
            do {
                regenAnchors = false;
                for(int i=0; i<anchorPoints.size(); i++) {
                    Vector2 point = anchorPoints.get(i);
                    Circle circle = new Circle(point.x, point.y, size/2);
                    for(int j=0; j<anchorPoints.size(); j++) {
                        if(i == j) continue;
                        if(OverlapTester.pointInCircle(circle, anchorPoints.get(j))) {
                            regenAnchors = true;
                        }
                    }
                }
                if(regenAnchors)
                    generateAnchorList(nbTokens, height);
            } while (regenAnchors);
			
		} else if (difficulty == DIFF_HARD) {
            nbTokens = 5;
            size = 100.0f;
            bonus = 20.0f;
            time = 1.5f;

            generateAnchorList(nbTokens, height);

            boolean regenAnchors;
            do {
                regenAnchors = false;
                for(int i=0; i<anchorPoints.size(); i++) {
                    Vector2 point = anchorPoints.get(i);
                    Circle circle = new Circle(point.x, point.y, size/2);
                    for(int j=0; j<anchorPoints.size(); j++) {
                        if(i == j) continue;
                        if(OverlapTester.pointInCircle(circle, anchorPoints.get(j))) {
                            regenAnchors = true;
                        }
                    }
                }
                if(regenAnchors)
                    generateAnchorList(nbTokens, height);
            } while (regenAnchors);
			
		}
		
		Random rand = new Random();
		for (int i = 0; i < nbTokens; i++) {
			Vector2 pos = anchorPoints.get(i);			
			BarrelToken bt = new BarrelToken(pos.x, pos.y, size, size, i, time, bonus);
			tokens.add(bt);
		}
	}

    public void generateAnchorList(int size, float height) {
        anchorPoints.clear();
        for(int i=0; i<size; i++) {
            int x = randomInRange(50, 740);
            int y = randomInRange(50, 1230);
            while(y+30 >= height && y-30 <= height) {
                y = randomInRange(50, 1230);
            }
            anchorPoints.add(new Vector2(x, y));
        }
    }
	
	public void inputSequence(BarrelToken bt)
	{
		if(bt.index == nextIndex) {
			bt.success = true;
			nextIndex = bt.index + 1;
			// PLAY COOL SOUND
		} else if (bt.index < tokens.size()){
			nextIndex = bt.index + 1;
			// PLAY BAD SOUND
		} else {
			finalizeSequence();
			// PLAY WIN SOUND
		}	
		
		bt.touched = true;
		
		int touched = 0;
		for (BarrelToken t : tokens) {
			touched += (t.touched) ? 1 : 0;
		}
		
		if(touched == tokens.size())
			finalizeSequence();
	}
	
	public void finalizeSequence()
	{
		float bonus = 15.0f;
		int count = 0;
		for (BarrelToken bt : tokens) {
			
			if(bt.success) {
				velocityBonus += bt.bonus;
				count ++;
			}
		}
		
		if(count == tokens.size()) {
			totalSuccess = true;
		}
		
		state = STATE_DEAD;
		completionBonus = bonus + velocityBonus;
	}

    public int randomInRange(int start, int end) {
        return start + (int)(Math.random() * ((end - start) + 1));
    }
}
