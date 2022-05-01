package com.example.sudikshagasagency.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
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
import android.widget.Toast;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class BalanceSheet extends Fragment {
    TextView tfrom, tto, getPdf;
    Calendar myCalendar;
    String from = "", to = "";
    ProgressBar cyc_progress;
    ImageView img;
    TextView tvheader;
    View v;
    TableLayout stk;
    HorizontalScrollView hv;

    int[] totalamount1 = {0};
    int[] returnedamount1 = {0};
    int[] hpcylinder1 = {0};
    int[] lpgcylinder1 = {0};
    int[] acecylinder1 = {0};
    int[] hpcylinderreturned1 = {0};
    int[] lpgcylinderreturned1 = {0};
    int[] acecylinderreturned1 = {0};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balancesheet, container, false);
        v = view;
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setDuration(300);
        scale.setInterpolator(new OvershootInterpolator());
        view.setAnimation(scale);
        HomeActivity.currentFragment = "BalanceFragment";
        stk = view.findViewById(R.id.table_main);
        hv = view.findViewById(R.id.hscrll1);
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Log.e(TAG, "setxml: peremission prob");
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);


        }
        //storeFile();
        myCalendar = Calendar.getInstance();
        tfrom = view.findViewById(R.id.from);
        tto = view.findViewById(R.id.to);
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
        TextView getPdf = view.findViewById(R.id.getPdf);
        getPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenShot();
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

    private void takeScreenShot()
    {
        View u = ((Activity) getContext()).findViewById(R.id.hscrll1);

        HorizontalScrollView z = (HorizontalScrollView) ((Activity) getContext()).findViewById(R.id.hscrll1);
        int totalHeight = z.getChildAt(0).getHeight();
        int totalWidth = z.getChildAt(0).getWidth();

        Bitmap b = getBitmapFromView(u,totalHeight,totalWidth);
        getPDf(b);

    }

    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth,totalHeight , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
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

    private void fetchRecords(View view) {
        cyc_progress.setVisibility(View.VISIBLE);
        stk.removeAllViews();
        TableRow tbrow0 = new TableRow(getActivity());
        TextView tv0 = new TextView(getActivity());
        tv0.setText("Name");
        tv0.setTextColor(Color.BLACK);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setPadding(20, 20, 20, 20);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(getActivity());
        tv1.setText("HP Taken");
        tv1.setPadding(20, 20, 20, 20);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(getActivity());
        tv2.setText("HP Returned");
        tv2.setPadding(20, 20, 20, 20);
        tv2.setTextColor(Color.BLACK);
        tv2.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(getActivity());
        tv3.setText("LPG Taken");
        tv3.setPadding(20, 20, 20, 20);
        tv3.setTypeface(null, Typeface.BOLD);
        tv3.setTextColor(Color.BLACK);
        tbrow0.addView(tv3);
        TextView tv4 = new TextView(getActivity());
        tv4.setText("LPG Returned");
        tv4.setPadding(20, 20, 20, 20);
        tv4.setTextColor(Color.BLACK);
        tv4.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv4);
        TextView tv5 = new TextView(getActivity());
        tv5.setText("Acetylene Taken");
        tv5.setTextColor(Color.BLACK);
        tv5.setTypeface(null, Typeface.BOLD);
        tv5.setPadding(20, 20, 20, 20);
        tbrow0.addView(tv5);
        TextView tv6 = new TextView(getActivity());
        tv6.setText("Acetylene Returned");
        tv6.setTextColor(Color.BLACK);
        tv6.setTypeface(null, Typeface.BOLD);
        tv6.setPadding(20, 20, 20, 20);
        tbrow0.addView(tv6);
        TextView tv7 = new TextView(getActivity());
        tv7.setText("Total Amount");
        tv7.setTextColor(Color.BLACK);
        tv7.setTypeface(null, Typeface.BOLD);
        tv7.setPadding(20, 20, 20, 20);
        tbrow0.addView(tv7);
        TextView tv8 = new TextView(getActivity());
        tv8.setText("Amount Returned");
        tv8.setTextColor(Color.BLACK);
        tv8.setTypeface(null, Typeface.BOLD);
        tv8.setPadding(20, 20, 20, 20);
        tbrow0.addView(tv8);
        stk.addView(tbrow0);

        TableRow tbrow1 = new TableRow(getActivity());
        TextView t1v = new TextView(getActivity());
        t1v.setText("Total");
        t1v.setTypeface(null, Typeface.BOLD);
        t1v.setTextColor(Color.BLACK);
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(20, 20, 20, 20);
        tbrow1.addView(t1v);
        TextView t2v = new TextView(getActivity());
        t2v.setText(String.valueOf(hpcylinder1[0]));
        t2v.setTextColor(Color.BLACK);
        t2v.setGravity(Gravity.CENTER);
        t2v.setPadding(20, 20, 20, 20);
        tbrow1.addView(t2v);
        TextView t3v = new TextView(getActivity());
        t3v.setText(String.valueOf(hpcylinderreturned1[0]));
        t3v.setTextColor(Color.BLACK);
        t3v.setGravity(Gravity.CENTER);
        t3v.setPadding(20, 20, 20, 20);
        tbrow1.addView(t3v);
        TextView t4v = new TextView(getActivity());
        t4v.setText(String.valueOf(lpgcylinder1[0]));
        t4v.setGravity(Gravity.CENTER);
        t4v.setTextColor(Color.BLACK);
        t4v.setPadding(20, 20, 20, 20);
        tbrow1.addView(t4v);
        TextView t5v = new TextView(getActivity());
        t5v.setText(String.valueOf(lpgcylinderreturned1[0]));
        t5v.setGravity(Gravity.CENTER);
        t5v.setTextColor(Color.BLACK);
        t5v.setPadding(20, 20, 20, 20);
        tbrow1.addView(t5v);
        TextView t6v = new TextView(getActivity());
        t6v.setText(String.valueOf(acecylinder1[0]));
        t6v.setGravity(Gravity.CENTER);
        t6v.setTextColor(Color.BLACK);
        t6v.setPadding(20, 20, 20, 20);
        tbrow1.addView(t6v);
        TextView t7v = new TextView(getActivity());
        t7v.setText(String.valueOf(acecylinderreturned1[0]));
        t7v.setTextColor(Color.BLACK);
        t7v.setGravity(Gravity.CENTER);
        t7v.setPadding(20, 20, 20, 20);
        tbrow1.addView(t7v);
        TextView t8v = new TextView(getActivity());
        t8v.setText(String.valueOf(totalamount1[0]));
        t8v.setTextColor(Color.BLACK);
        t8v.setGravity(Gravity.CENTER);
        t8v.setPadding(20, 20, 20, 20);
        tbrow1.addView(t8v);
        TextView t9v = new TextView(getActivity());
        t9v.setText(String.valueOf(returnedamount1[0]));
        t9v.setTextColor(Color.BLACK);
        t9v.setGravity(Gravity.CENTER);
        t9v.setPadding(20, 20, 20, 20);
        tbrow1.addView(t9v);
        stk.addView(tbrow1);
        FirebaseDatabase.getInstance().getReference("Supplier").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.exists()) {
                        Supplier supplier = snapshot.getValue(Supplier.class);
                        if (supplier != null) {
                            String name = supplier.getName();
                            int[] totalamount = {0};
                            int[] returnedamount = {0};
                            int[] hpcylinder = {0};
                            int[] lpgcylinder = {0};
                            int[] acecylinder = {0};
                            int[] hpcylinderreturned = {0};
                            int[] lpgcylinderreturned = {0};
                            int[] acecylinderreturned = {0};

                            FirebaseDatabase.getInstance().getReference("Record").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                    for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()) {
                                        if (dataSnapshot1.exists()) {
                                            Record record = dataSnapshot1.getValue(Record.class);
                                            if (record != null && record.getName().equals(name)) {
                                                if (!(from.equals("")) && !(to.equals(""))) {
                                                    if ((record.getTimeStamp().substring(0, 10)).compareTo(from) >= 0 && (record.getTimeStamp().substring(0, 10)).compareTo(to) <= 0) {
                                                        totalamount[0] += record.getTotal_amount();
                                                        returnedamount[0] += record.getAmountgiven();
                                                        if (record.getCylindertype().equals("High Pressure Cylinder")) {
                                                            hpcylinder[0] += record.getTotalcylinder();
                                                            hpcylinderreturned[0] += record.getCylinderreturned();
                                                        } else if (record.getCylindertype().equals("LPG Cylinder")) {
                                                            lpgcylinder[0] += record.getTotalcylinder();
                                                            lpgcylinderreturned[0] += record.getCylinderreturned();
                                                        } else {
                                                            acecylinder[0] += record.getTotalcylinder();
                                                            acecylinderreturned[0] += record.getCylinderreturned();
                                                        }
                                                    }
                                                } else {
                                                    totalamount[0] += record.getTotal_amount();
                                                    returnedamount[0] += record.getAmountgiven();
                                                    if (record.getCylindertype().equals("High Pressure Cylinder")) {
                                                        hpcylinder[0] += record.getTotalcylinder();
                                                        hpcylinderreturned[0] += record.getCylinderreturned();
                                                    } else if (record.getCylindertype().equals("LPG Cylinder")) {
                                                        lpgcylinder[0] += record.getTotalcylinder();
                                                        lpgcylinderreturned[0] += record.getCylinderreturned();
                                                    } else {
                                                        acecylinder[0] += record.getTotalcylinder();
                                                        acecylinderreturned[0] += record.getCylinderreturned();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    hpcylinder1[0] += hpcylinder[0];
                                    hpcylinderreturned1[0] += hpcylinderreturned[0];
                                    lpgcylinder1[0] += lpgcylinder[0];
                                    lpgcylinderreturned1[0] += lpgcylinderreturned[0];
                                    acecylinder1[0] += acecylinder[0];
                                    acecylinderreturned1[0] += acecylinderreturned[0];
                                    totalamount1[0] += totalamount[0];
                                    returnedamount1[0] += returnedamount[0];
                                    stk.removeView(tbrow1);
                                    if (totalamount[0] != 0) {
                                        TableRow tbrow = new TableRow(getActivity());
                                        TextView t1v = new TextView(getActivity());
                                        t1v.setText(name);
                                        t1v.setTextColor(Color.BLACK);
                                        t1v.setGravity(Gravity.CENTER);
                                        t1v.setPadding(20, 20, 20, 20);
                                        tbrow.addView(t1v);
                                        TextView t2v = new TextView(getActivity());
                                        t2v.setText(String.valueOf(hpcylinder[0]));
                                        t2v.setTextColor(Color.BLACK);
                                        t2v.setGravity(Gravity.CENTER);
                                        t2v.setPadding(20, 20, 20, 20);
                                        tbrow.addView(t2v);
                                        TextView t3v = new TextView(getActivity());
                                        t3v.setText(String.valueOf(hpcylinderreturned[0]));
                                        t3v.setTextColor(Color.BLACK);
                                        t3v.setGravity(Gravity.CENTER);
                                        t3v.setPadding(20, 20, 20, 20);
                                        tbrow.addView(t3v);
                                        TextView t4v = new TextView(getActivity());
                                        t4v.setText(String.valueOf(lpgcylinder[0]));
                                        t4v.setGravity(Gravity.CENTER);
                                        t4v.setTextColor(Color.BLACK);
                                        t4v.setPadding(20, 20, 20, 20);
                                        tbrow.addView(t4v);
                                        TextView t5v = new TextView(getActivity());
                                        t5v.setText(String.valueOf(lpgcylinderreturned[0]));
                                        t5v.setGravity(Gravity.CENTER);
                                        t5v.setTextColor(Color.BLACK);
                                        t5v.setPadding(20, 20, 20, 20);
                                        tbrow.addView(t5v);
                                        TextView t6v = new TextView(getActivity());
                                        t6v.setText(String.valueOf(acecylinder[0]));
                                        t6v.setGravity(Gravity.CENTER);
                                        t6v.setTextColor(Color.BLACK);
                                        t6v.setPadding(20, 20, 20, 20);
                                        tbrow.addView(t6v);
                                        TextView t7v = new TextView(getActivity());
                                        t7v.setText(String.valueOf(acecylinderreturned[0]));
                                        t7v.setTextColor(Color.BLACK);
                                        t7v.setGravity(Gravity.CENTER);
                                        t7v.setPadding(20, 20, 20, 20);
                                        tbrow.addView(t7v);
                                        TextView t8v = new TextView(getActivity());
                                        t8v.setText(String.valueOf(totalamount[0]));
                                        t8v.setTextColor(Color.BLACK);
                                        t8v.setGravity(Gravity.CENTER);
                                        t8v.setPadding(20, 20, 20, 20);
                                        tbrow.addView(t8v);
                                        TextView t9v = new TextView(getActivity());
                                        t9v.setText(String.valueOf(returnedamount[0]));
                                        t9v.setTextColor(Color.BLACK);
                                        t9v.setGravity(Gravity.CENTER);
                                        t9v.setPadding(20, 20, 20, 20);
                                        tbrow.addView(t9v);
                                        stk.addView(tbrow);
                                    }
                                    updateTotal(t2v, t3v, t4v, t5v, t6v, t7v, t8v, t9v);
                                    stk.addView(tbrow1);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
                cyc_progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getPDf(Bitmap recycler_view_bm)
    {
        Log.d("pdfEnter","enter");
        Log.d("pdfEnter","screenshot taken");


        try {



            final File sdCard = Environment.getExternalStorageDirectory();



            File dir = new File(sdCard.getAbsolutePath() + "/Sudiksha" + "/Records");
            dir.mkdirs();
            long t = System.currentTimeMillis();
            String time = String.valueOf(t);

            String fileName = "records."+"pdf";
            fileName.trim();
            Log.d("pathsssss", "onPictureTaken - wrote to " + fileName);

            File outFile = new File(dir, fileName);
            Log.d("pathssss", "onPictureTaken - wrote to " + outFile.getAbsolutePath());

            FileOutputStream outStream = new FileOutputStream(outFile);
            Log.d("pathssss", "pdf - wrote to " + outFile.getAbsolutePath());
            Toast.makeText(getContext(),"Stored at Sudiksha/Records/records.pdf",Toast.LENGTH_LONG).show();

            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(recycler_view_bm.getWidth(), recycler_view_bm.getHeight(), 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            recycler_view_bm.prepareToDraw();
            Canvas c;
            c = page.getCanvas();
            c.drawBitmap(recycler_view_bm,0,0,null);
            document.finishPage(page);
            Log.d("pdfEnter","file created2");

            try {
                document.writeTo(outStream);
            } catch (IOException ioException) {
                Log.d("pdfEnter","file " + ioException.getMessage());

                ioException.printStackTrace();
            }
            document.close();
            View parentLayout = v.findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar
                    .make(v , "PDF generated successfully.", Snackbar.LENGTH_LONG)
                    .setAction("Open", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            displaypdf();
                        }
                    });

            snackbar.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    String dir="/Sudiksha/Records";

    public void displaypdf() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        File file = null;
        file = new File(Environment.getExternalStorageDirectory()+dir+ "/records.pdf");
        Toast.makeText(getContext(), file.toString() , Toast.LENGTH_LONG).show();
        if(file.exists()) {
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
        }
        else
            Toast.makeText(getContext(), "File path is incorrect." , Toast.LENGTH_LONG).show();
    }


    private void updateTotal(TextView t2v, TextView t3v, TextView t4v, TextView t5v, TextView t6v, TextView t7v, TextView t8v, TextView t9v) {
        t2v.setText(String.valueOf(hpcylinder1[0]));
        t3v.setText(String.valueOf(hpcylinderreturned1[0]));
        t4v.setText(String.valueOf(lpgcylinder1[0]));
        t5v.setText(String.valueOf(lpgcylinderreturned1[0]));
        t6v.setText(String.valueOf(acecylinder1[0]));
        t7v.setText(String.valueOf(acecylinderreturned1[0]));
        t8v.setText(String.valueOf(totalamount1[0]));
        t9v.setText(String.valueOf(returnedamount1[0]));
    }
}
