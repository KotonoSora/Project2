package com.projectest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.projectest.util.AutoResizeTextView;


/**
 * Created by Admin on 9/27/2017.
 */

public class StickerTextView extends StickerView {
    private AutoResizeTextView tv_main;
    private String owner_id;

    public StickerTextView(Context context) {
        super(context);
    }

    public StickerTextView(Context context, AttributeSet attrs , int defStyle) {
        super(context, attrs ,defStyle);
    }


    @Override
    public View getMainView() {
        if(tv_main != null)
            return tv_main;

        tv_main = new AutoResizeTextView(getContext());
        //tv_main.setTextSize(22);
       // tv_main.setTextColor(Color.BLACK);
        tv_main.setGravity(Gravity.CENTER);
        tv_main.setTextSize(180);
        tv_main.setShadowLayer(3, 0, 0, Color.BLACK);
        tv_main.setMaxLines(4); // dong hien thi
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;
        tv_main.setLayoutParams(params);
        if(getImageViewFlip()!=null)
            getImageViewFlip().setVisibility(View.GONE);
        return tv_main;
    }

    public void setText(String text){
        if(tv_main!=null)
            tv_main.setText(text);
    }

    public String getText(){
        if(tv_main!=null)
            return tv_main.getText().toString();

        return null;
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

    @Override
    protected void onScaling(boolean scaleUp) {
        super.onScaling(scaleUp);
    }

    @Override
    public int setImageDrawable(int int_id_img) {
        return 0;
    }


    @Override
    public Object showTouchText(Object cpd_next) {
        return cpd_next;
    }

    public String getOwnerId() {
        return this.owner_id;
    }
    public void showTouchText(){
        ShowTextTouch();
    }

    public void setTextColor(int textColor) {
        tv_main.setTextColor(textColor);
    }


    public void setOwnerId(String ownerId) {
        this.owner_id = ownerId;
    }

    public  void setTypeface (Typeface font){
        if(tv_main != null){
            tv_main.setTypeface( font );
        }
    }
}
//