package mcity.com.mcity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class SearchHouseFilter extends Activity {
    ImageView submit;
    String URL = Data_Service.URL_API + "searchforrent";
    String SHOW_IMAGE = Data_Service.URL_IMG + "rent/";
    ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    ArrayList<HashMap<String, ArrayList<String>>> contactList1;
    static String description = "description";
    static String landmark = "bedroom";
    static String furnishedtype = "furnishedtype";
    static String address = "address";
    static String residential= "residential";
    static String monthlyrent = "monthlyrent";
    static String location ="location";
    static String bedroom = "bedroom";
    static String renttype="renttype";
    static String mobileno="mobileno";
    static String email="email";
    static String path="path";
    static String phone="phone";
    static String username="username";
    ArrayList<String> image_url;
    ProgressBar progressBar;



    LinearLayout back;
    SearchHouseAdapter houseAdapter;
    ArrayList<String> imagelist = new ArrayList<>();
    ArrayList<Integer> timeNames;
    String id, token;
    String loc, resi,furnish,minRent, maxRent, bedtype;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_filter);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);
        Intent intent = getIntent();
        loc = intent.getStringExtra("area");
        Log.e("tag","!!!!!3"+loc);
        resi=intent.getStringExtra("residential");
        furnish=intent.getStringExtra("Furnishedtype");
        minRent = intent.getStringExtra("minRent");
        maxRent = intent.getStringExtra("maxRent");
        bedtype = intent.getStringExtra("bedroom");

        listView = (ListView) findViewById(R.id.listView);
        back = (LinearLayout) findViewById(R.id.back_arrow);
        contactList = new ArrayList<>();
        contactList1 = new ArrayList<>();



        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString("token", "");
        id = sharedPreferences.getString("id", "");
        new SearchHouseAsync().execute();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), MRental.class);
                startActivity(intent);
                finish();
            }
        });
        imagelist.add("");

    }

    class SearchHouseAsync extends AsyncTask<String, Void, String> {

        public SearchHouseAsync() {
            String json = "", jsonStr = "";
        }

        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);


        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("location",loc);
                Log.e("tag","@@@@"+loc);
                jsonObject.accumulate("residential",resi);
                jsonObject.accumulate("bedroom",bedtype);
                jsonObject.accumulate("furnishedtype",furnish);
                jsonObject.accumulate("minvalue", minRent);
                jsonObject.accumulate("maxvalue", maxRent);



                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(URL, json, id, token);
            }
            catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
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

                Log.e("tag", "<-----data_length----->" + data.length());


                if (data.length() > 0) {
                    for (int i1 = 0; i1 < data.length(); i1++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonObject = data.getJSONObject(i1);


                        String postforrent = jsonObject.getString("postforrent");

                        JSONObject pos_rent = new JSONObject(postforrent);
                        Log.e("tag","pos_rent"+  pos_rent);

                        map.put("mobileno", jsonObject.getString("mobileno"));
                        Log.e("tag","number_status"+jsonObject.getString("mobileno"));
                        map.put("email", jsonObject.getString("email"));
                        map.put("username", jsonObject.getString("username"));


                        map.put("monthlyrent", pos_rent.getString("monthlyrent"));
                        map.put("description", pos_rent.getString("description"));
                        map.put("phone", pos_rent.getString("phone"));
                        map.put("bedroom", pos_rent.getString("bedroom"));
                        map.put("furnishedtype", pos_rent.getString("furnishedtype"));
                        map.put("address", pos_rent.getString("address"));
                        map.put("renttype", pos_rent.getString("renttype"));
                        map.put("residential", pos_rent.getString("residential"));
                        map.put("location", pos_rent.getString("location"));

                        JSONArray img_ar = pos_rent.getJSONArray("imageurl");

                        if(img_ar.length()>0){


                            for(int i =0;i<img_ar.length();i++){

                                JSONObject img_obj =img_ar.getJSONObject(i);

                                String path = SHOW_IMAGE + img_obj.getString("filename");

                                map.put("path" + i, path);
                                Log.e("tag","image_location"+path);


                            }
                        }

                        contactList.add(map);
                        Log.e("tag","<---contactList---->"+  contactList);


                    }

                        /*Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                        intent.putExtra("location", timeNames);
                        finish();*/
                    houseAdapter = new SearchHouseAdapter(getApplicationContext(), contactList);
                    listView.setAdapter(houseAdapter);

                } else
                {
                    TastyToast.makeText(getApplicationContext(), "your desired House/Apt is not Avalable now", TastyToast.LENGTH_LONG, TastyToast.INFO);
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(SearchHouseFilter.this,MRental.class);
        startActivity(i);
        finish();
    }
}