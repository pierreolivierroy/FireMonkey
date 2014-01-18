package game.firemonkey;

import com.bag.lib.*;
import com.bag.lib.gl.*;
import com.bag.lib.impl.*;

public class Assets {

    // Main Menu
    public static Texture mainMenuItems;
    public static TextureRegion mainMenuBackground;

    // Game Backgrounds
    public static Texture gameBackgroundItems;
    public static TextureRegion gameBackground;

    // Map Screen
    public static Texture mapBackgroundTexture;
    public static TextureRegion mapBackground;

    public static Texture mapButtonsTexture;
    public static TextureRegion levelButton;

    public static Texture starsTexture;
    public static TextureRegion starsCircle;

    // Game Interface

    // Game Objects
    public static Texture playerItems;
    public static TextureRegion player;

    public static Texture explosionItems;
    public static TextureRegion explo1;

    // Game Backgrounds
    public static Texture tileMapItems;
    public static TextureRegion blueTile;
    public static TextureRegion redTile;


    public static void load(GLGame game) {

        // Main Menu (UI)
        mainMenuItems = new Texture(game, "mapPack1.png");
        mainMenuBackground = new TextureRegion(mainMenuItems, 0, 0, 480, 800);

        gameBackgroundItems = new Texture(game, "background.png");
        gameBackground = new TextureRegion(mainMenuItems, 0, 0, 320, 480);

        // Map (level selector) Screen
        mapBackgroundTexture = new Texture(game, "game_map.jpg");
        mapBackground = new TextureRegion(mapBackgroundTexture, 0, 0, 768, 1280);

        mapButtonsTexture = new Texture(game, "bouton_map.png");
        levelButton = new TextureRegion(mapButtonsTexture, 0, 0, 64, 64);

        starsTexture = new Texture(game, "star_circle.png");
        starsCircle = new TextureRegion(starsTexture, 0, 0, 128, 128);

        // Game Screen
        tileMapItems = new Texture(game, "tilemap.png");
        blueTile = new TextureRegion(tileMapItems, 0, 0, 64, 64);
        redTile = new TextureRegion(tileMapItems, 64, 0, 64, 64);

        // Game Objects
        playerItems = new Texture(game, "pacman.png");
        player = new TextureRegion(playerItems, 0, 0, 64, 64);

    }

    public static void reload() {
        tileMapItems.reload();
        playerItems.reload();
        mainMenuItems.reload();
        gameBackgroundItems.reload();
        mapBackgroundTexture.reload();
        mapButtonsTexture.reload();
        starsTexture.reload();
        
        //if(Settings.soundEnabled )
        // music.play();
    }

    public static void playSound(Sound sound) {
        //if(Settings.soundEnabled);
        //   sound.play(0.4f);
    }
}