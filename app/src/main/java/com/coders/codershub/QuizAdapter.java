package com.coders.codershub;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
    private Context context;
    private ArrayList<Quiz> quiz;
    public QuizAdapter(Context c, ArrayList<Quiz> q)
    {
        context = c;
        quiz = q;
    }
    @NonNull
    @Override
    public QuizAdapter.QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuizViewHolder(LayoutInflater.from(context).inflate(R.layout.quiz_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final QuizAdapter.QuizViewHolder holder, final int position) {
        holder.q_title.setText(quiz.get(position).getTitle());
        holder.q_description.setText(quiz.get(position).getDescription());
        holder.q_Id.setText(quiz.get(position).getId());
        DatabaseReference PurchaseMade = reference.child(Objects.requireNonNull(currentUser.getEmail()).
                replace('.','_')).child("PurchaseMade");

        PurchaseMade.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(quiz.get(position).getId())){
                    holder.q_purchaseText.setText(R.string.purchased_text);
                    holder.q_purchaseText.setBackgroundColor(Color.parseColor("#60FF44"));
                }
                else{
                    holder.q_purchaseText.setText(String.format("Not Purchased!\nCost : %s Coins", quiz.get(position).getCost()));
                    holder.q_purchaseText.setBackgroundColor(Color.parseColor("#FF4444"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(context,"An unexpected error occurred!",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return quiz.size();
    }

    static class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView q_title,q_description,q_cost,q_purchaseText,q_Id;

        QuizViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            q_purchaseText = itemView.findViewById(R.id.quiz_purchase);
            q_title = itemView.findViewById(R.id.quiz_title);
            q_description = itemView.findViewById(R.id.quiz_description);
            q_cost = itemView.findViewById(R.id.quiz_cost);
            q_Id = itemView.findViewById(R.id.quiz_Id);
        }

        @Override
        public void onClick(final View view) {

            final String curr_quiz = "quiz_"+ (1+getAdapterPosition());
            final String[] quiz_cost = new String[1];
            final String quiz,quiz_title;
            final String[] curr_coins = new String[1];

            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            final DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
            assert currentUser != null;

            final DatabaseReference userRef = rootReference.child("Users").child(Objects.requireNonNull(currentUser.getEmail())
                    .replace('.','_'));

            userRef.child("Coins").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    curr_coins[0] = snapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            quiz_title = q_title.getText().toString();
            quiz = q_Id.getText().toString();

            final DatabaseReference quizRef = rootReference.child("Quizzes").child(curr_quiz);

            quizRef.child("Cost").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    quiz_cost[0] = snapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            userRef.child("PurchaseMade").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(quiz)){
                        //Purchased
                        Intent i = new Intent(view.getContext(),QuestionsActivity.class);
                        i.putExtra("Quiz_name",curr_quiz);
                        view.getContext().startActivity(i);
                    }
                    else {
                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Coders Hub")
                                .setMessage("\nDo you want to purchase '" +quiz_title+ "' quiz for " + quiz_cost[0] +" Coins ?")
                                .setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Make purchase
                                        int coins = Integer.parseInt(curr_coins[0]) - Integer.parseInt(quiz_cost[0]);
                                        if(coins >=0){
                                            userRef.child("Coins").setValue(String.valueOf(coins));
                                            DatabaseReference ref = userRef.child("PurchaseMade").child(quiz);
                                            ref.setValue("Purchased");
                                            //purchased
                                            q_purchaseText.setText(R.string.purchase_text);
                                            q_purchaseText.setBackgroundColor(Color.parseColor("#59FF00"));
                                        }
                                        else{
                                            Toast.makeText(view.getContext(),"Not Enough Coins",Toast.LENGTH_LONG).show();
                                        }

                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}
