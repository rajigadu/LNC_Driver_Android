package com.lncdriver;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Demo extends AppCompatActivity {

    private static final String TAG = "Demo";
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo);

        button = (Button) findViewById(R.id.button11);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                String myDate = "09-24-2019";
//                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
//                Date date = sdf.parse(myDate);
//
//                long xx = 1000 * 60 * 12 * 7 * 4;
//
//                long millis = date.getTime()+xx;
//
//                    Log.e(TAG , "millis "+millis);
//
//                    Date result = new Date(millis);
//
//                    Log.e(TAG , "TIme "+sdf.format(result));
//
//                    System.out.println(sdf.format(result));
//
//
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTimeInMillis(millis);
//                    System.out.println(sdf.format(calendar.getTime()));



                    SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy hh:mm a");
                    String dateInString = "09-24-2019 10:20 PM";
                    Date date = sdf.parse(dateInString);

                    System.out.println(dateInString);
                    System.out.println("Date - Time in milliseconds : " + date.getTime());

                  //  long xx = 1000 * 60 * 60 * 24;
                    long xx = 1000 * 10375;
                    long millis = date.getTime()+xx;

                    Date expireDate = new Date(millis);



//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTime(date);
//                    System.out.println("Calender - Time in milliseconds : " + calendar.getTimeInMillis());
//
                    System.out.println("Calender - Time in milliseconds : " + expireDate.getTime());
                    System.out.println(sdf.format(expireDate));



//                    int dd [] = {2,5,76,3,55,1};
//
//                    int index = 0;
//
//                    int max = dd[0];
//
//                    for (int i = 1; i < dd.length; i++){
//                        if (dd[i] > max){
//                            max = dd[i];
//                            index = i;
//                        }
//                    }
//
//
//
//                    Log.e(TAG , "large "+max);
//                    Log.e(TAG , "index "+index);




//                    int aaa = 45;
//
//                    if (aaa < 50 && aaa > 0){
//                        Log.e(TAG , "aaa111 "+aaa);
//
//                    }else{
//                        Log.e(TAG , "aaa222 "+aaa);
//                    }




//                    int a = 5;
//                    int b = 10;
//
//                    a = a+b;
//
//
//                    b = a-b;
//                    a = a-b;
//
//
//                    Log.e(TAG , "a "+a);
//                    Log.e(TAG , "b "+b);



                    int[] dd = {2,5,76,3,55,1};

                    int index = 0;

                    int max = dd[0];
                    int secondlast = dd[0];

                    for (int i = 1; i < dd.length; i++){
                        if (dd[i] > max){
                            secondlast = max;
                            max = dd[i];
                            index = i;
                        }else if(dd[i] > secondlast){
                            secondlast = dd[i];
                        }
                    }



                    Log.e(TAG , "large "+max);
                    Log.e(TAG , "index "+index);
                    Log.e(TAG , "secondlast "+secondlast);







                } catch (ParseException e) {
                    e.printStackTrace();
                }





            }
        });
    }
}
