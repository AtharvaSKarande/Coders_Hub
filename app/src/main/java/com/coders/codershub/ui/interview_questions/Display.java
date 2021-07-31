package com.coders.codershub.ui.interview_questions;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coders.codershub.R;


public class Display extends Fragment {

    String output;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View root = inflater.inflate(R.layout.activity_display,container,false);
       start(root);

       return root;
    }

    private void start(View view)
    {
        Bundle arg = getArguments();
        String question = arg.getString("Question");
        String program = arg.getString("Program");
        output = arg.getString("Output");

        Button button  = view.findViewById(R.id.programbutton);
        final NestedScrollView main  = view.findViewById(R.id.mainscroller);
        TextView questiontext = view.findViewById(R.id.question);
        TextView programtext = view.findViewById(R.id.programtext);

        programstyle style = new programstyle(program,programtext);
        questiontext.setText(question);


        programtext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                main.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), output, Toast.LENGTH_LONG).show();
            }
        });
    }

    }

