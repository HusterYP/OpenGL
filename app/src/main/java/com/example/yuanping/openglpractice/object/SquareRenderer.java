package com.example.yuanping.openglpractice.object;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

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
public class SquareRenderer implements GLSurfaceView.Renderer {

    private float[] square = {
            -0.5f, 0.5f, 0.0f, // top left
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f, // bottom right
            0.5f, 0.5f, 0.0f  // top right
    };

    private float[] color = {
            1f, 1f, 0f, 1f
    };

    private FloatBuffer pointBuffer;
    private static final int PER_BIT_FLOAT = 4;
    private int mProgramHandle;
    private float[] mProjectMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private int vMatrix;
    private int vColor;
    private int vPosition;
    private static final int COORDS_PER_VERTEX = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节
    private final int vertexCount = square.length / COORDS_PER_VERTEX;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        ByteBuffer buffer = ByteBuffer.allocateDirect(square.length * PER_BIT_FLOAT).order
                (ByteOrder.nativeOrder());
        pointBuffer = buffer.asFloatBuffer();
        pointBuffer.put(square);
        pointBuffer.position(0);

        int pointShader = ShaderUtils.loadShader(GLES20.GL_VERTEX_SHADER, ShaderUtils
                .getShaderRawCodeFromAssets("isoscelesTriangleVertex.glsl"));
        int fragShader = ShaderUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderUtils
                .getShaderRawCodeFromAssets("triangleFrag.glsl"));
        mProgramHandle = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgramHandle, pointShader);
        GLES20.glAttachShader(mProgramHandle, fragShader);
        GLES20.glLinkProgram(mProgramHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float radio = (float) width / height;
        Matrix.frustumM(mProjectMatrix, 0, -radio, radio, -1, 1, 3, 7f);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7f, 0, 0, 0, 0, 1f, 0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgramHandle);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        vPosition = GLES20.glGetAttribLocation(mProgramHandle, "vPosition");
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,
                vertexStride, pointBuffer);

        // 设置变换矩阵
        vMatrix = GLES20.glGetUniformLocation(mProgramHandle, "vMatrix");
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mMVPMatrix, 0);

        // 设置颜色
        vColor = GLES20.glGetUniformLocation(mProgramHandle, "vColor");
        GLES20.glUniform4fv(vColor, 1, color, 0);

        // 绘制点
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(vPosition);
    }
}
