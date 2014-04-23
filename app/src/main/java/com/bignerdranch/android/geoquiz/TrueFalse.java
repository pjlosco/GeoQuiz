package com.bignerdranch.android.geoquiz;

/**
 * Created by patrick on 4/22/14.
 */
public class TrueFalse {

    private int mQuestion;
    private boolean mTrueQuestion;
    private boolean mHasCheated = false;
    
    public TrueFalse(int question, boolean trueQuestion) {
        mQuestion = question;
        mTrueQuestion = trueQuestion;
    }

    public int getQuestion() {
        return mQuestion;
    }

    public void setQuestion(int mQuestion) {
        this.mQuestion = mQuestion;
    }

    public boolean isTrueQuestion() {
        return mTrueQuestion;
    }

    public void setTrueQuestion(boolean mTrueQuestion) {
        this.mTrueQuestion = mTrueQuestion;
    }

    public boolean isCheating() {
        return mHasCheated;
    }

    public void setCheating(boolean cheating) {
        this.mHasCheated = cheating;
    }
}
