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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
Purpose: This class dictates the functionality behind adding a new 'activity' item to storage. An
         activity being an item that is going to be timed by the user.
*/


public class Create extends Activity {

    private List<TimedActivity> timedActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        timedActivities = new ArrayList<TimedActivity>();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create, menu);
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
        Purpose: Save an activity to storage in an unstarted state
        Input: Button click
        Output: New activity object in list in storage
     */
    public void save(View view) {
        timedActivities = getList();

        EditText nameEditText = (EditText)findViewById(R.id.nameText);
        RadioButton work = (RadioButton)findViewById(R.id.workRadio);
        RadioButton personal = (RadioButton)findViewById(R.id.personalRadio);
        RadioButton other = (RadioButton)findViewById(R.id.otherRadio);
        int selectedId = -1;
        if(work.isChecked())
        {
            selectedId = 0;
        }
        else if(personal.isChecked())
        {
            selectedId = 1;
        }
        else if(other.isChecked())
        {
            selectedId = 2;
        }

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        //if new activity has a unique name try to save it
        if(validateItemName(nameEditText.getText().toString(),timedActivities))
        {

            TimedActivity item = new TimedActivity(nameEditText.getText().toString(), selectedId);
            timedActivities.add(item);
            Toast toast;
            if(writeListToStorage())
            {
                toast = Toast.makeText(context, "Save successful!", duration);
                toast.show();
                Intent intent = new Intent(this, MyActivity.class);
                startActivity(intent);
            }
            else
            {
                toast = Toast.makeText(context, "A problem occurred.", duration);
                toast.show();
            }
        }
        else
        {
            Toast toast = Toast.makeText(context, "Please choose a unique name!", duration);
            toast.show();
        }
    }

    /*
        Purpose: Save an activity to storage in a started state
        Input: Button click
        Output: New activity object in list in storage in started state
     */
    public void saveAndStart(View view) {

        timedActivities = getList();

        EditText nameEditText = (EditText)findViewById(R.id.nameText);
        RadioButton work = (RadioButton)findViewById(R.id.workRadio);
        RadioButton personal = (RadioButton)findViewById(R.id.personalRadio);
        RadioButton other = (RadioButton)findViewById(R.id.otherRadio);
        int selectedId = -1;
        if(work.isChecked())
        {
            selectedId = 0;
        }
        else if(personal.isChecked())
        {
            selectedId = 1;
        }
        else if(other.isChecked())
        {
            selectedId = 2;
        }

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        RadioGroup group = (RadioGroup)findViewById(R.id.typeGroup);

        //if item name is unique try to save and start
        if(validateItemName(nameEditText.getText().toString(),timedActivities))
        {

            TimedActivity item = new TimedActivity(nameEditText.getText().toString(), selectedId, System.currentTimeMillis());
            timedActivities.add(item);
            Toast toast;
            if(writeListToStorage())
            {
                toast = Toast.makeText(context, "Save successful!", duration);
                toast.show();
                Intent intent = new Intent(this, MyActivity.class);
                startActivity(intent);
            }
            else
            {
                toast = Toast.makeText(context, "A problem occurred.", duration);
                toast.show();
            }
        }
        else
        {
            Toast toast = Toast.makeText(context, "Please choose a unique name!", duration);
            toast.show();
        }
    }

    /*
        Purpose: Read and get list from storage
        Input: None
        Output: List of activities
     */
    private List<TimedActivity> getList()
    {
        List<TimedActivity> timedActivities = new ArrayList<TimedActivity>();

        try
        {
            timedActivities =  (List<TimedActivity>)InternalStorage.readObject(getApplicationContext(), "timedActivitiesList");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.i("Create activity", " Get list io exception");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return timedActivities;
    }

    /*
        Purpose: Validates new activity name for uniqueness
        Input: Activity list and new name
        Output: validated flag
     */
    public boolean validateItemName(String name, List<TimedActivity> list)
    {
        for(TimedActivity item : list)
        {
            if(item.getName().equals(name))
            {
                return false;
            }
        }

        return true;
    }

    /*
        Purpose: Write list back to storage after adding new activity
        Input: None
        Output: Updated storage
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
            Log.i("Create Activity", "IOException on write");
            written = false;
        }

        return written;
    }

}
