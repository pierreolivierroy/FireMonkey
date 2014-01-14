package game.firemonkey;

import java.util.ArrayList;

import com.bag.lib.gl.SpriteBatcher;

public class GameUI {

	SpriteBatcher batcher;
	
    ArrayList<UIButton> UIButtons;
	UIButton button1;
	UIButton button2;

	public GameUI(SpriteBatcher sBatcher)
	{
	    // UI Declaration
	    UIButtons = new ArrayList<UIButton>();
	    button1 = new UIButton(80, 50, 140, 70, Assets.redTile, Assets.blueTile, Assets.player);
	    button2 = new UIButton(400, 50, 140, 70, Assets.redTile, Assets.redTile, Assets.player);
	    
	    UIButtons.add(button1);
	    UIButtons.add(button2);
	    
	    batcher = sBatcher;
	}
	
	public void draw()
	{	
		try {
			batcher.beginBatch(Assets.tileMapItems);
			for (int i = 0; i < UIButtons.size(); i++) {
				UIButton u = UIButtons.get(i);
				if(u.state == UIButton.STATE_IDLE) {
					batcher.drawSprite(u.position.x, u.position.y, u.bounds.width, u.bounds.height, u.idleAsset);
				} else if(u.state == UIButton.STATE_READY){
					batcher.drawSprite(u.position.x, u.position.y, u.bounds.width, u.bounds.height, u.readyAsset);
				} else if (u.state == UIButton.STATE_PRESSED){
					batcher.drawSprite(u.position.x, u.position.y, u.bounds.width, u.bounds.height, u.pressedAsset);
				}
			}
			batcher.endBatch();
		} catch(Exception e){}
	}
}
