package com.example.yuanping.openglpractice.utils;

import android.content.Context;
import android.opengl.GLES20;

import com.example.yuanping.openglpractice.Application;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL;

/**
 * @created by PingYuan at 10/15/18
 * @email: husteryp@gmail.com
 * @description:
 */
public class ShaderUtils {
    private static Context sContext = Application.sAppContext;

    public static String getShaderRawCodeFromAssets(String fileName) {
        try {
            InputStream inputStream = sContext.getResources().getAssets().open(fileName);
            byte data[] = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
            return new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int loadShader(int shaderType, String rawCode) {
        int shader = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(shader, rawCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
