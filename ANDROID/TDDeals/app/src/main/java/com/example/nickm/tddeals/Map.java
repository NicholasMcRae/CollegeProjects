package com.example.nickm.tddeals;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/*
Purpose: Logic for Map fragment over relative layout. Using Google Places API to eventually pull
         back data and compare against deal data that would theoretically be set up by TD, as well
         as custom purchase data to find custom deals for the customer.
*/


public class Map extends FragmentActivity {

    private GoogleMap mMap;
    private double myLatitude;
    private double myLongitude;
    private GPSService gpsService;
    private boolean subscribed = false;
    ProgressDialog pDialog;
    GooglePlaces googlePlaces;
    PlacesList nearPlaces;
    ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
    AlertDialogManager alert = new AlertDialogManager();

    public static String KEY_REFERENCE = "reference";
    public static String KEY_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setUpMapIfNeeded();

        //successfully pulls back current location... somewhere in Iceland due to emulator
        updateLocation();

        //LatLng latLng = new LatLng(gpsService.getLatitude(), gpsService.getLongitude());
        //Using static co-ordinates for now as emulator is not returning local GPS
        LatLng latLng = new LatLng(42.986950, -81.243177);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        mMap.animateCamera(cameraUpdate);

        PlacesList nearPlaces = new PlacesList();

        //Get surrounding places asynchronously
        new LoadPlaces().execute();

        //Check if user is subscribed
        SharedPreferences settings = getSharedPreferences("state",Context.MODE_PRIVATE);
        subscribed = settings.getBoolean("subscribed",false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    //If user has never subscribed before show subscribe dialog on start
    @Override
    protected void onStart() {
        super.onStart();

        if(!subscribed)
            subscribeDialog();
    }

    /**
     * Note from Android API:
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        //Custom marker popup adapter
        mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
    }

    //Purpose: find all surrounding matches with any and all TD deals
    public void markAllDeals(View view)
    {
        //If user isn't subscribed do not run function
        if(!subscribed) {
            Context context = getApplicationContext();
            Toast toast;
            int duration = Toast.LENGTH_LONG;
            toast = Toast.makeText(context, "You must subscribe on app startup.", duration);
            toast.show();
            return;
        }

        //refresh map on new click
        mMap.clear();

        //Instantiate deal object
        JSONObject deals = new JSONObject();
        try {
            deals = new JSONObject(getString(getApplicationContext(), "deals.txt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //iterate through surrounding places and find locations that match with deals
        double latitude, longitude;
        if(nearPlaces.results != null) {
            for(Place place : nearPlaces.results) {
                Iterator<String> iter = deals.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    String desc = "";
                    String expiry = "";
                    if(key.equals(place.name)){
                        try {
                            JSONObject obj = deals.getJSONObject(key);
                            desc = obj.getString("Description");
                            expiry = obj.getString("Expires");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        latitude = place.geometry.location.lat;
                        longitude = place.geometry.location.lng;
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(place.name)
                                .snippet(desc + "\n" + expiry));
                    }
                }
            }
        }
    }

    //Purpose: populate map with markers that relate to customer's financial data
    public void markCustomDeals(View view) {

        //If not subscribed do not run function
        if(!subscribed) {
            Context context = getApplicationContext();
            Toast toast;
            int duration = Toast.LENGTH_LONG;
            toast = Toast.makeText(context, "You must subscribe on app start-up.", duration);
            toast.show();
            return;
        }

        //refresh map
        mMap.clear();

        //Find all deals that match with TD financial data and populate customMerchants
        List<String> customMerchants = getCustomDeals();

        //Instantiate deal JSON
        JSONObject deals = new JSONObject();
        try {
            deals = new JSONObject(getString(getApplicationContext(), "deals.txt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //If place is returned and matches deal + financial data put marker on map
        double latitude, longitude;
        if(nearPlaces.results != null) {
            for(Place place : nearPlaces.results) {
                for(String customMerchant : customMerchants)
                {
                    if(place.name.equals(customMerchant))
                    {
                        String desc = "";
                        String expiry = "";
                        try {
                            JSONObject obj = deals.getJSONObject(customMerchant);
                            desc = obj.getString("Description");
                            expiry = obj.getString("Expires");
                        } catch (JSONException e) {
                            Log.e("All Deals", e.getMessage());
                        }
                        latitude = place.geometry.location.lat;
                        longitude = place.geometry.location.lng;
                        String text = desc + "\n" + expiry;

                        //Create marker with custom bubble
                        addMarker(mMap, latitude, longitude, place.name, text);
                    }
                }
            }
        }
    }

    //Purpose: populate list with deals that match financial data
    private List<String> getCustomDeals()
    {
        JSONObject customerData = new JSONObject();
        JSONObject deals = new JSONObject();
        List<String> customDeals = new ArrayList();

        //Instantiate deals and customerdata object
        try {
            customerData = new JSONObject(getString(getApplicationContext(), "customerdata.txt"));
            deals = new JSONObject(getString(getApplicationContext(), "deals.txt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Iterator<String> dealIter = deals.keys();
        Iterator<String> transactions = null;

        //while there are more deals
        while (dealIter.hasNext()) {

            //capture deal business
            String dealLocale = dealIter.next();

            //Get transaction keys for customer 1
            try {
                transactions = customerData.getJSONObject("Customer 01").getJSONObject("Transactions").keys();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //iterating over transaction keys to find match between deals and transactions
            while (transactions.hasNext()) {
                String key = transactions.next();
                JSONObject transaction = null;

                //Getting transaction object
                try {
                    transaction = customerData.getJSONObject("Customer 01").getJSONObject("Transactions").getJSONObject(key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //get merchant from transaction key
                String merchant = null;
                try {
                    if (transaction != null) {
                        merchant = transaction.getString("Merchant");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //if we find a match between deal location and financial data add to the list and break inner loop
                if(dealLocale.equals((merchant))){
                    customDeals.add(dealLocale);
                    break; //if a matching transaction has been found we don't need to look at more transactions
                }
            }
        }

        return customDeals;
    }

    //Show subscribe dialog on app startup if user has never agreed to terms
    public void subscribeDialog()
    {
        new AlertDialog.Builder(this)
                .setTitle("Subscribe")
                .setMessage("Do you allow TD to use your location data? You must agree to use this application")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setSubscribedFlag();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = getApplicationContext();
                        Toast toast;
                        int duration = Toast.LENGTH_LONG;
                        toast = Toast.makeText(context, "Not subscribed!", duration);
                        toast.show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    //If user agrees to terms save true value to avoid further popups and ensure use of the app
    private void setSubscribedFlag()
    {
        SharedPreferences settings = getSharedPreferences("state", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("subscribed", true);
        editor.commit();
        subscribed = true;
    }

    //Convert txt file to string to construct JSON objects
    private String getString(Context context, String file)
    {
        String str = "";
        try
        {
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open(file);
            InputStreamReader isr = new InputStreamReader(in);
            char [] inputBuffer = new char[100];

            int charRead;
            while((charRead = isr.read(inputBuffer))>0)
            {
                String readString = String.copyValueOf(inputBuffer,0,charRead);
                str += readString;
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

        return str;
    }

    //pulls back user's current location
    private void updateLocation(){
        gpsService = new GPSService(getApplicationContext());
        Location location = gpsService.getLocation();
        while(!gpsService.isLocationAvailable)
        {
            location = gpsService.getLocation();
        }
    }

    //add marker to the map
    private void addMarker(GoogleMap map, double lat, double lon,
                           String title, String snippet) {
        map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(title)
                .snippet(snippet));
    }

    /*
        Code found in Google Places API tutorial. Interfaces with the Google Places API and retrieves
        location data asynchronously.
     */
    class LoadPlaces extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Map.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Places JSON
         * */
        protected String doInBackground(String... args) {
            // creating Places class object
            googlePlaces = new GooglePlaces();

            try {
                // Separeate your place types by PIPE symbol "|"
                // If you want all types places make it as null
                // Check list of types supported by google
                String types = null;

                // Radius in meters - increase this value if you don't find any places
                double radius = 1000; // 1000 meters

                //using static location data as the emulator doesn't focus on real location
                nearPlaces = googlePlaces.search(42.986950,-81.243177, radius, types);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * and show the data in UI
         * Always use runOnUiThread(new Runnable()) to update UI from background
         * thread, otherwise you will get error
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed Places into LISTVIEW
                     * */
                    // Get json response status
                    String status = nearPlaces.status;

                    // Check for all possible status
                    if(status.equals("OK")){
                        // Successfully got places details
                        if (nearPlaces.results != null) {
                            // loop through each place
                            for (Place p : nearPlaces.results) {
                                HashMap<String, String> map = new HashMap<String, String>();

                                // Place reference won't display in listview - it will be hidden
                                // Place reference is used to get "place full details"
                                map.put(KEY_REFERENCE, p.reference);

                                // Place name
                                map.put(KEY_NAME, p.name);

                                // adding HashMap to ArrayList
                                placesListItems.add(map);
                            }
                        }
                    }
                    else if(status.equals("ZERO_RESULTS")){
                        // Zero results found
                        alert.showAlertDialog(Map.this, "Near Places",
                                "Sorry no places found. Try to change the types of places",
                                false);
                    }
                    else if(status.equals("UNKNOWN_ERROR"))
                    {
                        alert.showAlertDialog(Map.this, "Places Error",
                                "Sorry unknown error occured.",
                                false);
                    }
                    else if(status.equals("OVER_QUERY_LIMIT"))
                    {
                        alert.showAlertDialog(Map.this, "Places Error",
                                "Sorry query limit to google places is reached",
                                false);
                    }
                    else if(status.equals("REQUEST_DENIED"))
                    {
                        alert.showAlertDialog(Map.this, "Places Error",
                                "Sorry error occured. Request is denied",
                                false);
                    }
                    else if(status.equals("INVALID_REQUEST"))
                    {
                        alert.showAlertDialog(Map.this, "Places Error",
                                "Sorry error occured. Invalid Request",
                                false);
                    }
                    else
                    {
                        alert.showAlertDialog(Map.this, "Places Error",
                                "Sorry error occured.",
                                false);
                    }
                }
            });

        }

    }
}
