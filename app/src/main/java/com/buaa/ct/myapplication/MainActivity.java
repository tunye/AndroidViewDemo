package com.buaa.ct.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EnterAdapter enterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.enter);
        enterAdapter=new EnterAdapter(this);
        recyclerView.setAdapter(enterAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
