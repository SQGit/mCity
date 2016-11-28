package mcity.com.mcity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
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
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Admin on 28-09-2016.
 */
public class RideSearch extends Activity implements AdapterView.OnItemSelectedListener {
    String URL = Data_Service.URL_API + "searchforride";
    String URL1 = Data_Service.URL_API + "searchforridefilter";
    String IMAGE_UPLOAD = Data_Service.URL_IMG + "licence/";
    String LOGOUT = Data_Service.URL_API + "logout";

    ProgressDialog mProgressDialog;
    Spinner search_from, search_go;
    TextView advanced_search, offer, morepost;
    ListView searchlist;
    //ImageView img_offerride;
    LinearLayout back_arrow;
    String token, uid;
    RideAdapter rideAdapter;
    Button filter;
    ProgressBar progressBar;
    ArrayList<HashMap<String, String>> searchridelist;
    ArrayList<HashMap<String, String>> normal_searchridelist;
    String str_from, str_to,pathnew;

    static String from = "from";
    static String to = "to";
    static String date = "date";
    static String mobileno = "mobileno";
    static String email = "email";
    static String price = "price";
    static String midwaydrop="midwaydrop";
    static String phone="phone";
    static String username="username";

    Typeface tf;
    List<String> fromadd;
    List<String> toadd;
    ImageView settings_icon;
    LinearLayout lin_offer_ride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.ride_search);

        search_from = (Spinner) findViewById(R.id.search_from);
        search_go = (Spinner) findViewById(R.id.search_go);
        offer = (TextView) findViewById(R.id.offer);

        searchridelist = new ArrayList<>();
        normal_searchridelist=new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        advanced_search = (TextView) findViewById(R.id.advanced_search);
        searchlist = (ListView) findViewById(R.id.searchlist);
        filter = (Button) findViewById(R.id.filter);
        settings_icon = (ImageView) findViewById(R.id.settings_icon);
        back_arrow = (LinearLayout) findViewById(R.id.back_arrow);
        morepost = (TextView) findViewById(R.id.morepost);
        lin_offer_ride=(LinearLayout)findViewById(R.id.lin_offer_ride);
        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);


        search_from.setOnItemSelectedListener(this);
        search_go.setOnItemSelectedListener(this);


        SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = s_pref.edit();

        edit.putString("view_post", "");
        edit.commit();



        // Spinner Drop down elements
        fromadd = new ArrayList<String>();
        fromadd.add("Start Location");
        fromadd.add("Aqualily/BMW");
        fromadd.add("Canopy/Sylvan County");
        fromadd.add("Iris Court");
        fromadd.add("Nova");
        fromadd.add("MRV");
        fromadd.add("Infosys Main Gate");
        fromadd.add("Zero Point");

        //List<String> toadd = new ArrayList<String>();

        toadd = new ArrayList<String>();
        toadd.add("Destination Location");
        toadd.add("Tambaram");
        toadd.add("Chengalpattu");
        toadd.add("Tnagar");
        toadd.add("Central Station");
        toadd.add("Chennai Airport");
        toadd.add("Paranur Station");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fromadd);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        search_from.setAdapter(dataAdapter);


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, toadd);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        search_go.setAdapter(dataAdapter1);
        setSpinner1();
        setSpinner2();



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString("token", "");
        uid = sharedPreferences.getString("id", "");

        //search_go.addTextChangedListener(passwordWatcher);

        new RideSearchAsync().execute();


        settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag", "DDD");


                PopupMenu popup = new PopupMenu(RideSearch.this, settings_icon);
                //Inflating the Popup using xml file
                // popup.getMenuInflater().inflate(R.menu.opt_menu, popup.getMenu());

                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.opt_menu, popup.getMenu());
                MenuItem pinMenuItem1 = popup.getMenu().getItem(0);
                MenuItem pinMenuItem2 = popup.getMenu().getItem(1);
                MenuItem pinMenuItem3 = popup.getMenu().getItem(2);

                applyFontToMenuItem(pinMenuItem1);
                applyFontToMenuItem(pinMenuItem2);
                applyFontToMenuItem(pinMenuItem3);

                //registering popup with OnMenuItemClickListener
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

                popup.show();//showing popup menu

            }
        });



        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                str_from=search_from.getSelectedItem().toString();
                str_to=search_go.getSelectedItem().toString();
                Log.e("tag","^^^^^^^"+str_to);

                SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor edit = s_pref.edit();
                edit.putString("view_post", "filter");
                edit.putString("from",str_from);
                edit.putString("to",str_to);
                edit.commit();

                if (Util.Operations.isOnline(RideSearch.this)) {
                    if (!str_from.equals("Start Location") && !str_to.equals("Destination Location") )
                    {
                        searchridelist.clear();
                        new filterAsync(str_from,str_to).execute();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please select Source & Destination..",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_SHORT).show();
                }



            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(i);
                finish();
            }
        });


        morepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchlist != null) {
                    Intent i = new Intent(getApplicationContext(), MorePOst.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    TastyToast.makeText(getApplicationContext(), "Sorry, No Rides are Available", TastyToast.LENGTH_LONG, TastyToast.INFO);
                }


            }
        });

        advanced_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  if( searchridelist.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"No one can post MRide",Toast.LENGTH_LONG).show();
                }*/

               /* str_from=search_from.getSelectedItem().toString();
                str_to=search_go.getSelectedItem().toString();*/
                Intent i = new Intent(getApplicationContext(), AdvancedSearch.class);
               /* i.putExtra("from_location", str_from);
                i.putExtra("to_location",str_to);*/
                startActivity(i);
                finish();
            }
        });

        lin_offer_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OfferRide.class);
                startActivity(i);
                finish();
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

        LayoutInflater layoutInflater = LayoutInflater.from(RideSearch.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(RideSearch.this).create();
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
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(RideSearch.this);
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

        LayoutInflater layoutInflater = LayoutInflater.from(RideSearch.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final AlertDialog alertD = new AlertDialog.Builder(RideSearch.this).create();
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
    private void setSpinner2() {
        final CustomAdapter arrayAdapter1 = new CustomAdapter(this, android.R.layout.simple_spinner_item, toadd) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }




            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                tv.setTypeface(tf);

                if (position == 0) {
                    tv.setTextColor(Color.RED);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        search_go.setAdapter(arrayAdapter1);

    }


    private void setSpinner1() {

        final CustomAdapter arrayAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, fromadd) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }


            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                tv.setTypeface(tf);

                if (position == 0) {
                    tv.setTextColor(Color.RED);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        search_from.setAdapter(arrayAdapter);
    }


    private final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Log.e("tag","%%%"+item);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class RideSearchAsync extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            String id = "";
            try {

                //location,landmark,address,roomtype,monthlyrent,gender,description
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(URL, json, uid, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;

        }



        @Override
        protected void onPostExecute(String jsonstr) {
            Log.e("tag", "<-----111111111--------->" + jsonstr);
            super.onPostExecute(jsonstr);
            progressBar.setVisibility(View.GONE);

            if (jsonstr.equals("")) {

                Toast.makeText(getApplicationContext(),"Check Network Connection",Toast.LENGTH_LONG).show();

            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("status");
                    JSONArray data1 = jo.getJSONArray("message");
                    Log.e("tag","...#...1"+data1);





                    if (data1.length() > 0) {
                        for (int i1 = 0; i1 < data1.length(); i1++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject jsonObject = data1.getJSONObject(i1);


                            String postforride = jsonObject.getString("postforride");

                            JSONObject pos_rent = new JSONObject(postforride);
                            Log.e("tag","pos_rent"+  pos_rent);

                            map.put("mobileno", jsonObject.getString("mobileno"));
                            map.put("email", jsonObject.getString("email"));
                            map.put("username", jsonObject.getString("username"));

                            map.put("from", pos_rent.getString("from"));
                            map.put("to", pos_rent.getString("to"));
                            map.put("date", pos_rent.getString("date"));
                            map.put("price", pos_rent.getString("price"));
                            map.put("midwaydrop", pos_rent.getString("midwaydrop"));
                            map.put("phone", pos_rent.getString("phone"));
                            Log.e("tag","please"+pos_rent.getString("phone"));


                            JSONArray img_ar = jsonObject.getJSONArray("licence");

                            Log.e("tag","pos_rent_image"+  img_ar);

                            if(img_ar.length()>0){

                                for(int i =0;i<img_ar.length();i++){

                                    JSONObject img_obj =img_ar.getJSONObject(i);

                                    pathnew = IMAGE_UPLOAD + img_obj.getString("filename");

                                    Log.e("tag", "data: " + pathnew);

                                    map.put("path", pathnew);


                                }
                            }

                            normal_searchridelist.add(map);
                            Log.e("tag","<---contactList---->"+  normal_searchridelist);


                        }


                    } else
                    {
                        //Toast.makeText(getApplicationContext(),"No Rides are Available in this Location..",Toast.LENGTH_LONG).show();
                    }


                    Log.e("tag","normal_searchridelist"+normal_searchridelist);

                    rideAdapter = new RideAdapter(RideSearch.this, normal_searchridelist);
                    searchlist.setAdapter(rideAdapter);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
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
                Log.e("tag","id"+uid);
                Log.e("tag","token"+token);
                //location,landmark,address,roomtype,monthlyrent,gender,description
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("from", source);
                jsonObject.accumulate("to", end);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(URL1, json, uid, token);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String jsonstr) {
            Log.e("tag", "<-----111111111--------->" + jsonstr);
            super.onPostExecute(jsonstr);
            progressBar.setVisibility(View.GONE);

            if (jsonstr.equals("")) {

                Toast.makeText(getApplicationContext(),"Check Network Connection",Toast.LENGTH_LONG).show();

            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("status");
                    JSONArray data1 = jo.getJSONArray("message");
                    Log.e("tag","...#...1"+data1);





                    if (data1.length() > 0) {
                        for (int i1 = 0; i1 < data1.length(); i1++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject jsonObject = data1.getJSONObject(i1);


                            String postforride = jsonObject.getString("postforride");

                            JSONObject pos_rent = new JSONObject(postforride);
                            Log.e("tag","pos_rent"+  pos_rent);

                            map.put("mobileno", jsonObject.getString("mobileno"));
                            map.put("email", jsonObject.getString("email"));
                            map.put("username", jsonObject.getString("username"));

                            map.put("from", pos_rent.getString("from"));
                            map.put("to", pos_rent.getString("to"));
                            map.put("date", pos_rent.getString("date"));
                            map.put("price", pos_rent.getString("price"));
                            map.put("midwaydrop", pos_rent.getString("midwaydrop"));
                            map.put("phone", pos_rent.getString("phone"));


                            JSONArray img_ar = jsonObject.getJSONArray("licence");

                            Log.e("tag","pos_rent_image"+  img_ar);

                            if(img_ar.length()>0){

                                for(int i =0;i<img_ar.length();i++){

                                    JSONObject img_obj =img_ar.getJSONObject(i);

                                    pathnew = IMAGE_UPLOAD + img_obj.getString("filename");

                                    Log.e("tag", "data: " + pathnew);

                                    map.put("path", pathnew);


                                }
                            }

                            searchridelist.add(map);
                            Log.e("tag","<---contactList---->"+  searchridelist);


                        }


                    } else
                    {
                        Toast.makeText(getApplicationContext(),"No Rides are Available in this Location..",Toast.LENGTH_LONG).show();
                    }


                    Log.e("tag","normal_searchridelist"+searchridelist);

                    rideAdapter = new RideAdapter(RideSearch.this, searchridelist);
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
        Intent i = new Intent(RideSearch.this,Dashboard.class);
        startActivity(i);
        finish();
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







