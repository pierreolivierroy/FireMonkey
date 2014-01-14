package game.firemonkey;

import com.bag.lib.DynamicGameObject;
import com.bag.lib.gl.TextureRegion;
import com.bag.lib.math.Vector2;
import android.util.Log;

public class Projectile extends DynamicGameObject{

	public Vector2 destination;
	public TextureRegion asset;
	
	public int damage;
	public float hitPercentage;
	public float speed;
	public float shootingAngle;
	public boolean isActive;
	
	public Projectile(float x, float y, float width, float height, Vector2 finalDestination, TextureRegion initAsset) {
		super(x, y, width, height);		
		this.destination = finalDestination;
		this.asset = initAsset;
		this.isActive = true;
	}

	public void initialize(int initDamage, float initHitChance, float initSpeed)
	{
		this.hitPercentage = initHitChance;
		this.damage = initDamage;
		this.speed = initSpeed;
		this.shootingAngle = (float) Math.atan2(destination.y - position.y, destination.x - position.x) * (float)(180/3.1416);
		Log.d("Test", "Angle :" + shootingAngle);
	}
	
	public void update(float dt)
	{
		// Move bounds for animation
		bounds.lowerLeft.set(position).sub(bounds.width/2, bounds.height/2);
	
		velocity.x = (float) (speed * Math.cos(shootingAngle*3.1416f/180));
        velocity.y = (float) (speed * Math.sin(shootingAngle*3.1416f/180));
        
		position.add(velocity.x * dt, velocity.y * dt);
		
		if(position.y > 600)
			isActive = false;
	}
}
