package game.firemonkey;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.SharedPreferences;

import com.bag.lib.Game;
import com.bag.lib.Input;
import com.bag.lib.Screen;
import com.bag.lib.gl.Camera2D;
import com.bag.lib.gl.SpriteBatcher;
import com.bag.lib.impl.GLScreen;
import com.bag.lib.math.OverlapTester;
import com.bag.lib.math.Vector2;

public class HighscoreScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Vector2 touchPoint;
    Screen screen;
    boolean changeScreen;
    float alpha;

    UIButton backButton;

    public HighscoreScreen(Game game) {
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
        // Assets.load((GLGame) game);
        Assets.intro.play();

        backButton = new UIButton(50, 50, 74, 74, Assets.menuBackButton, Assets.menuBackButton, null);
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
                if(OverlapTester.pointInRectangle(backButton.bounds, touchPoint)) {
                    backButton.state = UIButton.STATE_PRESSED;
                }
            }

            // Detect touch on specific bounding rects
            if(event.type == Input.TouchEvent.TOUCH_UP) {
                if(backButton.state == UIButton.STATE_PRESSED) {
                    changeScreen = true;
                    backButton.state = UIButton.STATE_IDLE;
                    screen = new MainMenuScreen(game);
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

        batcher.beginBatch(Assets.highscoreBackgroundTexture);
        batcher.drawSprite(768 / 2, 1280 / 2, 768, 1280, Assets.highscoreBackground);
        batcher.endBatch();

        batcher.beginBatch(Assets.menuButtonsTexture);
        batcher.drawSprite(backButton.position.x, backButton.position.y, backButton.R_width, backButton.R_height, Assets.menuBackButton);
        batcher.endBatch();

        // High score text
        batcher.beginBatch(Assets.fontTex);
        Assets.font.drawText(batcher, Integer.toString(getHighScore(0)), 768 / 2 - 65, 730);
        Assets.font.drawText(batcher, Integer.toString(getHighScore(1)), 768 / 2 - 65, 505);
        Assets.font.drawText(batcher, Integer.toString(getHighScore(2)), 768 / 2 - 65, 305);
        Assets.font.drawText(batcher, Integer.toString(getHighScore(3)), 768 / 2 - 65, 105);
        batcher.endBatch();

        batcher.beginBatch(Assets.bananasTexture);
        batcher.drawSprite(768 / 2 - 135, 510, 64, 64, Assets.bananaNormal);
        batcher.drawSprite(768 / 2 - 135, 310, 64, 64, Assets.bananaFrozen);
        batcher.drawSprite(768 / 2 - 135, 110, 64, 64, Assets.bananaSpace);

        batcher.drawSprite(768 / 2 - 120, 800, 64, 64, Assets.bananaNormal);
        batcher.drawSprite(768 / 2, 800, 64, 64, Assets.bananaSpace);
        batcher.drawSprite(768 / 2 + 120, 800, 64, 64, Assets.bananaFrozen);
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
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    // Use 0 for total high score
    public int getHighScore(int level) {
        final SharedPreferences prefs = getGCMPreferences();
        int score;
        switch (level) {
            case 0: score = prefs.getInt("GLOBAL_HIGHSCORE", 0);
                break;
            case 1: score = prefs.getInt("LEVEL1_HIGHSCORE", 0);
                break;
            case 2: score = prefs.getInt("LEVEL2_HIGHSCORE", 0);
                break;
            case 3: score = prefs.getInt("LEVEL3_HIGHSCORE", 0);
                break;
            default: score = 0;
                break;
        }
        return score;
    }

    public SharedPreferences getGCMPreferences() {
        return FireMonkeyActivity.context.getSharedPreferences(FireMonkeyActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
}
