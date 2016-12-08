package org.stationmapmaker;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by TheAppExperts on 07/12/2016.
 */

public class RailwayView extends View implements View.OnLongClickListener {
    public RailwayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnLongClickListener(this);
    }
    public RailwayView(Context context) {
        super(context);
        this.setOnLongClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    public boolean onLongClick(View view) {
        final ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(data, shadowBuilder, view, 0);
        if (view.getParent() instanceof RailwayViewGroup)
        view.setVisibility(View.INVISIBLE);
        return true;
    }
}
