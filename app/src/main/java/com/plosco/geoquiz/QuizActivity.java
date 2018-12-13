package com.plosco.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private TrueFalseQuestion[] mQuestionBank = new TrueFalseQuestion[]{
            new TrueFalseQuestion(R.string.question_oceans, true),
            new TrueFalseQuestion(R.string.question_turkey, false),
            new TrueFalseQuestion(R.string.question_mideast, false),
            new TrueFalseQuestion(R.string.question_africa, false),
            new TrueFalseQuestion(R.string.question_americas, true),
            new TrueFalseQuestion(R.string.question_asia, true)
    };

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getQuestion();
        mIsCheater = false;
        mQuestionTextView.setText(question);
        Log.d(TAG, "Showing question " + mCurrentIndex);
    }

    private void checkAnswer(boolean userPressedTrue) {
        // dont allow answer changes once submitted
        if (mQuestionBank[mCurrentIndex].hasAnswered()) {
            return;
        }

        // Keep track of ID's as ints rather than literal strings since this refers to a resource
        int messageResId = 0;

        if (mIsCheater || mQuestionBank[mCurrentIndex].isCheating()) {
            messageResId = R.string.judgement_toast;
        } else {
            // check if button pressed matches answer to determine result
            if (userPressedTrue == mQuestionBank[mCurrentIndex].isQuestionTrue()) {
                messageResId = R.string.correct_toast;
                mQuestionBank[mCurrentIndex].setAnsweredResults(true, true);
            } else {
                messageResId = R.string.incorrect_toast;
                mQuestionBank[mCurrentIndex].setAnsweredResults(true, false);
            }
        }
        Toast answer = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        answer.setGravity(Gravity.CENTER, 0, 0);
        answer.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle called");
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextQuestion();
            }
        });

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextQuestion();
            }
        });

        mPreviousButton = findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentIndex == 0) {
                    mCurrentIndex = mQuestionBank.length - 1;
                } else {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                }
                updateQuestion();
            }
        });

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                intent.putExtra(CheatActivity.EXTRA_QUESTION_IS_TRUE, mQuestionBank[mCurrentIndex].isQuestionTrue());
                startActivityForResult(intent, 0);
            }
        });

        updateQuestion();
    }

    private void showNextQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        mIsCheater = mQuestionBank[mCurrentIndex].isCheating();
        updateQuestion();
    }

    private int getQuizResults() {
        Log.d(TAG, "Getting quiz results");
        int numberOfCorrectAnswers = 0;
        for (TrueFalseQuestion question : mQuestionBank) {
            if (question.gotRight()) {
                numberOfCorrectAnswers++;
            }
        }
        Log.d(TAG, "Total number of correct answers = " +numberOfCorrectAnswers);
        return (numberOfCorrectAnswers * 100 / mQuestionBank.length );
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
        mQuestionBank[mCurrentIndex].setCheating(mIsCheater);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.get_score) {
            Toast answer = Toast.makeText(this, "Score: " + getQuizResults() + "%", Toast.LENGTH_SHORT);
            answer.setGravity(Gravity.CENTER, 0, 0);
            answer.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
