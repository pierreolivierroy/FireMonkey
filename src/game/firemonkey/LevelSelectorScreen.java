package game.firemonkey;

import android.content.Context;
import android.content.SharedPreferences;
import com.bag.lib.Game;
import com.bag.lib.gl.Camera2D;
import com.bag.lib.gl.SpriteBatcher;
import com.bag.lib.impl.GLGame;
import com.bag.lib.impl.GLScreen;
import com.bag.lib.math.Vector2;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;

public class LevelSelectorScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Vector2 touchPoint;

    ArrayList<UIButton> levelButtons;

    public LevelSelectorScreen(Game game) {
        super(game);

        // Main GL camera
        guiCam = new Camera2D(glGraphics, 768, 1280);

        // Batcher that will draw every sprites
        batcher = new SpriteBatcher(glGraphics, 100);

        // Touch location vector
        touchPoint = new Vector2();

        // Pre-load assets here
        Assets.load((GLGame) game);

        levelButtons = new ArrayList<UIButton>();
        levelButtons.add(new UIButton(240, 375, 64, 64, Assets.levelButton, Assets.levelButton, null));
        levelButtons.add(new UIButton(430, 700, 64, 64, Assets.levelButton, Assets.levelButton, null));
        levelButtons.add(new UIButton(200, 820, 64, 64, Assets.levelButton, Assets.levelButton, null));

    }

    @Override
    public void update(float deltaTime) {

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

        batcher.beginBatch(Assets.mapBackgroundTexture);
        batcher.drawSprite(768 / 2, 1280 / 2, 768, 1280, Assets.mapBackground);
        batcher.endBatch();

        for(UIButton button : levelButtons) {
            batcher.beginBatch(Assets.mapButtonsTexture);
            batcher.drawSprite(button.position.x, button.position.y, button.R_width, button.R_height, button.idleAsset);
            batcher.endBatch();
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
