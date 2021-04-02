package com.samyotech.petstand.activity.vat;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.samyotech.petstand.R;
import com.samyotech.petstand.models.NearByDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.Locale;

public class PlaceDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private CustomTextView ctvTime,ctvETime, tvName, tvMobile, tvAddress, tvAddress1, tvUrl;
    private ImageView ivProfile;
    private LinearLayout back;
    public static int PHONE_PERMISSION_CONSTANT = 102;
    String lat = "", lng = "", name = "", formatted_phone = "";
    LinearLayout LLphone;
    NearByDTO nearByDTO;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(PlaceDetailsActivity.this);
        setContentView(R.layout.place_detail);
        nearByDTO = (NearByDTO) getIntent().getSerializableExtra(Consts.NEAR_BY);

        tvName = findViewById(R.id.tvName);
        tvMobile = findViewById(R.id.tvMobile);
        tvAddress = findViewById(R.id.tvAddress);
        tvAddress1 = findViewById(R.id.tvAddress1);
        tvUrl = findViewById(R.id.tvUrl);
        ivProfile = findViewById(R.id.ivProfile);
        back = findViewById(R.id.back);
        LLphone = findViewById(R.id.LLphone);
        ctvTime = findViewById(R.id.ctvTime);
        ctvETime = findViewById(R.id.ctvETime);

        tvName.setText(nearByDTO.getName());
        tvMobile.setText(nearByDTO.getMobile());
        tvAddress.setText(nearByDTO.getAddress());
        tvAddress1.setText(nearByDTO.getAddress());
        ctvTime.setText("Morning Timing - " + nearByDTO.getStart_timing() + " to " + nearByDTO.getEnd_timing());
        ctvETime.setText("Evening Timing - " + nearByDTO.getE_open_time() + " to " + nearByDTO.getE_close_time());
        // tvUrl.setText(nearByDTO.get);
        Glide.with(PlaceDetailsActivity.this).load(nearByDTO.getImage_path()).placeholder(R.drawable.app_logo).into(ivProfile);

        LLphone.setOnClickListener(this);
        tvAddress1.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
        tvUrl.setOnClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

       /* String reference = getIntent().getStringExtra("reference");


        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        sb.append("reference=" + reference);
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyBCR6Y0Qn_piKnlPhb6olCPdXglcFOfhaM");


        // Creating a new non-ui thread task to download Google place details
        PlacesTask placesTask = new PlacesTask();

        // Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvMobile:
                makecall(nearByDTO.getMobile());
                break;
            case R.id.tvUrl:
                goAddress();
                break;

        }
    }

    private void goAddress() {
        try {

            String uri = String.format(Locale.ENGLISH, "geo:0,0?q=" + tvAddress.getText().toString().trim());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void makecall(String phone) {

        try {
            Intent my_callIntent = new Intent(Intent.ACTION_DIAL);
            my_callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(my_callIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void mapLocation(String locationName, String latitude, String longitude) {
        String label = locationName;
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    /* ;


     *//**
     * A method to download json data from url
     *//*
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
            Log.d("downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }






    *//**
     * A class, to download Google Place Details
     *//*
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

            // Start parsing the Google place details in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }


    *//**
     * A class to parse the Google Place Details in JSON format
     *//*
    private class ParserTask extends AsyncTask<String, Integer, HashMap<String, String>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected HashMap<String, String> doInBackground(String... jsonData) {

            HashMap<String, String> hPlaceDetails = null;
            PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Start parsing Google place details in JSON format
                hPlaceDetails = placeDetailsJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return hPlaceDetails;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(HashMap<String, String> hPlaceDetails) {


            name = hPlaceDetails.get("name");
            String icon = hPlaceDetails.get("icon");
            String vicinity = hPlaceDetails.get("vicinity");
            lat = hPlaceDetails.get("lat");
            lng = hPlaceDetails.get("lng");
            String formatted_address = hPlaceDetails.get("formatted_address");
            formatted_phone = hPlaceDetails.get("formatted_phone");
            String website = hPlaceDetails.get("website");
            String rating = hPlaceDetails.get("rating");
            String international_phone_number = hPlaceDetails.get("international_phone_number");
            String url = hPlaceDetails.get("url");


            tvName.setText(name);
            tvMobile.setText(formatted_phone);
            tvAddress.setText(vicinity);
            tvAddress1.setText(formatted_address);
            tvUrl.setText(url);
            Glide.with(PlaceDetailsActivity.this).load(icon).into(ivProfile);
            String mimeType = "text/html";
            String encoding = "utf-8";


            String data = "<html>" +
                    "<body><img style='float:left' src=" + icon + " /><h1><center>" + name + "</center></h1>" +
                    "<br style='clear:both' />" +
                    "<hr  />" +
                    "<p>Vicinity : " + vicinity + "</p>" +
                    "<p>Location : " + lat + "," + lng + "</p>" +
                    "<p>Address : " + formatted_address + "</p>" +
                    "<p>Phone : " + formatted_phone + "</p>" +
                    "<p>Website : " + website + "</p>" +
                    "<p>Rating : " + rating + "</p>" +
                    "<p>International Phone  : " + international_phone_number + "</p>" +
                    "<p>URL  : <a href='" + url + "'>" + url + "</p>" +
                    "</body></html>";

            // Setting the data in WebView

        }
    }*/
}