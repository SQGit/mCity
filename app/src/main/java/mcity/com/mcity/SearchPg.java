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
	String URL = Data_Service.URL_API + "searchforroomnew";
	EditText location_et, min_rent_et, max_rent_et;
	AutoCompleteTextView get_location;
	String location, minRent, maxRent,spin_val;
	String str_city,str_resi,str_gender,str_minval,str_maxval,room,gender;
	String[] city ={"Sylvan County","Aqualily","Iris Court","Nova"};
	private RadioGroup radioResidentialGroup,radioGenderGroup;
	private RadioButton radioResidentalButton,radioGenderButton;
	int idroom, idgender;

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
		min_rent_et = (EditText) findViewById(R.id.min_rent_et);
		max_rent_et = (EditText) findViewById(R.id.max_rent_et);
		radioResidentialGroup = (RadioGroup) findViewById(R.id.radioResidentialGroup);
		radioGenderGroup = (RadioGroup) findViewById(R.id.radioGenderGroup);

		FontsManager.initFormAssets(this, "mont.ttf");
		FontsManager.changeFonts(this);
		tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");


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


				str_city = spinner.getSelectedItem().toString();



				idroom = radioResidentialGroup.indexOfChild(radioResidentalButton);
				idgender = radioGenderGroup.indexOfChild(radioGenderButton);


				if (idroom > -1) {
					room = radioResidentalButton.getText().toString();
					Log.e("tag","residential"+str_resi);
					if (room.equals("PG/Hostel")) {
						str_resi = "pg";
					} else {
						str_resi = "room";
					}

				}
				else
				{
					Log.e("tag","empty1");
				}


				if (idgender > -1) {
					str_gender = radioGenderButton.getText().toString();
					Log.e("tag","gender"+str_gender);

				}
				else
				{
					Log.e("tag","empty2");
				}


				str_minval = min_rent_et.getText().toString();
				Log.e("tag","11"+str_minval);
				str_maxval = max_rent_et.getText().toString();
				Log.e("tag","22"+str_maxval);



				//////////////////////////////VALIDATION FOE ALL FIELDS/////////////////////////////////////////////////
				Log.e("tag", "12345" + str_city + str_resi + str_gender + str_minval+str_maxval);




				if(!str_city.equals("Select Location"))
				{
					Log.e("tag","g1");
					if(!str_minval.equals(""))
					{
						Log.e("tag","g2");
						if(str_minval!="0")
						{
							Log.e("tag","g3");
							if((!str_maxval.equals(""))&& str_maxval!="0")
							{
								Log.e("tag","g4");
								Intent intent = new Intent(getApplicationContext(), SearchPGFilter.class);
								intent.putExtra("location", str_city);
								if(str_resi!=null)

									intent.putExtra("room_type", str_resi);
								if(str_gender!=null)

									intent.putExtra("gender_type", str_gender);
								intent.putExtra("minRent", str_minval);
								intent.putExtra("maxRent", str_maxval);

								startActivity(intent);
							}
							else
							{
								Log.e("tag","g7");
								Toast.makeText(SearchPg.this, "enter Max Rent", Toast.LENGTH_SHORT).show();
							}
						}
						else
						{
							Log.e("tag","g8");
							Toast.makeText(SearchPg.this, "Enter Value", Toast.LENGTH_SHORT).show();
						}

					}



					else if(!str_maxval.equals(""))
					{
						Log.e("tag","g10");
						if(str_maxval!="0")
						{
							Log.e("tag","g11");
							if((!str_minval.equals(""))&& str_minval!="0")
							{
								Log.e("tag","g12");
								Intent intent = new Intent(getApplicationContext(), SearchPGFilter.class);
								intent.putExtra("location", str_city);
								if(str_resi!=null)

									intent.putExtra("room_type", str_resi);
								if(str_gender!=null)

									intent.putExtra("gender_type", str_gender);
								intent.putExtra("minRent", str_minval);
								intent.putExtra("maxRent", str_maxval);

								startActivity(intent);
							}
							else
							{
								Log.e("tag","g15");
								Toast.makeText(SearchPg.this, "enter Select Max Rent", Toast.LENGTH_SHORT).show();
							}
						}
						else
						{
							Log.e("tag","g16");
							Toast.makeText(SearchPg.this, "Enter Value", Toast.LENGTH_SHORT).show();
						}

					}
					else
					{
						Log.e("tag","g17");
						Intent intent = new Intent(getApplicationContext(), SearchPGFilter.class);
						intent.putExtra("location", str_city);
						if(str_resi!=null)
						intent.putExtra("room_type", str_resi);
						if(str_gender!=null)
						intent.putExtra("gender_type", str_gender);
						startActivity(intent);
					}
				}
				else
				{
					Log.e("tag","g20");
					Toast.makeText(SearchPg.this, "Select Location", Toast.LENGTH_SHORT).show();
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
		Intent i = new Intent(SearchPg.this,MRental.class);
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