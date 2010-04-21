package com.transmotion;

import java.util.ArrayList;
import java.util.Random;

public class Comet {

	public ArrayList<Particle> particles = new ArrayList<Particle>();
	
	float x, y, dx, dy;
	boolean main = true;
	int mx = 10;
	int my = 10;
	
	public static long MIN_UPDATE = 55;
	public static int MAX_RANDOM = 40;
	long next_update = 0;
	Random random;
	
	public Comet(Random gen) {
		x = -10f;
		y = 5f;
		dx = .5f; //gen.nextFloat()-.5f*2;
		dy = 0f; //gen.nextFloat()-.5f*2;
		random = gen;
	}
	
	public void update(long tick) {
		if (main) {
			if (x<-10 || y<-20 || x > mx || y > my) {
				main = false;
			} else{
				x += dx;
				y += dy;
			}
			
			// create new particles
			if (next_update < tick) {
				particles.add(new Particle(x, y, dx, -1, random));
				particles.add(new Particle(x, y, dx, -1, random));
				particles.add(new Particle(x, y, dx, 1, random));
				particles.add(new Particle(x, y, dx, 1, random));
				next_update += (MIN_UPDATE + random.nextInt(MAX_RANDOM));
			}
		}
		
		// update existing particles
		for(Particle particle: particles) {
			particle.update();
		}
		
		
	}
}
