package com.lncdriver.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.lncdriver.R;
import com.lncdriver.utils.ConstVariable;
import com.lncdriver.utils.OnlineRequest;
import com.lncdriver.utils.Utils;
import com.mikhaellopez.lazydatepicker.LazyDatePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.pimg)
    ImageView img_profile;

    @BindView(R.id.pimage_add)
    ImageView img_addprofile;

    @BindView(R.id.fname)
    EditText fname;

    @BindView(R.id.lname)
    EditText lname;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.mobile)
    EditText mobile;

    @BindView(R.id.address)
    EditText address;

    @BindView(R.id.dob)
    LazyDatePicker dob;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.cpassword)
    EditText cpassword;

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

    @BindView(R.id.signup)
    Button signup;

    @BindView(R.id.cb_w9_form)
    CheckBox cbW9Form;

    @BindView(R.id.w9_web_view)
    WebView webview;

    int eventselectedId = 0;
    String address_home = "";

    private final String loadUrl = "https://lnc.latenightchauffeurs.com/lnc-administrator/admin/lnc-form/index-pay.php";

    HashMap<String, Object> map;
    JSONObject json;

    public static final int RequestCodeCam = 1;
    public static final int MY_REQUEST_CODE = 3;
    public static final int RequestCodeLib = 2;
    public int pid_picker = 0;
    public static Bitmap thumbnail_one = null, thumbnail_two = null, thumbnail_three = null, thumbnail_four = null, thumbnail_five = null;
    public static Uri uri_one, uri_two, uri_three, uri_four, uri_five;
    public static Context mcontext;
    private String userChoosenTask;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    Calendar date;
    private static final String DATE_FORMAT = "MM-dd-yyyy";
    private boolean isW9FormChecked = false;

   /* @BindView(R.id.accnno)
    EditText accnNumber;
    @BindView(R.id.accnno_one)
    EditText accnNumber_Conf;

    @BindView(R.id.routingnum)
    EditText routingNum;
*/
   /* @BindView(R.id.npc)
    EditText npc;*/

    @BindView(R.id.type)
    EditText type;

    public static List<String> areas;
    public static String mtype = "", dob_user = "";

    private static final int REQUEST_CODE_AUTOCOMPLETE = 10;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        mcontext = this;

        thumbnail_one = null;
        thumbnail_two = null;
        thumbnail_three = null;
        thumbnail_four = null;
        thumbnail_five = null;

        signup.setOnClickListener(this);
        dob.setOnClickListener(this);
        type.setOnClickListener(this);
        img_addprofile.setOnClickListener(this);
        vehi_browse.setOnClickListener(this);
        img_addpproof.setOnClickListener(this);
        img_addlproof.setOnClickListener(this);
        img_addAbtract.setOnClickListener(this);
        back.setOnClickListener(this);
        address.setOnClickListener(this);

        cbW9Form.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    isW9FormChecked = true;
                    cbW9Form.setChecked(true);
                    webview.setVisibility(View.VISIBLE);
                    signup.setVisibility(View.INVISIBLE);
                    loadWebView();
                } else {
                    isW9FormChecked = false;
                    cbW9Form.setChecked(false);
                    webview.setVisibility(View.GONE);
                    signup.setVisibility(View.VISIBLE);
                }
            }
        });


        areas = new ArrayList<>();

        areas.add("Entry");
        areas.add("Check In");
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

        LazyDatePicker lazyDatePicker = findViewById(R.id.dob);
        lazyDatePicker.setDateFormat(LazyDatePicker.DateFormat.MM_DD_YYYY);
        //lazyDatePicker.setMinDate(minDate);
        // lazyDatePicker.setMaxDate(maxDate);

        dob_user = "";

        lazyDatePicker.setOnDatePickListener(new LazyDatePicker.OnDatePickListener() {
            @Override
            public void onDatePick(Date dateSelected) {
                //Toast.makeText(SignUp.this,"Selected tvRideDate: " + LazyDatePicker.dateToString(dateSelected, DATE_FORMAT),
                //         Toast.LENGTH_SHORT).show();
                // dob.setText(dateSelected.toString());
                // dob_user=dateSelected.getMonth()+dateSelected.getDay()+dateSelected.getYear();
                dob_user = LazyDatePicker.dateToString(dateSelected, DATE_FORMAT);
                Log.e("date12344555===", dob_user);
            }
        });
    }

    private void loadWebView() {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        //final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        progressBar = ProgressDialog.show(this, "", "Loading...");

        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

           /* public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
               alertDialog.setButton(errorCode,);
                alertDialog.show();
            }*/
        });
        webview.loadUrl(loadUrl);
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(SignUp.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                new TimePickerDialog(SignUp.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.v("", "The choosen one " + date.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    public void getdob() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                // dob.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:
                submit();
                break;
            case R.id.dob:
                getdob();
                break;
            case R.id.type:
                pickAccountType();
                break;
            case R.id.pimage_add:
                pickProfileImage();
                pid_picker = 1;
                break;
            case R.id.vbrowse:
                pickProfileImage();
                pid_picker = 2;
                break;
            case R.id.add1:
                pickProfileImage();
                pid_picker = 3;
                break;
            case R.id.add2:
                pickProfileImage();
                pid_picker = 4;
                break;
            case R.id.add3:
                pickProfileImage();
                pid_picker = 5;
                break;
            case R.id.back:
                if (webview.getVisibility() == View.VISIBLE) {
                    webview.setVisibility(View.GONE);
                    signup.setVisibility(View.VISIBLE);
                } else
                    finish();
                break;
            case R.id.address:
                openAutocompleteActivity();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (webview.getVisibility() == View.VISIBLE) {
            webview.setVisibility(View.GONE);
            signup.setVisibility(View.VISIBLE);
        } else
            finish();
    }

    public void pickAccountType() {
        new Utils(SignUp.this);
        // Utils.toastTxt("ok",UserSignup.this);

        final CharSequence[] items;
        items = areas.toArray(new CharSequence[areas.size()]);

        if (items.length > 0) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignUp.this);
            builder.setTitle("Account Type");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    type.setText(items[item]);
                    mtype = items[item].toString();
                }
            });
            android.app.AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void pickProfileImage() {
        LayoutInflater inflater = LayoutInflater.from(SignUp.this);
        final View dialogLayout = inflater.inflate(R.layout.activity_imagepicker, null);
        final AlertDialog builder = new AlertDialog.Builder(SignUp.this).create();
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

                if (!hasPermissions(SignUp.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(SignUp.this, PERMISSIONS, MY_REQUEST_CODE);
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

                if (!hasPermissions(SignUp.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(SignUp.this, PERMISSIONS, MY_REQUEST_CODE);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodeCam:
                    try {
                        if (pid_picker == 1) {
                            thumbnail_one = (Bitmap) data.getExtras().get("data");
                            uri_one = saveImageBitmap(thumbnail_one);
                            String picturePath = getRealPathFromURI(uri_one);
                            img_profile.setImageBitmap(thumbnail_one);
                            uri_one = Uri.fromFile(new File(picturePath));
                        } else if (pid_picker == 2) {
                            thumbnail_two = (Bitmap) data.getExtras().get("data");
                            uri_two = saveImageBitmap(thumbnail_two);
                            String picturePath = getRealPathFromURI(uri_two);
                            img_vehi.setImageBitmap(thumbnail_two);
                            uri_two = Uri.fromFile(new File(picturePath));
                        } else if (pid_picker == 3) {
                            thumbnail_three = (Bitmap) data.getExtras().get("data");
                            uri_three = saveImageBitmap(thumbnail_three);
                            String picturePath = getRealPathFromURI(uri_three);
                            img_personalproof.setImageBitmap(thumbnail_three);
                            uri_three = Uri.fromFile(new File(picturePath));
                        } else if (pid_picker == 4) {
                            thumbnail_four = (Bitmap) data.getExtras().get("data");
                            uri_four = saveImageBitmap(thumbnail_four);
                            String picturePath = getRealPathFromURI(uri_four);
                            img_licenceproof.setImageBitmap(thumbnail_four);
                            uri_four = Uri.fromFile(new File(picturePath));
                        } else if (pid_picker == 5) {
                            thumbnail_five = (Bitmap) data.getExtras().get("data");
                            uri_five = saveImageBitmap(thumbnail_four);
                            String picturePath = getRealPathFromURI(uri_five);
                            img_abtract.setImageBitmap(thumbnail_five);
                            uri_five = Uri.fromFile(new File(picturePath));
                        }
                    } catch (Exception e) {
                        Log.e("error123", e.getMessage());
                    }
                    break;

                case RequestCodeLib:
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = SignUp.this.getContentResolver().query(selectedImage, filePath,
                            null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();

                    try {
                        if (pid_picker == 1) {
                            thumbnail_one = (BitmapFactory.decodeFile(picturePath));
                            //   Log.logError("gallery.***********692." + thumbnail,picturePath);
                            // uri = Uri.fromFile(new File(picturePath));
                            uri_one = Uri.fromFile(new File(picturePath));

                            img_profile.setImageBitmap(thumbnail_one);
                        } else if (pid_picker == 2) {
                            thumbnail_two = (BitmapFactory.decodeFile(picturePath));
                            //   Log.logError("gallery.***********692." + thumbnail,picturePath);
                            // uri = Uri.fromFile(new File(picturePath));
                            uri_two = Uri.fromFile(new File(picturePath));
                            img_vehi.setImageBitmap(thumbnail_two);
                        } else if (pid_picker == 3) {
                            thumbnail_three = (BitmapFactory.decodeFile(picturePath));
                            //   Log.logError("gallery.***********692." + thumbnail,picturePath);
                            // uri = Uri.fromFile(new File(picturePath));
                            uri_three = Uri.fromFile(new File(picturePath));
                            img_personalproof.setImageBitmap(thumbnail_three);
                        } else if (pid_picker == 4) {
                            thumbnail_four = (BitmapFactory.decodeFile(picturePath));
                            //   Log.logError("gallery.***********692." + thumbnail,picturePath);
                            // uri = Uri.fromFile(new File(picturePath));
                            uri_four = Uri.fromFile(new File(picturePath));
                            img_licenceproof.setImageBitmap(thumbnail_four);
                        } else if (pid_picker == 5) {
                            thumbnail_five = (BitmapFactory.decodeFile(picturePath));
                            //   Log.logError("gallery.***********692." + thumbnail,picturePath);
                            // uri = Uri.fromFile(new File(picturePath));
                            uri_five = Uri.fromFile(new File(picturePath));
                            img_abtract.setImageBitmap(thumbnail_five);
                        }
                    } catch (Exception e) {
                        Log.e("gallery***********692.", "Exception==========Exception==============Exception" + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_CODE_AUTOCOMPLETE:
                    // Get the user's selected place from the Intent.
                    Place place = PlaceAutocomplete.getPlace(this, data);

                    Log.e("one=========", "Place Selected: " + place.getName());
                    Log.e("two=========", "Place Selected: " + place.getAddress());

                    //Log.logError("two=========", "Place Selected: " + place.getAddress());

                    address_home = place.getName().toString() + "," + place.getAddress().toString();
                    address.setText(address_home);

                       /* // Format the place's details and display them in the TextView.
                        address.setText(formatPlaceDetails(getResources(), place.getName(),
                                place.getId(), place.getAddress(), place.getPhoneNumber(),
                                place.getWebsiteUri()));*/
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
        options.inSampleSize = 8;
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
        // Utils.startActivity(ActivityLogin.this,ActivityEvents.class);
        new Utils(SignUp.this);

        if (thumbnail_one == null) {
            Utils.toastTxt("Please upload profile Image.", SignUp.this);
        } else if (fname.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter first name.", SignUp.this);
        } else if (lname.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter last name.", SignUp.this);
        } else if (email.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter email address.", SignUp.this);
        } else if (!Utils.isValidEmail(email.getText().toString().trim())) {
            Utils.toastTxt("Please enter valid email address.", SignUp.this);
        } else if (mobile.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter mobile number.", SignUp.this);
        } else if (mobile.getText().toString().trim().length() < 10) {
            Utils.toastTxt("Please enter 10 digit mobile number.", SignUp.this);
        } else if (dob_user.trim().isEmpty()) {
            Utils.toastTxt("Please select date of birth.", SignUp.this);
        } else if (address.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter home address.", SignUp.this);
        } else if (password.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter password.", SignUp.this);
        } else if (cpassword.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please enter confirm password.", SignUp.this);
        } else if (!cpassword.getText().toString().trim().equalsIgnoreCase(password.getText().toString().trim())) {
            Utils.toastTxt("Password and confirm password must be same.", SignUp.this);
        } else if (vehi_type.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please provide vehicle type", SignUp.this);
        } else if (vehi_model.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please provide vehicle model", SignUp.this);
        } else if (vehi_myear.getText().toString().trim().isEmpty()) {
            Utils.toastTxt("Please provide vehicle making year", SignUp.this);
        } else if (thumbnail_two == null) {
            Utils.toastTxt("Please upload Vehicle Image.", SignUp.this);
        } else if (thumbnail_three == null) {
            Utils.toastTxt("Please provide your personal proof.", SignUp.this);
        } else if (thumbnail_four == null) {
            Utils.toastTxt("Please provide your licence proof.", SignUp.this);
        } else if (thumbnail_five == null) {
            Utils.toastTxt("Please provide your abstract copy.", SignUp.this);
        } else if (!isW9FormChecked) {
            Utils.toastTxt("Please check w9 form", SignUp.this);
        }


       /* else if (accnNumber.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("A/c Number..", SignUp.this);
        }
        else if (accnNumber_Conf.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("confirm A/C Number.", SignUp.this);
        } else if (!accnNumber.getText().toString().trim().equalsIgnoreCase(accnNumber_Conf.getText().toString().trim()))
        {
            Utils.toastTxt("Account Numbers are not matched.", SignUp.this);
        }
        else if (routingNum.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Routing no.", SignUp.this);
        }
        */

       /* else if(npc.getText().toString().trim().isEmpty())
        {
           Utils.toastTxt("NPC Account.",SignUp.this);
        }

        else if(type.getText().toString().trim().isEmpty())
        {
            Utils.toastTxt("Select account type.", SignUp.this);
        }*/
        else {
            json = new JSONObject();
            try {
                json.put(ConstVariable.FULL_NAME, fname.getText().toString());
                json.put(ConstVariable.LAST_NAME, lname.getText().toString());
                json.put(ConstVariable.EMAIL, email.getText().toString());
                json.put(ConstVariable.MOBILE, mobile.getText().toString());
                json.put("dob", dob.getDate().toString());
                json.put("address", address.getText().toString());
                json.put(ConstVariable.PASSWORD, password.getText().toString());
                json.put("vtype", vehi_type.getText().toString());
                json.put("vmodel", vehi_model.getText().toString());
                json.put("makingyear", vehi_myear.getText().toString());
                // json.put("accnumber", accnNumber.getText().toString());
                // json.put("routingnumber", routingNum.getText().toString());
                // json.put("npcAccount",npc.getText().toString());
                // json.put("type", type.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            map = new HashMap<>();
            map.put("profilepic", thumbnail_one);
            map.put("vehicle_image", thumbnail_two);
            map.put("documents_image", thumbnail_three);
            map.put("license_image", thumbnail_four);
            map.put("driver_abstract", thumbnail_five);
            map.put("uri_two", uri_two);
            map.put("uri_three", uri_three);
            map.put("uri_four", uri_four);
            map.put("uri_five", uri_five);
            map.put("json", json);
            OnlineRequest.signupRequest(SignUp.this, uri_one, map);
        }
    }

    private void openAutocompleteActivity() {
        try {
            //The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to tvRideDate. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

   /* private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                            CharSequence address, CharSequence phoneNumber, Uri websiteUri)
    {
        Log.logError("87897897", res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
    }*/
}
