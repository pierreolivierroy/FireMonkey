package game.firemonkey;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.bag.lib.gl.Camera2D;
import com.bag.lib.gl.SpriteBatcher;
import com.bag.lib.gl.TextureRegion;
import com.bag.lib.impl.GLGraphics;
import com.bag.lib.math.Circle;

@SuppressWarnings("unused")
public class WorldRenderer {
    static final float FRUSTUM_WIDTH = 9;
    static final float FRUSTUM_HEIGHT = 16;
	
    GLGraphics 		glGraphics;
    World 			world;
    Camera2D 		cam;
    SpriteBatcher 	batcher;    
    
    // Constructor of the world renderer
    // Draws every game objects in the world
    public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world) {
        this.glGraphics = glGraphics;
        this.world = world;
        this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        this.batcher = batcher;
        this.cam.zoom = 1.0f;
    }

    public void render() {
    	
        if(world.monkey.position.y > cam.position.y && world.monkey.state == Monkey.PLAYER_STATE_FLYING)
            cam.position.y = world.monkey.position.y;
        
        cam.setViewportAndMatrices();
        renderBackground();
        renderObjects();
    }
    
    float modif = 0;
    public void renderBackground() {
        float center = 8;
        if(World.currentLevel == 1) {
        	batcher.beginBatch(Assets.jungleBackgrounds);
            if(world.maxHeight <= 24) {     
                batcher.drawSprite(cam.position.x, center, World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.jungleBackgroundStart);               
            }
            if(world.maxHeight >= 8 && world.maxHeight <= 24) {
                batcher.drawSprite(cam.position.x, center + 16, World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.jungleBackgroundRepeat);
            }
            if(world.maxHeight > 24) {
                batcher.drawSprite(cam.position.x, cam.position.y - world.screenIncrement + (world.repeatIncrement_1 * World.WORLD_HEIGHT), 
                		World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.jungleBackgroundRepeat);
                batcher.drawSprite(cam.position.x, cam.position.y - world.screenIncrement + ((1 + world.repeatIncrement_2) * World.WORLD_HEIGHT), 
                		World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.jungleBackgroundRepeat); 
            }
            batcher.endBatch();
        }

        else if(World.currentLevel == 2) {
        	batcher.beginBatch(Assets.snowBackgrounds);
            if(world.maxHeight <= 24) {
                batcher.drawSprite(cam.position.x, center, World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.snowBackgroundStart);
            }
            if(world.maxHeight >= 8 && world.maxHeight <= 24) {
                batcher.drawSprite(cam.position.x, center + 16, World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.snowBackgroundRepeat);
            }
            if(world.maxHeight > 24) {
                batcher.drawSprite(cam.position.x, cam.position.y - world.screenIncrement + (world.repeatIncrement_1 * World.WORLD_HEIGHT), 
                		World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.snowBackgroundRepeat);
                batcher.drawSprite(cam.position.x, cam.position.y - world.screenIncrement + ((1 + world.repeatIncrement_2) * World.WORLD_HEIGHT), 
                		World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.snowBackgroundRepeat); 
            }
            batcher.endBatch();
        }

        else if(World.currentLevel == 3) {
        	batcher.beginBatch(Assets.spaceBackgrounds);
            if(world.maxHeight <= 24) {           
                batcher.drawSprite(cam.position.x, center, World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.spaceBackgroundStart);
            }
            if(world.maxHeight >= 8 && world.maxHeight <= 24) {
                batcher.drawSprite(cam.position.x, center + 16, World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.spaceBackgroundRepeat);
            }
            if(world.maxHeight > 24) {
                batcher.drawSprite(cam.position.x, cam.position.y - world.screenIncrement + (world.repeatIncrement_1 * World.WORLD_HEIGHT), 
                		World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.spaceBackgroundRepeat);
                batcher.drawSprite(cam.position.x, cam.position.y - world.screenIncrement + ((1 + world.repeatIncrement_2) * World.WORLD_HEIGHT), 
                		World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.spaceBackgroundRepeat); 
            }
            batcher.endBatch();
        }
    }
    
    public void renderObjects() {
    	
    	// Setup (Required)
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glEnable(GL10.GL_LINE_SMOOTH);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4f(1, 1, 1, 1);
        
        renderPlayer();
        renderBarrel();
        renderBananas();
        renderExplosions();
        
        gl.glDisable(GL10.GL_BLEND);
    }
    
    private void renderPlayer()
    {
    	if(world.monkey.jumpBoostActive){
        	
        	if(world.monkey.position.y >= world.monkey.jumpHeight){
        		System.out.println("FINI");
        		world.monkey.jumpHeight = 0f;
        		world.monkey.jumpBoostActive = false;
        	}        
        	else if(world.monkey.state != Monkey.PLAYER_STATE_FALLING) {
        		batcher.beginBatch(Assets.flamesTexture);
            	batcher.drawSprite(world.monkey.position.x, world.monkey.position.y - 1f, 1f, 1f, Assets.flames);
            	batcher.endBatch();
        	}
        }
    	
        if(World.currentLevel == 1 || World.currentLevel == 2) {
            batcher.beginBatch(Assets.monkeyTexture);
            if(world.monkey.state == Monkey.PLAYER_STATE_STARTING || world.monkey.state == Monkey.PLAYER_STATE_FALLING)
                batcher.drawSprite(world.monkey.position.x, world.monkey.position.y, Monkey.PLAYER_WIDTH, Monkey.PLAYER_HEIGHT, Assets.monkey_idle);
            else if(world.monkey.state == Monkey.PLAYER_STATE_FLYING)
                batcher.drawSprite(world.monkey.position.x, world.monkey.position.y, Monkey.PLAYER_WIDTH, Monkey.PLAYER_HEIGHT, Assets.monkey_flying);
            else if(world.monkey.state == Monkey.PLAYER_STATE_BONUS) {
                batcher.drawSprite(world.monkey.position.x, world.monkey.position.y, 0, 0, Assets.monkey_flying);
            }
            else
                batcher.drawSprite(world.monkey.position.x, world.monkey.position.y, Monkey.PLAYER_WIDTH, Monkey.PLAYER_HEIGHT, Assets.monkey_flying);
            batcher.endBatch();
        }
        else if(World.currentLevel == 3) {
            batcher.beginBatch(Assets.monkeyTexture);
            if(world.monkey.state == Monkey.PLAYER_STATE_STARTING || world.monkey.state == Monkey.PLAYER_STATE_FALLING)
                batcher.drawSprite(world.monkey.position.x, world.monkey.position.y, Monkey.PLAYER_WIDTH, Monkey.PLAYER_HEIGHT, Assets.monkey_idle_space);
            else if(world.monkey.state == Monkey.PLAYER_STATE_FLYING)
                batcher.drawSprite(world.monkey.position.x, world.monkey.position.y, Monkey.PLAYER_WIDTH, Monkey.PLAYER_HEIGHT, Assets.monkey_flying_space);
            else if(world.monkey.state == Monkey.PLAYER_STATE_BONUS)
                batcher.drawSprite(world.monkey.position.x, world.monkey.position.y, 0, 0, Assets.monkey_flying);
            else
                batcher.drawSprite(world.monkey.position.x, world.monkey.position.y, Monkey.PLAYER_WIDTH, Monkey.PLAYER_HEIGHT, Assets.monkey_flying_space);
            batcher.endBatch();
        }
    }
    
    private void renderBananas()
    {
    	if(world.activeBananas == null || world.activeBananas.size() == 0)
    		return;
    	
    	batcher.beginBatch(Assets.bananasTexture);
    	for (Banana b : world.activeBananas) {
            if(World.currentLevel == 1) {
                batcher.drawSprite(b.position.x, b.position.y, b.bounds.width, b.bounds.height, b.tiltAngle-10, Assets.bananaNormal);
            } else if(World.currentLevel == 2) {
                batcher.drawSprite(b.position.x, b.position.y, b.bounds.width, b.bounds.height, b.tiltAngle-10, Assets.bananaFrozen);
            } else if(World.currentLevel == 3) {
                batcher.drawSprite(b.position.x, b.position.y, b.bounds.width, b.bounds.height, b.tiltAngle-10, Assets.bananaSpace);
            }
		}   	
    	batcher.endBatch();
    }
    
    private void renderBarrel()
    {
    	if(world.activeBarrel == null)
    		return;
    	
    	Barrel b = world.activeBarrel;
    	batcher.beginBatch(Assets.barrelsTexture);
        if(world.monkey.state == Monkey.PLAYER_STATE_BONUS) {
            batcher.drawSprite(b.position.x, b.position.y, b.bounds.width, b.bounds.height, b.tiltAngle-10, Assets.barrelFull);
        } else {
            batcher.drawSprite(b.position.x, b.position.y, b.bounds.width, b.bounds.height, b.tiltAngle-10, Assets.barrelEmpty);
        }

//    	if(world.activeBarrel.sequence != null) {
//	    	for (BarrelToken bt : world.activeBarrel.sequence.tokens) {
//	    		if(!bt.touched) {
//					batcher.drawSprite(bt.position.x, bt.position.y, bt.bounds.width, bt.bounds.height, Assets.barrelEmpty);
//	    		}
//			}
//    	}
    	
    	batcher.endBatch();
    }
   
    private void renderExplosions() {
      
    	if(world.activeExplosions == null || world.activeExplosions.size() == 0)
    		return;
    	
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glClear(GL10.GL_DEPTH_BUFFER_BIT);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	  	try {

			batcher.beginBatch(Assets.flamesTexture);

	        for(int j = 0; j < world.activeExplosions.size(); j++) {
	        	Explosion e = world.activeExplosions.get(j);
	        	
	        	for(int i = 0; i < e.particles.size(); i ++) {
		            Particle par = e.particles.get(i);
		            gl.glColor4f(1, 1, 1, par.alpha);
		      	  	batcher.drawSprite(par.x, par.y , 0.5f, 0.5f, par.asset);
	        	}
	        }
        
	        //gl.glDisable(GL10.GL_BLEND);
	        batcher.endBatch();
	        
		} catch (Exception e) {}
	    gl.glColor4f(1, 1, 1, 1);
    }
}


