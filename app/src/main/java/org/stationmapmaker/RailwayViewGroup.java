package org.stationmapmaker;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by TheAppExperts on 07/12/2016.
 */

public class RailwayViewGroup extends FrameLayout {

    public static final float GRID_LENGTH = 80.0f;

    public RailwayViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setPadding(1, 1, 1, 1);
        this.setBackground(ContextCompat.getDrawable(context, R.drawable.drawable_background));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            if (!(getChildAt(i) instanceof RailwayView))
                throw new IllegalArgumentException("All children must be instances of RailwayView");
        }
        setMeasuredDimension((int)GRID_LENGTH, (int)GRID_LENGTH);
    }
}
