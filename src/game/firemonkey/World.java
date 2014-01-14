package game.firemonkey;

import java.util.ArrayList;

import com.bag.lib.math.Vector2;


public class World {
	
	// Interface, mostly used to access sound effects
    public interface WorldListener {
          //public void sound();
		  int getTime();
    }

    // World's size
    public static final float WORLD_WIDTH 			= 16;
    public static final float WORLD_HEIGHT 			= 9;
    
    // World's states
    public static final int WORLD_STATE_RUNNING 	= 0;
    public static final int WORLD_STATE_NEXT_LEVEL 	= 1;
    public static final int WORLD_STATE_GAME_OVER 	= 2;

    public Ship playerShip;
    public Ship ennemyShip;
    public ArrayList<Ship> ships;
    
    public ArrayList<Projectile> projectiles;
    
    public final WorldListener listener;
    public GameUI gameUI;
    
    public int state;

    public World(WorldListener listener, GameUI gUI) {
    	
    	this.state = WORLD_STATE_RUNNING;
    	this.listener = listener;
    	this.gameUI = gUI;
    	
    	ships = new ArrayList<Ship>();
    	projectiles = new ArrayList<Projectile>();
    	
    	initializeShips();
    }

	public void update(float deltaTime) {
		updateShips(deltaTime);
		updateProjectiles(deltaTime);
		updateExplosions(deltaTime);
	}

//	private void updatePlayer(float deltaTime, float speed) {
//
////	    if(player.state == Player.PLAYER_STATE_HIT_WALL) {
////	    	explosion = new Explosion(50, (int)player.position.x, (int)player.position.y);
////	    	player.state = player.previousState;
////	    }
//	}
	
	private void updateShips(float deltaTime)
	{
		for (Ship s : ships) {
			s.update(deltaTime);
		}
	}

	private void updateExplosions(float deltaTime) 
	{
//		try{	
//			explosion.update(deltaTime);
//		} catch(Exception e){}
	}
	
	private void updateProjectiles(float deltaTime)
	{
		for (int i = 0; i < projectiles.size(); i++) {			
			Projectile p = projectiles.get(i);
			p.update(deltaTime);
			
			if(!p.isActive) 
				projectiles.remove(i);		
		}
	}
	
	private void initializeShips()
	{
		// Player Ship
		Vector2 pCenter = new Vector2(4, 4);
		
		// Rooms
		ArrayList<ShipRoom> rooms = new ArrayList<ShipRoom>();
		ShipRoom s1 = new ShipRoom(pCenter.x - 2, pCenter.y - 2, 2.2f, 2.0f);
		ShipRoom s2 = new ShipRoom(pCenter.x + 2, pCenter.y + 2, 2.2f, 2.0f);
		ShipRoom s3 = new ShipRoom(pCenter.x + 2, pCenter.y - 2, 2.2f, 2.0f);
		ShipRoom s4 = new ShipRoom(pCenter.x - 2, pCenter.y + 2, 2.2f, 2.0f);
		
		s1.assignAssets(Assets.blueTile, null, null);
		s2.assignAssets(Assets.blueTile, null, null);
		s3.assignAssets(Assets.blueTile, null, null);
		s4.assignAssets(Assets.blueTile, null, null);
		
		rooms.add(s1);
		rooms.add(s2);
		rooms.add(s3);
		rooms.add(s4);
		
		// Weapons
		ArrayList<ShipWeapon> weapons = new ArrayList<ShipWeapon>();
		
		ShipWeapon w1 = new ShipWeapon(pCenter.x+1.5f, pCenter.y+2.2f, 1, 1.4f);
		w1.assignAssets(Assets.redTile, Assets.blueTile, Assets.redTile);
		w1.initialize(5.0f, 30, 0.9f, 10f, "Shoota", gameUI.button1);
		
		weapons.add(w1);
		
		// Ship
		playerShip = new Ship(pCenter.x, pCenter.y, 6.5f, 4.5f);
		playerShip.assignAssets(Assets.redTile);
		playerShip.initialize(100, 100, 10, rooms, null, weapons);
		
		ships.add(playerShip);
	}
}

