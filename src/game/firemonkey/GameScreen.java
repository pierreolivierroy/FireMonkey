package game.firemonkey;

import game.firemonkey.World.WorldListener;

import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.SharedPreferences;

import com.bag.lib.Game;
import com.bag.lib.Input.TouchEvent;
import com.bag.lib.gl.Camera2D;
import com.bag.lib.gl.SpriteBatcher;
import com.bag.lib.impl.GLScreen;
import com.bag.lib.math.Vector2;

public class GameScreen extends GLScreen {

	public static GameScreen instance;

	// States
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_LEVEL_END = 3;
	static final int GAME_OVER = 4;
	static final int GAME_ENTERING = 5;

	// Touch States
	static final int STATE_TOUCH_STARTING = 10;
	static final int STATE_TOUCH_FLYING = 11;
	static final int STATE_TOUCH_REFUEL = 12;

	// Screen size
	static final int SCREEN_WIDTH = 768;
	static final int SCREEN_HEIGHT = 1280;

	int state;
	int touchState;

	Camera2D guiCam;
	Vector2 touchPoint;

	SpriteBatcher batcher;
	GameUI gameUI;

	World world;
	WorldListener worldListener;
	WorldRenderer renderer;

	float startTime;
	float elapsedTime;
	float transitionTime;

	public int currentLevelIndex;

	public GameScreen(Game game) {
		super(game);

		state = GAME_ENTERING;
		touchState = STATE_TOUCH_STARTING;

		guiCam = new Camera2D(glGraphics, SCREEN_WIDTH, SCREEN_HEIGHT);
		touchPoint = new Vector2();

		// Create a worldListener, to trigger events on the world
		worldListener = new WorldListener() {
			public int getTime() {
				return (int) elapsedTime;
			}

			public void playBananaHit() {
				try {
					Random rand = new Random();
					float r = rand.nextFloat();
					if (r > 0.5)
						Assets.bananaSound_1.play(0.8f);
					else
						Assets.bananaSound_2.play(0.8f);
				} catch (Exception e) {
				}
			}

			public void playBarrelOut() {
				Assets.barrelSound_1.play(0.9f);
			}

			public void playBonusAcquired() {
				Assets.bonusSound_1.play(1.0f);
			}

			public void playMiss() {
				Assets.missSound_1.play(0.9f);
			}

			public void playJump() {
				Assets.jumpSound_1.play(0.9f);
			}
		};

		if (Assets.intro.isPlaying()) {
			Assets.intro.stop();
		}

		selectMusic();

		batcher = new SpriteBatcher(glGraphics, 5000);
		gameUI = new GameUI(batcher);

		world = new World(worldListener, gameUI);
		renderer = new WorldRenderer(glGraphics, batcher, world);

		// Variables
		transitionTime = 1.0f;
		startTime = System.currentTimeMillis();
		elapsedTime = 0;
	}

	/**************************************************
	 * 
	 * GAMESCREEN UPDATE SECTION
	 * 
	 **************************************************/
	@Override
	public void update(float deltaTime) {

		/*
		 * if(deltaTime > 0.1f) deltaTime = 0.1f;
		 */

		switch (state) {
		case GAME_ENTERING:
			updateEntering(deltaTime);
			break;
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

	// Update when state is ENTERING
	private void updateEntering(float deltaTime) {

		if (transitionTime <= 0.0f) { // 1 second wait time
			state = GAME_READY;
		}
		transitionTime -= deltaTime / 1.3f;
	}

	// Update when state is READY
	private void updateReady() {
		// First touch
		if (game.getInput().getTouchEvents().size() > 0) {
			state = GAME_RUNNING;

		}
	}

	// Update when state is RUNNING
	private void updateRunning(float deltaTime) {

		// Check Touch Events
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);

			UITouchHandler(event, touchPoint);
			worldTouchHandler(event, touchPoint);

		}
		elapsedTime += deltaTime;

		// Update World
		world.update(deltaTime, game.getInput().getAccelX());

		if (world.monkey.state == Monkey.PLAYER_STATE_BONUS)
			renderer.cam.position.y = world.monkey.position.y;

		if (world.state == World.WORLD_STATE_GAME_OVER)
			this.state = GAME_OVER;
	}

	private void updatePaused() {
		try {
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
			int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				if (event.type == TouchEvent.TOUCH_UP) {
					state = GAME_RUNNING;
				}
			}
		} catch (Exception e) {}
	}

	private void updateGameOver() {
		try {
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
			int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				touchPoint.set(event.x, event.y);
				guiCam.touchToWorld(touchPoint);

				if (event.type == TouchEvent.TOUCH_UP) {
					stopMusic();
					if (World.GAME_MODE == World.GAME_MODE_CAMPAIGN) {
						game.setScreen(new LevelSelectorScreen(game));
					} else if (World.GAME_MODE == World.GAME_MODE_QUICKSTART) {
						game.setScreen(new MainMenuScreen(game));
					}
				}
			}
		} catch (Exception e) {
		}
	}

	private void worldTouchHandler(TouchEvent event, Vector2 point) {
		// Convert for world coordinate (ex 16:9 rather than 1280x768)
		point.x = (point.x / SCREEN_WIDTH) * WorldRenderer.FRUSTUM_WIDTH;
		point.y = (point.y / SCREEN_HEIGHT) * WorldRenderer.FRUSTUM_HEIGHT;

		switch (world.monkey.state) {

		// No prior touch states detected
		case Monkey.PLAYER_STATE_BONUS:
			if (event.type == TouchEvent.TOUCH_DRAGGED
					|| event.type == TouchEvent.TOUCH_DOWN) {

			} else if (event.type == TouchEvent.TOUCH_UP) {

			}
			break;

		default:
			if(event.type == TouchEvent.TOUCH_DRAGGED ||event.type == TouchEvent.TOUCH_DOWN){     
	         	
	        } 
			else if(event.type == TouchEvent.TOUCH_UP){
	        	
				if(world.monkey.state != Monkey.PLAYER_STATE_BONUS && world.monkey.immuneTime <= 0){
					if(world.monkey.firstJump){
		        		world.monkey.firstJump = false;
		        		world.monkey.velocity.y = 30;
		        	}
		        	else if(world.monkey.jump > 0 && !world.monkey.firstJump) {
		        		/*world.activeExplosions.add(new Explosion(30, (int)world.monkey.position.x, 
		        				(int) world.monkey.position.y + (int) Monkey.PLAYER_HEIGHT, 3.0f));*/
			        	world.monkey.velocity.y = 30;
				        world.monkey.jump--;
				        worldListener.playJump();
				        world.monkey.jumpBoostActive = true;
				        world.monkey.jumpHeight = world.monkey.position.y + Monkey.PLAYER_BOOST_MAX_HEIGHT;
		        	}
				}	        		
	        } 
			break;
		}
	}

	// Defines interaction with UI Elements according to different states
	private void UITouchHandler(TouchEvent event, Vector2 point) {
		switch (world.monkey.state) {

		// No prior touch states detected
		case Monkey.PLAYER_STATE_BONUS:
			if (event.type == TouchEvent.TOUCH_DRAGGED
					|| event.type == TouchEvent.TOUCH_DOWN) {

			} else if (event.type == TouchEvent.TOUCH_UP) {
				world.touchToken(point);
			}
			break;

		default:
			if (event.type == TouchEvent.TOUCH_DRAGGED
					|| event.type == TouchEvent.TOUCH_DOWN) {

			} else if (event.type == TouchEvent.TOUCH_UP) {

			}
			break;
		}
	}

	/**************************************************
	 * 
	 * GAMESCREEN DRAWING SECTION
	 * 
	 **************************************************/

	@Override
	public void present(float deltaTime) {

		deltaTime /= 2; // to adjust framerate

		GL10 gl = glGraphics.getGL();

		// Render Game objects
		for (int i = 0; i < 2; i++) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			renderer.render();
		}

		guiCam.setViewportAndMatrices();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4f(1, 1, 1, 1);

		// Draw the UI for current State
		switch (state) {
		case GAME_ENTERING:
			presentEntering(gl);
			break;
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

	private void presentEntering(GL10 gl) {
		batcher.beginBatch(Assets.tileMapItems);

		gl.glColor4f(1, 1, 1, transitionTime);
		batcher.drawSprite(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, SCREEN_WIDTH,
				SCREEN_HEIGHT, Assets.whiteTween);
		batcher.endBatch();

		gl.glColor4f(1, 1, 1, 1);
	}

	private void presentReady() {
		// batcher.beginBatch(Assets.fontTex);
		// Assets.font.drawText(batcher, "PREPARE FOR LAUNCH!", 262, 520);
		// Assets.font.drawText(batcher, "TOUCH TO START!", 300, 560);
		// batcher.endBatch();

		batcher.beginBatch(Assets.textsTexture);
		batcher.drawSprite(400, 550, 512, 128, Assets.readyText);
		batcher.drawSprite(470, 400, 512, 40, Assets.clickToStartText);
		batcher.endBatch();

		gameUI.draw();
	}

	private void presentRunning() {

		batcher.beginBatch(Assets.fontTex);
		Assets.font.drawText(batcher, "X" + world.score, 100, 30);
		batcher.endBatch();

		batcher.beginBatch(Assets.fontTex);
		Assets.font.drawText(batcher, world.monkey.jump + "X", 650, 30);
		batcher.endBatch();

		gameUI.draw();

		// Touch tokens
		if (world.activeBarrel == null || world.activeBarrel.sequence == null)
			return;

		try {
			batcher.beginBatch(Assets.numbersTexture);
			if (world.activeBarrel.sequence != null) {
				for (BarrelToken bt : world.activeBarrel.sequence.tokens) {
					if (!bt.touched) {
						batcher.drawSprite(bt.position.x, bt.position.y, 128,
								128, bt.tiltAngle, Assets.numbersList.get(bt.index));
					}
				}
			}
			batcher.endBatch();
		} catch (Exception e) {
		}

	}

	private void presentPaused() {

		batcher.beginBatch(Assets.textsTexture);
		batcher.drawSprite(400, 550, 512, 128, Assets.readyText);
		batcher.drawSprite(470, 400, 512, 40, Assets.clickToStartText);
		batcher.endBatch();

		gameUI.draw();
	}

	private void presentLevelEnd() {
		gameUI.draw();
	}

	private void presentGameOver() {

		batcher.beginBatch(Assets.textsTexture);
		batcher.drawSprite(400, 950, 512, 128, Assets.gameoverText);
		batcher.endBatch();

		batcher.beginBatch(Assets.fontTex);
		Assets.font.drawText(batcher, "Your Monkey ran ", 150, 830);
		Assets.font.drawText(batcher, "out of bananas!", 150, 790);
		Assets.font.drawText(batcher, "- Final Score -", 140, 600);
		Assets.font.drawText(batcher, "" + world.score, 340, 550);
		batcher.endBatch();

		setHighScore(World.currentLevel, world.score);

		gameUI.draw();
	}

	@Override
	public void pause() {
		state = GAME_PAUSED;
		stopMusic();
	}

	@Override
	public void resume() {
		selectMusic();
	}

	@Override
	public void dispose() {
		stopMusic();
	}

	// Music

	public void selectMusic() {
		if (World.currentLevel == 1) {
			Assets.jungleMusic.play();
		} else if (World.currentLevel == 2) {
			Assets.snowMusic.play();
		} else {
			Assets.spaceMusic.play();
		}
	}

	public void stopMusic() {
		if (World.currentLevel == 1) {
			Assets.jungleMusic.stop();
		} else if (World.currentLevel == 2) {
			Assets.snowMusic.stop();
		} else {
			Assets.spaceMusic.stop();
		}
	}

	// Use 0 for total high score
	public void setHighScore(int level, int score) {
		final SharedPreferences prefs = getGCMPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		if (score > getHighScore(level)) {
			switch (level) {
			case 1:
				editor.putInt("LEVEL1_HIGHSCORE", score);
				break;
			case 2:
				editor.putInt("LEVEL2_HIGHSCORE", score);
				break;
			case 3:
				editor.putInt("LEVEL3_HIGHSCORE", score);
				break;
			default:
				break;
			}
		}
		if (score > getHighScore(0)) {
			editor.putInt("GLOBAL_HIGHSCORE", score);
		}
		editor.commit();
	}

	// Use 0 for total high score
	public int getHighScore(int level) {
		final SharedPreferences prefs = getGCMPreferences();
		int score;
		switch (level) {
		case 0:
			score = prefs.getInt("GLOBAL_HIGHSCORE", 0);
			break;
		case 1:
			score = prefs.getInt("LEVEL1_HIGHSCORE", 0);
			break;
		case 2:
			score = prefs.getInt("LEVEL2_HIGHSCORE", 0);
			break;
		case 3:
			score = prefs.getInt("LEVEL3_HIGHSCORE", 0);
			break;
		default:
			score = 0;
			break;
		}
		return score;
	}

	public SharedPreferences getGCMPreferences() {
		return FireMonkeyActivity.context.getSharedPreferences(
				FireMonkeyActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	}
}
