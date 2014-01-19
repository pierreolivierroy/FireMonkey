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

public class MainMenuScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Vector2 touchPoint;
    Screen screen;
    boolean changeScreen;
    float alpha;

    UIButton quickGame;
    UIButton campaign;
    UIButton highScore;

    public MainMenuScreen(Game game) {
        super(game);
        alpha = 0;
        changeScreen = false;

        // Main GL camera
        guiCam = new Camera2D(glGraphics, 768, 1280);

        // Batcher that will draw every sprites
        batcher = new SpriteBatcher(glGraphics, 100);

        // Touch location vector
        touchPoint = new Vector2();

        // Pre-load assets here
        Assets.load((GLGame) game);

        quickGame = new UIButton(397, 765, 375, 110, null, null, null);
        campaign =  new UIButton(397, 645, 375, 110, null, null, null);
        highScore = new UIButton(397, 525, 375, 110, null, null, null);
    }

    @Override
    public void update(float deltaTime) {
        if(changeScreen) {
            alpha += (deltaTime / 1.3f );
            if(alpha >= 1) {
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
                if(OverlapTester.pointInRectangle(quickGame.bounds, touchPoint)) {
                    quickGame.state = UIButton.STATE_PRESSED;
                } else if(OverlapTester.pointInRectangle(campaign.bounds, touchPoint)) {
                    campaign.state = UIButton.STATE_PRESSED;
                } else if(OverlapTester.pointInRectangle(highScore.bounds, touchPoint)) {
                    highScore.state = UIButton.STATE_PRESSED;
                }
            }

            // Detect touch on specific bounding rects
            if(event.type == Input.TouchEvent.TOUCH_UP) {
                if(quickGame.state == UIButton.STATE_PRESSED) {
                    changeScreen = true;
                    quickGame.state = UIButton.STATE_IDLE;
                    Assets.loadLevel((GLGame) game, 1);
                    World.GAME_MODE = World.GAME_MODE_QUICKSTART;
                    screen = new GameScreen(game);
                } else if(campaign.state == UIButton.STATE_PRESSED) {
                    changeScreen = true;
                    campaign.state = UIButton.STATE_IDLE;
                    World.GAME_MODE = World.GAME_MODE_CAMPAIGN;
                    screen = new LevelSelectorScreen(game);
                } else if(highScore.state == UIButton.STATE_PRESSED) {
                    changeScreen = true;
                    highScore.state = UIButton.STATE_IDLE;
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

        batcher.beginBatch(Assets.menuBackgroundTexture);
        batcher.drawSprite(768 / 2, 1280 / 2, 768, 1280, Assets.menuBackground);
        batcher.endBatch();

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
}
