package game.firemonkey;

import com.bag.lib.GameObject;
import com.bag.lib.gl.TextureRegion;

public class UIImage extends GameObject {

	public TextureRegion asset;
	public UIImage(float x, float y, float width, float height, TextureRegion ast) {
		super(x, y, width, height);
		this.asset = ast;
	}

}
