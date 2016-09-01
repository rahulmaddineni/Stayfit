/*
 * ProgressViews
 * Copyright (c) 2015  Natasa Misic
 *
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.natasa.progressviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.natasa.progressviews.utils.ProgressStartPoint;
import com.natasa.progressviews.utils.ShapeType;

public class CircleSegmentBar extends ProgressView {
    public static final int rad_360 = 360;
    private float SEGMENT_WIDTH = 3f;
    private int PADDING = 10;

    private Path progressPath;
    private Path backgroundPath;
    final RectF oval = new RectF();

    private float radius;
    private float angle;
    private int angleStartPoint = ProgressStartPoint.DEFAULT.getValue();
    private float left;
    private float top;
    private float right;
    private float bottom;
    private boolean isGradientColor;

    public CircleSegmentBar(Context context) {
        super(context);
    }

    public CircleSegmentBar(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    void init() {
        initBackgroundColor();
        initForegroundColor();
        progressPath = new Path();
        backgroundPath = new Path();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        if (width > height) {
            radius = height / 3;
        } else {
            radius = width / 3;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        left = 0 + strokeWidth / 2;
        top = 0 + strokeWidth / 2;
        right = min - strokeWidth / 2;
        bottom = min - strokeWidth / 2;
        oval.set(left + PADDING, top + PADDING, right - PADDING, bottom
                - PADDING);
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawGradientColor();
        drawCircle(canvas);

    }


    private void drawCircle(Canvas canvas) {
        backgroundPath.reset();
        for (int i = 0; i < rad_360; i += 5) {
            backgroundPath.addArc(oval, i, SEGMENT_WIDTH);
        }
        canvas.drawPath(backgroundPath, backgroundPaint);
        progressPath.reset();

        angle = rad_360 * (int) progress / maximum_progress + angleStartPoint;
        for (int i = angleStartPoint; i < angle; i += 5) {
            progressPath.addArc(oval, i, SEGMENT_WIDTH);
        }
        canvas.drawPath(progressPath, foregroundPaint);
        drawText(canvas);

    }

    public void setCircleViewPadding(int padding) {
        PADDING = padding;
        invalidate();
    }

    public float getSegmentWidth() {
        return SEGMENT_WIDTH;
    }

    /***
     * @
     ***/
    public void setSegmentWidth(float segment_width) {
        SEGMENT_WIDTH = segment_width;
    }

    public int getPadding() {
        return PADDING;
    }

    // *******************START POSITION FOR CIRCLE AND CIRCLE SEGMENT
    // VIEW******************
    public int getProgressStartPosition() {
        return startPosInDegrees;
    }

    public void setStartPositionInDegrees(int degrees) {
        this.startPosInDegrees = degrees;
    }

    public void setStartPositionInDegrees(ProgressStartPoint position) {
        this.startPosInDegrees = position.getValue();
    }


    private void drawGradientColor() {
        if (isGradientColor)
            setLinearGradientProgress(gradColors);
    }

    public void setLinearGradientProgress(boolean isGradientColor) {
        this.isGradientColor = isGradientColor;
    }

    public void setLinearGradientProgress(boolean isGradientColor, int[] colors) {
        this.isGradientColor = isGradientColor;
        gradColors = colors;

    }

    private void setLinearGradientProgress(int[] gradColors) {
        if (gradColors != null)
            colorHelper.setGradientPaint(foregroundPaint, left, top, right, bottom, gradColors);
        else
            colorHelper.setGradientPaint(foregroundPaint, left, top, right, bottom);

    }

    @Override
    public ShapeType setType(ShapeType type) {
        // TODO Auto-generated method stub
        return ShapeType.SEGMENT_CIRCLE;
    }

}
