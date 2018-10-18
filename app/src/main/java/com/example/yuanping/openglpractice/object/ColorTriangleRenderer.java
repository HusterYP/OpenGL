package com.example.yuanping.openglpractice.object;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.yuanping.openglpractice.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @created by PingYuan at 10/18/18
 * @email: husteryp@gmail.com
 * @description:
 */
public class ColorTriangleRenderer implements GLSurfaceView.Renderer {

    private int mProgram;
    private static final int PER_BIT_FLOAT = 4;
    private final float[] vPosition = {
            0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f
    };
    private final float[] aColor = {
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f
    };
    private FloatBuffer positionBuffer;
    private FloatBuffer colorBuffer;
    private float[] mProjectMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private int mMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;
    private static final int COORDS_PER_VERTEX = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节
    private final int vertexCount = vPosition.length / COORDS_PER_VERTEX;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        ByteBuffer position = ByteBuffer.allocateDirect(vPosition.length * PER_BIT_FLOAT).order
                (ByteOrder.nativeOrder());
        positionBuffer = position.asFloatBuffer();
        positionBuffer.put(vPosition);
        positionBuffer.position(0);
        ByteBuffer color = ByteBuffer.allocateDirect(aColor.length * PER_BIT_FLOAT).order
                (ByteOrder.nativeOrder());
        colorBuffer = color.asFloatBuffer();
        colorBuffer.put(aColor);
        colorBuffer.position(0);

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
        // 设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -radio, radio, -1, 1, 3, 7);
        // 设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换  矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mMatrixHandle = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMVPMatrix, 0);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,
                vertexStride, positionBuffer);

        mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        //设置绘制三角形的颜色
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 0,
                colorBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
