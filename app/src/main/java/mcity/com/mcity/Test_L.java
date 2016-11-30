package mcity.com.mcity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sqindia on 23-11-2016.
 */

public class Test_L extends AppCompatActivity implements TextWatcher {
    public static String URL_LOGIN = Data_Service.URL_API + "login";
    public static String URL_OTP = Data_Service.URL + "otpgenerate";
    String URL = Data_Service.URL_API + "logout";
    String LICENCE = Data_Service.URL_API + "licence";

    private ViewPager viewPager;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    ImageView iv_login,iv_otp;
    EditText et_email, et_otp;
    String str_email, str_otp, str_otppin;
    TextView tv_otp, tv_register;
    SharedPreferences s_pref;
    SharedPreferences.Editor editor;
    LinearLayout layout_register;
    ProgressBar progressBar;
    Dialog dialog2;
    int i = 0, k;
    String str_mobileno, str_password,str_emailValue,str_token, str_uid,str_get_email,str_check,str_signupstatus;

    static EditText et_otp1, et_otp2, et_otp3, et_otp4, et_otp5;
    private View view;

    private Test_L(View view) {
        this.view = view;
    }

    public Test_L() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_l);
        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

       // iv_next = (ImageView) findViewById(R.id.submittv);
        tv_otp = (TextView) findViewById(R.id.text_Otp);
        //progressBar=(ProgressBar)findViewById(R.id.spinner);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");
        str_signupstatus=sharedPreferences.getString("signup", "");




        layouts = new int[]{
                R.layout.test_login,
                R.layout.test_otp,};

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);



        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        dialog2 = new Dialog(Test_L.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        switch (view.getId()) {
            case R.id.editext_otp1:

                if (editable.length() == 0) {
                    et_otp1.requestFocus();
                } else if (editable.length() == 1) {
                    et_otp2.requestFocus();
                }

                break;
            case R.id.editext_otp2:

                if (editable.length() == 0) {
                    et_otp1.requestFocus();
                } else if (editable.length() == 1) {
                    et_otp3.requestFocus();
                }

                break;
            case R.id.editext_otp3:

                if (editable.length() == 0) {
                    et_otp2.requestFocus();
                } else if (editable.length() == 1) {
                    et_otp4.requestFocus();
                }
                break;
            case R.id.editext_otp4:

                if (editable.length() == 0) {
                    et_otp3.requestFocus();
                }
                break;

        }


    }


    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
           /*FontsManager.initFormAssets(getApplicationContext(), "fonts/lato.ttf");       //initialization
            FontsManager.changeFonts((Activity) getApplicationContext());*/

            if (position == 0) {

                et_email = (EditText) view.findViewById(R.id.editText_email);
                iv_login = (ImageView) view.findViewById(R.id.login_btn);

                layout_register = (LinearLayout) view.findViewById(R.id.layout_register);
                layout_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent_reg = new Intent(Test_L.this, Signup.class);
                        startActivity(intent_reg);
                        finish();
                    }
                });


                iv_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (et_email.getText().toString().isEmpty()) {
                            et_email.setError("Please Enter Mobile No");
                            et_email.requestFocus();
                           }
                        else if(et_email.getText().toString().length()<10)
                        {
                            et_email.setError("Enter valid phone number");
                            et_email.requestFocus();
                        }
                    }
                });
                et_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e("tag", "11111");
                      //  iv_next.setEnabled(true);

                        iv_login.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("tag", "454545");

                                if (et_email.getText().toString().length() > 0) {


                                    str_email = et_email.getText().toString().trim();
                                    if (Util.Operations.isOnline(Test_L.this)) {
                                    new Otp().execute();
                                    } else
                                    {
                                        Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                else
                                {
                                    TastyToast.makeText(getApplicationContext(), "Please Enter Mobile No", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                                }
                            }
                        });

                    }


                });

            } else if (position == 1) {
                et_otp1 = (EditText) view.findViewById(R.id.editext_otp1);
                et_otp2 = (EditText) view.findViewById(R.id.editext_otp2);
                et_otp3 = (EditText) view.findViewById(R.id.editext_otp3);
                et_otp4 = (EditText) view.findViewById(R.id.editext_otp4);
                iv_otp = (ImageView) view.findViewById(R.id.otp_btn);
                et_otp1.addTextChangedListener(new Test_L(et_otp1));
                et_otp2.addTextChangedListener(new Test_L(et_otp2));
                et_otp3.addTextChangedListener(new Test_L(et_otp3));
                et_otp4.addTextChangedListener(new Test_L(et_otp4));







                iv_otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                            str_email = et_email.getText().toString().trim();
                            if (et_otp1.getText().toString().isEmpty()) {
                                et_otp1.requestFocus();
                            } else {
                                if (et_otp2.getText().toString().isEmpty()) {
                                    et_otp2.requestFocus();
                                } else {
                                    if (et_otp3.getText().toString().isEmpty()) {
                                        et_otp3.requestFocus();
                                    } else {
                                        if (et_otp4.getText().toString().isEmpty()) {
                                            et_otp4.requestFocus();
                                        }  else {
                                            str_otppin = et_otp1.getText().toString() + et_otp2.getText().toString() + et_otp3.getText().toString() + et_otp4.getText().toString();
                                            Log.e("tag", "pin:" + str_otppin);


                                            if (Util.Operations.isOnline(Test_L.this)) {
                                                if (str_otppin.length() > 0) {
                                                    new AsyncLogin(str_email, str_otppin).execute();
                                                } else {
                                                    TastyToast.makeText(getApplicationContext(), "Please Enter OTP code", TastyToast.LENGTH_LONG, TastyToast.WARNING);

                                                }

                                            } else {
                                                Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_SHORT).show();
                                            }




                                        }

                                    }
                                }
                            }
                    }
                });


            }


            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {


            if (position == 0) {


            } else if (position == 1) {


            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }


    };

    private class Otp extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            //progressBar.setVisibility(View.VISIBLE);
            dialog2.show();
            iv_login.setVisibility(View.GONE);
            super.onPreExecute();
        }


        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("mobileno", str_email);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest(URL_OTP, json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return jsonStr;

        }


        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "<-----result---->" + jsonStr);

            super.onPostExecute(jsonStr);

            //edt_pwd.setEnabled(true);

            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");


                if (status.equals("true")) {

                   // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    viewPager.setCurrentItem(R.layout.test_otp);

                    dialog2.dismiss();
                    iv_login.setVisibility(View.VISIBLE);

                } else
                {
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

    class AsyncLogin extends AsyncTask<String, Void, String> {

        String email, password;

        public AsyncLogin(String email, String password) {
            String json = "", jsonStr = "";
            this.email = email;
            this.password = password;

        }

        protected void onPreExecute() {

            dialog2.show();
            iv_otp.setVisibility(View.GONE);
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("mobileno", str_email);
                jsonObject.accumulate("password", str_otppin);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest(URL_LOGIN, json);
            } catch (Exception e) {
            }
            return null;

        }

        @Override
        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);


            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                JSONArray ja = jo.getJSONArray("licence");


                if (ja.length() > 0) {

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject img_obj = ja.getJSONObject(i);

                        String pathnew = LICENCE + img_obj.getString("filename");
                        s_pref = PreferenceManager.getDefaultSharedPreferences(Test_L.this);
                        editor = s_pref.edit();
                        editor.putString("file_generate", pathnew);
                        editor.commit();


                    }
                }

                if (status.equals("true")) {


                    dialog2.dismiss();
                    iv_otp.setVisibility(View.VISIBLE);
                    String id = jo.getString("id");
                    String token = jo.getString("token");
                    int s = ja.length();


                    SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = s_pref.edit();
                    edit.putString("id", id);
                    edit.putString("token", token);
                    edit.putInt("licence_activation", s);
                    edit.putString("login_status", "true");
                    edit.commit();

                    //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), msg + "\n      Please generate OTP", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {

        showExit();
    }

    private void showExit() {

        LayoutInflater layoutInflater = LayoutInflater.from(Test_L.this);
        View promptView = layoutInflater.inflate(R.layout.exitlogin, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(Test_L.this).create();
        alertD.setCancelable(false);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.head1);
        final TextView head2 = (TextView) promptView.findViewById(R.id.head2);
        final ImageView no = (ImageView) promptView.findViewById(R.id.no);
        final ImageView yes = (ImageView) promptView.findViewById(R.id.yes);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "mont.ttf");
        head1.setTypeface(tf);
        head2.setTypeface(tf);


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Intent.ACTION_MAIN);
                i1.setAction(Intent.ACTION_MAIN);
                i1.addCategory(Intent.CATEGORY_HOME);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i1);
                finish();
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
    }



