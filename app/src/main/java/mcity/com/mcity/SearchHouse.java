package mcity.com.mcity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchHouse extends Activity implements AdapterView.OnItemSelectedListener{
    ImageView submit;
    String URL = Data_Service.URL_API + "searchforrent";

    EditText location_et, min_rent_et, max_rent_et;
    String root1,root2,root3,location, minRent, maxRent;
    String str_location,str_residential,str_bedroom,str_bedroom2,str_residential1,str_bedroom1,str_minval,str_maxval,str_furnished_type;
    AutoCompleteTextView select_city;
    String[] city ={"Sylvan County","Aqualily","Iris Court","Nova"};
    RadioGroup radioResidentialGroup1,radioResidentialGroup2,radioResidentialGroupcut,radioBedroomGroupbar1,radioBedroomGroupbar2,radioBedroomGroup,radioBedroomothers,radioFurnishedGroup;
    RadioButton radioBedroomButton,radioresidentalButton1,radioresidentalButton2,radioresidentalButtoncut,radiobar1,radiobar2,radioFurnishedButton;
    Spinner spinner;
    LinearLayout restrict1,restrict2,restrict3,withoutbar,withbar1,withbar2,others;
    String spin_val;
    int selectedId1,selectedIdcut,selectedId3,selectedId4,selectedId6,selectedId7,other_bed,resi_iris,bed_iris,resi_nova,bed_nova;
    List<String> categories;
    Typeface tf;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_rent);

        submit = (ImageView) findViewById(R.id.submit);
        min_rent_et = (EditText) findViewById(R.id.min_rent_et);
        max_rent_et = (EditText) findViewById(R.id.max_rent_et);
        spinner = (Spinner) findViewById(R.id.spinner1);
        restrict1 = (LinearLayout) findViewById(R.id.restrict1);
        restrict2 = (LinearLayout) findViewById(R.id.restrict2);
        restrict3 =(LinearLayout) findViewById(R.id.restrict3);
        radioBedroomGroupbar1 = (RadioGroup) findViewById(R.id.radioBedroomGroupbar1);
        radioBedroomGroupbar2 = (RadioGroup) findViewById(R.id.radioBedroomGroupbar2);
        radioResidentialGroupcut= (RadioGroup) findViewById(R.id.radioResidentialGroupcut);
        radioBedroomothers=(RadioGroup) findViewById(R.id.radioBedroomothers);
        withoutbar = (LinearLayout) findViewById(R.id.withoutbar);
        withbar1 = (LinearLayout) findViewById(R.id.withbar1);
        withbar2 = (LinearLayout) findViewById(R.id.withbar2);
        others=(LinearLayout) findViewById(R.id.others);
        radioResidentialGroup1 = (RadioGroup) findViewById(R.id.radioResidentialGroup1);
        radioResidentialGroup2 = (RadioGroup) findViewById(R.id.radioResidentialGroup2);


        radioBedroomGroup= (RadioGroup) findViewById(R.id.radioBedroomGroup) ;
        radioFurnishedGroup=(RadioGroup) findViewById(R.id.radioFurnishedGroup) ;

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");

        restrict1.setVisibility(View.VISIBLE);
        restrict2.setVisibility(View.GONE);
        restrict3.setVisibility(View.GONE);
        withoutbar.setVisibility(View.VISIBLE);
        withbar1.setVisibility(View.GONE);
        withbar2.setVisibility(View.GONE);
        others.setVisibility(View.GONE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, city);


        spinner.setOnItemSelectedListener(this);

        categories = new ArrayList<String>();
        categories.add("Select Location");
        categories.add("Aqualily");
        categories.add("Iris Court");
        categories.add("Nova");
        categories.add("Sylvan County");
        categories.add("Others");

        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);


        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setAdapter(dataAdapter);
        setSpinner1();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spin_val = spinner.getItemAtPosition(position).toString();
                Log.e("tag","!!!!!!"+spin_val);

                setLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (spin_val.equals("Others")) {
                    selectedId3 = radioResidentialGroup1.getCheckedRadioButtonId();
                    radioresidentalButton1 = (RadioButton) findViewById(selectedId3);
                    str_residential = radioresidentalButton1.getText().toString();


                    other_bed = radioBedroomothers.getCheckedRadioButtonId();
                    radioBedroomButton = (RadioButton) findViewById(other_bed);
                    str_bedroom = radioBedroomButton.getText().toString();
                    Log.e("tag", "zzz1" + str_bedroom);
                    Log.e("tag", "zzz1" + str_residential);


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
                    Log.e("tag", "zzz1" + str_residential);


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
                    selectedId1 = radioResidentialGroup1.getCheckedRadioButtonId();
                    radioresidentalButton1 = (RadioButton) findViewById(selectedId1);
                    str_residential = radioresidentalButton1.getText().toString();

                    radioBedroomButton = (RadioButton) findViewById(selectedId3);
                    str_bedroom = radioBedroomButton.getText().toString();
                    Log.e("tag", "zzz1" + str_residential);
                    Log.e("tag", "zzz1" + str_bedroom);

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
                    resi_nova = radioResidentialGroup2.getCheckedRadioButtonId();
                    radioresidentalButton2 = (RadioButton) findViewById(resi_nova);
                    str_residential = radioresidentalButton2.getText().toString();


                    bed_nova = radioBedroomGroupbar1.getCheckedRadioButtonId();
                    radioBedroomButton = (RadioButton) findViewById(bed_nova);
                    str_bedroom = radioBedroomButton.getText().toString();


                    Log.e("tag", "zzz3" + str_bedroom);
                    Log.e("tag", "zzz3" + str_residential);

                    if (str_bedroom.equals("1/1.5 BHK")) {
                        root2 = "1/1.5bhk";
                    } else if (str_bedroom.equals("2/2.5 BHK")) {
                        root2 = "2/2.5bhk";
                    } else {
                        root2 = "studio";
                    }


                    if (str_residential.equals("Apartment")) {
                        root1 = "Apartment";
                    } else
                        root1 = "Duplex";

                } else if (spin_val.equals("Iris Court")) {

                    selectedIdcut = radioResidentialGroupcut.getCheckedRadioButtonId();
                    radioresidentalButtoncut = (RadioButton) findViewById(selectedIdcut);
                    str_residential = radioresidentalButtoncut.getText().toString();


                    bed_iris = radioBedroomGroupbar2.getCheckedRadioButtonId();
                    radioBedroomButton = (RadioButton) findViewById(bed_iris);
                    str_bedroom = radioBedroomButton.getText().toString();


                    Log.e("tag", "zzz2" + str_bedroom);
                    Log.e("tag", "zzz2" + str_residential);

                    if (str_bedroom.equals("2/2.5 BHK")) {
                        root2 = "2/2.5bhk";
                    } else {
                        root2 = "3bhk";
                    }


                    if (str_residential.equals("Apartment")) {
                        root1 = "Apartment";
                    } else
                        root1 = "Duplex";
                }


                //radioBedroomButton = (RadioButton) findViewById(selectedId2);

                selectedId3 = radioFurnishedGroup.getCheckedRadioButtonId();
                radioFurnishedButton = (RadioButton) findViewById(selectedId3);


                //str_bedroom=radioBedroomButton.getText().toString();
                str_furnished_type = radioFurnishedButton.getText().toString();
                Log.e("tag", "@@@" + str_furnished_type);
                str_minval = min_rent_et.getText().toString();
                str_maxval = max_rent_et.getText().toString();


                if (str_furnished_type.equals("Fully Furnished")) {
                    root3 = "Furnished";
                } else if (str_furnished_type.equals("Semi Furnished")) {
                    root3 = "Semi-furnished";
                } else
                    root3 = "Unfurnished";


                Log.e("tag", "location" + spin_val);
                Log.e("tag", "residential" + root1);
                Log.e("tag", "bedroom" + root2);


                if (!spin_val.isEmpty() && !str_minval.isEmpty() && !str_maxval.isEmpty()) {


                    Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                    intent.putExtra("area", spin_val);


                    intent.putExtra("residential", root1);
                    intent.putExtra("minRent", str_minval);
                    intent.putExtra("maxRent", str_maxval);

                    intent.putExtra("bedroom", root2);
                    intent.putExtra("Furnishedtype", root3);


                    Log.e("tag", "location" + spin_val);
                    Log.e("tag", "residential" + root1);
                    Log.e("tag", "minRent" + str_minval);
                    Log.e("tag", "maxRent" + str_maxval);
                    Log.e("tag", "bedroom" + root2);
                    Log.e("tag", "Furnishedtype" + root3);

                    startActivity(intent);
                    finish();


                } else {

                    Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                    intent.putExtra("area", spin_val);


                    intent.putExtra("residential", root1);
                    intent.putExtra("minRent", "100");
                    intent.putExtra("maxRent", "100000");

                    intent.putExtra("bedroom", root2);
                    intent.putExtra("Furnishedtype", root3);


                    Log.e("tag", "location" + spin_val);
                    Log.e("tag", "residential" + root1);
                    Log.e("tag", "minRent" + str_minval);
                    Log.e("tag", "maxRent" + str_maxval);
                    Log.e("tag", "bedroom" + root2);
                    Log.e("tag", "Furnishedtype" + root3);

                    startActivity(intent);
                    finish();

                  //  Toast.makeText(getApplicationContext(),"Field Vacant..",Toast.LENGTH_LONG).show();

                }
            }
        });
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


    private void setLayout() {

        if(spin_val.equals("Others")) {
            restrict1.setVisibility(View.VISIBLE);
            restrict2.setVisibility(View.GONE);
            restrict3.setVisibility(View.GONE);
            withbar1.setVisibility(View.GONE);
            withbar2.setVisibility(View.GONE);
            withoutbar.setVisibility(View.GONE);
            others.setVisibility(View.VISIBLE);
        }


        else if (spin_val.equals("Aqualily")) {
            restrict1.setVisibility(View.VISIBLE);
            restrict2.setVisibility(View.GONE);
            restrict3.setVisibility(View.GONE);
            withbar1.setVisibility(View.GONE);
            withbar2.setVisibility(View.GONE);
            withoutbar.setVisibility(View.VISIBLE);
            others.setVisibility(View.GONE);
        } else {
            if (spin_val.equals("Sylvan County")) {
                restrict1.setVisibility(View.VISIBLE);
                restrict2.setVisibility(View.GONE);
                restrict3.setVisibility(View.GONE);
                withbar1.setVisibility(View.GONE);
                withbar2.setVisibility(View.GONE);
                withoutbar.setVisibility(View.VISIBLE);
                others.setVisibility(View.GONE);

            } else {
                if (spin_val.equals("Nova")) {
                    Log.e("tag","sds");
                    restrict1.setVisibility(View.GONE);
                    restrict2.setVisibility(View.VISIBLE);
                    restrict3.setVisibility(View.GONE);
                    withbar1.setVisibility(View.VISIBLE);
                    withbar2.setVisibility(View.GONE);
                    withoutbar.setVisibility(View.GONE);
                    others.setVisibility(View.GONE);


                } else {

                    restrict1.setVisibility(View.GONE);
                    restrict2.setVisibility(View.GONE);
                    restrict3.setVisibility(View.VISIBLE);
                    withbar1.setVisibility(View.GONE);
                    withbar2.setVisibility(View.VISIBLE);
                    withoutbar.setVisibility(View.GONE);
                    others.setVisibility(View.GONE);

                }
            }
        }
    }



    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(SearchHouse.this,Dashboard.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

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

}