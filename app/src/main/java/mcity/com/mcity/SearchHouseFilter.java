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

public class SearchHouseFilter extends Activity {
    ImageView submit;
    String URL = Data_Service.URL_API + "searchforrentnew";
    String ALL_RENT = Data_Service.URL_API + "allrent";
    String SHOW_IMAGE = Data_Service.URL_IMG + "rent/";
    ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    ArrayList<HashMap<String, ArrayList<String>>> contactList1;
    static String description = "description";
    static String landmark = "bedroom";
    static String address = "address";
    static String monthlyrent = "monthlyrent";
    static String location ="location";
    static String mobileno="mobileno";
    static String email="email";
    static String path="path";
    static String phone="phone";
    static String username="username";
    HashMap<String, String> map;
    ProgressBar progressBar;
    Dialog dialog2;




    LinearLayout back,lnr_empty;
    SearchHouseAdapter houseAdapter;
    ArrayList<String> imagelist = new ArrayList<>();
    String id, token;
    String loc, resi,furnish, bedtype,allrent;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_filter);


        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        Intent intent = getIntent();
        loc = intent.getStringExtra("area");
        resi=intent.getStringExtra("residential");
        furnish=intent.getStringExtra("Furnishedtype");
        bedtype = intent.getStringExtra("bedroom");
        allrent=intent.getStringExtra("allrent");

        listView = (ListView) findViewById(R.id.listView);
        back = (LinearLayout) findViewById(R.id.back_arrow);
        lnr_empty=(LinearLayout)findViewById(R.id.lnr_empty);

        dialog2 = new Dialog(SearchHouseFilter.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        contactList = new ArrayList<>();
        contactList1 = new ArrayList<>();
        lnr_empty.setVisibility(View.GONE);


        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString("token", "");
        id = sharedPreferences.getString("id", "");


        if(loc==null)
        {
            loc = "";
        }
        else
        {
            if(resi==null)
            {
                resi="";
            }
            else{
                if(furnish==null)
                {
                    furnish="";
                }
                else
                {
                    if(loc==null&&resi==null&&furnish==null)
                    {
                        loc="";
                        furnish="";
                        resi="";
                    }
                    else
                    {
                        if(loc==null&&resi==null)
                        {
                            loc="";
                            resi="";
                        }
                        else
                        {
                            if(loc==null&&furnish==null)
                            {
                                loc="";
                                furnish="";
                            }
                            else
                            {
                                if(resi==null&&furnish==null)
                                {
                                    resi="";
                                    furnish="";
                                }
                            }
                        }
                    }
                }
            }
        }


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
            String json = "", jsonStr;
        }

        protected void onPreExecute() {
            dialog2.show();
            super.onPreExecute();



        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("location",loc);
                jsonObject.accumulate("residential",resi);
                jsonObject.accumulate("bedroom",bedtype);
                jsonObject.accumulate("furnishedtype",furnish);


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

                            map.put("mobileno", jsonObject.getString("mobileno"));
                            map.put("monthlyrent", jsonObject.getString("monthlyrent"));
                            map.put("description", jsonObject.getString("description"));
                            map.put("phone", jsonObject.getString("phone"));
                            map.put("bedroom", jsonObject.getString("bedroom"));
                            map.put("furnishedtype", jsonObject.getString("furnishedtype"));
                            map.put("address", jsonObject.getString("address"));
                            map.put("renttype", jsonObject.getString("renttype"));
                            map.put("residential", jsonObject.getString("residential"));
                            map.put("location", jsonObject.getString("location"));

                            JSONArray userdetails = jsonObject.getJSONArray("userdetails");

                            for(int u =0;u<userdetails.length();u++){
                                JSONObject user_obj =userdetails.getJSONObject(u);
                                map.put("email", user_obj.getString("email"));
                                map.put("username", user_obj.getString("username"));
                            }

                            JSONArray img_ar = jsonObject.getJSONArray("imageurl");

                            if(img_ar.length()>0){
                                for(int i =0;i<img_ar.length();i++){
                                    JSONObject img_obj =img_ar.getJSONObject(i);
                                    String path = SHOW_IMAGE + img_obj.getString("filename");
                                    map.put("path" + i, path);
                                }
                            }

                            contactList.add(map);
                        }
                        lnr_empty.setVisibility(View.GONE);
                        houseAdapter = new SearchHouseAdapter(getApplicationContext(), contactList);
                        listView.setAdapter(houseAdapter);

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