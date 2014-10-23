package com.example.nick.timelogger;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
Purpose: Stats class that could be fleshed out much further. In current form it totals the time
         spent on each activity.
*/

public class stats extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        List<TimedActivity> timedActivities = new ArrayList<TimedActivity>();

        timedActivities =  getListFromStorage();

        List<String> totalTimes = new ArrayList<String>();
        totalTimes = getTotalTimes(timedActivities);

        TextView work = (TextView)findViewById(R.id.workText);
        TextView personal = (TextView)findViewById(R.id.personalText);
        TextView other = (TextView)findViewById(R.id.otherText);
        work.setText(totalTimes.get(0));
        personal.setText(totalTimes.get(1));
        other.setText(totalTimes.get(2));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public List <String> getTotalTimes(List<TimedActivity> timedActivities)
    {
        long work = 0;
        long personal = 0;
        long other = 0;
        List <String> totalTimes = new ArrayList<String>();

        for(int i = 0; i < timedActivities.size(); ++i)
        {
            if(timedActivities.get(i).getType().equals("Work"))
            {
                work = work + timedActivities.get(i).getElapsedTimeLong();
            }
            else if(timedActivities.get(i).getType().equals("Personal"))
            {
                personal = personal + timedActivities.get(i).getElapsedTimeLong();
            }
            else if(timedActivities.get(i).getType().equals("Other"))
            {
                other = other + timedActivities.get(i).getElapsedTimeLong();
            }
        }

        totalTimes.add(formatInterval(work));
        totalTimes.add(formatInterval(personal));
        totalTimes.add(formatInterval(other));

        return totalTimes;
    }

    private String formatInterval(long l)
    {
        final long hr = TimeUnit.MILLISECONDS.toHours(l);
        final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
        final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
        return String.format("%02d:%02d:%02d", hr, min, sec);
    }

    /*
        Purpose: Get list from storage
        Input: None
        Output: List from storage
     */
    private List<TimedActivity> getListFromStorage()
    {
        List<TimedActivity> timedActivities = new ArrayList<TimedActivity>();

        try
        {
            timedActivities =  (List<TimedActivity>)InternalStorage.readObject(getApplicationContext(), "timedActivitiesList");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.i("Item Activity", "IOException on initial read");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return timedActivities;
    }
}
