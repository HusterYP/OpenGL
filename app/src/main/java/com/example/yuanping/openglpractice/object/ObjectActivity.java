package com.example.yuanping.openglpractice.object;

import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.yuanping.openglpractice.R;

public class ObjectActivity extends AppCompatActivity implements AlertDialog.OnClickListener {

    private GLSurfaceView mGLSurfaceView;
    private RelativeLayout root;
    private final String items[] = {"三角形", "正三角形", "彩色三角形", "正方形", "圆形", "正方体", "圆锥", "圆柱", "球体",
            "带光源的球体"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);
        init();
    }

    private void init() {
        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(new TriangleRenderer());
        root = findViewById(R.id.rl_root);
        root.addView(mGLSurfaceView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout
                .LayoutParams.MATCH_PARENT);
    }

    public void choseObj(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, this);
        builder.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0: {
                root.removeAllViews();
                mGLSurfaceView = null;
                mGLSurfaceView = new GLSurfaceView(this);
                mGLSurfaceView.setEGLContextClientVersion(2);
                mGLSurfaceView.setRenderer(new TriangleRenderer());
                root.addView(mGLSurfaceView, RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            break;
            case 1: {
                root.removeAllViews();
                mGLSurfaceView = null;
                mGLSurfaceView = new GLSurfaceView(this);
                mGLSurfaceView.setEGLContextClientVersion(2);
                mGLSurfaceView.setRenderer(new IsoscelesTriangleRenderer());
                root.addView(mGLSurfaceView, RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            break;
            case 2: {
                root.removeAllViews();
                mGLSurfaceView = null;
                mGLSurfaceView = new GLSurfaceView(this);
                mGLSurfaceView.setEGLContextClientVersion(2);
                mGLSurfaceView.setRenderer(new ColorTriangleRenderer());
                root.addView(mGLSurfaceView, RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            break;
            case 3: {
                root.removeAllViews();
                mGLSurfaceView = null;
                mGLSurfaceView = new GLSurfaceView(this);
                mGLSurfaceView.setEGLContextClientVersion(2);
                mGLSurfaceView.setRenderer(new SquareRenderer());
                root.addView(mGLSurfaceView, RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            break;
            case 4: {
                root.removeAllViews();
                mGLSurfaceView = null;
                mGLSurfaceView = new GLSurfaceView(this);
                mGLSurfaceView.setEGLContextClientVersion(2);
                mGLSurfaceView.setRenderer(new CircleRenderer());
                root.addView(mGLSurfaceView, RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            break;
            case 5: {
                root.removeAllViews();
                mGLSurfaceView = null;
                mGLSurfaceView = new GLSurfaceView(this);
                mGLSurfaceView.setEGLContextClientVersion(2);
                mGLSurfaceView.setRenderer(new CubeRenderer());
                root.addView(mGLSurfaceView, RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            break;
            case 6: {
                root.removeAllViews();
                mGLSurfaceView = null;
                mGLSurfaceView = new GLSurfaceView(this);
                mGLSurfaceView.setEGLContextClientVersion(2);
                mGLSurfaceView.setRenderer(new ConeRenderer());
                root.addView(mGLSurfaceView, RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            break;
            default:
                break;
        }
    }
}
