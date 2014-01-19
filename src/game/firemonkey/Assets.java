package game.firemonkey;

import com.bag.lib.*;
import com.bag.lib.gl.*;
import com.bag.lib.impl.*;

import java.util.ArrayList;

public class Assets {
    
    // Game Font
    public static Texture fontTex;  
    public static Font font; 
    
    // Map Screen
    public static Texture mapBackgroundTexture;
    public static TextureRegion mapBackground;

    public static Texture mapButtonsTexture;
    public static TextureRegion levelButton;

    public static Texture starsTexture;
    public static TextureRegion starsCircle;

    // Game Interface
    public static Texture highscoreBubblesTexture;
    public static TextureRegion highscoreBubbleLeft;
    public static TextureRegion highscoreBubbleRight;

    // Game Objects
    public static Texture playerItems;
    public static TextureRegion player;
    
    public static Texture monkeyTexture;
    public static TextureRegion monkey_idle;
    public static TextureRegion monkey_flying;
    public static TextureRegion monkey_hit_bonus;
    public static TextureRegion monkey_dead;
    public static TextureRegion monkey_idle_space;
    public static TextureRegion monkey_flying_space;
    public static TextureRegion monkey_hit_bonus_space;
    public static TextureRegion monkey_dead_space;

    public static Texture bananasTexture;
    public static TextureRegion bananaNormal;
    public static TextureRegion bananaFrozen;
    public static TextureRegion bananaSpace;

    public static Texture barrelsTexture;
    public static TextureRegion barrelEmpty;
    public static TextureRegion barrelFull;

    public static Texture numbersTexture;
    public static ArrayList<TextureRegion> numbersList;

    // Game Texts
    public static Texture textsTexture;
    public static TextureRegion readyText;
    public static TextureRegion clickToStartText;
    public static TextureRegion gameoverText;

    // Menu buttons
    public static Texture menuButtonsTexture;
    public static TextureRegion menuBackButton;

    // Game Backgrounds
    public static Texture menuBackgroundTexture;
    public static TextureRegion menuBackground;

    public static Texture tileMapItems;
    public static TextureRegion blueTile;
    public static TextureRegion redTile;
    public static TextureRegion whiteTween;

    public static Texture jungleBackgrounds;
    public static TextureRegion jungleBackgroundStart;
    public static TextureRegion jungleBackgroundRepeat;

    public static Texture snowBackgrounds;
    public static TextureRegion snowBackgroundStart;
    public static TextureRegion snowBackgroundRepeat;

    public static Texture spaceBackgrounds;
    public static TextureRegion spaceBackgroundStart;
    public static TextureRegion spaceBackgroundRepeat;
    
    public static Texture flamesTexture;
    public static TextureRegion flameRed;
    public static TextureRegion flameYellow;
    public static TextureRegion flameOrange;
    public static TextureRegion rocket;
    public static TextureRegion flames;

    public static Texture highscoreBackgroundTexture;
    public static TextureRegion highscoreBackground;

    public static Sound bananaSound_1;
    public static Sound bananaSound_2;
    public static Sound barrelSound_1;
    public static Sound bonusSound_1;
    public static Sound missSound_1;
    public static Sound jumpSound_1;
    
    public static Music intro;
    public static Music snowMusic;
    public static Music spaceMusic;
    public static Music jungleMusic;
    
    public static boolean loaded = false;

    public static void load(GLGame game) {

    	if(loaded) {
    		return;
    	} else {
    		loaded = true;
    	}

        // Map (level selector) Screen
        mapBackgroundTexture = new Texture(game, "game_map.png");
        mapBackground = new TextureRegion(mapBackgroundTexture, 0, 0, 768, 1280);

        mapButtonsTexture = new Texture(game, "bouton_map.png");
        levelButton = new TextureRegion(mapButtonsTexture, 0, 0, 64, 64);

        starsTexture = new Texture(game, "star_circle.png");
        starsCircle = new TextureRegion(starsTexture, 0, 0, 128, 128);

        // Game Screen
        tileMapItems = new Texture(game, "tilemap.png");
        blueTile = new TextureRegion(tileMapItems, 0, 0, 64, 64);
        redTile = new TextureRegion(tileMapItems, 64, 0, 64, 64);
        whiteTween = new TextureRegion(tileMapItems, 0, 64, 64, 64);

        // Game Interface
        highscoreBubblesTexture = new Texture(game, "highscore_bubbles.png");
        highscoreBubbleLeft = new TextureRegion(highscoreBubblesTexture, 256 , 0, 256, 256);
        highscoreBubbleRight = new TextureRegion(highscoreBubblesTexture, 0, 0, 256, 256);

        // Game Object      
    	monkeyTexture = new Texture(game, "monkeys-hd.png");
    	monkey_flying = new TextureRegion(monkeyTexture, 0, 0, 128, 128);
        monkey_idle = new TextureRegion(monkeyTexture, 128, 0, 128, 128);
        monkey_hit_bonus = new TextureRegion(monkeyTexture, 256, 0, 128, 128);
        monkey_dead = new TextureRegion(monkeyTexture, 384, 0, 128, 128);

        monkey_flying_space = new TextureRegion(monkeyTexture, 0, 128, 128, 128);
        monkey_idle_space = new TextureRegion(monkeyTexture, 256, 128, 128, 128);
        monkey_hit_bonus_space = new TextureRegion(monkeyTexture, 128, 128, 128, 128);
        monkey_dead_space = new TextureRegion(monkeyTexture, 384, 128, 128, 128);

        bananasTexture = new Texture(game, "bananas.png");
        bananaNormal = new TextureRegion(bananasTexture, 0, 0, 128, 128);
        bananaFrozen = new TextureRegion(bananasTexture, 128, 0, 128, 128);
        bananaSpace = new TextureRegion(bananasTexture, 0, 128, 128, 128);
        
        flamesTexture = new Texture(game, "flames.png");
        flameRed = new TextureRegion(flamesTexture, 32, 0, 32, 32);
        flameYellow= new TextureRegion(flamesTexture, 64, 0, 32, 32);
        flameOrange = new TextureRegion(flamesTexture, 0, 0, 32, 32);
        rocket = new TextureRegion(flamesTexture, 96, 0, 32, 32);
        flames = new TextureRegion(flamesTexture, 0, 0, 32, 32);

        barrelsTexture = new Texture(game, "barrels.png");
        barrelEmpty = new TextureRegion(barrelsTexture, 0, 0, 150, 181);
        barrelFull = new TextureRegion(barrelsTexture, 256, 0, 150, 181);

        numbersTexture = new Texture(game, "numbers.png");
        numbersList = new ArrayList<TextureRegion>();
        numbersList.add(new TextureRegion(numbersTexture, 0, 0, 128 ,128));
        numbersList.add(new TextureRegion(numbersTexture, 128, 0, 128 ,128));
        numbersList.add(new TextureRegion(numbersTexture, 0, 128, 128 ,128));
        numbersList.add(new TextureRegion(numbersTexture, 128, 128, 128 ,128));
        numbersList.add(new TextureRegion(numbersTexture, 0, 256, 128 ,128));

        // Game texts
        textsTexture = new Texture(game, "texts.png");
        readyText = new TextureRegion(textsTexture, 0, 0, 512, 128);
        clickToStartText = new TextureRegion(textsTexture, 0, 128, 512, 40);
        gameoverText = new TextureRegion(textsTexture, 0, 165, 512, 91);

        // Menu
        menuBackgroundTexture = new Texture(game, "menu.png");
        menuBackground = new TextureRegion(menuBackgroundTexture, 0, 0, 768, 1280);

        // Highschore
        highscoreBackgroundTexture = new Texture(game, "highscore_background.png");
        highscoreBackground = new TextureRegion(highscoreBackgroundTexture, 0, 0, 768, 1280);

        // Menu buttons
        menuButtonsTexture = new Texture(game, "menu_buttons.png");
        menuBackButton = new TextureRegion(menuButtonsTexture, 0, 0, 64, 64);

    	// Fonts
    	fontTex = new Texture(game, "font3.png");
        font = new Font(fontTex, 0, 0, 16, 32, 40);
        
        // Sounds
        bananaSound_1 = game.getAudio().newSound("banana_1.ogg");
        bananaSound_2 = game.getAudio().newSound("banana_2.ogg");
        bonusSound_1 = game.getAudio().newSound("bonus_1.ogg");
        barrelSound_1 = game.getAudio().newSound("barrel_1.ogg");
        missSound_1 = game.getAudio().newSound("miss_1.ogg");
        jumpSound_1 = game.getAudio().newSound("jump_1.ogg");
        
        intro = game.getAudio().newMusic("intro.ogg");
        intro.setLooping(true);
        intro.setVolume(1.0f);
        
        spaceMusic = game.getAudio().newMusic("space.ogg");
        spaceMusic.setLooping(true);
        spaceMusic.setVolume(0.8f);
        
        jungleMusic = game.getAudio().newMusic("jungle.ogg");
        jungleMusic.setLooping(true);
        jungleMusic.setVolume(0.8f);
        
        snowMusic = game.getAudio().newMusic("snow.ogg");
        snowMusic.setLooping(true);
        snowMusic.setVolume(0.8f);

    }
    
    public static void loadLevel(GLGame game, int index)
    {
    	if(index == 1) {
            jungleBackgrounds = new Texture(game, "jungleBackgrounds.png");
            jungleBackgroundStart = new TextureRegion(jungleBackgrounds, 0, 0, 768, 1280);
            jungleBackgroundRepeat = new TextureRegion(jungleBackgrounds, 768, 0, 768, 1280);
    	} else if (index == 2) {
            snowBackgrounds = new Texture(game, "snowBackgrounds.png");
            snowBackgroundStart = new TextureRegion(snowBackgrounds, 0, 0, 768, 1280);
            snowBackgroundRepeat = new TextureRegion(snowBackgrounds, 768, 0, 768, 1280);
    	} else {
            spaceBackgrounds = new Texture(game, "spaceBackgrounds.png");
            spaceBackgroundStart = new TextureRegion(spaceBackgrounds, 0, 0, 768, 1280);
            spaceBackgroundRepeat = new TextureRegion(spaceBackgrounds, 768, 0, 768, 1280);
    	}
    }

    public static void reload() {
        tileMapItems.reload();
        mapBackgroundTexture.reload();
        mapButtonsTexture.reload();
        starsTexture.reload();
        bananasTexture.reload();
        barrelsTexture.reload();
        numbersTexture.reload();
        monkeyTexture.reload();
        fontTex.reload();
        highscoreBubblesTexture.reload();
        
        try {
        	jungleBackgrounds.reload();
		} catch (Exception e) { }
        
        try {
        	snowBackgrounds.reload();
		} catch (Exception e) { }
        
        try {
        	spaceBackgrounds.reload();
		} catch (Exception e) { }
       
        //if(Settings.soundEnabled )
        // music.play();
    }

    public static void playSound(Sound sound) {
        //if(Settings.soundEnabled);
        //   sound.play(0.4f);
    }
}
