package game.firemonkey;

import java.util.Random;

import com.bag.lib.DynamicGameObject;
import com.bag.lib.math.Circle;

public class Barrel extends DynamicGameObject{
	
	public final static int STATE_IDLE 				= 0;
	public final static int STATE_MONKEY_IN 		= 1;
	public final static int STATE_LAUNCHING_MONKEY 	= 2;
	
	public final static int TILT_VELOCITY 			= 30;
	public final static int TILT_VELOCITY_MONKEY_IN = 250;
	
	public int points;
	public int state;
	public float tiltAngle;
	
	public float boostValue;
	public Circle hitZone;
	
	Random rand;
	boolean isTiltingUp;

	public Barrel(float x, float y, float width, float height) {
		super(x, y, width, height);
		
		state = STATE_IDLE;
		hitZone = new Circle(x, y, width/2);
		
		rand = new Random();
		
		int decider = rand.nextInt(1);
		if(decider == 0)
			isTiltingUp = true;
		else
			isTiltingUp = false;
		
		tiltAngle = rand.nextFloat() * 30;
	}
	
	public void update(float deltaTime)
	{
		float shaker = (state == STATE_IDLE) ? TILT_VELOCITY : TILT_VELOCITY_MONKEY_IN;
		
		if(isTiltingUp) {
			tiltAngle += deltaTime * shaker;
			if(tiltAngle >= 30)
				isTiltingUp = false;
		} else {
			tiltAngle -= deltaTime * shaker;
			if(tiltAngle <= 0)
				isTiltingUp = true;
		}
	}

}
