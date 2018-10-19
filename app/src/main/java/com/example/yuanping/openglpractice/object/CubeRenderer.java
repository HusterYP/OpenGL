package com.example.yuanping.openglpractice.object;

import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.yuanping.openglpractice.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @created by PingYuan at 10/19/18
 * @email: husteryp@gmail.com
 * @description:
 */
public class CubeRenderer implements GLSurfaceView.Renderer {

    private float[] cube = {
            -1f, 1f, 1f, // 正面左上
            1f, 1f, 1f, // 正面右上
            1f, -1f, 1f, // 正面右下
            -1f, -1f, 1f, // 正面左下
            -1f, 1f, -1f, // 背面左上
            1f, 1f, -1f, // 背面右上
            1f, -1f, -1f, // 背面右下
            -1f, -1f, -1f // 背面左下
    };
    private float[] color = {
            1f, 1f, 1f, 1f,
            1f, 1f, 0f, 1f,
            1f, 0f, 1f, 1f,
            0f, 1f, 1f, 1f,
            0f, 0f, 1f, 1f,
            0f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f
    };
    private short[] index = {
            0, 1, 2, 0, 2, 3,
            1, 5, 6, 1, 6, 2,
            4, 5, 6, 4, 6, 7,
            0, 4, 7, 0, 3, 7,
            0, 4, 5, 0, 1, 5,
            2, 3, 6, 3, 6, 7
    };
    private FloatBuffer pointBuffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer indexBuffer;
    private int mProgram;
    private float[] mProjectMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private int vPosition;
    private int vMatrix;
    private int aColor;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1f);

        ByteBuffer b1 = ByteBuffer.allocateDirect(cube.length * 4).order(ByteOrder.nativeOrder());
        pointBuffer = b1.asFloatBuffer();
        pointBuffer.put(cube);
        pointBuffer.position(0);

        ByteBuffer b2 = ByteBuffer.allocateDirect(color.length * 4).order(ByteOrder.nativeOrder());
        colorBuffer = b2.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

        ByteBuffer b3 = ByteBuffer.allocateDirect(index.length * 2).order(ByteOrder.nativeOrder());
        indexBuffer = b3.asShortBuffer();
        indexBuffer.put(index);
        indexBuffer.position(0);

        int vertexShader = ShaderUtils.loadShader(GLES20.GL_VERTEX_SHADER, ShaderUtils
                .getShaderRawCodeFromAssets("colorTriangle.glsl"));
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
        Matrix.orthoM(mProjectMatrix, 0, -radio * 6, radio * 6, -6, 6, 3, 20);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 10f, 0, 0, 0, 0, 1f, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
//        Matrix.rotateM(mMVPMatrix, 0, 30, 0, 0, 1);
        Matrix.rotateM(mMVPMatrix, 0, 30, 1, 0, 0);
        Matrix.rotateM(mMVPMatrix, 0, 30, 0, 1, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);

        vPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 3, GLES20.GL_FLOAT, false, 12, pointBuffer);

        vMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mMVPMatrix, 0);

        aColor = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(aColor);
        GLES20.glVertexAttribPointer(aColor, 3, GLES20.GL_FLOAT, false, 12, colorBuffer);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT,
                indexBuffer);
        GLES20.glDisableVertexAttribArray(aColor);
        GLES20.glDisableVertexAttribArray(vPosition);
    }
}
