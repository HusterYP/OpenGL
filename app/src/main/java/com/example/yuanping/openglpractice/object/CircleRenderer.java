package com.example.yuanping.openglpractice.object;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.yuanping.openglpractice.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @created by PingYuan at 10/18/18
 * @email: husteryp@gmail.com
 * @description:
 */
public class CircleRenderer implements GLSurfaceView.Renderer {

    private static final int POINT_COUNT = 1000; // 点的个数
    private static final float RADIUS = 0.8f; // 半径
    private float[] points = createCirclePoints();
    private float[] color = {
            1f, 1f, 0f, 1f
    };
    private int mProgram;
    private FloatBuffer pointBuffer;
    private float[] mProjectMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private int vMatrix;
    private int vPosition;
    private int vColor;

    private float[] createCirclePoints() {
        ArrayList<Float> points = new ArrayList<>();
        points.add(0f); // 圆心
        points.add(0f);
        points.add(0f); // z轴
        float dt = 360f / POINT_COUNT;
        for (int i = 0; i < POINT_COUNT; i++) {
            points.add((float) (RADIUS * Math.sin(dt * i)));
            points.add((float) (RADIUS * Math.cos(dt * i)));
            points.add(0f); // z轴
        }
        float[] arr = new float[points.size()];
        for (int i = 0; i < points.size(); i++)
            arr[i] = points.get(i);
        return arr;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        ByteBuffer buffer = ByteBuffer.allocateDirect(points.length * 4).order(ByteOrder
                .nativeOrder());
        pointBuffer = buffer.asFloatBuffer();
        pointBuffer.put(points);
        pointBuffer.position(0);

        int pointShader = ShaderUtils.loadShader(GLES20.GL_VERTEX_SHADER, ShaderUtils
                .getShaderRawCodeFromAssets("isoscelesTriangleVertex.glsl"));
        int fragShader = ShaderUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderUtils
                .getShaderRawCodeFromAssets("triangleFrag.glsl"));

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, pointShader);
        GLES20.glAttachShader(mProgram, fragShader);
        GLES20.glLinkProgram(mProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float radio = (float) width / height;
        Matrix.frustumM(mProjectMatrix, 0, -radio, radio, -1, 1, 3, 7);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7f, 0, 0, 0, 0, 1f, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUseProgram(mProgram);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // 顶点
        vPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 3, GLES20.GL_FLOAT, false, 12, pointBuffer);

        // 设置投影矩阵
        vMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mMVPMatrix, 0);

        // 设置颜色
        vColor = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(vColor, 1, color, 0);

        // 绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, POINT_COUNT);
        GLES20.glDisableVertexAttribArray(vPosition);
    }
}
