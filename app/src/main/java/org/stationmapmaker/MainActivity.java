package org.stationmapmaker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RailwayGrid main;
    private LinearLayout selectRail;
    private ImageButton colorPicker;
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
        colorPicker = (ImageButton)findViewById(R.id.color_picker);
        colorPicker.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        final ColorPicker picker = new ColorPicker(MainActivity.this);
        picker.show();
        Button okButton = (Button)picker.findViewById(R.id.okColorButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.setRailwayViewColor(Color.rgb(picker.getRed(), picker.getGreen(), picker.getBlue()));
                picker.dismiss();
            }
        });
    }
    public void setRailwayViewColor(int color) {
        int count = selectRail.getChildCount();
        for (int i = 0; i < count; i++) {
           View child = selectRail.getChildAt(i);
            if (child instanceof RailwayLine) {
                RailwayLine line = (RailwayLine)child;
                line.setLineColor(color);
                line.invalidate();
            }
        }
    }

}
