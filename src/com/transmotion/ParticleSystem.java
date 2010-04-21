package com.transmotion;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class ParticleSystem {
	
	public static final long INTERVAL = 2000;
	Context mContext;
	Random random;
	long next_swoosh = 0;
    ArrayList<Comet> mComets;
	
	// variables for holding object descriptions
	private FloatBuffer mVertexBuffer;
	private ShortBuffer mIndexBuffer;
	private FloatBuffer mTexBuffer;
	private int[] mTexture = new int[1];
	
    public ParticleSystem(Context context) {
        mContext = context;
        reset();
    }
        
    /**
     * Resets the system, loading models
     */
    public void reset() {
    	mComets = new ArrayList<Comet>();
    	
    	random = new Random(System.currentTimeMillis());
    	        
    	// vertices
        float[] coords = {
        	-1f,-1f,0,
        	1f,-1f,0,
        	1f,1f,0,
        	-1f,1f,0
        };
    
        // texture coordinates
        float[] tcoords = new float[]{
                  0f,1f,
                  1f,1f,
                  1f,0f,
                  0f,0f
                };

        // index coordinates
        short[] icoords = new short[]{0,1,2,3,0};
        
        // convert all arrays to buffers
        mVertexBuffer = makeFloatBuffer(coords);
        mTexBuffer = makeFloatBuffer(tcoords);
        mIndexBuffer = makeShortBuffer(icoords);
    }

    
    
    /**
     * Update all elements in the system
     * @param tick
     */
    public void update(long tick){
    	if (next_swoosh < tick){
    		mComets.add(new Comet(random));
    		next_swoosh += INTERVAL;
    		if (mComets.size() > 7) {
    			mComets.remove(0);
    		}
    	} else {
    		for(Comet comet: mComets){
    			comet.update(tick);
    		}
    	}
    }
    
    /**
     * Convert array to a buffer usable by OpenGL
     * @param arr
     * @return
     */
    private FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    /**
     * Convert array to a buffer usable by OpenGL
     * @param arr
     * @return
     */
    private ShortBuffer makeShortBuffer(short[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ib = bb.asShortBuffer();
        ib.put(arr);
        ib.position(0);
        return ib;
    }

    /**
     * Load texture used for particles
     * @param gl
     */
    public void loadTexture(GL10 gl){
        /* load texture */
        gl.glGenTextures(1, mTexture, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[0]);
        
        // load resource & convert to gl texture
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.particle);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        
        // set environment
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_BLEND);
    }
    
    /**
     * Draw all elements in the system
     * @param gl
     * @param interpolation - used for adjusting position of elements when render happens inbetween a tick.  Only needed for collision
     *                        detection.
     */
    public void draw(GL10 gl, float interpolation) {
    	ShortBuffer indexBuffer = mIndexBuffer;
    	
    	// set blend modes for particles
        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
        
        // render particles
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);    // set texture for particle
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);   // set the vertices for particle
        for (Comet comet: mComets) {
	        ArrayList<Particle> particles = comet.particles; 
	        for (Particle particle: particles) {
	            if (particle.alpha > 0){
	                gl.glPushMatrix();
	                gl.glTranslatef(particle.x, particle.y, 0);                                        // move particle into position
	                gl.glScalef(particle.s, particle.s, 0);                                            // scale the particle
	                gl.glColor4f(comet.r, comet.g, comet.b, particle.alpha);                  // set color
	                gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 4, GL10.GL_UNSIGNED_SHORT, indexBuffer);   // render the particle
	                gl.glPopMatrix();
	            }
	        }
        }
    }
}
