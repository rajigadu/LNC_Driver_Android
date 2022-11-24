package com.lncdriver.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lncdriver.BuildConfig;
import com.lncdriver.R;
import com.lncdriver.activity.Login;
import com.lncdriver.activity.Navigation;
import com.lncdriver.model.MyApplication;
import com.lncdriver.model.SavePref;
import com.lncdriver.other.AlarmJobService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by narip on 2/4/2017.
 */
public class Utils {

    static String TAG = "Utils";

    public static final String Tag = "Utils";
    public static final String DRIVER_ID = "driver_id";
    public static final String RIDE_ID = "ride_id";
    public static final String IS_NOTIFICATION_CLICK = "is_notification_click";
    public static final String IS_NOTIF_MAP = "is_notif_click";
    public static final String WEEK_DATA = "week_data";
    public static final String TOTAL_WAITING_TIME = "total_waiting_time";
    public static final String ADMIN_FARE = "admin_fee";
    public static final String RIDE_FARE = "ride_fare";
    public static final String EARNINGS = "earnings";
    public static final String POSITION_TIME = "position_time";
    public static final String TOTAL_UNPLANNED_TIME = "position_unplanned_count";
    public static final String FINAL_W_TIME = "final_w_time";
    public static final String FINAL_UNPLANNED = "final_unplanned";

    public static final String PARTNER_NAME = "partner_name";
    public static final String PARTNER_EMAIL = "partner_email";
    public static final String PARTNER_PHONE = "partner_phone";

    public static final String CUSTOMER_NAME = "customer_name";
    public static final String PICK_UP = "pick_up";
    public static final String DROP_OFF = "drop_off";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String IS_MY_ID = "is_my_id";

    public static Context context = null;
    public static Global global;
    public static PopupWindow pwindo;
    static LayoutInflater inflater;
    public static ProgressBar progress;
    public static ProgressDialog progressDialog;
    static Boolean showPopoup = true;
    //    // Milliseconds
    static LocationManager locationManager;

    // Image Loader end
    public static android.support.v7.app.AlertDialog cartdialog;
    static int eventselectedId = 0;
    static String type = "1";
    static RadioButton typebutton;
    static HashMap<String, Object> map;

    public Utils(Context context) {
        if (context == null)
            context = Global.getAppContext();
        if (context != null) {
            if (global == null)
                global = new Global();
            Utils.context = context;
            if (context instanceof Activity)
                global.setCurrentActivity((Activity) context);
        }
    }

    //schedule the start of the service every 10 - 30 seconds
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, AlarmJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 1000); // wait at least
        builder.setOverrideDeadline(3 * 1000); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

    public static void partnerLookingRequest(String status, Context mcontext) {
        SavePref pref1 = new SavePref();
        pref1.SavePref(mcontext);
        String rid = pref1.getRideId();

        new Utils(mcontext);
        map = new HashMap<>();
        map.put("request_id", rid);
        map.put("status", status);
        OnlineRequest.partnerLookingRequest(mcontext, map);
    }

    public static void setImagePiccaso(Context context, String url, ImageView imagevv) {
        //Utils.logError(Tag+"118", "PHOTO===="+url);

        Picasso.with(context).load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(imagevv);

        /*Picasso.with(context).load(url).resize(600, 600)
                .centerInside()
                .onlyScaleDown()
                .into(new Target()
                {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap,Picasso.LoadedFrom from)
                    {
                        imagevv.setImageBitmap(bitmap);
                        Log.logError("TAG"+"130", "SUCCESS");
                    }

                    @Override
                    public void onBitmapFailed(final Drawable errorDrawable)
                    {
                        Log.logError("TAG4"+"137", "FAILED");
                        imagevv.setImageResource(R.drawable.placeholder);
                    }

                    @Override
                    public void onPrepareLoad(final Drawable placeHolderDrawable)
                    {
                        Log.logError("TAG4"+"148", "Prepare Load");
                    }
                });*/
    }

    public static int getHeightWidth(String mode) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int lenthval = 0;
        if (mode.equalsIgnoreCase("height")) {
            lenthval = height;
        } else if (mode.equalsIgnoreCase("width")) {
            lenthval = width;
        }
        return lenthval;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void showOkDialog(String dlgText, Context parent) {
        if (context == null && Utils.context != null) {
            context = Utils.context;
        }
        // final Dialog dialog = new Dialog(parent);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog. getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // dialog.setContentView(R.layout.alert_dialog);

        LayoutInflater inflater = LayoutInflater.from(Utils.context);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog1, null);
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(Utils.context).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        final RelativeLayout rootLo = (RelativeLayout) dialog.findViewById(R.id.rootlo);
        // rootLo.getLayoutParams().width = getHeightWidth("width") - getHeightWidth("width")/4;
        final TextView title = dialog.findViewById(R.id.title);
        final TextView textView = dialog.findViewById(R.id.desc);
        final Button buttonOk = dialog.findViewById(R.id.buttonOk);
        // ImageView iconImage =  dialog .findViewById(R.id.imageView);
        // iconImage.setVisibility(View.VISIBLE);
        textView.setText(dlgText);
        title.setText(R.string.app_name);
        // Utils.setFontStyle(context, buttonok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                } catch (Exception e) {
                } finally {
                    dialog.cancel();
                }
            }
        });
        dialog.show();
    }

    public static void showCancelRideStatusDialog(String mtitle, String dlgText, Context mcontext) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog1, null);
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mcontext,
                android.R.style.Theme_Material_Dialog_Alert).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setView(dialogLayout);
        dialog.show();

        final RelativeLayout rootLo = dialog.findViewById(R.id.rootlo);
        //rootLo.getLayoutParams().width = getHeightWidth("width") - getHeightWidth("width")/4;
        final TextView title = dialog.findViewById(R.id.title);
        final TextView textView = dialog.findViewById(R.id.desc);
        final Button buttonok = dialog.findViewById(R.id.buttonOk);
        // final ImageView iconImage = (ImageView) dialog.findViewById(R.id.imageView);
        // iconImage.setVisibility(View.VISIBLE);
        textView.setText(dlgText);
        title.setText(mtitle);
        //Utils.setFontStyle(context, buttonok);
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                } catch (Exception e) {
                } finally {
                    dialog.cancel();
                }
            }
        });
        dialog.show();

       /* dialogLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                YoYo.with(Techniques.ZoomIn).duration(500).playOn(dialogLayout);
            }
        });*/
    }

    public static void showRideRequestDialog(final Context mcontext) {
        LayoutInflater inflater = LayoutInflater.from(Utils.context);
        final View dialogLayout = inflater.inflate(R.layout.alert_dialog2, null);
        cartdialog = new android.support.v7.app.AlertDialog.Builder(Utils.context).create();
        cartdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cartdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        cartdialog.getWindow().setGravity(Gravity.CENTER);
        cartdialog.setView(dialogLayout);
        cartdialog.show();

        final RadioGroup typegroup = cartdialog.findViewById(R.id.agroups);
        final Button request = cartdialog.findViewById(R.id.request);
        final ImageView close = cartdialog.findViewById(R.id.close);
        final RelativeLayout rl_partner = cartdialog.findViewById(R.id.rl_partner);
        TextView pname = cartdialog.findViewById(R.id.pname);
        TextView pmail = cartdialog.findViewById(R.id.pemail);
        TextView pmobile = cartdialog.findViewById(R.id.pmobile);

        rl_partner.setVisibility(View.GONE);
        eventselectedId = typegroup.getCheckedRadioButtonId();
        typebutton = cartdialog.findViewById(eventselectedId);
        type = "1";

        typegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio1:
                        eventselectedId = typegroup.getCheckedRadioButtonId();
                        typebutton = (RadioButton) cartdialog.findViewById(eventselectedId);
                        type = "1";
                        rl_partner.setVisibility(View.GONE);
                        Toast.makeText(mcontext, type, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio2:
                        eventselectedId = typegroup.getCheckedRadioButtonId();
                        typebutton = (RadioButton) cartdialog.findViewById(eventselectedId);
                        type = "2";
                        rl_partner.setVisibility(View.GONE);
                        Toast.makeText(mcontext, type, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio3:
                        eventselectedId = typegroup.getCheckedRadioButtonId();
                        typebutton = (RadioButton) cartdialog.findViewById(eventselectedId);
                        type = "3";
                        rl_partner.setVisibility(View.VISIBLE);
                        Toast.makeText(mcontext, type, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartdialog.cancel();
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastTxt("Still not implemented.", mcontext);

                /*if (type.equalsIgnoreCase("3"))
                {
                    if (pname.toString().isEmpty())
                    {
                        toastTxt("please enter partner name.",mcontext);
                    }
                    else if (pmail.toString().isEmpty())
                    {
                        toastTxt("please enter partner email.",mcontext);
                    }
                    else if(!Utils.isValidEmail(pmail.getText().toString().trim()))
                    {
                        Utils.toastTxt("Please enter valid email address.",mcontext);
                    }
                    else if (pmobile.toString().isEmpty())
                    {
                        toastTxt("please enter partner mobile number.",mcontext);
                    }
                    else
                    {
                        cartdialog.cancel();
                        map=new HashMap<>();
                        map.put("servicetype",type);
                        map.put("pname",pname.getText().toString());
                        map.put("pemail",pmail.getText().toString());
                        map.put("pmobile",pmobile.getText().toString());
                        OnlineRequest.driverAcceptRequest(mcontext,map);

                    }
                }
                else if (type.equalsIgnoreCase("2"))
                {
                    cartdialog.cancel();
                    map=new HashMap<>();
                    map.put("servicetype",type);
                    OnlineRequest.driverAcceptRequest(mcontext,map);

                }
                else  if (type.equalsIgnoreCase("1"))
                {
                    cartdialog.cancel();
                    map=new HashMap<>();
                    map.put("servicetype",type);
                    OnlineRequest.driverAcceptRequest(mcontext,map);
                }               */
            }
        });
        cartdialog.show();
    }

    public static void setLogOut(Context context) {
        if (context == null && Utils.context != null) {
            context = Utils.context;
        }

        /* prefs = context.getSharedPreferences("com.haystack",0);
         editor = prefs.edit();
         editor.clear();
         editor.commit();*/
        Utils.toastTxt("You Log_Out Successfully", Utils.context);

        Settings.NETWORK_STATUS = "";
    }

    public static final void toastTxt(String str, Context context) {
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static final void longToastTxt(String str, Context context) {
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
        toast.show();
    }

    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        if (context == null && Utils.context != null) {
            context = Utils.context;
        }
        NetworkInfo activeNetworkInfo = null;
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                Settings.NETWORK_STATUS = "";
                Settings.NETWORK_TYPE = "";
            } else {
                if (activeNetworkInfo.getType() == 1) {
                    Settings.NETWORK_TYPE = "WiFi";
                }
                if (activeNetworkInfo.getType() == 0)
                    Settings.NETWORK_TYPE = "3G";
                if (activeNetworkInfo.getType() == 0
                        && activeNetworkInfo.getType() == 1)
                    Settings.NETWORK_TYPE = "WiFi";
                if (activeNetworkInfo.getType() == 6) {
                    Settings.NETWORK_TYPE = "WiMax";
                }
            }
        }
        return activeNetworkInfo != null;
    }

    public static void showInternetErrorMessage(Context context) {
        if (context == null && Utils.context != null)
            context = Utils.context;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView textView = dialog.findViewById(R.id.desc);
        Button buttonOk = dialog.findViewById(R.id.buttonOk);
        textView.setText(R.string.connection_subtext);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                } finally {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public static final void logError(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void uploadImage(Context mcontext, Uri fileUri, HashMap<String, Object> param, final int mode) {
        Utils.logError("JsonPost54123", "send events are >>>" + param);
        // create upload service client
        // initiatePopupWindow(mcontext, "uploading Image...");
        initiatePopupWindow(context, "uploading Image...");

        UploadImage service = ServiceGenerator.createService(UploadImage.class);

        //https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri

        MultipartBody.Part body_one = null;
        MultipartBody.Part body_two = null;
        MultipartBody.Part body_three = null;
        MultipartBody.Part body_four = null;
        MultipartBody.Part body_five = null;

        if (mode == ConstVariable.Edit_Vehicle_Info) {
            if (param.containsKey("vehicle_image") && !param.get("vehicle_image").toString().equalsIgnoreCase("")) {
                File file = new File(fileUri.getPath());
                // create RequestBody instance from file
                RequestBody reqFile = RequestBody.create(MediaType.parse("vehicle_image/*"), file);
                // MultipartBody.Part is used to send also the actual file name
                body_one = MultipartBody.Part.createFormData("vehicle_image", "vehicle_image.png", reqFile);
            }
            if (param.containsKey("documents_image") && !param.get("documents_image").toString().equalsIgnoreCase("")) {
                Uri myUri = Uri.parse(param.get("uri_two").toString());

                File file = new File(myUri.getPath());
                // create RequestBody instance from file
                RequestBody reqFile = RequestBody.create(MediaType.parse("documents_image/*"), file);
                // MultipartBody.Part is used to send also the actual file name
                body_two = MultipartBody.Part.createFormData("documents_image", "documents_image.png", reqFile);
            }
            if (param.containsKey("license_image") && !param.get("license_image").toString().equalsIgnoreCase("")) {
                Uri myUri = Uri.parse(param.get("uri_three").toString());

                File file = new File(myUri.getPath());
                // create RequestBody instance from file
                RequestBody reqFile = RequestBody.create(MediaType.parse("license_image/*"), file);
                // MultipartBody.Part is used to send also the actual file name
                body_three = MultipartBody.Part.createFormData("license_image", "license_image.png", reqFile);
            }
            if (param.containsKey("driver_abstract") && !param.get("driver_abstract").toString().equalsIgnoreCase("")) {
                Uri myUri = Uri.parse(param.get("uri_four").toString());

                File file = new File(myUri.getPath());
                // create RequestBody instance from file
                RequestBody reqFile = RequestBody.create(MediaType.parse("driver_abstract/*"), file);
                // MultipartBody.Part is used to send also the actual file name
                body_four = MultipartBody.Part.createFormData("driver_abstract", "driver_abstract.png", reqFile);
            }
        } else if (fileUri != null) {
            File file = new File(fileUri.getPath());
            // create RequestBody instance from file
            RequestBody reqFile = RequestBody.create(MediaType.parse("profilepic/*"), file);
            // MultipartBody.Part is used to send also the actual file name
            body_one = MultipartBody.Part.createFormData("profilepic", "profilepic.png", reqFile);
        }

        if (mode == ConstVariable.SignUp) {
            if (param.containsKey("vehicle_image") && !param.get("vehicle_image").toString().equalsIgnoreCase("")) {
                Uri myUri = Uri.parse(param.get("uri_two").toString());

                File file = new File(myUri.getPath());
                // create RequestBody instance from file
                RequestBody reqFile = RequestBody.create(MediaType.parse("vehicle_image/*"), file);
                // MultipartBody.Part is used to send also the actual file name
                body_two = MultipartBody.Part.createFormData("vehicle_image", "vehicle_image.png", reqFile);
            }
            if (param.containsKey("documents_image") && !param.get("documents_image").toString().equalsIgnoreCase("")) {
                Uri myUri = Uri.parse(param.get("uri_three").toString());

                File file = new File(myUri.getPath());
                // create RequestBody instance from file
                RequestBody reqFile = RequestBody.create(MediaType.parse("documents_image/*"), file);
                // MultipartBody.Part is used to send also the actual file name
                body_three = MultipartBody.Part.createFormData("documents_image", "documents_image.png", reqFile);
            }
            if (param.containsKey("license_image") && !param.get("license_image").toString().equalsIgnoreCase("")) {
                Uri myUri = Uri.parse(param.get("uri_four").toString());

                File file = new File(myUri.getPath());
                // create RequestBody instance from file
                RequestBody reqFile = RequestBody.create(MediaType.parse("license_image/*"), file);
                // MultipartBody.Part is used to send also the actual file name
                body_four = MultipartBody.Part.createFormData("license_image", "license_image.png", reqFile);
            }
            if (param.containsKey("driver_abstract") && !param.get("driver_abstract").toString().equalsIgnoreCase("")) {
                Uri myUri = Uri.parse(param.get("uri_five").toString());

                File file = new File(myUri.getPath());
                // create RequestBody instance from file
                RequestBody reqFile = RequestBody.create(MediaType.parse("abstract_image/*"), file);
                // MultipartBody.Part is used to send also the actual file name
                body_five = MultipartBody.Part.createFormData("driver_abstract", "driver_abstract.png", reqFile);
            }
        }

        RequestBody id = null;

        if (mode != ConstVariable.SignUp)
            id = RequestBody.create(MediaType.parse("multipart/form-data"), param.get(ConstVariable.DRIVER_ID).toString());

        RequestBody fname = null, lname = null, mobile = null, json = null, vtype = null, vmodel = null, vmyear = null;
        Call<ResponseBody> call = null;

        if (mode == ConstVariable.SignUp) {
            json = RequestBody.create(MediaType.parse("multipart/form-data"), param.get("json").toString());
            call = service.signup(param.get(ConstVariable.URL).toString(), body_one, body_two, body_three, body_four, body_five, json);
        } else if (mode == ConstVariable.Edit_Personal_Info) {
            fname = RequestBody.create(MediaType.parse("multipart/form-data"), param.get(ConstVariable.FULL_NAME).toString());
            lname = RequestBody.create(MediaType.parse("multipart/form-data"), param.get("lname").toString());
            mobile = RequestBody.create(MediaType.parse("multipart/form-data"), param.get(ConstVariable.MOBILE).toString());

            call = service.upload(param.get(ConstVariable.URL).toString(), id, fname, lname, mobile, body_one);
        } else if (mode == ConstVariable.Edit_Vehicle_Info) {
            vtype = RequestBody.create(MediaType.parse("multipart/form-data"), param.get("vehicle_type").toString());
            vmodel = RequestBody.create(MediaType.parse("multipart/form-data"), param.get("vehicle_model").toString());
            vmyear = RequestBody.create(MediaType.parse("multipart/form-data"), param.get("vehile_making_year").toString());

            call = service.upload_vehicle(param.get(ConstVariable.URL).toString(), id, vtype, vmodel, vmyear, body_one, body_two, body_three, body_four);
        } else {
            call = service.uploadres(param.get(ConstVariable.URL).toString(), body_one);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (Utils.progressDialog != null) {
                        Utils.progressDialog.dismiss();
                        Utils.progressDialog = null;
                    }
                    String resp = "";
                    Log.e("Rrespppppp123456--->", response.body().toString());
                    if (response.body() != null) {
                        resp = response.body().string();
                        System.out.println("Rrespppppp--->" + resp);
                        Log.e("response4227", "response------------------>" + resp);

                        if (mode == ConstVariable.Login) {
                            String result = JsonHelper.getResults(resp, context, mode);

                            if (result.equalsIgnoreCase(ConstVariable.SUCCESS)) {
                                Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), Utils.context);
                                Utils.startActivity(context, Login.class);
                            } else {
                                Utils.showOkDialog(result.toString(), context);
                            }
                        }

                        if (mode == ConstVariable.SignUp) {
                            String result = JsonHelper.getResults(resp, context, mode);

                            if (result.equalsIgnoreCase(ConstVariable.SUCCESS)) {
                                new Utils(context);
                                // Utils.logError(TAG + "146","signup successfull====");
                                Utils.longToastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                                Utils.startActivity(context, com.lncdriver.activity.Login.class);
                            } else {
                                new Utils(context);
                                // Log.logError("119","response"+result.toString());
                                Utils.toastTxt(Utils.global.mapMain.get(ConstVariable.MESSAGE).toString(), context);
                            }
                        }

                        if (mode == ConstVariable.Edit_Vehicle_Info) {
                            String result = JsonHelper.getResults(resp, context, mode);

                            if (result.equalsIgnoreCase(ConstVariable.SUCCESS)) {
                                new Utils(context);
                                Utils.toastTxt(Utils.global.mapMain.get("message").toString(), context);
                                //com.munched.activity.EditProfile.Instance.closeActivity();

                                /*SavePref pref1 = new SavePref();
                                pref1.SavePref(context);
                                pref1.setImage(Utils.global.mapMain.get("profile_pic").toString());
                                Log.logError("Rrespppppp9999999--->",Utils.global.mapMain.get("profile_pic").toString());
                                pref1.setUserFName(Utils.global.mapMain.get("first_name").toString());
                                pref1.setUserLName(Utils.global.mapMain.get("last_name").toString());
                                pref1.setMobile(Utils.global.mapMain.get(ConstVariable.MOBILE).toString());*/
                                Navigation.nid = 9;
                                Utils.startActivity(context, Navigation.class);
                            } else {
                                new Utils(context);
                                Utils.toastTxt(Utils.global.mapMain.get("message").toString(), context);
                            }
                        }

                        if (mode == ConstVariable.Edit_Personal_Info) {
                            String result = JsonHelper.getResults(resp, context, mode);

                            if (result.equalsIgnoreCase(ConstVariable.SUCCESS)) {
                                Utils.global.mapMain = Utils.global.personaldetailsList.get(0);

                                new Utils(context);
                                Utils.toastTxt("Profile details updated successfully.", context);

                                SavePref pref1 = new SavePref();
                                pref1.SavePref(context);

                                if (Utils.global.mapMain.containsKey("profile_pic") && !Utils.global.mapMain.get("profile_pic").toString().equalsIgnoreCase(""))
                                    pref1.setImage(Utils.global.mapMain.get("profile_pic").toString());

                                if (Utils.global.mapMain.containsKey("first_name") && !Utils.global.mapMain.get("first_name").toString().equalsIgnoreCase(""))
                                    pref1.setUserFName(Utils.global.mapMain.get("first_name").toString());

                                if (Utils.global.mapMain.containsKey("last_name") && !Utils.global.mapMain.get("last_name").toString().equalsIgnoreCase(""))
                                    pref1.setUserLName(Utils.global.mapMain.get("last_name").toString());

                                if (Utils.global.mapMain.containsKey(ConstVariable.MOBILE) && !Utils.global.mapMain.get(ConstVariable.MOBILE).toString().equalsIgnoreCase(""))
                                    pref1.setMobile(Utils.global.mapMain.get(ConstVariable.MOBILE).toString());

                                Navigation.nid = 9;
                                Utils.startActivity(context, Navigation.class);
                            } else {
                                new Utils(context);
                                Utils.toastTxt(result.toString(), context);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (Utils.progressDialog != null) {
                    Utils.progressDialog.dismiss();
                    Utils.progressDialog = null;
                }
                //Log.logError("Upload error:", t.getMessage());
            }
        });
    }

    public static void initiatePopupWindow(Context mcontext, String text) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(mcontext);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait request is in progress...");
                progressDialog.show();
            }
        } catch (Exception e) {
            //Utils.logError("Utils 3548","Exception=============Exception==================Exception");
            e.printStackTrace();
        } finally {
            //Utils.logError("2initiatePopupWindow","2513test------" + Global.mCurrentActivity);
        }
    }

    public static void startActivity(Context context, Class<?> activity) {
        if (context == null)
            context = Utils.context;

        Intent intent = new Intent(context, activity);
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ((Activity) context).startActivityForResult(intent, 0);
        } catch (Exception ex) {
            //Utils.logError(Tag + "1896", "Exception===========Exception===========Exception");
            ex.printStackTrace();
        }
        intent = null;
    }

    public static void hideSoftKeyboard(Activity activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static HashMap<String, Object> GetJsonDataIntoMap(Context context, JSONArray array, String mode) {

        // SharedPreferences prefs =
//        Utils.logError("Utils 709", "GetEventOfflineList ok");
        HashMap<String, Object> item = new HashMap<String, Object>();
        // List<HashMap<String, Object>> collection = new ArrayList<HashMap<String, Object>>();
//        if (context instanceof MainActivityAdmin) {
        try {
//                Utils.logError("Utils 712", "GetEventOfflineList ok");

            HashMap<String, Object> itemSub = null;
//                Utils.logError("Utils 715", "GetEventOfflineList ok"+array);
            JSONObject ary = null;
            JSONObject arySub = null;
//                Utils.logError("Utils760", ""+array);
            for (int i = 0; i < array.length(); i++) {
//                    Utils.logError("Utils 717", "GetEventOfflineList ok " + i);
//                String obj = (String) array.get(i);
                ary = array.optJSONObject(i);

//                    Utils.logError("Utils 719", "JSONObject ok " + ary);
                if (ary != null) {
//                        Utils.logError("Utils 720", "JSONObject ok " + ary);
                    Iterator<String> it = ary.keys();
//                        Utils.logError("Utils 723", "Iterator ok " + it);
                    while (it.hasNext()) {
                        String key = it.next();

                        //for sub array

                        if (key.equalsIgnoreCase(ConstVariable.SUBARRAY)) {
//                            Utils.logError("Utils 733", "JSONObject ok "+ConstVariable.EVENTIMAGE);
                            JSONArray arraySub = ary.optJSONArray(ConstVariable.SUBARRAY);
//                            Utils.logError("Utils 733", "arraySub ok "+arraySub);
                            if (arraySub != null) {
                                List<HashMap<String, Object>> tempArrayMapImage = new ArrayList<HashMap<String, Object>>();
                                for (int j = 0; j < arraySub.length(); j++) {
                                    arySub = arraySub.optJSONObject(j);
                                    if (arySub != null) {
//                                Utils.logError("Utils 736", "JSONObject ok "+arySub);
                                        Iterator<String> itSub = arySub.keys();
//                                Utils.logError("Utils 738", "Iterator ok "+itSub);
                                        itemSub = new HashMap<String, Object>();
//                                        Utils.logError("Utils 786", "arySubPer ok " + arySub);
                                        while (itSub.hasNext()) {
                                            String keySub = itSub.next();
//                                            Utils.logError("Utils 786", keySub + "GetEventOfflineList ok " + arySub.get(keySub));
                                            if (arySub.get(keySub) != null) {
//                                    Utils.logError("Utils780",key+" : "+ary.get(key));
                                                itemSub.put(keySub, arySub.get(keySub));
                                            } else {
//                                    Utils.logError("Utils780","key : "+key);
                                                itemSub.put(keySub, "");
                                            }
                                        }
                                        tempArrayMapImage.add(itemSub);
                                    } else {
                                        tempArrayMapImage = null;
                                    }
                                }
                                item.put(ConstVariable.SUBARRAY, tempArrayMapImage);
//                                    Utils.logError("Utils 795", "arySubPer ok " + item);
                            } else {
                                item.put(ConstVariable.SUBARRAY, null);
                            }
                        } else if (key.equalsIgnoreCase(ConstVariable.SUBARRAY1)) {
//                            Utils.logError("Utils 733", "JSONObject ok "+ConstVariable.EVENTIMAGE);
                            JSONArray arraySub = ary.optJSONArray(ConstVariable.SUBARRAY1);
//                            Utils.logError("Utils 733", "arraySub ok "+arraySub);
                            if (arraySub != null) {
                                List<HashMap<String, Object>> tempArrayMapImage = new ArrayList<HashMap<String, Object>>();
                                for (int j = 0; j < arraySub.length(); j++) {
                                    arySub = arraySub.optJSONObject(j);
                                    if (arySub != null) {
//                                Utils.logError("Utils 736", "JSONObject ok "+arySub);
                                        Iterator<String> itSub = arySub.keys();
//                                Utils.logError("Utils 738", "Iterator ok "+itSub);
                                        itemSub = new HashMap<String, Object>();
//                                        Utils.logError("Utils 786", "arySubPer ok " + arySub);
                                        while (itSub.hasNext()) {
                                            String keySub = itSub.next();
//                                            Utils.logError("Utils 786", keySub + "GetEventOfflineList ok " + arySub.get(keySub));
                                            if (arySub.get(keySub) != null) {
//                                    Utils.logError("Utils780",key+" : "+ary.get(key));
                                                itemSub.put(keySub, arySub.get(keySub));
                                            } else {
//                                    Utils.logError("Utils780","key : "+key);
                                                itemSub.put(keySub, "");
                                            }
                                        }
                                        tempArrayMapImage.add(itemSub);
                                    } else {
                                        tempArrayMapImage = null;
                                    }
                                }
                                item.put(ConstVariable.SUBARRAY1, tempArrayMapImage);
//                                    Utils.logError("Utils 795", "arySubPer ok " + item);
                            } else {
                                item.put(ConstVariable.SUBARRAY1, null);
                            }
                        } else {
                            if (ary.get(key) != null) {
//                                    Utils.logError("Utils780",key+" : "+ary.get(key));
                                item.put(key, ary.get(key));
                            } else {
//                                    Utils.logError("Utils780","key : "+key);
                                item.put(key, "");
                            }
                        }

                    }
//                    collection.add(item);

                }
            }
        } catch (JSONException e) {
            //Utils.logError("Error 4094", "while parsing===================JSONException==============JSONException");
            e.printStackTrace();
        }
//        }
        //Utils.logError("Utils 751", "collection ok " + item);
        return item;
    }

    public static List GetJsonDataIntoList(Context context, JSONArray array, String mode) {
        List<HashMap<String, Object>> collection = new ArrayList<HashMap<String, Object>>();
        try {
            HashMap<String, Object> item = null;
            HashMap<String, Object> itemSub = null;
            JSONObject ary = null;
            JSONObject arySub = null;
            for (int i = 0; i < array.length(); i++) {
                ary = array.optJSONObject(i);
                if (ary != null) {
                    Iterator<String> it = ary.keys();
                    item = new HashMap<>();
                    while (it.hasNext()) {
                        String key = it.next();
                        if (key.equalsIgnoreCase(ConstVariable.SUBARRAY2)) {
                            JSONArray arraySub = ary.optJSONArray(ConstVariable.SUBARRAY2);
                            if (arraySub != null) {
                                List<HashMap<String, Object>> tempArrayMapImage = new ArrayList<HashMap<String, Object>>();
                                for (int j = 0; j < arraySub.length(); j++) {
                                    arySub = arraySub.optJSONObject(j);
                                    if (arySub != null) {
//                                Utils.logError("Utils 736", "JSONObject ok "+arySub);
                                        Iterator<String> itSub = arySub.keys();
//                                Utils.logError("Utils 738", "Iterator ok "+itSub);
                                        itemSub = new HashMap<String, Object>();
//                                        Utils.logError("Utils 786", "arySubPer ok " + arySub);
                                        while (itSub.hasNext()) {
                                            String keySub = itSub.next();
//                                            Utils.logError("Utils 786", keySub + "GetEventOfflineList ok " + arySub.get(keySub));
                                            if (arySub.get(keySub) != null) {
//                                    Utils.logError("Utils780",key+" : "+ary.get(key));
                                                item.put(keySub, arySub.get(keySub));
                                            } else {
//                                    Utils.logError("Utils780","key : "+key);
                                                item.put(keySub, "");
                                            }
                                        }
                                        tempArrayMapImage.add(itemSub);
                                    } else {
                                        tempArrayMapImage = null;
                                    }
                                }
                                //item.put(key, ary.get(key));
                                //item.put(ConstVariable.SUBARRAY2, tempArrayMapImage);
//                                    Utils.logError("Utils 795", "arySubPer ok " + item);
                            } else {
                                //item.put(key,"");
                                //item.put(ConstVariable.SUBARRAY2, null);
                            }
                        } else {
                            if (ary.get(key) != null) {
//                                    Utils.logError("Utils780",key+" : "+ary.get(key));
                                item.put(key, ary.get(key));
                            } else {
//                                    Utils.logError("Utils780","key : "+key);
                                item.put(key, "");
                            }
                        }
                    }
                    collection.add(item);
//                        Utils.logError("Utils 751", "collection ok " + collection);
                }
            }
        } catch (JSONException e) {
            // Utils.logError("Error 3860", "while parsing=====JSONException================JSONException");
            e.printStackTrace();
        }
//        }
        return collection;
    }

    public static List GetPartnerJsonDataIntoList(Context context, JSONArray array, String mode) {
        List<HashMap<String, Object>> collection = new ArrayList<>();
        try {
            HashMap<String, Object> item = null;
            HashMap<String, Object> itemSub = null;
            JSONObject ary = null;
            JSONObject arySub = null;
            for (int i = 0; i < array.length(); i++) {
                ary = array.optJSONObject(i);
                if (ary != null) {
                    Iterator<String> it = ary.keys();
                    item = new HashMap<>();
                    while (it.hasNext()) {
                        String key = it.next();

                        if (key.equalsIgnoreCase("partnerData")) {
                            JSONArray arraySub = ary.optJSONArray("partnerData");

                            if (arraySub != null) {
                                List<HashMap<String, Object>> tempArrayMapImage = new ArrayList<HashMap<String, Object>>();
                                for (int j = 0; j < arraySub.length(); j++) {
                                    arySub = arraySub.optJSONObject(j);
                                    if (arySub != null) {
//                                Utils.logError("Utils 736", "JSONObject ok "+arySub);
                                        Iterator<String> itSub = arySub.keys();
//                                Utils.logError("Utils 738", "Iterator ok "+itSub);
                                        itemSub = new HashMap<String, Object>();
//                                        Utils.logError("Utils 786", "arySubPer ok " + arySub);
                                        while (itSub.hasNext()) {
                                            String keySub = itSub.next();
//                                            Utils.logError("Utils 786", keySub + "GetEventOfflineList ok " + arySub.get(keySub));
                                            if (arySub.get(keySub) != null) {
//                                    Utils.logError("Utils780",key+" : "+ary.get(key));
                                                item.put(keySub, arySub.get(keySub));
                                            } else {
//                                    Utils.logError("Utils780","key : "+key);
                                                item.put(keySub, "");
                                            }
                                        }
                                        tempArrayMapImage.add(itemSub);
                                    } else {
                                        tempArrayMapImage = null;
                                    }
                                }
                                //item.put(key, ary.get(key));
                                //item.put(ConstVariable.SUBARRAY2, tempArrayMapImage);
//                                    Utils.logError("Utils 795", "arySubPer ok " + item);
                            } else {
                                //item.put(key,"");
                                //item.put(ConstVariable.SUBARRAY2, null);
                            }
                        } else {
                            if (ary.get(key) != null) {
//                                    Utils.logError("Utils780",key+" : "+ary.get(key));
                                item.put(key, ary.get(key));
                            } else {
//                                    Utils.logError("Utils780","key : "+key);
                                item.put(key, "");
                            }
                        }
                    }
                    collection.add(item);
//                        Utils.logError("Utils 751", "collection ok " + collection);
                }
            }
        } catch (JSONException e) {
            // Utils.logError("Error 3860", "while parsing=====JSONException================JSONException");
            e.printStackTrace();
        }
//        }
        return collection;
    }

    public static void startPowerSaverIntent(final Context context) {
        SharedPreferences settings = context.getSharedPreferences("ProtectedApps", Context.MODE_PRIVATE);
        boolean skipMessage = settings.getBoolean("skipProtectedAppCheck", false);
        if (!skipMessage) {
            final SharedPreferences.Editor editor = settings.edit();
            boolean foundCorrectIntent = false;
            for (final Intent intent : POWERMANAGER_INTENTS) {
                if (isCallable(context, intent)) {
                    foundCorrectIntent = true;
                    final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(context);
                    dontShowAgain.setText("Do not show again");
                    dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            editor.putBoolean("skipProtectedAppCheck", isChecked);
                            editor.apply();
                        }
                    });

                    new AlertDialog.Builder(context)
                            .setTitle(Build.MANUFACTURER + " Protected Apps")
                            .setMessage(String.format("%s requires to be enabled in 'Protected Apps'" +
                                    " to function properly.%n", context.getString(R.string.app_name)))
                            .setView(dontShowAgain)
                            .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    context.startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                    break;
                }
            }
            if (!foundCorrectIntent) {
                editor.putBoolean("skipProtectedAppCheck", true);
                editor.apply();
            }
        }
    }

    private static boolean isCallable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static List<Intent> POWERMANAGER_INTENTS = Arrays.asList(
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")).setData(Uri.fromParts("package", MyApplication.getContext().getPackageName(), null)),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.autostart.AutoStartActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"))
                    .setData(android.net.Uri.parse("mobilemanager://function/entry/AutoStart")),
            new Intent().setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.SHOW_APPSEC")).addCategory(Intent.CATEGORY_DEFAULT).putExtra("packageName", BuildConfig.APPLICATION_ID)
    );

    public static void ifHuaweiAlert(final Context context) {
        final SharedPreferences settings = context.getSharedPreferences("ProtectedApps", Context.MODE_PRIVATE);
        final String saveIfSkip = "skipProtectedAppsMessage";
        boolean skipMessage = settings.getBoolean(saveIfSkip, false);
        if (!skipMessage) {
            final SharedPreferences.Editor editor = settings.edit();
            Intent intent = new Intent();
            intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            if (isCallable(context, intent)) {
                final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(context);
                dontShowAgain.setText("Do not show again");
                dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        editor.putBoolean(saveIfSkip, isChecked);
                        editor.apply();
                    }
                });

                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Huawei Protected Apps")
                        .setMessage(String.format("%s requires to be enabled in 'Protected Apps' to function properly.%n", context.getString(R.string.app_name)))
                        .setView(dontShowAgain)
                        .setPositiveButton("Protected Apps", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                huaweiProtectedApps(context);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            } else {
                editor.putBoolean(saveIfSkip, true);
                editor.apply();
            }
        }
    }


    private static void huaweiProtectedApps(Context context) {
        try {
            String cmd = "am start -n com.huawei.systemmanager/.optimize.process.ProtectActivity";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cmd += " --user " + getUserSerial(context);
            }
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) {
        }
    }

    private static String getUserSerial(Context context) {
        //noinspection ResourceType
        @SuppressLint("WrongConstant") Object userManager = context.getSystemService("user");
        if (null == userManager) return "";

        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
            Long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            if (userSerial != null) {
                return String.valueOf(userSerial);
            } else {
                return "";
            }
        } catch (Exception ignored) {
        }
        return "";
    }






    public static HashMap<String, Object> getImageLoaderDefaultCar(Activity context) {
        HashMap<String, Object> loaderHashMap = new HashMap<>();

        Resources mResources = context.getResources();
        Bitmap mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.appicon);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                mResources,
                mBitmap
        );
//        roundedBitmapDrawable.setCornerRadius(20.0f);
        roundedBitmapDrawable.setAntiAlias(true);

        ImageLoader imageLoader = null;
        DisplayImageOptions options = null;

        try{
            imageLoader = ImageLoader.getInstance();

            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(roundedBitmapDrawable)
                    .showImageForEmptyUri(roundedBitmapDrawable)
                    .showImageOnFail(roundedBitmapDrawable)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
//                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .displayer(new RoundedBitmapDisplayer(20))
//                         .displayer(new CircleBitmapDisplayer(Color.parseColor("#19457d"), 1))
                    .build();
        }catch(Exception e){
            Log.d(TAG, "myError11: "+e.getMessage());
        }

        loaderHashMap.put("imageLoader", imageLoader);
        loaderHashMap.put("options", options);

        return loaderHashMap;
    }


}
