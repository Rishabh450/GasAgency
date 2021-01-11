package com.example.sudikshagasagency.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sudikshagasagency.Activity.HomeActivity;
import com.example.sudikshagasagency.BuildConfig;
import com.example.sudikshagasagency.ModalClass.Record;
import com.example.sudikshagasagency.R;
import com.example.sudikshagasagency.Adapter.RecordAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class RecordFragment extends Fragment {
    List<Record> list =  new ArrayList<>();
    RecyclerView recyclerView;
    RecordAdapter adapter;
    Context ctx;
    TextView tfrom,tto,getPdf;
    Calendar myCalendar;
    String from="",to="";
    ProgressBar cyc_progress;
    ImageView img;
    TextView tvheader;
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        v = view;
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setDuration(300);
        scale.setInterpolator(new OvershootInterpolator());
        view.setAnimation(scale);
        HomeActivity.currentFragment="RecordFragment";
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
        getPdf = view.findViewById(R.id.getPdf);
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



    private void getPDf(View view)
    {
        Log.d("pdfEnter","enter");
        Bitmap recycler_view_bm =     getScreenshotFromRecyclerView(recyclerView);
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
            View parentLayout = view.findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar
                    .make(view , "PDF generated successfully.", Snackbar.LENGTH_LONG)
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

  
    public Bitmap getScreenshotFromRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }

                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(Color.WHITE);

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }

        }
        return bigBitmap;
    }



    private void fetchRecords(View view)
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
                                    if ((record.getTimeStamp().substring(0,10)).compareTo(from) >= 0 && (record.getTimeStamp().substring(0,10)).compareTo(to) <= 0) {
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
                    getPdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                            {
                                // Log.e(TAG, "setxml: peremission prob");
                                ActivityCompat.requestPermissions((Activity) getContext(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);


                            }
                            else
                            getPDf(v);
                        }
                    });
                    cyc_progress.setVisibility(View.GONE);
                    if(list.isEmpty())
                    {
                        img.setVisibility(View.VISIBLE);
                        tvheader.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        img.setVisibility(View.GONE);
                        tvheader.setVisibility(View.GONE);
                    }

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
