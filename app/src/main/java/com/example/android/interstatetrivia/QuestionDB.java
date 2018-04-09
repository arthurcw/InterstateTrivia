package com.example.android.interstatetrivia;

import android.content.Context;

/**
 * Last Update by Arthur Chan (4/8/2018)
 * Organize questions, answer choices and correct answers
 * There are 16 questions divided into 4 pots
 * Question 1 = Q1 to Q4 (User has to type in answer)
 * Question 2 = Q5 to Q8 (More than one answer may be true)
 * Question 3 = Q9 to Q12 (User can only select one answer)
 * Question 4 = Q13 to Q16 (User can only select one answer)
 */

public class QuestionDB {

    private Context context;
    public QuestionDB(Context context) { this.context = context; }

    // Build the question list from array-string in strings.xml
    public String[] questionList() {
        return context.getResources().getStringArray(R.array.questionList);
    }

    // Build the answer option (or hint for EditText) list from array-string in strings.xml
    public String[][] answerList() {

        String[][] buildList = {context.getResources().getStringArray(R.array.hintQuestion1),
                context.getResources().getStringArray(R.array.hintQuestion2),
                context.getResources().getStringArray(R.array.hintQuestion3),
                context.getResources().getStringArray(R.array.hintQuestion4),
                context.getResources().getStringArray(R.array.answerQuestion5),
                context.getResources().getStringArray(R.array.answerQuestion6),
                context.getResources().getStringArray(R.array.answerQuestion7),
                context.getResources().getStringArray(R.array.answerQuestion8),
                context.getResources().getStringArray(R.array.answerQuestion9),
                context.getResources().getStringArray(R.array.answerQuestion10),
                context.getResources().getStringArray(R.array.answerQuestion11),
                context.getResources().getStringArray(R.array.answerQuestion12),
                context.getResources().getStringArray(R.array.answerQuestion13),
                context.getResources().getStringArray(R.array.answerQuestion14),
                context.getResources().getStringArray(R.array.answerQuestion15),
                context.getResources().getStringArray(R.array.answerQuestion16)};
        return buildList;

    }

    // Correct Answer list
    public String[][] correctAnswer() {

        String[][] correctList = {{"MI"}, {"70"}, {"80"}, {"Chicago"},
                {"false", "true", "true", "false"},
                {"true", "true", "false", "true"},
                {"true", "true", "true", "true"},
                {"true", "false", "false", "true"},
                {"3"}, {"2"}, {"0"}, {"3"}, {"2"}, {"3"}, {"2"}, {"2"}};
        return correctList;
    }

}
