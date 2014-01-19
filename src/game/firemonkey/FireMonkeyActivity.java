package game.firemonkey;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import com.bag.lib.Screen;
import com.bag.lib.impl.GLGame;

public class FireMonkeyActivity extends GLGame {

    boolean firstTimeCreate = true;
    public static Context context;

    public Screen getStartScreen() {
        context = getBaseContext();
        return new MainMenuScreen(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
            Assets.load(this);
            firstTimeCreate = false;
        } else {
            Assets.reload();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //if(Settings.soundEnabled)
        //Assets.music.pause();
    }
}
