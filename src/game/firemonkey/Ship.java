package game.firemonkey;

import java.util.ArrayList;

import com.bag.lib.DynamicGameObject;
import com.bag.lib.gl.TextureRegion;
import com.bag.lib.math.Vector2;

public class Ship extends DynamicGameObject {
	
	// Ship specific Fields
	public ArrayList<ShipRoom> rooms;		// Different Rooms per Ship
	public ArrayList<ShipMember> members;	// Members aboard the Ship
	public ArrayList<ShipWeapon> weapons;	// Members aboard the Ship
	
	public ShipWeapon selectedWeapon;		// User-selected weapon
	public ShipMember selectedMember;		// User-selected member
	
	public float durability;				// HP of the Ship
	public float armor;						// Armor of the Ship
	public int missiles;					// Current number of missiles
	
	// Textures
	public TextureRegion bodyTexture;
	
	public Ship(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	// Separate generic construction from specific fields
	public void initialize(float initArmor, float initDurability, int initMissiles,
						   ArrayList<ShipRoom> initRooms, 
						   ArrayList<ShipMember> initMembers, 
						   ArrayList<ShipWeapon> initWeapons)
	{
		this.selectedMember = null;
		this.selectedWeapon = null;
		
		this.rooms = initRooms;
		this.members = initMembers;
		this.weapons = initWeapons;
		
		this.durability = initDurability;
		this.armor = initArmor;
		this.missiles = initMissiles;
	} 
	
	public void assignAssets(TextureRegion bodyAsset)
	{
		this.bodyTexture = bodyAsset;
	}
	
	public void update(float dt)
	{
		for (ShipWeapon w : weapons) {
			w.update(dt);
		}
	}
	
	public boolean selectWeapon(int index)
	{
		selectedWeapon = weapons.get(index);
		
		if(!selectedWeapon.isFireReady)
			return false;
		
		// Reset all weapon states
		for (ShipWeapon w : weapons) {
			w.isSelected = false;
		}
		
		selectedWeapon.isSelected = true;
		return true;
	}
	
	public void shoot(Vector2 destination)
	{
		// Test if there is an enemy ship
//		if(GameScreen.getInstance().world.ennemyShip == null)
//			return;
		// Test 
		
		// Test if dest is in one the enemy ship rooms
		
		// Create projectile
		Projectile proj = new Projectile(
				selectedWeapon.position.x + selectedWeapon.bounds.width/2, 
				selectedWeapon.position.y + selectedWeapon.bounds.height/2, 
				0.5f, 0.5f, 
				destination, Assets.redTile);
		proj.initialize(selectedWeapon.damage, selectedWeapon.hitPercentage, selectedWeapon.projectileSpeed);
		
		GameScreen.getInstance().world.projectiles.add(proj);
		
		// Unselect
		selectedWeapon.reset();
		selectedWeapon = null;
	}
}








