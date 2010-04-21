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
	Context mContext;
	
	private FloatBuffer mVertexBuffer;
	private ShortBuffer mIndexBuffer;
	private FloatBuffer mTexBuffer;
	private int[] mTexture = new int[1];
	Random random;
	
    public ParticleSystem(Context context) {
        mContext = context;
        reset();
    }
        
    public void reset() {
    	random = new Random(System.currentTimeMillis());
    	        
        float[] coords = {
        	-1f,-1f,0,
        	1f,-1f,0,
        	1f,1f,0,
        	-1f,1f,0
        };
    
        float[] tcoords = new float[]{
                  0f,1f,
                  1f,1f,
                  1f,0f,
                  0f,0f
                };

        short[] icoords = new short[]{0,1,2,3,0};
        mVertexBuffer = makeFloatBuffer(coords);
        mTexBuffer = makeFloatBuffer(tcoords);
        mIndexBuffer = makeShortBuffer(icoords);
    }

    public static final long INTERVAL = 10000;
    long next_swoosh = 0;
    Comet comet;
    
    public void update(long tick){
    	if (next_swoosh < tick){
    		comet = new Comet(random);
    		next_swoosh += INTERVAL;
    	} else {
    		comet.update(tick);
    	}
    }
    
    private FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    private ShortBuffer makeShortBuffer(short[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ib = bb.asShortBuffer();
        ib.put(arr);
        ib.position(0);
        return ib;
    }

    public void loadTexture(GL10 gl){
        /* load texture */
        gl.glGenTextures(1, mTexture, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[0]);
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.particle);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_BLEND);
    }
    
    public void draw(GL10 gl, float interpolation) {
        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
        
        // render particles
        ArrayList<Particle> particles = comet.particles; 
        for (Particle particle: particles) {
            if (particle.alpha > 0){
                gl.glPushMatrix();
                gl.glTranslatef(particle.x, particle.y, 0);
                gl.glScalef(particle.s, particle.s, 0);
                gl.glColor4f(particle.r, particle.g, particle.b, particle.alpha);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
                gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 4, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);            
                gl.glPopMatrix();
            }
        }
    }
}
