package com.coders.codershub;

public class Question {
    private String Question_title,Option_1,Option_2,Option_3,Option_4,Option_correct,Quiz_Id,Reward;

    public String getQuestion_title() {
        return Question_title;
    }

    public void setQuestion_title(String question_title) {
        this.Question_title = question_title;
    }

    public String getOption_1() {
        return Option_1;
    }

    public void setOption_1(String option_1) {
        this.Option_1 = option_1;
    }

    public String getOption_2() {
        return Option_2;
    }

    public void setOption_2(String option_2) {
        this.Option_2 = option_2;
    }

    public String getOption_3() {
        return Option_3;
    }

    public void setOption_3(String option_3) {
        this.Option_3 = option_3;
    }

    public String getOption_4() {
        return Option_4;
    }

    public void setOption_4(String option_4) {
        this.Option_4 = option_4;
    }

    public String getOption_correct() {
        return Option_correct;
    }

    public void setOption_correct(String option_correct) {
        this.Option_correct = option_correct;
    }

    public String getQuiz_Id() {
        return Quiz_Id;
    }

    public void setQuiz_Id(String quiz_Id) {
        this.Quiz_Id = quiz_Id;
    }

    public String getReward() {
        return Reward;
    }

    public void setReward(String reward) {
        this.Reward = reward;
    }
}

