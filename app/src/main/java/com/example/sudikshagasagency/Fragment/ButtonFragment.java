package com.example.sudikshagasagency.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sudikshagasagency.Activity.HomeActivity;
import com.example.sudikshagasagency.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ButtonFragment extends Fragment {
    CardView card_view;
    TextView tv, tv1, tv2;
    Button newSupplier, addrecord, record;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button, container, false);
        HomeActivity.currentFragment = "ButtonFragment";
        card_view = view.findViewById(R.id.card_view);
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment someFragment = new RateFormFragment();
                fragment(someFragment);
            }
        });
        tv = view.findViewById(R.id.LPGcylinder);
        tv1 = view.findViewById(R.id.HPcylinder);
        tv2 = view.findViewById(R.id.Acylinder);
        FirebaseDatabase.getInstance().getReference("Rate Chart").child("Rate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    tv.setText("LPG cylinder - Rs " + (String) snapshot.child("LPG").getValue());
                    tv1.setText("High Pressure - Rs " + (String) snapshot.child("Hp").getValue());
                    tv2.setText("Acetylen - Rs " + (String) snapshot.child("Acetylene").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        newSupplier = view.findViewById(R.id.newSupplier);
        addrecord = view.findViewById(R.id.addrecord);
        newSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment someFragment = new NewSupplierFormFragment();
                fragment(someFragment);
            }
        });
        addrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment someFragment = new AddRecordFragment();
                fragment(someFragment);
            }
        });
        record = view.findViewById(R.id.record);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment someFragment = new RecordFragment();
                fragment(someFragment);
            }
        });
        return view;
    }

    public void fragment(Fragment someFragment) {
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}