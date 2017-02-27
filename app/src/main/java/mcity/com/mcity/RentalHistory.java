package mcity.com.mcity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.sloop.fonts.FontsManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Admin on 09-11-2016.
 */

public class RentalHistory extends TabActivity {
    String LOGOUT = Data_Service.URL_API + "logout";
    LinearLayout back_arrow;
    public int currentimageindex = 0;
    Typeface tf;
    LinearLayout back;
    TabHost.TabSpec firstTabSpec1, secondTabSpec1;
    View tabView1, tabViewroom1;
    int val = 0;
    TabHost tabHost1;
    ImageView settings_icon, slidingimage;
    String token, uid;

    LinearLayout lt_postproperty, lt_searchproperty;
    private int[] IMAGE_IDS = {
            R.drawable.ad1, R.drawable.ad2};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rental_history);


        back_arrow = (LinearLayout) findViewById(R.id.back_arrow);
        slidingimage = (ImageView) findViewById(R.id.iv);
        lt_postproperty = (LinearLayout) findViewById(R.id.layout_postproperty);
        lt_searchproperty = (LinearLayout) findViewById(R.id.layout_searchproperty);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        final Handler mHandler = new Handler();
        final Runnable mUpdateResults = new Runnable() {
            public void run() {
                AnimateandSlideShow();
            }
        };
        int delay = 30; // delay for 1 sec.
        int period = 5000; // repeat every 4 sec.
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                mHandler.post(mUpdateResults);

            }

        }, delay, period);


        lt_postproperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post = new Intent(getApplicationContext(), MRentalPost.class);
                startActivity(post);
                finish();
            }
        });


        lt_searchproperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post = new Intent(getApplicationContext(), MRental.class);
                startActivity(post);
                finish();
            }

        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(post);
                finish();
            }
        });

        tabHost1 = (TabHost) findViewById(android.R.id.tabhost);
        back = (LinearLayout) findViewById(R.id.back_arrow);
        settings_icon = (ImageView) findViewById(R.id.settings_icon);
        tabView1 = createTabView(getApplicationContext(), "House", R.drawable.house);
        tabViewroom1 = createTabViewRoom(getApplicationContext(), "Paying Guest", R.drawable.house);
        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString("token", "");
        uid = sharedPreferences.getString("id", "");


        settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(RentalHistory.this, settings_icon);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.opt_menu2, popup.getMenu());
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
                                Intent history = new Intent(getApplicationContext(), MPostHistory.class);
                                startActivity(history);
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


        //*************************************************
        firstTabSpec1 = tabHost1.newTabSpec("tid1");
        secondTabSpec1 = tabHost1.newTabSpec("tid1");
        Intent intent = new Intent(this, HistoryHouse.class);
        firstTabSpec1.setIndicator(tabView1).setContent(intent);

        secondTabSpec1.setIndicator(tabViewroom1).setContent(new Intent(this, HistoryPg.class));
        tabHost1.addTab(firstTabSpec1);
        tabHost1.addTab(secondTabSpec1);
    }


    private void AnimateandSlideShow() {

        slidingimage.setImageResource(IMAGE_IDS[currentimageindex % IMAGE_IDS.length]);
        currentimageindex++;
        Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.right_left);
        slidingimage.startAnimation(rotateimage);
    }


    //************* change option menu typeface settings page.
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "mont.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    private void exitIcon() {
        LayoutInflater layoutInflater = LayoutInflater.from(RentalHistory.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(RentalHistory.this).create();
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
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(RentalHistory.this);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("check", "");
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

        LayoutInflater layoutInflater = LayoutInflater.from(RentalHistory.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final AlertDialog alertD = new AlertDialog.Builder(RentalHistory.this).create();
        alertD.setCancelable(false);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.txt_head2);
        final TextView txt_msg = (TextView) promptView.findViewById(R.id.txt_msg);
        final Button yes = (Button) promptView.findViewById(R.id.btn_ok2);


        txt_msg.setText("    SQIndia is a total Information Technology Company based out of Guduvanchery.SQIndia has its own Software Development Centre and provides Technology Consulting Services to its clients in India,US,UK and Singapore.Some of its Elite Customers include Mahindra,TVS,Nissan,ZOHO.\n\n              SQIndia also operates 2 Exclusive Lenovo Outlets - Guduvanchery and Chengalpet.SQIndia also has a MultiBranded Mobile showroom with a LIVE DEMO counters.\n\n          The aspirations to grow and serve people in all aspects has always been part of the Company's motto. Mr.Gopi who is the CEO/Founder of the Organization has spent more than a decade in the US and strives to make things easy and accessible for a common man.");
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




    private View createTabView(Context context, String tabText, int home_icon) {

        View view = LayoutInflater.from(context).inflate(R.layout.customlayout, null, true);
        TextView tv = (TextView) view.findViewById(R.id.tabTitleText1);
        tf = Typeface.createFromAsset(this.getAssets(), "mont.ttf");
        tv.setText(tabText);
        tv.setTypeface(tf);
        tv.setTextSize(14);
        return view;
    }

    private View createTabViewRoom(Context context, String tabText, int home_icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.customlayoutroom, null, true);
        TextView tv1 = (TextView) view.findViewById(R.id.tabTitleText1);
        tf = Typeface.createFromAsset(this.getAssets(), "mont.ttf");
        tv1.setText(tabText);
        tv1.setTypeface(tf);
        tv1.setTextSize(14);
        return view;
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
                postMethod.addHeader("x-access-token", token);
                postMethod.addHeader("id", uid);
                postMethod.addHeader("Content-Type", "multipart-form-data");
                HttpResponse response = client.execute(postMethod);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                Log.e("tag", "res " + response.getStatusLine().toString());
                if (statusCode == 200) {
                    json = EntityUtils.toString(r_entity);

                    JSONObject result1 = new JSONObject(json);
                    String status = result1.getString("status");
                    Log.e("tag", "status..........." + status);

                    if (status.equals("true")) {
                        Log.e("tag", "Success...........");
                    }
                } else {
                    json = "Error occurred! Http Status Code: " + statusCode;
                    Log.e("tag", "failure...........");
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

                if (status.equals("true")) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    Intent exit = new Intent(getApplicationContext(), Test_L.class);
                    startActivity(exit);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    Log.e("tag", "no...........");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }




    @Override
    public void onBackPressed() {
        Log.e("tag","111");
        Intent i = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(i);
        finish();
    }

}

