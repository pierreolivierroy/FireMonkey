package game.firemonkey;

import java.util.Random;

import com.bag.lib.DynamicGameObject;
import com.bag.lib.math.Circle;

public class Banana extends DynamicGameObject{

	public final static int STATE_IDLE = 0;
	public final static int STATE_TOUCHED = 1;
	public final static int TILT_VELOCITY = 30;
	
	public final static int BOOST_LOW = 15;
	public final static int BOOST_MED = 22;
	public final static int BOOST_HIGH = 33;
	
	public final static int POINTS_LOW = 20;
	public final static int POINTS_MED = 40;
	public final static int POINTS_HIGH = 60;
	
	public int points;
	public int state;
	public float tiltAngle;
	
	public float boostValue;
	public Circle hitZone;
	
	Random rand;
	boolean isTiltingUp;
	
	public Banana(float x, float y, float width, float height, float boost, int points) {
		super(x, y, width, height);
		state = STATE_IDLE;
		boostValue = boost;
		this.points = points;
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
		if(isTiltingUp) {
			tiltAngle += deltaTime * TILT_VELOCITY;
			if(tiltAngle >= 30)
				isTiltingUp = false;
		} else {
			tiltAngle -= deltaTime * TILT_VELOCITY;
			if(tiltAngle <= 0)
				isTiltingUp = true;
		}
	}
}
