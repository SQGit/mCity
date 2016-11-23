package mcity.com.mcity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
    String URL = Data_Service.URL_API + "myrent";
    String str_token, str_uid;
    ProgressBar progressBar;
    ArrayList<HashMap<String, String>> rentHistory;
    ListView searchlist;
    MyHouseAdapter myHouseAdapter;
    ListView listView;

    static String mobileno = "mobileno";
    static String email = "email";
    static String monthlyrent = "monthlyrent";
    static String furnishedtype="furnishedtype";
    static String bedroom="bedroom";
    static String location="location";
    static String residential="residential";
    static String renttype="renttype";
    static String id_main="id_main";
    static String id_sub="id_sub";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_history);

        searchlist = (ListView) findViewById(R.id.searchlist);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        listView=(ListView)findViewById(R.id.searchlist);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);
        rentHistory = new ArrayList<>();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");

        new HouseHistoryAsync().execute();

    }


   /* private void aboutUs() {

        LayoutInflater layoutInflater = LayoutInflater.from(HouseHistory.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final AlertDialog alertD = new AlertDialog.Builder(HouseHistory.this).create();
        alertD.setCancelable(false);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.txt_head2);
        final TextView txt_msg = (TextView) promptView.findViewById(R.id.txt_msg);
        final Button yes = (Button) promptView.findViewById(R.id.btn_ok2);


        txt_msg.setText("\n" +
                "This mCity App has no direct or indirect connection with Mahindra Group in any sort or form." +
                "It is completely self funded and wants to provide an Independent medium of Communication.Future growth may depend on Ad revenues." +
                "The purpose of mCity App is to help the locals for the locals." +
                "The data collected with this app will NOT be shared or forwarded.We respect privacy.\n \n         There are many more ideas and concepts that will be coming in the near future.\n" +
                "Please send your valuable feedback,concerns and suggestions to info@sqindia.net");

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        head1.setTypeface(tf);
        txt_msg.setTypeface(tf);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });


        alertD.setView(promptView);
        alertD.show();

    }
*/
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
            Log.e("tag","two");
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
            Log.e("tag", "whole data" + jsonstr);
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(jsonstr);
            try {
                JSONObject jo = new JSONObject(jsonstr);
                String status = jo.getString("status");
                Log.e("tag", "<-----Status----->" + status);
                if (status.equals("true")) {
                    JSONObject data = jo.getJSONObject("message");

                    Log.e("tag", "<-----data_length----->" + data.length());
                    JSONArray data1 = data.getJSONArray("postforrent");
                    Log.e("tag", "****" + data1);
                    Log.e("tag", "length" + data1.length());


                    if (data1.length() > 0) {

                        for (int m = 0; m < data1.length(); m++)
                        {
                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject pos_rent = data1.getJSONObject(m);
                            {

                                map.put("mobileno", data.getString("mobileno"));
                                map.put("email", data.getString("email"));
                                map.put("id_main", data.getString("_id"));

                                map.put("monthlyrent", pos_rent.getString("monthlyrent"));
                                map.put("description", pos_rent.getString("description"));
                                map.put("phone", pos_rent.getString("phone"));
                                map.put("bedroom", pos_rent.getString("bedroom"));
                                map.put("furnishedtype", pos_rent.getString("furnishedtype"));
                                map.put("address", pos_rent.getString("address"));
                                map.put("renttype", pos_rent.getString("renttype"));
                                map.put("residential", pos_rent.getString("residential"));
                                map.put("location", pos_rent.getString("location"));
                                map.put("id_sub", pos_rent.getString("_id"));

                                rentHistory.add(map);
                            }

                        }


                        myHouseAdapter = new MyHouseAdapter(HouseHistory.this, rentHistory);
                        listView.setAdapter(myHouseAdapter);
                    }


                    else
                    {
                        Toast.makeText(getApplicationContext(),"Your desired house/PG is not available Now..",Toast.LENGTH_SHORT).show();
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