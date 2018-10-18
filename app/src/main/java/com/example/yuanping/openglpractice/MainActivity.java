package com.example.yuanping.openglpractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.yuanping.openglpractice.object.ObjectActivity;
import com.example.yuanping.openglpractice.utils.AssetsUtils;
import com.example.yuanping.openglpractice.utils.RvUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        RecyclerView rvRoot = findViewById(R.id.rv_root);
        RvUtils.setCommonRv(rvRoot, getClazz(), getTitles(), this);
    }

    private List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(AssetsUtils.getStringFromResource(R.string.object));
        return titles;
    }

    private List<Class> getClazz() {
        List<Class> listeners = new ArrayList<>();
        listeners.add(ObjectActivity.class);
        return listeners;
    }
}
