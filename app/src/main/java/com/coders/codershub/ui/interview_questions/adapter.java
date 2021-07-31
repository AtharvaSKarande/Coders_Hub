package com.coders.codershub.ui.interview_questions;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.coders.codershub.R;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.MyViewHolder> {

    ArrayList<String> CompanyNames;
    ArrayList<Integer> num ;
    private OnItemClickListener mListener;
    private int color[] =
            {
                    Color.rgb(30,136,229),
                    Color.rgb(192,202,51),
                    Color.rgb(229,57,53),
                    Color.rgb(100,192,202),
                    Color.rgb(73,182,236),
                    Color.rgb(251,140,0)

            };
    public interface OnItemClickListener
    {
        void onItemClick(int position,int color);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        mListener = onItemClickListener;
    }

    public adapter(ArrayList<String> companyNames,ArrayList<Integer> num) {
        CompanyNames = companyNames;
        this.num = num;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlist,parent,false);
        return  new MyViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(CompanyNames.get(position));
        holder.num.setText("Number of questions : " + num.get(position));
        holder.card.setBackgroundColor(color[position%color.length]);

    }

    @Override
    public int getItemCount() {
        return CompanyNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,num;
        CardView card;
        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.textView);
            num = itemView.findViewById(R.id.textView2);
            card = itemView.findViewById(R.id.cardview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                    {
                        int position = getAdapterPosition();
                        int col = color[position%color.length];
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position,col);
                        }
                    }
                }
            });

        }
    }

}

