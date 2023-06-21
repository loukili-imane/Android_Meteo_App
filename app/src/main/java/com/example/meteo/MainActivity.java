package com.example.meteo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker marker;
    EditText textView;
    Button button;
    ImageView imageView;
    EditText longitude,
            latitude,
            humidity,
            pressure,
            country,
            max_temp,
            min_temp;
    TextView time, city_nam, temptv;
    double let_at,let_log;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        city_nam = findViewById(R.id.editTextTextPersonName);
       textView= findViewById(R.id.editTextTextPersonName);
        button = findViewById(R.id.button3);
        imageView = findViewById(R.id.img);
        temptv = findViewById(R.id.tmp);
        time = findViewById(R.id.txtdate);

        humidity = findViewById(R.id.humiditetxt);
        pressure = findViewById(R.id.pressiontxt);
        city_nam = findViewById(R.id.ville);
        max_temp = findViewById(R.id.tmpmaxtxt);
        min_temp = findViewById(R.id.tmpmintxt);


        button.setOnClickListener(new View.OnClickListener()
                                  {
                                      @Override
                                      public void onClick(View v)

                                      {
                                              FindWeather();
                                              SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                                      .findFragmentById(R.id.map);
                                              mapFragment.getMapAsync(MainActivity.this);
                                          }

                                  }
        );

    }
    public void FindWeather()
    {
        final String city =textView.getText().toString();
       String url= "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=462f445106adc1d21494341838c10019&units=metric";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject object = jsonObject.getJSONObject("main");

                            //find temperature
                            double temp = object.getDouble("temp");
                            temptv.setText(temp+"°C");

                            //find city
                            String city = jsonObject.getString("name");
                            JSONObject object2 = jsonObject.getJSONObject("coord");

                            //find latitude
                            double lat_find = object2.getDouble("lat");
                            let_at=lat_find;


                            //find longitude
                            double long_find = object2.getDouble("lon");
                            let_log=long_find;

                            city_nam.setText(city);

                            //find date & time
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat std = new SimpleDateFormat("HH:mm a \nE, MMM dd yyyy");
                            String date = std.format(calendar.getTime());
                            time.setText(date);



                            //find humidity
                            JSONObject object4 = jsonObject.getJSONObject("main");
                            int humidity_find = object4.getInt("humidity");
                            humidity.setText(humidity_find+"  %");

                            //find pressure
                            JSONObject object7 = jsonObject.getJSONObject("main");
                            String pressure_find = object7.getString("pressure");
                            pressure.setText(pressure_find+"  hPa");

                            //find min temperature
                            JSONObject object10 = jsonObject.getJSONObject("main");
                            double mintemp = object10.getDouble("temp_min");
                            min_temp.setText(mintemp+" °C");

                            //find max temperature
                            JSONObject object12 = jsonObject.getJSONObject("main");
                            double maxtemp = object12.getDouble("temp_max");
                            max_temp.setText(maxtemp+" °C");



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        final String city = textView.getText().toString();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=462f445106adc1d21494341838c10019&units=metric";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Trouver la température
                            JSONObject jsonObject = new JSONObject(response);
                            // Trouver la latitude
                            JSONObject object2 = jsonObject.getJSONObject("coord");
                            double lat_find = object2.getDouble("lat");

                            double long_find = object2.getDouble("lon");
                            mMap = googleMap;
                            if (marker != null && marker.isVisible()) {
                                marker.remove();
                            }
                            // Ajouter un marqueur à la ville et déplacer la caméra
                            LatLng cityLatLng = new LatLng(lat_find, long_find);
                            marker = mMap.addMarker(new MarkerOptions().position(cityLatLng).title("Je suis à " + city));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(cityLatLng));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
    /*
        TextView ville;
        TextView tmp;
        TextView tmpmin;
        TextView tmpmax;

        TextView txtpression;
        TextView txthumidite;
        TextView txtdate;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ImageView imgview = findViewById(R.id.img);
            imgview.setImageResource(R.drawable.picture);

                    if (android.os.Build.VERSION.SDK_INT > 9) {
                        StrictMode.ThreadPolicy policy = new
                                StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                    }

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            final Context co = this;

            ville = findViewById(R.id.ville);
            tmp = findViewById(R.id.tmp);
            tmpmin = findViewById(R.id.tmpmin);
            tmpmax = findViewById(R.id.tmpmax);
            txtpression = findViewById(R.id.txtpression);
            txthumidite = findViewById(R.id.txthumidite);
            txtdate = findViewById(R.id.txtdate);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    ville.setText(query);
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url = "http://api.openweathermap.org/data/2.5/weather?q="
                            + query + "&appid=e457293228d5e1465f30bcbe1aea456b";
                    //https://api.openweathermap.org/data/2.5/weather?q=London&appid=e457293228d5e1465f30bcbe1aea456b

                    // l'ancienne clé : 5bd7e048cf1ef62c79254f75dfe27d19
                    // la clé actuelle: e457293228d5e1465f30bcbe1aea456b
                    //clé 2022 : e457293228d5e1465f30bcbe1aea456b


                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.i("MyLog", "----------------------------------------------");
                                Log.i("MyLog", response);

                                JSONObject jsonObject = new JSONObject(response);

                                Date date = new Date(jsonObject.getLong("" +
                                        "") * 1000);
                                SimpleDateFormat simpleDateFormat =
                                        new SimpleDateFormat("dd-MMM-yyyy' T 'HH:mm");
                                String dateString = simpleDateFormat.format(date);

                                JSONObject main = jsonObject.getJSONObject("main");
                                int Temp = (int) (main.getDouble("temp") - 273.15);
                                int TempMin = (int) (main.getDouble("temp_min") - 273.15);
                                int TempMax = (int) (main.getDouble("temp_max") - 273.15);
                                int Pression = (int) (main.getDouble("pressure"));
                                int Humidite = (int) (main.getDouble("humidity"));

                                JSONArray weather = jsonObject.getJSONArray("weather");
                                String meteo = weather.getJSONObject(0).getString("main");

                                txtdate.setText(dateString);
                                tmp.setText(String.valueOf(Temp + "°C"));
                                tmpmin.setText(String.valueOf(TempMin) + "°C");
                                tmpmax.setText(String.valueOf(TempMax) + "°C");
                                txtpression.setText(String.valueOf(Pression + " hPa"));
                                txthumidite.setText(String.valueOf(Humidite) + "%");

                                Log.i("Weather", "----------------------------------------------");
                                Log.i("Meteo", meteo);
                                setImage(meteo);
                                Toast.makeText(co, meteo, Toast.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext( ), response, Toast.LENGTH_LONG).show( );


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("MyLog", "-------Connection problem-------------------");
                                    Toast.makeText(MainActivity.this,
                                            "City not fond", Toast.LENGTH_LONG).show();


                                }
                            });

                    queue.add(stringRequest);


                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {


                    return false;
                }
            });

            return true;
        }

        public void setImage(String s) {
            ImageView imgview = findViewById(R.id.img);
            if (s.equals("Rain")) {
                imgview.setImageResource(R.drawable.rainy1);
            } else if (s.equals("Clear")) {
                imgview.setImageResource(R.drawable.sunny);
            } else if (s.equals("Thunderstorm")) {
                imgview.setImageResource(R.drawable.storm);
            } else if (s.equals("Clouds")) {
                imgview.setImageResource(R.drawable.cloudy);
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

     */
    }

