package mcity.com.mcity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 23-01-2017.
 */
public class SearchSellActivity extends Activity {
    String LOGOUT = Data_Service.URL_API + "logout";
    String URL_SEARCH_SELL = Data_Service.URL_API + "searchforsell";
    String SHOW_IMAGE = Data_Service.URL_IMG + "sell/";
    HashMap<String, String> map;
    String category_value,str_token,str_uid;
    ArrayList<HashMap<String, String>> search_sell_array;
    MSearchAdapter search_garage_adapter;
    ListView list_ga;
    ProgressBar progressBar;
    Dialog dialog2;
    LinearLayout back_arrow,lnr_empty;
    ImageView settings_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sell);
        category_value = getIntent().getExtras().getString("category");
        Log.e("tag","checking_category"+category_value);
        back_arrow=(LinearLayout)findViewById(R.id.back_arrow);
        list_ga=(ListView)findViewById(R.id.list_ga);
        lnr_empty=(LinearLayout)findViewById(R.id.lnr_empty);
        search_sell_array=new ArrayList<>();

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        dialog2 = new Dialog(SearchSellActivity.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);

        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);
        settings_icon=(ImageView)findViewById(R.id.settings_icon);
        lnr_empty.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");

        if (Util.Operations.isOnline(SearchSellActivity.this)) {


            new SearchSellAsync(category_value).execute();


        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
        }

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post=new Intent(getApplicationContext(),MGarage_History.class);
                startActivity(post);
                finish();
            }
        });

        settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(SearchSellActivity.this, settings_icon);
                popup.getMenuInflater().inflate(R.menu.opt_menu1, popup.getMenu());

                MenuInflater inflater = popup.getMenuInflater();
                MenuItem pinMenuItem1 = popup.getMenu().getItem(0);
                MenuItem pinMenuItem2 = popup.getMenu().getItem(1);

                applyFontToMenuItem(pinMenuItem1);
                applyFontToMenuItem(pinMenuItem2);

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

    }

    private void exitIcon() {

        LayoutInflater layoutInflater = LayoutInflater.from(SearchSellActivity.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(SearchSellActivity.this).create();
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
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(SearchSellActivity.this);
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



        if (Util.Operations.isOnline(SearchSellActivity.this)) {


            new Logout().execute();


        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
        }

    }

    private void aboutUs() {

        LayoutInflater layoutInflater = LayoutInflater.from(SearchSellActivity.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final AlertDialog alertD = new AlertDialog.Builder(SearchSellActivity .this).create();
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


    //************* change option menu typeface settings page.
    private void applyFontToMenuItem(MenuItem mi)
    {
        Typeface font = Typeface.createFromAsset(getAssets(), "mont.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public void onBackPressed() {
        Intent post = new Intent(SearchSellActivity.this,MGarage_History.class);
        startActivity(post);
        finish();
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

                    if (status.equals("true"))
                    {
                    }
                } else
                {
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
            dialog2.dismiss();

            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");


                if (status.equals("true"))
                {

                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    Intent exit=new Intent(getApplicationContext(),Test_L.class);
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



    private class SearchSellAsync extends AsyncTask<String,String,String> {

        String category_value;

        public SearchSellAsync(String category_value) {

            this.category_value=category_value;
        }



        protected void onPreExecute() {
            super.onPreExecute();
            dialog2.show();
        }




        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            String id = "";
            try {

                //location,landmark,address,roomtype,monthlyrent,gender,description
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("category",category_value);
                json = jsonObject.toString();

              return jsonStr = HttpUtils.makeRequest1(URL_SEARCH_SELL, json, str_uid, str_token);
            }


            catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(String responseString) {

            super.onPostExecute(responseString);
            dialog2.dismiss();


            try {
                JSONObject jo = new JSONObject(responseString);
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

                            JSONArray userdetails = jsonObject.getJSONArray("userdetails");
                            Log.e("tag", "userdetails" + userdetails);

                            for(int u =0;u<userdetails.length();u++){
                                JSONObject user_obj =userdetails.getJSONObject(u);
                                map.put("email", user_obj.getString("email"));
                                Log.e("tag", "07" + user_obj.getString("email"));
                                map.put("username", user_obj.getString("username"));
                                Log.e("tag", "08" + user_obj.getString("username"));
                            }
                            Log.e("tag","hghg");
                            JSONArray pos_garage_array = jsonObject.getJSONArray("imageurl");
                            Log.e("tag","jjjjjjj"+pos_garage_array);
                            for (int j = 0; j < pos_garage_array.length(); j++) {
                                JSONObject pos_rent = pos_garage_array.getJSONObject(j);

                                String path = SHOW_IMAGE + pos_rent.getString("filename");
                                map.put("path" + j, path);
                            }
                            lnr_empty.setVisibility(View.GONE);
                            search_sell_array.add(map);
                        }
                        search_garage_adapter = new MSearchAdapter(SearchSellActivity.this, search_sell_array);
                        list_ga.setAdapter(search_garage_adapter);

                    }
                    else
                    {
                        lnr_empty.setVisibility(View.VISIBLE);
                        //exitIcon1();
                       // Toast.makeText(getApplicationContext(), "Sorry, No relevent data available... ", Toast.LENGTH_LONG).show();
                    }
                }
            }
            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void exitIcon1() {

        LayoutInflater layoutInflater = LayoutInflater.from(SearchSellActivity.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog1, null);
        final AlertDialog alertD = new AlertDialog.Builder(SearchSellActivity.this).create();
        alertD.setCancelable(false);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.head1);
        final TextView head2 = (TextView) promptView.findViewById(R.id.head2);

        final Button yes = (Button) promptView.findViewById(R.id.yes);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        head1.setTypeface(tf);
        head2.setTypeface(tf);
        yes.setTypeface(tf);
        head1.setText("INFO");
        head2.setText("Sorry, No Relevant data Available...");


        yes.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                alertD.dismiss();
                Intent ff=new Intent(getApplicationContext(),MGarage_History.class);
                startActivity(ff);
                finish();
            }
        });

        alertD.setView(promptView);
        alertD.show();


    }

}
