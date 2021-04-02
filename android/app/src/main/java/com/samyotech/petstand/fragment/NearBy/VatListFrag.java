package com.samyotech.petstand.fragment.NearBy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.vat.PlaceDetailsActivity;
import com.samyotech.petstand.activity.vat.PlaceJSONParser;
import com.samyotech.petstand.utils.GPSTracker;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;


public class VatListFrag extends Fragment implements OnMapReadyCallback {
    private View view;
    private GoogleMap mMap;

    Spinner mSprPlaceType;
    String[] mPlaceType = null;
    String[] mPlaceTypeName = null;
    Button btnFind;
    public GPSTracker gps;
    double mLatitude, mLongitude;

    HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vat_list, container, false);
        gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {

            mLatitude = gps.getLatitude();
            mLongitude = gps.getLongitude();


            Log.e("Loction", "Lat" + mLatitude + "long" + mLongitude);
        } else {

            gps.showSettingsAlert();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Array of place types
        mPlaceType = getResources().getStringArray(R.array.place_type);

        // Array of place type names
        mPlaceTypeName = getResources().getStringArray(R.array.place_type_name);

        // Creating an array adapter with an array of Place types
        // to populate the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mPlaceTypeName);

        // Getting reference to the Spinner
        mSprPlaceType = (Spinner) view.findViewById(R.id.spr_place_type);

        // Setting adapter on Spinner to set place types
        mSprPlaceType.setAdapter(adapter);


        // Getting reference to Find Button
        btnFind = (Button) view.findViewById(R.id.btn_find);

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getBaseContext());
        btnFind.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                int selectedPosition = mSprPlaceType.getSelectedItemPosition();
                String type = mPlaceType[selectedPosition];


                StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                sb.append("location=" + mLatitude + "," + mLongitude);
                sb.append("&radius=5000");
                //sb.append("&types="+type);
                sb.append("&types=veterinary_care");
                sb.append("&sensor=true");
                sb.append("&key=AIzaSyBCR6Y0Qn_piKnlPhb6olCPdXglcFOfhaM");


                // Creating a new non-ui thread task to download Google place json data
                PlacesTask placesTask = new PlacesTask();

                // Invokes the "doInBackground()" method of the class PlaceTask
                placesTask.execute(sb.toString());


            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        btnFind.performClick();

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                Intent intent = new Intent(getActivity().getBaseContext(), PlaceDetailsActivity.class);
                String reference = mMarkerPlaceLink.get(arg0.getId());
                intent.putExtra("reference", reference);
                // Starting the Place Details Activity
                startActivity(intent);
            }
        });
    }


    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
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
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Erro downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }


    /**
     * A class, to download Google Places
     */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }

    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            // Clears all the existing markers
            mMap.clear();

            for (int i = 0; i < list.size(); i++) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");

                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                //This will be displayed on taping the marker
                markerOptions.title(name + " : " + vicinity);

                // Placing a marker on the touched position
                Marker m = mMap.addMarker(markerOptions);
                /*mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));*/

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(12).build();
                //Zoom in and animate the camera.
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                // Linking Marker id and place reference
                mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));


            }

        }

    }
}
