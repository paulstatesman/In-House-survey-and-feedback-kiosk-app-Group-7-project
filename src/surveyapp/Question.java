package surveyapp;

import java.util.List;

public class Question {
    public String text;
    public String type; // Text, Single Choice, Multiple Choice
    public List<String> options;

    public Question(String text, String type, List<String> options){
        this.text = text;
        this.type = type;
        this.options = options;
    }
}
