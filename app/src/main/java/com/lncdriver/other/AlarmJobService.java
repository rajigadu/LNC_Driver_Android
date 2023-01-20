package com.lncdriver.other;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AlarmJobService extends JobService
{
    private static final String TAG = "JobSchedulerService";

    @Override
    public boolean onStartJob(JobParameters params)
    {
        Log.e(TAG, "onStartJob:");

        //Reschedule the Service before calling job finished
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            scheduleRefresh();

        //Call Job Finished
        jobFinished(params,true);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params)
    {
        Log.i(TAG, "onStopJob:");
        return false;
    }

    private void scheduleRefresh()
    {
        JobScheduler mJobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo.Builder mJobBuilder = new JobInfo.Builder(123,new ComponentName(getPackageName(),AlarmJobService.class.getName()));

        /* For Android N and Upper Versions */

            final JobScheduler jobScheduler;
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            {
                jobScheduler = (JobScheduler) getSystemService( Context.JOB_SCHEDULER_SERVICE);
                final ComponentName name = new ComponentName(this, AlarmJobService.class);
                final int result = jobScheduler.schedule(getJobInfo(123, 1, name));

                if(result == JobScheduler.RESULT_SUCCESS)
                {
                    Log.d("", "Scheduled job successfully!");
                }
            }
    }

    private JobInfo getJobInfo(final int id,final long hour,final ComponentName name)
    {
        final JobInfo jobInfo;
        final long interval = 5000;
        final boolean isPersistent = true;
        final int networkType = JobInfo.NETWORK_TYPE_ANY;

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N)
        {
            Log.e("767878687", "below=====:");
            jobInfo = new JobInfo.Builder(id, name)
                    .setMinimumLatency (interval)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .build();
        }
        else
        {
            Log.e("687687687", "Above=====:");
            jobInfo = new JobInfo.Builder(id, name)
                    .setMinimumLatency (interval)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .build();
        }
        return jobInfo;
    }
}
