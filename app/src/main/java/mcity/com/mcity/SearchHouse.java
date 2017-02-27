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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchHouse extends Activity implements AdapterView.OnItemSelectedListener {
    ImageView submit;
    String URL = Data_Service.URL_API + "searchforrentnew";
    String SHOW_IMAGE = Data_Service.URL_IMG + "rent/";
    String ALL_RENT = Data_Service.URL_API + "allrent";
    String str_uid, str_token;
    EditText location_et, min_rent_et, max_rent_et;
    String root1, root2, root3, location, minRent, maxRent;
    String str_location, str_residential, str_bedroom, str_bedroom2, str_residential1, str_bedroom1, str_minval, str_maxval, str_furnished_type;
    AutoCompleteTextView select_city;
    String[] city = {"Sylvan County", "Aqualily", "Iris Court", "Nova"};
    RadioGroup radioResidentialGroup1, radioResidentialGroup2, radioResidentialGroupcut, radioBedroomGroupbar1, radioBedroomGroupbar2, radioBedroomGroup, radioBedroomothers, radioFurnishedGroup;
    RadioButton radioBedroomButton, radioresidentalButton1, radioresidentalButton2, radioresidentalButtoncut, radiobar1, radiobar2, radioFurnishedButton;
    Spinner spinner;
    LinearLayout restrict1, restrict2, restrict3, withoutbar, withbar1, withbar2, others;
    String spin_val;
    int selectedId1, selectedIdcut, selectedId3, selectedId4, selectedId6, selectedId7, other_bed, bed_iris, resi_nova, bed_nova;
    List<String> categories;
    Typeface tf;
    int idx, idroom, idxfurnished;
    HashMap<String, String> map;
    ArrayList<HashMap<String, String>> contactList1;
    HouseAdapter houseAdapter;
    ListView listView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_rent);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");
        contactList1=new ArrayList<>();

        submit = (ImageView) findViewById(R.id.submit);
        min_rent_et = (EditText) findViewById(R.id.min_rent_et);
        max_rent_et = (EditText) findViewById(R.id.max_rent_et);
        spinner = (Spinner) findViewById(R.id.spinner1);
        restrict1 = (LinearLayout) findViewById(R.id.restrict1);
        restrict2 = (LinearLayout) findViewById(R.id.restrict2);
        restrict3 = (LinearLayout) findViewById(R.id.restrict3);
        radioBedroomGroupbar1 = (RadioGroup) findViewById(R.id.radioBedroomGroupbar1);
        radioBedroomGroupbar2 = (RadioGroup) findViewById(R.id.radioBedroomGroupbar2);
        radioResidentialGroupcut = (RadioGroup) findViewById(R.id.radioResidentialGroupcut);
        radioBedroomothers = (RadioGroup) findViewById(R.id.radioBedroomothers);
        withoutbar = (LinearLayout) findViewById(R.id.withoutbar);
        withbar1 = (LinearLayout) findViewById(R.id.withbar1);
        withbar2 = (LinearLayout) findViewById(R.id.withbar2);

        others = (LinearLayout) findViewById(R.id.others);
        radioResidentialGroup1 = (RadioGroup) findViewById(R.id.radioResidentialGroup1);
        radioResidentialGroup2 = (RadioGroup) findViewById(R.id.radioResidentialGroup2);
        radioBedroomGroup = (RadioGroup) findViewById(R.id.radioBedroomGroup);
        radioFurnishedGroup = (RadioGroup) findViewById(R.id.radioFurnishedGroup);



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
                (this, android.R.layout.select_dialog_item, city);


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
                Log.e("tag", "!!!!!!" + spin_val);

                setLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "456"+spin_val);
            ///////////////////Aqualily//////////////////////////////
                if (!spin_val.matches("Select Location")) {
                    Log.e("tag", "11110000");
                    if(spin_val.equals("Aqualily"))
                    {
                        selectedId3 = radioBedroomGroup.getCheckedRadioButtonId();
                        selectedId1 = radioResidentialGroup1.getCheckedRadioButtonId();
                        radioresidentalButton1 = (RadioButton) findViewById(selectedId1);
                        radioBedroomButton = (RadioButton) findViewById(selectedId3);


                        idx = radioResidentialGroup1.indexOfChild(radioresidentalButton1);
                        idroom = radioBedroomGroup.indexOfChild(radioBedroomButton);

                        if (idx > -1) {

                            str_residential = radioresidentalButton1.getText().toString();
                            if (str_residential.equals("Apartment")) {
                                root1 = "Apartment";
                            } else if (str_residential.equals("Villa")) {
                                root1 = "Villa";
                            } else {
                                root1 = "Duplex";
                            }

                        }else
                        {

                        }


                        if (idroom > -1) {
                            Log.e("tag","2"+idroom);
                            Log.e("tag","2"+str_bedroom);
                            str_bedroom = radioBedroomButton.getText().toString();
                            Log.e("tag", "str_bedroom" + str_bedroom);
                            if (str_bedroom.equals("2 BHK")) {
                                root2 = "2bhk";
                            } else if (str_bedroom.equals("3 BHK")) {
                                root2 = "3bhk";
                            } else {
                                root2 = "4bhk";
                            }

                        } else {
                            Log.e("tag", "dddddd" + idroom);
                        }



                    }
                    else
                    {

                    }






                    ///////////////////IrishCourt//////////////////////////////


                    if (spin_val.equals("Iris Court")) {

                        selectedIdcut = radioResidentialGroupcut.getCheckedRadioButtonId();
                        radioresidentalButtoncut = (RadioButton) findViewById(selectedIdcut);
                        bed_iris = radioBedroomGroupbar2.getCheckedRadioButtonId();
                        radioBedroomButton = (RadioButton) findViewById(bed_iris);
                        idx = radioResidentialGroupcut.indexOfChild(radioresidentalButtoncut);
                        idroom = radioBedroomGroupbar2.indexOfChild(radioBedroomButton);

                         if (idx > -1) {
                        str_residential = radioresidentalButtoncut.getText().toString();
                        if (str_residential.equals("Apartment")) {
                            root1 = "Apartment";
                        } else
                            root1 = "Duplex";


                         } else {
                        Log.e("tag", "33333" + str_residential);

                    }


                    if (idroom > -1) {
                    str_bedroom = radioBedroomButton.getText().toString();
                    Log.e("tag", "str_bedroom" + str_bedroom);

                    if (str_bedroom.equals("2/2.5 BHK")) {
                        root2 = "2/2.5bhk";
                    } else {
                        root2 = "3bhk";
                    }

                    } else {
                    Log.e("tag", "dddddd" + idroom);

                }
            }






                    ////////////////////////Nova///////////////////////////////


                    if (spin_val.equals("Nova")) {

                        resi_nova = radioResidentialGroup2.getCheckedRadioButtonId();
                        radioresidentalButton2 = (RadioButton) findViewById(resi_nova);
                        // str_residential = radioresidentalButton2.getText().toString();

                        bed_nova = radioBedroomGroupbar1.getCheckedRadioButtonId();
                        radioBedroomButton = (RadioButton) findViewById(bed_nova);
                        //  str_bedroom = radioBedroomButton.getText().toString();


                        idx = radioResidentialGroup2.indexOfChild(radioresidentalButton2);
                        idroom = radioBedroomGroupbar1.indexOfChild(radioBedroomButton);

                        Log.e("tag", "zzz1" + idroom);
                        if (idx > -1) {
                        str_residential = radioresidentalButton2.getText().toString();
                        Log.e("tag", "111111111" + str_residential);
                        if (str_residential.equals("Apartment")) {
                            root1 = "Apartment";
                        } else
                            root1 = "Duplex";


                       } else {
                        Log.e("tag", "33333" + str_residential);

                    }


                   if (idroom > -1) {
                    str_bedroom = radioBedroomButton.getText().toString();
                    Log.e("tag", "str_bedroom" + str_bedroom);
                    if (str_bedroom.equals("1/1.5 BHK")) {
                        root2 = "1/1.5bhk";
                    } else if (str_bedroom.equals("2/2.5 BHK")) {
                        root2 = "2/2.5bhk";
                    } else {
                        root2 = "studio";
                    }
                    } else {
                    Log.e("tag", "dddddd" + idroom);

                }

            }







                    ////////////////////////////Sylvan///////////////

                    if (spin_val.equals("Sylvan County")) {


                        selectedId3 = radioBedroomGroup.getCheckedRadioButtonId();
                        selectedId1 = radioResidentialGroup1.getCheckedRadioButtonId();
                        radioresidentalButton1 = (RadioButton) findViewById(selectedId1);

                        radioBedroomButton = (RadioButton) findViewById(selectedId3);


                        idx = radioResidentialGroup1.indexOfChild(radioresidentalButton1);
                        idroom = radioBedroomGroup.indexOfChild(radioBedroomButton);

                        Log.e("tag", "zzz1" + idroom);
                        if (idx > -1) {
                        str_residential = radioresidentalButton1.getText().toString();
                        Log.e("tag", "111111111" + str_residential);
                        if (str_residential.equals("Apartment")) {
                            root1 = "Apartment";
                        } else if (str_residential.equals("Villa")) {
                            root1 = "Villa";
                        } else {
                            root1 = "Duplex";
                        }


                         } else {
                        Log.e("tag", "33333" + str_residential);

                    }

                    if (idroom > -1) {
                    str_bedroom = radioBedroomButton.getText().toString();
                    Log.e("tag", "str_bedroom" + str_bedroom);
                    if (str_bedroom.equals("2 BHK")) {
                        root2 = "2bhk";
                    } else if (str_bedroom.equals("3 BHK")) {
                        root2 = "3bhk";
                    } else {
                        root2 = "4bhk";
                    }
                     } else {
                    Log.e("tag", "dddddd" + idroom);

                }

            }

            ////////////////////////////////OTHERS//////////////////////////////////

            if (spin_val.equals("Others")) {

                selectedId3 = radioResidentialGroup1.getCheckedRadioButtonId();
                radioresidentalButton1 = (RadioButton) findViewById(selectedId3);
                other_bed = radioBedroomothers.getCheckedRadioButtonId();
                radioBedroomButton = (RadioButton) findViewById(other_bed);

                idx = radioResidentialGroup1.indexOfChild(radioresidentalButton1);
                idroom = radioBedroomothers.indexOfChild(radioBedroomButton);

                Log.e("tag", "zzz1" + idroom);
                if (idx > -1) {
                    str_residential = radioresidentalButton1.getText().toString();
                    Log.e("tag", "111111111" + str_residential);
                    if (str_residential.equals("Apartment")) {
                        root1 = "Apartment";
                    } else if (str_residential.equals("Villa")) {
                        root1 = "Villa";
                    } else {
                        root1 = "Duplex";
                    }


                } else {
                    Log.e("tag", "33333" + str_residential);

                }


                if (idroom > -1) {
                    str_bedroom = radioBedroomButton.getText().toString();
                    Log.e("tag", "str_bedroom" + str_bedroom);
                    if (str_bedroom.equals("2 BHK")) {
                        root2 = "2bhk";
                    } else if (str_bedroom.equals("3 BHK")) {
                        root2 = "3bhk";
                    } else if (str_bedroom.equals("4 BHK")) {
                        root2 = "4bhk";
                    } else {
                        root2 = "1bhk";
                    }
                } else {
                    Log.e("tag", "dddddd" + idroom);

                }
            }


                    ///////////////////////////////FURNISHED TYPE///////////////////////////////////////////

                    selectedId3 = radioFurnishedGroup.getCheckedRadioButtonId();
                    radioFurnishedButton = (RadioButton) findViewById(selectedId3);

                    idxfurnished = radioFurnishedGroup.indexOfChild(radioFurnishedButton);
                    Log.e("tag", "hhhhhhhhhhhh" + idxfurnished);


                    if (idxfurnished > -1) {
                    str_furnished_type = radioFurnishedButton.getText().toString();

                    if (str_furnished_type.equals("Fully Furnished")) {
                        root3 = "Furnished";
                    } else if (str_furnished_type.equals("Semi Furnished")) {
                        root3 = "Semi-furnished";
                        } else
                        root3 = "Unfurnished";

                    }



                    //////////////////////////////VALIDATION FOE ALL FIELDS/////////////////////////////////////////////////
                    Log.e("tag", "12345" + spin_val + root1 + root2 + root3);


                    if(root2==null&&root3==null&root1==null)
                    {
                        Log.e("tag","C1");
                        Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                        intent.putExtra("area", spin_val);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        if(spin_val.equals("Select Location")&&root3.equals(null)&root1.equals(null))
                        {
                            Log.e("tag","C2");
                            Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                            intent.putExtra("bedroom", root2);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            if(spin_val.equals("Select Location")&&root2.equals(null)&root1.equals(null))
                            {
                                Log.e("tag","C3");
                                Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                                intent.putExtra("Furnishedtype", root3);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                if(spin_val.equals("Select Location")&&root2.equals(null)&root3.equals(null))
                                {
                                    Log.e("tag","C4");
                                    Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                                    intent.putExtra("residential", root1);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    if(root3==null&&root1==null)
                                    {
                                        Log.e("tag","C5");
                                        Log.e("tag","check"+root2);
                                        Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                                        intent.putExtra("area", spin_val);
                                        intent.putExtra("bedroom", root2);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        if(spin_val.equals("Select Location")&&root2==null)
                                        {
                                            Log.e("tag","C6");
                                            Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                                            intent.putExtra("Furnishedtype", root3);
                                            intent.putExtra("residential", root1);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                        {
                                            if(root2==null&&root1==null)
                                            {
                                                Log.e("tag","C7");
                                                Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                                                intent.putExtra("area", spin_val);
                                                intent.putExtra("Furnishedtype", root3);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else
                                            {
                                                if(spin_val.equals("Select Location")&&root3==null)
                                                {
                                                    Log.e("tag","C8");
                                                    Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                                                    intent.putExtra("bedroom", root2);
                                                    intent.putExtra("residential", root1);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else
                                                {
                                                    if(root2==null&&root3==null)
                                                    {
                                                        Log.e("tag","C9");
                                                        Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                                                        intent.putExtra("area", spin_val);
                                                        intent.putExtra("residential", root1);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                    else
                                                    {
                                                        if(spin_val.equals("Select Location")&&root1==null)
                                                        {
                                                            Log.e("tag","C10");
                                                            Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                                                            intent.putExtra("bedroom", root2);
                                                            intent.putExtra("Furnishedtype", root3);

                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                        else
                                                        {
                                                            if(root1==null)
                                                            {
                                                                Log.e("tag","C11");
                                                                Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                                                                intent.putExtra("area", spin_val);
                                                                intent.putExtra("bedroom", root2);
                                                                intent.putExtra("Furnishedtype", root3);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                            else
                                                            {
                                                                if(root2==null)
                                                                {
                                                                    Log.e("tag","C12");
                                                                    Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                                                                    intent.putExtra("area", spin_val);
                                                                    intent.putExtra("residential", root1);
                                                                    intent.putExtra("Furnishedtype", root3);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                                else
                                                                {
                                                                    if(spin_val.equals("Select Location"))
                                                                    {
                                                                        Log.e("tag","C13");
                                                                        Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                                                                        intent.putExtra("bedroom", root2);
                                                                        intent.putExtra("residential", root1);
                                                                        intent.putExtra("Furnishedtype", root3);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                    else
                                                                    {
                                                                        if(root3==null)
                                                                        {
                                                                            Log.e("tag","C15");
                                                                            Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                                                                            intent.putExtra("area", spin_val);
                                                                            intent.putExtra("residential", root1);
                                                                            intent.putExtra("Furnishedtype", root3);
                                                                            startActivity(intent);
                                                                            finish();
                                                                        }
                                                                        else
                                                                        {
                                                                            if(spin_val.equals("Select Location")&&root1==null&&root2==null&&root3==null)
                                                                            {
                                                                                Log.e("tag","C16");
                                                                                Toast.makeText(getApplicationContext(), "Please Select Anyone", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                            else
                                                                            {
                                                                                Intent intent = new Intent(getApplicationContext(), SearchHouseFilter.class);
                                                                                intent.putExtra("area", spin_val);
                                                                                intent.putExtra("residential", root1);
                                                                                intent.putExtra("Furnishedtype", root3);
                                                                                intent.putExtra("bedroom", root2);
                                                                                startActivity(intent);
                                                                                finish();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


        }
                    else {
                        Toast.makeText(getApplicationContext(), "Please Select Any Location", Toast.LENGTH_LONG).show();
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

        if (spin_val.equals("Others")) {
            restrict1.setVisibility(View.VISIBLE);
            restrict2.setVisibility(View.GONE);
            restrict3.setVisibility(View.GONE);
            withbar1.setVisibility(View.GONE);
            withbar2.setVisibility(View.GONE);
            withoutbar.setVisibility(View.GONE);
            others.setVisibility(View.VISIBLE);
        } else if (spin_val.equals("Aqualily")) {
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
                    Log.e("tag", "sds");
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
    public void onBackPressed() {
        Intent i = new Intent(SearchHouse.this, Dashboard.class);
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
