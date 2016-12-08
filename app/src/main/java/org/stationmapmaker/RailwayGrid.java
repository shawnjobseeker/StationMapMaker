package org.stationmapmaker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by TheAppExperts on 08/12/2016.
 */

public class RailwayGrid extends FrameLayout implements View.OnDragListener {

    private HorizontalScrollView horizontal;
    private ScrollView vertical;
    private GridLayout grid;
    private ValueAnimator mAnimator;
    private AtomicBoolean mIsScrollingX = new AtomicBoolean(false);
    private AtomicBoolean mIsScrollingY = new AtomicBoolean(false);

    public RailwayGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.railway_grid, this);
        horizontal = (HorizontalScrollView) findViewById(R.id.grid_horizontal);
        vertical = (ScrollView) findViewById(R.id.grid_vertical);
        grid = (GridLayout) findViewById(R.id.grid);
        grid.setColumnCount(40);
        grid.setRowCount(40);
        grid.setOnDragListener(this);
    }
    public void addView(RailwayViewGroup view) {
        grid.addView(view);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        final View view = (View) event.getLocalState();
        final int index = calculateNewIndex(event.getX(), event.getY());
        RailwayViewGroup newGrid = (RailwayViewGroup)grid.getChildAt(index);
        View viewBeingAdded = null;
        RailwayViewGroup oldGrid = null;
        if (view.getParent() instanceof RailwayViewGroup)
            oldGrid = (RailwayViewGroup)view.getParent();
        if (oldGrid != null) {
            oldGrid.removeView(view);
            viewBeingAdded = view;
        }
        else
            viewBeingAdded = new RailwayLine((RailwayLine)view);
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_LOCATION:
                // do nothing if hovering above own position
                if (view == v) return true;
                // get the new list index
                // auto-scroll horizontally and vertically (but not at the same time)
                getScrollConditions(event.getY(), vertical, mIsScrollingY);
                if (!mIsScrollingY.get())
                    getScrollConditions(event.getX(), horizontal, mIsScrollingX);
                break;
            case DragEvent.ACTION_DROP:
                // remove the view from the old position
                grid.removeView(newGrid);
                // and push to the new
                newGrid.addView(viewBeingAdded);
                grid.addView(newGrid, index);
                viewBeingAdded.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                if (!event.getResult()) {
                    viewBeingAdded.setVisibility(View.VISIBLE);
                }
                stopScrolling();
                break;
        }
        return true;
    }
    private int calculateNewIndex(float x, float y) {
        // calculate which column to move to
        final float cellWidth = grid.getWidth() / grid.getColumnCount();
        final int column = (int)(x / cellWidth);

        // calculate which row to move to
        final float cellHeight = grid.getHeight() / grid.getRowCount();
        final int row = (int)Math.floor(y / cellHeight);

        // the items in the GridLayout is organized as a wrapping list
        // and not as an actual grid, so this is how to get the new index
        int index = row * grid.getColumnCount() + column;
        if (index >= grid.getChildCount()) {
            index = grid.getChildCount() - 1;
        }

        return index;
    }
    private void getScrollConditions(float dragOffSet, FrameLayout scrollView, AtomicBoolean mIsScrolling) {
        int scroll = (scrollView.getId() == R.id.grid_horizontal) ? scrollView.getScrollX() : scrollView.getScrollY();
        int gridLimit = (scrollView.getId() == R.id.grid_horizontal) ? grid.getWidth() : grid.getHeight();
        int current = (scrollView.getId() == R.id.grid_horizontal) ? scrollView.getWidth() : scrollView.getHeight();
        final Rect rect = new Rect();
        scrollView.getHitRect(rect);
        stopScrolling();
        if (dragOffSet -  scroll > current - 100) {
            startScrolling(scroll, gridLimit, scrollView, mIsScrolling);
            Log.d("scroll","downRight");
        }
        else if (dragOffSet - scroll < 100) {
            startScrolling(scroll, 0, scrollView, mIsScrolling);
            Log.d("scroll","upLeft");
        }
        else {
            stopScrolling();
            Log.d("scroll","no");
        }
    }
    private void startScrolling(int from, int to, final FrameLayout mScrollView, final AtomicBoolean mIsScrolling) {
        if (from != to && mAnimator == null) {
            mIsScrolling.set(true);
            mAnimator = new ValueAnimator();
            mAnimator.setInterpolator(new OvershootInterpolator());
            mAnimator.setDuration(10 * Math.abs(to - from));
            mAnimator.setIntValues(from, to);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (mScrollView instanceof ScrollView)
                        ((ScrollView)mScrollView).smoothScrollTo(0, (int) valueAnimator.getAnimatedValue());
                    else if (mScrollView instanceof HorizontalScrollView)
                        ((HorizontalScrollView)mScrollView).smoothScrollTo((int) valueAnimator.getAnimatedValue(), 0);
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsScrolling.set(false);
                    mAnimator = null;
                }
            });
            mAnimator.start();
        }
    }

    private void stopScrolling() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

}
