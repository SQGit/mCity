package mcity.com.mcity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.LinkAddress;
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
import android.view.animation.AnimationUtils;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Admin on 30-09-2016.
 */
public class TrainSearch extends Activity {

    LinearLayout back_arrow;
    ImageView get_train,train_menu;
    String dayOfTheWeek, time;
    Dialog dialog2;
    int mytime;
    ProgressBar progressBar;
    String token, uid,more_train;
    TextView txt_time,txt_am_pm;
    String URL_NORMAL = Data_Service.URL_API + "trainsearch";
    String URL_SUNDAY = Data_Service.URL_API + "trainsearchsun";
    String URL_NORMAL_REV = Data_Service.URL_API + "parche";
    String URL_SUNDAY_REV = Data_Service.URL_API + "parchesun";
    String LOGOUT = Data_Service.URL_API + "logout";
    ArrayList<HashMap<String, String>> searchridelist;
    TrainAdapter trainAdapter;
    String str_token,str_uid,train_status;
    ListView searchlist;
    java.util.Date noteTS;
    String tt, date;
    ImageView settings_icon;
    Button getmoretrain;
    TextView txt_sou_des;
    static String name = "name";
    static String departuretime = "departuretime";
    static String arrivaltime = "arrivaltime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.train_search);

        searchridelist = new ArrayList<>();
        back_arrow = (LinearLayout) findViewById(R.id.back_arrow);
        //get_train=(ImageView)findViewById(R.id.get_train);
        getmoretrain=(Button)findViewById(R.id.getmoretrain);
        train_menu=(ImageView)findViewById(R.id.train_menu);
        txt_sou_des=(TextView)findViewById(R.id.txt_sou_des);
        txt_time=(TextView)findViewById(R.id.txt_time);
        txt_am_pm=(TextView)findViewById(R.id.txt_am_pm);

        searchlist=(ListView)findViewById(R.id.searchlist);
        settings_icon = (ImageView) findViewById(R.id.settings_icon);

        dialog2 = new Dialog(TrainSearch.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("train_status", "pcb");
        editor.commit();

        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");
        uid = sharedPreferences.getString("id", "");
        train_status = sharedPreferences.getString("train_status", "");


        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);
        Log.e("tag", "date1" + dayOfTheWeek);

        DateFormat df = new SimpleDateFormat("HH:mm");
        time = df.format(Calendar.getInstance().getTime());
        txt_time.setText(time);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour < 12 && hour >= 0)
        {
            txt_am_pm.setText(" AM");
        }
        else {
            hour -= 12;
            if(hour == 0)
            {
                hour = 12;
            }
            txt_am_pm.setText(" PM");
        }



        if(train_status.equals("pcb"))
        {
            txt_sou_des.setText("From Paranur To Chennai Beach");
            if (dayOfTheWeek.equals("Sunday"))

            {
                if (Util.Operations.isOnline(TrainSearch.this)) {


                    new TrainSearchForSunday().execute();


                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                }
            } else {
                if (Util.Operations.isOnline(TrainSearch.this)) {


                    new TrainSearchForAllday().execute();


                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                }
            }
        }

        else
        {
            txt_sou_des.setText("From  Paranur  To Chengalpattu");


            if (dayOfTheWeek.equals("Sunday"))

            {
                if (Util.Operations.isOnline(TrainSearch.this)) {


                    new TrainSearchForSundayRev().execute();


                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                }
            } else {
                if (Util.Operations.isOnline(TrainSearch.this)) {


                    new TrainSearchForAlldayRev().execute();


                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                }
            }


        }



        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(i);
                finish();
            }
        });



        getmoretrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchridelist.clear();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("more_train", "true");
                editor.commit();
                train_status = sharedPreferences.getString("train_status", "");


                if(train_status.equals("pcb"))
                {
                    if (dayOfTheWeek.equals("Sunday"))
                    {
                        if (Util.Operations.isOnline(TrainSearch.this)) {
                            new TrainSearchForSunday().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (Util.Operations.isOnline(TrainSearch.this)) {
                            new TrainSearchForAllday().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                else
                {
                    if (dayOfTheWeek.equals("Sunday"))
                    {
                        if (Util.Operations.isOnline(TrainSearch.this)) {
                            new TrainSearchForSundayRev().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (Util.Operations.isOnline(TrainSearch.this)) {
                            new TrainSearchForAlldayRev().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        train_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TrainSearch.this);
                train_status = sharedPreferences.getString("train_status", "");

                if(train_status.equals("pcb"))
                {

                    txt_sou_des.setText("From  Paranur  To Chengalpattu");


                    SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("train_status", "cbp");
                editor.commit();
                    searchridelist.clear();
                    if (dayOfTheWeek.equals("Sunday"))

                    {
                        if (Util.Operations.isOnline(TrainSearch.this)) {


                            new TrainSearchForSundayRev().execute();


                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (Util.Operations.isOnline(TrainSearch.this)) {


                            new TrainSearchForAlldayRev().execute();


                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                        }
                    }

                }

                else
                {
                    txt_sou_des.setText("From Paranur To Chennai Beach");

                    searchridelist.clear();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("train_status", "pcb");
                    editor.commit();

                    if (dayOfTheWeek.equals("Sunday"))

                    {
                        if (Util.Operations.isOnline(TrainSearch.this)) {


                            new TrainSearchForSunday().execute();


                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (Util.Operations.isOnline(TrainSearch.this)) {


                            new TrainSearchForAllday().execute();


                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                        }
                    }


                }




            }
        });


        settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag", "DDD");


                PopupMenu popup = new PopupMenu(TrainSearch.this, settings_icon);
                //Inflating the Popup using xml
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

                popup.show();//showing popup menu

            }
        });

    }

    //************* change option menu typeface settings page.
    private void applyFontToMenuItem(MenuItem mi)
    {
        Typeface font = Typeface.createFromAsset(getAssets(), "mont.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void exitIcon() {

        LayoutInflater layoutInflater = LayoutInflater.from(TrainSearch.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(TrainSearch.this).create();
        alertD.setCancelable(false);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.head1);
        final TextView head2 = (TextView) promptView.findViewById(R.id.head2);
        final ImageView no = (ImageView) promptView.findViewById(R.id.no);
        final ImageView yes = (ImageView) promptView.findViewById(R.id.yes);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        head1.setTypeface(tf);


        head1.setText("mCity");
        head2.setText("Do You want to Sign Out?");


        yes.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(TrainSearch.this);
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

    private void aboutUs() {

        LayoutInflater layoutInflater = LayoutInflater.from(TrainSearch.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final AlertDialog alertD = new AlertDialog.Builder(TrainSearch.this).create();
        alertD.setCancelable(false);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.txt_head2);
        final TextView txt_msg = (TextView) promptView.findViewById(R.id.txt_msg);
        final Button yes = (Button) promptView.findViewById(R.id.btn_ok2);


        txt_msg.setText("    SQIndia is a total Information Technology Company based out of Guduvanchery.SQIndia has its own Software Development Centre and provides Technology Consulting Services to its clients in India,US,UK and Singapore.Some of its Elite Customers include Mahindra,TVS,Nissan,ZOHO.\n\n              SQIndia also operates 2 Exclusive Lenovo Outlets - Guduvanchery and Chengalpet.SQIndia also has a MultiBranded Mobile showroom with a LIVE DEMO counters.\n\n          The aspirations to grow and serve people in all aspects has always been part of the Company's motto. Mr.Gopi who is the CEO/Founder of the Organization has spent more than a decade in the US and strives to make things easy and accessible for a common man."
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


    @Override
    public void onBackPressed() {

        Intent i = new Intent(TrainSearch.this, Dashboard.class);
        startActivity(i);
        finish();
        // super.onBackPressed();
    }

    private class TrainSearchForSunday extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            super.onPreExecute();
            dialog2.show();

        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr;

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("departuretime", time);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(URL_SUNDAY, json, uid, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String jsonstr) {
            Log.e("tag", "<-----111111111--------->" + jsonstr);
            super.onPostExecute(jsonstr);
           dialog2.dismiss();

            if (jsonstr.equals("")) {

                Toast.makeText(getApplicationContext(), "Check Network Connection", Toast.LENGTH_SHORT).show();

            } else {
                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("status");
                    JSONArray data1 = jo.getJSONArray("message");
                    Log.e("tag", "...#...1" + data1);
                    Log.e("tag","ss"+data1.length());
                    HashMap<String, String> map = new HashMap<String, String>();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TrainSearch.this);
                    more_train = sharedPreferences.getString("more_train", "");

                    if(more_train.equals("true"))
                    {


                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("more_train", "false");
                        editor.commit();


                        for (int j = 0; j < data1.length(); j++) {
                            Log.e("tag","check");
                            JSONObject dataObj = data1.getJSONObject(j);
                            {
                                HashMap<String, String> map1 = new HashMap<String, String>();
                                map1.put("name", dataObj.getString("name"));
                                map1.put("departuretime", dataObj.getString("departuretime"));
                                map1.put("arrivaltime", dataObj.getString("arrivaltime"));
                                searchridelist.add(map1);
                                Log.e("tag", "searchridelist" + searchridelist);
                            }
                        }
                    }
                    else {

                        for (int j = 0; j < 4; j++) {
                            Log.e("tag", "check");
                            JSONObject dataObj = data1.getJSONObject(j);
                            {
                                HashMap<String, String> map1 = new HashMap<String, String>();
                                map1.put("name", dataObj.getString("name"));
                                map1.put("departuretime", dataObj.getString("departuretime"));
                                map1.put("arrivaltime", dataObj.getString("arrivaltime"));
                                searchridelist.add(map1);
                                Log.e("tag", "searchridelist" + searchridelist);
                            }
                        }

                    }

                    trainAdapter = new TrainAdapter(TrainSearch.this, searchridelist);
                    searchlist.setAdapter(trainAdapter);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }

    private class TrainSearchForAllday extends AsyncTask<String, String, String> {


        protected void onPreExecute() {
            super.onPreExecute();
            dialog2.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            String id = "";
            try {

                //location,landmark,address,roomtype,monthlyrent,gender,description
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("departuretime", time);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(URL_NORMAL, json, uid, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonstr) {
            Log.e("tag", "<-----111111111--------->" + jsonstr);
            super.onPostExecute(jsonstr);
            dialog2.dismiss();

            if (jsonstr.equals("")) {

                Toast.makeText(getApplicationContext(), "Check Network Connection", Toast.LENGTH_LONG).show();

            } else {
                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("status");
                    //   String msg = jo.getString("message");
                    //    Log.e("tag","123...."+msg);

                    JSONArray data1 = jo.getJSONArray("message");
                    Log.e("tag", "...#...1" + data1);
                    Log.e("tag","ss"+data1.length());
                    HashMap<String, String> map = new HashMap<String, String>();


                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TrainSearch.this);
                    more_train = sharedPreferences.getString("more_train", "");

                    if(more_train.equals("true"))
                    {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("more_train", "false");
                        editor.commit();


                        for (int j = 0; j < data1.length(); j++) {
                            Log.e("tag","check");
                            JSONObject dataObj = data1.getJSONObject(j);
                            {
                                HashMap<String, String> map1 = new HashMap<String, String>();
                                map1.put("name", dataObj.getString("name"));
                                map1.put("departuretime", dataObj.getString("departuretime"));
                                map1.put("arrivaltime", dataObj.getString("arrivaltime"));
                                searchridelist.add(map1);
                                Log.e("tag", "searchridelist" + searchridelist);
                            }
                        }
                    }
                    else {

                        for (int j = 0; j < 4; j++) {
                            Log.e("tag", "check");
                            JSONObject dataObj = data1.getJSONObject(j);
                            {
                                HashMap<String, String> map1 = new HashMap<String, String>();
                                map1.put("name", dataObj.getString("name"));
                                map1.put("departuretime", dataObj.getString("departuretime"));
                                map1.put("arrivaltime", dataObj.getString("arrivaltime"));
                                searchridelist.add(map1);
                                Log.e("tag", "searchridelist" + searchridelist);
                            }
                        }

                    }

                    trainAdapter = new TrainAdapter(TrainSearch.this, searchridelist);
                    searchlist.setAdapter(trainAdapter);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }




    private class TrainSearchForSundayRev extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            super.onPreExecute();
            dialog2.show();

        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            String id = "";
            try {

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("departuretime", time);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(URL_SUNDAY_REV, json, uid, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String jsonstr) {
            Log.e("tag", "<-----111111111--------->" + jsonstr);
            super.onPostExecute(jsonstr);
            dialog2.dismiss();

            if (jsonstr.equals("")) {

                Toast.makeText(getApplicationContext(), "Check Network Connection", Toast.LENGTH_SHORT).show();

            } else {
                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("status");
                    JSONArray data1 = jo.getJSONArray("message");
                    Log.e("tag", "...#...1" + data1);
                    Log.e("tag","ss"+data1.length());
                    HashMap<String, String> map = new HashMap<String, String>();


                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TrainSearch.this);
                    more_train = sharedPreferences.getString("more_train", "");

                    if(more_train.equals("true"))
                    {


                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("more_train", "false");
                        editor.commit();


                        for (int j = 0; j < data1.length(); j++) {
                            Log.e("tag","check");
                            JSONObject dataObj = data1.getJSONObject(j);
                            {
                                HashMap<String, String> map1 = new HashMap<String, String>();
                                map1.put("name", dataObj.getString("name"));
                                map1.put("departuretime", dataObj.getString("departuretime"));
                                map1.put("arrivaltime", dataObj.getString("arrivaltime"));
                                searchridelist.add(map1);
                                Log.e("tag", "searchridelist" + searchridelist);
                            }
                        }
                    }
                    else {

                        for (int j = 0; j < 4; j++) {
                            Log.e("tag", "check");
                            JSONObject dataObj = data1.getJSONObject(j);
                            {
                                HashMap<String, String> map1 = new HashMap<String, String>();
                                map1.put("name", dataObj.getString("name"));
                                map1.put("departuretime", dataObj.getString("departuretime"));
                                map1.put("arrivaltime", dataObj.getString("arrivaltime"));
                                searchridelist.add(map1);
                                Log.e("tag", "searchridelist" + searchridelist);
                            }
                        }

                    }



                    trainAdapter = new TrainAdapter(TrainSearch.this, searchridelist);
                    searchlist.setAdapter(trainAdapter);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }

    private class TrainSearchForAlldayRev extends AsyncTask<String, String, String> {


        protected void onPreExecute() {
            super.onPreExecute();
            dialog2.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            String id = "";
            try {

                //location,landmark,address,roomtype,monthlyrent,gender,description
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("departuretime", time);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(URL_NORMAL_REV, json, uid, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonstr) {
            Log.e("tag", "<-----111111111--------->" + jsonstr);
            super.onPostExecute(jsonstr);
           dialog2.dismiss();

            if (jsonstr.equals("")) {

                Toast.makeText(getApplicationContext(), "Check Network Connection", Toast.LENGTH_LONG).show();

            } else {
                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("status");
                    //   String msg = jo.getString("message");
                    //    Log.e("tag","123...."+msg);

                    JSONArray data1 = jo.getJSONArray("message");
                    Log.e("tag", "...#...1" + data1);
                    Log.e("tag","ss"+data1.length());
                    HashMap<String, String> map = new HashMap<String, String>();


                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TrainSearch.this);
                    more_train = sharedPreferences.getString("more_train", "");

                    if(more_train.equals("true"))
                    {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("more_train", "false");
                        editor.commit();


                        for (int j = 0; j < data1.length(); j++) {
                            Log.e("tag","check");
                            JSONObject dataObj = data1.getJSONObject(j);
                            {

                                HashMap<String, String> map1 = new HashMap<String, String>();
                                map1.put("name", dataObj.getString("name"));
                                map1.put("departuretime", dataObj.getString("departuretime"));
                                map1.put("arrivaltime", dataObj.getString("arrivaltime"));
                                searchridelist.add(map1);
                                Log.e("tag", "searchridelist" + searchridelist);
                            }
                        }
                    }
                    else {

                        for (int j = 0; j < 4; j++) {
                            Log.e("tag", "check");
                            JSONObject dataObj = data1.getJSONObject(j);
                            {
                                HashMap<String, String> map1 = new HashMap<String, String>();
                                map1.put("name", dataObj.getString("name"));
                                map1.put("departuretime", dataObj.getString("departuretime"));
                                map1.put("arrivaltime", dataObj.getString("arrivaltime"));
                                searchridelist.add(map1);
                                Log.e("tag", "searchridelist" + searchridelist);
                            }
                        }

                    }

                    Log.e("tag", "searchridelist" + searchridelist);

                    trainAdapter = new TrainAdapter(TrainSearch.this, searchridelist);
                    searchlist.setAdapter(trainAdapter);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private class Logout extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            super.onPreExecute();
            dialog2.show();

        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost(LOGOUT);
                postMethod.addHeader("x-access-token", token);
                postMethod.addHeader("id", uid);
                postMethod.addHeader("Content-Type", "multipart-form-data");

                HttpResponse response = client.execute(postMethod);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                Log.e("tag","res "+response.getStatusLine().toString());
                if (statusCode == 200) {
                    json = EntityUtils.toString(r_entity);

                    JSONObject result1 = new JSONObject(json);
                    String status = result1.getString("status");
                    Log.e("tag","status..........."+status);

                    if (status.equals("true")) {
                        Log.e("tag","Success...........");
                    }
                } else {
                    json = "Error occurred! Http Status Code: " + statusCode;
                    Log.e("tag","failure...........");
                }

            } catch (Exception e) {
                json = e.toString();
            }
            return json;



        }


        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "<-----result---->" + jsonStr);
            super.onPostExecute(jsonStr);
            dialog2.dismiss();

            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.e("tag", "<-----Status----->" + status);
                Log.e("tag", "<-----msg----->" + msg);

                if (status.equals("true"))
                {

                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                    Intent exit=new Intent(getApplicationContext(),Test_L.class);
                    startActivity(exit);
                    finish();


                    Log.e("tag","s...........");
                }
                else
                {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    Log.e("tag","no...........");

                }
            }
            catch (JSONException e) {
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




}

