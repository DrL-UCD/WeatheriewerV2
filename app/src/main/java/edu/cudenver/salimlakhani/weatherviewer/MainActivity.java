package edu.cudenver.salimlakhani.weatherviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private EditText editTextCity;
    private TextView textViewDescription;
    private TextView textViewTemp;
    private TextView textViewDate;
    private TextView textViewMin;
    private TextView textViewMax;
    private TextView textViewHumidity;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        textViewDescription = findViewById(R.id.textViewDEscription);
        textViewTemp = findViewById(R.id.textViewTemp);
        textViewDate = findViewById(R.id.textViewDate);
        textViewMin = findViewById(R.id.textViewMin);;
        textViewMax = findViewById(R.id.textViewMax);;
        textViewHumidity = findViewById(R.id.textViewHumdity);
        imageView = findViewById(R.id.imageView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = editTextCity.getText().toString();

                if (location.equals("")) {
                    location = "Denver";
                }

                try {
                    String url1 = "https://api.openweathermap.org/data/2.5/weather?q=";
                    String url2 = "&appid=xxxxxxxxxxx";   //Replace xxxx with your own key
                    String url = url1 + location + url2;


                    sendAndRequestResponse (url);



                }
                catch (Exception e) {
                    Log.i ("info", e.getMessage());
                }


            }
        });
    }

    public void sendAndRequestResponse (String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        /*
        Constructor for StringRequest takes 4 parameters.
         */
        StringRequest requestString = new StringRequest (
                /*
                First parameter specify which method you want to use to make HTTP connection.
                There are two possibilities. We can either use get method or post metho.
                In my example I am using Get method.
                 */
                Request.Method.GET,
                /*
                Second parameter is the URl (in form of a string for the Web server
                 */
                url,
                /*
                Third parameter is Response.Listener object which must override
                the onResponse method
                 */
                new Response.Listener<String>() {
            public void onResponse (String response) {
                Weather weather = new Weather (response);
                displayResult(weather);
                }
            },
                /*
                Fourt parameter is Response.Error listener object which must override
                omErrorResponse method.
                 */
                new Response.ErrorListener() {
            public void onErrorResponse (VolleyError error) {
                Log.i("info", "Error: " + error.toString());
            }
        }
        );

        requestQueue.add(requestString);
    }


    public void displayResult (Weather weather) {

        //Log.i ("info", "Date: " + weather.getDate());
        textViewDate.setText(weather.getDate());
        textViewDescription.setText(weather.getDescription());
        textViewTemp.setText(weather.getTemp());
        textViewMin.setText(weather.getMinTemp());
        textViewMax.setText(weather.getMaxTemp());
        textViewHumidity.setText(weather.getHumidity());

        //Add Picasso in build.gradle
        Picasso.get().load(weather.getIconURL()).resize(1600,1600)
                .into(imageView);
        Log.i ("info", "Image Url: " + weather.getIconURL());
    }
}