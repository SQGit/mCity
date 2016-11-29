package mcity.com.mcity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchPGFilter extends Activity {
    ImageView submit;
    String URL = Data_Service.URL_API + "searchforroom";
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

    LinearLayout back;
    HotelAdapter hotelAdapter;
    String id, token;
    String str_location,str_roomtype,str_gendertype, minRent, maxRent;

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
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        str_location = intent.getStringExtra("location");
        Log.e("tag","1"+str_location);
        str_roomtype = intent.getStringExtra("room_type");
        Log.e("tag","checkroom"+str_roomtype);
        str_gendertype= intent.getStringExtra("gender_type");
        minRent = intent.getStringExtra("minRent");
        Log.e("tag","2"+minRent);
        maxRent = intent.getStringExtra("maxRent");
        Log.e("tag","3"+maxRent);


        listView = (ListView) findViewById(R.id.listView);
        back = (LinearLayout) findViewById(R.id.back_arrow);
        contactList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString("token", "");
        id = sharedPreferences.getString("id", "");
        new SearchHouseAsync().execute();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    class SearchHouseAsync extends AsyncTask<String, Void, String> {

        public SearchHouseAsync() {
            String json = "", jsonStr = "";
        }

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
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
            Log.e("tag", "<-----result112323---->" + jsonStr);
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(jsonStr);

            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                //String msg = jo.getString("message");
                Log.d("tag", "<-----Status----->" + status);
                if (status.equals("true")) {
                    //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    //finish();


                    JSONArray data = jo.getJSONArray("message");
                    Log.e("tag", "<-----data----->" + data.length());
                    if (data.length() > 0) {
                        for (int i1 = 0; i1 < data.length(); i1++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject jsonObject = data.getJSONObject(i1);

                            String postforroom = jsonObject.getString("postforroom");
                            JSONObject pos_rent = new JSONObject(postforroom);
                            Log.e("tag","pos_rent"+  pos_rent);

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

                        }

                        hotelAdapter = new HotelAdapter(getApplicationContext(), contactList);
                        listView.setAdapter(hotelAdapter);
                    } else
                    {
                        TastyToast.makeText(getApplicationContext(), "Your desired Room/PG is not Avalable now", TastyToast.LENGTH_LONG, TastyToast.INFO);
                        //Toast.makeText(getApplicationContext(),"Your desired house/PG is not available Now..",Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(SearchPGFilter.this,Dashboard.class);
        startActivity(i);
        finish();
    }
}