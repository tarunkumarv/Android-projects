package com.fire2thoughts.radiusassignment;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    //recyclerView items
    List<Trips_Info> data = new ArrayList<>();
    RecyclerView recyclerView;
    //

    //profile data
    TextView name;
    TextView city,rides,freerides,credits,cr_symbol;
    ImageView profile_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView=findViewById(R.id.recycleradapter);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //profile
        name=findViewById(R.id.p_name);
        profile_img=findViewById(R.id.p_image);
        city=findViewById(R.id.p_address);
        rides=findViewById(R.id.p_r_count);
        freerides=findViewById(R.id.p_f_count);
        cr_symbol=findViewById(R.id.p_c_amount);


        //Json URL
        String JsonURL = "https://gist.githubusercontent.com/iranjith4/522d5b466560e91b8ebab54743f2d0fc/raw/7b10\n" +
                "8e4aaac287c6c3fdf93c3343dd1c62d24faf/radius-mobile-intern.json";

        //Json Request
        StringRequest stringRequest=new StringRequest(Request.Method.GET, JsonURL, new Response.Listener<String>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(String response) {
                try {
                    //on response parsing the data
                    Log.e("response",""+response);
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("before","");

                    JSONObject persondata=jsonObject.getJSONObject("data");
                    JSONObject person=persondata.getJSONObject("profile");
                    String s=person.getString("first_name");
                    s=s+" "+person.getString("last_name");
                    name.setText(s);
                    city.setText(person.getString("city")+", "+person.getString("Country"));
                    String img=person.getString("middle_name");
                    parseimage(img);



                    //parsing trips information
                    JSONArray jsonArray=persondata.getJSONArray("trips");

                    //parsing trips array
                    for(int i=0;i<jsonArray.length();i++){
                        //Accessing the JsonObject in the JsonArray
                        JSONObject json_data = jsonArray.getJSONObject(i);


                        Trips_Info info=new Trips_Info();
                        info.start=json_data.getString("from");
                        info.end=json_data.getString("to");
                        int ftime,ttime;
                        String start=json_data.getString("from_time");
                       String end =json_data.getString("to_time");
                        info.duration=json_data.getString("trip_duration_in_mins");
                        info.sdate=timestamp(start);
                        info.edate=timestamp(end);

                            //parsing trip cost
                            JSONObject jsonOb=json_data.getJSONObject("cost");
                            info.price=jsonOb.getString("value");
                            info.symbol=jsonOb.getString("currency_symbol");


                        //Adding each time to a list
                        data.add(info);
                        Log.e("listadapter",""+data);
                    }

                   //passing data to the recycler Adapter
                    Trip_Info_Adapter info_adapter=new Trip_Info_Adapter(data);
                    recyclerView.setAdapter(info_adapter);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    //parsing rides information
                    JSONObject stats=persondata.getJSONObject("stats");
                    rides.setText(stats.getString("rides"));
                    freerides.setText(stats.getString("free_rides"));

                    JSONObject credt=persondata.getJSONObject("credits");
                    cr_symbol.setText(stats.getString("currency_symbol")+stats.getString("value"));

                    //parsing theme info
                    JSONObject theme=persondata.getJSONObject("theme");
                    String darktheme=theme.getString("dark_colour");
                    String lighttheme=theme.getString("light_colour");
                    ConstraintLayout tripui=findViewById(R.id.tripui);
                    tripui.setBackgroundColor(Integer.parseInt(lighttheme));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Any error occured will display in Log
                Log.e("Volley error","while connecting");
            }
        });
        MySingleton.getInstance(this).addTORequestQueue(stringRequest);

    }

    public void parseimage(String img){
        ImageRequest imageRequest = new ImageRequest(
                img, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response
                        profile_img.setImageBitmap(response);


                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something with error response
                        error.printStackTrace();
                        //Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                    }
                }
        );

        MySingleton.getInstance(this).addTORequestQueue(imageRequest);

    }
    public String timestamp(String t){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Integer.parseInt(t) * 1000L);
        String date = DateFormat.format("MMM dd, hh:mm", cal).toString();
        return date;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     getMenuInflater().inflate(R.menu.menu,menu);
     return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
