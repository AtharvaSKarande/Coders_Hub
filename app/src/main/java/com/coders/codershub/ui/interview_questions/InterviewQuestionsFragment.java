package com.coders.codershub.ui.interview_questions;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coders.codershub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InterviewQuestionsFragment extends Fragment {

    RecyclerView rlist;
    adapter list_adapter;
    DatabaseReference myRef;
    FirebaseDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_main_interview, container, false);
        rlist= root.findViewById(R.id.recyclerview);
        database        = FirebaseDatabase.getInstance();
    //    database.setPersistenceEnabled(true);

        startDisplay();
        return root;
    }

    private void startDisplay()
    {

        myRef = database.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //THIS METHOD IS USED TO CREATE THE RECYCLER VIEW LIST

                createList(snapshot.child("Companies").getChildren());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createList(final Iterable<DataSnapshot> snapshot)
    {

        final ArrayList<String> list  = new ArrayList<>();
        final ArrayList<Integer> num = new ArrayList<>();
        DataSnapshot snapshot1;
        String key;
        int value;
        while(snapshot.iterator().hasNext())
        {

            snapshot1 = snapshot.iterator().next();
            key = snapshot1.getKey();
            if(key!=null)
            {
                list.add(key);
            }else
                list.add("");
            if(snapshot1.getValue()!=null) {
                value = Integer.parseInt(snapshot1.getValue().toString());
                num.add(value);

            }
            else
            {
                num.add(0);
            }




        }

        list_adapter =  new adapter(list,num);

        rlist.setAdapter(list_adapter);
        rlist.setLayoutManager(new LinearLayoutManager(getContext()));

        list_adapter.setOnItemClickListener(new adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position,int color) {

                Bundle vars = new Bundle();
                vars.putString("Company_Name",list.get(position));
                vars.putInt("Color", color);
                questions frag = new questions();
                frag.setArguments(vars);

                FragmentTransaction trans = getParentFragmentManager().beginTransaction();

                trans.replace(R.id.nav_host_fragment,frag).addToBackStack("frag").commit();
               // Intent intent = new Intent(DiscussFragment.this,questions.class);
                //intent.putExtra("Company_Name",list.get(position));
               // intent.putExtra("Color",color);

              //  startActivity(intent);

            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        startDisplay();
    }
}