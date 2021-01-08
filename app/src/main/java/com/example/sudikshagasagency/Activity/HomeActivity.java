package com.example.sudikshagasagency.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.example.sudikshagasagency.Fragment.ButtonFragment;
import com.example.sudikshagasagency.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static String currentFragment=null;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth mAuth;


    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.e("ak47","on Start");
        super.onStart();
        Log.e("ak47","on Start after super");
        mAuth.addAuthStateListener(authStateListener);
        Log.e("ak47","on Start Ends");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        authStateListener = firebaseAuth -> {
            if(firebaseAuth.getCurrentUser()==null)
            {
                Log.e("ak47","user null");
                Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }


        };
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                new ButtonFragment()).commit();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    Boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {

        Log.d("backstalkcount", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
        if(currentFragment.equals("AddRecordFragment")|| currentFragment.equals("NewFragment")|| currentFragment.equals("RecordFragment")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                    new ButtonFragment()).commit();
        }

        else
            super.onBackPressed();
    }
}