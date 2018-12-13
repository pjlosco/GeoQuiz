package com.plosco.geoquiz;

public class TrueFalseQuestion {
    private int mQuestion;
    private boolean mTrueQuestion;
    private boolean mHasCheated = false;
    private boolean mHasAnswered = false;
    private boolean mGotRight = false;

    public TrueFalseQuestion(int question, boolean trueQuestion) {
        mQuestion = question;
        mTrueQuestion = trueQuestion;
    }

    public int getQuestion() {
        return mQuestion;
    }

    public boolean isQuestionTrue() {
        return mTrueQuestion;
    }

    public boolean isCheating() {
        return mHasCheated;
    }

    /**
     * Once an answer is flagged as cheating then it is a wrong answer
     * @param cheating
     */
    public void setCheating(boolean cheating) {
        this.mHasCheated = cheating;
        mHasAnswered = false;
        mGotRight = false;
    }

    public boolean hasAnswered() {
        return mHasAnswered;
    }

    public void setAnsweredResults(boolean hasAnswered, boolean wasRight) {
        if (isCheating() || hasAnswered()) {
            return;
        } else {
            mHasAnswered = hasAnswered;
            mGotRight = wasRight;
        }
    }

    public boolean gotRight() {
        return mGotRight;
    }
}
