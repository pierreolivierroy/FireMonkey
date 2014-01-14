package game.firemonkey;

import com.bag.lib.DynamicGameObject;
import com.bag.lib.gl.TextureRegion;

public class ShipRoom extends DynamicGameObject {

	public TextureRegion bodyTextureHealthy;
	public TextureRegion bodyTextureDamaged;
	public TextureRegion bodyTextureDown;
	
	public ShipRoom(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public void assignAssets(TextureRegion bodyOK, TextureRegion bodyDmg, TextureRegion bodyDown)
	{
		this.bodyTextureHealthy = bodyOK;
		this.bodyTextureDamaged = bodyDmg;
		this.bodyTextureDown = bodyDown;
	}

}
