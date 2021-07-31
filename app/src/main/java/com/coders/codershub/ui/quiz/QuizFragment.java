package com.coders.codershub.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coders.codershub.Quiz;
import com.coders.codershub.QuizAdapter;
import com.coders.codershub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizFragment extends Fragment {

    RecyclerView quiz_recyclerView;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Quizzes");
    ArrayList<Quiz> list;
    QuizAdapter quizAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_quiz, container, false);

        quiz_recyclerView = root.findViewById(R.id.quiz_recycler_view);
        quiz_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Quiz q = dataSnapshot1.getValue(Quiz.class);
                    list.add(q);
                }
                quizAdapter = new QuizAdapter(getContext(),list);
                quiz_recyclerView.setAdapter(quizAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(getContext(),"An unexpected error occurred!",Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }
}