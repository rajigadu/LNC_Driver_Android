package com.lncdriver.utils;

import android.util.Log;

import com.lncdriver.activity.ItemNotificaitons;
import com.lncdriver.adapter.DriverFutureRideAdapter;
import com.lncdriver.fragment.ItemFutureEstimation;
import com.lncdriver.reward.ItemBonusProgram;
import com.lncdriver.reward.ItemHistoryRewardProgram;
import com.lncdriver.reward.ItemMyRewardProgram;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParsingHelper {


    private static final String TAG = "ParsingHelper";

    public ArrayList<ItemMyRewardProgram> getMyRewardProgramList(String responseCode) {
        ArrayList<ItemMyRewardProgram> arrayList = new ArrayList<>();

        try
        {
            JSONObject jsonObject = new JSONObject(responseCode);
            String status = jsonObject.getString("status");
            if(status.equalsIgnoreCase("1")){

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if(jsonArray.length() > 0){
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObjectProgram = jsonArray.getJSONObject(i);

                       // JSONObject jsonObjectProgram = jsonObjectOuter.getJSONObject("Program");

                        String id = jsonObjectProgram.getString("id");
                        String pname = jsonObjectProgram.getString("pname");
                        String pdescription = jsonObjectProgram.getString("pdescription");
                        String note = jsonObjectProgram.getString("note");
                        String target = jsonObjectProgram.getString("target");
                        String bonus = jsonObjectProgram.getString("bonus");
                        String status1 = jsonObjectProgram.getString("status");
                        String craeted_date = jsonObjectProgram.getString("craeted_date");
                        String total = jsonObjectProgram.getString("total");
                        String driver_time = jsonObjectProgram.getString("driver_time");



                        ItemMyRewardProgram program = new ItemMyRewardProgram();
                        program.setId(id);
                        program.setPname(pname);
                        program.setPdescription(pdescription);
                        program.setNote(note);
                        program.setTarget(target);
                        program.setBonus(bonus);
                        program.setStatus1(status1);
                        program.setTotal(total);
                        program.setCraeted_date(craeted_date);

                        program.setDriver_time(driver_time);

                        arrayList.add(program);
                    }
                }
            }
        }catch (Exception e){

        }

        return arrayList;
    }






    public ArrayList<ItemBonusProgram> getBonusRewardProgramList(String responseCode) {
        ArrayList<ItemBonusProgram> arrayList = new ArrayList<>();

        try
        {
            JSONObject jsonObject = new JSONObject(responseCode);
            String status = jsonObject.getString("status");
            if(status.equalsIgnoreCase("1")){

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if(jsonArray.length() > 0){
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObjectOuter = jsonArray.getJSONObject(i);

                        JSONObject jsonObjectProgram = jsonObjectOuter.getJSONObject("Program");

                        String id = jsonObjectProgram.getString("id");
                        String pname = jsonObjectProgram.getString("pname");
                        String pdescription = jsonObjectProgram.getString("pdescription");
                        String note = jsonObjectProgram.getString("note");
                        String target = jsonObjectProgram.getString("target");
                        String bonus = jsonObjectProgram.getString("bonus");
                        String status1 = jsonObjectProgram.getString("status");
                        String craeted_date = jsonObjectProgram.getString("craeted_date");

                        ItemBonusProgram program = new ItemBonusProgram();
                        program.setId(id);
                        program.setPname(pname);
                        program.setPdescription(pdescription);
                        program.setNote(note);
                        program.setTarget(target);
                        program.setBonus(bonus);
                        program.setStatus1(status1);
                        program.setCraeted_date(craeted_date);

                        arrayList.add(program);
                    }
                }
            }
        }catch (Exception e){

        }

        return arrayList;
    }






    public ArrayList<ItemHistoryRewardProgram> getHistoryRewardProgramList(String responseCode) {
        ArrayList<ItemHistoryRewardProgram> arrayList = new ArrayList<>();

        try
        {
            JSONObject jsonObject = new JSONObject(responseCode);
            String status = jsonObject.getString("status");
            if(status.equalsIgnoreCase("1")){

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if(jsonArray.length() > 0){
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObjectOuter = jsonArray.getJSONObject(i);

                        String pickup_address = jsonObjectOuter.getString("pickup_address");
                        String drop_address = jsonObjectOuter.getString("drop_address");
                        String otherdate = jsonObjectOuter.getString("otherdate");
                        String time = jsonObjectOuter.getString("time");
                        String distance = jsonObjectOuter.getString("distance");
                        String point = jsonObjectOuter.getString("point");

                        ItemHistoryRewardProgram program = new ItemHistoryRewardProgram();
                        program.setPickup_address(pickup_address);
                        program.setDrop_address(drop_address);
                        program.setOtherdate(otherdate);
                        program.setTime(time);
                        program.setDistance(distance);
                        program.setPoint(point);

                        arrayList.add(program);
                    }
                }
            }
        }catch (Exception e){

        }

        return arrayList;
    }






    public ArrayList<ItemNotificaitons> getNotications(String responseCode) {
        ArrayList<ItemNotificaitons> arrayList = new ArrayList<ItemNotificaitons>();

        try
        {
            JSONObject jsonObject = new JSONObject(responseCode);
            String status = jsonObject.getString("status");
            if(status.equalsIgnoreCase("1")){

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if(jsonArray.length() > 0){
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObjectOuter = jsonArray.getJSONObject(i);

                        String id = jsonObjectOuter.getString("id");
                        String title = jsonObjectOuter.getString("title");
                        String message = jsonObjectOuter.getString("message");
                        String date = jsonObjectOuter.getString("date");


                        ItemNotificaitons program = new ItemNotificaitons();
                        program.setId(id);
                        program.setTitle(title);
                        program.setMessage(message);
                        program.setDate(date);

                        arrayList.add(program);
                    }
                }
            }
        }catch (Exception e){

        }

        return arrayList;
    }






    public ArrayList<ItemFutureEstimation> getFutureEstimation(String responseCode) {
        final ArrayList<ItemFutureEstimation> arrayList = new ArrayList<ItemFutureEstimation>();
        try
        {
            JSONObject jsonObject = new JSONObject(responseCode);
            String status = jsonObject.getString("status");
            if(status.equalsIgnoreCase("1")){

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if(jsonArray.length() > 0){
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        final JSONObject jsonObjectOuter = jsonArray.getJSONObject(i);

                        if (jsonObjectOuter.has("second") && !jsonObjectOuter.getString("second").equalsIgnoreCase("")) {
                            int value = Integer.parseInt(jsonObjectOuter.getString("second"));
                           // int value = object3.getInt("value");
                            Log.e(TAG, "value " + value);

                            SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy hh:mm a");
                            SimpleDateFormat sdf1 = new SimpleDateFormat("M-dd-yyyy");
                            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
                            String dateInString = jsonObjectOuter.getString("otherdate") + " " + jsonObjectOuter.getString("time");
                            Date date = sdf.parse(dateInString);

                            System.out.println(dateInString);
                            System.out.println("Date - Time in milliseconds : " + date.getTime());

                            //  long xx = 1000 * 60 * 60 * 24;
                            long oneMinute = 60 * 1000;


                            long xx = 1000 * value;
                            long yy = date.getTime();


                            long millis = yy + xx +oneMinute;

                            Date expireDate = new Date(millis);


                            String dateT = sdf1.format(expireDate);
                            String timeT = sdf2.format(expireDate);


                            ItemFutureEstimation program = new ItemFutureEstimation();
                            program.setFirstTime(yy);
                            program.setSecondTime(millis);


                            String resultXXX = downloadUrlXXX(Settings.URL_MAIN_DATA + "partner_accepted_future_ride_list.php?driver_id=" + jsonObjectOuter.getString("future_partner_id"));


                            setDataOfParner(resultXXX, arrayList, program);

//
                            arrayList.add(program);


                        }

//                                                        String api = Settings.GOOGLE_DESTINATIOON+"distancematrix/json?origins="
//                                                                +jsonObjectOuter.getString("pickup_lat")+","+jsonObjectOuter.getString("pickup_long")
//                                                        +"&destinations="+jsonObjectOuter.getString("d_lat")+","+jsonObjectOuter.getString("d_long")
//                                                                +"&key=AIzaSyDhDD6XROfPs4WkFmbTHvV1KqkNVP1kEmk";
//
//                                                        Log.e(TAG, "resultdownloadUrlapi "+api);
//
//
//                                                        String result = downloadUrl(api);
//
//                                                        Log.e(TAG, "resultdownloadUrl "+result);
//
//
//
//
//                                                        try {
//                                                            JSONObject object = new JSONObject(result);
//                                                            if(object.getString("status").equalsIgnoreCase("OK")){
//                                                                JSONArray jsonArrayQQ = object.getJSONArray("rows");
//                                                                if(jsonArrayQQ.length() > 0){
//                                                                    JSONObject object1 = jsonArrayQQ.getJSONObject(0);
//                                                                    JSONArray jsonArray2 = object1.getJSONArray("elements");
//                                                                    if(jsonArray2.length() > 0){
//                                                                        JSONObject object2 = jsonArray2.getJSONObject(0);
//                                                                        if(object2.getString("status").equalsIgnoreCase("OK")){
//                                                                            JSONObject object3 = object2.getJSONObject("duration");
//
//
//                                                                            int value = object3.getInt("value");
//                                                                            Log.e(TAG , "value "+value);
//
//                                                                            SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy hh:mm a");
//                                                                            SimpleDateFormat sdf1 = new SimpleDateFormat("M-dd-yyyy");
//                                                                            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
//                                                                            String dateInString = jsonObjectOuter.getString("otherdate")+" "+jsonObjectOuter.getString("time");
//                                                                            Date date = sdf.parse(dateInString);
//
//                                                                            System.out.println(dateInString);
//                                                                            System.out.println("Date - Time in milliseconds : " + date.getTime());
//
//                                                                            //  long xx = 1000 * 60 * 60 * 24;
//                                                                            long xx = 1000 * value;
//                                                                            long yy = date.getTime();
//
//
//                                                                            long millis = yy + xx;
//
//                                                                            Date expireDate = new Date(millis);
//
//
//                                                                            String dateT = sdf1.format(expireDate);
//                                                                            String timeT = sdf2.format(expireDate);
//
//
//                                                                            ItemFutureEstimation program = new ItemFutureEstimation();
//                                                                            program.setFirstTime(yy);
//                                                                            program.setSecondTime(millis);
//
//
//
//                                                                            String resultXXX = downloadUrlXXX(Settings.URL_MAIN_DATA+"partner_accepted_future_ride_list.php?driver_id="+jsonObjectOuter.getString("future_partner_id").toString());
//
//                                                                            Log.e(TAG, "resultdownloadUrl "+result);
//
//
//                                                                            setDataOfParner(resultXXX, arrayList, program);
//
////
//                                                                            arrayList.add(program);
//
//
//
//                                                                            Log.e(TAG , "SSSSSSS ");
//                                                                        }
//                                                                    }
//                                                                }
//                                                            }
//
//
//                                                            }catch (Exception e) {
//                                                            }
//
//
//
//
//
                    }
                }
            }
        }catch (Exception e){

        }

        return arrayList;
    }

    private void setDataOfParner(String resultXXX, ArrayList<ItemFutureEstimation> arrayList, ItemFutureEstimation program1) {
       // final ArrayList<ItemFutureEstimation> arrayList = new ArrayList<ItemFutureEstimation>();
        try
        {
            JSONObject jsonObject = new JSONObject(resultXXX);
            String status = jsonObject.getString("status");
            if(status.equalsIgnoreCase("1")){

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if(jsonArray.length() > 0){
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        final JSONObject jsonObjectOuter = jsonArray.getJSONObject(i);

                        if (jsonObjectOuter.has("second") && !jsonObjectOuter.getString("second").equalsIgnoreCase("")) {
                            int value = Integer.parseInt(jsonObjectOuter.getString("second"));

//                            int value = object3.getInt("value");
                            Log.e(TAG, "value " + value);

                            SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy hh:mm a");
                            SimpleDateFormat sdf1 = new SimpleDateFormat("M-dd-yyyy");
                            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
                            String dateInString = jsonObjectOuter.getString("otherdate") + " " + jsonObjectOuter.getString("time");
                            Date date = sdf.parse(dateInString);

                            System.out.println(dateInString);
                            System.out.println("Date - Time in milliseconds : " + date.getTime());

                            //  long xx = 1000 * 60 * 60 * 24;
                            long oneMinute = 60 * 1000;

                            long xx = 1000 * value;
                            long yy = date.getTime();


                            long millis = yy + xx +oneMinute;

                            Date expireDate = new Date(millis);




                            ItemFutureEstimation program = new ItemFutureEstimation();
                            program.setFirstTime(yy);
                            program.setSecondTime(millis);


//
                            arrayList.add(program);

                        }

//                        String api = Settings.GOOGLE_DESTINATIOON+"distancematrix/json?origins="
//                                +jsonObjectOuter.getString("pickup_lat")+","+jsonObjectOuter.getString("pickup_long")
//                                +"&destinations="+jsonObjectOuter.getString("d_lat")+","+jsonObjectOuter.getString("d_long")
//                                +"&key=AIzaSyDhDD6XROfPs4WkFmbTHvV1KqkNVP1kEmk";
//
//                        Log.e(TAG, "resultdownloadUrlapi "+api);
//
//
//                        String result = downloadUrl(api);
//
//                        Log.e(TAG, "resultdownloadUrl "+result);
//
//
//
//
//                        try {
//                            JSONObject object = new JSONObject(result);
//                            if(object.getString("status").equalsIgnoreCase("OK")){
//                                JSONArray jsonArrayQQ = object.getJSONArray("rows");
//                                if(jsonArrayQQ.length() > 0){
//                                    JSONObject object1 = jsonArrayQQ.getJSONObject(0);
//                                    JSONArray jsonArray2 = object1.getJSONArray("elements");
//                                    if(jsonArray2.length() > 0){
//                                        JSONObject object2 = jsonArray2.getJSONObject(0);
//                                        if(object2.getString("status").equalsIgnoreCase("OK")){
//                                            JSONObject object3 = object2.getJSONObject("duration");
//                                            int value = object3.getInt("value");
//                                            Log.e(TAG , "value "+value);
//
//                                            SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy hh:mm a");
//                                            SimpleDateFormat sdf1 = new SimpleDateFormat("M-dd-yyyy");
//                                            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
//                                            String dateInString = jsonObjectOuter.getString("otherdate")+" "+jsonObjectOuter.getString("time");
//                                            Date date = sdf.parse(dateInString);
//
//                                            System.out.println(dateInString);
//                                            System.out.println("Date - Time in milliseconds : " + date.getTime());
//
//                                            //  long xx = 1000 * 60 * 60 * 24;
//                                            long xx = 1000 * value;
//                                            long yy = date.getTime();
//
//
//                                            long millis = yy + xx;
//
//                                            Date expireDate = new Date(millis);
//
//
//                                            String dateT = sdf1.format(expireDate);
//                                            String timeT = sdf2.format(expireDate);
//
//
//                                            ItemFutureEstimation program = new ItemFutureEstimation();
//                                            program.setFirstTime(yy);
//                                            program.setSecondTime(millis);
//
//
//
////
//                                              arrayList.add(program);
//                                                //arrayList.add(program1);
//
//
//                                            Log.e(TAG , "SSSSSSS ");
//                                        }
//                                    }
//                                }
//                            }
//
//
//                        }catch (Exception e) {
//                        }


                    }
                }else{
                   // arrayList.add(program1);
                }
            }
        }catch (Exception e){

        }


    }


    public ArrayList<ItemFutureEstimation> getPartnerFutureEstimation(String responseCode) {
        final ArrayList<ItemFutureEstimation> arrayList = new ArrayList<ItemFutureEstimation>();
        try
        {
            JSONObject jsonObject = new JSONObject(responseCode);
            String status = jsonObject.getString("status");
            if(status.equalsIgnoreCase("1")){

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if(jsonArray.length() > 0){
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        final JSONObject jsonObjectOuter = jsonArray.getJSONObject(i);

                        if (jsonObjectOuter.has("second") && !jsonObjectOuter.getString("second").equalsIgnoreCase("")) {

                            int value = Integer.parseInt(jsonObjectOuter.getString("second"));
//                            int value = object3.getInt("value");
                            Log.e(TAG, "value " + value);

                            SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy hh:mm a");
                            SimpleDateFormat sdf1 = new SimpleDateFormat("M-dd-yyyy");
                            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
                            String dateInString = jsonObjectOuter.getString("otherdate") + " " + jsonObjectOuter.getString("time");
                            Date date = sdf.parse(dateInString);

                            System.out.println(dateInString);
                            System.out.println("Date - Time in milliseconds : " + date.getTime());

                            //  long xx = 1000 * 60 * 60 * 24;
                            long oneMinute = 60 * 1000;

                            long xx = 1000 * value;
                            long yy = date.getTime();


                            long millis = yy + xx + oneMinute;

                            Date expireDate = new Date(millis);


                            String dateT = sdf1.format(expireDate);
                            String timeT = sdf2.format(expireDate);


                            ItemFutureEstimation program = new ItemFutureEstimation();
                            program.setFirstTime(yy);
                            program.setSecondTime(millis);

//                                                                            program.setOtherdate(otherdate);
//                                                                            program.setTime(time);
//                                                                            program.setNewDate(dateT);
//                                                                            program.setNewTime(timeT);
//                                                                            program.setDuration(object3.getString("text"));
                            arrayList.add(program);

                        }

//                        String api = Settings.GOOGLE_DESTINATIOON+"distancematrix/json?origins="
//                                +jsonObjectOuter.getString("pickup_lat")+","+jsonObjectOuter.getString("pickup_long")
//                                +"&destinations="+jsonObjectOuter.getString("d_lat")+","+jsonObjectOuter.getString("d_long")
//                                +"&key=AIzaSyDhDD6XROfPs4WkFmbTHvV1KqkNVP1kEmk";
//
//                        Log.e(TAG, "resultdownloadUrlapi "+api);
//
//
//                        String result = downloadUrl(api);
//
//                        Log.e(TAG, "resultdownloadUrl "+result);
//
//
//
//
//                        try {
//                            JSONObject object = new JSONObject(result);
//                            if(object.getString("status").equalsIgnoreCase("OK")){
//                                JSONArray jsonArrayQQ = object.getJSONArray("rows");
//                                if(jsonArrayQQ.length() > 0){
//                                    JSONObject object1 = jsonArrayQQ.getJSONObject(0);
//                                    JSONArray jsonArray2 = object1.getJSONArray("elements");
//                                    if(jsonArray2.length() > 0){
//                                        JSONObject object2 = jsonArray2.getJSONObject(0);
//                                        if(object2.getString("status").equalsIgnoreCase("OK")){
//                                            JSONObject object3 = object2.getJSONObject("duration");
//                                            int value = object3.getInt("value");
//                                            Log.e(TAG , "value "+value);
//
//                                            SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy hh:mm a");
//                                            SimpleDateFormat sdf1 = new SimpleDateFormat("M-dd-yyyy");
//                                            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
//                                            String dateInString = jsonObjectOuter.getString("otherdate")+" "+jsonObjectOuter.getString("time");
//                                            Date date = sdf.parse(dateInString);
//
//                                            System.out.println(dateInString);
//                                            System.out.println("Date - Time in milliseconds : " + date.getTime());
//
//                                            //  long xx = 1000 * 60 * 60 * 24;
//                                            long xx = 1000 * value;
//                                            long yy = date.getTime();
//
//
//                                            long millis = yy + xx;
//
//                                            Date expireDate = new Date(millis);
//
//
//                                            String dateT = sdf1.format(expireDate);
//                                            String timeT = sdf2.format(expireDate);
//
//
//                                            ItemFutureEstimation program = new ItemFutureEstimation();
//                                            program.setFirstTime(yy);
//                                            program.setSecondTime(millis);
//
////                                                                            program.setOtherdate(otherdate);
////                                                                            program.setTime(time);
////                                                                            program.setNewDate(dateT);
////                                                                            program.setNewTime(timeT);
////                                                                            program.setDuration(object3.getString("text"));
//                                            arrayList.add(program);
//
//
//
////                                                                            ItemFutureEstimation program = new ItemFutureEstimation();
////                                                                            program.setOtherdate(otherdate);
////                                                                            program.setTime(time);
//
////                                                                            program.setPickup_lat(pickup_lat);
////                                                                            program.setPickup_long(pickup_long);
////                                                                            program.setD_lat(d_lat);
////                                                                            program.setD_long(d_long);
//
////                                                                            program.setNewDate(dateT);
////                                                                            program.setNewTime(timeT);
////
////                                                                            arrayList.add(program);
//
//
//
//                                            Log.e(TAG , "SSSSSSS ");
//                                        }
//                                    }
//                                }
//                            }
//
//
//                        }catch (Exception e) {
//                        }


                    }
                }
            }
        }catch (Exception e){

        }

        return arrayList;
    }





    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }





    private String downloadUrlXXX(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }





}
