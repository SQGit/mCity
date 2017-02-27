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
import android.widget.Toast;
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
    String URL = Data_Service.URL_API + "myroomnew";
    String str_token, str_uid;
    MyPgAdapter mypgAdapter;
    ArrayList<HashMap<String, String>> rideHistory;
    ListView searchlist;
    LinearLayout lnr_back_arrow;
    ProgressBar progressBar;
    Dialog dialog2;
    HashMap<String, String> map;


    static String mobileno = "mobileno";
    static String email = "email";
    static String phone = "phone";
    static String gender = "gender";
    static String monthlyrent = "monthlyrent";
    static String roomtype = "roomtype";
    static String address = "address";
    static String location="location";
    static String main_id="main_id";
    static String _id="_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_history);
        dialog2 = new Dialog(PGHistory.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        searchlist = (ListView) findViewById(R.id.searchlist);



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
            dialog2.show();
            super.onPreExecute();

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






                            rideHistory.add(map);
                            Log.e("tag", "CONTACT_LIST"+rideHistory);
                        }


                        mypgAdapter = new MyPgAdapter(PGHistory.this, rideHistory);
                        searchlist.setAdapter(mypgAdapter);

                    } else
                    {
                        Toast.makeText(getApplicationContext(), "Sorry, No House/Apt are available Now", Toast.LENGTH_LONG).show();
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
        Intent i = new Intent(PGHistory.this,MRentalPost.class);
        startActivity(i);
        finish();
    }
}