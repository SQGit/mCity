package mcity.com.mcity;

import android.app.AlertDialog;
import android.app.TabActivity;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/**
 * Created by Admin on 25-10-2016.
 */
public class MPostHistory extends TabActivity {
    String LOGOUT = Data_Service.URL_API + "logout";
    Typeface tf;
    LinearLayout lnr_back;
    TabHost.TabSpec firstTabSpec,secondTabSpec;
    View tabView,tabViewroom;
    int val=0;
    ImageView img_settings_icon;


    TabHost tabHost;
    String str_token, str_uid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout_history);
        img_settings_icon = (ImageView) findViewById(R.id.settings_icon);


        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        lnr_back=(LinearLayout) findViewById(R.id.back_arrow);
        tabView = createTabView(getApplicationContext(), "House History", R.drawable.house);
        tabViewroom = createTabViewRoom(getApplicationContext(), "PG History", R.drawable.house);
        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");
        firstTabSpec = tabHost.newTabSpec("tid1");
        secondTabSpec = tabHost.newTabSpec("tid1");
        firstTabSpec.setIndicator(tabView).setContent(new Intent(this, HouseHistory.class));

        secondTabSpec.setIndicator(tabViewroom).setContent(new Intent(this, PGHistory.class));

        tabHost.addTab(firstTabSpec);
        tabHost.addTab(secondTabSpec);

        lnr_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        img_settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(MPostHistory.this, img_settings_icon);

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


    private void aboutUs() {

        LayoutInflater layoutInflater = LayoutInflater.from(MPostHistory.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final AlertDialog alertD = new AlertDialog.Builder(MPostHistory.this).create();
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



    private void exitIcon() {

        LayoutInflater layoutInflater = LayoutInflater.from(MPostHistory.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(MPostHistory.this).create();
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
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(MPostHistory.this);
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



    private View createTabView(Context context, String tabText, int home_icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.customlayout, null, true);
        TextView tv = (TextView) view.findViewById(R.id.tabTitleText1);
        tf = Typeface.createFromAsset(this.getAssets(), "mont.ttf");
        tv.setText(tabText);
        tv.setTextSize(12);
        tv.setTypeface(tf);
        return view;
    }

    private View createTabViewRoom(Context context, String tabText, int home_icon) {

        View view = LayoutInflater.from(context).inflate(R.layout.customlayoutroom, null, true);
        TextView tv = (TextView) view.findViewById(R.id.tabTitleText1);
        tf = Typeface.createFromAsset(this.getAssets(), "mont.ttf");

        tv.setText(tabText);
        tv.setTypeface(tf);
        tv.setTextSize(12);
        return view;
    }
    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(MPostHistory.this,Dashboard.class);
        startActivity(i);
        finish();
    }
}