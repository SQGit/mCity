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
public class MyAddHistory extends Activity {
    String URL = Data_Service.URL_API + "mysell";
    String SHOW_IMAGE = Data_Service.URL_IMG + "sell/";
    String str_token, str_uid;
    ArrayList<HashMap<String, String>> myadapt;
    ListView searchlist;
    Myselladapter mysellAdapter;
    ListView listView;
    LinearLayout back_arrow;
    ProgressBar progressBar;
    Dialog dialog2;
    HashMap<String, String> map;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaddhistory);

        listView=(ListView)findViewById(R.id.listView);
        back_arrow=(LinearLayout)findViewById(R.id.back_arrow);

        dialog2 = new Dialog(MyAddHistory.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MGarage_History.class);
                startActivity(i);
                finish();
            }
        });


        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);
        myadapt = new ArrayList<>();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");
        new GarargeHistoryAsync().execute();
    }



    //************* change option menu typeface settings page.
    private void applyFontToMenuItem(MenuItem mi)
    {
        Typeface font = Typeface.createFromAsset(getAssets(), "mont.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private class GarargeHistoryAsync extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag","two");
            dialog2.show();


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
            Log.e("tag", "whole data" + jsonstr);
            dialog2.dismiss();
            super.onPostExecute(jsonstr);
            try {
                JSONObject jo = new JSONObject(jsonstr);
                String status = jo.getString("status");
                Log.e("tag", "<-----Status----->" + status);
                if (status.equals("true")) {
                    JSONArray data = jo.getJSONArray("message");

                    Log.e("tag","checking"+data);

                    if (data.length() > 0)
                    {
                        Log.e("tag","1");
                        for (int i1 = 0; i1 < data.length(); i1++) {
                            Log.e("tag", "2");


                            JSONObject jsonObject = data.getJSONObject(i1);
                            map = new HashMap<String, String>();
                            map.put("_id", jsonObject.getString("_id"));
                            Log.e("tag", "01" + jsonObject.getString("_id"));
                            map.put("phone", jsonObject.getString("phone"));
                            Log.e("tag", "02" + jsonObject.getString("phone"));
                            map.put("description", jsonObject.getString("description"));
                            Log.e("tag","03"+jsonObject.getString("description"));
                            map.put("field4", jsonObject.getString("field4"));
                            Log.e("tag", "04" + jsonObject.getString("field4"));
                            map.put("field3", jsonObject.getString("field3"));
                            Log.e("tag", "05" + jsonObject.getString("field3"));
                            map.put("field2", jsonObject.getString("field2"));
                            Log.e("tag", "06" + jsonObject.getString("field2"));
                            map.put("field1", jsonObject.getString("field1"));
                            Log.e("tag", "07" + jsonObject.getString("field1"));
                            map.put("price", jsonObject.getString("price"));
                            Log.e("tag", "08" + jsonObject.getString("price"));
                            map.put("category", jsonObject.getString("category"));

                          /*  JSONArray userdetails = jsonObject.getJSONArray("userdetails");
                            Log.e("tag", "userdetails" + userdetails);

                            for(int u =0;u<userdetails.length();u++){
                                JSONObject user_obj =userdetails.getJSONObject(u);
                                map.put("email", user_obj.getString("email"));
                                Log.e("tag", "07" + user_obj.getString("email"));
                                map.put("username", user_obj.getString("username"));
                                Log.e("tag", "08" + user_obj.getString("username"));
                            }

                            Log.e("tag","hghg");*/
                            JSONArray pos_garage_array = jsonObject.getJSONArray("imageurl");
                            Log.e("tag","jjjjjjj"+pos_garage_array);
                            for (int j = 0; j < pos_garage_array.length(); j++) {
                                JSONObject pos_rent = pos_garage_array.getJSONObject(j);

                                String path = SHOW_IMAGE + pos_rent.getString("filename");
                                map.put("path" + j, path);


                            }
                            myadapt.add(map);
                            Log.e("tag", "CONTACT_LIST"+myadapt);
                        }


                        mysellAdapter = new Myselladapter(MyAddHistory.this, myadapt);
                        listView.setAdapter(mysellAdapter);
                    } else
                    {

                        Toast.makeText(getApplicationContext(), "Sorry, No relevent data available... ", Toast.LENGTH_LONG).show();
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
        Intent i = new Intent(MyAddHistory.this,MGarage_History.class);
        startActivity(i);
        finish();
    }
}