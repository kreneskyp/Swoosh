package com.transmotion;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;

public class Swoosh extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mGLView = new ClearGLSurfaceView(this);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    private GLSurfaceView mGLView;
}

class ClearGLSurfaceView extends GLSurfaceView {
    public ClearGLSurfaceView(Context context) {
        super(context);
        mRenderer = new ClearRenderer(context);
        setRenderer(mRenderer);
    }

    ClearRenderer mRenderer;
}

class ClearRenderer implements GLSurfaceView.Renderer {
	private ParticleSystem mParticleSystem;
	
	static final int TICKS_PER_SECOND = 25;
	static final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
	static final int MAX_FRAMESKIP = 5;
	static final long start = System.currentTimeMillis();
	long next_game_tick = getTickCount();
	
	public long getTickCount() {
		return System.currentTimeMillis() - start;
	}
	
	public ClearRenderer(Context context) {
		mParticleSystem = new ParticleSystem(context);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLU.gluOrtho2D(gl, -12f, 12f, -20f, 20f);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_REPEAT);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_REPEAT);
        
        mParticleSystem.loadTexture(gl);
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
        GLU.gluOrtho2D(gl, -1f, 1f, -1f, 1f);
        //float ratio = (float) w / h;
        //gl.glMatrixMode(GL10.GL_PROJECTION);
        //gl.glLoadIdentity();
        //gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);

    }

    public void onDrawFrame(GL10 gl) {
    	gl.glClearColor(0, 0, 0f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
                GL10.GL_REPLACE);
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    	gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
    	

    	int loops = 0;
        while( getTickCount() > next_game_tick && loops < MAX_FRAMESKIP) {
        	mParticleSystem.update(getTickCount());
            next_game_tick += SKIP_TICKS;
            loops++;
        }

        float interpolation = ((float)(getTickCount()+SKIP_TICKS-next_game_tick))
                        /  ((float)SKIP_TICKS);

        mParticleSystem.draw(gl, interpolation);
    }
}