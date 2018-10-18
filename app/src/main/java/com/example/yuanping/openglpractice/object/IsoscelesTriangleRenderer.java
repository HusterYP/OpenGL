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
 * @created by PingYuan at 10/16/18
 * @email: husteryp@gmail.com
 * @description:
 */
public class IsoscelesTriangleRenderer implements GLSurfaceView.Renderer {

    private float[] mProjectMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] triangle = {
            0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f
    };
    private static final int PER_BIT_FLOAT = 4;
    private static final int COORDS_PER_VERTEX = 3;
    private FloatBuffer vertexBuffer;
    private int mProgram;
    private int mMatrixHandler;
    private int mPositionHandler;
    private int mColorHandler;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节
    private float[] color = {1f, 0f, 0f, 1f};
    private final int vertexCount = triangle.length / COORDS_PER_VERTEX;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // 设置背景
        ByteBuffer buffer = ByteBuffer.allocateDirect(triangle.length * PER_BIT_FLOAT).order
                (ByteOrder.nativeOrder());
        vertexBuffer = buffer.asFloatBuffer();
        vertexBuffer.put(triangle);
        vertexBuffer.position(0);
        int vertexShader = ShaderUtils.loadShader(GLES20.GL_VERTEX_SHADER, ShaderUtils
                .getShaderRawCodeFromAssets("isoscelesTriangleVertex.glsl"));
        int fragShader = ShaderUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderUtils
                .getShaderRawCodeFromAssets("triangleFrag.glsl"));
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragShader);
        GLES20.glLinkProgram(mProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0);

        mPositionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandler);
        GLES20.glVertexAttribPointer(mPositionHandler, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        mColorHandler = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandler, 1, color, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(mPositionHandler);
    }
}
