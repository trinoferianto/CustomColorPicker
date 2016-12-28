package com.example.colorpicker;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.provider.CalendarContract;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.jar.Attributes;

/**
 * Created by Mohammad_T on 12/26/2016.
 */
public class CustomColorPicker extends View {

    public interface OnColorChangedListener {
        void colorChanged(int color);
    }

    // constant properties
    private final float originalWidth = 256;
    private final float originalHeight = 20;
    private final String mNamespace = "http://schemas.android.com/apk/res/android";

    // properties
    private OnColorChangedListener mListener;
    private Paint mPaint;
    private float mCurrentHue;
    private int mSelectedHue = -1;
    private int[] mHueBarColors = new int[258];
    private int currentColor;
    private float widthScale;
    private int widthType;
    private int heightType;
    private float usedHeight;
    private float currentX = 0;

    public CustomColorPicker(Context context){
        super(context);
        mListener = (OnColorChangedListener)context;
    }

    public CustomColorPicker(Context context, AttributeSet attrs){
        super(context, attrs);
        mListener = (OnColorChangedListener)context;

        // get width and height type from attributes
        widthType = attrs.getAttributeIntValue(mNamespace, "layout_width", LinearLayout.LayoutParams.MATCH_PARENT);
        heightType = attrs.getAttributeIntValue(mNamespace, "layout_height", LinearLayout.LayoutParams.MATCH_PARENT);

        // Initializes the Paint that will draw the View
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(12);

        // Initialize the colors of the hue slider bar
        int index = 0;
        // Red (#f00) to pink (#f0f)
        for (float i = 0; i < 256; i += 256 / 42)
        {
            mHueBarColors[index] = Color.rgb(255, 0, (int) i);
            index++;
        }
        // Pink (#f0f) to blue (#00f)
        for (float i = 0; i < 256; i += 256 / 42)
        {
            mHueBarColors[index] = Color.rgb(255 - (int) i, 0, 255);
            index++;
        }
        // Blue (#00f) to light blue (#0ff)
        for (float i = 0; i < 256; i += 256 / 42)
        {
            mHueBarColors[index] = Color.rgb(0, (int) i, 255);
            index++;
        }
        // Light blue (#0ff) to green (#0f0)
        for (float i = 0; i < 256; i += 256 / 42)
        {
            mHueBarColors[index] = Color.rgb(0, 255, 255 - (int) i);
            index++;
        }
        // Green (#0f0) to yellow (#ff0)
        for (float i = 0; i < 256; i += 256 / 42)
        {
            mHueBarColors[index] = Color.rgb((int) i, 255, 0);
            index++;
        }
        // Yellow (#ff0) to red (#f00)
        for (float i = 0; i < 256; i += 256 / 42)
        {
            mHueBarColors[index] = Color.rgb(255, 255 - (int) i, 0);
            index++;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // count the height and width scales for drawing color bar hue
        // if layout_width is set to wrap_content from xml attribute, then return 1
        widthScale = widthType == LinearLayout.LayoutParams.WRAP_CONTENT ? 1 : ((float)(getWidth() - 50)) / originalWidth;
        // if layout_height is set to wrap_content from xml attribute, then use origin
        usedHeight = heightType == LinearLayout.LayoutParams.WRAP_CONTENT ? originalHeight : getHeight();

        // translate current hue
        int translatedHue = 255 - (int) (mCurrentHue * 255 / 360);

        // Display all the colors of the hue bar with lines
        for (int x = 0; x < 256; x++) {
            if (translatedHue != x) {
                mPaint.setColor(mHueBarColors[x]);
                mPaint.setStrokeWidth((int)widthScale + 10);
            }

            float xAxis = x*widthScale + 25;
            canvas.drawLine(xAxis, (usedHeight)/2, xAxis, (usedHeight)/2 + 1, mPaint);
        }

        if (mSelectedHue > -1 && mSelectedHue < 256){
            mPaint.setColor(mHueBarColors[mSelectedHue]);
        }

        if (currentX == 0) {
            currentX = 25;
        }

        mPaint.setColor(mHueBarColors[mSelectedHue == -1 ? 0 : mSelectedHue]);
        canvas.drawCircle(currentX, usedHeight / 2, 10, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                onColorChange(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                onColorChange(x, y);
                invalidate();
                break;
            default:
                return super.onTouchEvent(event);
        }

        return true;
    }

    private void onColorChange(float x, float y){
        // get selected color
        if (x > 0 + 25 && x < 256*widthScale + 25 && y > 0 && y < usedHeight) {
            mCurrentHue = (255 - (x-25)/widthScale) * 360 / 255;
            mSelectedHue = 255 - (int) (mCurrentHue * 255 / 360);
            currentColor =  mHueBarColors[mSelectedHue];
            currentX = x;
            mListener.colorChanged(currentColor);
        }
    }
}

