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
import android.graphics.RectF;
import android.util.AttributeSet;

import com.natasa.progressviews.utils.ProgressStartPoint;
import com.natasa.progressviews.utils.ShapeType;

public class CircleProgressBar extends ProgressView {

    private int PADDING = 3;
    private RectF rectF;
    private float bottom;
    private float right;
    private float top;
    private float left;
    private float angle;
    private int radius;
    private boolean isGradientColor;
    private boolean isSweepGradientColor;
    private int colorStart, colorEnd;

    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        rectF = new RectF();
        initBackgroundColor();
        initForegroundColor();

    }

    //public void setRadialGradient() {
    //	ColorsHelper.setRadialGradientPaint(foregroundPaint, left, top, min);
    //}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGradientColor();
        drawStrokeCircle(canvas);

    }


    private void drawStrokeCircle(Canvas canvas) {

        canvas.drawOval(rectF, backgroundPaint);
        angle = 360 * getProgress() / maximum_progress;
        canvas.drawArc(rectF, startPosInDegrees, angle, false, foregroundPaint);
        drawText(canvas);
    }


    private void drawGradientColor() {
        if (isGradientColor) {
            setLinearGradientProgress(gradColors);
        }
        if (isSweepGradientColor) {
            setSweepGradPaint();
        }
    }

    public void setLinearGradientProgress(boolean isGradientColor) {
        this.isGradientColor = isGradientColor;
    }

    public void setLinearGradientProgress(boolean isGradientColor, int[] colors) {
        this.isGradientColor = isGradientColor;
        gradColors = colors;

    }

    public void setSweepGradientProgress(boolean isGradientColor, int colorStart, int colorEnd) {
        this.isSweepGradientColor = isGradientColor;
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;


    }

    private void setSweepGradPaint() {
        if (colorStart != 0 && colorEnd != 0 && colorHelper != null) {
            colorHelper.setSweepGradientPaint(foregroundPaint, min / 2, min / 2, colorStart, colorEnd);
        }
    }

    private void setLinearGradientProgress(int[] gradColors) {
        if (gradColors != null)
            colorHelper.setGradientPaint(foregroundPaint, left, top, right, bottom, gradColors);
        else
            colorHelper.setGradientPaint(foregroundPaint, left, top, right, bottom);

    }

    public void setCircleViewPadding(int padding) {
        PADDING = padding;
        invalidate();
    }

    public int getPadding() {
        return PADDING;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        left = 0 + strokeWidth / 2;
        top = 0 + strokeWidth / 2;
        right = min - strokeWidth / 2;
        bottom = min - strokeWidth / 2;
        rectF.set(left + PADDING, top + PADDING, right - PADDING, bottom
                - PADDING);
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

    @Override
    public ShapeType setType(ShapeType type) {
        return ShapeType.CIRCLE;

    }

}
