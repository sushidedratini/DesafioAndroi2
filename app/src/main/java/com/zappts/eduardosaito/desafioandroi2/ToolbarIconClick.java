package com.zappts.eduardosaito.desafioandroi2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.appcompat.widget.AppCompatImageButton;

public class ToolbarIconClick implements View.OnClickListener, ExampleAdapter.OnEditCListener{

    private static final String TAG = "ToolbarIconClick";
    private Context context;
    private View backlayer;
    private View frontlayer;
    private Drawable hambergerIcon;
    private Drawable closeIcon;
    private Drawable doneIcon;
    private int translate;
    private Interpolator interpolator;
    private int anim_duration;
    private boolean dropped=false;
    private AnimatorSet animatorSet=new AnimatorSet();
    private AppCompatImageButton toolbaricon;
    private boolean clickedEdit = false;

    public ToolbarIconClick(Context context, View frontview, View backview, Drawable mMenuicon,
                            Drawable mCloseicon, Drawable mDoneIcon, int height, Interpolator interpolator, int duration) {

        this.context=context;
        this.frontlayer=frontview;
        this.backlayer=backview;
        this.hambergerIcon=mMenuicon;
        this.closeIcon=mCloseicon;
        this.doneIcon = mDoneIcon;
        this.interpolator=interpolator;
        anim_duration=duration;
        this.translate=height;
    }

    public  void  open(){

        if (!dropped){
            onClick(toolbaricon);
        }
    }

    public void close(){
        if (dropped){
            onClick(toolbaricon);
        }
    }

    @Override
    public void onClick(View v) {

        if (toolbaricon==null){
            this.toolbaricon=(AppCompatImageButton) v;
        }
        dropped=!dropped;
        animatorSet.removeAllListeners();
        animatorSet.end();
        animatorSet.cancel();

        updateIcon( v );

        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(frontlayer,"translationY",
                dropped? translate:0);
        animatorSet.play(objectAnimator);
        objectAnimator.setDuration(1000);
        objectAnimator.setInterpolator(interpolator);
        objectAnimator.start();
    }

    private void updateIcon(View v) {
        if (hambergerIcon!=null&&closeIcon!=null){
            if (dropped){
                toolbaricon.setImageDrawable(closeIcon);
            }else {
                toolbaricon.setImageDrawable(hambergerIcon);
            }
        }
    }

    @Override
    public void onEditClick(int position) {
        //teste
        clickedEdit = true;
    }

}