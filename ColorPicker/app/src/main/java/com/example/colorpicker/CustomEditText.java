package com.example.colorpicker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Created by Mohammad_T on 1/2/2017.
 */
public class CustomEditText extends EditText {

    private Drawable closeIcon;
    private ViewParent parent;

    public CustomEditText(Context context){
        super(context);
        init(context);
    }

    private void init(Context context){
        this.setBackground(null);
        this.setHint("Type here");

        closeIcon = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_close_clear_cancel);
        closeIcon.setBounds(0, 0, 20, 20);

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    CustomEditText et = CustomEditText.this;
                    if (event.getX() > et.getWidth() - et.getPaddingRight() - 20) {
                        parent = et.getParent();
                        if (parent != null && parent instanceof RelativeLayout) {
                            ((RelativeLayout) parent).removeView(et);
                        }
                    }
                }
                return false;
            }
        });
    }

    public void setCloseIcon(Boolean showed){
        if (showed){
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], closeIcon, this.getCompoundDrawables()[3]);
        }
        else {
            this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], null, this.getCompoundDrawables()[3]);
        }
    }



}
