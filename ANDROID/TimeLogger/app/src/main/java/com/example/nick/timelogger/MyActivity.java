package com.example.nick.timelogger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.nick.timelogger.TimedActivity;

/*
Purpose: Main activity page used to navigate to other areas of functionality of the app.
*/

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<TimedActivity> timedActivities = new ArrayList<TimedActivity>();
        boolean listNotWritten = false;

        /*
            Here's some code that ensures we have a list written to storage. When the app starts
            up it does an initial read and if an exception is thrown it sets a flag that causes
            the list to be initialized. I assume there's a better way to do this, but that's
            all I got : - ).
         */
        try
        {
          timedActivities =  (List<TimedActivity>)InternalStorage.readObject(getApplicationContext(), "timedActivitiesList");
        }
         catch (IOException e)
         {
            e.printStackTrace();
            Log.i("Main Activity", "IOException on initial read");
            listNotWritten = true;
         }
         catch (ClassNotFoundException e)
         {
            e.printStackTrace();
         }

        if(listNotWritten)
        {
            try
            {
                InternalStorage.writeObject(getApplicationContext(), "timedActivitiesList", timedActivities);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Log.i("Main Activity", "IOException on initial write");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

    public void goToCreate(View view) {
        Intent intent = new Intent(this, Create.class);
        startActivity(intent);
    }

    public void goToActivities(View view) {
        Intent intent = new Intent(this, itemTable.class);
        startActivity(intent);
    }

    public void goToStats(View view) {
        Intent intent = new Intent(this, stats.class);
        startActivity(intent);
    }

    public void goToAbout(View view) {
        Intent intent = new Intent(this, about2.class);
        startActivity(intent);
    }
}
