package com.coders.codershub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class QuestionsActivity extends AppCompatActivity {

    RecyclerView question_recyclerView;
    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference quizReference = rootReference.child("Questions");
    ArrayList<Question> list;
    QuestionAdapter questionAdapter;
    TextView TextCoin;
    String curr_quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null){
            curr_quiz = (String) b.get("Quiz_name");
        }
        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        @SuppressLint("InflateParams") View mCustomView = mInflater.inflate(R.layout.action_bar,null);
        TextCoin = mCustomView.findViewById(R.id.coin_text);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        question_recyclerView = findViewById(R.id.question_recycler_view);
        question_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        quizReference.child(curr_quiz).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Question q = snapshot1.getValue(Question.class);
                    list.add(q);
                }
                questionAdapter = new QuestionAdapter(QuestionsActivity.this,list);
                question_recyclerView.setAdapter(questionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(QuestionsActivity.this,"An unexpected error occurred!",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
        assert curr_user != null;
        DatabaseReference userInfo = rootReference.child("Users")
                .child(Objects.requireNonNull(curr_user.getEmail()).replace('.','_'));

        userInfo.child("Coins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TextCoin.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(QuestionsActivity.this,"An unexpected error occurred!",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(QuestionsActivity.this,HomeActivity.class));
    }
}