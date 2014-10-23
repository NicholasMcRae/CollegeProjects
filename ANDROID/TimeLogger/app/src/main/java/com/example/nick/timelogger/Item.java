package com.example.nick.timelogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
Purpose: This class dictates the functionality behind updating the activity object. With the logic
         contained a user can start, stop, pause, or delete an activity for update in storage.
*/

public class Item extends Activity {

    private List<TimedActivity> timedActivities;
    private TimedActivity timedActivity;
    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        //pull in id from object table to get pressed activity
        Bundle b = getIntent().getExtras();
        index = b.getInt("tableId");

        timedActivities = new ArrayList<TimedActivity>();
        timedActivities = getListFromStorage();

        timedActivity = timedActivities.get(index);

        //set some of the text fields on the item page with activity details
        TextView activityName = (TextView)findViewById(R.id.activityName);
        activityName.setText(timedActivity.getName());

        TextView elapsed = (TextView)findViewById(R.id.elapsed);
        elapsed.setText(timedActivity.getElapsedTime());

        TextView stat = (TextView)findViewById(R.id.status);
        stat.setText(timedActivity.getStatus());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item, menu);
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

    /*
        Purpose: Logic that occurs when the start button on this page is pressed
        Input: Button click
        Output: Updated (or not) activity status
     */
    public void startActivity(View view)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        if(timedActivity.getStarted())
        {
            Toast toast = Toast.makeText(context, "Activity already started!", duration);
            toast.show();
        }
        else if(!timedActivity.getStarted() && !timedActivity.getPaused() && !timedActivity.getFinished())
        {
            timedActivity.setStartTime(System.currentTimeMillis());
            timedActivity.setStarted(true);
            getTimedActivities().set(index, timedActivity);
            Toast toast;
            if(writeListToStorage())
            {
                toast = Toast.makeText(context, "Activity started!", duration);
            }
            else
            {
                toast = Toast.makeText(context, "Problem occurred!", duration);
            }

            toast.show();
        }
        else if(timedActivity.getPaused())
        {
            timedActivity.addPauseTime(System.currentTimeMillis());
            timedActivity.setStarted(true);
            timedActivity.setPaused(false);
            getTimedActivities().set(index, timedActivity);
            Toast toast;
            if(writeListToStorage())
            {
                toast = Toast.makeText(context, "Activity started!", duration);
            }
            else
            {
                toast = Toast.makeText(context, "Problem occurred.", duration);
            }

            toast.show();
        }
        else if(timedActivity.getFinished())
        {
            timedActivity.addPauseTime(timedActivity.getEndTime());
            timedActivity.addPauseTime(System.currentTimeMillis());
            timedActivity.setEndTime(0);
            timedActivity.setStarted(true);
            timedActivity.setFinished(false);
            getTimedActivities().set(index, timedActivity);
            Toast toast;
            if(writeListToStorage())
            {
                toast = Toast.makeText(context, "Activity started!", duration);
            }
            else
            {
                toast = Toast.makeText(context, "Problem occurred!", duration);
            }

            toast.show();
        }
    }

    /*
        Purpose: Logic that occurs when the stop button on this page is pressed
        Input: Button click
        Output: Updated (or not) activity status
     */
    public void stopActivity(View view)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        if(timedActivity.getStarted())
        {
            timedActivity.setEndTime(System.currentTimeMillis());
            timedActivity.setStarted(false);
            timedActivity.setFinished(true);
            getTimedActivities().set(index, timedActivity);
            Toast toast;
            if(writeListToStorage())
            {
                toast = Toast.makeText(context, "Activity stopped!", duration);
            }
            else
            {
                toast = Toast.makeText(context, "Problem occurred!", duration);
            }

            toast.show();
        }
        else if(timedActivity.getPaused())
        {
            timedActivity.setEndTime(timedActivity.getPauseTimeList()[timedActivity.getPauseTimeList().length -1]);
            timedActivity.addPauseTime(timedActivity.getEndTime());
            timedActivity.setPaused(false);
            timedActivity.setFinished(true);
            getTimedActivities().set(index, timedActivity);
            Toast toast;
            if(writeListToStorage())
            {
                toast = Toast.makeText(context, "Activity stopped!", duration);

            }
            else
            {
                toast = Toast.makeText(context, "Problem occurred.", duration);
            }

            toast.show();
        }
        else if(timedActivity.getFinished())
        {
            Toast toast = Toast.makeText(context, "Activity already stopped!", duration);
            toast.show();
        }
        else if(!timedActivity.getStarted() && !timedActivity.getPaused() && !timedActivity.getFinished())
        {
            Toast toast = Toast.makeText(context, "Activity not started yet!", duration);
            toast.show();
        }
    }

    /*
        Purpose: Logic that occurs when the pause button on this page is pressed
        Input: Button click
        Output: Updated (or not) activity status
     */
    public void pauseActivity(View view)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        if(timedActivity.getStarted())
        {
            timedActivity.addPauseTime(System.currentTimeMillis());
            timedActivity.setStarted(false);
            timedActivity.setPaused(true);
            getTimedActivities().set(index, timedActivity);
            Toast toast;
            if(writeListToStorage())
            {
                toast = Toast.makeText(context, "Activity paused!", duration);
            }
            else
            {
                toast = Toast.makeText(context, "Problem occurred!", duration);
            }

            toast.show();
        }
        else if(timedActivity.getPaused())
        {
            Toast toast = Toast.makeText(context, "Activity already paused!", duration);
            toast.show();
        }
        else if(timedActivity.getFinished())
        {
            Toast toast = Toast.makeText(context, "Activity stopped!", duration);
            toast.show();
        }
        else if(!timedActivity.getStarted() && !timedActivity.getPaused() && !timedActivity.getFinished())
        {
            Toast toast = Toast.makeText(context, "Activity not started yet!", duration);
            toast.show();
        }
    }

    /*
        Purpose: Delete activity
        Input: Button click
        Output: Updated list in internal storage
     */
    public void deleteActivity(View view)
    {
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delete(index);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = getApplicationContext();
                        Toast toast;
                        int duration = Toast.LENGTH_SHORT;
                        toast = Toast.makeText(context, "Activity not deleted!", duration);
                        toast.show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    private List<TimedActivity> getTimedActivities()
    {
        return timedActivities;
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

    /*
        Purpose: Write list back to storage
        Input: None
        Output: Write status
     */
    private boolean writeListToStorage()
    {
        boolean written = true;

        try
        {
            InternalStorage.writeObject(getApplicationContext(), "timedActivitiesList", timedActivities);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.i("Item Activity", "IOException on write");
            written = false;
        }

        return written;
    }

    private void delete(int index)
    {
        timedActivities.remove(index);
        Toast toast;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        if(writeListToStorage())
        {
            toast = Toast.makeText(context, "Activity deleted!", duration);
            toast.show();
        }
        else
        {
            toast = Toast.makeText(context, "Problem occurred!", duration);
            toast.show();
        }
    }
}
