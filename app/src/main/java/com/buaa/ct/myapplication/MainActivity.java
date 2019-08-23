package com.buaa.ct.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.buaa.ct.myapplication.adapter.EnterAdapter;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EnterAdapter enterAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        recyclerView = findViewById(R.id.enter);
        enterAdapter = new EnterAdapter(this);
        recyclerView.setAdapter(enterAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, true));
    }
}
