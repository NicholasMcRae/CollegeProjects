package com.example.nick.timelogger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
Purpose: This class dictates the logic behind the clickable activity table. When a new activity is created
         it is stored in a list, and that list is used to populate this table. The activities on this
         table can be clicked to bring the user to the update page.
*/

public class itemTable extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_table);

        RelativeLayout.LayoutParams tableParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        TableLayout tab = (TableLayout)findViewById(R.id.itemTable);
        tab.setLayoutParams(tableParams);

        TableRow header = new TableRow(getApplicationContext());
        header.setLayoutParams(rowParams);

        TextView itemName = new TextView(getApplicationContext());
        itemName.setLayoutParams(rowParams);
        itemName.setText("Activity Name");
        itemName.setTextColor(Color.BLACK);
        itemName.setTextSize(16f);
        itemName.setTypeface(Typeface.DEFAULT);
        itemName.setPadding(5, 5, 5, 5);
        itemName.setMinWidth(75);
        itemName.setGravity(Gravity.CENTER);

        TextView status = new TextView(getApplicationContext());
        status.setLayoutParams(rowParams);
        status.setText("Status");
        status.setTextColor(Color.BLACK);
        status.setTextSize(16f);
        status.setTypeface(Typeface.DEFAULT);
        status.setPadding(5, 5, 5, 5);
        status.setMinWidth(75);
        status.setGravity(Gravity.CENTER);

        TextView elapsed = new TextView(getApplicationContext());
        elapsed.setLayoutParams(rowParams);
        elapsed.setText("Elapsed Time");
        elapsed.setTextColor(Color.BLACK);
        elapsed.setTextSize(16f);
        elapsed.setTypeface(Typeface.DEFAULT);
        elapsed.setPadding(5, 5, 5, 5);
        elapsed.setMinWidth(75);
        elapsed.setGravity(Gravity.CENTER);

        header.addView(itemName, 0);
        header.addView(status, 1);
        header.addView(elapsed, 2);

        tab.addView(header);

        List<TimedActivity> timedActivities = new ArrayList<TimedActivity>();

        timedActivities =  getListFromStorage();

        /*
            Creates clickable list of activity objects that move us to the page where we
            can update them.
         */

        for(int i = 0; i < timedActivities.size(); i++)
        {
            TableRow itemRow = new TableRow(getApplicationContext());
            itemRow.setLayoutParams(rowParams);

            itemName = new TextView(getApplicationContext());
            itemName.setLayoutParams(rowParams);
            itemName.setText(timedActivities.get(i).getName());
            itemName.setTextColor(Color.BLACK);
            itemName.setTextSize(16f);
            itemName.setTypeface(Typeface.DEFAULT);
            itemName.setPadding(5, 5, 5, 5);
            itemName.setMinWidth(75);
            itemName.setGravity(Gravity.CENTER);

            status = new TextView(getApplicationContext());
            status.setLayoutParams(rowParams);
            status.setText(timedActivities.get(i).getStatus());
            status.setTextColor(Color.BLACK);
            status.setTextSize(16f);
            status.setTypeface(Typeface.DEFAULT);
            status.setPadding(5, 5, 5, 5);
            status.setMinWidth(75);
            status.setGravity(Gravity.CENTER);

            elapsed = new TextView(getApplicationContext());
            elapsed.setLayoutParams(rowParams);
            elapsed.setText(timedActivities.get(i).getElapsedTime());
            elapsed.setTextColor(Color.BLACK);
            elapsed.setTextSize(16f);
            elapsed.setTypeface(Typeface.DEFAULT);
            elapsed.setPadding(5, 5, 5, 5);
            elapsed.setMinWidth(75);
            elapsed.setGravity(Gravity.CENTER);

            itemRow.addView(itemName, 0);
            itemRow.addView(status, 1);
            itemRow.addView(elapsed, 2);
            final int id = i;
            itemRow.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(v.getContext(), Item.class);
                    myIntent.putExtra("tableId", id);
                    itemTable.this.startActivity(myIntent);
                }
            });
            tab.addView(itemRow);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_table, menu);
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
