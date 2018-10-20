package com.example.yuanping.openglpractice.object;

import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.yuanping.openglpractice.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @created by PingYuan at 10/20/18
 * @email: husteryp@gmail.com
 * @description: 圆锥
 */
public class ConeRenderer implements GLSurfaceView.Renderer {

    private static final float HEIGHT = 2f;
    private static final int POINT_COUNT = 360;  // 点数量
    private static final float dt = (float) (Math.PI / 180f);
    private static final float radius = 0.8f;

    private float[] cone = getConePoint();
    private FloatBuffer coneBuffer;
    private int mProgram;
    private float[] mProjectMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private int vPosition;
    private int vMatrix;

    private float[] getConePoint() {
        ArrayList<Float> cone = new ArrayList<>();
        cone.add(0f);
        cone.add(0f);
        cone.add(HEIGHT);
        for (int i = 0; i < POINT_COUNT; i++) {
            cone.add((float) (radius * Math.cos(dt * i)));
            cone.add((float) (radius * Math.sin(dt * i)));
            cone.add(0f);
        }

        float[] result = new float[cone.size()];
        final int n = cone.size();
        for (int i = 0; i < n; i++) {
            result[i] = cone.get(i);
        }

        return result;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        ByteBuffer b1 = ByteBuffer.allocateDirect(cone.length * 4).order(ByteOrder.nativeOrder());
        coneBuffer = b1.asFloatBuffer();
        coneBuffer.put(cone);
        coneBuffer.position(0);

        int vertexShader = ShaderUtils.loadShader(GLES20.GL_VERTEX_SHADER, ShaderUtils
                .getShaderRawCodeFromAssets("cone.glsl"));
        int fragShader = ShaderUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderUtils
                .getShaderRawCodeFromAssets("colorTriangleFrag.glsl"));

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragShader);
        GLES20.glLinkProgram(mProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float radio = (float) width / height;
        Matrix.frustumM(mProjectMatrix, 0, -radio, radio, -1, 1, 3, 20);
//        Matrix.orthoM(mProjectMatrix, 0, -radio * 6, radio * 6, -6, 6, 3, 20);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 10f, 0, 0, 0, 0, 1f, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
        Matrix.rotateM(mMVPMatrix, 0, -60, 1f, 0, 0);
//        Matrix.rotateM(mMVPMatrix, 0, 60, 0f, 1f, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        vPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 3, GLES20.GL_FLOAT, false, 12, coneBuffer);

        vMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mMVPMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, POINT_COUNT);
        GLES20.glDisableVertexAttribArray(vPosition);
    }
}
