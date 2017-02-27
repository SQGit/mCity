package mcity.com.mcity;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.List;


/**
 * Created by Admin on 22-11-2016.
 */
public class MGarage_PostAds extends Activity implements AdapterView.OnItemSelectedListener{
    String LOGOUT = Data_Service.URL_API + "logout";
    String POST_GARAGE = Data_Service.URL_API + "postforsell";
    LinearLayout back_arrow,spinner_value;
    String str_category_main,str_category_sub;
    String str_field1, str_field2, str_field3, str_field4, str_field5, str_field6, str_field7, str_field8,category_details;
    String token, id,str_pho_enable,spin_val_fuel;
    EditText edt_description, editText_price, editText_yrmodel, editText_colour, editText_petrol, editText_kms, editText_description,editText_price1;
    LinearLayout linear_hide_bike1, linear_hide_bike2;
    ImageView submit;
    ProgressBar progressBar;
    Dialog dialog2;
    CheckBox chk_phone_enable;
    Spinner spinner_fuel;
    Typeface tf;
    List<String> categories;
    SharedPreferences shapre;
    SharedPreferences.Editor edtt;
    SharedPreferences s_pref;
    ImageView settings_icon;
    SharedPreferences.Editor editor;


    //photo
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    Button camera;
    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    private ViewGroup mSelectedImagesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mgarage_postads);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);



        back_arrow = (LinearLayout) findViewById(R.id.back_arrow);
        edt_description = (EditText) findViewById(R.id.txt_description);
        editText_price = (EditText) findViewById(R.id.editText_price);
        editText_yrmodel = (EditText) findViewById(R.id.editText_yrmodel);
        editText_colour = (EditText) findViewById(R.id.editText_colour);
        editText_price1=(EditText)findViewById(R.id.editText_price1);
        editText_kms = (EditText) findViewById(R.id.editText_kms);
        editText_description = (EditText) findViewById(R.id.editText_description);
        linear_hide_bike1 = (LinearLayout) findViewById(R.id.linear_hide_bike1);
        linear_hide_bike2 = (LinearLayout) findViewById(R.id.linear_hide_bike2);
        submit = (ImageView) findViewById(R.id.submit);
        chk_phone_enable=(CheckBox)findViewById(R.id.phone_enable);
        spinner_value=(LinearLayout)findViewById(R.id.spinner_value);
        spinner_fuel = (Spinner) findViewById(R.id.spinner);
        settings_icon=(ImageView)findViewById(R.id.settings_icon);

        dialog2 = new Dialog(MGarage_PostAds.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);
        editText_price.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "10000000")});
        editText_price1.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "10000000")});

        linear_hide_bike2.setVisibility(View.GONE);
        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);
        View getImages = findViewById(R.id.camera);
        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");

        /*shapre = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edtt = shapre.edit();
        edtt.remove(category_details);
        edtt.apply();
        edtt.commit();*/

        categoriesList();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString("token", "");
        id = sharedPreferences.getString("id", "");



        spinner_fuel.setOnItemSelectedListener(this);
        categories=new ArrayList<String>();
        categories = new ArrayList<String>();
        categories.add("Select Fuel");
        categories.add("Petrol");
        categories.add("Diesel");
        categories.add("LPG");
        // Creating adapter for spinner

        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent);
                finish();
            }
        });


        //*******************************Camera1 Activity*******************************************
        getImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean result = Utility.checkPermission(MGarage_PostAds.this);
                if (result) {

                    Config config = new Config();
                    config.setSelectionMin(4);
                    config.setSelectionLimit(4);
                    ImagePickerActivity.setConfig(config);
                    Intent intent = new Intent(MGarage_PostAds.this, ImagePickerActivity.class);
                    startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
                }

            }
        });
        //***********************************************end**********



        settings_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MGarage_PostAds.this, settings_icon);
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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_phone_enable.isChecked()) {
                    str_pho_enable = "enabled";
                } else {
                    str_pho_enable = "Hidden Contact";
                }

                str_field1 = edt_description.getText().toString();
                if(str_field1.equals("Motorcycles")||str_field1.equals("Scootors")||str_field1.equals("Cars")||str_field1.equals("Commercial Vehicles"))
                {

                    str_field2 = editText_price.getText().toString();
                    str_field3 = editText_yrmodel.getText().toString();
                    str_field4 = editText_colour.getText().toString();
                    Log.e("tag","check_color"+str_field4);
                    str_field5 = spinner_fuel.getSelectedItem().toString();
                    str_field6 = editText_kms.getText().toString();
                    str_field7 = editText_description.getText().toString();

                    if (Util.Operations.isOnline(MGarage_PostAds.this))
                    {
                        if (!str_field1.isEmpty() && !str_field2.isEmpty() && !str_field3.isEmpty() && !str_field4.isEmpty() && !str_field5.isEmpty() && !str_field6.isEmpty() && !str_field7.isEmpty()) {
                            new PostGarageAsync(str_field1, str_field2, str_field3, str_field4, str_field5, str_field6,
                                    str_field7, str_field8,str_pho_enable).execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Fields..", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                        }
                }
                else
                {
                    str_field1 = edt_description.getText().toString();
                    str_field2 = editText_price1.getText().toString();
                    str_field7 = editText_description.getText().toString();

                    if (Util.Operations.isOnline(MGarage_PostAds.this))
                    {
                        if (!str_field1.isEmpty() && !str_field2.isEmpty()&& !str_field7.isEmpty()) {
                            new PostGarageAsyncOne(str_field1, str_field2, str_field7,str_field8,str_pho_enable).execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Fields..", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
                    }
                }
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

        LayoutInflater layoutInflater = LayoutInflater.from(MGarage_PostAds.this);
        View promptView = layoutInflater.inflate(R.layout.aboutus, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(MGarage_PostAds .this).create();
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

        LayoutInflater layoutInflater = LayoutInflater.from(MGarage_PostAds.this);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(MGarage_PostAds.this).create();
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
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(MGarage_PostAds.this);
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

    private void categoriesList() {

        SharedPreferences s_pref =PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = s_pref.edit();
        edit.remove(category_details);
        edit.apply();


        submit.setVisibility(View.GONE);
        LayoutInflater layoutInflater = LayoutInflater.from(MGarage_PostAds.this);
        View promptView = layoutInflater.inflate(R.layout.category_list, null);
        final AlertDialog alertD1 = new AlertDialog.Builder(MGarage_PostAds.this).create();
        alertD1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertD1.setCancelable(true);
        Window window = alertD1.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tv_head = (TextView) promptView.findViewById(R.id.txtv);
        final RadioGroup radioGroup = (RadioGroup) promptView.findViewById(R.id.radioGroup2);
        final RadioButton rb_bike = (RadioButton) promptView.findViewById(R.id.radio1);
        final RadioButton rb_cars = (RadioButton) promptView.findViewById(R.id.radio2);
        final RadioButton rb_electronics = (RadioButton) promptView.findViewById(R.id.radio3);
        final RadioButton rb_mobile = (RadioButton) promptView.findViewById(R.id.radio4);
        final RadioButton rb_furniture = (RadioButton) promptView.findViewById(R.id.radio5);
        final RadioButton rb_books = (RadioButton) promptView.findViewById(R.id.radio6);
        final RadioButton rb_pets = (RadioButton) promptView.findViewById(R.id.radio7);
        final Button garage_submit = (Button) promptView.findViewById(R.id.garage_submit);
        final Button garage_back = (Button) promptView.findViewById(R.id.garage_back);


        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        category_details = sharedPreferences.getString("category_details", "");
        Log.e("Tag12", "aaa" + category_details);

        if(!category_details.equals(""))
        {
            if(category_details.equals("Bikes, Bike Spares, Accessories"))
            {
                rb_bike.setChecked(true);

            }else if(category_details.equals("Cars, Car Spare Parts"))
            {
                rb_cars.setChecked(true);
            }else if(category_details.equals("Electronics & Home Appliances"))
            {
                rb_electronics.setChecked(true);
            }
            else if(category_details.equals("Mobiles & Mobile Accessories"))
            {
                rb_mobile.setChecked(true);
            }
            else if(category_details.equals("Furniture, Household Articles"))
            {
                rb_furniture.setChecked(true);
            }
            else if(category_details.equals("Books"))
            {
                rb_books.setChecked(true);
            }
            else if(category_details.equals("Pets"))
            {
                rb_pets.setChecked(true);
            }
        }
        else
        {
            rb_bike.setChecked(false);
            rb_cars.setChecked(false);
            rb_electronics.setChecked(false);
            rb_mobile.setChecked(false);
            rb_furniture.setChecked(false);
            rb_books.setChecked(false);
            rb_pets.setChecked(false);
        }




        Typeface tff = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        tv_head.setTypeface(tff);
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        rb_bike.setTypeface(tf);
        rb_cars.setTypeface(tf);
        rb_electronics.setTypeface(tf);
        rb_mobile.setTypeface(tf);
        rb_furniture.setTypeface(tf);
        rb_books.setTypeface(tf);
        rb_pets.setTypeface(tf);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertD1.isShowing()) {
                    alertD1.dismiss();
                }
            }
        };

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio1) {
                    str_category_main = "Bikes, Bike Spares, Accessories";
                    Log.e("TAG", "asd1" + str_category_main);

                } else if (checkedId == R.id.radio2) {
                    str_category_main = "Cars, Car Spare Parts";
                    Log.e("TAG", "asd2" + str_category_main);

                } else if (checkedId == R.id.radio3) {
                    str_category_main = "Electronics & Home Appliances";
                    Log.e("TAG", "asd3" + str_category_main);

                } else if (checkedId == R.id.radio4) {
                    str_category_main = "Mobiles & Mobile Accessories";
                    Log.e("TAG", "asd4" + str_category_main);

                } else if (checkedId == R.id.radio5) {
                    str_category_main = "Furniture, Household Articles";
                    Log.e("TAG", "asd5" + str_category_main);

                } else if (checkedId == R.id.radio6) {
                    str_category_main = "Books";
                    Log.e("TAG", "asd6" + str_category_main);

                } else if (checkedId == R.id.radio7) {
                    str_category_main = "Pets";
                    Log.e("TAG", "asd7" + str_category_main);
                }
                SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor edit = s_pref.edit();
                edit.putString("category_details", str_category_main);
                edit.commit();
            }
        });


        garage_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb_bike.setChecked(false);
                rb_cars.setChecked(false);
                rb_electronics.setChecked(false);
                rb_mobile.setChecked(false);
                rb_furniture.setChecked(false);
                rb_books.setChecked(false);
                rb_pets.setChecked(false);

                Intent garage_back=new Intent(getApplicationContext(),MGarage_History.class);
                startActivity(garage_back);
                finish();
            }
        });


        garage_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str_category_main == null) {
                    Toast.makeText(MGarage_PostAds.this, "Please select any Category", Toast.LENGTH_LONG).show();
                } else if (str_category_main.equals("Bikes, Bike Spares, Accessories")) {

                    selectBikeCategory();
                    alertD1.dismiss();
                } else if (str_category_main.equals("Cars, Car Spare Parts")) {
                    selectCarCategory();
                    alertD1.dismiss();
                } else if (str_category_main.equals("Electronics & Home Appliances")) {
                    selectElectricCategory();
                    alertD1.dismiss();
                } else if (str_category_main.equals("Mobiles & Mobile Accessories")) {
                    selectMobileCategory();
                    alertD1.dismiss();
                } else if (str_category_main.equals("Furniture, Household Articles")) {
                    selectfurnitureCategory();
                    alertD1.dismiss();
                } else if (str_category_main.equals("Books")) {
                    selectBookCategory();
                    alertD1.dismiss();
                } else if (str_category_main.equals("Pets")) {
                    selectPetCategory();
                    alertD1.dismiss();
                }

            }
        });
        alertD1.setView(promptView);
        alertD1.show();
    }


    //********************************************Pets Activity Start************************************
    private void selectPetCategory() {
        LayoutInflater layoutInflater = LayoutInflater.from(MGarage_PostAds.this);
        View promptView = layoutInflater.inflate(R.layout.pets, null);
        final AlertDialog alertD_pets = new AlertDialog.Builder(MGarage_PostAds.this).create();
        alertD_pets.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = alertD_pets.getWindow();
        alertD_pets.setCancelable(true);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tv_head = (TextView) promptView.findViewById(R.id.txtv);
        final RadioGroup radioGroup = (RadioGroup) promptView.findViewById(R.id.radioGroup2);
        final RadioButton rb_pets1 = (RadioButton) promptView.findViewById(R.id.rb_pets1);
        final RadioButton rb_pets2 = (RadioButton) promptView.findViewById(R.id.rb_pets2);
        final RadioButton rb_pets3 = (RadioButton) promptView.findViewById(R.id.rb_pets3);
        final RadioButton rb_pets4 = (RadioButton) promptView.findViewById(R.id.rb_pets4);
        final Button btn_pet_submit = (Button) promptView.findViewById(R.id.btn_pet_submit);
        final Button btn_pet_back = (Button) promptView.findViewById(R.id.btn_pet_back);
        Typeface tff = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        tv_head.setTypeface(tff);
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        rb_pets1.setTypeface(tf);
        rb_pets2.setTypeface(tf);
        rb_pets3.setTypeface(tf);
        rb_pets4.setTypeface(tf);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertD_pets.isShowing()) {
                    alertD_pets.dismiss();
                }
            }
        };


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_pets1) {
                    str_category_sub = "Fishes and Aquarium";
                } else if (checkedId == R.id.rb_pets2) {
                    str_category_sub = "Dogs";
                } else if (checkedId == R.id.rb_pets3) {
                    str_category_sub = "Other Pets";
                } else if (checkedId == R.id.rb_pets4) {
                    str_category_sub = "Pet Food And Accessories";
                }


            }
        });


        btn_pet_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertD_pets.dismiss();
                categoriesList();
            }
        });


        btn_pet_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str_category_sub == null) {
                    Toast.makeText(MGarage_PostAds.this, "Please select any Category", Toast.LENGTH_LONG).show();
                } else if (str_category_sub.equals("Fishes and Aquarium")) {
                    edt_description.setText(str_category_sub);
                    Log.e("tag", "1");
                    editText_price1.requestFocus();
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_pets.dismiss();
                } else if (str_category_sub.equals("Dogs")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_pets.dismiss();

                } else if (str_category_sub.equals("Other Pets")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_pets.dismiss();
                } else if (str_category_sub.equals("Pet Food And Accessories")) {
                    edt_description.setText(str_category_sub);
                    editText_price1.requestFocus();
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_pets.dismiss();
                } else if (str_category_sub.equals(null)) {
                    Toast.makeText(MGarage_PostAds.this, "Please any Category", Toast.LENGTH_LONG).show();
                }


            }
        });

        alertD_pets.setView(promptView);
        alertD_pets.show();

    }
    //********************************************Books Activity End************************************


    //********************************************Books Activity Start************************************
    private void selectBookCategory() {
        LayoutInflater layoutInflater = LayoutInflater.from(MGarage_PostAds.this);
        View promptView = layoutInflater.inflate(R.layout.books, null);
        final AlertDialog alertD_book = new AlertDialog.Builder(MGarage_PostAds.this).create();
        alertD_book.setCancelable(true);
        Window window = alertD_book.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tv_head = (TextView) promptView.findViewById(R.id.txtv);
        final RadioGroup radioGroup = (RadioGroup) promptView.findViewById(R.id.radioGroup2);
        final RadioButton rb_book1 = (RadioButton) promptView.findViewById(R.id.rb_book1);
        final RadioButton rb_book2 = (RadioButton) promptView.findViewById(R.id.rb_book2);
        final RadioButton rb_book3 = (RadioButton) promptView.findViewById(R.id.rb_book3);
        final RadioButton rb_book4 = (RadioButton) promptView.findViewById(R.id.rb_book4);
        final RadioButton rb_book5 = (RadioButton) promptView.findViewById(R.id.rb_book5);

        final Button btn_book_submit = (Button) promptView.findViewById(R.id.btn_book_submit);
        final Button btn_book_back = (Button) promptView.findViewById(R.id.btn_book_back);

        Typeface tff = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        tv_head.setTypeface(tff);
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        rb_book1.setTypeface(tf);
        rb_book2.setTypeface(tf);
        rb_book3.setTypeface(tf);
        rb_book4.setTypeface(tf);
        rb_book5.setTypeface(tf);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertD_book.isShowing())
                {
                    alertD_book.dismiss();
                }
            }
        };


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_book1) {
                    str_category_sub = "Books";

                } else if (checkedId == R.id.rb_book2) {
                    str_category_sub = "Musical Instruments";
                } else if (checkedId == R.id.rb_book3) {
                    str_category_sub = "Sports Equipment";
                } else if (checkedId == R.id.rb_book4) {
                    str_category_sub = "Gym and Fitness";
                } else if (checkedId == R.id.rb_book5) {
                    str_category_sub = "Other Hobbies";
                }

            }
        });


        btn_book_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertD_book.dismiss();
                categoriesList();
            }
        });
        btn_book_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str_category_sub == null) {
                    Toast.makeText(MGarage_PostAds.this, "Please Select any Category", Toast.LENGTH_LONG).show();
                } else if (str_category_sub.equals("Books")) {
                    edt_description.setText(str_category_sub);
                    Log.e("tag", "1");
                    editText_price1.requestFocus();
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_book.dismiss();
                } else if (str_category_sub.equals("Musical Instruments")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_book.dismiss();
                } else if (str_category_sub.equals("Sports Equipment")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_book.dismiss();
                } else if (str_category_sub.equals("Gym and Fitness")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_book.dismiss();
                } else if (str_category_sub.equals("Other Hobbies")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_book.dismiss();
                }

            }
        });
        alertD_book.setView(promptView);
        alertD_book.show();
    }
    //********************************************Books Activity End************************************


    //********************************************Furniture Activity Start************************************
    private void selectfurnitureCategory() {

        LayoutInflater layoutInflater = LayoutInflater.from(MGarage_PostAds.this);
        View promptView = layoutInflater.inflate(R.layout.furniture, null);
        final AlertDialog alertD_furniture = new AlertDialog.Builder(MGarage_PostAds.this).create();
        alertD_furniture.setCancelable(true);
        Window window = alertD_furniture.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tv_head = (TextView) promptView.findViewById(R.id.txtv);
        final RadioGroup radioGroup = (RadioGroup) promptView.findViewById(R.id.radioGroup2);
        final RadioButton rb_fur1 = (RadioButton) promptView.findViewById(R.id.rb_fur1);
        final RadioButton rb_fur2 = (RadioButton) promptView.findViewById(R.id.rb_fur2);
        final RadioButton rb_fur3 = (RadioButton) promptView.findViewById(R.id.rb_fur3);
        final RadioButton rb_fur4 = (RadioButton) promptView.findViewById(R.id.rb_fur4);
        final RadioButton rb_fur5 = (RadioButton) promptView.findViewById(R.id.rb_fur5);

        final Button btn_furniture_submit = (Button) promptView.findViewById(R.id.btn_furniture_submit);
        final Button btn_furniture_back = (Button) promptView.findViewById(R.id.btn_furniture_back);

        Typeface tff = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        tv_head.setTypeface(tff);
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        rb_fur1.setTypeface(tf);
        rb_fur2.setTypeface(tf);
        rb_fur3.setTypeface(tf);
        rb_fur4.setTypeface(tf);
        rb_fur5.setTypeface(tf);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertD_furniture.isShowing()) {
                    alertD_furniture.dismiss();
                }
            }
        };


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_fur1) {
                    str_category_sub = "Sofa and Dining";

                } else if (checkedId == R.id.rb_fur2) {
                    str_category_sub = "Beds and Wardrobes";
                } else if (checkedId == R.id.rb_fur3) {
                    str_category_sub = "Home Decor and Garden";
                } else if (checkedId == R.id.rb_fur4) {
                    str_category_sub = "Other Household Items";
                } else if (checkedId == R.id.rb_fur5) {
                    str_category_sub = "Kids Furniture";
                }

            }
        });

        btn_furniture_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertD_furniture.dismiss();
                categoriesList();
            }
        });

        btn_furniture_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str_category_sub == null) {
                    Toast.makeText(MGarage_PostAds.this, "Please Select any Category", Toast.LENGTH_LONG).show();
                } else if (str_category_sub.equals("Sofa and Dining")) {
                    edt_description.setText(str_category_sub);
                    editText_price1.requestFocus();
                    Log.e("tag", "1");
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_furniture.dismiss();
                } else if (str_category_sub.equals("Beds and Wardrobes")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_furniture.dismiss();
                } else if (str_category_sub.equals("Home Decor and Garden")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_furniture.dismiss();
                } else if (str_category_sub.equals("Other Household Items")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_furniture.dismiss();
                } else if (str_category_sub.equals("Kids Furniture")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_furniture.dismiss();
                }


            }
        });

        alertD_furniture.setView(promptView);
        alertD_furniture.show();
    }
    //********************************************Furniture Activity End************************************


    //********************************************Mobile Activity Start************************************
    private void selectMobileCategory() {
        LayoutInflater layoutInflater = LayoutInflater.from(MGarage_PostAds.this);
        View promptView = layoutInflater.inflate(R.layout.mobile, null);
        final AlertDialog alertD_mobile = new AlertDialog.Builder(MGarage_PostAds.this).create();
        alertD_mobile.setCancelable(true);
        Window window = alertD_mobile.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tv_head = (TextView) promptView.findViewById(R.id.txtv);
        final RadioGroup radioGroup = (RadioGroup) promptView.findViewById(R.id.radioGroup2);
        final RadioButton rb_mob1 = (RadioButton) promptView.findViewById(R.id.rb_mob1);
        final RadioButton rb_mob2 = (RadioButton) promptView.findViewById(R.id.rb_mob2);
        final RadioButton rb_mob3 = (RadioButton) promptView.findViewById(R.id.rb_mob3);

        final Button btn_mobile_submit = (Button) promptView.findViewById(R.id.btn_mobile_submit);
        final Button btn_mobile_back = (Button) promptView.findViewById(R.id.btn_mobile_back);

        Typeface tff = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        tv_head.setTypeface(tff);
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        rb_mob1.setTypeface(tf);
        rb_mob2.setTypeface(tf);
        rb_mob3.setTypeface(tf);


        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertD_mobile.isShowing()) {
                    alertD_mobile.dismiss();
                }
            }
        };


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_mob1) {
                    str_category_sub = "Mobile Phones";

                } else if (checkedId == R.id.rb_mob2) {
                    str_category_sub = "Tablets";
                } else if (checkedId == R.id.rb_mob3) {
                    str_category_sub = "Accessories";
                }
            }
        });

        btn_mobile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD_mobile.dismiss();
                categoriesList();
            }
        });

        btn_mobile_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str_category_sub == null) {
                    Toast.makeText(MGarage_PostAds.this, "Please select any Category", Toast.LENGTH_LONG).show();
                } else if (str_category_sub.equals("Mobile Phones")) {
                    edt_description.setText(str_category_sub);
                    Log.e("tag", "1");
                    editText_price1.requestFocus();
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_mobile.dismiss();
                } else if (str_category_sub.equals("Tablets")) {
                    edt_description.setText(str_category_sub);
                    editText_price1.requestFocus();
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_mobile.dismiss();
                } else if (str_category_sub.equals("Accessories")) {
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    editText_price1.requestFocus();
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_mobile.dismiss();
                }


            }
        });

        alertD_mobile.setView(promptView);
        alertD_mobile.show();
    }
    //********************************************Mobile Activity End************************************


    //********************************************Electronics Activity Start************************************
    private void selectElectricCategory() {
        LayoutInflater layoutInflater = LayoutInflater.from(MGarage_PostAds.this);
        View promptView = layoutInflater.inflate(R.layout.electronics, null);
        final AlertDialog alertD_elec = new AlertDialog.Builder(MGarage_PostAds.this).create();
        alertD_elec.setCancelable(true);
        Window window = alertD_elec.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tv_head = (TextView) promptView.findViewById(R.id.txtv);
        final RadioGroup radioGroup = (RadioGroup) promptView.findViewById(R.id.radioGroup2);
        final RadioButton rb_ele1 = (RadioButton) promptView.findViewById(R.id.rb_ele1);
        final RadioButton rb_ele2 = (RadioButton) promptView.findViewById(R.id.rb_ele2);
        final RadioButton rb_ele3 = (RadioButton) promptView.findViewById(R.id.rb_ele3);
        //final RadioButton rb_ele4 = (RadioButton) promptView.findViewById(R.id.rb_ele4);
        final RadioButton rb_ele5 = (RadioButton) promptView.findViewById(R.id.rb_ele5);
        //final RadioButton rb_ele6 = (RadioButton) promptView.findViewById(R.id.rb_ele6);
        final RadioButton rb_ele7 = (RadioButton) promptView.findViewById(R.id.rb_ele7);
        final RadioButton rb_ele8 = (RadioButton) promptView.findViewById(R.id.rb_ele8);
        final RadioButton rb_ele9 = (RadioButton) promptView.findViewById(R.id.rb_ele9);
        final RadioButton rb_ele10 = (RadioButton) promptView.findViewById(R.id.rb_ele10);
        final Button btn_elec_submit = (Button) promptView.findViewById(R.id.btn_elec_submit);
        final Button btn_elec_back = (Button) promptView.findViewById(R.id.btn_elec_back);

        Typeface tff = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        tv_head.setTypeface(tff);
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        rb_ele1.setTypeface(tf);
        rb_ele2.setTypeface(tf);
        rb_ele3.setTypeface(tf);
        //rb_ele4.setTypeface(tf);
        rb_ele5.setTypeface(tf);
        //rb_ele6.setTypeface(tf);
        rb_ele7.setTypeface(tf);
        rb_ele8.setTypeface(tf);
        rb_ele9.setTypeface(tf);
        rb_ele10.setTypeface(tf);


        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertD_elec.isShowing()) {
                    alertD_elec.dismiss();
                }
            }
        };


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_ele1) {
                    str_category_sub = "Computers and Laptops";

                } else if (checkedId == R.id.rb_ele2) {
                    str_category_sub = "TVs, Video- Audio";
                } else if (checkedId == R.id.rb_ele3) {
                    str_category_sub = "Hard Disks, Printers and Monitors";
                }

                else if (checkedId == R.id.rb_ele5) {
                    str_category_sub = "Washing Machines";
                }

                else if (checkedId == R.id.rb_ele7) {
                    str_category_sub = "Computer Accessories";
                } else if (checkedId == R.id.rb_ele8) {
                    str_category_sub = "Cameras and Lenses";
                } else if (checkedId == R.id.rb_ele9) {
                    str_category_sub = "Kitchen and Other Appliances";
                } else if (checkedId == R.id.rb_ele10) {
                    str_category_sub = "Games and Entertainment";
                }


            }
        });
        btn_elec_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertD_elec.dismiss();
                categoriesList();
            }
        });






        btn_elec_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (str_category_sub == null) {
                    Toast.makeText(MGarage_PostAds.this, "Please select any Category", Toast.LENGTH_LONG).show();
                } else if (str_category_sub.equals("Computers and Laptops")) {
                    edt_description.setText(str_category_sub);
                    editText_price1.requestFocus();
                    Log.e("tag", "1");
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_elec.dismiss();
                } else if (str_category_sub.equals("TVs, Video- Audio")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_elec.dismiss();
                } else if (str_category_sub.equals("Hard Disks, Printers and Monitors")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_elec.dismiss();
                }

                else if (str_category_sub.equals("Washing Machines")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_elec.dismiss();
                } else if (str_category_sub.equals("Fridges")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_elec.dismiss();
                } else if (str_category_sub.equals("Computer Accessories")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_elec.dismiss();
                } else if (str_category_sub.equals("Cameras and Lenses")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_elec.dismiss();
                } else if (str_category_sub.equals("Kitchen and Other Appliances")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_elec.dismiss();
                } else if (str_category_sub.equals("Games and Entertainment")) {
                    editText_price1.requestFocus();
                    edt_description.setText(str_category_sub);
                    Log.e("tag", "3");
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_elec.dismiss();
                }


            }
        });

        alertD_elec.setView(promptView);
        alertD_elec.show();
    }
    //********************************************Electronics Activity Start************************************


    //********************************************Car Activity Start************************************
    private void selectCarCategory() {
        LayoutInflater layoutInflater = LayoutInflater.from(MGarage_PostAds.this);
        View promptView = layoutInflater.inflate(R.layout.cars, null);
        final AlertDialog alertD_car = new AlertDialog.Builder(MGarage_PostAds.this).create();
        alertD_car.setCancelable(true);
        Window window = alertD_car.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tv_head = (TextView) promptView.findViewById(R.id.txtv);
        final RadioGroup radioGroup = (RadioGroup) promptView.findViewById(R.id.radioGroup2);
        final RadioButton rb_car1 = (RadioButton) promptView.findViewById(R.id.rb_car1);
        final RadioButton rb_car2 = (RadioButton) promptView.findViewById(R.id.rb_car2);
        final RadioButton rb_car3 = (RadioButton) promptView.findViewById(R.id.rb_car3);
        final RadioButton rb_car4 = (RadioButton) promptView.findViewById(R.id.rb_car4);
        final Button btn_car_submit = (Button) promptView.findViewById(R.id.btn_car_submit);
        final Button btn_car_back = (Button) promptView.findViewById(R.id.btn_car_back);
        Typeface tff = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        tv_head.setTypeface(tff);
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        rb_car1.setTypeface(tf);
        rb_car2.setTypeface(tf);
        rb_car3.setTypeface(tf);
        rb_car4.setTypeface(tf);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertD_car.isShowing()) {
                    alertD_car.dismiss();
                }
            }
        };


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_car1) {
                    str_category_sub = "Cars";

                } else if (checkedId == R.id.rb_car2) {
                    str_category_sub = "Commercial Vehicles";
                } else if (checkedId == R.id.rb_car3) {
                    str_category_sub = "Other Vehicles";
                } else if (checkedId == R.id.rb_car4) {
                    str_category_sub = "Spare Parts";
                }


            }
        });

        btn_car_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD_car.dismiss();
                categoriesList();
            }
        });

        btn_car_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str_category_sub == null) {
                    Toast.makeText(MGarage_PostAds.this, "Please select any Category", Toast.LENGTH_LONG).show();
                } else if (str_category_sub.equals("Cars")) {
                    edt_description.setText(str_category_sub);
                    editText_price.requestFocus();
                    linear_hide_bike1.setVisibility(View.VISIBLE);
                    linear_hide_bike2.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_car.dismiss();
                } else if (str_category_sub.equals("Commercial Vehicles")) {
                    edt_description.setText(str_category_sub);
                    editText_price.requestFocus();
                    linear_hide_bike1.setVisibility(View.VISIBLE);
                    linear_hide_bike2.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_car.dismiss();
                } else if (str_category_sub.equals("Other Vehicles")) {
                    edt_description.setText(str_category_sub);
                    editText_price1.requestFocus();
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_car.dismiss();
                } else if (str_category_sub.equals("Spare Parts")) {
                    edt_description.setText(str_category_sub);
                    editText_price1.requestFocus();
                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_car.dismiss();
                }


            }
        });

        alertD_car.setView(promptView);
        alertD_car.show();
    }
    //********************************************Car Activity Start************************************


    //********************************************Bike Activity Start************************************
    private void selectBikeCategory() {

        LayoutInflater layoutInflater = LayoutInflater.from(MGarage_PostAds.this);
        View promptView = layoutInflater.inflate(R.layout.bikes, null);
        final AlertDialog alertD_bike = new AlertDialog.Builder(MGarage_PostAds.this).create();
        alertD_bike.setCancelable(true);
        Window window = alertD_bike.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tv_head = (TextView) promptView.findViewById(R.id.txtv);
        final RadioGroup radioGroup = (RadioGroup) promptView.findViewById(R.id.radioGroup2);
        final RadioButton rb_bike1 = (RadioButton) promptView.findViewById(R.id.radio1);
        final RadioButton rb_bike2 = (RadioButton) promptView.findViewById(R.id.radio2);
        final RadioButton rb_bike3 = (RadioButton) promptView.findViewById(R.id.radio3);
        final RadioButton rb_bike4 = (RadioButton) promptView.findViewById(R.id.radio4);
        final Button btn_bike_submit = (Button) promptView.findViewById(R.id.btn_bike_submit);
        final Button btn_bike_back = (Button) promptView.findViewById(R.id.btn_bike_back);

        Typeface tff = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        tv_head.setTypeface(tff);
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        rb_bike1.setTypeface(tf);
        rb_bike2.setTypeface(tf);
        rb_bike3.setTypeface(tf);
        rb_bike4.setTypeface(tf);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertD_bike.isShowing()) {
                    alertD_bike.dismiss();
                }
            }
        };


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radio1) {
                    str_category_sub = "Motorcycles";

                } else if (checkedId == R.id.radio2) {
                    str_category_sub = "Scootors";
                } else if (checkedId == R.id.radio3) {
                    str_category_sub = "Bicycles";
                } else if (checkedId == R.id.radio4) {
                    str_category_sub = "Spare Parts";

                }



            }
        });


        btn_bike_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               alertD_bike.dismiss();
            categoriesList();
            }
        });

        btn_bike_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (str_category_sub == null) {
                    Toast.makeText(MGarage_PostAds.this, "Please Select Category", Toast.LENGTH_LONG).show();
                } else if (str_category_sub.equals("Motorcycles")) {
                    edt_description.setText(str_category_sub);
                    editText_price.requestFocus();

                    linear_hide_bike1.setVisibility(View.VISIBLE);
                    linear_hide_bike2.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_bike.dismiss();
                } else if (str_category_sub.equals("Scootors")) {
                    edt_description.setText(str_category_sub);
                    editText_price.requestFocus();
                    linear_hide_bike1.setVisibility(View.VISIBLE);
                    linear_hide_bike2.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_bike.dismiss();
                } else if (str_category_sub.equals("Bicycles")) {
                    edt_description.setText(str_category_sub);
                    editText_price1.requestFocus();

                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_bike.dismiss();

                } else if (str_category_sub.equals("Spare Parts")) {
                    edt_description.setText(str_category_sub);
                    editText_price1.requestFocus();

                    linear_hide_bike1.setVisibility(View.GONE);
                    linear_hide_bike2.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    alertD_bike.dismiss();
                }
            }
        });

        alertD_bike.setView(promptView);
        alertD_bike.show();
    }
    //********************************************Bike Activity End************************************




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
            submit.setVisibility(View.GONE);
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            String responseString = null;
            ContentBody cbFile = null;
            String jsonStr;
            //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost(POST_GARAGE);
                postMethod.addHeader("x-access-token", token);
                postMethod.addHeader("id", id);
                postMethod.addHeader("adtitle", str_field1);
                postMethod.addHeader("category", str_category_main);
                Log.e("tag","check_category"+ str_category_main);
                postMethod.addHeader("price", str_field2);
                postMethod.addHeader("field1", str_field3);
                postMethod.addHeader("field2", str_field4);
                postMethod.addHeader("field3", str_field5);
                postMethod.addHeader("field4", str_field6);
                postMethod.addHeader("description", str_field7);
                postMethod.addHeader("phone", str_pho_enable);

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
        protected void onPostExecute(String responseString) {

            super.onPostExecute(responseString);
            Log.e("tag","agvavahsh"+responseString);
            dialog2.dismiss();
            submit.setVisibility(View.VISIBLE);

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
        // Remove all views before
        // adding the new ones.
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
            String responseString = null;
            ContentBody cbFile = null;
            String jsonStr;
            //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

            try {
                Log.e("tag","3333");
                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost(POST_GARAGE);
                postMethod.addHeader("x-access-token", token);
                Log.e("tag","44444"+token+id);
                postMethod.addHeader("id", id);
                postMethod.addHeader("adtitle", str_field1);
                postMethod.addHeader("category", str_category_main);
                postMethod.addHeader("price", str_field2);
                postMethod.addHeader("description", str_field7);
                postMethod.addHeader("phone", str_pho_enable);
                postMethod.addHeader("field1", "not specified");
                postMethod.addHeader("field2", "not specified");
                postMethod.addHeader("field3", "not specified");
                postMethod.addHeader("field4", "not specified");


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
    public void onBackPressed()
    {
        Intent i = new Intent(MGarage_PostAds.this,Dashboard.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
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