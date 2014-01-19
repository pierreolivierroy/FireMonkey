package game.firemonkey;

import android.content.Context;
import android.content.SharedPreferences;
import com.bag.lib.Game;
import com.bag.lib.Input;
import com.bag.lib.Screen;
import com.bag.lib.gl.Camera2D;
import com.bag.lib.gl.SpriteBatcher;
import com.bag.lib.impl.GLGame;
import com.bag.lib.impl.GLScreen;
import com.bag.lib.math.OverlapTester;
import com.bag.lib.math.Vector2;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.List;

public class LevelSelectorScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Vector2 touchPoint;
    float rotationAngle;
    Screen screen;
    boolean changeScreen;
    float alpha;

    static int playerPosition = 1;

    ArrayList<UIButton> levelButtons;
    UIButton startGame;

    public LevelSelectorScreen(Game game) {
        super(game);
        rotationAngle = 0;
        alpha = 0;
        changeScreen = false;

        // Main GL camera
        guiCam = new Camera2D(glGraphics, 768, 1280);

        // Batcher that will draw every sprites
        batcher = new SpriteBatcher(glGraphics, 100);

        // Touch location vector
        touchPoint = new Vector2();

        // Pre-load assets here
        //Assets.load((GLGame) game);

        levelButtons = new ArrayList<UIButton>();
        levelButtons.add(new UIButton(240, 375, 64, 64, Assets.levelButton, Assets.levelButton, null));
        levelButtons.add(new UIButton(430, 700, 64, 64, Assets.levelButton, Assets.levelButton, null));
        levelButtons.add(new UIButton(200, 820, 64, 64, Assets.levelButton, Assets.levelButton, null));

        startGame = new UIButton(390, 100, 375, 110, null, null, null);
        
        if(!Assets.intro.isPlaying()) {
        	Assets.intro.setVolume(1.0f);
        	Assets.intro.play();
        }
        
        setUnlockedLevel(3);
    }

    @Override
    public void update(float deltaTime) {
        rotationAngle += (40 * deltaTime) % 360;

    	setUnlockedLevel(3);
        
        if(changeScreen) {
            alpha += (deltaTime / 1.3f );
            Assets.intro.setVolume(1 - alpha);
            if(alpha >= 1) {
            	Assets.intro.stop();
                game.setScreen(screen);
            }
        }

        // Acquire all of touch events
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            // Cycle through every touch events
            Input.TouchEvent event = touchEvents.get(i);

            // Assign touch point after conversion to our World coordinates
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);

            if(event.type == Input.TouchEvent.TOUCH_DOWN) {
                for(UIButton button : levelButtons) {
                    if(OverlapTester.pointInRectangle(button.bounds, touchPoint)) {
                        button.state = UIButton.STATE_PRESSED;
                        break;
                    }
                }
                if(OverlapTester.pointInRectangle(startGame.bounds, touchPoint)) {
                    startGame.state = UIButton.STATE_PRESSED;
                }
            }

            // Detect touch on specific bounding rects
            if(event.type == Input.TouchEvent.TOUCH_UP) {
                int curLevel = 1;
                for(UIButton button : levelButtons) {
                    if(button.state == UIButton.STATE_PRESSED) {
                        if(playerPosition == curLevel) {
                            changeScreen = true;
                            World.currentLevel = curLevel;
                            Assets.loadLevel((GLGame) game, curLevel);
                            screen = new GameScreen(game);
                            
                        } else if(getUnlockedLevel() >= curLevel) {
                            playerPosition = curLevel;
                        }
                        button.state = UIButton.STATE_IDLE;
                    }
                    curLevel++;
                }
                if(startGame.state == UIButton.STATE_PRESSED) {
                    changeScreen = true;
                    startGame.state = UIButton.STATE_IDLE;
                    World.currentLevel = playerPosition;
                    Assets.loadLevel((GLGame) game, playerPosition);
                    screen = new GameScreen(game);
                }
            }
        }
    }

    @Override
    // Draw method - draws the present assets
    public void present(float deltaTime) {

        // GL buffer flush
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        guiCam.setViewportAndMatrices();
        
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4f(1,1,1,1);

        batcher.beginBatch(Assets.mapBackgroundTexture);
        batcher.drawSprite(768 / 2, 1280 / 2, 768, 1280, Assets.mapBackground);
        batcher.endBatch();

        int curLevel = 1;
        int level = getUnlockedLevel();
        for(UIButton button : levelButtons) {
            batcher.beginBatch(Assets.mapButtonsTexture);
            batcher.drawSprite(button.position.x, button.position.y, button.R_width, button.R_height, button.idleAsset);
            batcher.endBatch();

            if(level >= curLevel) {
                batcher.beginBatch(Assets.starsTexture);
                batcher.drawSprite(button.position.x, button.position.y, 128, 128, rotationAngle, Assets.starsCircle);
                batcher.endBatch();
            }

            if(playerPosition == curLevel) {
                batcher.beginBatch(Assets.monkeyTexture);
                if(playerPosition == 3) {
                    batcher.drawSprite(button.position.x, button.position.y, 64*1.5f, 64*1.5f, Assets.monkey_flying_space);
                } else {
                    batcher.drawSprite(button.position.x, button.position.y, 64*1.5f, 64*1.5f, Assets.monkey_flying);
                }
                batcher.endBatch();

                batcher.beginBatch(Assets.highscoreBubblesTexture);
                if(playerPosition == 1) {
                    batcher.drawSprite(button.position.x - 10, button.position.y + 105, 256, 256, Assets.highscoreBubbleLeft);
                } else if(playerPosition == 2) {
                    batcher.drawSprite(button.position.x, button.position.y + 115, 256, 256, Assets.highscoreBubbleRight);
                } else if(playerPosition == 3) {
                    batcher.drawSprite(button.position.x - 10, button.position.y + 105, 256, 256, Assets.highscoreBubbleLeft);
                }
                batcher.endBatch();

                batcher.beginBatch(Assets.bananasTexture);
                if(playerPosition == 1) {
                    batcher.drawSprite(button.position.x - 85, button.position.y + 117, 64, 64, Assets.bananaNormal);
                } else if(playerPosition == 2) {
                    batcher.drawSprite(button.position.x - 75, button.position.y + 125, 64, 64, Assets.bananaFrozen);
                } else if(playerPosition == 3) {
                    batcher.drawSprite(button.position.x - 85, button.position.y + 117, 64, 64, Assets.bananaSpace);
                }
                batcher.endBatch();

                batcher.beginBatch(Assets.fontTex);
                if(playerPosition == 1) {
                    Assets.font.drawText(batcher, "High", button.position.x - 40, button.position.y + 137);
                    //Assets.font.drawText(batcher, "TOUCH TO START!", button.position.x - 80, button.position.y);
                } else if(playerPosition == 2) {
                    Assets.font.drawText(batcher, "High", button.position.x - 30, button.position.y + 145);
                    //Assets.font.drawText(batcher, "TOUCH TO START!", button.position.x - 80, button.position.y);
                } else if(playerPosition == 3) {
                    Assets.font.drawText(batcher, "High", button.position.x - 40, button.position.y + 137);
                    //Assets.font.drawText(batcher, "TOUCH TO START!", button.position.x - 80, button.position.y);
                }
                batcher.endBatch();
            }
            curLevel++;
        }

        if(changeScreen) {
            gl.glColor4f(1,1,1,alpha);
            batcher.beginBatch(Assets.tileMapItems);
            batcher.drawSprite(768 / 2, 1280 / 2, 768, 1280, Assets.whiteTween);
            batcher.endBatch();
            gl.glColor4f(1,1,1,1);
        }

        gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void pause() {
        //Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    public int getUnlockedLevel() {
        final SharedPreferences prefs = getGCMPreferences();
        int level = prefs.getInt("UNLOCKED_LEVEL", 1);
        return level;
    }

    public void setUnlockedLevel(int level) {
        final SharedPreferences prefs = getGCMPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("UNLOCKED_LEVEL", level);
        editor.commit();
    }

    public SharedPreferences getGCMPreferences() {
        return FireMonkeyActivity.context.getSharedPreferences(FireMonkeyActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
}
