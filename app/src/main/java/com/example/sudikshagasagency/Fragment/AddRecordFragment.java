package com.example.sudikshagasagency.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sudikshagasagency.Activity.HomeActivity;
import com.example.sudikshagasagency.ModalClass.Record;
import com.example.sudikshagasagency.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class AddRecordFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner spinnerName,spinnerCylinderName;
    EditText numberofCylinder;
    Button btn;
    String cylinder[]={"Select Cylinder Type","High Pressure Cylinder","Acetylene Cylinder","LPG Cylinder"};
   ArrayList<String> name;
   String deliveryBoy="",gas="",number,rate="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addrecord, container, false);HomeActivity.currentFragment = "AddRecordFragment";
    spinnerName = view.findViewById(R.id.spinnerName);
    spinnerCylinderName = view.findViewById(R.id.spinnerCylinderName);
    numberofCylinder = view.findViewById(R.id.numberofCylinders);
    name = new ArrayList<>();
    name.add("Select Delivery Boy");
    btn = view.findViewById(R.id.btn);
        FirebaseDatabase.getInstance().getReference("Supplier").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot napshot : snapshot.getChildren()){
                    String nam = (String) napshot.child("name").getValue();
                    name.add(nam);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, name);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerName.setAdapter(adapter);
        spinnerName.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) getContext());
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, cylinder);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCylinderName.setAdapter(adapter1);
        spinnerCylinderName.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) getContext());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = numberofCylinder.getText().toString().trim();
                if(deliveryBoy.equals("Select Delivery Boy") ){
                    spinnerName.requestFocus();
                    Toast.makeText(getActivity(),"Please select name of delivery boy",Toast.LENGTH_SHORT).show();
                }
                else if(gas.equals("Select Cylinder Type")){
                    spinnerCylinderName.requestFocus();
                    Toast.makeText(getActivity(),"Please select type of cylinder",Toast.LENGTH_SHORT).show();
                }
                else if(number.isEmpty()){
                    numberofCylinder.setError("Please enter number of cylinder");
                    numberofCylinder.requestFocus();
                }
                else{
                    submit(deliveryBoy,gas,number);
                }
            }
        });
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == R.id.spinnerName)
            deliveryBoy = parent.getItemAtPosition(position).toString();

        if(parent.getId() == R.id.spinnerCylinderName)
            gas = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void submit(String deliveryBoy,String gas,String number) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("yyyy MMM dd hh:mm a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String time = date.format(currentLocalTime);
        FirebaseDatabase.getInstance().getReference("Rate Chart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (gas.equals("High Pressure Cylinder")) {
                        rate = (String) snapshot.child("Hp").getValue();
                    } else if (gas.equals("Acetylene Cylinder")) {
                        rate = (String) snapshot.child("Acetylene").getValue();
                    } else if (gas.equals("LPG Cylinder")) {
                        rate = (String) snapshot.child("LPG").getValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DateFormat df = new SimpleDateFormat("yyyy MMM dd");
        df.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String currentdate = df.format(currentLocalTime);
        Record r = new Record(currentdate, rate, "N.A", gas, deliveryBoy, number, "N.A", time);
        FirebaseDatabase.getInstance().getReference("Record").child(time).setValue(r);
        Toast.makeText(getActivity(), "Record Added Successfully", Toast.LENGTH_SHORT).show();
        Fragment someFragment = new ButtonFragment();
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
