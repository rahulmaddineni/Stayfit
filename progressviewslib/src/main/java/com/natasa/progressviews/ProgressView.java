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

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.natasa.progressviews.utils.OnProgressViewListener;
import com.natasa.progressviews.utils.ProgressShape;
import com.natasa.progressviews.utils.ProgressStartPoint;

abstract class ProgressView extends View implements ProgressShape {

    protected float progress = 0;
    protected float strokeWidth = getResources().getDimension(
            R.dimen.default_stroke_width);
    protected float backgroundStrokeWidth = getResources().getDimension(
            R.dimen.default_background_stroke_width);
    protected int backgroundColor = getResources().getColor(R.color.background_color);
    protected int color = getResources().getColor(R.color.progress_color);
    protected int height;
    protected int width;
    protected int min;
    protected Paint backgroundPaint;
    protected Paint foregroundPaint;
    private String PROGRESS = getResources().getString(R.string.progress);
    protected int startPosInDegrees = ProgressStartPoint.DEFAULT.ordinal();
    private ObjectAnimator objAnimator;
    protected ProgressViewTextData text_data = new ProgressViewTextData(
            Color.LTGRAY, 42);
    private OnProgressViewListener listenr;
    protected boolean isShadow_background, isShadow_progress;
    protected float maximum_progress = 100f;
    private int shaderColor = getResources().getColor(R.color.shader_color);
    protected ColorsHelper colorHelper;
    protected int[] gradColors;
    protected boolean isRoundEdge;


    abstract void init();

    public void setOnProgressViewListener(OnProgressViewListener listener) {
        this.listenr = listener;
    }

    public ProgressView(Context context) {
        super(context);
        init();
        colorHelper = new ColorsHelper();

    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypedArray(context, attrs);
        init();

    }

    public ProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.CircleProgressBar, 0, 0);
        try {
            progress = typedArray.getFloat(
                    R.styleable.CircleProgressBar_progress, progress);
            strokeWidth = typedArray.getDimension(
                    R.styleable.CircleProgressBar_progress_width, strokeWidth);
            backgroundStrokeWidth = typedArray.getDimension(
                    R.styleable.CircleProgressBar_bar_width,
                    backgroundStrokeWidth);
            color = typedArray.getInt(
                    R.styleable.CircleProgressBar_progress_color, color);
            backgroundColor = typedArray.getInt(
                    R.styleable.CircleProgressBar_bar_color, backgroundColor);
            text_data.textColor = typedArray.getInt(
                    R.styleable.CircleProgressBar_text_color,
                    text_data.textColor);
            text_data.textSize = typedArray
                    .getInt(R.styleable.CircleProgressBar_text_size,
                            text_data.textSize);
        } finally {
            typedArray.recycle();
        }
        setShadowLayer();
        colorHelper = new ColorsHelper();


    }

    private void setShadowLayer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, backgroundPaint);
            setLayerType(LAYER_TYPE_SOFTWARE, foregroundPaint);

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        min = setDimensions(widthMeasureSpec, heightMeasureSpec);
    }

    protected int setDimensions(int widthMeasureSpec, int heightMeasureSpec) {
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);

        final int smallerDimens = Math.min(width, height);
        if (this instanceof ArcProgressBar) {
            setMeasuredDimension(smallerDimens, smallerDimens / 2);

        } else {
            setMeasuredDimension(smallerDimens, smallerDimens);

        }
        return smallerDimens;
    }

    // *****************************PROGRESS****************************************

    /**
     * @return Current progress value
     */
    protected float getProgress() {
        return progress;
    }

    /**
     * ProgressBar with progress values shown.
     *
     * @param progress value
     */
    public void setProgress(float progress) {
        setProgressInView(progress);
    }

    private void setProgressInView(float progress) {
        this.progress = (progress <= maximum_progress) ? progress : maximum_progress;
        invalidate();
        trackProgressInView(progress);
    }

    /****
     * reset progress to 0
     */
    public void resetProgressBar() {
        setProgress(0);

    }

    // ********************PROGRESS WIDTH**********************************
    public void setWidth(int circleWidth) {
        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.width = circleWidth;
        requestLayout();

    }

    public float getWidthProgressBarLine() {
        return strokeWidth;
    }

    public void setWidthProgressBarLine(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        foregroundPaint.setStrokeWidth(strokeWidth);
        invalidate();
        requestLayout();
    }

    public float getWidthProgressBackground() {
        return backgroundStrokeWidth;
    }

    public void setWidthProgressBackground(float backgroundStrokeWidth) {
        this.backgroundStrokeWidth = backgroundStrokeWidth;
        backgroundPaint.setStrokeWidth(strokeWidth);
        invalidate();
        requestLayout();
    }

    // *********************COLORS************************************

    protected void initForegroundColor() {
        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(color);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);
        if (isRoundEdge) {
            foregroundPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        // if (isShadow_progress)
        //foregroundPaint.setShadowLayer(1, 2, 4, shaderColor);

    }

    protected void initBackgroundColor() {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);
        if (isShadow_background)
            backgroundPaint.setShadowLayer(2, 2, 4, shaderColor);

    }

    public void setRoundEdgeProgress(boolean isRoundEdge) {
        this.isRoundEdge = isRoundEdge;
        init();
    }

    /**
     * @return progress color from ProgressBar
     ***/

    public int getProgressColor() {
        return color;
    }

    public void setProgressColor(int color) {
        this.color = color;
        foregroundPaint.setColor(color);
        invalidate();
        requestLayout();
    }

    /**
     * @return background color from ProgressBar
     ***/
    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPaint.setColor(backgroundColor);
        invalidate();
        requestLayout();
    }


// ***********************ANIMATOR**********************************************

    /**
     * ProgressBar with with progress values shown.Duration of Animation is
     * default (1000ms). For Animation duration change use {
     * setProgressAnimation(float progress, int animSpeedMilisec)}
     */
    // private void setProgressWithAnimation(float progress, int
    // animSpeedMillisec) {
    // initAnimator(progress);
    // objAnimator.setDuration(animSpeedMillisec);
    // objAnimator.start();
    // }
    private void initAnimator(float progres) {
        objAnimator = ObjectAnimator.ofFloat(this, PROGRESS, progres);
        objAnimator.setInterpolator(new DecelerateInterpolator());

        trackProgressInView(progress);
    }

    private void trackProgressInView(float progress) {
        if (listenr != null) {
            listenr.onProgressUpdate(progress);
            if (progress >= maximum_progress) {
                listenr.onFinish();
            }
        }
    }

    /**
     * ProgressBar with indeterminate mode with an infinite looping animation.
     * <i>Duration is set by default value (1000 ms).</i> For Animation duration
     * change use <br>setProgressIndeterminateAnimation(int animSpeedMillisec)</br>
     */
    //public void setProgressIndeterminateAnimation() {
    //setProgressIndeterminateAnimation(1000);
    //}

    /**
     * ProgressBar with indeterminate mode with an infinite looping animation.
     * <b>Use this as loading wheel without progress.</b> Cancel animation with
     * <code>cancelAnimation</code> method.
     *
     * @param animSpeedMillisec animation duration in milliseconds
     */
    public void setProgressIndeterminateAnimation(int animSpeedMillisec) {

        initAnimator(maximum_progress);
        objAnimator.setDuration(animSpeedMillisec);
        objAnimator.setRepeatCount(ValueAnimator.INFINITE);

        objAnimator.start();

    }

    /**
     * Cancel ProgressBar with indeterminate mode.
     */
    public void cancelAnimation() {
        if (objAnimator != null) {
            objAnimator.cancel();
        }
    }

    // *************************** TEXT
    // METHODS*************************************
    public static class ProgressViewTextData {
        public int textColor;
        public int textSize;
        public boolean isWithText;
        public String progressText;

        public ProgressViewTextData(int textColor, int textSize) {
            this.textColor = textColor;
            this.textSize = textSize;
        }

    }

    public int getTextColor() {
        return this.text_data.textColor;
    }

    public void setTextColor(int textColor) {
        this.text_data.textColor = textColor;
        invalidate();
    }

    public int getTextSize() {
        return this.text_data.textSize;
    }

    /**
     * @param textSize text size in pixels
     **/
    public void setTextSize(int textSize) {
        this.text_data.textSize = textSize;
    }

    /**
     * @param text to be shown
     **/
    public void setText(String text) {
        this.text_data.isWithText = true;
        text_data.progressText = text;
        invalidate();
    }

    /**
     * @param text  to be shown
     * @param color text color
     **/
    public void setText(String text, int color) {
        this.text_data.isWithText = true;
        text_data.progressText = text;
        this.text_data.textColor = color;
        invalidate();
    }

    public void setText(String text, int textSize, int color) {
        this.text_data.isWithText = true;
        this.text_data.progressText = text;
        this.text_data.textColor = color;
        this.text_data.textSize = textSize;
        invalidate();
    }

    protected void drawText(Canvas canvas) {
        if (text_data.isWithText)
            colorHelper.drawTextCenter(canvas, text_data.progressText,
                    text_data.textColor, text_data.textSize, min);

    }

    protected void drawTextLine(Canvas canvas) {
        if (text_data.isWithText)
            colorHelper.drawTextCenter(canvas, text_data.progressText,
                    text_data.textColor, text_data.textSize, width);

    }

//TODO:*******************	SHADOW ***********************

//public boolean isShadowInBackground() {
    //return isShadow_background;
    //}

    //public void setShadowInBackground(boolean isShadow_background) {
    //	this.isShadow_background = isShadow_background;
    //	init();

    //}
    //public void setShadowInBackground(boolean isShadow_background, String hexColor) {
    //this.isShadow_background = isShadow_background;
    //convertStringToIntColor(hexColor);
    //	init();

    //}


    //public boolean isShadowInProgress() {
    //	return isShadow_progress;
    //}
    //public void setShadowInProgress(boolean isShadow_progress, String hexColor) {
    //	this.isShadow_progress = isShadow_progress;
    //	convertStringToIntColor(hexColor);
    //	init();

    //}
    //public void setShadowInProgress(boolean isShadow_progress) {
    //	this.isShadow_progress = isShadow_progress;
    //	init();
    //}
    private void convertStringToIntColor(String hexColor) {
        if (hexColor != null) {
            try {
                this.shaderColor = Color.parseColor(hexColor);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
