package com.coders.codershub;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private Context context;
    private ArrayList<Question> question;

    public QuestionAdapter(Context c, ArrayList<Question> q) {
        context = c;
        question = q;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionViewHolder(LayoutInflater.from(context).inflate(R.layout.question_card, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.QuestionViewHolder holder, int position) {
        holder.question_title.setText(question.get(position).getQuestion_title());
        holder.option_1.setText(question.get(position).getOption_1());
        holder.option_2.setText(question.get(position).getOption_2());
        holder.option_3.setText(question.get(position).getOption_3());
        holder.option_4.setText(question.get(position).getOption_4());
        holder.correctOption.setText(question.get(position).getOption_correct());
        holder.quiz_id.setText(question.get(position).getQuiz_Id());
        holder.reward.setText(String.format("Reward : %s", question.get(position).getReward()));
    }

    @Override
    public int getItemCount() {
        return question.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView question_title, correctOption, quiz_id,reward;
        Button option_1, option_2, option_3, option_4, submit;
        RadioGroup radioGroup = itemView.findViewById(R.id.options);
        ImageView correctness = itemView.findViewById(R.id.correctness);

        QuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            quiz_id = itemView.findViewById(R.id.quiz_Id);
            question_title = itemView.findViewById(R.id.question_title);
            reward = itemView.findViewById(R.id.Reward);
            option_1 = itemView.findViewById(R.id.Option_1);
            option_1.setOnClickListener(this);
            option_2 = itemView.findViewById(R.id.Option_2);
            option_2.setOnClickListener(this);
            option_3 = itemView.findViewById(R.id.Option_3);
            option_3.setOnClickListener(this);
            option_4 = itemView.findViewById(R.id.Option_4);
            option_4.setOnClickListener(this);
            correctOption = itemView.findViewById(R.id.Option_Correct);
            submit = itemView.findViewById(R.id.question_option_submit);
            submit.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            if(view.getId() == R.id.question_option_submit){
                if(radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(view.getContext(), "No Option Selected!", Toast.LENGTH_SHORT).show();
                }
                else {
                    final String qId = quiz_id.getText().toString();
                    final String[] SolvedQ = new String[1];
                    final String[] curr_coins = new String[1];
                    final int position = 1 + getAdapterPosition();
                    final int Reward = Integer.parseInt(reward.getText().toString().replace("Reward : ",""));

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    assert currentUser != null;
                    final DatabaseReference user = FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(Objects.requireNonNull(currentUser.getEmail()).replace('.','_'));

                    final DatabaseReference SQ = user.child("SolvedQuestions");
                    final DatabaseReference stars = user.child("Stars");
                    SQ.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            SolvedQ[0] = snapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    user.child("Coins").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            curr_coins[0] = snapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    user.child("PurchaseMade").child(qId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!Objects.requireNonNull(snapshot.getValue(String.class)).contains("Question_" + position)) {
                                user.child("PurchaseMade").child(qId).setValue(snapshot.getValue(String.class) + ",Question_" + position);
                                if (check(radioGroup.getCheckedRadioButtonId())) {

                                    stars.setValue(String.valueOf(FindStars(Integer.parseInt(SolvedQ[0])+1)));

                                    SQ.setValue(String.valueOf(Integer.parseInt(SolvedQ[0])+1));
                                    user.child("Coins").setValue(String.valueOf(Integer.parseInt(curr_coins[0]) + Reward));
                                }
                            }
                            if (check(radioGroup.getCheckedRadioButtonId())) {
                                correctness.setBackgroundResource(R.drawable.correct);
                                correctness.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correctness.setVisibility(View.GONE);
                                    }
                                }, 1000);
                                //Toast.makeText(view.getContext(), "Correct Answer", Toast.LENGTH_SHORT).show();
                            } else {
                                correctness.setBackgroundResource(R.drawable.incorrect);
                                correctness.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correctness.setVisibility(View.GONE);
                                    }
                                }, 1000);
                                //Toast.makeText(view.getContext(), "Wrong Answer", Toast.LENGTH_SHORT).show();
                            }
                        }

                        private int FindStars(int q) {
                            int s=0;
                            for(int i=1;i<6;i++){
                                if(q>=i*i){
                                    s=i;
                                }
                            }
                            return s;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        }

        private Boolean check(int Id) {
            String ans = correctOption.getText().toString();
            if (Id == R.id.Option_1 && ans.equals("Option_1")) {
                return true;
            }
            if (Id == R.id.Option_2 && ans.equals("Option_2")) {
                return true;
            }
            if (Id == R.id.Option_3 && ans.equals("Option_3")) {
                return true;
            }
            return Id == R.id.Option_4 && ans.equals("Option_4");
        }
    }
}