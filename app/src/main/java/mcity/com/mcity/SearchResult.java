package mcity.com.mcity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 03-10-2016.
 */
public class SearchResult extends Activity {

    String str_from,str_to,str_date,str_price,midway,val1,val2;
    String token,uid,str_midway;
    String URL = Data_Service.URL_API + "advancedsearch";
    ListView searchlist;
    AdvancedRideAdapter rideAdapter;
    ArrayList<HashMap<String, String>> searchridelist;
    ProgressBar progressBar;
    LinearLayout back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        searchridelist = new ArrayList<>();
        searchlist=(ListView) findViewById(R.id.searchlist);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        back_arrow=(LinearLayout)findViewById(R.id.back_arrow);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString("token", "");
        uid = sharedPreferences.getString("id", "");

        Intent intent = getIntent();

        str_from = intent.getStringExtra("source");
        str_to = intent.getStringExtra("dest");
        str_date = intent.getStringExtra("date");
        midway = intent.getStringExtra("midway");
        midway = intent.getStringExtra("minval");
        midway = intent.getStringExtra("maxval");
        val1 = intent.getStringExtra("minval");
        val2=intent.getStringExtra("maxval");

        if (Util.Operations.isOnline(SearchResult.this)) {

                new SearchRideAsync(str_from, str_to, str_date, str_price, str_midway).execute();

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_SHORT).show();
        }


        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Dashboard.class);
                startActivity(i);
                finish();
            }
        });
    }

    private class SearchRideAsync extends AsyncTask<String,String,String> {
        String str_from,str_to,str_date,str_price,str_midway;
        public SearchRideAsync(String str_from, String str_to, String str_date,String str_price, String str_midway) {
            this.str_from = str_from;
            this.str_to = str_to;
            this.str_price=str_price;
            this.str_date = str_date;
            this.str_midway = str_midway;

        }

        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }


        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            String id = "";
            try {
                Log.e("tag","id"+uid);
                Log.e("tag","token"+token);
                //location,landmark,address,roomtype,monthlyrent,gender,description
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("from", str_from);

                jsonObject.accumulate("to", str_to);

                jsonObject.accumulate("date", str_date);
                jsonObject.accumulate("midwaydrop", midway);
                jsonObject.accumulate("minvalue", val1);
                jsonObject.accumulate("maxvalue", val2);
               /* jsonObject.accumulate("price", str_price);
                jsonObject.accumulate("midwaydrop", str_midway);*/
                Log.e("tag","one"+str_from);
                Log.e("tag","two"+str_to);
                Log.e("tag","three"+val1);
                Log.e("tag","four"+val2);



                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(URL, json, uid, token);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return jsonStr;
        }


        @Override
        protected void onPostExecute(String jsonstr) {
            Log.e("tag", "<-----result---->" + jsonstr);
            super.onPostExecute(jsonstr);
            progressBar.setVisibility(View.GONE);


            try {
                JSONObject jo = new JSONObject(jsonstr);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.d("tag", "<-----Status----->" + status);
                if (status.equals("true"))
                {

                    JSONArray data1 = jo.getJSONArray("message");
                    Log.e("tag","...#...1"+data1);
                    HashMap<String, String> map = new HashMap<String, String>();

                    for (int i = 0; i < data1.length(); i++) {
                        JSONObject data = data1.getJSONObject(i);
                        map.put("mobileno", data.getString("mobileno"));
                        Log.e("tag","%%%%"+ data.getString("mobileno"));
                        JSONArray data2 = data.getJSONArray("postforride");
                        Log.e("tag","...#...2"+data2);
                        for (int j = 0; j < data2.length(); j++)
                        {
                            JSONObject dataObj = data2.getJSONObject(j);
                            {
                                HashMap<String, String> map1 = new HashMap<String, String>();
                                map1.put("from", dataObj.getString("from"));
                                map1.put("to", dataObj.getString("to"));
                                map1.put("date", dataObj.getString("date"));
                                Log.e("tag","value"+dataObj.getString("date"));
                                map1.put("mobileno", data.getString("mobileno"));
                                searchridelist.add(map1);
                            }

                        }

                    }
                    Log.e("tag","searchridelist"+searchridelist);

                    rideAdapter = new AdvancedRideAdapter(SearchResult.this, searchridelist);
                    searchlist.setAdapter(rideAdapter);

                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Some error occured please check",Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {

        Intent i = new Intent(SearchResult.this,Dashboard.class);
        startActivity(i);
        finish();
        // super.onBackPressed();
    }
}
