package org.stationmapmaker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import static org.stationmapmaker.RailwayViewGroup.GRID_LENGTH;

/**
 * Created by TheAppExperts on 05/12/2016.
 */

public class RailwayLine extends RailwayView {

    private int lineColor;
    private float thickness;
    private Paint painter;
    private int style;

    public enum Direction {
        HORIZONTAL, VERTICAL, TOP_LEFT_CURVE, TOP_RIGHT_CURVE, BOTTOM_LEFT_CURVE, BOTTOM_RIGHT_CURVE
    }

    public RailwayLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RailwayLine, 0, 0);
        lineColor = array.getColor(R.styleable.RailwayLine_color, ContextCompat.getColor(context, android.R.color.black));
        thickness = array.getDimensionPixelSize(R.styleable.RailwayLine_thickness, 10);
        style = array.getInteger(R.styleable.RailwayLine_direction, 0);
        painter = new Paint(Paint.ANTI_ALIAS_FLAG);
    }
    public RailwayLine(RailwayLine line) {
        super(line.getContext());
        lineColor = line.getLineColor();
        style = line.getDirection();
        thickness = line.getThickness();
        painter = new Paint(Paint.ANTI_ALIAS_FLAG);
    }
    public void setLineColor(int color) {
        this.lineColor = color;
    }
    public int getDirection() {
        return style;
    }

    public int getLineColor() {
        return lineColor;
    }

    public float getThickness() {
        return thickness;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float mid = GRID_LENGTH / 2;
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        painter.setColor(lineColor);
        painter.setAntiAlias(true);
        painter.setStyle(Paint.Style.STROKE);
        painter.setStrokeWidth(thickness);
        switch (style) {
            case 0: canvas.drawLine(0.0f, mid, GRID_LENGTH, mid, painter);
                break;
            case 1: canvas.drawLine(mid, 0.0f, mid, GRID_LENGTH, painter);
                     break;
            case 2: drawArc(canvas, mid, 0, 0, mid);
                    break;
            case 3: drawArc(canvas, GRID_LENGTH, mid, mid, 0);
                break;
            case 4: drawArc(canvas, 0, mid, mid, GRID_LENGTH);
                break;
            case 5: drawArc(canvas, mid, GRID_LENGTH, GRID_LENGTH, mid);
                break;
            default:
                break;
        }
    }
    private void drawArc(Canvas canvas, float x1, float y1, float x2, float y2) {
        final Path path = new Path();
        float midX            = x1 + ((x2 - x1) / 2);
        float midY            = y1 + ((y2 - y1) / 2);
        float curveRadius = GRID_LENGTH / 3;
        float xDiff         = midX - x1;
        float yDiff         = midY - y1;
        double angle        = (Math.atan2(yDiff, xDiff) * (180 / Math.PI)) - 90;
        double angleRadians = Math.toRadians(angle);
        float pointX        = (float) (midX + curveRadius * Math.cos(angleRadians));
        float pointY        = (float) (midY + curveRadius * Math.sin(angleRadians));

        path.moveTo(x1, y1);
        path.cubicTo(x1,y1,pointX, pointY, x2, y2);
        canvas.drawPath(path, painter);
    }
    @Override
    public void setRotation(float rotation) {
        // disable rotation "the usual way"
    }

    @Override
    public void setPivotX(float pivotX) {
        // disable setting pivot
    }

    @Override
    public void setPivotY(float pivotY) {
        // disable setting pivot
    }

    @Override
    public void setTranslationY(float translationY) {
        // disable translation "the usual way"
    }
    @Override
    public void setTranslationX(float translationX) {
        // disable translation "the usual way"
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension((int)GRID_LENGTH, (int)GRID_LENGTH);

    }
}
