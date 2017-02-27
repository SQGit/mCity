package mcity.com.mcity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 09-11-2016.
 */
public class HistoryPg extends Activity {

    String URL = Data_Service.URL_API + "allroomnew";
    String str_uid,str_token;
    ArrayList<HashMap<String, String>> contactList;
    HotelAdapter hotelAdapter;
    ListView listView;
    HashMap<String, String> map;
    ProgressBar progressBar;
    Dialog dialog2;
    TextView t1,t2,t3,t4;
    LinearLayout lnr_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_pg);


        contactList=new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");

        lnr_empty=(LinearLayout)findViewById(R.id.lnr_empty) ;
        t1=(TextView)findViewById(R.id.t1) ;
        t2=(TextView)findViewById(R.id.t2) ;
        t3=(TextView)findViewById(R.id.t3) ;
        t4=(TextView)findViewById(R.id.t4) ;
        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);
        lnr_empty.setVisibility(View.GONE);
        listView=(ListView)findViewById(R.id.listView);
        dialog2 = new Dialog(HistoryPg.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        new HistoryPgs().execute();
    }

    private class HistoryPgs extends AsyncTask<String,String,String> {

        protected void onPreExecute() {
            super.onPreExecute();
           dialog2.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr;

            try {
                JSONObject jsonObject = new JSONObject();
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
            dialog2.dismiss();
            super.onPostExecute(jsonStr);
            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                Log.e("tag", "<-----Status----->" + status);
                if (status.equals("true")) {
                    JSONArray data = jo.getJSONArray("message");

                    Log.e("tag","111"+data);

                    Log.e("tag", "<-----data_length----->" + data.length());


                    if (data.length() > 0)
                    {
                        Log.e("tag","1");
                        for (int i1 = 0; i1 < data.length(); i1++) {
                            Log.e("tag", "2");


                            JSONObject jsonObject = data.getJSONObject(i1);
                            map = new HashMap<String, String>();

                            map.put("_id", jsonObject.getString("_id"));
                            Log.e("tag", "_03" + jsonObject.getString("_id"));
                            map.put("phone", jsonObject.getString("phone"));
                            Log.e("tag", "04" + jsonObject.getString("phone"));
                            map.put("description", jsonObject.getString("description"));
                            Log.e("tag", "05" + jsonObject.getString("description"));
                            map.put("gender", jsonObject.getString("gender"));
                            Log.e("tag", "06" + jsonObject.getString("gender"));
                            map.put("roomtype", jsonObject.getString("roomtype"));
                            Log.e("tag", "07" + jsonObject.getString("roomtype"));
                            map.put("address", jsonObject.getString("address"));
                            Log.e("tag", "08" + jsonObject.getString("address"));
                            map.put("location", jsonObject.getString("location"));
                            Log.e("tag", "09" + jsonObject.getString("location"));
                            map.put("mobileno", jsonObject.getString("mobileno"));
                            Log.e("tag", "10" + jsonObject.getString("mobileno"));
                            map.put("monthlyrent", jsonObject.getString("monthlyrent"));
                            Log.e("tag", "11" + jsonObject.getString("monthlyrent"));
                            map.put("landmark", jsonObject.getString("landmark"));
                            Log.e("tag", "12" + jsonObject.getString("landmark"));


                            JSONArray userdetails = jsonObject.getJSONArray("userdetails");
                            Log.e("tag", "userdetails" + userdetails);

                            for(int u =0;u<userdetails.length();u++){
                                JSONObject user_obj =userdetails.getJSONObject(u);
                                map.put("email", user_obj.getString("email"));
                                Log.e("tag", "07" + user_obj.getString("email"));
                                map.put("username", user_obj.getString("username"));
                                Log.e("tag", "08" + user_obj.getString("username"));
                            }



                            contactList.add(map);
                            Log.e("tag", "CONTACT_LIST"+contactList);
                        }

                        lnr_empty.setVisibility(View.GONE);
                        hotelAdapter = new HotelAdapter(getApplicationContext(), contactList);
                        listView.setAdapter(hotelAdapter);

                    } else
                    {
                        lnr_empty.setVisibility(View.VISIBLE);
                        //Toast.makeText(getApplicationContext(),"Sorry no one can POST PROPERTY",Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        Log.e("tag","222");
        Intent i = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(i);
        finish();
    }

}

