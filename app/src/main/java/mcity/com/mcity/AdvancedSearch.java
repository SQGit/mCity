package mcity.com.mcity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 30-09-2016.
 */
public class AdvancedSearch extends Activity implements AdapterView.OnItemSelectedListener {
    String LOGOUT = Data_Service.URL_API + "logout";

    Spinner spn_source, spn_dest;
    TextView txt_date, txt_time, txt_godate, txt_returndate;
    EditText  edt_weight,edt_minval,edt_maxval,edt_valfrom,edt_valto;
    ImageView img_search_ride,img_settings_icon;
    CheckBox chk_checkBox, chk_midway_drop, chk_round_trip, chk_luggage;
    String str_token, str_uid, str_midway,str_from, str_to, str_date, str_price, str_time, str_merge,str_minval,str_maxval,str_getfrom,str_getto;
    Button btn_add, btn_sub;
    private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 1111;
    static final int DATE_PICKER_ID2 = 2222;
    static final int DATE_PICKER_ID3 = 3333;

    LinearLayout lnr_back_arrow, lnr_luggage_layout, lnr_triplayout, lnr_date_lin, lnr_time_lin, lnr_date_layout;
    int num = 0;
    List<String> fromadd;
    List<String> toadd;
    Typeface tf;
    ArrayList<HashMap<String, String>> searchridelist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.advance_search);

        searchridelist = new ArrayList<>();
        lnr_back_arrow = (LinearLayout) findViewById(R.id.back_arrow);
        spn_source = (Spinner) findViewById(R.id.edt_from);
        spn_dest = (Spinner) findViewById(R.id.edt_to);
        edt_weight = (EditText) findViewById(R.id.weight);
        img_search_ride = (ImageView) findViewById(R.id.submit);
        txt_date = (TextView) findViewById(R.id.select_date);
        txt_time = (TextView) findViewById(R.id.timer);
        txt_godate = (TextView) findViewById(R.id.go_date);
        txt_returndate = (TextView) findViewById(R.id.return_date);
        edt_minval=(EditText)findViewById(R.id.minval);
        edt_maxval=(EditText)findViewById(R.id.maxval);
        btn_add = (Button) findViewById(R.id.add);
        btn_sub = (Button) findViewById(R.id.sub);
        img_settings_icon = (ImageView) findViewById(R.id.settings_icon);
        lnr_luggage_layout = (LinearLayout) findViewById(R.id.luggage_layout);
        lnr_triplayout = (LinearLayout) findViewById(R.id.triplayout);
        lnr_date_lin = (LinearLayout) findViewById(R.id.date_lin);
        lnr_time_lin = (LinearLayout) findViewById(R.id.time_lin);
        lnr_date_layout = (LinearLayout) findViewById(R.id.date_layout);
        edt_valfrom = (EditText) findViewById(R.id.maxval);
        edt_valto=(EditText)findViewById(R.id.minval);
        chk_checkBox = (CheckBox) findViewById(R.id.other_det);
        chk_luggage = (CheckBox) findViewById(R.id.luggage);
        chk_midway_drop = (CheckBox) findViewById(R.id.midway_drop);
        chk_round_trip = (CheckBox) findViewById(R.id.round_trip);


        Intent intent = getIntent();
        str_getfrom = intent.getStringExtra("from_location");
        str_getto=intent.getStringExtra("to_location");

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        lnr_triplayout.setVisibility(View.GONE);
        lnr_date_layout.setVisibility(View.GONE);
        lnr_luggage_layout.setVisibility(View.GONE);


        spn_source.setOnItemSelectedListener(this);
        spn_dest.setOnItemSelectedListener(this);



        // Spinner Drop down elements
        fromadd = new ArrayList<String>();
        fromadd.add("Select Location");
        fromadd.add("Aqualily/BMW");
        fromadd.add("Canopy/Sylvan County");
        fromadd.add("Iris Court");
        fromadd.add("Nova");
        fromadd.add("MRV");
        fromadd.add("Infosys Main Gate");
        fromadd.add("Zero Point");


        //List<String> toadd = new ArrayList<String>();
        toadd = new ArrayList<String>();
        toadd.add("Select Location");
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
        spn_source.setAdapter(dataAdapter);


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, toadd);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spn_dest.setAdapter(dataAdapter1);
        setSpinner1();
        setSpinner2();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        txt_date.setText(new StringBuilder()
                .append(day).append("-").append(month + 1).append("-")
                .append(year).append(""));

        lnr_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RideSearch.class);
                startActivity(i);
                finish();
            }
        });


        img_settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(AdvancedSearch.this, img_settings_icon);
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


        //set time
        lnr_time_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);


                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AdvancedSearch.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override

                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        int hourInt = selectedHour;
                        String hourConverted = "" + hourInt;
                        if (hourInt < 10) {
                            hourConverted = "0" + hourConverted;
                        }


                        int minuteInt = selectedMinute;
                        String minuteConverted = "" + minuteInt;
                        if (minuteInt < 10) {
                            minuteConverted = "0" + minuteConverted;
                        }

                        txt_time.setText(hourConverted + ":" + minuteConverted);

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }

        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (num == 0) {
                    btn_sub.setEnabled(false);

                } else {
                    btn_sub.setEnabled(true);
                }
                btn_sub.setClickable(true);
                num++;
                edt_weight.setText(Integer.toString(num));

            }
        });


        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gg = edt_weight.getText().toString();

                if (gg.equals("0")) {
                    btn_sub.setClickable(false);
                } else {
                    btn_sub.setClickable(true);
                    num--;
                    edt_weight.setText(Integer.toString(num));
                }


            }
        });
        chk_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lnr_triplayout.setVisibility(View.VISIBLE);

                } else {
                    lnr_triplayout.setVisibility(View.GONE);

                }
            }
        });

        chk_luggage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lnr_luggage_layout.setVisibility(View.VISIBLE);
                } else {
                    lnr_luggage_layout.setVisibility(View.GONE);
                }
            }
        });


        chk_round_trip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lnr_date_layout.setVisibility(View.VISIBLE);

                } else {
                    lnr_date_layout.setVisibility(View.GONE);

                }
            }
        });








        chk_midway_drop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    str_midway = "Midway Available";
                } else {
                    str_midway = "Midway Unavailable";

                }
            }
        });


        lnr_date_lin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);

            }

        });

        txt_godate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID2);
            }

        });


        txt_returndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID3);
            }

        });


        img_search_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                str_from = spn_source.getSelectedItem().toString();
                str_to = spn_dest.getSelectedItem().toString();
                str_date = txt_date.getText().toString();
                str_time = txt_time.getText().toString();
                str_merge = str_date + " T " + str_time;
                str_price = edt_weight.getText().toString();
                str_minval=edt_minval.getText().toString();
                str_maxval=edt_maxval.getText().toString();

                if(chk_midway_drop.isChecked())
                {
                    str_midway="Midwaydrop Available";
                }
                else
                {
                    str_midway="Midwaydrop Unavailable";
                }


                Intent intent = new Intent(getApplicationContext(), SearchResult.class);
                intent.putExtra("source", str_from);
                intent.putExtra("dest", str_to);
                intent.putExtra("date", str_merge);
                intent.putExtra("midway", str_midway);
                intent.putExtra("minval",str_minval);
                intent.putExtra("maxval",str_maxval);
                startActivity(intent);
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

        LayoutInflater layoutInflater = LayoutInflater.from(AdvancedSearch.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(AdvancedSearch.this).create();
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
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(AdvancedSearch.this);
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

        LayoutInflater layoutInflater = LayoutInflater.from(AdvancedSearch.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final AlertDialog alertD = new AlertDialog.Builder(AdvancedSearch .this).create();
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
        spn_dest.setAdapter(arrayAdapter1);

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
        spn_source.setAdapter(arrayAdapter);
    }


    //date convertion
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                return new DatePickerDialog(this, pickerListener, year, month, day);

            case DATE_PICKER_ID2:
                return new DatePickerDialog(this, pickerListener1, year, month, day);


            case DATE_PICKER_ID3:
                return new DatePickerDialog(this, pickerListener3, year, month, day);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            int monthInt = month + 1;
            String monthConverted = "" + monthInt;
            if (monthInt < 10) {
                monthConverted = "0" + monthConverted;
            }


            int dayInt = day;
            String dayConverted = "" + dayInt;
            if (dayInt < 10) {
                dayConverted = "0" + dayConverted;
            }



            // Show selected date
            txt_date.setText(new StringBuilder().append(dayConverted)
                    .append("-").append(monthConverted).append("-").append(year)
                    .append(""));

        }
    };


    private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            int monthInt = month + 1;
            String monthConverted = "" + monthInt;
            if (monthInt < 10) {
                monthConverted = "0" + monthConverted;
            }


            int dayInt = day;
            String dayConverted = "" + dayInt;
            if (dayInt < 10) {
                dayConverted = "0" + dayConverted;
            }

            // Show selected date
            txt_godate.setText(new StringBuilder().append(dayConverted)
                    .append("-").append(monthConverted).append("-").append(year)
                    .append(""));

        }
    };

    private DatePickerDialog.OnDateSetListener pickerListener3 = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;


            int monthInt = month + 1;
            String monthConverted = "" + monthInt;
            if (monthInt < 10) {
                monthConverted = "0" + monthConverted;
            }


            int dayInt = day;
            String dayConverted = "" + dayInt;
            if (dayInt < 10) {
                dayConverted = "0" + dayConverted;
            }

            txt_returndate.setText(new StringBuilder().append(dayConverted)
                    .append("-").append(monthConverted).append("-").append(year)
                    .append(""));
        }
    };

    @Override
    public void onBackPressed() {

        Intent i = new Intent(AdvancedSearch.this,RideSearch.class);
        startActivity(i);
        finish();
        // super.onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


}
