package game.firemonkey;

import com.bag.lib.GameObject;
import com.bag.lib.gl.TextureRegion;

public class UIButton extends GameObject{

	public static int STATE_IDLE 	= 0;
	public static int STATE_PRESSED	= 1;
	public static int STATE_READY	= 2;
	
	public TextureRegion idleAsset;
	public TextureRegion pressedAsset;
	public TextureRegion readyAsset;
	
	public float R_width;
	public float R_height;
	
	public int state;
	public boolean isReady;
	
	public UIButton(float x, float y, float width, float height, TextureRegion idle, TextureRegion pressed, TextureRegion ready) {
		super(x, y, width, height);
		
		idleAsset = idle;
		pressedAsset = pressed;
		readyAsset = ready;
		
		state = STATE_IDLE;
		isReady = false;
		
		R_width = width;
		R_height = height;
	}
}
