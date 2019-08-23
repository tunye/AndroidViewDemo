package com.buaa.ct.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.buaa.ct.myapplication.adapter.UIEnterAdapter;

public class UIActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    UIEnterAdapter enterAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.ui);
        recyclerView = findViewById(R.id.enter);
        enterAdapter = new UIEnterAdapter(this);
        recyclerView.setAdapter(enterAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
    }
}
