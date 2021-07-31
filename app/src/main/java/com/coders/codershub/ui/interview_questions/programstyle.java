package com.coders.codershub.ui.interview_questions;

import android.graphics.Color;
import android.widget.TextView;

public class programstyle {
  private String program_text;

    private TextView textView;
    public programstyle(String program_text , TextView textView)
    {

        this.program_text = program_text;
        this.textView = textView;
        attach();
    }
    public void attach()
    {
        setSpannable spannable = new setSpannable(program_text);

        spannable.getWords("#include","struct ","union ","int ","char ","float ","boolean ","void ","double ","printf","scanf",
                "if","else","while","for","goto","switch","case","typedef ","\"");
        spannable.getSize(-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1);
        spannable.getColors(Color.rgb(50, 168, 82),Color.rgb(173, 81, 170)
                ,Color.rgb(173, 81, 170),Color.rgb(119, 168, 217)
                ,Color.rgb(119, 168, 217),Color.rgb(119, 168, 217)
                ,Color.rgb(119, 168, 217),Color.rgb(119, 168, 217)
                ,Color.rgb(119, 168, 217),Color.rgb(4, 76, 148),
                Color.rgb(4, 76, 148), Color.rgb(230, 92, 96)
                , Color.rgb(230, 92, 96), Color.rgb(230, 92, 96)
                , Color.rgb(230, 92, 96), Color.rgb(230, 92, 96)
                , Color.rgb(230, 92, 96), Color.rgb(230, 92, 96),
                Color.rgb(242, 142, 70),Color.LTGRAY);
        spannable.getbcolor(-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1);
        spannable.setSpannableText(textView);
    }

}
