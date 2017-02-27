package mcity.com.mcity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
public class HouseHistory extends Activity {
    String URL = Data_Service.URL_API + "myrentnew";
    String SHOW_IMAGE = Data_Service.URL_IMG + "rent/";
    String str_token, str_uid;
    ArrayList<HashMap<String, String>> rentHistory;
    ListView searchlist;
    MyHouseAdapter myHouseAdapter;
    ListView listView;
    HashMap<String, String> map;
    ProgressBar progressBar;
    Dialog dialog2;

    static String mobileno = "mobileno";
    static String email = "email";
    static String monthlyrent = "monthlyrent";
    static String furnishedtype="furnishedtype";
    static String bedroom="bedroom";
    static String location="location";
    static String residential="residential";
    static String renttype="renttype";
    static String id_main="id_main";
    static String _id="_id";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_history);

        searchlist = (ListView) findViewById(R.id.searchlist);
        dialog2 = new Dialog(HouseHistory.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);


        listView=(ListView)findViewById(R.id.searchlist);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);
        rentHistory = new ArrayList<>();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");

        new HouseHistoryAsync().execute();

    }



    //************* change option menu typeface settings page.
    private void applyFontToMenuItem(MenuItem mi)
    {
        Typeface font = Typeface.createFromAsset(getAssets(), "mont.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private class HouseHistoryAsync extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            super.onPreExecute();
            dialog2.show();
            Log.e("tag","two");


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
                            map.put("_id",jsonObject.getString("_id"));



                            JSONArray img_ar = jsonObject.getJSONArray("imageurl");

                            if(img_ar.length()>0){
                                for(int i =0;i<img_ar.length();i++){
                                    JSONObject img_obj =img_ar.getJSONObject(i);
                                    String path = SHOW_IMAGE + img_obj.getString("filename");
                                    map.put("path" + i, path);
                                }
                            }
                            rentHistory.add(map);
                        }


                        myHouseAdapter = new MyHouseAdapter(HouseHistory.this, rentHistory);
                        listView.setAdapter(myHouseAdapter);

                    } else
                    {
                        Toast.makeText(getApplicationContext(), "Sorry, No House/Apt are available Now", Toast.LENGTH_LONG).show();
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
        Intent i = new Intent(HouseHistory.this,MRentalPost.class);
        startActivity(i);
        finish();
    }
}