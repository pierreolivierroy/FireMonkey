package game.firemonkey;

import javax.microedition.khronos.opengles.GL10;

import com.bag.lib.gl.Camera2D;
import com.bag.lib.gl.SpriteBatcher;
import com.bag.lib.gl.TextureRegion;
import com.bag.lib.impl.GLGraphics;

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
        cam.setViewportAndMatrices();
        renderBackground();
        renderObjects();
    }
    
    public void renderBackground() {	
    	batcher.beginBatch(Assets.gameBackgroundItems);
    	batcher.drawSprite(World.WORLD_WIDTH/2, World.WORLD_HEIGHT/2, World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.gameBackground_1);
    	batcher.endBatch();
    }
    
    public void renderObjects() {
    	
    	// Setup (Required)
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glEnable(GL10.GL_LINE_SMOOTH);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4f(1, 1, 1, 1);
        
        renderShips();
        renderProjectiles();
        
        gl.glDisable(GL10.GL_BLEND);
    }
    
    private void renderShips()
    {
    	batcher.beginBatch(Assets.tileMapItems);
       
    	for (Ship s : world.ships) {
    		
    		// Ship Body
    		batcher.drawSprite(s.position.x, s.position.y , s.bounds.width, s.bounds.height, s.bodyTexture);
    		
    		// Ship Rooms
    		for (ShipRoom r : s.rooms) {
    			batcher.drawSprite(r.position.x, r.position.y , r.bounds.width, r.bounds.height, r.bodyTextureHealthy);
    			
    			// Ship Crew ?
    		}
    		
    		// Ship Weapons
       		for (ShipWeapon w : s.weapons) {
    			batcher.drawSprite(w.position.x, w.position.y , w.bounds.width, w.bounds.height, w.bodyTexture);
    		}
		}
 
        batcher.endBatch();
    }
    
    private void renderProjectiles()
    {
    	try {
	    	batcher.beginBatch(Assets.tileMapItems);
	    	for (Projectile p : world.projectiles) {
	    		batcher.drawSprite(p.position.x, p.position.y, p.bounds.width, p.bounds.height, p.asset);
			}
	    	batcher.endBatch();
    	} 
    	catch(Exception e){}
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


