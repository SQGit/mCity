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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sloop.fonts.FontsManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 24-10-2016.
 */
public class MyRideHistory extends Activity{
    String URL = Data_Service.URL_API + "myridesnew";
    String SHOW_IMAGE = Data_Service.URL_IMG + "licence/";
    String str_token, str_uid;
    RideHistoryAdapter ridehistoryAdapter;
    ArrayList<HashMap<String, String>> rideHistory;
    ListView searchlist;
    LinearLayout lnr_back_arrow,lnr_empty;
    ImageView img_settings_icon;
    ProgressBar progressBar;
    Dialog dialog2;


    static String from = "from";
    static String to = "to";
    static String date = "date";
    static String mobileno = "mobileno";
    static String email = "email";
    static String id_main = "id_main";
    static String id_sub = "id_sub";
    static String midwaydrop="midwaydrop";
    static String noofpersons="noofpersons";
    static String price="price";
    static String returndate="returndate";
    static String godate="godate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.ride_history);


        searchlist = (ListView) findViewById(R.id.searchlist);
        lnr_back_arrow=(LinearLayout)findViewById(R.id.back_arrow);

        lnr_empty=(LinearLayout)findViewById(R.id.lnr_empty);
        img_settings_icon=(ImageView)findViewById(R.id.settings_icon);


        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);


        dialog2 = new Dialog(MyRideHistory.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader1);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        rideHistory = new ArrayList<>();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");

        lnr_empty.setVisibility(View.GONE);

        img_settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag", "DDD");


                PopupMenu popup = new PopupMenu(MyRideHistory.this, img_settings_icon);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.opt_menu1, popup.getMenu());

                MenuInflater inflater = popup.getMenuInflater();
                //inflater.inflate(R.menu.opt_menu1, popup.getMenu());
                MenuItem pinMenuItem1 = popup.getMenu().getItem(0);
                MenuItem pinMenuItem2 = popup.getMenu().getItem(1);

                applyFontToMenuItem(pinMenuItem1);
                applyFontToMenuItem(pinMenuItem2);

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {


                        int id = item.getItemId();
                        switch (id) {
                            case R.id.item1:
                                aboutUs();
                                return true;

                            case R.id.item2:
                                exitIcon();
                                return true;
                        }
                        return true;
                    }

                });

                popup.show();

            }
        });


        lnr_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RideSearch.class);
                startActivity(i);
                finish();
            }
        });

        new RideHistoryAsync().execute();
    }

    //************* change option menu typeface settings page.
    private void applyFontToMenuItem(MenuItem mi)
    {
        Typeface font = Typeface.createFromAsset(getAssets(), "mont.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void aboutUs() {
        LayoutInflater layoutInflater = LayoutInflater.from(MyRideHistory.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final AlertDialog alertD = new AlertDialog.Builder(MyRideHistory.this).create();
        alertD.setCancelable(false);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.txt_head2);
        final TextView txt_msg = (TextView) promptView.findViewById(R.id.txt_msg);
        final Button yes = (Button) promptView.findViewById(R.id.btn_ok2);

        txt_msg.setText("\n" +"    SQIndia is a total Information Technology Company based out of Guduvanchery.SQIndia has its own Software Development Centre and provides Technology Consulting Services to its clients in India,US,UK and Singapore.Some of its Elite Customers include Mahindra,TVS,Nissan,ZOHO.\n\n              SQIndia also operates 2 Exclusive Lenovo Outlets - Guduvanchery and Chengalpet.SQIndia also has a MultiBranded Mobile showroom with a LIVE DEMO counters.\n\n          The aspirations to grow and serve people in all aspects has always been part of the Company's motto. Mr.Gopi who is the CEO/Founder of the Organization has spent more than a decade in the US and strives to make things easy and accessible for a common man."
        );

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


    private void exitIcon() {
        LayoutInflater layoutInflater = LayoutInflater.from(MyRideHistory.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(MyRideHistory.this).create();
        alertD.setCancelable(false);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.head1);
        final TextView head2 = (TextView) promptView.findViewById(R.id.head2);
        final ImageView no = (ImageView) promptView.findViewById(R.id.no);
        final ImageView yes = (ImageView) promptView.findViewById(R.id.yes);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        head1.setTypeface(tf);
        head1.setText("Exit");
        head2.setText("Do You want to Logout?");

        yes.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(MyRideHistory.this);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("check","");
                editor.commit();
                logoutMethod();
                alertD.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });
        alertD.setView(promptView);
        alertD.show();
    }

    private void logoutMethod() {

        new Logout().execute();
    }


    private class Logout extends AsyncTask<String, String, String> {
        @Override

        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost("http://104.197.7.143:3000/api/logout");
                postMethod.addHeader("x-access-token", str_token);
                postMethod.addHeader("id", str_uid);
                postMethod.addHeader("Content-Type", "multipart-form-data");
                HttpResponse response = client.execute(postMethod);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    json = EntityUtils.toString(r_entity);
                    JSONObject result1 = new JSONObject(json);
                    String status = result1.getString("status");
                    if (status.equals("true")){
                    }
                } else {
                    json = "Error occurred! Http Status Code: " + statusCode;
                }

            } catch (Exception e) {
                json = e.toString();
            }
            return json;
        }


        @Override
        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);
            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");

                if (status.equals("true"))
                {
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    Intent exit=new Intent(getApplicationContext(),Login.class);
                    startActivity(exit);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



    private class RideHistoryAsync extends AsyncTask<String, String, String> {
        @Override

        protected void onPreExecute() {
            dialog2.show();
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

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
            Log.e("tag", "<-----0000000--------->" + jsonstr);
            dialog2.dismiss();
            super.onPostExecute(jsonstr);
            if (jsonstr.equals("")) {
                Toast.makeText(getApplicationContext(),"Check Network Connection",Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("status");
                    JSONArray data1 = jo.getJSONArray("message");

                    if (data1.length() > 0) {
                        for (int i1 = 0; i1 < data1.length(); i1++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject jsonObject = data1.getJSONObject(i1);
                            map.put("_id", jsonObject.getString("_id"));
                            map.put("phone", jsonObject.getString("phone"));
                            map.put("midwaydrop", jsonObject.getString("midwaydrop"));
                            map.put("price", jsonObject.getString("price"));
                            map.put("to", jsonObject.getString("to"));
                            map.put("from", jsonObject.getString("from"));
                            map.put("mobileno", jsonObject.getString("mobileno"));
                            map.put("date", jsonObject.getString("date"));

                            JSONObject other_det = jsonObject.getJSONObject("otherdetails");
                            map.put("noofpersons", other_det.getString("noofpersons"));
                            JSONObject other_det1 = other_det.getJSONObject("roundtrip");
                            map.put("returndate", other_det1.getString("returndate"));
                            map.put("godate", other_det1.getString("godate"));
                            rideHistory.add(map);
                        }
                        ridehistoryAdapter = new RideHistoryAdapter(MyRideHistory.this, rideHistory);
                        searchlist.setAdapter(ridehistoryAdapter);

                    } else
                    {
                        lnr_empty.setVisibility(View.VISIBLE);
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
        Intent i = new Intent(MyRideHistory.this,RideSearch.class);
        startActivity(i);
        finish();
    }
}