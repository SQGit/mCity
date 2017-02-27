package mcity.com.mcity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 02-02-2017.
 */

public class OfferRideNew extends Activity implements AdapterView.OnItemSelectedListener {
    String RIDE_VERIFY = Data_Service.URL_API + "mrideverify";
    String POST_RIDE = Data_Service.URL_API + "postforridenew";
    String LICENCE = Data_Service.URL_API + "licenceupload";
    Spinner search_from, search_go;
    List<String> fromadd;
    List<String> toadd;
    Typeface tf;
    LinearLayout lnr_backarrow,lnr_date_lin,lnr_date_layout,lnr_luggage_layout,lnr_time_lin;
    String token,id,mride,str_from, str_to, str_date, str_price, str_time,str_merge,str_midway,str_pho_enable,str_person,str_godate,str_return;
    public ImageView img_license_image;
    int num = 0;
    EditText edt_weight, edt_ticket;
    Dialog dialog;
    ProgressBar progressBar;
    Dialog dialog2;
    TextView select_date,txt_go_date,txt_return_date,timer;
    CheckBox chk_round_trip,chk_luggage,chk_midway_drop,chk_phone_enable;
    Button btn_add, btn_sub;
    ImageView ride_submit,img_settings_icon;
    //camera
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    Button camera;
    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    private ViewGroup mSelectedImagesContainer;


    //date picker
    static final int DATE_PICKER_ID = 1111;
    static final int DATE_PICKER_ID2 = 2222;
    static final int DATE_PICKER_ID3 = 3333;
    private int year,year1,month1,day1;
    private int month;
    private int day;



    private int mHour,mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_ride_new);

        select_date=(TextView)findViewById(R.id.select_date);
        txt_go_date=(TextView)findViewById(R.id.go_date);
        timer=(TextView)findViewById(R.id.timer) ;
        txt_return_date=(TextView)findViewById(R.id.return_date);
        search_from = (Spinner) findViewById(R.id.search_from);
        search_go = (Spinner) findViewById(R.id.search_go);
        lnr_backarrow = (LinearLayout) findViewById(R.id.back_arrow);
        lnr_date_lin = (LinearLayout) findViewById(R.id.date_lin);
        lnr_time_lin=(LinearLayout) findViewById(R.id.time_lin) ;
        lnr_luggage_layout = (LinearLayout) findViewById(R.id.luggage_layout);
        chk_round_trip = (CheckBox) findViewById(R.id.round_trip);
        chk_luggage = (CheckBox) findViewById(R.id.luggage);
        chk_midway_drop=(CheckBox)findViewById(R.id.midway_drop);
        chk_phone_enable=(CheckBox)findViewById(R.id.phone_enable);
        edt_weight = (EditText) findViewById(R.id.weight);
        edt_ticket = (EditText) findViewById(R.id.ticket);
        lnr_date_layout = (LinearLayout) findViewById(R.id.date_layout);
        btn_add = (Button) findViewById(R.id.add);
        btn_sub = (Button) findViewById(R.id.sub);
        ride_submit = (ImageView) findViewById(R.id.ride_submit);
        img_settings_icon = (ImageView) findViewById(R.id.settings_icon);

        dialog2 = new Dialog(OfferRideNew.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);

        lnr_date_layout.setVisibility(View.GONE);
        lnr_luggage_layout.setVisibility(View.GONE);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);
        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor=sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        id = sharedPreferences.getString("id", "");
        mride=sharedPreferences.getString("mride","");
        Log.e("tag","1"+token);
        Log.e("tag","2"+id);
        Log.e("tag","3"+mride);


        if(mride.equals("false"))
        {
            new mrideVerify().execute();
        }
        else
        {
           Log.e("tag","jdhf");
        }


        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
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

        lnr_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RideSearch.class);
                startActivity(i);
                finish();
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

        //*******************************DATE PICKER****************************
        lnr_date_lin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_PICKER_ID);

            }

        });

        txt_go_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID2);
            }

        });


        txt_return_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID3);
            }

        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (num >= 0) {
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
                    btn_sub.setClickable(true);
                    num--;
                    edt_weight.setText(Integer.toString(num));

            }
        });

        img_settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(OfferRideNew.this, img_settings_icon);
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


        lnr_time_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(OfferRideNew.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // et_time.setText( selectedHour + ":" + selectedMinute);
                        updateTime(selectedHour, selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }


              /* Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);


                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(OfferRideNew.this, new TimePickerDialog.OnTimeSetListener() {
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
                        timer.setText(hourConverted + ":" + minuteConverted);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();*/



        });

        //**********************************main onclick listener**************************
        ride_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_from = search_from.getSelectedItem().toString();
                Log.e("tag","12@"+str_from+str_to);
                str_to = search_go.getSelectedItem().toString();
                str_date = select_date.getText().toString();
                str_time = timer.getText().toString();
                str_merge = str_date + " T " + str_time;
                str_price = edt_ticket.getText().toString();
                str_person=edt_weight.getText().toString();
                Log.e("tag","10@"+str_person);


                if(chk_round_trip.isChecked())
                {
                    str_godate=txt_go_date.getText().toString();
                    str_return=txt_return_date.getText().toString();
                }
                else
                {
                    str_godate="empty";
                    str_return="empty";
                }

                if (chk_midway_drop.isChecked()) {
                    str_midway = "Midway Drop Available";
                } else {
                    str_midway = "Midway Drop Unavailable";
                }


                if (chk_phone_enable.isChecked()) {
                    str_pho_enable = "enabled";
                } else {
                    str_pho_enable = "Hidden Contact";
                }


               if (Util.Operations.isOnline(OfferRideNew.this)) {
                   if ( !str_time.isEmpty() && !str_price.isEmpty()&& !str_person.isEmpty()) {
                        if(!str_date.contains("Date")&& !str_time.contains("Time")&&!str_date.equals(null)&&!str_time.equals(null)){
                            if(!str_from.equals(null)&&!str_to.equals(null)&&!str_from.contains("Start Location")&&!str_to.contains("Destination Location")) {
                                new PostRideAsync(str_from, str_to, str_merge, str_price, str_godate, str_return, str_midway, str_pho_enable,str_person).execute();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Please Select Source & Destination", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Please Select Date & TIme", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Invalid Fields..", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                }
            }
        });



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

        lnr_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RideSearch.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        timer.setText(aTime);
    }


    private void aboutUs() {

        LayoutInflater layoutInflater = LayoutInflater.from(OfferRideNew.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(OfferRideNew.this).create();
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


    private void exitIcon() {
        LayoutInflater layoutInflater = LayoutInflater.from(OfferRideNew.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(OfferRideNew.this).create();
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
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(OfferRideNew.this);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("check","");
                editor.clear();
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

        if (Util.Operations.isOnline(OfferRideNew.this)) {
                new Logout().execute();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
            }

    }


    private void applyFontToMenuItem(MenuItem mi)
    {
        Typeface font = Typeface.createFromAsset(getAssets(), "mont.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }
    //@@@@@@@@@@@@ Date Picker@@@@@@@@@@@@@@@@@@
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, pickerListener1, year, month, day );
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 100000);
                return datePickerDialog;


            case DATE_PICKER_ID2:
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(this, pickerListener2, year, month, day );
                datePickerDialog2.getDatePicker().setMinDate(System.currentTimeMillis() + 100000);
                return datePickerDialog2;

           case DATE_PICKER_ID3:
               DatePickerDialog datePickerDialog3 = new DatePickerDialog(this, pickerListener3, year, month, day );
               datePickerDialog3.getDatePicker().setMinDate(System.currentTimeMillis() + 100000);

               return datePickerDialog3;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            Log.e("tag","5*");

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

            Log.e("tag","1*");
            select_date.setText(new StringBuilder().append(year)
                    .append("/").append(monthConverted).append("/").append(dayConverted)
                    .append(""));
        }
    };


    private DatePickerDialog.OnDateSetListener pickerListener2 = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year1 = selectedYear;
            month1 = selectedMonth;
            day1 = selectedDay;

            int monthInt = month1 + 1;
            String monthConverted = "" + monthInt;
            if (monthInt < 10) {
                monthConverted = "0" + monthConverted;
            }


            int dayInt = day1;
            String dayConverted = "" + dayInt;
            if (dayInt < 10) {
                dayConverted = "0" + dayConverted;
            }

            // Show selected date
            txt_go_date.setText(new StringBuilder().append(year1+1)
                    .append("/").append(monthConverted).append("/").append(dayConverted)
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

            // Show selected date
            txt_return_date.setText(new StringBuilder().append(year)
                    .append("/").append(monthConverted).append("/").append(dayConverted)
                    .append(""));
        }
    };

    private void permissionDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(OfferRideNew.this);
        View promptView = layoutInflater.inflate(R.layout.permission_dialog, null);
        final AlertDialog alertD1 = new AlertDialog.Builder(OfferRideNew.this).create();
        alertD1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertD1.setCancelable(true);
        Window window = alertD1.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView tv_head = (TextView) promptView.findViewById(R.id.head1);
        final TextView head2 = (TextView) promptView.findViewById(R.id.head2);
        final Button submit = (Button) promptView.findViewById(R.id.yes);
        final Button cancel = (Button) promptView.findViewById(R.id.no);

        Typeface tff = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        tv_head.setTypeface(tff);
        head2.setTypeface(tff);
        submit.setTypeface(tf);
        cancel.setTypeface(tf);

        tv_head.setText("WARNING");
        head2.setText("Mcity App(or)SQIndia.net does not take any responsibility in verification or validation on their ID of the person. It is left to individuals to use their descretion to offer (or) take a Ride.\n\n Once you Upload License please wait for confirmation message in your mobile. After that your Ride added to Mcity App.");
          submit.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  alertD1.dismiss();
                  takeLicenseproof();
              }
          });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertD1.dismiss();
                    Intent back_ride=new Intent(getApplicationContext(),RideSearch.class);
                    startActivity(back_ride);
                    finish();
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

    private void takeLicenseproof() {
        dialog = new Dialog(OfferRideNew.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.driving_license);
        final TextView head1 = (TextView) dialog.findViewById(R.id.head1);
        final TextView takephoto = (TextView) dialog.findViewById(R.id.takephoto);
        img_license_image = (ImageView) dialog.findViewById(R.id.license_image);
        final ImageView submit = (ImageView) dialog.findViewById(R.id.submit);
        final ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel);
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        head1.setTypeface(tf);
        takephoto.setTypeface(tf);

        head1.setText("Please Upload your License\n Proof by Either taking the Photo or \n Selecting from Gallery");
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = Utility.checkPermission(OfferRideNew.this);
                if (result) {
                    Config config = new Config();
                    config.setSelectionMin(1);
                    config.setSelectionLimit(1);
                    ImagePickerActivity.setConfig(config);
                    Intent intent = new Intent(OfferRideNew.this, ImagePickerActivity.class);
                    startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_cancel=new Intent(getApplicationContext(),RideSearch.class);
                startActivity(intent_cancel);
                finish();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.Operations.isOnline(OfferRideNew.this))
                {
                    new ImageUpload().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                }

            }
        });
        dialog.show();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String item = adapterView.getItemAtPosition(position).toString();
        Log.e("tag","%%%"+item);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK ) {

            image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);


            Log.e("tag","12345"+image_uris);

            if (image_uris != null) {


                showMedia();
            }
        }
    }


    private void showMedia() {

        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        for (Uri uri : image_uris) {
            img_license_image = (ImageView) dialog.findViewById(R.id.license_image);
            Glide.with(this)
                    .load(uri.toString())
                    .fitCenter()
                    .into(img_license_image);

        }
    }

    private class mrideVerify extends AsyncTask<String,String,String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";

            try {

                //location,landmark,address,roomtype,monthlyrent,gender,description
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(RIDE_VERIFY, json, id, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(String responseString) {

            super.onPostExecute(responseString);

            try {
                JSONObject jo = new JSONObject(responseString);
                String status = jo.getString("status");
                Log.e("tag","!0"+status);
                JSONObject msg = jo.getJSONObject("message");
                String m=msg.getString("mride");
                Log.e("tag","!00"+m);



                if (m.equals("true")) {
                    Log.e("tag","!1");
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("mride","true");
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), OfferRide.class);
                    startActivity(intent);
                    finish();


                }
                else {
                    Log.e("tag","!2");
                    permissionDialog();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class ImageUpload extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            dialog2.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String responseString = null;
            ContentBody cbFile = null;
            String jsonStr;
            //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost(LICENCE);
                postMethod.addHeader("x-access-token", token);
                postMethod.addHeader("id", id);


                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                for( int i=0;i<image_uris.size();i++){
                    Log.e("tag","111"+image_uris);
                    Log.e("tag","222"+image_uris.size());
                    File sourceFile = new File(String.valueOf(image_uris.get(i)));
                    cbFile = new FileBody( sourceFile, "image/jpeg" );
                    entity.addPart("file", cbFile);
                }

                postMethod.setEntity(entity);
                HttpResponse response = client.execute(postMethod);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();


                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                    JSONObject result1 = new JSONObject(responseString);
                    String status = result1.getString("status");
                    if (status.equals("true")) {
                    }
                } else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }

            } catch (Exception e) {
                responseString = e.toString();
            }
            return responseString;
        }


        @Override
        protected void onPostExecute(String jsonStr) {
            dialog2.dismiss();
            super.onPostExecute(jsonStr);

            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                //String licence_key = jo.getString("licence");

                if (status.equals("true")) {

                    //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = s_pref.edit();
                    edit.putString("mride","true");
                    edit.commit();
                    rplytoUser();

                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void rplytoUser() {
        LayoutInflater layoutInflater = LayoutInflater.from(OfferRideNew.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog1, null);
        final AlertDialog alertD1 = new AlertDialog.Builder(OfferRideNew.this).create();
        alertD1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertD1.setCancelable(true);
        Window window = alertD1.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView head1 = (TextView) promptView.findViewById(R.id.head1);
        final TextView head2 = (TextView) promptView.findViewById(R.id.head2);
        final Button submit = (Button) promptView.findViewById(R.id.yes);


        Typeface tff = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        head1.setTypeface(tff);
        head2.setTypeface(tff);



        head1.setText("INFO");
        head2.setText("Your license proof has been added successfully");

        /*final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        category_details = sharedPreferences.getString("category_details", "");
        Log.e("Tag12", "aaa" + category_details);
*/
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD1.dismiss();
                Intent iii=new Intent(getApplicationContext(),RideSearch.class);
                startActivity(iii);
                finish();

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

    private class PostRideAsync extends AsyncTask<String,String,String> {

        String str_from, str_to, str_merge, str_price, str_godate, str_return, str_midway, str_pho_enable, str_person;

        public PostRideAsync(String str_from, String str_to, String str_merge, String str_price, String str_godate, String str_return, String str_midway, String str_pho_enable, String str_person) {

            this.str_from = str_from;
            this.str_to = str_to;
            this.str_merge = str_merge;
            this.str_price = str_price;
            this.str_godate = str_godate;
            this.str_return = str_return;
            this.str_midway = str_midway;
            this.str_pho_enable = str_pho_enable;
            this.str_person = str_person;
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
                jsonObject.accumulate("from", str_from);
                jsonObject.accumulate("to", str_to);
                jsonObject.accumulate("date", str_merge);
                jsonObject.accumulate("price", str_price);
                jsonObject.accumulate("midwaydrop", str_midway);
                jsonObject.accumulate("phone", str_pho_enable);
                jsonObject.accumulate("godate", str_godate);
                jsonObject.accumulate("returndate", str_return);
                jsonObject.accumulate("noofpersons", str_person);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(POST_RIDE, json, id, token);
            } catch (Exception e) {

            }
            return jsonStr;
        }
            @Override
            protected void onPostExecute (String jsonstr){
                dialog2.dismiss();
                super.onPostExecute(jsonstr);


                try {
                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("status");
                    String msg = jo.getString("message");

                    if (status.equals("true")) {


                        exitIcon1();

                       /* Toast.makeText(getApplicationContext(), "Your post has been added successfully", Toast.LENGTH_SHORT).show();
                        Intent fd=new Intent(getApplicationContext(),RideSearch.class);
                        startActivity(fd);
                        finish();*/
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occured please check", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    private void exitIcon1() {

        LayoutInflater layoutInflater = LayoutInflater.from(OfferRideNew.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog1, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(OfferRideNew.this).create();
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
        head1.setText("SUCCESS");
        head2.setText("Your Post has been added Succesfully");


        yes.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                alertD.dismiss();
                Intent ff=new Intent(getApplicationContext(),RideSearch.class);
                startActivity(ff);
                finish();
            }
        });

        alertD.setView(promptView);
        alertD.show();
    }



    @Override
    public void onBackPressed() {

        Intent i = new Intent(OfferRideNew.this, RideSearch.class);
        startActivity(i);
        finish();

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
                HttpPost postMethod = new HttpPost(LICENCE);
                postMethod.addHeader("x-access-token", token);
                postMethod.addHeader("id", id);
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
                    Intent exit=new Intent(getApplicationContext(),Login.class);
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
