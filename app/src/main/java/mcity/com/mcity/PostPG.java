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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

public class PostPG extends Activity implements AdapterView.OnItemSelectedListener{
	String URL = Data_Service.URL_API + "postforroom";
	ImageView img_submit;
	String str_uid, str_token, str_location, str_landmark, str_address, str_rent, str_description, str_residential, str_gender,room,str_spin_val,str_pho_enable;
	EditText  edt_lanmark_et, edt_address_et, edt_rentamount_et, edt_description_et;
	String[] city = {"Sylvan County", "Aqualily", "Iris Court", "Nova"};
	private RadioGroup radioResidentialGroup, radioGenderGroup;
	private RadioButton radioresidentalButton, radiogGnderButton;
	CheckBox chk_phone_enable;
	Spinner spinner;
	Typeface tf;
	List<String> categories;
	ProgressBar progressBar;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_pg);

		//select_city = (AutoCompleteTextView) findViewById(R.id.location_et);
		//location_et = (EditText) findViewById(R.id.location_et);
		edt_lanmark_et = (EditText) findViewById(R.id.landmark_et);
		edt_address_et = (EditText) findViewById(R.id.Address_et);
		//rentamount_et = (EditText) findViewById(R.id.rent_et);
		edt_description_et = (EditText) findViewById(R.id.description_et);
		img_submit = (ImageView) findViewById(R.id.submit);
		edt_rentamount_et = (EditText) findViewById(R.id.rent_et);
		spinner=(Spinner)findViewById(R.id.spinner);
		radioResidentialGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGenderGroup= (RadioGroup) findViewById(R.id.radioGroup2) ;
		progressBar=(ProgressBar)findViewById(R.id.progressBar);
		chk_phone_enable=(CheckBox)findViewById(R.id.phone_enable);

		FontsManager.initFormAssets(this, "mont.ttf");
		FontsManager.changeFonts(this);
		tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		str_token = sharedPreferences.getString("token", "");
		str_uid = sharedPreferences.getString("id", "");
		progressBar.setVisibility(View.GONE);
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
				str_spin_val = spinner.getItemAtPosition(position).toString();
				Log.e("tag", "123" + str_spin_val);

				setLocation();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});



		img_submit.setOnClickListener(new View.OnClickListener()

		{
			@Override
			public void onClick (View v){
				int selectedId1 = radioResidentialGroup.getCheckedRadioButtonId();
				int selectedId2 = radioGenderGroup.getCheckedRadioButtonId();
				radioresidentalButton = (RadioButton) findViewById(selectedId1);
				radiogGnderButton = (RadioButton) findViewById(selectedId2);


				str_location = spinner.getSelectedItem().toString();
				str_landmark = edt_lanmark_et.getText().toString();
				str_address = edt_address_et.getText().toString();
				str_residential = radioresidentalButton.getText().toString();
				str_gender = radiogGnderButton.getText().toString();
				str_rent = edt_rentamount_et.getText().toString();
				str_description = edt_description_et.getText().toString();


				if(str_gender.equals("I Have a PG/Hostel")) {
					room = "pg";
				}
				else
				{
					room = "room";
				}



				if (chk_phone_enable.isChecked()) {
					str_pho_enable = "enabled";
				} else {
					str_pho_enable = "Hidden Contact";
				}




			if (Util.Operations.isOnline(PostPG.this)) {
				if (!str_location.isEmpty() && !str_landmark.isEmpty() && !str_address.isEmpty() && !str_residential.isEmpty() && !str_gender.isEmpty() && !str_rent.isEmpty() && !str_description.isEmpty()) {

					new PostHouseAsync().execute();


				} else {
					Toast.makeText(getApplicationContext(), "Invalid Fields..", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
			}
			}
		});




	}

	private void setLocation() {


		if (str_spin_val.equals("Aqualily")) {
			edt_lanmark_et.setText("Opposite BMW Factory");
			edt_address_et.setText("Mahindra City");
		} else {
			if (str_spin_val.equals("Iris Court")) {
				edt_lanmark_et.setText("Close to Paranur Station");
				edt_address_et.setText("Mahindra City");
			} else {
				if (str_spin_val.equals("Nova")) {
					edt_lanmark_et.setText("Close to Paranur Station");
					edt_address_et.setText("Mahindra City");

				} else if (str_spin_val.equals("Sylvan County")){

					edt_lanmark_et.setText("Close to Canopy");
					edt_address_et.setText("Mahindra City");
				}
				else
				{
					edt_lanmark_et.setText("");
					edt_address_et.setText("");
				}
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
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	class PostHouseAsync extends AsyncTask<String, Void, String> {


		public PostHouseAsync() {
			String json = "", jsonStr = "";


		}

		protected void onPreExecute() {
			img_submit.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		protected String doInBackground(String... params) {

			String json = "", jsonStr = "";
			String id = "";
			try {
				Log.e("tag","id"+str_uid);
				Log.e("tag","token"+str_token);
				//location,landmark,address,roomtype,monthlyrent,gender,description
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("location", str_location);
				jsonObject.accumulate("landmark", str_landmark);
				jsonObject.accumulate("address", str_address);
				jsonObject.accumulate("roomtype",room );
				jsonObject.accumulate("monthlyrent", str_rent);
				jsonObject.accumulate("gender", str_gender);
				jsonObject.accumulate("description", str_description);
				jsonObject.accumulate("phone", str_pho_enable);


				Log.e("tag","1"+str_location);

				json = jsonObject.toString();

				return jsonStr = HttpUtils.makeRequest1(URL, json, str_uid, str_token);
			} catch (Exception e) {
				Log.d("InputStream", e.getLocalizedMessage());
			}
			return null;

		}

		@Override
		protected void onPostExecute(String jsonStr) {
			Log.e("tag", "<-----result---->" + jsonStr);
			img_submit.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			super.onPostExecute(jsonStr);
			try {
				JSONObject jo = new JSONObject(jsonStr);
				String status = jo.getString("status");
				String msg = jo.getString("message");
				Log.d("tag", "<-----Status----->" + status);
				if (status.equals("true")) {
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					edt_lanmark_et.setText("");
					edt_address_et.setText("");
					edt_description_et.setText("");
					edt_rentamount_et.setText("");



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
	public void onBackPressed()
	{
		Intent i = new Intent(PostPG.this,Dashboard.class);
		startActivity(i);
		finish();
	}
}