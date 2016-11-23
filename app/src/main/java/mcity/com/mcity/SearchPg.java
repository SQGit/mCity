package mcity.com.mcity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sloop.fonts.FontsManager;

import java.util.ArrayList;
import java.util.List;

public class SearchPg extends Activity implements AdapterView.OnItemSelectedListener{
	ImageView submit;
	String URL = Data_Service.URL_API + "searchforroom";
	EditText location_et, min_rent_et, max_rent_et;
	AutoCompleteTextView get_location;
	String location, minRent, maxRent,spin_val;
	String str_city,str_resi,str_gender,str_minval,str_maxval,room;
	String[] city ={"Sylvan County","Aqualily","Iris Court","Nova"};
	private RadioGroup radioResidentialGroup,radioGenderGroup;
	private RadioButton radioResidentalButton,radioGenderButton;

	Spinner spinner;
	Typeface tf;
	List<String> categories;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_pg);


		submit = (ImageView) findViewById(R.id.submit);
		spinner = (Spinner) findViewById(R.id.spinner);
		//location_et = (EditText) findViewById(R.id.location_et);
		min_rent_et = (EditText) findViewById(R.id.min_rent_et);
		max_rent_et = (EditText) findViewById(R.id.max_rent_et);
		//get_location = (AutoCompleteTextView) findViewById(R.id.get_location);
		radioResidentialGroup = (RadioGroup) findViewById(R.id.radioResidentialGroup);
		radioGenderGroup = (RadioGroup) findViewById(R.id.radioGenderGroup);

		FontsManager.initFormAssets(this, "mont.ttf");
		FontsManager.changeFonts(this);
		tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");


		spinner.setOnItemSelectedListener(this);

		// Spinner Drop down elements
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
				Log.e("tag", "123" + spin_val);


			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});


		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int selectedId1 = radioResidentialGroup.getCheckedRadioButtonId();
				int selectedId2 = radioGenderGroup.getCheckedRadioButtonId();

				radioResidentalButton = (RadioButton) findViewById(selectedId1);
				radioGenderButton = (RadioButton) findViewById(selectedId2);


				//new SearchHouseAsync().execute();
				str_city = spinner.getSelectedItem().toString();
				Log.e("tag", "1" + str_city);
				str_resi = radioResidentalButton.getText().toString();

				Log.e("tag", "2" + str_resi);
				str_gender = radioGenderButton.getText().toString();
				Log.e("tag", "2" + str_gender);
				str_minval = min_rent_et.getText().toString();
				Log.e("tag", "4" + str_minval);
				str_maxval = max_rent_et.getText().toString();
				Log.e("tag", "5" + str_maxval);
				if (str_gender.equals("PG/Hostel")) {
					room = "pg";
				} else {
					room = "room";
				}

				if (!str_city.isEmpty() && !str_resi.isEmpty() && !str_gender.isEmpty() && !str_minval.isEmpty() && !str_maxval.isEmpty()) {

					Intent intent = new Intent(getApplicationContext(), SearchPGFilter.class);
					intent.putExtra("location", str_city);
					intent.putExtra("room_type", room);
					intent.putExtra("gender_type", str_gender);
					intent.putExtra("minRent", str_minval);
					intent.putExtra("maxRent", str_maxval);
					startActivity(intent);

				} else {

					Intent intent = new Intent(getApplicationContext(), SearchPGFilter.class);
					intent.putExtra("location", str_city);
					intent.putExtra("room_type", room);
					intent.putExtra("gender_type", str_gender);
					intent.putExtra("minRent", "100");
					intent.putExtra("maxRent", "200000");
					startActivity(intent);

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


	@Override
	public void onBackPressed()
	{
		Intent i = new Intent(SearchPg.this,Dashboard.class);
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

}