package game.firemonkey;

import com.bag.lib.DynamicGameObject;
import com.bag.lib.gl.TextureRegion;

public class ShipWeapon extends DynamicGameObject {

	public TextureRegion bodyTexture;
	public TextureRegion iconTextureReady;
	public TextureRegion iconTextureLoading;
	
	public int damage;
	public float hitPercentage;
	public float projectileSpeed;
	public String name;
	
	public boolean isSelected;
	public boolean isFireReady;
	public boolean isFiring;
	
	private boolean isSwitched;
	private float reloadTime;
	private float time;
	
	private UIButton linkedButton;
	
	public ShipWeapon(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public void reset()
	{
		isSelected = false;
		isFireReady = false; // DEBUG
		isFiring = false;
		isSwitched = false;
		linkedButton.state = UIButton.STATE_IDLE;
		time = 0.0f;
	}
		
	public void initialize(float fixedReloadTime, int dmg, float hit, float speed, String initName, UIButton button)
	{
		this.name = initName;
		this.reloadTime = fixedReloadTime;
		this.damage = dmg;
		this.hitPercentage = hit;
		this.projectileSpeed = speed;
		
		// Assign state to the UIButton linked to this weapon
		this.linkedButton = button;
		
		reset();
	}
	
	public void assignAssets(TextureRegion bodyAsset, TextureRegion iconAssetRdy , TextureRegion iconAssetLoad)
	{
		this.bodyTexture = bodyAsset;
		this.iconTextureReady = iconAssetRdy;
		this.iconTextureLoading = iconAssetLoad;
	}
	
	public void update(float dt)
	{
		time += dt;
		
		if(time >= reloadTime && !isSwitched) {
			linkedButton.state = UIButton.STATE_READY;
			isFireReady = true; // Set false when shooting
			isSwitched = true;
		}
	}
	
	public void unselect()
	{
		isSelected = false;
	}
}
