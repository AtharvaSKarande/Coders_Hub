package com.coders.codershub.ui.interview_questions;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/*
* String s = getString(R.string.recursiveprogram);
        setSpannable span = new setSpannable(s);
        span.getWords("void","main","int ","\"","return","if","else if","else");
        span.getbcolor(-1,-1,-1,-1,-1,-1,-1,-1);
        span.getColors(Color.DKGRAY,Color.rgb(12, 16, 237),Color.rgb(250, 27, 209),Color.LTGRAY,Color.DKGRAY,Color.RED,Color.RED,Color.RED);
        span.getSize(-1,-1,-1,-1,-1,-1,-1,-1);
        span.setSpannableText(scanf1);
* */
/*
TextView text = findViewById(R.id.printftext);

        text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollermain.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
 */
public class setSpannable {
    String text;
    ArrayList<String> listwords ;
    ArrayList<Integer> listcolors;
    SpannableString spannablestring ;
    int start=0 ,length_of_word ,end=0 ;
    int length;
    ArrayList<Integer>sizeword;
    ArrayList<Integer>backgroundcol;
    public setSpannable(String text)
    {
        this.text = text ;
        spannablestring = new SpannableString(text);
    }
    public setSpannable()
    {

    }
    public void setString(String text)
    {
        // this.text = text ;
        spannablestring = new SpannableString(text);

    }
    public void getbcolor(Integer... colors)
    {
        backgroundcol = new ArrayList<>();
        backgroundcol.addAll(Arrays.asList(colors));
    }
    public void getWords(String... words)
    {
        listwords = new ArrayList<>();
        listwords.addAll(Arrays.asList(words));
    }
    public void getColors(int... colors)
    {
        listcolors = new ArrayList<>();
        for(int color : colors)
        {
            listcolors.add(color);
        }
    }
    public void getSize(int... size)
    {
        sizeword = new ArrayList<>();
        for(int sizee : size)
        {
            sizeword.add(sizee);
        }
    }
    public void setSpannableText(TextView textview)
    {

        for(int a=0  ; a< listwords.size() ; a++)
        {

            String insidetext = text ;
            int starter;int size,color;
            end=0;
            start=0;
            length=0;
            length_of_word = listwords.get(a).length();
            if(!listwords.get(a).equals("\"") || !listwords.get(a).equals("'")) {
                while ((start = insidetext.indexOf(listwords.get(a))) != -1) {
                    starter = start + end;
                    //start = text.indexOf(listwords.get(a));
                    end = starter + length_of_word;


                    spannablestring.setSpan(new ForegroundColorSpan(listcolors.get(a)), starter, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    try {
                        if (( color = backgroundcol.get(a)) != -1) {

                            spannablestring.setSpan(new BackgroundColorSpan(color), starter, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }catch(Exception ignored){}

                    try {
                        if ((size = sizeword.get(a)) != -1) {

                            spannablestring.setSpan(new AbsoluteSizeSpan(size,true), starter, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }catch(Exception ignored){}
                    insidetext = shortString(text, end);

                    if (insidetext == null) {
                        break;
                    }
                }
            }
            if(listwords.get(a).equals("\"")) {
                end = -1;
                start = 0;
                while ((start = text.indexOf("\"", end + 1)) != -1) {


                    end = text.indexOf("\"", start + 1);

                    spannablestring.setSpan(new ForegroundColorSpan(listcolors.get(a)), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    end++;
                }

            }

            if(listwords.get(a).equals("'")) {
                end = -1;
                start = 0;
                while ((start = text.indexOf("'", end + 1)) != -1) {

                    end = text.indexOf("'", start + 1);

                    spannablestring.setSpan(new ForegroundColorSpan(listcolors.get(a)), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    end++;
                }

            }

        }





        textview.setText(spannablestring);

    }
    private String shortString(String oldtext,int start)
    {

        try {
            return oldtext.substring(start);
        }
        catch(Exception ex)
        {
            return null;
        }
    }
}
