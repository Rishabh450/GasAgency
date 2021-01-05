package com.example.sudikshagasagency.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.example.sudikshagasagency.Fragment.ButtonFragment;
import com.example.sudikshagasagency.R;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static String currentFragment=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                new ButtonFragment()).commit();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(currentFragment.equals("AddRecordFragment")|| currentFragment.equals("NewFragment")|| currentFragment.equals("RecordFragment")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                    new ButtonFragment()).commit();
        }
    }
}