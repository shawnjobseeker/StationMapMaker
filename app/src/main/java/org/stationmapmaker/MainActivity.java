package org.stationmapmaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RailwayGrid main;
    private LinearLayout selectRail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (RailwayGrid)findViewById(R.id.activity_main);
        selectRail = (LinearLayout)findViewById(R.id.select_rail);
        for (int i = 0; i < 40*40; i++) {
            main.addView(new RailwayViewGroup(this, null));
        }
        selectRail.setOnDragListener(main);
    }

}
