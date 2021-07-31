package com.coders.codershub;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    static boolean exist =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!exist) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        exist = true;

        Objects.requireNonNull(getSupportActionBar()).hide();
        if(!isConnected(MainActivity.this))
        {
            buildDialog(MainActivity.this).show();
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This is loading screen. Will run for 2sec.
                    Intent i = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 2000);
        }
    }

    //  Code For Connectivity check STARTS.

    /*if(!isConnected(MainActivity.this))
        {
            buildDialog(MainActivity.this).show();
        }
     */

    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setCancelable(false);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Unable to access database. Please check your internet connection.");

        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                finish();
            }
        });
        builder.setPositiveButton("Retry",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });
        return builder;
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null;
    }

    //  Code For Connectivity check ENDS.

    @Override
    public void onBackPressed() {

    }
}