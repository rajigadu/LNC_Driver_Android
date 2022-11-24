package com.lncdriver.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lncdriver.R;
import com.lncdriver.model.SavePref;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Settings;
import com.lncdriver.utils.Utils;
import com.makeramen.roundedimageview.RoundedImageView;
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

public class EditPersonalInfo extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EditPersonalInfo";
    @BindView(R.id.pimg)
    ImageView img_profile;

    @BindView(R.id.pimage_add)
    ImageView img_addprofile;

    @BindView(R.id.fname)
    EditText fname;

    @BindView(R.id.lname)
    EditText lname;

    @BindView(R.id.mobile)
    EditText mobile;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.submit)
    Button submit;


    String picturePath = "";

    public static final int RequestCodeCam = 1;
    public static final int RequestCodeLib = 2;
    public static final int MY_REQUEST_CODE = 3;
    public static Bitmap thumbnail = null;
    public static Uri uri;
    public static Context mcontext;
    private String userChoosenTask;
    HashMap<String, Object> map;

    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpersonalinfo);
        ButterKnife.bind(this);

        mcontext = this;

        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        img_addprofile.setOnClickListener(this);

        SavePref pref1 = new SavePref();
        pref1.SavePref(EditPersonalInfo.this);
        String fnam = pref1.getUserFName();
        String llname = pref1.getUserLName();
        String unum = pref1.getMobile();
        final String imag = pref1.getImage();

        if (!imag.toString().equalsIgnoreCase("")) {
            //Picasso.with(EditProfile.this).load(imag).into(pic);
            Picasso.with(EditPersonalInfo.this).load(Settings.URLIMAGEBASE + imag).placeholder(R.drawable.appicon).into(img_profile);
        }

        if (!fnam.toString().equalsIgnoreCase("")) {
            fname.setText(fnam);
        }
        if (!unum.toString().equalsIgnoreCase("")) {
            mobile.setText(unum);
        }
        if (!llname.toString().equalsIgnoreCase("")) {
            lname.setText(llname);
        }


        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "imagAAAA "+picturePath);
                if(picturePath.equalsIgnoreCase("")){
                    Intent intent = new Intent(EditPersonalInfo.this , ImageZoom.class);
                    intent.putExtra("key" , "1");
                    intent.putExtra("imageLink" , ""+Settings.URLIMAGEBASE + imag);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(EditPersonalInfo.this , ImageZoom.class);
                    intent.putExtra("key" , "2");
                    intent.putExtra("imageLink" , ""+picturePath);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                //submit();
                new AsyncTaskRunner().execute();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.pimage_add:
                pickProfileImage();
                break;
        }
    }

    public void pickProfileImage() {
        LayoutInflater inflater = LayoutInflater.from(EditPersonalInfo.this);
        final View dialogLayout = inflater.inflate(R.layout.activity_imagepicker, null);
        final AlertDialog builder = new AlertDialog.Builder(EditPersonalInfo.this).create();
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setView(dialogLayout);
        builder.show();

        final TextView Choosegallary = (TextView) dialogLayout.findViewById(R.id.gallary);
        final TextView Takephoto = (TextView) dialogLayout.findViewById(R.id.takephoto);
        final TextView Deletephoto = (TextView) dialogLayout.findViewById(R.id.cancel);

        Takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                userChoosenTask = Takephoto.getText().toString();

                if (!hasPermissions(EditPersonalInfo.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(EditPersonalInfo.this, PERMISSIONS, MY_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, RequestCodeCam);
                }
            }
        });
        Choosegallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                userChoosenTask = Choosegallary.getText().toString();

                if (!hasPermissions(EditPersonalInfo.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(EditPersonalInfo.this, PERMISSIONS, MY_REQUEST_CODE);
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
                        thumbnail = (Bitmap) data.getExtras().get("data");
                        uri = saveImageBitmap(thumbnail);
                        picturePath = getRealPathFromURI(uri);
                        img_profile.setImageBitmap(thumbnail);
                        uri = Uri.fromFile(new File(compressImage(picturePath)));
                    } catch (Exception e) {
                        Log.e("error123", e.getMessage());
                    }

                    break;
                case RequestCodeLib:
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = EditPersonalInfo.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    picturePath = c.getString(columnIndex);
                    c.close();

                    try {
                        thumbnail = (BitmapFactory.decodeFile(picturePath));
                        //   Log.logError("gallery.***********692." + thumbnail,picturePath);
                        // uri = Uri.fromFile(new File(picturePath));
                        uri = Uri.fromFile(new File(compressImage(picturePath)));
                        img_profile.setImageBitmap(thumbnail);

//                        RequestOptions options = new RequestOptions();
//                        options.fitCenter().placeholder(getResources().getDrawable(R.drawable.profile));
//                        Glide.with(this)
//                                .asBitmap()
//                                .load("file:///"+picturePath)
//                                .apply(options)
//                                .into(img_profile);

                    } catch (Exception e) {
                        Log.e("gallery***********692.", "Exception==========Exception==============Exception" + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public static Uri saveImageBitmap(Bitmap bitmap) {
        String strDirectoy = mcontext.getFilesDir().getAbsolutePath();
        //   Utils.logError(Tag+"4149", ""+strDirectoy);
        String imageName = "usericon.PNG";

        OutputStream fOut = null;
        File file = new File(strDirectoy, imageName);
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

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        private String resp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            if (img_profile.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.profile).getConstantState()) {
                thumbnail = ((BitmapDrawable) img_profile.getDrawable()).getBitmap();
                uri = saveImageBitmap(thumbnail);
            }


         //   thumbnail = ((BitmapDrawable) img_profile.getDrawable()).getBitmap();

//            thumbnail =  getRoundedCornerBitmap(((BitmapDrawable) img_profile.getDrawable()).getBitmap(), 100);
//                uri = saveImageBitmap(thumbnail);
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            submit();
        }
        // execution of result of Long time consuming operation
    }

    public void submit() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                new Utils(EditPersonalInfo.this);

                if (thumbnail == null) {
                    Utils.toastTxt("Please upload profile Image.", EditPersonalInfo.this);
                } else if (fname.getText().toString().trim().isEmpty()) {
                    Utils.toastTxt("Please enter first name.", EditPersonalInfo.this);
                } else if (lname.getText().toString().trim().isEmpty()) {
                    Utils.toastTxt("Please enter last name.", EditPersonalInfo.this);
                } else if (mobile.getText().toString().trim().isEmpty()) {
                    Utils.toastTxt("Please enter mobile number.", EditPersonalInfo.this);
                } else if (mobile.getText().toString().trim().length() < 10) {
                    Utils.toastTxt("Please enter 10 digit mobile number.", EditPersonalInfo.this);
                } else {
                    map = new HashMap<>();
                    map.put("profilepic", thumbnail);
                    map.put(ConstVariable.FULL_NAME, fname.getText().toString());
                    map.put(ConstVariable.LAST_NAME, lname.getText().toString());
                    map.put(ConstVariable.MOBILE, mobile.getText().toString());

                    OnlineRequest.editPersonalInfoRequest(EditPersonalInfo.this, uri, map);
                }
            }
        });

    }



    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
