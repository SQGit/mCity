package mcity.com.mcity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 09-11-2016.
 */
public class HistoryHouse extends Activity {
    String URL = Data_Service.URL_API + "allrent";
    String str_uid,str_token;
    ArrayList<HashMap<String, String>> contactList1;
    HouseAdapter houseAdapter;
    ListView listView;
    ProgressBar progressBar;
    HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_house);

        contactList1=new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        listView=(ListView)findViewById(R.id.listView);

        new HistoryRental().execute();



    }



    private class HistoryRental extends AsyncTask<String,String,String>{

        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {



            String json = "", jsonStr = "";
            String id = "";
            try {

                //location,landmark,address,roomtype,monthlyrent,gender,description
                JSONObject jsonObject = new JSONObject();
                //jsonObject.accumulate("departuretime", time);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(URL, json, str_uid, str_token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;

        }


        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "whole data" + jsonStr);
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(jsonStr);
            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                Log.e("tag", "<-----Status----->" + status);
                if (status.equals("true")) {
                    JSONArray data = jo.getJSONArray("message");

                    Log.e("tag","111"+data);

                    Log.e("tag", "<-----data_length----->" + data.length());


                    if (data.length() > 0) {
                        Log.e("tag","1");
                        for (int i1 = 0; i1 < data.length(); i1++) {
                            Log.e("tag", "2");


                            JSONObject jsonObject = data.getJSONObject(i1);

                                JSONArray pos_rent_array = jsonObject.getJSONArray("postforrent");
                                Log.e("tag", "3" + pos_rent_array);

                                for (int j = 0; j < pos_rent_array.length(); j++) {


                                    map = new HashMap<String, String>();
                                    JSONObject pos_rent = pos_rent_array.getJSONObject(j);


                                    map.put("mobileno", jsonObject.getString("mobileno"));
                                    Log.e("tag","01"+jsonObject.getString("mobileno"));
                                    map.put("email", jsonObject.getString("email"));
                                    Log.e("tag","02"+jsonObject.getString("email"));
                                    map.put("username", jsonObject.getString("username"));
                                    Log.e("tag","03"+jsonObject.getString("username"));
                                    map.put("monthlyrent", pos_rent.getString("monthlyrent"));
                                    Log.e("tag","04"+jsonObject.getString("monthlyrent"));
                                    map.put("description", pos_rent.getString("description"));
                                    Log.e("tag","05"+jsonObject.getString("description"));
                                    map.put("phone", pos_rent.getString("phone"));
                                    Log.e("tag","06"+jsonObject.getString("phone"));
                                    map.put("bedroom", pos_rent.getString("bedroom"));
                                    Log.e("tag","07"+jsonObject.getString("bedroom"));
                                    map.put("furnishedtype", pos_rent.getString("furnishedtype"));
                                    Log.e("tag","08"+jsonObject.getString("furnishedtype"));
                                    map.put("address", pos_rent.getString("address"));
                                    Log.e("tag","09"+jsonObject.getString("address"));
                                    map.put("renttype", pos_rent.getString("renttype"));
                                    Log.e("tag","10"+jsonObject.getString("renttype"));
                                    map.put("residential", pos_rent.getString("residential"));
                                    Log.e("tag","11"+jsonObject.getString("residential"));
                                    map.put("location", pos_rent.getString("location"));
                                    Log.e("tag","12"+jsonObject.getString("location"));
                                    contactList1.add(map);
                                }
                            }

                        Log.e("tag", "<---adpatrttt---->");

                        houseAdapter = new HouseAdapter(HistoryHouse.this, contactList1);
                        listView.setAdapter(houseAdapter);

                    } else
                    {
                        Toast.makeText(getApplicationContext(),"Your desired house/PG is not available Now..",Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}

