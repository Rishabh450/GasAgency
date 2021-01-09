package com.example.sudikshagasagency.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.DatePicker;
import android.widget.ProgressBar;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class RecordFragment extends Fragment {
    List<Record> list =  new ArrayList<>();
    RecyclerView recyclerView;
    RecordAdapter adapter;
    Context ctx;
    TextView tfrom,tto;
    Calendar myCalendar;
    String from="",to="";
    ProgressBar cyc_progress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setDuration(300);
        scale.setInterpolator(new OvershootInterpolator());
        view.setAnimation(scale);
        HomeActivity.currentFragment="RecordFragment";
         myCalendar = Calendar.getInstance();
        tfrom = view.findViewById(R.id.from);
        tto  = view.findViewById(R.id.to);
        cyc_progress = view.findViewById(R.id.cyc_progress);
        fetchRecords();
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
        TextView apply = view.findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchRecords();
            }
        });
        tfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        tto.setOnClickListener(new View.OnClickListener() {
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

    return view;}
    private void updateLabel() {
        DateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");

        date1.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String time1 = date1.format(myCalendar.getTime());
        from = time1;
        tfrom.setText(from);
    }

    private void fetchRecords()
    {

        cyc_progress.setVisibility(View.VISIBLE);
            list.clear();
            FirebaseDatabase.getInstance().getReference("Record").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        if(dataSnapshot.exists()){
                            Record record = dataSnapshot.getValue(Record.class);
                            if(record!=null) {
                                if(!(from.equals("")) && !(to.equals("")))
                                {
                                    if (record.getTimeStamp().compareTo(from) >= 0 && record.getTimeStamp().compareTo(to) <= 0) {
                                        list.add(record);
                                    }
                                }
                                else
                                    list.add(record);

                            }
                        }
                    }
                    adapter = new RecordAdapter(list,ctx);
                    recyclerView.setAdapter(adapter);
                    cyc_progress.setVisibility(View.GONE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
    private void updateLabel1() {
        DateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");

        date1.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String time1 = date1.format(myCalendar.getTime());
        to = time1;
        tto.setText(to);
    }
}
