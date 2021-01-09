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

import java.util.HashMap;

public class RateFormFragment extends Fragment {
    EditText et,et1,et2;
    Button b;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rateform, container, false);
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setDuration(300);
        scale.setInterpolator(new OvershootInterpolator());
        view.setAnimation(scale);
        et = view.findViewById(R.id.Lpgcylinder);
        et1 = view.findViewById(R.id.Hpcylinder);
        et2 = view.findViewById(R.id.ACylinder);
        b = view.findViewById(R.id.btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lpg = et.getText().toString().trim();
                String HP = et1.getText().toString().trim();
                String Acetylene = et2.getText().toString().trim();
                if(lpg.isEmpty()){
                    et.setError("Please enter LPG rate");
                    et.requestFocus();
                }
                else if(HP.isEmpty()){
                    et1.setError("Please enter High Pressure rate");
                    et1.requestFocus();
                }
                else if(Acetylene.isEmpty()){
                    et2.setError("Please enter Acetylene rate");
                    et2.requestFocus();
                }
                else{
                    okfunction(lpg,HP,Acetylene);
                }
            }
        });
    return view;}
    public void okfunction(String lpg,String hp,String Acetylene){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("LPG",lpg);
        hashMap.put("Hp",hp);
        hashMap.put("Acetylene",Acetylene);
        FirebaseDatabase.getInstance().getReference("Rate Chart").child("Rate").setValue(hashMap);
        Toast.makeText(getActivity(),"Rate Updated",Toast.LENGTH_SHORT).show();
        Fragment someFragment = new ButtonFragment();
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, someFragment );
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
