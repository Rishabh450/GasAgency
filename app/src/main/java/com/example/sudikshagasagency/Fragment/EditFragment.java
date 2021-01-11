package com.example.sudikshagasagency.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sudikshagasagency.R;
import com.google.firebase.database.FirebaseDatabase;

public class EditFragment extends Fragment {
    EditText et,et1;
    String time;
    Button btn;
    int cylinder_lmt;
    int amt_lmt;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setDuration(300);
        scale.setInterpolator(new OvershootInterpolator());
        view.setAnimation(scale);
        assert getArguments() != null;
        time = getArguments().getString("id");
        cylinder_lmt = getArguments().getInt("cylinder_lmt");
        amt_lmt = getArguments().getInt("amt_lmt");
        et = view.findViewById(R.id.cylinder);
        et1 = view.findViewById(R.id.amount);
        btn = view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cylinder = et.getText().toString().trim();
                String amount = et1.getText().toString().trim();
                if(cylinder.isEmpty()){
                    et.setError("Please enter Cylinder returned");
                    et.requestFocus();
                }
                else if(Integer.parseInt(cylinder.trim())>cylinder_lmt )
                {
                    et.setError("Maximum cylinders can be " + cylinder_lmt);
                    et.requestFocus();
                }
                else if(amount.isEmpty()){
                    et1.setError("Please enter Amount Given");
                    et1.requestFocus();
                }
                else if(Integer.parseInt(amount.trim())>amt_lmt)
                {
                    et1.setError("Maximum amount can be " + amt_lmt);
                    et1.requestFocus();
                }
                else{
                    edit(Integer.parseInt(cylinder.trim()) ,Integer.parseInt(amount.trim()) );
                }
            }
        });
        return view;
    }
    public void edit(int cylinder,int amount){
        FirebaseDatabase.getInstance().getReference("Record").child(time).child("cylinderreturned").setValue(cylinder);
        FirebaseDatabase.getInstance().getReference("Record").child(time).child("amountgiven").setValue(amount);
        Toast.makeText(getActivity(),"Updated Successfully",Toast.LENGTH_SHORT).show();
        Fragment someFragment = new RecordFragment();
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, someFragment );
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
