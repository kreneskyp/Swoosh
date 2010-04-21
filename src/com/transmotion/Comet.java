package com.transmotion;

import java.util.ArrayList;
import java.util.Random;

public class Comet {

	public ArrayList<Particle> particles = new ArrayList<Particle>();
	
	float mX, mY, mDeltaX, mDeltaY;
	boolean mMoving = true;
	int mMaxX = 10;
	int mMaxY = 10;
	
	public static long MIN_UPDATE = 55;
	public static int MAX_RANDOM = 40;
	long mNext_update = 0;
	Random mRandom;
	
	public Comet(Random gen) {
		mX = -10f;
		mY = 5f;
		mDeltaX = .5f; //gen.nextFloat()-.5f*2;
		mDeltaY = 0f; //gen.nextFloat()-.5f*2;
		mRandom = gen;
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
			if (x<-10 || y<-20 || x > mMaxX || y > mMaxY) {
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
