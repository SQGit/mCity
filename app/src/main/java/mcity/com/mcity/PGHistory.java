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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.sloop.fonts.FontsManager;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Admin on 25-10-2016.
 */
public class PGHistory extends Activity {
    String URL = Data_Service.URL_API + "myroom";
    String str_token, str_uid;
    ProgressBar progressBar;
    MyPgAdapter mypgAdapter;
    ArrayList<HashMap<String, String>> rideHistory;
    ListView searchlist;
    LinearLayout lnr_back_arrow;


    static String mobileno = "mobileno";
    static String email = "email";
    static String phone = "phone";
    static String gender = "gender";
    static String monthlyrent = "monthlyrent";
    static String roomtype = "roomtype";
    static String address = "address";
    static String location="location";
    static String main_id="main_id";
    static String sub_id="sub_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_history);

        searchlist = (ListView) findViewById(R.id.searchlist);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);


        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);
        rideHistory = new ArrayList<>();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");


        new HouseHistoryAsync().execute();
    }

    private class HouseHistoryAsync extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            String id = "";
            try {

                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost();
                postMethod.addHeader("x-access-token", str_token);
                postMethod.addHeader("id", str_uid);
                postMethod.addHeader("Content-Type", "application/x-www-form-urlencoded");


                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id", str_uid);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(URL, json, str_uid, str_token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;

        }


        @Override
        protected void onPostExecute(String jsonstr) {
            super.onPostExecute(jsonstr);
            progressBar.setVisibility(View.GONE);

            if (jsonstr.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Check Network Connection", Toast.LENGTH_SHORT).show();
            }

            else {
                try {
                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("status");
                    JSONObject data11 = jo.getJSONObject("message");
                    HashMap<String, String> map = new HashMap<String, String>();


                    map.put("mobileno", data11.getString("mobileno"));
                    map.put("email", data11.getString("email"));
                    map.put("id_main", data11.getString("_id"));


                    JSONArray data1 = data11.getJSONArray("postforroom");

                    if (data1.length() > 0) {

                        for (int m = 0; m < data1.length(); m++)
                        {
                            HashMap<String, String> map1 = new HashMap<String, String>();
                            JSONObject jsonObject = data1.getJSONObject(m);

                                map1.put("phone", jsonObject.getString("phone"));
                                map1.put("description", jsonObject.getString("description"));
                                map1.put("gender", jsonObject.getString("gender"));
                                map1.put("monthlyrent", jsonObject.getString("monthlyrent"));
                                map1.put("roomtype", jsonObject.getString("roomtype"));
                                map1.put("location", jsonObject.getString("location"));
                                map1.put("sub_id", jsonObject.getString("_id"));
                                map1.put("mobileno", data11.getString("mobileno"));
                                map1.put("email", data11.getString("email"));
                                map1.put("main_id", data11.getString("_id"));

                            rideHistory.add(map1);



                        }


                        mypgAdapter = new MyPgAdapter(PGHistory.this, rideHistory);
                        searchlist.setAdapter(mypgAdapter);
                    }


                    else
                    {
                        TastyToast.makeText(getApplicationContext(), "You did't Post any Room/PG", TastyToast.LENGTH_LONG, TastyToast.INFO);
                        //Toast.makeText(getApplicationContext(), "You did't Post any Room/PG", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
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
        Intent i = new Intent(PGHistory.this,MRentalPost.class);
        startActivity(i);
        finish();
    }
}