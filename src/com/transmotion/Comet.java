package com.transmotion;

import java.util.ArrayList;
import java.util.Random;

public class Comet {

	public ArrayList<Particle> particles = new ArrayList<Particle>();
	
	float mX, mY, mDeltaX, mDeltaY;
	boolean mMoving = true;
	int mMaxX = 12;
	int mMaxY = 20;
	
	public static long MIN_UPDATE = 55;
	public static int MAX_RANDOM = 40;
	long mNext_update = 0;
	Random mRandom;
	
	// color
	float r,g,b;
	
	public Comet(Random gen) {
		mRandom = gen;
		
		// pick a random direction
		switch (gen.nextInt(4)){
			case 0:// RIGHT
				mX = -11f;
				mY = gen.nextFloat()*20f-10f;
				mDeltaX = .5f;
				mDeltaY = 0f;
				break;
		
			case 1:// LEFT
				mX = 12f;
				mY = gen.nextFloat()*40f-20f;
				mDeltaX = -.5f;
				mDeltaY = 0f;
				mRandom = gen;
				break;
				
			case 2:// UP
				mX = gen.nextFloat()*24f-12f;
				mY = -20f;
				mDeltaX = 0;
				mDeltaY = .5f;
				break;
				
			case 3:// DOWN
				mX = gen.nextFloat()*24f-12f;
				mY = 20f;
				mDeltaX = 0f;
				mDeltaY = -.5f;
				break;
		}
		
		r = gen.nextFloat();
		g = gen.nextFloat();
		b = gen.nextFloat();
	}
	
	/**
	 * 
	 * @param tick - current clocktime
	 */
	public void update(long tick) {
		// store local variables for performance reasons
		float x = mX;
		float y = mY;
		float deltaX = mDeltaX;
		float deltaY = mDeltaY;
		Random random = mRandom;
		
		if (mMoving) {
			// collision detection with boundary of screen
			if (x<-mMaxX || y<-mMaxY || x > mMaxX || y > mMaxY) {
				mMoving = false;
			} else{
				mX += deltaX;
				mY += deltaY;
			}
			
			// create new particles
			if (mNext_update < tick) {
				particles.add(new Particle(x, y, deltaX, -1, random));
				particles.add(new Particle(x, y, deltaX, -1, random));
				particles.add(new Particle(x, y, deltaX, 1, random));
				particles.add(new Particle(x, y, deltaX, 1, random));
				mNext_update += (MIN_UPDATE + random.nextInt(MAX_RANDOM));
			}
		}
		
		// update existing particles
		for(Particle particle: particles) {
			particle.update();
		}
		
		
	}
}
