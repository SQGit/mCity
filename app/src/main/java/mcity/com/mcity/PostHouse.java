package mcity.com.mcity;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;



import static android.R.attr.data;
import static android.R.attr.defaultValue;

public class PostHouse extends Activity implements AdapterView.OnItemSelectedListener {

    String POST_HOUSE = Data_Service.URL_API + "postforrentnew";

    String id, token, path,  address,   description,  str_pho_enable;
    String root1,  root2, root3, str_landmark, str_address, str_residential,  str_bedroom,  str_bedroom2, str_renttype, str_furnished_type, str_monthrent, str_deposit, str_description, str_city;
    EditText  lanmark_et, address_et, rentamount_et, depositamount_et, description_et;
    TextView msg;
    int index;
    private static final int REQUEST_CODE = 1;
    private RadioGroup radioResidentialGroup1, radioResidentialGroupcut, radioBedroomothers, radioResidentialGroup2, radioBedroomGroup, radioBedroomGroupbar1, radioBedroomGroupbar2, radiorentGroup, radioFurnishedGroup;
    private RadioButton radioresidentalButton1, radioresidentalButtoncut, radioresidentalButton2, radioBedroomButton, radiobar1, radiobar2, radiorentButton, radioFurnishedButton;
    String[] city = {"Sylvan County", "Aqualily", "Iris Court", "Nova"};
    private static final String TAG = PostHouse.class.getSimpleName();
    HashSet<Uri> mMedia = new HashSet<Uri>();
    Spinner spinner;
    LinearLayout lnr_restrict1, lnr_restrict2, lnr_restrict3, lnr_withoutbar, lnr_withbar1, lnr_withbar2, lnr_progress_linear, lnr_button_liner, lnr_others;
    String spin_val;
    SharedPreferences sharedpreferences, sharedPreferences;
    ImageView img_submit;
    ArrayList<String> mdatas;
    int selectedId1, selectedId2, selectedId3, selectedId4, selectedId5, selectedId6, selectedId7, other_bed, selectedIdcut, selrent;
    Typeface tf;
    List<String> categories;
    CheckBox chk_phone_enable;
    Gallery gallery;
    ProgressBar progressBar;
    Dialog dialog2;



    //photo
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    Button camera;
    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    private ViewGroup mSelectedImagesContainer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_house);

        lanmark_et = (EditText) findViewById(R.id.landmark_et);
        address_et = (EditText) findViewById(R.id.Address_et);
        rentamount_et = (EditText) findViewById(R.id.rent_et);
        depositamount_et = (EditText) findViewById(R.id.deposit_et);
        description_et = (EditText) findViewById(R.id.description_et);
        img_submit = (ImageView) findViewById(R.id.post_submit);
        spinner = (Spinner) findViewById(R.id.spinner);
        lnr_restrict1 = (LinearLayout) findViewById(R.id.restrict1);
        lnr_restrict2 = (LinearLayout) findViewById(R.id.restrict2);
        lnr_restrict3 = (LinearLayout) findViewById(R.id.restrict3);
        lnr_withoutbar = (LinearLayout) findViewById(R.id.withoutbar);
        lnr_withbar1 = (LinearLayout) findViewById(R.id.withbar1);
        lnr_withbar2 = (LinearLayout) findViewById(R.id.withbar2);
        lnr_others = (LinearLayout) findViewById(R.id.others);
        radioResidentialGroup1 = (RadioGroup) findViewById(R.id.radioResidentialGroup1);
        radioResidentialGroup2 = (RadioGroup) findViewById(R.id.radioResidentialGroup2);
        radioResidentialGroupcut = (RadioGroup) findViewById(R.id.radioResidentialGroupcut);
        radioBedroomothers = (RadioGroup) findViewById(R.id.radioBedroomothers);
        radioBedroomGroup = (RadioGroup) findViewById(R.id.radioBedroomGroup);
        radioBedroomGroupbar1 = (RadioGroup) findViewById(R.id.radioBedroomGroupbar1);
        radioBedroomGroupbar2 = (RadioGroup) findViewById(R.id.radioBedroomGroupbar2);
        radiorentGroup = (RadioGroup) findViewById(R.id.radiorentGroup);
        radioFurnishedGroup = (RadioGroup) findViewById(R.id.radioFurnishedGroup);
        lnr_button_liner = (LinearLayout) findViewById(R.id.button_liner);
        chk_phone_enable = (CheckBox) findViewById(R.id.phone_enable);

        rentamount_et.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "5000000")});
        depositamount_et.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "10000000")});

        dialog2 = new Dialog(PostHouse.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);
        View getImages = findViewById(R.id.camera);
        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString("token", "");
        id = sharedPreferences.getString("id", "");

        //msg.setVisibility(View.VISIBLE);
        lnr_restrict1.setVisibility(View.VISIBLE);
        lnr_restrict2.setVisibility(View.GONE);
        lnr_restrict3.setVisibility(View.GONE);
        lnr_withoutbar.setVisibility(View.VISIBLE);
        lnr_withbar1.setVisibility(View.GONE);
        lnr_withbar2.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        img_submit.setVisibility(View.VISIBLE);
        lnr_others.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, city);

        mdatas = new ArrayList<>();
        spinner.setOnItemSelectedListener(this);
        categories = new ArrayList<String>();
        categories.add("Select Location");
        categories.add("Aqualily");
        categories.add("Iris Court");
        categories.add("Nova");
        categories.add("Sylvan County");
        categories.add("Others");


        // Creating adapter for spinner
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        setSpinner1();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spin_val = spinner.getItemAtPosition(position).toString();


                setLayout();
                setLocation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //*******************************Camera1 Activity*******************************************
        getImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()) {
                    Config config = new Config();
                    //config.setCameraHeight(R.dimen.app_camera_height);
                    //config.setToolbarTitleRes(R.string.custom_title);
                    config.setSelectionMin(1);
                    config.setSelectionLimit(4);

                    ImagePickerActivity.setConfig(config);
                    Intent intent = new Intent(PostHouse.this, ImagePickerActivity.class);
                    startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
                }
            }
        });
        //***********************************************end**********

        img_submit.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View v) {
                                              Log.e("tag", "12345");

                                              selrent = radiorentGroup.getCheckedRadioButtonId();//rent
                                              radiorentButton = (RadioButton) findViewById(selrent);
                                              selectedId5 = radioFurnishedGroup.getCheckedRadioButtonId();//furnish
                                              radioFurnishedButton = (RadioButton) findViewById(selectedId5);

                                              str_landmark = lanmark_et.getText().toString();
                                              str_address = address_et.getText().toString();
                                              str_renttype = radiorentButton.getText().toString();
                                              str_furnished_type = radioFurnishedButton.getText().toString();
                                              str_monthrent = rentamount_et.getText().toString();
                                              str_deposit = depositamount_et.getText().toString();
                                              str_description = description_et.getText().toString();


                                              if (spin_val.equals("Others")) {
                                                  selectedId3 = radioResidentialGroup1.getCheckedRadioButtonId();
                                                  radioresidentalButton1 = (RadioButton) findViewById(selectedId3);
                                                  str_residential = radioresidentalButton1.getText().toString();

                                                  other_bed = radioBedroomothers.getCheckedRadioButtonId();
                                                  radioBedroomButton = (RadioButton) findViewById(other_bed);
                                                  str_bedroom = radioBedroomButton.getText().toString();


                                                  if (str_bedroom.equals("2 BHK")) {
                                                      root2 = "2bhk";
                                                  } else if (str_bedroom.equals("3 BHK")) {
                                                      root2 = "3bhk";
                                                  } else if (str_bedroom.equals("4 BHK")) {
                                                      root2 = "4bhk";
                                                  } else {
                                                      root2 = "1bhk";
                                                  }


                                                  if (str_residential.equals("Apartment")) {
                                                      root1 = "Apartment";
                                                  } else if (str_residential.equals("Villa")) {
                                                      root1 = "Villa";
                                                  } else {
                                                      root1 = "Duplex";
                                                  }

                                              } else if (spin_val.equals("Aqualily")) {
                                                  selectedId3 = radioBedroomGroup.getCheckedRadioButtonId();
                                                  selectedId1 = radioResidentialGroup1.getCheckedRadioButtonId();
                                                  radioresidentalButton1 = (RadioButton) findViewById(selectedId1);
                                                  str_residential = radioresidentalButton1.getText().toString();
                                                  radioBedroomButton = (RadioButton) findViewById(selectedId3);
                                                  str_bedroom = radioBedroomButton.getText().toString();

                                                  if (str_bedroom.equals("2 BHK")) {
                                                      root2 = "2bhk";
                                                  } else if (str_bedroom.equals("3 BHK")) {
                                                      root2 = "3bhk";
                                                  } else {
                                                      root2 = "4bhk";
                                                  }


                                                  if (str_residential.equals("Apartment")) {
                                                      root1 = "Apartment";
                                                  } else if (str_residential.equals("Villa")) {
                                                      root1 = "Villa";
                                                  } else {
                                                      root1 = "Duplex";
                                                  }

                                              } else if (spin_val.equals("Sylvan County")) {
                                                  selectedId3 = radioBedroomGroup.getCheckedRadioButtonId();
                                                  lanmark_et.setText("Opposite BMW Factory");
                                                  address_et.setText("Mahindra City");
                                                  selectedId1 = radioResidentialGroup1.getCheckedRadioButtonId();
                                                  radioresidentalButton1 = (RadioButton) findViewById(selectedId1);
                                                  str_residential = radioresidentalButton1.getText().toString();

                                                  radioBedroomButton = (RadioButton) findViewById(selectedId3);
                                                  str_bedroom = radioBedroomButton.getText().toString();


                                                  if (str_bedroom.equals("2 BHK")) {
                                                      root2 = "2bhk";
                                                  } else if (str_bedroom.equals("3 BHK")) {
                                                      root2 = "3bhk";
                                                  } else {
                                                      root2 = "4bhk";
                                                  }


                                                  if (str_residential.equals("Apartment")) {
                                                      root1 = "Apartment";
                                                  } else if (str_residential.equals("Villa")) {
                                                      root1 = "Villa";
                                                  } else {
                                                      root1 = "Duplex";
                                                  }


                                              } else if (spin_val.equals("Nova")) {
                                                  selectedId2 = radioResidentialGroup2.getCheckedRadioButtonId();
                                                  selectedId6 = radioBedroomGroupbar1.getCheckedRadioButtonId();
                                                  radioresidentalButton2 = (RadioButton) findViewById(selectedId2);
                                                  radiobar1 = (RadioButton) findViewById(selectedId6);
                                                  str_bedroom = radiobar1.getText().toString();
                                                  str_residential = radioresidentalButton2.getText().toString();

                                                  if (str_bedroom.equals("1/1.5 BHK")) {
                                                      root2 = "1/1.5bhk";
                                                  } else if (str_bedroom.equals("2/2.5 BHK")) {
                                                      root2 = "2/2.5bhk";
                                                  } else {
                                                      root2 = "studio";
                                                  }

                                                  str_residential = radioresidentalButton2.getText().toString();

                                                  if (str_residential.equals("Apartment")) {

                                                  } else
                                                      root1 = "Duplex";
                                              } else if (spin_val.equals("Iris Court")) {
                                                  selectedIdcut = radioResidentialGroupcut.getCheckedRadioButtonId();
                                                  radioresidentalButtoncut = (RadioButton) findViewById(selectedIdcut);
                                                  selectedId7 = radioBedroomGroupbar2.getCheckedRadioButtonId();
                                                  str_residential = radioresidentalButtoncut.getText().toString();
                                                  radiobar2 = (RadioButton) findViewById(selectedId7);
                                                  str_bedroom = radiobar2.getText().toString();


                                                  if (str_bedroom.equals("2/2.5 BHK")) {
                                                      root2 = "2/2.5bhk";
                                                  } else
                                                      root2 = "3bhk";

                                                  if (str_residential.equals("Apartment")) {
                                                      root1 = "Apartment";
                                                  }
                                              }

                                              InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                              imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


                                              if (str_furnished_type.equals("Fully Furnished")) {
                                                  root3 = "Furnished";
                                              } else if (str_furnished_type.equals("Semi Furnished")) {
                                                  root3 = "Semi-furnished";
                                              } else
                                                  root3 = "Unfurnished";


                                              if (chk_phone_enable.isChecked()) {
                                                  str_pho_enable = "enabled";
                                              } else {
                                                  str_pho_enable = "Hidden Contact";
                                              }







                                              if(Util.Operations.isOnline(PostHouse.this)) {

                                                  if (!spin_val.equals(null) && !spin_val.contains("Select Location")) {

                                                      if (!str_landmark.isEmpty() && !str_address.isEmpty() && !str_monthrent.isEmpty() && !str_deposit.isEmpty() && !str_description.isEmpty()) {

                                                          new PostHouseAsync(spin_val, str_landmark, str_address, root1, root2, str_renttype,
                                                                  root3, str_monthrent, str_deposit, str_description, str_city, str_pho_enable).execute();

                                                      } else {
                                                          Toast.makeText(getApplicationContext(), "Invalid Fields..", Toast.LENGTH_LONG).show();
                                                      }


                                                  } else {
                                                      Toast.makeText(getApplicationContext(), "Please Select any Location..", Toast.LENGTH_LONG).show();
                                                  }

                                              }
                                                  else
                                                  {
                                                      Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                                                  }

                                          }
                                      }
        );
    }



    //**********************************************************************************************************************

    private boolean isStoragePermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("tag", "Permission is granted");
                return true;
            } else {
                Log.e("tag", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.e("tag", "Permission is granted");
            return true;
        }

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

            //do something
        }
    }



    private void showMedia() {
        mSelectedImagesContainer.removeAllViews();
        if (image_uris.size() >= 1) {
            mSelectedImagesContainer.setVisibility(View.VISIBLE);
        }

        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());


        for (Uri uri : image_uris) {

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item, null);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);

            Glide.with(this)
                    .load(uri.toString())
                    .fitCenter()
                    .into(thumbnail);

            mSelectedImagesContainer.addView(imageHolder);
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
        }

    }
//**********************************************************************************************************************
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
        spinner.setAdapter(arrayAdapter);
    }

    private void setLocation() {
        if (spin_val.equals("Aqualily")) {
            lanmark_et.setText("Opposite BMW Factory");
            address_et.setText("Mahindra City");
            lanmark_et.setEnabled(false);
            address_et.setEnabled(false);

        } else {
            if (spin_val.equals("Iris Court")) {
                lanmark_et.setText("Close to Paranur Station");
                address_et.setText("Mahindra City");
                lanmark_et.setEnabled(false);
                address_et.setEnabled(false);
            } else {
                if (spin_val.equals("Nova")) {
                    lanmark_et.setText("Close to Paranur Station");
                    address_et.setText("Mahindra City");
                    lanmark_et.setEnabled(false);
                    address_et.setEnabled(false);

                } else if (spin_val.equals("Sylvan County")) {

                    lanmark_et.setText("Close to Canopy");
                    address_et.setText("Mahindra City");
                    lanmark_et.setEnabled(false);
                    address_et.setEnabled(false);
                } else {
                    lanmark_et.setText("");
                    address_et.setText("");
                    lanmark_et.setEnabled(true);
                    address_et.setEnabled(true);
                }
            }
        }
    }

    private void setLayout() {
        if (spin_val.equals("Others")) {
            lnr_restrict1.setVisibility(View.VISIBLE);
            lnr_restrict2.setVisibility(View.GONE);
            lnr_restrict3.setVisibility(View.GONE);
            lnr_withbar1.setVisibility(View.GONE);
            lnr_withbar2.setVisibility(View.GONE);
            lnr_withoutbar.setVisibility(View.GONE);
            lnr_others.setVisibility(View.VISIBLE);

        } else if (spin_val.equals("Aqualily")) {
            lnr_restrict1.setVisibility(View.VISIBLE);
            lnr_restrict2.setVisibility(View.GONE);
            lnr_restrict3.setVisibility(View.GONE);
            lnr_withbar1.setVisibility(View.GONE);
            lnr_withbar2.setVisibility(View.GONE);
            lnr_withoutbar.setVisibility(View.VISIBLE);
            lnr_others.setVisibility(View.GONE);
        }
        else {
            if (spin_val.equals("Sylvan County")) {
                lnr_restrict1.setVisibility(View.VISIBLE);
                lnr_restrict2.setVisibility(View.GONE);
                lnr_restrict3.setVisibility(View.GONE);
                lnr_withbar1.setVisibility(View.GONE);
                lnr_withbar2.setVisibility(View.GONE);
                lnr_withoutbar.setVisibility(View.VISIBLE);
                lnr_others.setVisibility(View.GONE);

            }
            else {
                if (spin_val.equals("Nova")) {
                    Log.e("tag", "sds");
                    lnr_restrict1.setVisibility(View.GONE);
                    lnr_restrict2.setVisibility(View.VISIBLE);
                    lnr_restrict3.setVisibility(View.GONE);
                    lnr_withbar1.setVisibility(View.VISIBLE);
                    lnr_withbar2.setVisibility(View.GONE);
                    lnr_withoutbar.setVisibility(View.GONE);
                    lnr_others.setVisibility(View.GONE);
                }
                else {

                    lnr_restrict1.setVisibility(View.GONE);
                    lnr_restrict2.setVisibility(View.GONE);
                    lnr_restrict3.setVisibility(View.VISIBLE);
                    lnr_withbar1.setVisibility(View.GONE);
                    lnr_withbar2.setVisibility(View.VISIBLE);
                    lnr_withoutbar.setVisibility(View.GONE);
                    lnr_others.setVisibility(View.GONE);
                }
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class PostHouseAsync extends AsyncTask<String, String, String> {

        String spin_val, str_landmark, str_address, str_residential, str_bedroom, str_renttype, str_furnished_type, str_monthrent, str_deposit, str_description, str_city, str_pho_enable;


        public PostHouseAsync(String spin_val, String str_landmark, String str_address, String str_residential, String str_bedroom, String str_renttype, String str_furnished_type, String str_monthrent, String str_deposit, String str_description, String str_city, String str_pho_enable) {
            progressBar.setVisibility(View.VISIBLE);
            this.spin_val = spin_val;
            this.str_landmark = str_landmark;
            this.str_address = str_address;
            this.str_residential = str_residential;
            this.str_bedroom = str_bedroom;
            this.str_renttype = str_renttype;
            this.str_furnished_type = str_furnished_type;
            this.str_monthrent = str_monthrent;
            this.str_deposit = str_deposit;
            this.str_description = str_description;
            this.str_city = str_city;
            this.str_pho_enable = str_pho_enable;
        }


        protected void onPreExecute() {
            super.onPreExecute();
           dialog2.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String responseString = null;
            ContentBody cbFile = null;
            String jsonStr;
            //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost(POST_HOUSE);
                postMethod.addHeader("x-access-token", token);
                postMethod.addHeader("id", id);
                postMethod.addHeader("location", spin_val);
                postMethod.addHeader("landmark", str_landmark);
                postMethod.addHeader("address", str_address);
                postMethod.addHeader("city", "Mahindra City");
                postMethod.addHeader("furnishedtype", str_furnished_type);
                postMethod.addHeader("bedroom", root2);
                postMethod.addHeader("renttype", str_renttype);
                postMethod.addHeader("monthlyrent", str_monthrent);
                postMethod.addHeader("deposit", str_deposit);
                postMethod.addHeader("description", str_description);
                postMethod.addHeader("residential", root1);
                postMethod.addHeader("phone", str_pho_enable);

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                for( int i=0;i<image_uris.size();i++){
                    Log.e("tag","111"+image_uris);
                    Log.e("tag","222"+image_uris.size());
                    File sourceFile = new File(String.valueOf(image_uris.get(i)));
                    cbFile = new FileBody(sourceFile, "image/jpeg");
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

                    if (status.equals("true"))
                    {
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


                if (status.equals("true")) {
                    Log.e("tag","error_post"+msg);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    Intent back_service=new Intent(getApplicationContext(),RentalHistory.class);
                    startActivity(back_service);
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PostHouse.this, RentalHistory.class);
        startActivity(i);
        finish();
    }
}








