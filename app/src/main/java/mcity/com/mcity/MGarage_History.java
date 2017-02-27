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
import android.support.design.widget.FloatingActionButton;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * Created by Admin on 22-11-2016.
 */
public class MGarage_History  extends Activity {
    String LOGOUT = Data_Service.URL_API + "logout";
    String ALL_GARAGE = Data_Service.URL_API + "allsell";
    String SHOW_IMAGE = Data_Service.URL_IMG + "sell/";

    LinearLayout btn_postAd;
    LinearLayout btn_searchAd,lnr_empty;
    String str_category;
    LinearLayout back_arrow,layout_postproperty;
    TextView text_info;
    MgarageAdapter garageadapter;
    ImageView settings_icon;
    String str_uid,str_token;
    HashMap<String, String> map;
    ArrayList<HashMap<String, String>> garageadapt;
    ListView list_garage;
    ProgressBar progressBar;
    Dialog dialog2;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mgarage_history);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);
        garageadapt=new ArrayList<>();

        btn_postAd = (LinearLayout) findViewById(R.id.layout_postads);
        lnr_empty=(LinearLayout)findViewById(R.id.lnr_empty);
        back_arrow=(LinearLayout)findViewById(R.id.back_arrow);
        list_garage=(ListView)findViewById(R.id.list_garage);
        settings_icon=(ImageView)findViewById(R.id.settings_icon);
        //text_info=(TextView)findViewById(R.id.text_info);
        layout_postproperty=(LinearLayout)findViewById(R.id.layout_postproperty);

        fab = (FloatingActionButton) findViewById(R.id.fab);


        dialog2 = new Dialog(MGarage_History.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");
        Log.e("tag","aaa"+str_uid+  str_token);

        //text_info.setText("No relevant data available... If u want post ADS please click POSTADS link.");
        lnr_empty.setVisibility(View.GONE);

        if (Util.Operations.isOnline(MGarage_History.this)) {
            new HistoryGarage().execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No Internet Connectivity..Please Check",Toast.LENGTH_LONG).show();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchCategory();
            }
        });

        layout_postproperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post_sell = new Intent(MGarage_History.this,MGarage_PostAds.class);
                startActivity(post_sell);
                finish();
            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post=new Intent(getApplicationContext(),Dashboard.class);
                startActivity(post);
                finish();
            }
        });

        settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MGarage_History.this, settings_icon);
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
                                Intent intent=new Intent(getApplicationContext(),MyAddHistory.class);
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

    private void searchCategory() {

            LayoutInflater layoutInflater = LayoutInflater.from(MGarage_History.this);
            View promptView = layoutInflater.inflate(R.layout.search_category_list, null);
            final android.support.v7.app.AlertDialog alertD1 = new android.support.v7.app.AlertDialog.Builder(MGarage_History.this).create();
            alertD1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            alertD1.setCancelable(true);
            Window window = alertD1.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            final TextView tv_head = (TextView) promptView.findViewById(R.id.txt_head);
            final ImageView cross =(ImageView) promptView.findViewById(R.id.cross);
            final LinearLayout lnr_bike=(LinearLayout) promptView.findViewById(R.id.lnr_bike);
            final LinearLayout lnr_car=(LinearLayout) promptView.findViewById(R.id.lnr_car);
            final LinearLayout lnr_elec=(LinearLayout) promptView.findViewById(R.id.lnr_elec);
            final LinearLayout lnr_mobile=(LinearLayout) promptView.findViewById(R.id.lnr_mobile);
            final LinearLayout lnr_furniture=(LinearLayout) promptView.findViewById(R.id.lnr_furniture);
            final LinearLayout lnr_book=(LinearLayout) promptView.findViewById(R.id.lnr_book);
            final LinearLayout lnr_pets=(LinearLayout) promptView.findViewById(R.id.lnr_pets);

            Typeface tff = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
            tv_head.setTypeface(tff);


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD1.dismiss();
            }
        });


        lnr_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnr_bike.setBackgroundResource(R.drawable.sell_fill_circle);
                lnr_car.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_elec.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_mobile.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_furniture.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_book.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_pets.setBackgroundResource(R.drawable.sell_search_circle);
                str_category="Bikes, Bike Spares, Accessories";

                Intent newActivity1 = new Intent(getApplicationContext(), SearchSellActivity.class);
                newActivity1.putExtra("category", str_category);
                startActivity(newActivity1);
                finish();

            }
        });



        lnr_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnr_car.setBackgroundResource(R.drawable.sell_fill_circle);
                lnr_pets.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_elec.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_mobile.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_furniture.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_book.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_bike.setBackgroundResource(R.drawable.sell_search_circle);
                str_category="Cars, Car Spare Parts";


                Intent newActivity2 = new Intent(getApplicationContext(), SearchSellActivity.class);
                newActivity2.putExtra("category", str_category);
                startActivity(newActivity2);
            }
        });


        lnr_elec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnr_elec.setBackgroundResource(R.drawable.sell_fill_circle);
                lnr_bike.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_car.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_mobile.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_furniture.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_book.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_pets.setBackgroundResource(R.drawable.sell_search_circle);
                str_category="Electronics & Home Appliances";

                Intent newActivity3 = new Intent(getApplicationContext(), SearchSellActivity.class);
                newActivity3.putExtra("category", str_category);
                startActivity(newActivity3);
            }
        });

        lnr_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnr_mobile.setBackgroundResource(R.drawable.sell_fill_circle);
                lnr_car.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_elec.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_bike.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_furniture.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_book.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_pets.setBackgroundResource(R.drawable.sell_search_circle);
                str_category="Mobiles & Mobile Accessories";

                Intent newActivity4 = new Intent(getApplicationContext(), SearchSellActivity.class);
                newActivity4.putExtra("category", str_category);
                startActivity(newActivity4);
            }
        });

        lnr_furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnr_furniture.setBackgroundResource(R.drawable.sell_fill_circle);
                lnr_car.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_elec.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_mobile.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_bike.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_book.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_pets.setBackgroundResource(R.drawable.sell_search_circle);
                str_category="Furniture, Household Articles";


                Intent newActivity5 = new Intent(getApplicationContext(), SearchSellActivity.class);
                newActivity5.putExtra("category", str_category);
                startActivity(newActivity5);
            }
        });


        lnr_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnr_book.setBackgroundResource(R.drawable.sell_fill_circle);
                lnr_car.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_elec.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_mobile.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_furniture.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_bike.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_pets.setBackgroundResource(R.drawable.sell_search_circle);
                str_category="Books";

                Intent newActivity6 = new Intent(getApplicationContext(), SearchSellActivity.class);
                newActivity6.putExtra("category", str_category);
                startActivity(newActivity6);
            }
        });


        lnr_pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnr_pets.setBackgroundResource(R.drawable.sell_fill_circle);
                lnr_car.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_elec.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_mobile.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_furniture.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_book.setBackgroundResource(R.drawable.sell_search_circle);
                lnr_bike.setBackgroundResource(R.drawable.sell_search_circle);
                str_category="Pets";

                Intent newActivity7 = new Intent(getApplicationContext(), SearchSellActivity.class);
                newActivity7.putExtra("category", str_category);
                startActivity(newActivity7);
            }
        });


            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (alertD1.isShowing()) {
                        alertD1.dismiss();
                    }
                }
            };
            alertD1.setView(promptView);
            alertD1.show();
        }



    private void exitIcon() {

        LayoutInflater layoutInflater = LayoutInflater.from(MGarage_History.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(MGarage_History.this).create();
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
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(MGarage_History.this);
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

        LayoutInflater layoutInflater = LayoutInflater.from(MGarage_History.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final AlertDialog alertD = new AlertDialog.Builder(MGarage_History .this).create();
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
        Intent post = new Intent(MGarage_History.this,Dashboard.class);
        startActivity(post);
        finish();
    }

    private class HistoryGarage extends AsyncTask<String,String,String> {
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
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(ALL_GARAGE, json, str_uid, str_token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "whole data" + jsonStr);
            dialog2.dismiss();

            super.onPostExecute(jsonStr);
            try {
                JSONObject jo = new JSONObject(jsonStr);
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
                            map.put("mobileno", jsonObject.getString("mobileno"));

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
                            garageadapt.add(map);
                            Log.e("tag", "CONTACT_LIST"+garageadapt);
                        }


                        garageadapter = new MgarageAdapter(MGarage_History.this, garageadapt);
                        list_garage.setAdapter(garageadapter);
                    } else
                    {
                        lnr_empty.setVisibility(View.VISIBLE);
                        //Toast.makeText(getApplicationContext(), "Sorry, No relevent data available... ", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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


}


