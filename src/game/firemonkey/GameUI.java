package game.firemonkey;

import java.util.ArrayList;

import com.bag.lib.gl.SpriteBatcher;

public class GameUI {

	SpriteBatcher batcher;
	SpriteBatcher batcherMonkey;
	
    ArrayList<UIButton> UIButtons;
    ArrayList<UIImage> UIImagesBanana;
	UIButton button1;
	UIButton button2;
	UIImage bananaIcon1;
	UIImage bananaIcon2;
	UIImage bananaIcon3;
	UIImage bananaIcon4;
	UIImage bananaIcon5;
	UIImage jumpIcon;
	
	public GameUI(SpriteBatcher sBatcher)
	{
	    // UI Declaration
	    UIButtons = new ArrayList<UIButton>();
	    button1 = new UIButton(80, 50, 140, 70, Assets.redTile, Assets.blueTile, Assets.player);
	    button2 = new UIButton(400, 50, 140, 70, Assets.redTile, Assets.redTile, Assets.player);
	    
	    UIImagesBanana = new ArrayList<UIImage>();
	    bananaIcon1 = new UIImage(40, 37, 60, 45, -5, Assets.bananaNormal);
	    bananaIcon2 = new UIImage(40, 37, 60, 45, 0, Assets.bananaNormal);
	    bananaIcon3 = new UIImage(40, 37, 60, 45, 20, Assets.bananaNormal);
	    bananaIcon4 = new UIImage(48, 35, 60, 45, 30, Assets.bananaNormal);
	    bananaIcon5 = new UIImage(55, 34, 60, 45, 40, Assets.bananaNormal);
	    
	
	    jumpIcon = new UIImage(650, 35, 50, 50, 0, Assets.rocket);	    
	    
	    UIImagesBanana.add(bananaIcon1);
	    UIImagesBanana.add(bananaIcon2);
	    UIImagesBanana.add(bananaIcon3);
	    UIImagesBanana.add(bananaIcon4); 
	    UIImagesBanana.add(bananaIcon5);
	   
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
			
			for (UIImage u : UIImagesBanana) {
				batcher.drawSprite(u.position.x, u.position.y, u.bounds.width, u.bounds.height, u.angle, u.asset);
			}
			
			batcher.endBatch();
			
			batcher.beginBatch(Assets.flamesTexture);
			batcher.drawSprite(jumpIcon.position.x, jumpIcon.position.y, jumpIcon.bounds.width, jumpIcon.bounds.height, jumpIcon.angle, jumpIcon.asset);
			batcher.endBatch();
	         
		} catch(Exception e){}
	}
}
