package game.firemonkey;

import com.bag.lib.GameObject;
import com.bag.lib.gl.TextureRegion;

public class UIImage extends GameObject {

	public TextureRegion asset;
	public int angle;
	public UIImage(float x, float y, float width, float height, int angle, TextureRegion ast) {
		super(x, y, width, height);
		this.asset = ast;
		this.angle = angle;
	}

}
