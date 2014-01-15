package game.firemonkey;

import java.util.List;
import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.bag.lib.Game;
import com.bag.lib.Input.TouchEvent;
import com.bag.lib.gl.Camera2D;
import com.bag.lib.gl.SpriteBatcher;
import com.bag.lib.impl.GLScreen;
import com.bag.lib.math.OverlapTester;
import com.bag.lib.math.Vector2;

import game.firemonkey.World.WorldListener;

public class GameScreen extends GLScreen {
	
	public static GameScreen instance;
	
	// States 
    static final int GAME_READY 	= 0;    
    static final int GAME_RUNNING 	= 1;
    static final int GAME_PAUSED 	= 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER 		= 4;
    
    // Touch States
    static final int STATE_TOUCH_STARTING = 10;
    static final int STATE_TOUCH_FLYING = 11;
    static final int STATE_TOUCH_REFUEL = 12;
    
    // Screen size
    static final int SCREEN_WIDTH	= 480;
    static final int SCREEN_HEIGHT	= 800;

    int 			state;
    int 			touchState;
    
    Camera2D 		guiCam;
    Vector2 		touchPoint;

    SpriteBatcher 	batcher;
    GameUI			gameUI;
    
    World 			world;
    WorldListener 	worldListener;
    WorldRenderer 	renderer;
    
    float 			startTime;
    float 			elapsedTime;
    
	boolean 		gameOverTouch = false;
	
    public GameScreen(Game game) {
        super(game);
        
        state = GAME_READY;
        touchState = STATE_TOUCH_STARTING;
        
        guiCam = new Camera2D(glGraphics, SCREEN_WIDTH, SCREEN_HEIGHT);
        touchPoint = new Vector2();
        
        // Create a worldListener, to trigger events on the world
        worldListener = new WorldListener() {		
			public int getTime() {
				return (int)elapsedTime;			
			}
        };
        
        batcher = new SpriteBatcher(glGraphics, 5000);
        gameUI = new GameUI(batcher);
        
        world = new World(worldListener, gameUI);
        renderer = new WorldRenderer(glGraphics, batcher, world);
        
        // Variables
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
    }
    
	/**************************************************
	 * 
	 * 	GAMESCREEN UPDATE SECTION
	 * 
	 **************************************************/
	@Override
	public void update(float deltaTime) {
	    
/*		if(deltaTime > 0.1f)
	        deltaTime = 0.1f;*/
	    
	    switch(state) {
	    case GAME_READY:
	        updateReady();
	        break;
	    case GAME_RUNNING:
	        updateRunning(deltaTime);
	        break;
	    case GAME_PAUSED:
	        updatePaused();
	        break;
	    case GAME_OVER:
	        updateGameOver();
	        break;
	    }
	}
	
	// Update when state is READY
	private void updateReady() {
		// First touch 
	    //if(game.getInput().getTouchEvents().size() > 0) {
	        state = GAME_RUNNING;
	    //}
	}
	
	// Update when state is RUNNING
	private void updateRunning(float deltaTime) {
	    
		// Check Touch Events
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {
	        TouchEvent event = touchEvents.get(i);
	        touchPoint.set(event.x, event.y);
	        guiCam.touchToWorld(touchPoint);
	        
	        UITouchHandler(event, touchPoint);
	        worldTouchHandler(event, touchPoint);
	        
	    }    
	    elapsedTime += deltaTime;
	    
	    // Update World
	    world.update(deltaTime, game.getInput().getAccelX());
	}
	
	private void updatePaused() {
	}	
	
	private void updateGameOver() {
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {      
	        TouchEvent event = touchEvents.get(i);
	        touchPoint.set(event.x, event.y);
	        guiCam.touchToWorld(touchPoint);
	    }
	}
	
	private void worldTouchHandler(TouchEvent event, Vector2 point)
	{
		// Convert for world coordinate (ex 16:9 rather than 800x480)
		point.x = (point.x/SCREEN_WIDTH) * WorldRenderer.FRUSTUM_WIDTH;
		point.y = (point.y/SCREEN_HEIGHT) * WorldRenderer.FRUSTUM_HEIGHT;
		
		switch (touchState) {
		
		// No prior touch states detected
		case STATE_TOUCH_STARTING:
			if(event.type == TouchEvent.TOUCH_DRAGGED ||event.type == TouchEvent.TOUCH_DOWN){     
	         	
	        } else if(event.type == TouchEvent.TOUCH_UP){
	
	        }
			break;
			
		// User previously touched a ship member
		case STATE_TOUCH_REFUEL:
			if(event.type == TouchEvent.TOUCH_DRAGGED ||event.type == TouchEvent.TOUCH_DOWN){     
	         	
	        } else if(event.type == TouchEvent.TOUCH_UP){
	
	        }
			break;
			
		// User previously touched a weapon in the UI
		case STATE_TOUCH_FLYING:			
			break;

		default:
			break;
		}
	}
	
	// Defines interaction with UI Elements according to different states
	private void UITouchHandler(TouchEvent event, Vector2 point)
	{
		switch (touchState) {
		
		// No prior touch states detected
		case STATE_TOUCH_STARTING:
			break;
			
		// User previously touched a ship member
		case STATE_TOUCH_REFUEL:
			break;
			
		// User previously touched a weapon in the UI
		case STATE_TOUCH_FLYING:
			break;

		default:
			break;
		}
	}

	/**************************************************
	 * 
	 * 	GAMESCREEN DRAWING SECTION
	 * 
	 **************************************************/
	
	@Override
	public void present(float deltaTime) {
		
		deltaTime /= 2; // to adjust framerate

		GL10 gl = glGraphics.getGL();
	    
		// Render Game objects
		for(int i = 0; i < 2; i++){
		    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		    gl.glEnable(GL10.GL_TEXTURE_2D);
			renderer.render();
		}
	    
	    guiCam.setViewportAndMatrices();
	    gl.glEnable(GL10.GL_BLEND);
	    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    gl.glColor4f(1, 1, 1, 1);
	    
		// Draw the UI for current State
	    switch(state) {
	    case GAME_READY:
	        presentReady();
	        break;
	    case GAME_RUNNING:
	        presentRunning();
	        break;
	    case GAME_PAUSED:
	        presentPaused();
	        break;
	    case GAME_LEVEL_END:
	        presentLevelEnd();
	        break;
	    case GAME_OVER:
	        presentGameOver();
	        break;
	    }

	    gl.glDisable(GL10.GL_BLEND);
		
	}
	
	private void presentReady() {
         // Draw here
		drawUI();
	}
	
	private void presentRunning() {
		// Draw here
		drawUI();
	}
	
	private void presentPaused() { 
		// Draw here
	}
	
	private void presentLevelEnd() {
		// Draw here
	}
	
	private void presentGameOver() {
		// Draw here
	}
	
	private void drawUI() {
		gameUI.draw();
	}

    @Override
    public void pause() {
    	
    }

    @Override
    public void resume() {   
    	
    }

    @Override
    public void dispose() {       
    }
}


