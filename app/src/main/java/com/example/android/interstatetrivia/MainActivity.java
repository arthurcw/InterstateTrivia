package com.example.android.interstatetrivia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Last Update by Arthur Chan (4/8/2018)
 */

public class MainActivity extends AppCompatActivity {

    // Declare global variables, views and question data set
    QuestionDB questionDB;                                     // Question class
    TextView[] textQuestion = new TextView[4];                 // Question TextView in layout
    EditText editTextQuestion1;                                // Question 1 EditText answer
    CheckBox[] boxQuestion2 = new CheckBox[4];                 // Question 2 Checkbox answer
    RadioGroup[] buttonGroupQuestion34 = new RadioGroup[2];    // Question 3/4 RadioGroup answer
    RadioButton[][] buttonQuestion34 = new RadioButton[2][4];  // Question 3/4 RadioButton answer

    // Question setup
    int numberOfQuestion = 4;                                  // 4 questions to show in layout
    int numberOfQuestionPerPot = 4;                            // 4 questions per question bin
    int[] questionListOffset = {0, 4, 8, 12};                  // location of each bin in questionList
    int[] questionDrawn = new int[numberOfQuestion];           // store which question is randomly selected
    int numberOfAnswers = 4;                                   // 4 answers to list for Q2 to Q4

    // Score Keeping
    int score;
    // Submit answer button and counter
    Button submitButton;
    int countHitSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Display logo in action bar
        actionBarIcon();

        // Initialize views
        initializeViews();

        // Draw and display questions on screen
        drawQuestion();

    }


    /**
     * Check answers when user hit Submit Answers button
     * 1) Make sure all questions are answered
     * 2) Check Answers
     *    Add score if correct and display in Toast
     *    Show correct answers in green, wrong answers in red
     * 3) Display number of correct answers
     * 4) Deactivate submit answer button
     */
    public void submitScore(View view) {

        // 1) Check all questions are answered
        // 2) If so, check answer for each question; if not, show message in Toast
        if (checkAnswerComplete()) {
            // Reset Score at the beginning
            score = 0;

            // Question 1 Check
            if (editTextQuestion1.getText().toString().toLowerCase().equals(
                    questionDB.correctAnswer()[questionDrawn[0]][0].toLowerCase())) {
                score++;
                editTextQuestion1.setBackgroundColor(
                        getResources().getColor(R.color.correct_answer));   //correct in green
            } else {
                editTextQuestion1.setBackgroundColor(
                        getResources().getColor(R.color.wrong_answer));     //wrong in red
            }

            // Question 2 Check
            // Check answer for each check box
            int countCorrectCheckBox = 0;
            for (int i = 0; i < numberOfAnswers; i++) {
                if (boxQuestion2[i].isChecked() ==
                        Boolean.valueOf(questionDB.correctAnswer()[questionDrawn[1]][i])) {
                    countCorrectCheckBox++;
                    boxQuestion2[i].setBackgroundColor(
                            getResources().getColor(R.color.correct_answer));   //correct in green
                } else {
                    boxQuestion2[i].setBackgroundColor(
                            getResources().getColor(R.color.wrong_answer));     //wrong in red
                }
            }
            if (countCorrectCheckBox == 4) { score++; }

            // Questions 3 and 4 Check
            // Checked if the correct radio button is checked
            for (int i = 2; i < numberOfQuestion; i++) {
                int correctAnswer = Integer.valueOf(
                        questionDB.correctAnswer()[questionDrawn[i]][0]);
                if (buttonQuestion34[i - 2][correctAnswer].isChecked()) {
                    score++;
                } else {
                    // Highlight wrong answer in red
                    findViewById(buttonGroupQuestion34[i - 2].
                            getCheckedRadioButtonId()).setBackgroundColor(
                                    getResources().getColor(R.color.wrong_answer));
                }

                // Highlight correct answer in green
                buttonQuestion34[i - 2][correctAnswer].setBackgroundColor(
                        getResources().getColor(R.color.correct_answer));

            }

            // 3) Display number of correct answers in Toast
            Toast.makeText(this, getString(R.string.toast_correct_answers, score),
                    Toast.LENGTH_LONG).show();

            // 4) Deactivate submit answer button
            countHitSubmitButton++;
            submitButtonStatus(countHitSubmitButton);

        } else {
            // Tell user to answer all questions before hitting submit button in Toast
            Toast.makeText(this, getString(R.string.toast_answer_all),
                    Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Reset layout when reset button is hit: Clear answers, draw 4 new questions
     */
    public void resetQuestion(View view) {

        // Clear answers and background color
        // Question 1
        editTextQuestion1.setText("");
        editTextQuestion1.setBackgroundColor(0);
        // Question 2
        for (int i = 0; i < numberOfAnswers; i++) {
            boxQuestion2[i].setChecked(false);
            boxQuestion2[i].setBackgroundColor(0);
        }
        // Questions 3 and 4
        for (int i = 2; i < numberOfQuestion; i++) {
            buttonGroupQuestion34[i - 2].clearCheck();
            for (int j = 0; j < numberOfAnswers; j++) {
               //buttonQuestion34[i - 2][j].setChecked(false);
               buttonQuestion34[i - 2][j].setBackgroundColor(0);
            }
        }

        // Draw 4 new questions
        drawQuestion();

        // Reset submit answer button status
        countHitSubmitButton = 0;
        submitButtonStatus(countHitSubmitButton);

    }

    /**
     * Randomly draw four questions from respective bin
     */
    private void drawQuestion(){

        // For loop for 4 questions
        // 1) Draw random number
        // 2) Populate the question to layout
        // 3) Populate the answer options to layout (Q1: EditText, Q2: CheckBox. Q3/4: RadioButton)
        for (int i = 0; i < numberOfQuestion; i++) {
            // 1) Draw a random integer from 0 to 3
            int random = (int) Math.floor(Math.random() * numberOfQuestionPerPot);
            questionDrawn[i] = random + questionListOffset[i];
            //Log.v("MainActivity", "Random number " + i + " is " + random);

            // 2) Populate the question to layout
            textQuestion[i].setText(questionDB.questionList()[questionDrawn[i]]);

            // 3) Populate answer options
            switch (i) {
                case 0:    // Question 1: Display hint and set EditText InputType
                    editTextQuestion1.setHint(
                            questionDB.answerList()[questionDrawn[i]][0]);
                    editTextQuestion1.setInputType(Integer.valueOf(
                            questionDB.answerList()[questionDrawn[i]][1]));
                    break;

                case 1:    // Question 2: Populate Checkboxes
                    for (int j = 0; j < numberOfAnswers; j++) {
                        boxQuestion2[j].setText(
                                questionDB.answerList()[questionDrawn[i]][j]);
                    }
                    break;

                case 2:    // Questions 3 and 4
                case 3:
                    for (int j = 0; j < numberOfAnswers; j++) {
                        buttonQuestion34[i-2][j].setText(
                                questionDB.answerList()[questionDrawn[i]][j]);
                    }
                    break;
            }

        }

    }

    /**
     * Add interstate logo to action bar, called in OnCreate Method
     */
    private void actionBarIcon() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.i94actionbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    /**
     * Initialize view, called in onCreate Method
     */
    private void initializeViews() {

        questionDB = new QuestionDB(this);
        // Question 1
        textQuestion[0] = (TextView) findViewById(R.id.q1_question);
        editTextQuestion1 = (EditText) findViewById(R.id.q1_answer);
        // Question 2
        textQuestion[1] = (TextView) findViewById(R.id.q2_question);
        boxQuestion2[0] = (CheckBox) findViewById(R.id.q2_checkBox1);
        boxQuestion2[1] = (CheckBox) findViewById(R.id.q2_checkBox2);
        boxQuestion2[2] = (CheckBox) findViewById(R.id.q2_checkBox3);
        boxQuestion2[3] = (CheckBox) findViewById(R.id.q2_checkBox4);
        // Question 3
        textQuestion[2] = (TextView) findViewById(R.id.q3_question);
        buttonGroupQuestion34[0] = (RadioGroup) findViewById(R.id.q3_buttonGroup);
        buttonQuestion34[0][0] = (RadioButton) findViewById(R.id.q3_button1);
        buttonQuestion34[0][1] = (RadioButton) findViewById(R.id.q3_button2);
        buttonQuestion34[0][2] = (RadioButton) findViewById(R.id.q3_button3);
        buttonQuestion34[0][3] = (RadioButton) findViewById(R.id.q3_button4);
        // Question 4
        textQuestion[3] = (TextView) findViewById(R.id.q4_question);
        buttonGroupQuestion34[1] = (RadioGroup) findViewById(R.id.q4_buttonGroup);
        buttonQuestion34[1][0] = (RadioButton) findViewById(R.id.q4_button1);
        buttonQuestion34[1][1] = (RadioButton) findViewById(R.id.q4_button2);
        buttonQuestion34[1][2] = (RadioButton) findViewById(R.id.q4_button3);
        buttonQuestion34[1][3] = (RadioButton) findViewById(R.id.q4_button4);

        // Submit Button
        countHitSubmitButton = 0;
        submitButton = findViewById(R.id.submit_button);
        submitButtonStatus(countHitSubmitButton);

    }

    /**
     * Check state of answers, @return true if all questions are answered
     */
    private boolean checkAnswerComplete() {

        boolean q1State = false, q2State = false, q3State = false, q4State = false;

        // Question 1
        if (editTextQuestion1.getText().length()>0) { q1State = true; }

        // Question 2
        if (boxQuestion2[0].isChecked() || boxQuestion2[1].isChecked()
                || boxQuestion2[2].isChecked() || boxQuestion2[3].isChecked()) { q2State = true; }

        // Question 3
        if (buttonGroupQuestion34[0].getCheckedRadioButtonId() != -1) { q3State = true; }

        // Question 4
        if (buttonGroupQuestion34[1].getCheckedRadioButtonId() != -1) { q4State = true; }

        // Return true if all questions are answered
        return (q1State && q2State && q3State && q4State);

    }

    /**
     * Activate/deactivate submit button
     * Can only submit answer once until questions are reset
     */
    private void submitButtonStatus(int count) {

        if (count == 0) {
            submitButton.setClickable(true);
            submitButton.setTextColor(getResources().getColor(R.color.button_text));
        } else {
            submitButton.setClickable(false);
            submitButton.setTextColor(getResources().getColor(R.color.button_grey_out_text));
        }

    }

}