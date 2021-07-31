package com.coders.codershub.ui.interview_questions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.coders.codershub.R;

public class questionsadapter extends RecyclerView.Adapter<questionsadapter.Holderquestion> {
    public questionsadapter(int size,int color) {
        this.size = size;
        this.color = color;
    }
    int size;
    int color;
    private questionsadapter.OnItemClickListener mListener;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(questionsadapter.OnItemClickListener onItemClickListener)
    {
        mListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Holderquestion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.questionrow,parent,false);
        return new Holderquestion(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Holderquestion holder, int position) {
        holder.text.setText("Q" + (position+1));
        holder.card.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return size;
    }

    static class Holderquestion extends RecyclerView.ViewHolder
    {
        TextView text ;
        CardView card;
        public Holderquestion(@NonNull View itemView, final questionsadapter.OnItemClickListener listener) {
            super(itemView);
            text   =  itemView.findViewById(R.id.textView);
            card = itemView.findViewById(R.id.card);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                    {
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}

