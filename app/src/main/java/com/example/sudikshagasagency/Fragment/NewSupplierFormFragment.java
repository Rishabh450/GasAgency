package com.example.sudikshagasagency.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sudikshagasagency.Activity.HomeActivity;
import com.example.sudikshagasagency.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class NewSupplierFormFragment extends Fragment {
    EditText et,et1,et2,et3;
    Button btn;
    String name,number,area,code;
    Uri pickedImage;
    ImageView civProfile ;
     long ts;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsupplierform, container, false);
        HomeActivity.currentFragment="NewFragment";
        et = view.findViewById(R.id.Lpgcylinder);
        et1 = view.findViewById(R.id.Hpcylinder);
        et2 = view.findViewById(R.id.ACylinder);
        btn = view.findViewById(R.id.btn);
        et3 = view.findViewById(R.id.code);
        civProfile = view.findViewById(R.id.civProfile);
        civProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    // Log.e(TAG, "setxml: peremission prob");
                    ActivityCompat.requestPermissions((Activity) getContext(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);


                }
                else
                    addImage();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = et.getText().toString().trim();
                number = et1.getText().toString().trim();
                area = et2.getText().toString().trim();
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
              else{
               ts = (long) System.currentTimeMillis();

                    code = String.valueOf(ts);
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("name",name);
                hashMap.put("number",number);
                hashMap.put("area",area);
                hashMap.put("code",code);
                if(pickedImage==null)
                {
                    hashMap.put("picture","");
                    FirebaseDatabase.getInstance().getReference("Supplier").child(code).setValue(hashMap);
                    Toast.makeText(getActivity(),"Supplier Added Successfully",Toast.LENGTH_SHORT).show();
                    Fragment someFragment = new ButtonFragment();
                    assert getFragmentManager() != null;
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, someFragment );
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else
                {
                    uploadImage();
                }

                }
            }
        });
    return view;}


    public void addImage()
    {

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");

        startActivityForResult(chooserIntent, 1);

    }

    private void uploadImage() {


        if (pickedImage != null) {
            final FirebaseDatabase[] database1 = {FirebaseDatabase.getInstance()};
            final DatabaseReference databaseReference1 = database1[0].getReference();
            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("DelImage/").child(String.valueOf(ts));
            final UploadTask uploadTask;
            Bitmap bitmap = null;
            try {


                bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), pickedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            byte[] dat = baos.toByteArray();
            Log.d("kamwa kiya", "true");
            uploadTask = filePath.putBytes(dat);

            // uploadTask = filePath.putFile(Uri.parse(selectedImage));
            Log.d("sender", String.valueOf(pickedImage));
            final ProgressDialog mProgress = new ProgressDialog(getContext());
            mProgress.setTitle("Uploading...");


            mProgress.setCancelable(true);

            mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    uploadTask.cancel();
                }
            });

            mProgress.show();

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mProgress.dismiss();
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("name",name);
                            hashMap.put("number",number);
                            hashMap.put("area",area);
                            hashMap.put("code",code);
                            hashMap.put("picture",String.valueOf(uri));

                                FirebaseDatabase.getInstance().getReference("Supplier").child(code).setValue(hashMap);
                                Toast.makeText(getActivity(),"Supplier Added Successfully",Toast.LENGTH_SHORT).show();
                                Fragment someFragment = new ButtonFragment();
                                assert getFragmentManager() != null;
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_container, someFragment );
                                transaction.addToBackStack(null);
                                transaction.commit();

                            Log.d("url", String.valueOf(uri));

                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    mProgress.setMessage("Uploaded: " + (int) progress + "%");
                    mProgress.setProgress((int) progress);

                }
            });


        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK&&requestCode==1) {


            pickedImage = data.getData();

            Log.d("successhuacrop","addhua"+pickedImage);
            civProfile.setImageURI(pickedImage);


        }
    }
}
