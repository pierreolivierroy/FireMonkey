package game.firemonkey;

import com.bag.lib.GameObject;

public class BarrelToken extends GameObject {

	public int index;
	public float lifeTime;
	public float bonus;
	
	public BarrelToken(float x, float y, float width, float height, int idx, float time, float bonus) {
		super(x, y, width, height);
		
		this.index = idx;
		this.lifeTime = time;
		this.bonus = bonus;
	}
	
	public void update(float deltaTime)
	{
		this.lifeTime -= deltaTime;
	}
}
