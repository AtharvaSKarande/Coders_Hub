package com.coders.codershub;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    TextView TextCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        BottomNavigationView navView = findViewById(R.id.nav_view);


        //ALL THE NAMES FOR THE BOTTOM_NAVIGATION ARE GIVEN HERE
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_interview_questions, R.id.navigation_quiz, R.id.navigation_profile)
                .build();


        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        @SuppressLint
                ("InflateParams") View mCustomView = mInflater.inflate(R.layout.action_bar, null);
        TextCoin = mCustomView.findViewById(R.id.coin_text);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        assert currentUser != null;
        final DatabaseReference userInfo = reference.child(Objects.requireNonNull(currentUser.getEmail()).replace('.', '_'));

        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Coins")) {
                    TextCoin.setText(snapshot.child("Coins").getValue(String.class));
                } else {
                    DatabaseReference coins = userInfo.child("Coins");
                    coins.setValue("50");
                    DatabaseReference stars = userInfo.child("Stars");
                    stars.setValue("0");
                    DatabaseReference SolvedQ = userInfo.child("SolvedQuestions");
                    SolvedQ.setValue("0");
                    DatabaseReference purchaseMade = userInfo.child("PurchaseMade").child("BasicLogIn");
                    purchaseMade.setValue("Done");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(HomeActivity.this, "An unexpected error occurred!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_right_corner_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.settings_menu){
            Intent i = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(i);
        }
        if(item.getItemId() == R.id.about_menu){
            Intent i = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (findViewById(R.id.quiz_recycler_view) != null ||
                findViewById(R.id.recyclerview) != null ||
                findViewById(R.id.userEmailId)!=null) {
            new AlertDialog.Builder(this)
                    .setTitle("Coders Hub")
                    .setMessage("\nAre you sure you want to exit?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }
}