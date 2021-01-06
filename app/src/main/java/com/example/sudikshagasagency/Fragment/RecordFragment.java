package com.example.sudikshagasagency.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sudikshagasagency.Activity.HomeActivity;
import com.example.sudikshagasagency.ModalClass.Record;
import com.example.sudikshagasagency.R;
import com.example.sudikshagasagency.Adapter.RecordAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RecordFragment extends Fragment {
    List<Record> list;
    RecyclerView recyclerView;
    RecordAdapter adapter;
    Context ctx;
    TextView t,t1;
    Calendar myCalendar;
    String from="",to="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        HomeActivity.currentFragment="RecordFragment";
         myCalendar = Calendar.getInstance();
        t = view.findViewById(R.id.from);
        t1 = view.findViewById(R.id.to);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }
        };
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ctx = getContext();
        if(!(from.equals("")) && !(to.equals(""))){
        FirebaseDatabase.getInstance().getReference("Record").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(dataSnapshot.exists()){
                        Record record = dataSnapshot.getValue(Record.class);
                        if(record!=null)
                            if(record.getDate().compareTo(from)>=0 && record.getDate().compareTo(to)<=0) {
                                list.add(record);
                            }
                    }
                }
                adapter = new RecordAdapter(list,ctx);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}
    return view;}
    private void updateLabel() {
        String myFormat = "yyyy MMM dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        t.setText(sdf.format(myCalendar.getTime()));
        from = t.getText().toString();
    }
    private void updateLabel1() {
        String myFormat = "yyyy MMM dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        t1.setText(sdf.format(myCalendar.getTime()));
        to = t1.getText().toString();
    }
}
