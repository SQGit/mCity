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
public class HistoryHouse extends Activity {
    String ALL_RENT = Data_Service.URL_API + "allrentnew";
    String SHOW_IMAGE = Data_Service.URL_IMG + "rent/";
    String str_uid,str_token;
    ArrayList<HashMap<String, String>> contactList1;
    HouseAdapter houseAdapter;
    ListView listView;
    ProgressBar progressBar;
    LinearLayout lnr_empty;
    Dialog dialog2;
    HashMap<String, String> map;
    TextView t1,t2,t3,t4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_house);

        contactList1=new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");


        listView=(ListView)findViewById(R.id.listView);
        t1=(TextView)findViewById(R.id.t1) ;
        t2=(TextView)findViewById(R.id.t2) ;
        t3=(TextView)findViewById(R.id.t3) ;
        t4=(TextView)findViewById(R.id.t4) ;
        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        lnr_empty=(LinearLayout)findViewById(R.id.lnr_empty) ;
        dialog2 = new Dialog(HistoryHouse.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        lnr_empty.setVisibility(View.GONE);
        new HistoryRental().execute();
    }


    private class HistoryRental extends AsyncTask<String,String,String>{

        protected void onPreExecute() {
            super.onPreExecute();
            dialog2.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            String id = "";

            try {

                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(ALL_RENT, json, str_uid, str_token);
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

                                map.put("mobileno", jsonObject.getString("mobileno"));
                                Log.e("tag", "01" + jsonObject.getString("mobileno"));
                                map.put("monthlyrent", jsonObject.getString("monthlyrent"));
                                Log.e("tag", "04" + jsonObject.getString("monthlyrent"));
                                map.put("description", jsonObject.getString("description"));
                                Log.e("tag", "05" + jsonObject.getString("description"));
                                map.put("phone", jsonObject.getString("phone"));
                                Log.e("tag", "06" + jsonObject.getString("phone"));
                                map.put("bedroom", jsonObject.getString("bedroom"));
                                Log.e("tag", "07" + jsonObject.getString("bedroom"));
                                map.put("furnishedtype", jsonObject.getString("furnishedtype"));
                                Log.e("tag", "08" + jsonObject.getString("furnishedtype"));
                                map.put("address", jsonObject.getString("address"));
                                Log.e("tag", "09" + jsonObject.getString("address"));
                                map.put("renttype", jsonObject.getString("renttype"));
                                Log.e("tag", "10" + jsonObject.getString("renttype"));
                                map.put("residential", jsonObject.getString("residential"));
                                Log.e("tag", "11" + jsonObject.getString("residential"));
                                map.put("location", jsonObject.getString("location"));
                                Log.e("tag", "12" + jsonObject.getString("location"));

                            JSONArray userdetails = jsonObject.getJSONArray("userdetails");
                            Log.e("tag", "userdetails" + userdetails);

                            for(int u =0;u<userdetails.length();u++){
                                JSONObject user_obj =userdetails.getJSONObject(u);
                                map.put("email", user_obj.getString("email"));
                                Log.e("tag", "07" + user_obj.getString("email"));
                                map.put("username", user_obj.getString("username"));
                                Log.e("tag", "08" + user_obj.getString("username"));
                            }

                                JSONArray img_ar = jsonObject.getJSONArray("imageurl");

                                if(img_ar.length()>0){
                                    for(int i =0;i<img_ar.length();i++){
                                        JSONObject img_obj =img_ar.getJSONObject(i);
                                        String path = SHOW_IMAGE + img_obj.getString("filename");
                                        map.put("path" + i, path);
                                    }
                                }

                                contactList1.add(map);
                                Log.e("tag", "CONTACT_LIST"+contactList1);
                        }

                        lnr_empty.setVisibility(View.GONE);
                        houseAdapter = new HouseAdapter(HistoryHouse.this, contactList1);
                        listView.setAdapter(houseAdapter);

                    } else
                    {
                        lnr_empty.setVisibility(View.VISIBLE);
                       //Toast.makeText(getApplicationContext(), "Sorry, No House/Apt are available Now", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


   /* @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }*/

    @Override
    public void onBackPressed() {
        Log.e("tag","222");
        Intent i = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(i);
        finish();
    }

}

