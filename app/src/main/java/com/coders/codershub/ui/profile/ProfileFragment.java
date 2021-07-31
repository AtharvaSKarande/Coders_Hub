package com.coders.codershub.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.coders.codershub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    TextView userName,userEmailId;
    ImageView userProfilePic;
    ImageView[] st = new ImageView[5];
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference userInfo = FirebaseDatabase.getInstance().getReference().child("Users")
            .child(Objects.requireNonNull(Objects.requireNonNull(user).getEmail()).replace('.','_'));

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        st[0] = root.findViewById(R.id.star1);
        st[1] = root.findViewById(R.id.star2);
        st[2] = root.findViewById(R.id.star3);
        st[3] = root.findViewById(R.id.star4);
        st[4] = root.findViewById(R.id.star5);

        assert user != null;
        userName = root.findViewById(R.id.userName);
        userEmailId = root.findViewById(R.id.userEmailId);
        userProfilePic = root.findViewById(R.id.userProfilePic);
        userName.setText(user.getDisplayName());
        userEmailId.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).into(userProfilePic);
        return root;
    }

    private void ApplyStars(int star) {

        for(int i=0;i<5;i++){
            if(i<star)
                st[i].setBackgroundResource(R.drawable.star);
            else
                st[i].setBackgroundResource(R.drawable.starfainted);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        userInfo.child("Stars").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ApplyStars(Integer.parseInt(Objects.requireNonNull(snapshot.getValue(String.class))));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}