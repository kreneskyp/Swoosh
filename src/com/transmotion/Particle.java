package com.transmotion;

import java.util.Random;

public class Particle {

	float[] coords = {
     -0.5f,-0.5f,0,
	  0.5f,-0.5f,0,
	  0.5f,0.5f,0,
	  -0.5f,0.5f,0
	};
		
	short[] icoords = new short[]{0,1,2,3,0};
	
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
	
	// count;
	int count = 0;
	
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
	
	public static float MAX_TRAVEL = 1f;
	
	public void update() {
		x += dx;
		y += dy;
		a += 0;
		count++;
		float xt = count*dx;
		float yt = count*dy;
		if (xt >MAX_TRAVEL || xt<-MAX_TRAVEL || yt > MAX_TRAVEL || yt < -MAX_TRAVEL ) {
			alpha -= 0.02f;
		}
	}
		

}
