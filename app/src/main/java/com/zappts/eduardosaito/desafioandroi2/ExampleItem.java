package com.zappts.eduardosaito.desafioandroi2;

public class ExampleItem {

    private String mText;
    private int mPriority;
    private boolean mAlarmSet;

    public ExampleItem(String text, int priority, boolean alarmSet) {
        mText = text;
        mPriority = priority;
        mAlarmSet = alarmSet;
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

    public boolean ismAlarmSet() {
        return mAlarmSet;
    }

    public void setmAlarmSet(boolean mAlarmSet) {
        this.mAlarmSet = mAlarmSet;
    }
}
