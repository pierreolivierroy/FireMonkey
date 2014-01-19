package game.firemonkey;

import java.util.Random;

import com.bag.lib.GameObject;

public class BarrelToken extends GameObject {

	public int index;
	public String index_s;
	public float lifeTime;
	public float actualTime;
	public float bonus;
	public boolean success;
	public boolean touched;

	public float tiltAngle;
	boolean isTiltingUp;
	
	public BarrelToken(float x, float y, float width, float height, int idx, float time, float bonus) {
		super(x, y, width, height);
		
		this.index = idx;
		this.index_s = String.valueOf(index);
		this.lifeTime = time;
		this.actualTime = lifeTime;
		this.bonus = bonus;
		this.success = false;
		this.touched = false;
		
		Random rand = new Random();
		
		int decider = rand.nextInt(1);
		if(decider == 0)
			isTiltingUp = true;
		else
			isTiltingUp = false;
		
		this.tiltAngle = rand.nextFloat() * 30;
	}
	
	public void update(float deltaTime)
	{
		this.actualTime -= deltaTime;
		
		if(isTiltingUp) {
			tiltAngle += deltaTime * 40;
			if(tiltAngle >= 30)
				isTiltingUp = false;
		} else {
			tiltAngle -= deltaTime * 40;
			if(tiltAngle <= 0)
				isTiltingUp = true;
		}
		
	}
}
