package game.firemonkey;

import java.util.ArrayList;

import com.bag.lib.gl.SpriteBatcher;

public class GameUI {

	SpriteBatcher batcher;
	SpriteBatcher batcherMonkey;
	
    ArrayList<UIImage> UIImagesBanana;

	UIImage bananaIcon1;
	UIImage bananaIcon2;
	UIImage bananaIcon3;
	UIImage bananaIcon4;
	UIImage bananaIcon5;
	UIImage jumpIcon;
	
	public GameUI(SpriteBatcher sBatcher)
	{	    
	    UIImagesBanana = new ArrayList<UIImage>();
	    bananaIcon1 = new UIImage(40, 37, 60, 45, -5, Assets.bananaNormal);
	    bananaIcon2 = new UIImage(40, 37, 60, 45, 0, Assets.bananaNormal);
	    bananaIcon3 = new UIImage(40, 37, 60, 45, 20, Assets.bananaNormal);
	    bananaIcon4 = new UIImage(48, 35, 60, 45, 30, Assets.bananaNormal);
	    bananaIcon5 = new UIImage(55, 34, 60, 45, 40, Assets.bananaNormal);
	    
	    jumpIcon = new UIImage(730, 35, 50, 50, 0, Assets.rocket);	    
	    
	    UIImagesBanana.add(bananaIcon1);
	    UIImagesBanana.add(bananaIcon2);
	    UIImagesBanana.add(bananaIcon3);
	    UIImagesBanana.add(bananaIcon4); 
	    UIImagesBanana.add(bananaIcon5);
	    
	    batcher = sBatcher;
	}
	
	public void draw()
	{	
		try {
			batcher.beginBatch(Assets.bananasTexture); 
			
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
