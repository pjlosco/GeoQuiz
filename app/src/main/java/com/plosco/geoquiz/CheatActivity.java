package com.plosco.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String TAG = "CheatActivity";

    public static final String EXTRA_QUESTION_IS_TRUE = "geoquiz.cheat.question_is_true";
    public static final String EXTRA_ANSWER_SHOWN = "geoquiz.cheat.answer_shown";
    private boolean mAnswerIsTrue;
    private boolean mAlreadyShownAnswer;

    private TextView mAnswerTextView;
    private TextView mAreYouSure;
    private Button mShowAnswer;

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
        mAlreadyShownAnswer = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        setAnswerShownResult(false);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_QUESTION_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);
        mAreYouSure = (TextView) findViewById(R.id.are_you_sure);
        mShowAnswer = (Button) findViewById(R.id.showAnswerButton);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true);

                hideViewWithAnimation(mShowAnswer);
                hideViewWithAnimation(mAreYouSure);
            }
        });

        if (savedInstanceState !=null) {
            mAlreadyShownAnswer = savedInstanceState.getBoolean(EXTRA_ANSWER_SHOWN, false);
            setAnswerShownResult(true);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void hideViewWithAnimation(View localView) {
        int cx = localView.getWidth() / 2;
        int cy = localView.getHeight() / 2;
        float radius = localView.getWidth();
        Animator animator = ViewAnimationUtils.createCircularReveal(localView, cx, cy, radius, 0);
        animator.addListener(new MyAnimatorListenerAdapter(localView));
        animator.start();
    }

    private class MyAnimatorListenerAdapter extends AnimatorListenerAdapter {
        View mView;

        MyAnimatorListenerAdapter(View view) {
            this.mView = view;
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            this.mView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putBoolean(EXTRA_ANSWER_SHOWN, mAlreadyShownAnswer);
    }
}
