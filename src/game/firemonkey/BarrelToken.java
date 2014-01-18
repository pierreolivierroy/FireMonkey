package game.firemonkey;

import com.bag.lib.GameObject;

public class BarrelToken extends GameObject {

	public int index;
	public String index_s;
	public float lifeTime;
	public float actualTime;
	public float bonus;
	public boolean success;
	public boolean touched;
	
	public BarrelToken(float x, float y, float width, float height, int idx, float time, float bonus) {
		super(x, y, width, height);
		
		this.index = idx;
		this.index_s = String.valueOf(index);
		this.lifeTime = time;
		this.actualTime = lifeTime;
		this.bonus = bonus;
		this.success = false;
		this.touched = false;
	}
	
	public void update(float deltaTime)
	{
		this.actualTime -= deltaTime;
	}
}
