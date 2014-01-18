package game.firemonkey;

import java.util.ArrayList;

import com.bag.lib.gl.SpriteBatcher;

public class GameUI {

	SpriteBatcher batcher;
	SpriteBatcher batcherMonkey;
	
    ArrayList<UIButton> UIButtons;
    ArrayList<UIImage> UIImages;
	UIButton button1;
	UIButton button2;
	UIImage bananaIcon1;
	UIImage bananaIcon2;
	UIImage bananaIcon3;

	public GameUI(SpriteBatcher sBatcher)
	{
	    // UI Declaration
	    UIButtons = new ArrayList<UIButton>();
	    button1 = new UIButton(80, 50, 140, 70, Assets.redTile, Assets.blueTile, Assets.player);
	    button2 = new UIButton(400, 50, 140, 70, Assets.redTile, Assets.redTile, Assets.player);
	    
	    UIImages = new ArrayList<UIImage>();
	    bananaIcon1 = new UIImage(50, 35, 120, 90, 10, Assets.bananaNormal);
	    bananaIcon2 = new UIImage(50, 35, 120, 90, 10, Assets.bananaNormal);
	    bananaIcon3 = new UIImage(50, 35, 120, 90, 10, Assets.bananaNormal);
	
	    UIImages.add(bananaIcon1);
	    UIImages.add(bananaIcon2);
	    UIImages.add(bananaIcon3);
	    //UIButtons.add(button1);
	    //UIButtons.add(button2);
	    
	    batcher = sBatcher;
	}
	
	public void draw()
	{	
		try {
			batcher.beginBatch(Assets.bananasTexture); 
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
			
			for (UIImage u : UIImages) {
				batcher.drawSprite(u.position.x, u.position.y, u.bounds.width, u.bounds.height, u.angle, u.asset);
			}
			batcher.endBatch();
	         
		} catch(Exception e){}
	}
}
