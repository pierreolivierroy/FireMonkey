package game.firemonkey;

import java.util.ArrayList;
import java.util.Random;

import com.bag.lib.gl.TextureRegion;

public class Explosion {

	public static final int STATE_ALIVE         = 0;        // at least 1 particle is alive
	public static final int STATE_DEAD                 = 1;        // all particles are dead

	public ArrayList<Particle> particles;                        // particles in the explosion
	protected int size;                                                // number of particles
	protected int state;                                                // whether it's still active or not

	public Explosion(int particleNr, int x, int y, float size) {
		this.state = STATE_ALIVE;
		this.particles = new ArrayList<Particle>();
		Random rand = new Random();
		for (int i = 0; i < particleNr; i++) {
			
			float color = rand.nextFloat();
			TextureRegion asset = Assets.flameOrange;
			
			if(color >= 0 && color < 0.33)
				asset = Assets.flameOrange;
			else if(color >= 0.33 && color < 0.66)
				asset = Assets.flameRed;
			else
				asset = Assets.flameYellow;
			
			Particle p = new Particle(x, y, size, asset);
			this.particles.add(p);
			
		}
		size = particleNr;
	}

	public void update(float deltaTime) {
		if (this.state != STATE_DEAD) {
			for (int i = 0; i < this.particles.size(); i++) {
				Particle par = this.particles.get(i);
				if (par.state == STATE_ALIVE) {
					par.update(deltaTime);
				}
				else
					this.state = STATE_DEAD;
			}
		}
	}
}