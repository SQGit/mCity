package mcity.com.mcity;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
 * Created by Admin on 16-10-2016.
 */
public class MorePOst extends Activity {
    String SEARCH_RIDE = Data_Service.URL_API + "searchforride";
    String RIDE_FILTER = Data_Service.URL_API + "searchforridefilter";
    String GET_IMAGE = Data_Service.URL_IMG + "licence/";
    String LOGOUT = Data_Service.URL_API + "logout";


    ProgressBar progressBar;
    ListView searchlist;
    RideAdapter1 rideAdapter;
    ArrayList<HashMap<String, String>> searchridelist;
    ArrayList<HashMap<String, String>> normal_searchridelist;
    String str_token, str_uid,str_pathnew,str_key_open,str_from,str_to;
    LinearLayout lnr_back_arrow,lnr_progress_linr;
    ImageView img_settings_icon;


    static String phone = "phone";
    static String from = "from";
    static String to = "to";
    static String date = "date";
    static String mobileno = "mobileno";
    static String email = "email";
    static String price = "price";
    static String midwaydrop="midwaydrop";
    static String username="username";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.more_post);

        searchlist = (ListView) findViewById(R.id.searchlist);
        lnr_back_arrow=(LinearLayout) findViewById(R.id.back_arrow);
        lnr_progress_linr=(LinearLayout) findViewById(R.id.progress_linr);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        img_settings_icon = (ImageView) findViewById(R.id.settings_icon);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        searchridelist = new ArrayList<>();
        normal_searchridelist=new ArrayList<>();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");
        str_key_open=sharedPreferences.getString("view_post","");
        str_from=sharedPreferences.getString("from","");
        str_to=sharedPreferences.getString("to","");

        lnr_progress_linr.setVisibility(View.GONE);

        if(sharedPreferences.getString("view_post","").equals("")){
            new RideSearchAsync().execute();
        }
        else{
            str_from=sharedPreferences.getString("from","");
            str_to=sharedPreferences.getString("to","");
            new filterAsync(str_from,str_to).execute();
        }

        lnr_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),RideSearch.class);
                startActivity(i);
                finish();
            }
        });

        img_settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(MorePOst.this, img_settings_icon);

                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.opt_menu, popup.getMenu());
                MenuItem pinMenuItem1 = popup.getMenu().getItem(0);
                MenuItem pinMenuItem2 = popup.getMenu().getItem(1);
                MenuItem pinMenuItem3 = popup.getMenu().getItem(2);

                applyFontToMenuItem(pinMenuItem1);
                applyFontToMenuItem(pinMenuItem2);
                applyFontToMenuItem(pinMenuItem3);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();


                        switch (id) {
                            case R.id.item1:
                                aboutUs();
                                return true;

                            case R.id.item2:
                                Intent intent=new Intent(getApplicationContext(),MyRideHistory.class);
                                startActivity(intent);
                                finish();
                                return true;


                            case R.id.item3:
                                exitIcon();
                                return true;

                        }
                        return true;
                    }

                });

                popup.show();

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

        LayoutInflater layoutInflater = LayoutInflater.from(MorePOst.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(MorePOst.this).create();
        alertD.setCancelable(false);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.head1);
        final TextView head2 = (TextView) promptView.findViewById(R.id.head2);
        final ImageView no = (ImageView) promptView.findViewById(R.id.no);
        final ImageView yes = (ImageView) promptView.findViewById(R.id.yes);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        head1.setTypeface(tf);


        yes.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(MorePOst.this);
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

        LayoutInflater layoutInflater = LayoutInflater.from(MorePOst.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final AlertDialog alertD = new AlertDialog.Builder(MorePOst.this).create();
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

    private class RideSearchAsync extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            super.onPreExecute();
            lnr_progress_linr.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            String id = "";
            try {


                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(SEARCH_RIDE, json, str_uid, str_token);
            } catch (Exception e) {
            }
            return null;

        }



        @Override
        protected void onPostExecute(String jsonstr) {
            Log.e("tag", "<-----111111111--------->" + jsonstr);
            super.onPostExecute(jsonstr);
            progressBar.setVisibility(View.GONE);

            if (jsonstr.equals("")) {

                Toast.makeText(getApplicationContext(),"Check Network Connection",Toast.LENGTH_SHORT).show();

            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("status");
                    JSONArray data1 = jo.getJSONArray("message");


                    if (data1.length() > 0) {
                        for (int i1 = 0; i1 < data1.length(); i1++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject jsonObject = data1.getJSONObject(i1);


                            String postforride = jsonObject.getString("postforride");
                            JSONObject pos_rent = new JSONObject(postforride);

                            map.put("mobileno", jsonObject.getString("mobileno"));
                            map.put("email", jsonObject.getString("email"));
                            map.put("username", jsonObject.getString("username"));
                            map.put("from", pos_rent.getString("from"));
                            map.put("to", pos_rent.getString("to"));
                            map.put("date", pos_rent.getString("date"));
                            map.put("price", pos_rent.getString("price"));
                            map.put("phone", pos_rent.getString("phone"));
                            map.put("midwaydrop", pos_rent.getString("midwaydrop"));


                            JSONArray img_ar = jsonObject.getJSONArray("licence");

                            if(img_ar.length()>0){

                                for(int i =0;i<img_ar.length();i++){

                                    JSONObject img_obj =img_ar.getJSONObject(i);

                                    str_pathnew = GET_IMAGE + img_obj.getString("filename");

                                    Log.e("tag", "data: " + str_pathnew);

                                    map.put("path", str_pathnew);
                                }
                            }

                            normal_searchridelist.add(map);
                        }


                    } else
                    {
                        Toast.makeText(getApplicationContext(),"No Rides are Available in this Location..",Toast.LENGTH_SHORT).show();
                    }

                    rideAdapter = new RideAdapter1(MorePOst.this, normal_searchridelist);
                    searchlist.setAdapter(rideAdapter);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(MorePOst.this,RideSearch.class);
        startActivity(i);
        finish();
    }

    private class filterAsync extends AsyncTask<String,String,String>{

        String source,end;
        public filterAsync(String source, String end) {
            this.source = source;
            this.end = end;
        }


        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            String id = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("from", source);
                jsonObject.accumulate("to", end);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(RIDE_FILTER, json, str_uid, str_token);
            } catch (Exception e) {
            }
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String jsonstr) {

            super.onPostExecute(jsonstr);
            progressBar.setVisibility(View.GONE);

            if (jsonstr.equals("")) {

                Toast.makeText(getApplicationContext(),"Check Network Connection",Toast.LENGTH_SHORT).show();

            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("status");
                    JSONArray data1 = jo.getJSONArray("message");


                    if (data1.length() > 0) {
                        for (int i1 = 0; i1 < data1.length(); i1++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject jsonObject = data1.getJSONObject(i1);


                            String postforride = jsonObject.getString("postforride");

                            JSONObject pos_rent = new JSONObject(postforride);
                            map.put("mobileno", jsonObject.getString("mobileno"));
                            map.put("email", jsonObject.getString("email"));
                            map.put("username", jsonObject.getString("username"));
                            map.put("from", pos_rent.getString("from"));
                            map.put("to", pos_rent.getString("to"));
                            map.put("date", pos_rent.getString("date"));
                            map.put("price", pos_rent.getString("price"));
                            map.put("midwaydrop", pos_rent.getString("midwaydrop"));


                            JSONArray img_ar = jsonObject.getJSONArray("licence");

                            if(img_ar.length()>0){

                                for(int i =0;i<img_ar.length();i++){

                                    JSONObject img_obj =img_ar.getJSONObject(i);

                                    str_pathnew = GET_IMAGE + img_obj.getString("filename");

                                    map.put("path", str_pathnew);


                                }
                            }

                            searchridelist.add(map);
                            Log.e("tag","<---contactList---->"+  searchridelist);


                        }


                    } else
                    {
                        Toast.makeText(getApplicationContext(),"Your desired house/PG is not available Now..",Toast.LENGTH_SHORT).show();
                    }

                    rideAdapter = new RideAdapter1(MorePOst.this, searchridelist);
                    searchlist.setAdapter(rideAdapter);


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
            //progressBar.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost(LOGOUT);
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


                    if (status.equals("true")) {

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

}