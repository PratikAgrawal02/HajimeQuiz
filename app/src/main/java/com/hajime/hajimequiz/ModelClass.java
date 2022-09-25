package com.hajime.hajimequiz;

public class ModelClass {

    String Question;
    String Option1;
    String Option2;
    String Option3;
    String Option4;
    String answer;

    public ModelClass(String question, String option1, String option2, String option3, String option4, String answer){
        Question = question;
        this.Option1= option1;
        this.Option2= option2;
        this.Option3 = option3;
        this.Option4= option4;
        this.answer = answer;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getOption1() {
        return Option1;
    }

    public void setOption1(String option1) {
        Option1 = option1;
    }

    public String getOption2() {
        return Option2;
    }

    public void setOption2(String option2) {
        Option2 = option2;
    }

    public String getOption3() {
        return Option3;
    }

    public void setOption3(String option3) {
        Option3 = option3;
    }

    public String getOption4() {
        return Option4;
    }

    public void setOption4(String option4) {
        Option4 = option4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
