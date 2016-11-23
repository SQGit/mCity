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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Created by Admin on 27-09-2016.
 */
public class OfferRide extends Activity {
    String URL = Data_Service.URL_API + "postforride";
    String LICENCE = Data_Service.URL_API + "licenceupload";
    static final int TIME_DIALOG_ID = 4444;

    public static int count = 0;
    private static final int REQUEST_CODE = 1;
    static final int DATE_PICKER_ID = 1111;
    static final int DATE_PICKER_ID2 = 2222;
    static final int DATE_PICKER_ID3 = 3333;
    private TextView txt_fromdate, txt_go_date, txt_return_date,txt_date, txt_timer;
    ImageView img_submit;

    int num = 0;
    int TAKE_PHOTO_CODE = 0;

    private int year;
    private int month;
    private int day;
    Intent intent;

    private int hour;
    private int minute;

    public ImageView img_license_image,img_settings_icon;
    EditText edt_weight, edt_ticket;
    LinearLayout lnr_triplayout,  lnr_date_layout, lnr_luggage_layout, lnr_backarrow, lnr_time_lin, lnr_date_lin, lnr_submit_linr, lnr_settings_linr;
    String str_midway, str_pho_enable, str_token, str_uid, str_merge, str_lic_activation, file_check, str_imagepath, str_round_godate, str_round_returndate, str_round_luggage,str_from, str_to, str_date, str_price, str_time;
    Button btn_add, btn_sub;
    CheckBox chk_checkBox, chk_midway_drop, chk_phone_enable, chk_round_trip, chk_luggage;
    ProgressBar progressBar;
    Spinner spn_from, spn_to;
    List<String> fromadd;
    List<String> toadd;
    Typeface tf;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_ride);

        img_submit = (ImageView) findViewById(R.id.submit);
        txt_fromdate = (TextView) findViewById(R.id.select_date);
        chk_checkBox = (CheckBox) findViewById(R.id.other_det);
        chk_luggage = (CheckBox) findViewById(R.id.luggage);
        chk_midway_drop = (CheckBox) findViewById(R.id.midway_drop);
        chk_phone_enable = (CheckBox) findViewById(R.id.phone_enable);
        chk_round_trip = (CheckBox) findViewById(R.id.round_trip);
        lnr_luggage_layout = (LinearLayout) findViewById(R.id.luggage_layout);
        lnr_triplayout = (LinearLayout) findViewById(R.id.triplayout);
        lnr_submit_linr = (LinearLayout) findViewById(R.id.submit_linr);
        lnr_settings_linr = (LinearLayout) findViewById(R.id.settings_linr);
        lnr_date_lin = (LinearLayout) findViewById(R.id.date_lin);
        edt_weight = (EditText) findViewById(R.id.weight);
        lnr_time_lin = (LinearLayout) findViewById(R.id.time_lin);
        edt_ticket = (EditText) findViewById(R.id.ticket);
        lnr_backarrow = (LinearLayout) findViewById(R.id.back_arrow);
        lnr_date_layout = (LinearLayout) findViewById(R.id.date_layout);
        spn_from = (Spinner) findViewById(R.id.edt_from);
        spn_to = (Spinner) findViewById(R.id.edt_to);
        txt_go_date = (TextView) findViewById(R.id.go_date);
        txt_timer = (TextView) findViewById(R.id.timer);
        txt_date = (TextView) findViewById(R.id.select_date);
        txt_return_date = (TextView) findViewById(R.id.return_date);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        img_settings_icon = (ImageView) findViewById(R.id.settings_icon);


        btn_add = (Button) findViewById(R.id.add);
        btn_sub = (Button) findViewById(R.id.sub);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = sharedPreferences.edit();


        lnr_triplayout.setVisibility(View.GONE);
        btn_sub.setEnabled(false);
        lnr_date_layout.setVisibility(View.GONE);
        lnr_luggage_layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        lnr_submit_linr.setVisibility(View.VISIBLE);
        lnr_settings_linr.setVisibility(View.GONE);
        // Get current date by calender

        final Calendar ca = Calendar.getInstance();
        // Current Hour
        hour = ca.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = ca.get(Calendar.MINUTE);

        updateTime(hour, minute);

        fromadd = new ArrayList<String>();
        fromadd.add("Select Location");
        fromadd.add("Aqualily/BMW");
        fromadd.add("Canopy/Sylvan County");
        fromadd.add("Iris Court");
        fromadd.add("Nova");
        fromadd.add("MRV");
        fromadd.add("Infosys Main Gate");
        fromadd.add("Zero Point");


        toadd = new ArrayList<String>();
        toadd.add("Select Location");
        toadd.add("Tambaram");
        toadd.add("Chengalpattu");
        toadd.add("Tnagar");
        toadd.add("Central Station");
        toadd.add("Chennai Airport");
        toadd.add("Paranur Station");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fromadd);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_from.setAdapter(dataAdapter);


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, toadd);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spn_to.setAdapter(dataAdapter1);
        setSpinner1();
        setSpinner2();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");
        int a = sharedPreferences.getInt("licence_activation", 0);  //get from login class

        str_lic_activation = String.valueOf(a);


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);



        txt_fromdate.setText(new StringBuilder()
                .append(year).append("/").append(month + 1).append("/")
                .append(day).append(""));

        txt_go_date.setText(new StringBuilder()
                .append(year).append("/").append(month + 1).append("/")
                .append(day).append(""));



        lnr_time_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }

        });


        img_settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PopupMenu popup = new PopupMenu(OfferRide.this, img_settings_icon);
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

        lnr_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RideSearch.class);
                startActivity(i);
                finish();
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

        chk_phone_enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    str_pho_enable = "enabled";
                } else {
                    str_pho_enable = "disabled";

                }
            }
        });
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


        img_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_from = spn_from.getSelectedItem().toString();
                str_to = spn_to.getSelectedItem().toString();
                str_date = txt_date.getText().toString();
                str_time = txt_timer.getText().toString();
                Log.e("tag","testing...."+str_time);
                str_merge = str_date + " T " + str_time;
                str_price = edt_ticket.getText().toString();

                str_round_godate = txt_go_date.getText().toString();
                str_round_returndate = txt_return_date.getText().toString();
                str_round_luggage = edt_weight.getText().toString();


                if (chk_midway_drop.isChecked()) {
                    str_midway = "Midwaydrop Available";
                } else {
                    str_midway = "Midwaydrop Unavailable";
                }



                if (chk_phone_enable.isChecked()) {
                    str_pho_enable = "enabled";
                } else {
                    str_pho_enable = "Hidden Contact";
                }


                if (Util.Operations.isOnline(OfferRide.this)) {
                    if (!str_from.isEmpty() && !str_to.isEmpty() && !str_date.isEmpty() && !str_time.equals("Time") && !str_price.isEmpty()) {

                        if (!(sharedPreferences.getString("file_generate","").equals(""))) {
                            new PostRideAsync(str_from, str_to, str_merge, str_price, str_midway, str_pho_enable, str_round_godate, str_round_returndate, str_round_luggage).execute();
                        }
                        else
                        {
                            takeLicenseproof();
                        }
                    } else
                    {
                        Toast.makeText(getApplicationContext(), "Invalid Fields..", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                }
            }


        });

    }

    private void exitIcon() {


        LayoutInflater layoutInflater = LayoutInflater.from(OfferRide.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(OfferRide.this).create();
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
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(OfferRide.this);
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

        new Logout().execute();
    }

    private void aboutUs() {

        LayoutInflater layoutInflater = LayoutInflater.from(OfferRide.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final AlertDialog alertD = new AlertDialog.Builder(OfferRide.this).create();
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

    private void takeLicenseproof() {


        dialog = new Dialog(OfferRide.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.driving_license);
        final TextView head1 = (TextView) dialog.findViewById(R.id.head1);
        final TextView takephoto = (TextView) dialog.findViewById(R.id.takephoto);
        img_license_image = (ImageView) dialog.findViewById(R.id.license_image);
        final ImageView submit = (ImageView) dialog.findViewById(R.id.submit);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        head1.setTypeface(tf);
        takephoto.setTypeface(tf);

        head1.setText("Please Upload your License\n Proof by Either taking the Photo or \n Selecting from Gallery");


        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
                File newdir = new File(dir);
                newdir.mkdirs();
                count++;
                str_imagepath = dir + count + ".jpg";
                Log.e("tag", "*********" + str_imagepath);

                File newfile = new File(str_imagepath);
                try {
                    newfile.createNewFile();
                } catch (IOException e) {
                }

                Uri outputFileUri = Uri.fromFile(newfile);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageUpload().execute();


            }
        });
        dialog.show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        img_license_image.setImageURI(Uri.parse(str_imagepath));

        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && requestCode == TAKE_PHOTO_CODE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();

            if (photos != null) {

                selectedPhotos.addAll(photos);
            }

            Uri uri = Uri.fromFile(new File(selectedPhotos.get(0)));

            img_license_image.setImageURI(Uri.parse(str_imagepath));


        }
    }



    //************* change option menu typeface settings page.
    private void applyFontToMenuItem(MenuItem mi)
    {
        Typeface font = Typeface.createFromAsset(getAssets(), "mont.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
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
        spn_to.setAdapter(arrayAdapter1);
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
        spn_from.setAdapter(arrayAdapter);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                return new DatePickerDialog(this, pickerListener, year, month, day);

            case DATE_PICKER_ID2:
                return new DatePickerDialog(this, pickerListener1, year, month, day);


            case DATE_PICKER_ID3:
                return new DatePickerDialog(this, pickerListener3, year, month, day);

            case TIME_DIALOG_ID:

                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);

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

            txt_fromdate.setText(new StringBuilder().append(year)
                    .append("/").append(monthConverted).append("/").append(dayConverted)
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


            txt_go_date.setText(new StringBuilder().append(year)
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


    private class ImageUpload extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
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
                postMethod.addHeader("x-access-token", str_token);
                postMethod.addHeader("id", str_uid);


                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                File sourceFile = new File(str_imagepath);
                cbFile = new FileBody(sourceFile, "image/jpeg");
                entity.addPart("file", cbFile);
                postMethod.setEntity(entity);

                HttpResponse response = client.execute(postMethod);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                Log.e("tag", "res " + response.getStatusLine().toString());
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
            super.onPostExecute(jsonStr);
            progressBar.setVisibility(View.GONE);
            dialog.dismiss();
            Intent driving = new Intent(getApplicationContext(), OfferRide.class);
            startActivity(driving);
            finish();

            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                String licence_key = jo.getString("licence");

                if (status.equals("true")) {


                    edit.putString("file_generate", licence_key);
                    edit.commit();

                    new PostRideAsync(str_from, str_to, str_merge, str_price, str_midway, str_pho_enable, str_round_godate, str_round_returndate, str_round_luggage).execute();

                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    private class PostRideAsync extends AsyncTask<String, String, String> {

        String str_from, str_to, merge, str_price, str_midway, str_pho_enable, round_godate, round_returndate, round_luggage;

        public PostRideAsync(String str_from, String str_to, String merge, String str_price, String str_midway, String str_pho_enable, String round_godate, String round_returndate, String round_luggage) {

            this.str_from = str_from;
            this.str_to = str_to;
            this.merge = merge;
            this.str_price = str_price;
            this.str_midway = str_midway;
            this.str_pho_enable = str_pho_enable;
            this.round_godate = round_godate;
            this.round_returndate = round_returndate;
            this.round_luggage = round_luggage;

        }

        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            img_submit.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            String id = "";
            try {

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("from", str_from);
                jsonObject.accumulate("to", str_to);
                jsonObject.accumulate("date", merge);
                jsonObject.accumulate("price", str_price);
                jsonObject.accumulate("midwaydrop", str_midway);
                jsonObject.accumulate("phone", str_pho_enable);


                jsonObject.accumulate("godate", round_godate);
                jsonObject.accumulate("returndate", round_returndate);
                jsonObject.accumulate("extraluggage", round_luggage);


                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(URL, json, str_uid, str_token);
            } catch (Exception e) {

            }
            return jsonStr;
        }


        @Override
        protected void onPostExecute(String jsonstr) {
            super.onPostExecute(jsonstr);
            progressBar.setVisibility(View.GONE);
            img_submit.setVisibility(View.VISIBLE);

            try {
                JSONObject jo = new JSONObject(jsonstr);
                String status = jo.getString("status");
                String msg = jo.getString("message");

                if (status.equals("true")) {

                    Toast.makeText(getApplicationContext(), "Your post has been added successfully", Toast.LENGTH_SHORT).show();
                    spn_from.clearFocus();
                    spn_to.clearFocus();
                    txt_date.setText("");
                    //txt_timer.setText("");
                    edt_ticket.setText("");
                    chk_midway_drop.setChecked(false);
                }


                else{
                    Toast.makeText(getApplicationContext(), "Some error occured please check", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(OfferRide.this, RideSearch.class);
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



/*
    @Override
    protected Dialog onCreateDialog1(int id) {
        switch (id) {
            case TIME_DIALOG_ID:

                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);

        }
        return null;
    }*/

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour   = hourOfDay;
            minute = minutes;

            updateTime(hour,minute);

        }

    };

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
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

        txt_timer.setText(aTime);
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







