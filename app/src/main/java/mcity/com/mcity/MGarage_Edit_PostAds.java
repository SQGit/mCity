package mcity.com.mcity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 28-01-2017.
 */
public class MGarage_Edit_PostAds extends Activity implements AdapterView.OnItemSelectedListener {
    String UPDATE_GARAGE = Data_Service.URL_API + "updatesell";
    LinearLayout linear_hide_bike1, linear_hide_bike2;
    String s1,s2,s3,s4,s5,s6,s7,spin_val_fuel,str_pho_enable,str_field1,str_field2,str_field3,str_field4,str_field5,str_field6,str_field7,str_field8;
    LinearLayout back_arrow;
    EditText edt_description, editText_price, editText_yrmodel, editText_colour,  editText_kms, editText_description,editText_price1;
    Spinner spinner_fuel;
    ImageView img_update;
    CheckBox chk_phone_enable;
    List<String> categories;
    Typeface tf;
    ProgressBar progressBar;
    Dialog dialog2;
    String token, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mgarage_edit_postads);


        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        edt_description = (EditText) findViewById(R.id.txt_description);
        editText_price = (EditText) findViewById(R.id.editText_price);
        editText_yrmodel = (EditText) findViewById(R.id.editText_yrmodel);
        editText_colour = (EditText) findViewById(R.id.editText_colour);
        editText_price1=(EditText)findViewById(R.id.editText_price1);
        editText_kms = (EditText) findViewById(R.id.editText_kms);
        editText_description = (EditText) findViewById(R.id.editText_description);
        linear_hide_bike1 = (LinearLayout) findViewById(R.id.linear_hide_bike1);
        linear_hide_bike2 = (LinearLayout) findViewById(R.id.linear_hide_bike2);
        back_arrow = (LinearLayout) findViewById(R.id.back_arrow);
        spinner_fuel = (Spinner) findViewById(R.id.spinner);
        chk_phone_enable=(CheckBox)findViewById(R.id.phone_enable);
        img_update=(ImageView)findViewById(R.id.update);
        editText_price.requestFocus();

        dialog2 = new Dialog(MGarage_Edit_PostAds.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString("token", "");
        id = sharedPreferences.getString("id", "");
        Intent intent = getIntent();
        s1 =  intent.getStringExtra("key1");
        s2 =  intent.getStringExtra("key2");
        s3 =  intent.getStringExtra("key3");
        s4 =  intent.getStringExtra("key4");
        s5 =  intent.getStringExtra("key5");
        s6 = intent.getStringExtra("key6");
        s7 = intent.getStringExtra("key7");

        Log.e("tag","test1"+s1);
        Log.e("tag","test2"+s2);
        Log.e("tag","test3"+s3);
        Log.e("tag","test4"+s4);
        Log.e("tag","test5"+s5);


        if(s3.equals("not specified")&& s4.equals("not specified"))
        {

            linear_hide_bike2.setVisibility(View.VISIBLE);
            linear_hide_bike1.setVisibility(View.GONE);
            edt_description.setText(s1);
            editText_price.setText(s2);
            editText_description.setText(s5);

        }
        else
        {
            linear_hide_bike1.setVisibility(View.VISIBLE);
            linear_hide_bike2.setVisibility(View.GONE);
            edt_description.setText(s1);
            editText_price.setText(s2);
            editText_yrmodel.setText(s3);
            editText_colour.setText(s4);
            editText_description.setText(s5);
            editText_kms.setText(s6);

        }


        spinner_fuel.setOnItemSelectedListener(this);
        categories=new ArrayList<String>();
        categories = new ArrayList<String>();
        categories.add("Select Fuel");
        categories.add("Petrol");
        categories.add("Diesel");
        categories.add("LPG");
        // Creating adapter for spinner

        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_fuel.setAdapter(dataAdapter);
        setSpinner1();


        spinner_fuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spin_val_fuel = spinner_fuel.getItemAtPosition(position).toString();
                TextView spinnerText = (TextView) spinner_fuel.getChildAt(0);
                spinnerText.setTextSize(14);
                spinnerText.setTypeface(tf);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Myselladapter.class);
                startActivity(intent);
                finish();
            }
        });


       /* img_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chk_phone_enable.isChecked()) {
                    str_pho_enable = "enabled";
                } else {
                    str_pho_enable = "Hidden Contact";
                }


                str_field1 = edt_description.getText().toString();
                if (str_field1.equals("Motorcycles") || str_field1.equals("Scootors") || str_field1.equals("Cars") || str_field1.equals("Commercial Vehicles")) {
                Log.e("tag","@1");
                    str_field2 = editText_price.getText().toString();
                    str_field3 = editText_yrmodel.getText().toString();
                    str_field4 = editText_colour.getText().toString();
                    Log.e("tag", "check_color" + str_field4);
                    str_field5 = spinner_fuel.getSelectedItem().toString();
                    str_field6 = editText_kms.getText().toString();
                    str_field7 = editText_description.getText().toString();

                    if (Util.Operations.isOnline(MGarage_Edit_PostAds.this)) {
                        if (!str_field1.isEmpty() && !str_field2.isEmpty() && !str_field3.isEmpty() && !str_field4.isEmpty() && !str_field5.isEmpty() && !str_field6.isEmpty() && !str_field7.isEmpty()) {
                            new PostGarageAsync(str_field1, str_field2, str_field3, str_field4, str_field5, str_field6,
                                    str_field7, str_field8, str_pho_enable).execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Fields..", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Log.e("tag","@2");
                    str_field1 = edt_description.getText().toString();
                    str_field2 = editText_price1.getText().toString();
                    str_field7 = editText_description.getText().toString();

                    if (Util.Operations.isOnline(MGarage_Edit_PostAds.this)) {

                            new PostGarageAsyncOne(str_field1, str_field2, str_field7, str_field8, str_pho_enable).execute();

                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });*/


    }


    private class PostGarageAsync extends AsyncTask<String, String, String> {

        String str_field1, str_field2, str_field3, str_field4, str_field5, str_field6, str_field7, str_field8,str_pho_enable;


        public PostGarageAsync(String str_field1, String str_field2, String str_field3, String str_field4, String str_field5, String str_field6, String str_field7, String str_field8,String str_pho_enable) {

            this.str_field1 = str_field1;
            this.str_field2 = str_field2;
            this.str_field3 = str_field3;
            this.str_field4 = str_field4;
            this.str_field5 = str_field5;
            this.str_field6 = str_field6;
            this.str_field7 = str_field7;
            this.str_field8 = str_field8;
            this.str_pho_enable=str_pho_enable;


        }


        protected void onPreExecute() {

            dialog2.show();
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            String json = "",jsonStr;
            try {

                //location,landmark,address,roomtype,monthlyrent,gender,description
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id2", s7);
                jsonObject.accumulate("field1", str_field6);
                jsonObject.accumulate("field2", str_field3);
                jsonObject.accumulate("field3", str_field4);
                jsonObject.accumulate("field4", str_field5);
                jsonObject.accumulate("phone", str_pho_enable);
                jsonObject.accumulate("description", str_field7);
                jsonObject.accumulate("price", str_field2);
                jsonObject.accumulate("adtitle",str_field1 );
                jsonObject.accumulate("category", "");

                Log.e("tag","11111111"+s7+str_field1+str_field2+str_field7+str_pho_enable+str_field5+str_field4+str_field3+str_field6);


                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(UPDATE_GARAGE, json, id, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(String responseString) {

            super.onPostExecute(responseString);
            Log.e("tag","agvavahsh"+responseString);
            dialog2.dismiss();


            try {
                JSONObject jo = new JSONObject(responseString);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.e("tag","00000");

                if (status.equals("true")) {

                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MGarage_History.class);
                    startActivity(intent);
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





    private class PostGarageAsyncOne extends AsyncTask<String,String,String>{

        String str_field1,str_field2,str_field7,str_field8,str_pho_enable;
        public PostGarageAsyncOne(String str_field1, String str_field2,String str_field7, String str_field8,String str_pho_enable) {
            this.str_field1=str_field1;
            this.str_field2=str_field2;
            this.str_field7=str_field7;
            this.str_field8=str_field8;
            this.str_pho_enable=str_pho_enable;
        }

        protected void onPreExecute() {

            dialog2.show();
            // submit.setVisibility(View.GONE);
            Log.e("tag","11111");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr;
            try {

                //location,landmark,address,roomtype,monthlyrent,gender,description
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id2", s7);
                jsonObject.accumulate("field1", "not specified");
                jsonObject.accumulate("field2", "not specified");
                jsonObject.accumulate("field3", "not specified");
                jsonObject.accumulate("field4", "not specified");
                jsonObject.accumulate("phone", str_pho_enable);
                jsonObject.accumulate("description", str_field7);
                jsonObject.accumulate("price", str_field2);
                jsonObject.accumulate("adtitle",str_field1);
                
                jsonObject.accumulate("category", "kk");


                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(UPDATE_GARAGE, json, id, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;

        }


        @Override
        protected void onPostExecute(String responseString) {

            super.onPostExecute(responseString);
            Log.e("tag","agvavahsh"+responseString);
            dialog2.dismiss();
            //submit.setVisibility(View.VISIBLE);

            try {
                JSONObject jo = new JSONObject(responseString);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.e("tag","00000");

                if (status.equals("true")) {

                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MGarage_History.class);
                    startActivity(intent);
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


    private void setSpinner1() {


        final CustomAdapter arrayAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, categories) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                tv.setTypeface(tf);
                tv.setTextSize(14);
                tv.setText("ndfndnf");
                return super.getView(position, convertView, parent);
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                tv.setTypeface(tf);
                tv.setTextSize(12);

                if (position == 0) {
                    tv.setTextColor(Color.RED);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinner_fuel.setAdapter(arrayAdapter);
    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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