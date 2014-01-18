package game.firemonkey;

import com.bag.lib.DynamicGameObject;

public class Banana extends DynamicGameObject{

	public final static int STATE_IDLE = 0;
	public final static int STATE_TOUCHED = 1;
	
	public int points;
	public int state;
	public float boostValue;
	
	public Banana(float x, float y, float width, float height, float boost) {
		super(x, y, width, height);
		state = STATE_IDLE;
		boostValue = boost;
	}
}
