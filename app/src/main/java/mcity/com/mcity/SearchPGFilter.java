package mcity.com.mcity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
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

public class SearchPGFilter extends Activity {
    ImageView submit;
    String URL = Data_Service.URL_API + "searchforroomnew";
    ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    static String location = "location";
    static String address = "address";
    static String roomtype = "roomtype";
    static String description = "description";
    static String gender = "gender";
    static String monthlyrent = "monthlyrent";
    static String bedroom = "bedroom";
    static String mobileno="mobileno";
    static String email="email";
    static String phone="phone";
    static String username="username";

    ProgressBar progressBar;
    Dialog dialog2;
    LinearLayout back,lnr_empty;
    HotelAdapter hotelAdapter;
    String id, token;
    String str_location,str_roomtype,str_gendertype, minRent, maxRent;
    HashMap<String, String> map;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_filter);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);
        Intent intent = getIntent();
        str_location = intent.getStringExtra("location");
        str_roomtype = intent.getStringExtra("room_type");
        str_gendertype= intent.getStringExtra("gender_type");
        minRent = intent.getStringExtra("minRent");
        maxRent = intent.getStringExtra("maxRent");

        listView = (ListView) findViewById(R.id.listView);
        back = (LinearLayout) findViewById(R.id.back_arrow);
        lnr_empty = (LinearLayout) findViewById(R.id.lnr_empty);
        dialog2 = new Dialog(SearchPGFilter.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        contactList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString("token", "");
        id = sharedPreferences.getString("id", "");
        new SearchHouseAsync().execute();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lnr_empty.setVisibility(View.GONE);

    }

    class SearchHouseAsync extends AsyncTask<String, Void, String> {

        public SearchHouseAsync() {
            String json = "", jsonStr = "";
        }

        protected void onPreExecute() {
            dialog2.show();
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("location", str_location);
                jsonObject.accumulate("gender", str_gendertype);
                jsonObject.accumulate("roomtype",str_roomtype);
                jsonObject.accumulate("minvalue", minRent);
                jsonObject.accumulate("maxvalue", maxRent);


                Log.e("tag","*1"+str_location);
                Log.e("tag","*2"+str_gendertype);
                Log.e("tag","*3"+str_roomtype);
                Log.e("tag","*4"+minRent);
                Log.e("tag","*5"+maxRent);

                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(URL, json, id, token);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String jsonStr) {
            dialog2.dismiss();
            super.onPostExecute(jsonStr);
            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    JSONArray data = jo.getJSONArray("message");


                    if (data.length() > 0)
                    {

                        for (int i1 = 0; i1 < data.length(); i1++) {
                            JSONObject jsonObject = data.getJSONObject(i1);
                            map = new HashMap<String, String>();

                            map.put("monthlyrent", jsonObject.getString("monthlyrent"));
                            Log.e("tag","1"+jsonObject.getString("monthlyrent"));
                            map.put("roomtype", jsonObject.getString("roomtype"));
                            Log.e("tag","2"+jsonObject.getString("roomtype"));
                            map.put("gender", jsonObject.getString("gender"));
                            Log.e("tag","3"+jsonObject.getString("gender"));
                            map.put("location", jsonObject.getString("location"));
                            Log.e("tag","4"+jsonObject.getString("location"));
                            map.put("mobileno", jsonObject.getString("mobileno"));
                            Log.e("tag","5"+jsonObject.getString("mobileno"));
                            map.put("phone", jsonObject.getString("phone"));
                            Log.e("tag","6"+jsonObject.getString("phone"));
                            map.put("landmark", jsonObject.getString("landmark"));
                            Log.e("tag","6"+jsonObject.getString("landmark"));
                            map.put("description", jsonObject.getString("description"));
                            Log.e("tag","6"+jsonObject.getString("description"));

                            JSONArray userdetails = jsonObject.getJSONArray("userdetails");
                            Log.e("tag","7"+userdetails);
                            for(int u =0;u<userdetails.length();u++){
                                JSONObject user_obj =userdetails.getJSONObject(u);
                                map.put("email", user_obj.getString("email"));
                                Log.e("tag","8"+user_obj.getString("email"));
                                map.put("username", user_obj.getString("username"));
                                Log.e("tag","9"+user_obj.getString("username"));
                            }


                            contactList.add(map);
                        }
                        lnr_empty.setVisibility(View.GONE);
                        hotelAdapter = new HotelAdapter(getApplicationContext(), contactList);
                        listView.setAdapter(hotelAdapter);


                    } else
                    {
                        lnr_empty.setVisibility(View.VISIBLE);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(SearchPGFilter.this,RentalHistory.class);
        startActivity(i);
        finish();
    }
}