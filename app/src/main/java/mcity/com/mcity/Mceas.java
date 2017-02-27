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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 09-01-2017.
 */
public class Mceas extends Activity {
    String POST_MCEAS = Data_Service.URL_API + "updateceas";
    String LOGOUT = Data_Service.URL_API + "logout";
    TextView txt_con_one,txt_con_two;
    private CheckBox chk_one, chk_two, chk_three,chk_four;
    LinearLayout back_arrow;
    String token, uid;
    ProgressBar progressBar;
    ImageView imgview_submit,img_settings_icon;
    String str_con1,str_con2,str_token, str_uid,str_getnotify,str_volunteer;
    Dialog dialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mceas);

        txt_con_one=(TextView)findViewById(R.id.txt_con_one);
        txt_con_two=(TextView)findViewById(R.id.txt_con_two);
        back_arrow=(LinearLayout)findViewById(R.id.back_arrow);
        chk_one=(CheckBox)findViewById(R.id.chkone);
        chk_two=(CheckBox)findViewById(R.id.chktwo);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        chk_three=(CheckBox)findViewById(R.id.chkthree);
        imgview_submit=(ImageView) findViewById(R.id.submit);
        chk_four=(CheckBox)findViewById(R.id.chkfour);
        img_settings_icon = (ImageView) findViewById(R.id.settings_icon);
        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString("token", "");
        uid = sharedPreferences.getString("id", "");
        Log.e("tag","check_token"+token);
        Log.e("tag","check_id"+uid);
        progressBar.setVisibility(View.GONE);

        dialog2 = new Dialog(Mceas.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(),Dashboard.class);
                startActivity(i);
                finish();
            }
        });


        chk_one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked){
                    str_con1=chk_one.getText().toString();
                    Log.e("tag","000"+str_con1);
                    chk_two.setChecked(false);

                }
            }
        });


        chk_two.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked){
                    chk_one.setChecked(false);
                    str_con1=chk_two.getText().toString();


                    if(chk_two.isChecked())
                    {

                    }
                }
            }

        });

        chk_three.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked){
                    chk_four.setChecked(false);
                    str_con2=chk_three.getText().toString();
                }
            }

        });

        chk_four.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked){
                    chk_three.setChecked(false);
                    str_con2=chk_four.getText().toString();
                }
            }

        });


        imgview_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("tag","hhhh"+str_con1+str_con2);
                if(str_con1==null)
                {
                    str_getnotify="no";
                }
                else if(str_con1.equals("Yes via Sms"))
                {
                    str_getnotify="yes";
                }
                else if(str_con1.equals("No I do not want to be notified."))
                {
                    str_getnotify="no";
                }



                if(str_con2==null)
                {
                    str_volunteer="no";
                }
                else if(str_con2.equals("Yes"))
                {
                    str_volunteer="yes";

                }
                else if(str_con2.equals("No"))
                {
                    str_volunteer="no";
                }



                if (Util.Operations.isOnline(Mceas.this)) {

                        new PostCeasAsync(str_getnotify, str_volunteer).execute();


                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                }

            }
        });


        img_settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Mceas.this, img_settings_icon);
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

    //************* change option menu typeface settings page.
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "mont.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    private void aboutUs() {

        LayoutInflater layoutInflater = LayoutInflater.from(Mceas.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final AlertDialog alertD = new AlertDialog.Builder(Mceas.this).create();
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


    private void exitIcon() {

        LayoutInflater layoutInflater = LayoutInflater.from(Mceas.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(Mceas.this).create();
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
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(Mceas.this);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("login_status", "false");
                editor.commit();
                logoutMethod();
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
            dialog2.show();
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
            dialog2.dismiss();

            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.e("tag", "<-----Status----->" + status);
                Log.e("tag", "<-----msg----->" + msg);

                if (status.equals("true")) {

                    Log.e("tag","first");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    Intent intent_logout=new Intent(Mceas.this,Test_L.class);
                    startActivity(intent_logout);
                    finish();


                } else { Log.e("tag","second");

                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    private class PostCeasAsync extends AsyncTask<String,String,String>{
        String str_getnotify, str_volunteer;


        public PostCeasAsync(String str_getnotify, String str_volunteer) {
            this.str_getnotify=str_getnotify;
            this.str_volunteer=str_volunteer;

        }


        protected void onPreExecute() {
            dialog2.show();
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("getnotify", str_getnotify);
                jsonObject.accumulate("volunteer",str_volunteer);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(POST_MCEAS, json, uid,token );
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            progressBar.setVisibility(View.VISIBLE);
            super.onPostExecute(jsonStr);
            dialog2.dismiss();

            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");


                if (status.equals("true")) {

                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    Intent iii=new Intent(getApplicationContext(),Dashboard.class);
                    startActivity(iii);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(Mceas.this, Dashboard.class);
        startActivity(i);
        finish();
    }

}


