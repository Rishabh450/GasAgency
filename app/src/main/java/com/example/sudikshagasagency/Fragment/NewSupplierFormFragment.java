package com.example.sudikshagasagency.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sudikshagasagency.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NewSupplierFormFragment extends Fragment {
    EditText et,et1,et2,et3;
    Button btn;
    String name,number,area,code;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsupplierform, container, false);
        et = view.findViewById(R.id.name);
        et1 = view.findViewById(R.id.mobilenumber);
        et2 = view.findViewById(R.id.area);
        btn = view.findViewById(R.id.btn);
        et3 = view.findViewById(R.id.code);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = et.getText().toString().trim();
                number = et1.getText().toString().trim();
                area = et2.getText().toString().trim();
                code = et3.getText().toString().trim();
                if(name.isEmpty()){
                    et.setError("Please enter Name");
                    et.requestFocus();
                }
                else if(number.isEmpty()){
                    et1.setError("Please enter Mobile Number");
                    et1.requestFocus();
                }
                else if(area.isEmpty()){
                    et2.setError("Please enter Area");
                    et2.requestFocus();
                }
                else if(code.isEmpty()){
                    et3.setError("Please enter Supplier Code");
                    et3.requestFocus();
                }else{
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("name",name);
                hashMap.put("number",number);
                hashMap.put("area",area);
                hashMap.put("code",code);
                FirebaseDatabase.getInstance().getReference("Supplier").child(code).setValue(hashMap);
                    Toast.makeText(getActivity(),"Supplier Added Successfully",Toast.LENGTH_SHORT).show();
                    Fragment someFragment = new ButtonFragment();
                    assert getFragmentManager() != null;
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, someFragment );
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
    return view;}
}
