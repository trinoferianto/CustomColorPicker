package com.example.colorpicker;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CustomColorPicker.OnColorChangedListener {

    TextView endTV;
    EditText dragText;
    RelativeLayout dragArea;
    RelativeLayout.LayoutParams layoutParams;
    Button saveImg;
    ImageView resImg;
    TextView focused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveImg = (Button) findViewById(R.id.addBtn);
        saveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
            }
        });

        resImg = (ImageView) findViewById(R.id.resultImg);
        dragArea = (RelativeLayout) findViewById(R.id.dragArea);
        endTV = (TextView) findViewById(R.id.endTextView);
        dragText = (EditText) findViewById(R.id.dragET);
        dragText.setOnLongClickListener(new MyLongClickListener());

//        dragArea.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EditText et = new EditText(view.getContext());
//                dragArea.addView(et);
//                et.setFocusable(true);
//                et.setHint("Notes" + view.getX() + ", " + view.getY());
//                et.setOnLongClickListener(new MyLongClickListener());
//            }
//        });

        dragArea.setOnDragListener(new MyOnDragListener());
        dragArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    final CustomEditText et = new CustomEditText(view.getContext());
                    dragArea.addView(et);

                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)et.getLayoutParams();
                    lp.leftMargin = (int) motionEvent.getX();
                    lp.topMargin = (int) motionEvent.getY();
                    et.setLayoutParams(lp);
                    et.setOnLongClickListener(new MyLongClickListener());
                    et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean hasFocus) {
                            if (hasFocus) {
                                focused = et;
                            }

                            et.setCloseIcon(hasFocus);
                        }
                    });

                }
                return true;
            }
        });
    }

    @Override
    public void colorChanged(int color) {
        endTV.setTextColor(color);
    }

    private void saveImage(){

        if (focused != null){
            focused.setCursorVisible(false);
        }

        dragArea.setDrawingCacheEnabled(true);
        dragArea.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        dragArea.buildDrawingCache(true);

        Bitmap bmp = Bitmap.createBitmap(dragArea.getDrawingCache());
        dragArea.setDrawingCacheEnabled(false);
        resImg.setImageBitmap(bmp);
    }

    private class MyLongClickListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.VISIBLE);

            return true;
        }
    }

    private class MyOnDragListener implements View.OnDragListener{

        @Override
        public boolean onDrag(View v, DragEvent dragEvent) {
            float x = dragEvent.getX();
            float y = dragEvent.getY();
            View view = (View) dragEvent.getLocalState();

            layoutParams = (RelativeLayout.LayoutParams)view.getLayoutParams();

            switch (dragEvent.getAction()){
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
//                        x = dragEvent.getX();
//                        y = dragEvent.getY();
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    x = dragEvent.getX();
                    y = dragEvent.getY();
                    layoutParams.leftMargin = (int)x;
                    layoutParams.topMargin = (int)y;
                    view.setLayoutParams(layoutParams);
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    x = dragEvent.getX();
                    y = dragEvent.getY();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    break;
                case DragEvent.ACTION_DROP:
                    x = dragEvent.getX();
                    y = dragEvent.getY();
                    layoutParams.leftMargin = (int)x;
                    layoutParams.topMargin = (int)y;
                    view.setLayoutParams(layoutParams);
                    break;
            }

            return true;
        }
    }
}
