package com.example.sudikshagasagency.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sudikshagasagency.Activity.HomeActivity;
import com.example.sudikshagasagency.Adapter.RecordAdapter;
import com.example.sudikshagasagency.ModalClass.Record;
import com.example.sudikshagasagency.ModalClass.Supplier;
import com.example.sudikshagasagency.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class BalanceSheet extends Fragment {
    TextView tfrom,tto,getPdf;
    Calendar myCalendar;
    String from="",to="";
    ProgressBar cyc_progress;
    ImageView img;
    TextView tvheader;
    View v;
    TableLayout stk;
    HorizontalScrollView hv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balancesheet, container, false);
        v = view;
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setDuration(300);
        scale.setInterpolator(new OvershootInterpolator());
        view.setAnimation(scale);
        HomeActivity.currentFragment="BalanceFragment";
        stk = view.findViewById(R.id.table_main);
        hv=view.findViewById(R.id.hscrll1);
        if(ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            // Log.e(TAG, "setxml: peremission prob");
            ActivityCompat.requestPermissions((Activity) getContext(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);


        }
        //storeFile();
        myCalendar = Calendar.getInstance();
        tfrom = view.findViewById(R.id.from);
        tto  = view.findViewById(R.id.to);
        img = view.findViewById(R.id.img);
        //getPdf = view.findViewById(R.id.getPdf);
        tvheader = view.findViewById(R.id.tv_header);
        cyc_progress = view.findViewById(R.id.cyc_progress);
       fetchRecords(view);
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
                fetchRecords(view);
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
        return view;
    }
    private void updateLabel() {
        DateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");

        date1.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String time1 = date1.format(myCalendar.getTime());
        from = time1;
        tfrom.setText(from);
    }
    private void updateLabel1() {
        DateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");

        date1.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String time1 = date1.format(myCalendar.getTime());
        to = time1;
        tto.setText(to);
    }
    private void fetchRecords(View view)
    {
        cyc_progress.setVisibility(View.VISIBLE);
        stk.removeAllViews();
        TableRow tbrow0 = new TableRow(getActivity());
        TextView tv0 = new TextView(getActivity());
        tv0.setText("Name");
        tv0.setTextColor(Color.BLACK);
        tv0.setPadding(20,20,20,20);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(getActivity());
        tv1.setText("HP Taken");
        tv1.setPadding(20,20,20,20);
        tv1.setTextColor(Color.BLACK);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(getActivity());
        tv2.setText("HP Returned");
        tv2.setPadding(20,20,20,20);
        tv2.setTextColor(Color.BLACK);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(getActivity());
        tv3.setText("LPG Taken");
        tv3.setPadding(20,20,20,20);
        tv3.setTextColor(Color.BLACK);
        tbrow0.addView(tv3);
        TextView tv4 = new TextView(getActivity());
        tv4.setText("LPG Returned");
        tv4.setPadding(20,20,20,20);
        tv4.setTextColor(Color.BLACK);
        tbrow0.addView(tv4);
        TextView tv5 = new TextView(getActivity());
        tv5.setText("Acetylene Taken");
        tv5.setTextColor(Color.BLACK);
        tv5.setPadding(20,20,20,20);
        tbrow0.addView(tv5);
        TextView tv6 = new TextView(getActivity());
        tv6.setText("Acetylene Returned");
        tv6.setTextColor(Color.BLACK);
        tv6.setPadding(20,20,20,20);
        tbrow0.addView(tv6);
        TextView tv7= new TextView(getActivity());
        tv7.setText("Total Amount");
        tv7.setTextColor(Color.BLACK);
        tv7.setPadding(20,20,20,20);
        tbrow0.addView(tv7);
        TextView tv8 = new TextView(getActivity());
        tv8.setText("Amount Returned");
        tv8.setTextColor(Color.BLACK);
        tv8.setPadding(20,20,20,20);
        tbrow0.addView(tv8);
        stk.addView(tbrow0);
        FirebaseDatabase.getInstance().getReference("Supplier").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                if(snapshot.exists()){
                    Supplier supplier = snapshot.getValue(Supplier.class);
                    if(supplier!=null){
                        String name=supplier.getName();
                        final int[] totalamount = {0};
                        final int[] returnedamount = { 0 };
                        final int[] hpcylinder = { 0 };
                        final int[] lpgcylinder = { 0 };
                        final int[] acecylinder = { 0 };
                        final int[] hpcylinderreturned = { 0 };
                        final int[] lpgcylinderreturned = { 0 };
                        final int[] acecylinderreturned = { 0 };
                        FirebaseDatabase.getInstance().getReference("Record").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                for(DataSnapshot dataSnapshot1:snapshot1.getChildren()){
                                    if(dataSnapshot1.exists()){
                                        Record record = dataSnapshot1.getValue(Record.class);
                                        if(record!=null && record.getName().equals(name)) {
                                            if(!(from.equals("")) && !(to.equals("")))
                                            {
                                                if ((record.getTimeStamp().substring(0,10)).compareTo(from) >= 0 && (record.getTimeStamp().substring(0,10)).compareTo(to) <= 0) {
                                                    totalamount[0] +=record.getTotal_amount();
                                                    returnedamount[0] +=record.getAmountgiven();
                                                    if(record.getCylindertype().equals("High Pressure Cylinder")) {
                                                        hpcylinder[0] +=record.getTotalcylinder();
                                                        hpcylinderreturned[0] +=record.getCylinderreturned();
                                                    }
                                                    else if(record.getCylindertype().equals("LPG Cylinder")){
                                                        lpgcylinder[0] +=record.getTotalcylinder();
                                                        lpgcylinderreturned[0] +=record.getCylinderreturned();
                                                    }
                                                    else{
                                                        acecylinder[0] +=record.getTotalcylinder();
                                                        acecylinderreturned[0] +=record.getCylinderreturned();
                                                    }
                                                }
                                            }
                                            else{
                                                totalamount[0] +=record.getTotal_amount();
                                                returnedamount[0] +=record.getAmountgiven();
                                                if(record.getCylindertype().equals("High Pressure Cylinder")) {
                                                    hpcylinder[0] +=record.getTotalcylinder();
                                                    hpcylinderreturned[0] +=record.getCylinderreturned();
                                                }
                                                else if(record.getCylindertype().equals("LPG Cylinder")){
                                                    lpgcylinder[0] +=record.getTotalcylinder();
                                                    lpgcylinderreturned[0] +=record.getCylinderreturned();
                                                }
                                                else{
                                                    acecylinder[0] +=record.getTotalcylinder();
                                                    acecylinderreturned[0] +=record.getCylinderreturned();
                                                }
                                            }
                                        }
                                    }
                                }
                                if(totalamount[0]!=0) {
                                    TableRow tbrow = new TableRow(getActivity());
                                    TextView t1v = new TextView(getActivity());
                                    t1v.setText(name);
                                    t1v.setTextColor(Color.BLACK);
                                    t1v.setGravity(Gravity.CENTER);
                                    t1v.setPadding(20,20,20,20);
                                    tbrow.addView(t1v);
                                    TextView t2v = new TextView(getActivity());
                                    t2v.setText(String.valueOf(hpcylinder[0]));
                                    t2v.setTextColor(Color.BLACK);
                                    t2v.setGravity(Gravity.CENTER);
                                    t2v.setPadding(20,20,20,20);
                                    tbrow.addView(t2v);
                                    TextView t3v = new TextView(getActivity());
                                    t3v.setText(String.valueOf(hpcylinderreturned[0]));
                                    t3v.setTextColor(Color.BLACK);
                                    t3v.setGravity(Gravity.CENTER);
                                    t3v.setPadding(20,20,20,20);
                                    tbrow.addView(t3v);
                                    TextView t4v = new TextView(getActivity());
                                    t4v.setText(String.valueOf(lpgcylinder[0]));
                                    t4v.setGravity(Gravity.CENTER);
                                    t4v.setTextColor(Color.BLACK);
                                    t4v.setPadding(20,20,20,20);
                                    tbrow.addView(t4v);
                                    TextView t5v = new TextView(getActivity());
                                    t5v.setText(String.valueOf(lpgcylinderreturned[0]));
                                    t5v.setGravity(Gravity.CENTER);
                                    t5v.setTextColor(Color.BLACK);
                                    t5v.setPadding(20,20,20,20);
                                    tbrow.addView(t5v);
                                    TextView t6v = new TextView(getActivity());
                                    t6v.setText(String.valueOf(acecylinder[0]));
                                    t6v.setGravity(Gravity.CENTER);
                                    t6v.setTextColor(Color.BLACK);
                                    t6v.setPadding(20,20,20,20);
                                    tbrow.addView(t6v);
                                    TextView t7v = new TextView(getActivity());
                                    t7v.setText(String.valueOf(acecylinderreturned[0]));
                                    t7v.setTextColor(Color.BLACK);
                                    t7v.setGravity(Gravity.CENTER);
                                    t7v.setPadding(20,20,20,20);
                                    tbrow.addView(t7v);
                                    TextView t8v = new TextView(getActivity());
                                    t8v.setText(String.valueOf(totalamount[0]));
                                    t8v.setTextColor(Color.BLACK);
                                    t8v.setGravity(Gravity.CENTER);
                                    t8v.setPadding(20,20,20,20);
                                    tbrow.addView(t8v);
                                    TextView t9v = new TextView(getActivity());
                                    t9v.setText(String.valueOf(returnedamount[0]));
                                    t9v.setTextColor(Color.BLACK);
                                    t9v.setGravity(Gravity.CENTER);
                                    t9v.setPadding(20,20,20,20);
                                    tbrow.addView(t9v);
                                    stk.addView(tbrow);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                cyc_progress.setVisibility(View.GONE);
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
