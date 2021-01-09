package com.example.sudikshagasagency.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class AddRecordFragment extends Fragment{
    Spinner spinnerName,spinnerCylinderName;
    EditText numberofCylinder;
    Button btn;
    String cylinder[]={"Select Cylinder Type","High Pressure Cylinder","Acetylene Cylinder","LPG Cylinder"};
    int cylinderPrice[]={0,0,0};
    String picture = "";
   ArrayList<String> name;
    ArrayList<String> urls = new ArrayList<>();
    ArrayList<Long> code = new ArrayList<>();
    long delcode;
   String deliveryBoy="";
    String gas="";
    String number;
    TextView amt_return;
    int rate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addrecord, container, false);HomeActivity.currentFragment = "AddRecordFragment";
    spinnerName = view.findViewById(R.id.spinnerName);
    spinnerCylinderName = view.findViewById(R.id.spinnerCylinderName);
    amt_return= view.findViewById(R.id.amt_return);

    numberofCylinder = view.findViewById(R.id.numberofCylinders);
    name = new ArrayList<>();
    name.add("Select Delivery Boy");
    btn = view.findViewById(R.id.btn);
    getCylinderData();
    setCountListener();
    getDelBoys();

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


    public void submit(String deliveryBoy,String gas,String number) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("yyyy MMM dd hh:mm a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String time = date.format(currentLocalTime);
        DateFormat df = new SimpleDateFormat("yyyy MMM dd");
        df.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String currentdate = df.format(currentLocalTime);
        if (gas.equals("High Pressure Cylinder")) {
            rate = cylinderPrice[0];
        } else if (gas.equals("Acetylene Cylinder")) {
            rate =  cylinderPrice[1];
        } else if (gas.equals("LPG Cylinder")) {
            rate =  cylinderPrice[2];
        }
        DateFormat date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date1.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String time1 = date1.format(currentLocalTime);
        Record r = new Record(currentdate, rate, 0, gas, deliveryBoy, Integer.parseInt(number) , 0, time,delcode,(rate* Integer.parseInt(number)),picture,time1);
        FirebaseDatabase.getInstance().getReference("Record").child(time1).setValue(r);
        Toast.makeText(getActivity(), "Record Added Successfully", Toast.LENGTH_SHORT).show();
        Fragment someFragment = new ButtonFragment();
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void getDelBoys()
    {
        FirebaseDatabase.getInstance().getReference("Supplier").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot napshot : snapshot.getChildren()){
                    String nam = (String) napshot.child("name").getValue();
                    name.add(nam);
                    long x = Long.parseLong(napshot.child("code").getValue().toString());
                    code.add(x );
                    Log.d("picurl"," p");
                    String pic =napshot.child("picture").getValue().toString();

                    urls.add(pic);

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, name);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerName.setAdapter(adapter);
                spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d("spinnerselection",adapterView.getItemAtPosition(i).toString() + i);
                        deliveryBoy = adapterView.getItemAtPosition(i).toString();
                        if(i>0) {
                            delcode = code.get(i - 1);
                            picture = urls.get(i-1);
                        }
                        else {
                            delcode = 0;
                            picture = "";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, cylinder);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCylinderName.setAdapter(adapter1);
                spinnerCylinderName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d("spinnerselection",adapterView.getItemAtPosition(i).toString() + i);
                        gas = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setCountListener()
    {
        numberofCylinder.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!charSequence.toString().trim().isEmpty())
                {
                    Log.d("cylinder",charSequence.toString());
                    if (gas.equals("High Pressure Cylinder")) {
                        rate = cylinderPrice[0];
                    } else if (gas.equals("Acetylene Cylinder")) {
                        rate =  cylinderPrice[1];
                    } else if (gas.equals("LPG Cylinder")) {
                        rate =  cylinderPrice[2];
                    }
                    if(rate!=0)
                    {
                        int amt = Integer.parseInt(charSequence.toString())*rate;
                        amt_return.setText("Total Amount to be returned : " + getString(R.string.rupee_symbol) + amt);
                    }

                }



            }




            @Override
            public void afterTextChanged(Editable editable) {



            }
        });
    }

    private void getCylinderData()
    {
        FirebaseDatabase.getInstance().getReference("Rate Chart").child("Rate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cylinderPrice[0]= Integer.parseInt(snapshot.child("Hp").getValue().toString());
                cylinderPrice[1]= Integer.parseInt(snapshot.child("Acetylene").getValue().toString());
                cylinderPrice[2]= Integer.parseInt(snapshot.child("LPG").getValue().toString());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
