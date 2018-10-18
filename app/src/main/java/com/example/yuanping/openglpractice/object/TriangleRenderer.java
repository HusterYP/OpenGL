package com.example.yuanping.openglpractice.object;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.yuanping.openglpractice.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @created by PingYuan at 10/15/18
 * @email: husteryp@gmail.com
 * @description:
 */
public class TriangleRenderer implements GLSurfaceView.Renderer {

    private final float[] triangle = {
            0.5f, 0.5f, 0f, // top
            -0.5f, -0.5f, 0f, // left
            0.5f, -0.5f, 0f // right
    };
    private float[] color = {1f, 0f, 1f, 1f};
    private static final int PER_BYTE_FLOAT = 4; // 每个float占4 byte
    private static final int COORDS_PER_VERTEX = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节
    private FloatBuffer vertexBuffer;
    private int mProgram;
    private int mPositionHandler;
    private int mColorHandler;
    private final int vertexCount = triangle.length / COORDS_PER_VERTEX;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // 设置背景
        ByteBuffer buffer = ByteBuffer.allocateDirect(triangle.length * PER_BYTE_FLOAT).order
                (ByteOrder.nativeOrder());
        vertexBuffer = buffer.asFloatBuffer();
        vertexBuffer.put(triangle);
        vertexBuffer.position(0);
        int vertexShader = ShaderUtils.loadShader(GLES20.GL_VERTEX_SHADER, ShaderUtils
                .getShaderRawCodeFromAssets("triangleVertex.glsl"));
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
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
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
