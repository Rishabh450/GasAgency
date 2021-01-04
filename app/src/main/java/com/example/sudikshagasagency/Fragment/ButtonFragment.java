package com.example.sudikshagasagency.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sudikshagasagency.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ButtonFragment extends Fragment {
    TextView tv,tv1,tv2;
    Button newSupplier,addrecord;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button, container, false);
        tv = view.findViewById(R.id.LPGcylinder);
        tv1 = view.findViewById(R.id.HPcylinder);
        tv2 = view.findViewById(R.id.Acylinder);
        FirebaseDatabase.getInstance().getReference("Rate Chart").child("Rate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    tv.setText("LPG cylinder - Rs " + (String) snapshot.child("LPG").getValue());
                    tv1.setText("High Pressure - Rs "+(String) snapshot.child("Hp").getValue());
                    tv2.setText("Acetylen - Rs "+(String) snapshot.child("Acetylene").getValue());
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
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, someFragment );
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        addrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment someFragment = new AddRecordFragment();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, someFragment );
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    return view;}
}
