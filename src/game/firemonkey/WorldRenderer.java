package game.firemonkey;

import javax.microedition.khronos.opengles.GL10;

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
    	
        if(world.player.position.y > cam.position.y )
            cam.position.y = world.player.position.y;
        
        cam.setViewportAndMatrices();
        renderBackground();
        renderObjects();
    }
    
    public void renderBackground() {	
    	batcher.beginBatch(Assets.gameBackgroundItems);
    	batcher.drawSprite(cam.position.x, World.WORLD_HEIGHT/2, World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.gameBackground);
    	batcher.endBatch();
    }
    
    public void renderObjects() {
    	
    	// Setup (Required)
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glEnable(GL10.GL_LINE_SMOOTH);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4f(1, 1, 1, 1);
        
        renderPlayer();
        renderClouds();
        
        gl.glDisable(GL10.GL_BLEND);
    }
    
    private void renderPlayer()
    {
    	if(world.player.position.y - World.WORLD_HEIGHT/2 > 0 )
    		cam.position.y = world.player.position.y;
    	
    	batcher.beginBatch(Assets.playerItems);
    	batcher.drawSprite(world.player.position.x, world.player.position.y, 1, 1, Assets.player);
    	batcher.endBatch();
    }
    
    private void renderClouds()
    {
    	if(world.clouds == null || world.clouds.size() == 0)
    		return;
    	
    	batcher.beginBatch(Assets.tileMapItems);
    	for (Circle c : world.clouds) {
    		batcher.drawSprite(c.center.x, c.center.y, c.radius, c.radius, Assets.redTile);
		}   	
    	batcher.endBatch();
    }
    
 
    
//    private void renderExplosions() {
//      
//        GL10 gl = glGraphics.getGL();
//        gl.glEnable(GL10.GL_BLEND);
//        gl.glClear(GL10.GL_DEPTH_BUFFER_BIT);
//        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//	  	try {
//
//			batcher.beginBatch(Assets.tileMapItems);
//
//	        for(int j = 0; j < world.explosion.particles.size(); j++) {
//	        	
//	            Particle par = world.explosion.particles.get(j);
//	            gl.glColor4f(1, 1, 1, par.alpha);
//	      	  	batcher.drawSprite(par.x, par.y , 0.5f, 0.5f, Assets.redTile);
//	      	  	
//	        }
//        
//	        //gl.glDisable(GL10.GL_BLEND);
//	        batcher.endBatch();
//	        
//		} catch (Exception e) {}
//	    gl.glColor4f(1, 1, 1, 1);
//    }
}


