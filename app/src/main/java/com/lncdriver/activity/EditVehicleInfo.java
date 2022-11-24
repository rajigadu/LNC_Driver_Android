package com.lncdriver.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lncdriver.R;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.JsonPost;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Settings;
import com.lncdriver.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by narayana on 5/21/2018.
 */

public class EditVehicleInfo extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.vtype)
    EditText vehi_type;

    @BindView(R.id.vmodel)
    EditText vehi_model;

    @BindView(R.id.myear)
    EditText vehi_myear;

    @BindView(R.id.vbrowse)
    Button vehi_browse;

    @BindView(R.id.vimage)
    ImageView img_vehi;

    @BindView(R.id.pprofimg)
    ImageView img_personalproof;

    @BindView(R.id.add1)
    ImageView img_addpproof;

    @BindView(R.id.lprofimg)
    ImageView img_licenceproof;

    @BindView(R.id.add2)
    ImageView img_addlproof;

    @BindView(R.id.img_antract)
    ImageView img_abtract;

    @BindView(R.id.add3)
    ImageView img_addAbtract;

    @BindView(R.id.submit)
    Button submit;

    public static final int RequestCodeCam = 1;
    public static final int RequestCodeLib = 2;
    public static final int MY_REQUEST_CODE = 3;
    public static Context mcontext;
    public int pid_picker = 0;
    HashMap<String, Object> map;
    private String userChoosenTask;
    public static EditVehicleInfo Instance;

    public static Bitmap thumbnail_one = null, thumbnail_two = null, thumbnail_three = null, thumbnail_four = null;
    public static Uri uri_one, uri_two, uri_three, uri_four;

    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editvehicleinfo);
        ButterKnife.bind(this);

        thumbnail_one = null;
        thumbnail_two = null;
        thumbnail_three = null;
        thumbnail_four = null;

        Instance = this;
        mcontext = this;

        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        vehi_browse.setOnClickListener(this);
        img_addpproof.setOnClickListener(this);
        img_addlproof.setOnClickListener(this);
        img_addAbtract.setOnClickListener(this);

        getVehicleInfoRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                submit();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.vbrowse:
                pickProfileImage();
                pid_picker = 1;
                break;
            case R.id.add1:
                pickProfileImage();
                pid_picker = 2;
                break;
            case R.id.add2:
                pickProfileImage();
                pid_picker = 3;
                break;
            case R.id.add3:
                pickProfileImage();
                pid_picker = 4;
                break;
        }
    }

    public void pickProfileImage() {
        LayoutInflater inflater = LayoutInflater.from(EditVehicleInfo.this);
        final View dialogLayout = inflater.inflate(R.layout.activity_imagepicker, null);
        final AlertDialog builder = new AlertDialog.Builder(EditVehicleInfo.this).create();
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setView(dialogLayout);
        builder.show();

        final TextView Choosegallary = (TextView) dialogLayout.findViewById(R.id.gallary);
        final TextView Takephoto = (TextView) dialogLayout.findViewById(R.id.takephoto);
        final TextView Deletephoto = (TextView) dialogLayout.findViewById(R.id.cancel);

       /* dialogLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                YoYo.with(Techniques.ZoomIn).duration(500).playOn(dialogLayout);
            }
        });*/

        Takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // boolean result = Utility.checkPermission(EditProfile.this);
                builder.dismiss();
                userChoosenTask = Takephoto.getText().toString();

                if (!hasPermissions(EditVehicleInfo.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(EditVehicleInfo.this, PERMISSIONS, MY_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, RequestCodeCam);
                }

                /*if (result)
                {
                    if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(EditProfile.this,new String[]{Manifest.permission.CAMERA},MY_REQUEST_CODE);
                    }
                    else
                    {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,RequestCodeCam);
                    }
                }*/
            }
        });
        Choosegallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                userChoosenTask = Choosegallary.getText().toString();

                if (!hasPermissions(EditVehicleInfo.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(EditVehicleInfo.this, PERMISSIONS, MY_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RequestCodeLib);
                }
            }
        });

        Deletephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // profileImageDelete();
                builder.dismiss();
            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take a Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, RequestCodeCam);
                    } else if (userChoosenTask.equals("Choose Gallary")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, RequestCodeLib);
                    }
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodeCam:
                    try {
                        if (pid_picker == 1) {
                            thumbnail_one = (Bitmap) data.getExtras().get("data");
                            uri_one = saveImageBitmap(thumbnail_one, "img_vehi");
                            String picturePath = getRealPathFromURI(uri_one);
                            img_vehi.setImageBitmap(thumbnail_one);
                            uri_one = Uri.fromFile(new File(compressImage(picturePath)));
                        } else if (pid_picker == 2) {
                            thumbnail_two = (Bitmap) data.getExtras().get("data");
                            uri_two = saveImageBitmap(thumbnail_two, "img_personalproof");
                            String picturePath = getRealPathFromURI(uri_two);
                            img_personalproof.setImageBitmap(thumbnail_two);
                            uri_two = Uri.fromFile(new File(compressImage(picturePath)));
                        } else if (pid_picker == 3) {
                            thumbnail_three = (Bitmap) data.getExtras().get("data");
                            uri_three = saveImageBitmap(thumbnail_three, "img_licenceproof");
                            String picturePath = getRealPathFromURI(uri_three);
                            img_licenceproof.setImageBitmap(thumbnail_three);
                            uri_three = Uri.fromFile(new File(compressImage(picturePath)));
                        } else if (pid_picker == 4) {
                            thumbnail_four = (Bitmap) data.getExtras().get("data");
                            uri_four = saveImageBitmap(thumbnail_four, "img_abtract");
                            String picturePath = getRealPathFromURI(uri_four);
                            img_abtract.setImageBitmap(thumbnail_four);
                            uri_four = Uri.fromFile(new File(compressImage(picturePath)));
                        }
                    } catch (Exception e) {
                        Log.e("error123", e.getMessage());
                    }
                    break;
                case RequestCodeLib:
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = EditVehicleInfo.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();

                    try {
                        if (pid_picker == 1) {
                            thumbnail_one = (BitmapFactory.decodeFile(picturePath));
                            //   Log.logError("gallery.***********692." + thumbnail,picturePath);
                            // uri = Uri.fromFile(new File(picturePath));
                            uri_one = Uri.fromFile(new File(compressImage(picturePath)));
                            img_vehi.setImageBitmap(thumbnail_one);
                        } else if (pid_picker == 2) {
                            thumbnail_two = (BitmapFactory.decodeFile(picturePath));
                            //   Log.logError("gallery.***********692." + thumbnail,picturePath);
                            // uri = Uri.fromFile(new File(picturePath));
                            uri_two = Uri.fromFile(new File(compressImage(picturePath)));
                            img_personalproof.setImageBitmap(thumbnail_two);
                        } else if (pid_picker == 3) {
                            thumbnail_three = (BitmapFactory.decodeFile(picturePath));
                            //   Log.logError("gallery.***********692." + thumbnail,picturePath);
                            // uri = Uri.fromFile(new File(picturePath));
                            uri_three = Uri.fromFile(new File(compressImage(picturePath)));
                            img_licenceproof.setImageBitmap(thumbnail_three);
                        } else if (pid_picker == 4) {
                            thumbnail_four = (BitmapFactory.decodeFile(picturePath));
                            //   Log.logError("gallery.***********692." + thumbnail,picturePath);
                            // uri = Uri.fromFile(new File(picturePath));
                            uri_four = Uri.fromFile(new File(compressImage(picturePath)));
                            img_abtract.setImageBitmap(thumbnail_four);
                        }
                    } catch (Exception e) {
                        Log.e("gallery***********692.", "Exception==========Exception==============Exception" + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public static Uri saveImageBitmap(Bitmap bitmap, String name) {
        String strDirectoy = mcontext.getFilesDir().getAbsolutePath();
        //   Utils.logError(Tag+"4149", ""+strDirectoy);

        OutputStream fOut = null;
        File file = new File(strDirectoy, name);
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            MediaStore.Images.Media.insertImage(mcontext.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }


    public String compressImage(String imageUri) {
        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        options.inJustDecodeBounds = false;

        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {

            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);


            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public void submit() {
        if (img_vehi.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.profile).getConstantState()) {
            thumbnail_one = ((BitmapDrawable) img_vehi.getDrawable()).getBitmap();
            uri_one = saveImageBitmap(thumbnail_one, "img_vehi");
        }

        if (img_personalproof.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.empty).getConstantState()) {
            thumbnail_two = ((BitmapDrawable) img_personalproof.getDrawable()).getBitmap();
            uri_two = saveImageBitmap(thumbnail_two, "img_personalproof");
        }

        if (img_licenceproof.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.empty).getConstantState()) {
            thumbnail_three = ((BitmapDrawable) img_licenceproof.getDrawable()).getBitmap();
            uri_three = saveImageBitmap(thumbnail_three, "img_licenceproof");
        }

        if (img_abtract.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.empty).getConstantState()) {
            thumbnail_four = ((BitmapDrawable) img_abtract.getDrawable()).getBitmap();
            uri_four = saveImageBitmap(thumbnail_four, "img_abtract");
        }

        new Utils(EditVehicleInfo.this);

        if (vehi_type.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please provide vehicle type", EditVehicleInfo.this);
        } else if (vehi_model.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please provide vehicle model", EditVehicleInfo.this);
        } else if (vehi_myear.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please provide vehicle making year", EditVehicleInfo.this);
        } else if (thumbnail_one == null) {
            Utils.toastTxt("Please upload Vehicle Image.", EditVehicleInfo.this);
        } else if (thumbnail_two == null) {
            Utils.toastTxt("Please provide your personal proof.", EditVehicleInfo.this);
        } else if (thumbnail_three == null) {
            Utils.toastTxt("Please provide your licence proof.", EditVehicleInfo.this);
        } else if (thumbnail_four == null) {
            Utils.toastTxt("Please provide your abstract copy.", EditVehicleInfo.this);
        } else {
            map = new HashMap<>();
            map.put("vtype", vehi_type.getText().toString());
            map.put("vmodel", vehi_model.getText().toString());
            map.put("makingyear", vehi_myear.getText().toString());
            map.put("vehicle_image", thumbnail_one);
            map.put("documents_image", thumbnail_two);
            map.put("license_image", thumbnail_three);
            map.put("driver_abstract", thumbnail_four);
            map.put("uri_two", uri_two);
            map.put("uri_three", uri_three);
            map.put("uri_four", uri_four);
            OnlineRequest.editVehicleInfoRequest(EditVehicleInfo.this, uri_one, map);
        }
    }

    public static void getVehicleInfoRequest() {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String id = pref1.getUserId();

        new Utils(mcontext);
        Utils.global.mapMain();
        Utils.global.mapMain.put(ConstVariable.DRIVER_ID, id);
        Utils.global.mapMain.put(ConstVariable.URL, com.lncdriver.utils.Settings.URL_GETVEHICLEINFO);

        if (Utils.isNetworkAvailable(mcontext)) {
            JsonPost.getNetworkResponse(mcontext, null, Utils.global.mapMain, ConstVariable.GetVehicleInfo);
        } else {
            Utils.showInternetErrorMessage(mcontext);
        }
    }

    public void populateVehicleDetails(HashMap<String, Object> map) {
        if (map != null) {
            if (map.containsKey("vehicle_type") && !map.get("vehicle_type").toString().equalsIgnoreCase("") && !map.get("vehicle_type").toString().equalsIgnoreCase("null")) {
                vehi_type.setText(map.get("vehicle_type").toString());
            }

            if (map.containsKey("vehicle_model") && !map.get("vehicle_model").toString().equalsIgnoreCase("") && !map.get("vehicle_model").toString().equalsIgnoreCase("null")) {
                vehi_model.setText(map.get("vehicle_model").toString());
            }

            if (map.containsKey("vehile_making_year") && !map.get("vehile_making_year").toString().equalsIgnoreCase("") && !map.get("vehile_making_year").toString().equalsIgnoreCase("null")) {
                vehi_myear.setText(map.get("vehile_making_year").toString());
            }

            if (map.containsKey("vehicle_image") && !map.get("vehicle_image").toString().equalsIgnoreCase("") && !map.get("vehicle_image").toString().equalsIgnoreCase("null")) {
                Picasso.with(EditVehicleInfo.this).load(Settings.URLIMAGEBASE + map.get("vehicle_image").toString()).error(R.drawable.appicon).into(img_vehi);
            }

            if (map.containsKey("document_image") && !map.get("document_image").toString().equalsIgnoreCase("") && !map.get("document_image").toString().equalsIgnoreCase("null")) {
                Picasso.with(EditVehicleInfo.this).load(Settings.URLIMAGEBASE + map.get("document_image").toString()).error(R.drawable.appicon).into(img_personalproof);
            }

            if (map.containsKey("license_image") && !map.get("license_image").toString().equalsIgnoreCase("") && !map.get("license_image").toString().equalsIgnoreCase("null")) {
                Picasso.with(EditVehicleInfo.this).load(Settings.URLIMAGEBASE + map.get("license_image").toString()).error(R.drawable.appicon).into(img_licenceproof);
            }
            if (map.containsKey("abstract_image") && !map.get("abstract_image").toString().equalsIgnoreCase("") && !map.get("abstract_image").toString().equalsIgnoreCase("null")) {
                Picasso.with(EditVehicleInfo.this).load(Settings.URLIMAGEBASE + map.get("abstract_image").toString()).error(R.drawable.appicon).into(img_abtract);
            }
        }
    }
}
