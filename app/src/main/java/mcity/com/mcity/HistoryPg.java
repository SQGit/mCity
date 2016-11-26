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

import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 09-11-2016.
 */
public class HistoryPg extends Activity {

    String URL = Data_Service.URL_API + "allroom";
    String str_uid,str_token;
    ArrayList<HashMap<String, String>> contactList;
    HotelAdapter hotelAdapter;
    ListView listView;
    HashMap<String, String> map;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_pg);


        contactList=new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");


        listView=(ListView)findViewById(R.id.listView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        new HistoryPgs().execute();




    }

    private class HistoryPgs extends AsyncTask<String,String,String> {

        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {



            String json = "", jsonStr = "";
            String id = "";
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
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(jsonStr);
            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    JSONArray data = jo.getJSONArray("message");

                    if (data.length() > 0) {

                        for (int i1 = 0; i1 < data.length(); i1++) {


                            JSONObject jsonObject = data.getJSONObject(i1);



                            JSONArray pos_rent_array=jsonObject.getJSONArray("postforroom");


                            for (int j = 0; j < pos_rent_array.length(); j++) {


                                map = new HashMap<String, String>();
                                JSONObject pos_rent = pos_rent_array.getJSONObject(j);


                                map.put("mobileno", jsonObject.getString("mobileno"));
                                map.put("email", jsonObject.getString("email"));
                                map.put("username", jsonObject.getString("username"));

                                map.put("location", pos_rent.getString("location"));
                                map.put("address", pos_rent.getString("address"));
                                map.put("roomtype", pos_rent.getString("roomtype"));
                                map.put("description", pos_rent.getString("description"));
                                map.put("phone", pos_rent.getString("phone"));
                                map.put("monthlyrent", pos_rent.getString("monthlyrent"));
                                map.put("gender", pos_rent.getString("gender"));

                                contactList.add(map);
                                Log.e("tag","yyy"+contactList);
                            }



                        }

                        hotelAdapter = new HotelAdapter(getApplicationContext(), contactList);
                        listView.setAdapter(hotelAdapter);

                    } else
                    {
                        TastyToast.makeText(getApplicationContext(), "Sorry no one can POST PROPERTY", TastyToast.LENGTH_LONG, TastyToast.INFO);
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

