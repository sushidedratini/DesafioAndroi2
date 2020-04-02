package com.zappts.eduardosaito.desafioandroi2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.evolve.backdroplibrary.BackdropActions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class BackdropContainer extends FrameLayout implements BackdropActions, ExampleAdapter.OnEditCListener {

    private Context context;
    private Toolbar toolbar;
    private ToolbarIconClick toolbarIconClick;
    private Drawable mMenuicon;
    private Drawable mCloseicon;
    private Drawable mDoneicon;
    private int height;
    private static final String TAG = "BackdropContainer";
    private boolean clickedEdit = false;

    Interpolator interpolator;
    int duration;
    public BackdropContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context=context;
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.BackdropContainer,0,0);

        mMenuicon=typedArray.getDrawable(R.styleable.BackdropContainer_menuIcon);
        mCloseicon=typedArray.getDrawable(R.styleable.BackdropContainer_closeIcon);
        mDoneicon=typedArray.getDrawable( R.styleable.BackdropContainer_doneIcon );
        duration=typedArray.getInt(R.styleable.BackdropContainer_duration,1000);

        typedArray.recycle();

        DisplayMetrics metrics=new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height=metrics.heightPixels;
    }

    public BackdropContainer attachToolbar(Toolbar toolbar){
        this.toolbar=toolbar;
        this.toolbar.setNavigationIcon(mMenuicon);
        return  this;
    }

    public BackdropContainer dropHeight(int peek){
        height=height-peek;
        return this;
    }

    public BackdropContainer dropInterpolator(Interpolator interpolator){
        this.interpolator=interpolator;
        return  this;
    }

    public void build(){
        if (checkTotalview()){
            toolbarIconClick =new ToolbarIconClick(context, getFrontview(), getBackview(), mMenuicon,
                    mCloseicon, mDoneicon, height, interpolator, duration);
            toolbar.setNavigationOnClickListener(toolbarIconClick);
        }else {
            throw new ArrayIndexOutOfBoundsException("Backdrop should contain only two child");
        }
    }

    private int getFrontViewMargin() {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getFrontview().getLayoutParams();
        int frontViewtopMargin = layoutParams.topMargin;
        return frontViewtopMargin;
    }

    boolean checkTotalview(){
        if (getChildCount()>2){
            return false;
        }
        return true;
    }

    View getFrontview(){
        return getChildAt(1);
    }
    View getBackview(){
        return getChildAt(0);
    }

    private int dpToPx(int topmargin){
        Resources resources=getResources();
        float topMArginPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,topmargin,resources.getDisplayMetrics());
        return (int) topMArginPixels;
    }

    @Override
    public void showBackview() {
        toolbarIconClick.open();
    }

    @Override
    public void closeBackview() {
        toolbarIconClick.close();
    }

    @Override
    public void onEditClick(int position) {
        clickedEdit = true;
        //teste
    }
}
