package com.mohit.varma.apnimandi.activitites;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mohit.varma.apnimandi.MyApplication;
import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.database.MyFirebaseDatabase;
import com.mohit.varma.apnimandi.model.OrderByImage;
import com.mohit.varma.apnimandi.model.UserAddress;
import com.mohit.varma.apnimandi.utilites.Constants;
import com.mohit.varma.apnimandi.utilites.Session;
import com.mohit.varma.apnimandi.utilites.Webservice;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderByImageActivity extends AppCompatActivity {

    private Toolbar OrderByImageActivityToolbar;
    private ImageView orderImage;
    private MaterialButton TakeImageButtonFromCamera, OrderButton;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Session session;
    private ProgressDialog progressDialog;
    private MyFirebaseDatabase myFirebaseDatabase;
    private StorageReference mountainImagesRef;
    private String imageURI;
    private Bitmap bitmap;
    private Uri capturedImageUri=null;
    private String filePath = "";

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_by_image);

        context = this;

        OrderByImageActivityToolbar = (Toolbar) findViewById(R.id.OrderByImageActivityToolbar);
        orderImage = (ImageView) findViewById(R.id.ImageOfList);
        TakeImageButtonFromCamera = (MaterialButton) findViewById(R.id.TakeImageButtonFromCamera);
        OrderButton = (MaterialButton) findViewById(R.id.OrderButton);
        firebaseAuth = MyApplication.getFirebaseAuth();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl(Webservice.FIREBASE_ROOT_REFERENCE);
        session = new Session(context);
        myFirebaseDatabase = new MyFirebaseDatabase();
        progressDialog = new ProgressDialog(context);

        OrderByImageActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "onClick: ");
                onBackPressed();
            }
        });


        setSupportActionBar(OrderByImageActivityToolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TakeImageButtonFromCamera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!hasPermissions(context, PERMISSIONS)) {
                    ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    createImageFileAndSave();
//                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, 1);
                }
            }
        });

        OrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            bitmap = photo;
//            orderImage.setImageBitmap(photo);
//        }

        if (requestCode == 1) {
            //Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap photo = BitmapFactory.decodeFile(filePath);
                bitmap = photo;
                orderImage.setImageBitmap(photo);
        }
    }

    public void placeOrder() {
        if (firebaseAuth != null) {
            if (firebaseAuth.getCurrentUser() != null) {
                if (!firebaseAuth.getCurrentUser().isAnonymous()) {
                    if(bitmap != null){
                        uploadFile(bitmap);
                    }
                    Toast.makeText(this, "Hello" + firebaseAuth.getCurrentUser().getPhoneNumber(), Toast.LENGTH_SHORT).show();
                    UserAddress userAddress = session.getAddress();
                } else {
                    Toast.makeText(OrderByImageActivity.this, Constants.getStringFromID(context, R.string.anonymous_welcome_msg), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OrderByImageActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                }
            }
        }
    }


    private void uploadFile(Bitmap bitmap) {
        showProgressDialog();
            mountainImagesRef = myFirebaseDatabase.getStorageReference().child("orderedByImage" + "/" + new Timestamp(System.currentTimeMillis()).getTime() + ".jpg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                dismissProgressDialog();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return mountainImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            if (downUri != null) {
                                imageURI = downUri.toString();
                                if (imageURI != null && !imageURI.isEmpty()) {
                                    addItemToDatabase(imageURI);
                                }
                            }
                        }
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage("Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    public void addItemToDatabase(String imageURI) {
        OrderByImage orderByImage = new OrderByImage();
        orderByImage.setImageURL(imageURI);
        orderByImage.setPhoneNumber(firebaseAuth.getCurrentUser().getPhoneNumber());
            myFirebaseDatabase.getReference().child("OrderByImage").child(firebaseAuth.getCurrentUser().getPhoneNumber())
                    .push()
                    .setValue(orderByImage).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dismissProgressDialog();
                    onBackPressed();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("OrderByImage", "onFailure: ");
                }
            });
        }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createImageFileAndSave() {
        try {

            Intent sceneViewerIntent = new Intent(Intent.ACTION_VIEW);
            Uri intentUri =
                    Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                            .appendQueryParameter("file", "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Avocado/glTF/Avocado.gltf")
                            .appendQueryParameter("mode", "ar_only")
                            .build();
            sceneViewerIntent.setData(intentUri);
            sceneViewerIntent.setPackage("com.google.ar.core");
            startActivity(sceneViewerIntent);

//            File file = createImageFile();
//            this.capturedImageUri = FileProvider.getUriForFile(context, "com.mohit.varma.apnimandi",file);
//            Log.d("Simple", "createImageFileAndSave: " + capturedImageUri.getAuthority());
//            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            i.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
//            startActivityForResult(i, 1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/DimeStore/Ordered/");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        filePath = image.getAbsolutePath();
        return image;
    }
}