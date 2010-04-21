package com.transmotion;

import java.util.Random;

public class Particle {

	// location
	public float x;
	public float y;
	
	//color 
	public float r;
	public float g;
	public float b;
	public float alpha = 1;
	
	// movement
	float dx;
	float dy;
	
	//scale;
	float s;
	
	// acceleration;
	float a = 1;
	
	// max distance a particle can travel before beginning to fade
	public static float MAX_TRAVEL = 1f;
	
	// max number of ticks before beginning to fade
	public static float MAX_TICK = 100;
	
	// tick count;
	int mCount = 0;
	
	/**
	 * Generate a particle - particles are given a specific location (x,y) and are randomly placed near this
	 * to create a more fluid effect.
	 * @param x
	 * @param y
	 * @param dx - X delta of parent comet
	 * @param dy - Y delta of parent comet
	 * @param random - random generator
	 */
	public Particle(float x, float y, float dx, float dy, Random random) {
		this.x = x+random.nextFloat()*.5f-.25f;
		this.y = y+random.nextFloat()*.4f*dy;
		this.dx = random.nextFloat()*.02f;
		this.dy = dy*random.nextFloat()*.03f;
		this.s = random.nextFloat()*2f-.5f;
		
		this.r = 1f;
		this.g = 0;
		this.b = 0;
	}
	
	/**
	 * Updates the particles position
	 */
	public void update() {
		x += dx;
		y += dy;
		mCount++;
		float xt = mCount*dx;
		float yt = mCount*dy;
		if (mCount>MAX_TICK || xt >MAX_TRAVEL || xt<-MAX_TRAVEL || yt > MAX_TRAVEL || yt < -MAX_TRAVEL ) {
			alpha -= 0.02f;
		}
	}
		

}
