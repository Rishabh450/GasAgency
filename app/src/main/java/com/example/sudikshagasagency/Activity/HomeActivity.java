package com.example.sudikshagasagency.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("menuclicked","yes");
        if (item.getItemId() == R.id.signout) {
            logoutDialog();
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

            super.onBackPressed();
    }


    private void logoutDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = ((Activity) this).getLayoutInflater();


        final View view = inflater.inflate(R.layout.confirm_layout, null);
        builder.setView(view);
        final Dialog dialog = builder.create();

        dialog.setContentView(R.layout.confirm_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.MyAnimation_Window;
        // (0x80000000, PorterDuff.Mode.MULTIPLY);
        dialog.show();
        CardView logout =  dialog.findViewById(R.id.logout_confirm);
        CardView cancel_logout = (CardView) dialog.findViewById(R.id.cancel_logout);
        ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                dialog.dismiss();

                // return false;
            }


        });
        cancel_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}