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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Admin on 25-02-2017.
 */
public class CallCouponFromShop extends Activity {
    String LOGOUT = Data_Service.URL_API + "logout";
    String CALL_COUPON = Data_Service.URL_API + "showcouponsfromshop";
    HashMap<String, String> map;
    ProgressBar progressBar;
    public int currentimageindex = 0;
    Dialog dialog2;
    ImageView img_settings_icon,img_slidingimage;
    ListView list;
    CouponCall couponcall;
    String str_token, str_uid,coupons_id;
    ArrayList<HashMap<String, String>> call_coupon_arraylist;
    LinearLayout lnr_back_arrow,lnr_empty;


    private int[] IMAGE_IDS = {
            R.drawable.ad1,R.drawable.ad2};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_coupon);

        list=(ListView)findViewById(R.id.list);
        img_settings_icon=(ImageView)findViewById(R.id.settings_icon);
        lnr_back_arrow=(LinearLayout)findViewById(R.id.back_arrow);
        img_slidingimage = (ImageView) findViewById(R.id.iv);
        lnr_empty=(LinearLayout)findViewById(R.id.lnr_empty);


        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);
        call_coupon_arraylist=new ArrayList<>();
        Intent intent = getIntent();
        coupons_id = intent.getStringExtra("demo_shop_id");
        Log.e("tag","system"+coupons_id);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");

        dialog2 = new Dialog(getApplicationContext());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader1);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);
        lnr_empty.setVisibility(View.GONE);

        final Handler mHandler = new Handler();
        final Runnable mUpdateResults = new Runnable() {
            public void run()
            {
                AnimateandSlideShow();
            }
        };

        int delay = 100; // delay for 1 sec.
        int period = 5000; // repeat every 4 sec.
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                mHandler.post(mUpdateResults);
            }

        }, delay, period);


        callParticularCoupon();

        lnr_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_arrow=new Intent(getApplicationContext(),MShop.class);
                startActivity(back_arrow);
                finish();
            }
        });

        img_settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), img_settings_icon);
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
                                Log.e("tag","111");
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

    private void callParticularCoupon() {

        if (Util.Operations.isOnline(getApplicationContext())) {
            Log.e("tag","check2");
            new CallCoupon().execute();

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
        }

    }

    //************* change option menu typeface settings page.
    private void applyFontToMenuItem(MenuItem mi)
    {
        Typeface font = Typeface.createFromAsset(getAssets(), "mont.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mNewTitle.setSpan(new RelativeSizeSpan(10f), 0, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mi.setTitle(mNewTitle);
    }




    private void aboutUs() {

        LayoutInflater layoutInflater = LayoutInflater.from(CallCouponFromShop.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final AlertDialog alertD = new AlertDialog.Builder(CallCouponFromShop.this).create();
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


    private void exitIcon() {
        LayoutInflater layoutInflater = LayoutInflater.from(CallCouponFromShop.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(CallCouponFromShop.this).create();
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
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(CallCouponFromShop.this);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("login_status","false");
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



    private void AnimateandSlideShow() {
        img_slidingimage.setImageResource(IMAGE_IDS[currentimageindex % IMAGE_IDS.length]);
        currentimageindex++;
        Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.right_left);
        img_slidingimage.startAnimation(rotateimage);
    }

    private class CallCoupon extends AsyncTask<String,String,String> {

        protected void onPreExecute() {
            Log.e("tag", "check3");
            //dialog2.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr;

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("demo_shop_id", coupons_id);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(CALL_COUPON, json, str_uid, str_token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;

        }


        @Override
        protected void onPostExecute(String jsonstr) {
            super.onPostExecute(jsonstr);
            dialog2.dismiss();


            try {
                JSONObject jo = new JSONObject(jsonstr);
                String status = jo.getString("status");


                JSONArray data1 = jo.getJSONArray("message");
                if (data1.length() > 0)
                {
                    {


                        for (int j = 0; j < data1.length(); j++) {

                            JSONObject dataObj = data1.getJSONObject(j);
                            map = new HashMap<String, String>();

                            map.put("_id", dataObj.getString("_id"));
                            Log.e("tag", "1.." + dataObj.getString("_id"));
                            map.put("coupon_code", dataObj.getString("coupon_code"));
                            Log.e("tag", "2.." + dataObj.getString("coupon_code"));
                            map.put("coupon_name", dataObj.getString("coupon_name"));
                            Log.e("tag", "3.." + dataObj.getString("coupon_name"));
                            map.put("coupon_desc", dataObj.getString("coupon_desc"));
                            Log.e("tag", "4.." + dataObj.getString("coupon_desc"));
                            map.put("coupon_expiry_date", dataObj.getString("coupon_expiry_date"));
                            Log.e("tag", "5.." + dataObj.getString("coupon_expiry_date"));

                            JSONArray data2 = dataObj.getJSONArray("shopdetails");
                            for (int k = 0; k < data2.length(); k++) {

                                JSONObject path = data2.getJSONObject(k);
                                map.put("shop_name", path.getString("shop_name"));
                                Log.e("tag", "9.." + path.getString("shop_name"));
                                map.put("shop_logo", path.getString("shop_logo"));
                                Log.e("tag", "10.." + path.getString("shop_logo"));
                            }


                            call_coupon_arraylist.add(map);
                        }

                    }
                   }else
                    {
                        lnr_empty.setVisibility(View.VISIBLE);
                    }




                couponcall = new CouponCall(getApplicationContext(), call_coupon_arraylist,CallCouponFromShop.this);
                list.setAdapter(couponcall);


            }



                catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


        }
    }



    private class Logout extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost(LOGOUT);
                postMethod.addHeader("x-access-token", str_token);
                postMethod.addHeader("id", str_uid);
                postMethod.addHeader("Content-Type", "application/x-www-form-urlencoded");

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



    @Override
    public void onBackPressed()
    {
        Intent back_arrow=new Intent(getApplicationContext(),MShop.class);
        startActivity(back_arrow);
        finish();
    }

}