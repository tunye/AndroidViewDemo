package com.buaa.ct.easyui.listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.buaa.ct.easyui.R;

public class ParallaxScollListViewTrestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parallax_scoll_listview_test);
        initListView();
    }

    private void initListView() {
        ParallaxScollListView mListView = findViewById(R.id.layout_listview);
        View header = LayoutInflater.from(this).inflate(R.layout.parallax_listview_header, null);
        ImageView mImageView = header.findViewById(R.id.layout_header_image);

        mListView.setZoomRatio(ParallaxScollListView.NO_ZOOM);
        mListView.setParallaxImageView(mImageView);
        mListView.addHeaderView(header);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1,
                new String[]{
                        "First Item",
                        "Second Item",
                        "Third Item",
                        "Fifth Item",
                        "Sixth Item",
                        "Seventh Item",
                        "Eighth Item",
                        "Ninth Item",
                        "Tenth Item",
                        "....."
                }
        );
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(view.getContext(), "click", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
