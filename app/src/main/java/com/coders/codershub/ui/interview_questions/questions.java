package com.coders.codershub.ui.interview_questions;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coders.codershub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class questions extends Fragment {
    DatabaseReference myRef;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_questions_interview,container,false);

        start(root);

        return root;

    }

    private void start(View view)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        Bundle args = getArguments();
        String Company_Name =args.getString("Company_Name");
        final int color = args.getInt("Color", Color.WHITE);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycle2);
        assert Company_Name != null;
        myRef.child(Company_Name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final ArrayList<String> question, program, output;
                question = new ArrayList<>();
                program = new ArrayList<>();
                output = new ArrayList<>();
                DataSnapshot snapshot1;
                Iterable<DataSnapshot> snap = snapshot.getChildren();
                while (snap.iterator().hasNext()) {
                    snapshot1 = snap.iterator().next();
                    if (snapshot1.child("Statement").getValue() != null) {
                        question.add(snapshot1.child("Statement").getValue().toString().replaceAll("__", "\n"));
                    } else {
                        question.add("UNDER CONSTRUCTION");

                    }
                    if (snapshot1.child("Program").getValue() != null) {
                        program.add(snapshot1.child("Program").getValue().toString().replaceAll("__", "\n"));
                    } else {
                        program.add("UNDER CONSTRUCTION");

                    }
                    if (snapshot1.child("Output").getValue() != null) {
                        output.add(snapshot1.child("Output").getValue().toString().replaceAll("__", "\n"));
                    } else {
                        output.add("UNDER CONSTRUCTION");
                    }

                }
                questionsadapter adapter = new questionsadapter(question.size(), color);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                adapter.setOnItemClickListener(new questionsadapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        Bundle vars = new Bundle();
                        vars.putString("Question", question.get(position));
                        vars.putString("Program",program.get(position));
                        vars.putString("Output",output.get(position));

                        Display frag = new Display();
                        frag.setArguments(vars);
                        FragmentTransaction trans = getParentFragmentManager().beginTransaction();
                        trans.replace(R.id.nav_host_fragment,frag).addToBackStack("Display").commit();

                        //  Intent intent = new Intent(questions.this, Display.class);
                        // intent.putExtra("Question", question.get(position));
                        //   intent.putExtra("Program",program.get(position));
                        //  intent.putExtra("Output",output.get(position));
                        //  startActivity(intent);
                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}