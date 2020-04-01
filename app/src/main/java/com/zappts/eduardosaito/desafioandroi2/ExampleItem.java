package com.zappts.eduardosaito.desafioandroi2;

public class ExampleItem {

    private String mText;
    private int mPriority;

    public ExampleItem(String text, int priority) {
        mText = text;
        mPriority = priority;
    }

    public void changemText(String text) {
        mText = text;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public int getmPriority() {
        return mPriority;
    }

    public void setmPriority(int mPriority) {
        this.mPriority = mPriority;
    }
}
