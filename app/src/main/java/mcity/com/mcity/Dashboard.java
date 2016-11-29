package mcity.com.mcity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.sloop.fonts.FontsManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ramya on 03-09-2016.
 */

public class Dashboard extends AppCompatActivity {

    String LOGOUT = Data_Service.URL_API + "logout";
    LinearLayout lin_mcoupon,lin_shop,lin_auto,lin_train,lin_rental,lin_ride,lin_garage,lin_food,lin_order;
    ImageView img_settings_icon,imv_coupon,img_shop,img_auto;
    TextView txt_coupon,txt_shop,txt_auto,txt_train,txt_rental,txt_ride,txt_garage,txt_desclaimer, txt_site;


   /* public ImageView img_mRental, img_mRides, img_train_icon, img_mauto, img_mfood, img_morder, img_mshop, img_mcoupon, img_mgarage, img_settings_icon, img_slidingimage;
    TextView txt_desclaimer, txt_site;*/
    String str_token, str_uid, str_imagepath;

    SharedPreferences s_pref;
    Intent intent;
    Typeface tf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);


        img_settings_icon = (ImageView) findViewById(R.id.settings_icon);
        lin_mcoupon=(LinearLayout)findViewById(R.id.lin_mcoupon);
        lin_shop=(LinearLayout)findViewById(R.id.lin_shop);
        lin_auto=(LinearLayout)findViewById(R.id.lin_auto);
        lin_train=(LinearLayout)findViewById(R.id.lin_train);
        lin_rental=(LinearLayout)findViewById(R.id.lin_rental) ;
        lin_ride=(LinearLayout)findViewById(R.id.lin_ride) ;
        lin_garage=(LinearLayout)findViewById(R.id.lin_garage) ;
        lin_food=(LinearLayout)findViewById(R.id.lin_food) ;
        lin_order=(LinearLayout) findViewById(R.id.lin_order);

        imv_coupon=(ImageView)findViewById(R.id.imv_coupon);
        img_shop=(ImageView)findViewById(R.id.img_shop);
        img_auto=(ImageView)findViewById(R.id.img_auto);


        txt_coupon=(TextView)findViewById(R.id.txt_coupon);
        txt_shop=(TextView)findViewById(R.id.txt_shop);
        txt_auto=(TextView)findViewById(R.id.txt_auto);
        txt_train=(TextView)findViewById(R.id.txt_train);
        txt_rental=(TextView)findViewById(R.id.txt_rental);
        txt_ride=(TextView)findViewById(R.id.txt_ride);
        txt_garage=(TextView)findViewById(R.id.txt_garage);

        txt_desclaimer = (TextView) findViewById(R.id.txt_desclaimer);
        txt_site = (TextView) findViewById(R.id.txt_site);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        Log.e("tag","check_token"+str_token);
        str_uid = sharedPreferences.getString("id", "");
        Log.e("tag","check_id"+str_uid);

        lin_mcoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_mcoupon.setBackgroundResource(R.drawable.coupon_press_bg);
                txt_coupon.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.coupon_txt_color));
                Intent coupon = new Intent(getApplicationContext(), MCoupon.class);
                startActivity(coupon);
                finish();
            }
        });


        lin_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_shop.setBackgroundResource(R.drawable.coupon_press_bg);
                txt_shop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.shop_text_color));
                Intent coupon = new Intent(getApplicationContext(), MShop.class);
                startActivity(coupon);
                finish();
            }
        });

        lin_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_auto.setBackgroundResource(R.drawable.auto_press_bg);
                txt_auto.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.auto_text_color));
                Intent mauto = new Intent(getApplicationContext(), MAuto.class);
                startActivity(mauto);
                finish();
            }
        });


        lin_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_train.setBackgroundResource(R.drawable.train_press_bg);
                txt_train.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.train_text_color));
                Intent intent = new Intent(getApplicationContext(), TrainSearch.class);
                startActivity(intent);
                finish();
            }
        });

        lin_rental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_rental.setBackgroundResource(R.drawable.rental_press_bg);
                txt_rental.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.auto_text_color));
                Intent intent = new Intent(getApplicationContext(), RentalHistory.class);
                startActivity(intent);
                finish();
            }
        });


        lin_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_ride.setBackgroundResource(R.drawable.rental_press_bg);
                txt_ride.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.rental_text_color));
                Intent intent = new Intent(getApplicationContext(), RideSearch.class);
                startActivity(intent);
                finish();
            }
        });


        lin_garage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "Coming Soon !", TastyToast.LENGTH_LONG, TastyToast.INFO);

                /*lin_garage.setBackgroundResource(R.drawable.auto_press_bg);
                txt_garage.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.auto_text_color));
                Intent garage = new Intent(getApplicationContext(),  MGarage_History.class);
                startActivity(garage);
                finish();*/
            }
        });

        lin_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "Coming Soon !", TastyToast.LENGTH_LONG, TastyToast.INFO);

            }
        });

        lin_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "Coming Soon !", TastyToast.LENGTH_LONG, TastyToast.INFO);

            }
        });



        txt_desclaimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDesclaimerContent();
            }
        });

        txt_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.sqindia.net/"));
                startActivity(browserIntent);
                finish();
            }
        });

        img_settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Dashboard.this, img_settings_icon);
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
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "mont.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    private void aboutUs() {

        LayoutInflater layoutInflater = LayoutInflater.from(Dashboard.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final AlertDialog alertD = new AlertDialog.Builder(Dashboard.this).create();
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


    private void showDesclaimerContent() {

        final Dialog dialog = new Dialog(Dashboard.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        //adding text dynamically
        TextView txt_head2 = (TextView) dialog.findViewById(R.id.txt_head2);
        TextView txt_msg = (TextView) dialog.findViewById(R.id.txt_msg);
        TextView txt_msg2 = (TextView) dialog.findViewById(R.id.txt_msg2);
        Button btn_ok2 = (Button) dialog.findViewById(R.id.btn_ok2);

        Typeface tt = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        txt_head2.setTypeface(tt);
        txt_msg.setTypeface(tt);
        btn_ok2.setTypeface(tt);
        txt_msg2.setTypeface(tt);

        txt_msg.setText(
                "This mCity App has no direct or indirect connection with Mahindra Group in any sort or form." +
                "It is completely self funded and wants to provide an Independent medium of Communication. Future growth may depend on Ad revenues." +
                "The purpose of mCity App is to help the locals for the locals." +
                "The data collected with this app will NOT be shared or forwarded.We respect privacy.\n \n \n This App requires an Email configured in your mobile & an Internet Connection. \n\n\n        There are many more ideas and concepts that will be coming in the near future.\n" +
                "Please send your valuable feedback,concerns and suggestions to ");
        txt_msg2.setText("info@sqindia.net");


        txt_msg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "info@sqindia.net", null));

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MCity");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(emailIntent);
            }
        });
        btn_ok2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();

    }


    private void exitIcon() {

        LayoutInflater layoutInflater = LayoutInflater.from(Dashboard.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(Dashboard.this).create();
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
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(Dashboard.this);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("login_status", "false");
                editor.commit();
                logoutMethod();
                //alertD.dismiss();
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

            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.e("tag", "<-----Status----->" + status);
                Log.e("tag", "<-----msg----->" + msg);

                if (status.equals("true")) {

                Log.e("tag","first");
                    TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    Intent intent_logout=new Intent(Dashboard.this,Test_L.class);
                    startActivity(intent_logout);
                    finish();


                } else { Log.e("tag","second");

                    TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                   // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        showExit();
    }

    private void showExit() {


        LayoutInflater layoutInflater = LayoutInflater.from(Dashboard.this);
        View promptView = layoutInflater.inflate(R.layout.exitlogin, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(Dashboard.this).create();
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
                Dashboard.super.onBackPressed();
                onRestart();
                Intent i1 = new Intent(Intent.ACTION_MAIN);
                i1.setAction(Intent.ACTION_MAIN);
                i1.addCategory(Intent.CATEGORY_HOME);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i1);
                alertD.dismiss();
                finish();
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
}
