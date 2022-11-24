package com.lncdriver.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class SavePref {
    private static final String TAG = "SavePref";
    Context con;

    SharedPreferences preferences = null;
    Editor editor = null;

    public void clear() {
//		 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//	    editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public void SavePref(Context c) {
        con = c;
        preferences = PreferenceManager.getDefaultSharedPreferences(con);
        editor = preferences.edit();
    }

    public void setName(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("setName", string);
        editor.commit();
        //  System.out.println("val save email ");
    }

    public String getName() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("setName", "");

        return name;
    }

    public void setEmail(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("setEmail", string);
        editor.commit();
        //  System.out.println("val save email ");


    }


    public String getEmail() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("setEmail", "");


        return name;
    }


    public void setisnewmsg(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("isnewmsg", string);
        editor.commit();
        //  System.out.println("val save email ");

        //Log.d(TAG, "getUserId received");
    }

    public String getIsNewMsg() {
        //preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("isnewmsg", "");
        //Log.d(TAG, "getUserId received "+name);
        return name;
    }

    public void setIsFNewMsg(String string) {
        editor.putString("isFNewMsg", string);
        editor.commit();
    }

    public String getIsFNewMsg() {
        //preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("isFNewMsg", "");
        //Log.d(TAG, "getUserId received "+name);
        return name;
    }


    public void setMobile(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("setMobile", string);
        editor.commit();
        //  System.out.println("val save email ");


    }

    public String getMobile() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("setMobile", "");


        return name;
    }


    public void setImage(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("setimage", string);
        editor.commit();
        //  System.out.println("val save email ");


    }

    public String getImage() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("setimage", "");


        return name;
    }

    public void setUserFName(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("FName", string);
        editor.commit();
        //  System.out.println("val save email ");


    }

    public String getUserFName() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("FName", "");


        return name;
    }

    public void setUserLName(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("LName", string);
        editor.commit();
        //  System.out.println("val save email ");
    }

    public String getUserLName() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("LName", "");
        return name;
    }

    public void setUserId(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("getUserId", string);
        editor.commit();
        //  System.out.println("val save email ");
    }

    public String getUserId() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("getUserId", "");
        return name;
    }

    public void setDriverId(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("DriverId", string);
        editor.commit();
        //  System.out.println("val save email ");

    }

    public String getDriverId() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("DriverId", "");
        return name;
    }

    public void setRating(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("Rating", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getRating() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("Rating", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setdToken(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("devicetoken", string);
        editor.commit();
        //  System.out.println("val save email ");
    }

    public String getdToken() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("devicetoken", "");

        return name;
    }

    public void setdrivertype(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("drivertype", string);
        editor.commit();
        //  System.out.println("val save email ");

        //Log.d(TAG, "getUserId received");
    }

    public String getdrivertype() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("drivertype", "");

        //Log.d(TAG, "getUserId received "+name);

        return name;
    }

    public void setridemap(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("ridemap", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getridemap() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("ridemap", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setCustomerId(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("CustomerId", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getCustomerId() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("CustomerId", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setcustomermap(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("customermap", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getcustomermap() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("customermap", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setisnewride(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("isnewride", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getisnewride() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("isnewride", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setRideId(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("RideId", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getRideId() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("RideId", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public void setaccepttype(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("accepttype", string);
        editor.commit();
        //  System.out.println("val save email ");

        Log.d(TAG, "getUserId received");
    }

    public String getaccepttype() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("accepttype", "");

        Log.d(TAG, "getUserId received " + name);

        return name;
    }

    public String getisridestart() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("isridestart", "");

        //Log.d(TAG, "getUserId received "+name);

        return name;
    }

    public void setisridestart(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("isridestart", string);
        editor.commit();
        //  System.out.println("val save email ");

        //Log.d(TAG, "getUserId received");
    }


    public String getisfridestart() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("isfridestart", "");

        //Log.d(TAG, "getUserId received "+name);

        return name;
    }

    public void setisfridestart(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("isfridestart", string);
        editor.commit();
        //  System.out.println("val save email ");

        //Log.d(TAG, "getUserId received");
    }


    public String getispartnerAuth() {
        // TODO Auto-generated method stub
        //	 preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String name = preferences.getString("ispartnerAuth", "");

        //Log.d(TAG, "getUserId received "+name);
        return name;
    }

    public void setispartnerAuth(String string) {
        // TODO Auto-generated method stub
//			 preferences = PreferenceManager.getDefaultSharedPreferences(con);
//			   editor = preferences.edit();
        editor.putString("ispartnerAuth", string);
        editor.commit();
        //  System.out.println("val save email ");

        //Log.d(TAG, "getUserId received");
    }








    public void setBadgeCount(int string) {
        // TODO Auto-generated method stub
        editor.putInt("getBadgeCount", string);
        editor.commit();
    }

    public int getBadgeCount() {
        int name = preferences.getInt("getBadgeCount", 0);
        return name;
    }

}
