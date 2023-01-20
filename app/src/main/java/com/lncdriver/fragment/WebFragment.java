package com.lncdriver.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lncdriver.R;
import com.lncdriver.utils.APIClient;
import com.lncdriver.utils.APIInterface;
import com.lncdriver.utils.Settings;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebFragment extends Fragment {
    ProgressBar progressBar;
    WebView webView;

    private static final String TAG = "WebActivity";
    String url = "";


    public APIInterface apiInterface  = APIClient.getClientAdmin().create(APIInterface.class);
    public Call<ResponseBody> call = null;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web_fragment, container, false);

        webView = (WebView) view.findViewById( R.id.webView1 );
        progressBar = (ProgressBar) view.findViewById( R.id.progressBar1444);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);


      //  Bundle bundle = getArguments();
       // url = "https://lnc.latenightchauffeurs.com/lnc-administrator/admin/handbook_ios.php";
        url = "https://www.adobe.com/content/dam/acom/en/devnet/acrobat/pdfs/pdf_open_parameters.pdf";
//        webView.getSettings().setLoadWithOverviewMode(true);
      //  webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setPluginState(WebSettings.PluginState.OFF);




        callApiMethod();

        return view;
    }





    private void callApiMethod() {
        progressBar.setVisibility(View.VISIBLE);
        call = apiInterface.handbook_api();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                String responseCode = "";
                try{
                    if(response.body() != null) {
                        responseCode = response.body().string();

                        Log.e(TAG , "XXXXXFFFFAAAA "+responseCode);

                        JSONObject jsonObject = new JSONObject(responseCode);
                        String pdf_name = jsonObject.getString("pdf_name");


                        loadWebView(Settings.URL_MAIN+"admin/"+pdf_name);
                    }

                }catch (Exception e){

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });



    }





    private void loadWebView(String url) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        progressBar.setVisibility(View.VISIBLE);

        //final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

       // progressBar = ProgressDialog.show(getActivity(), "", "Loading...");

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
//                if (progressBar.isShowing()) {
//                    progressBar.dismiss();
//                }
            }

           /* public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
               alertDialog.setButton(errorCode,);
                alertDialog.show();
            }*/
        });

      //  String myPdfUrl = "https://www.adobe.com/content/dam/acom/en/devnet/acrobat/pdfs/pdf_open_parameters.pdf";
        String url1 = "http://docs.google.com/gview?embedded=true&url=" + url;

//        String data = "<!DOCTYPE html>";
//        data += "<head><title>THIS WILL NOT WORK</title></head>";
//        data += "<body><img src=\"file:///android_res/drawable/logo.png\" /></body>";
//        data += "</html>";
//        webView.loadData(data, "text/html", "UTF-8");



        webView.loadUrl(url1);
    }




}
