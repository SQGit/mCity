package mcity.com.mcity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ramya on 03-09-2016.
 */

public class Login extends AppCompatActivity {
    public static String URL_REGISTER = Data_Service.URL_API+"login";
    public static String URL_OTP = Data_Service.URL+"otpgenerate";
    String URL = Data_Service.URL_API + "logout";

    Typeface tf;
    ImageView img_submit;
    LinearLayout lnr_register,lnr_hide_pwd;
    EditText edt_email, edt_pwd;
    String str_mobileno, str_password,str_emailValue,str_token, str_uid,str_get_email,str_check,str_signupstatus;
    TextView txt_reg;
    Button txt_otp;

    ProgressBar progressBar;
    SharedPreferences s_pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        s_pref = PreferenceManager.getDefaultSharedPreferences(Login.this);
        str_check = s_pref.getString("check", "");


        lnr_register=(LinearLayout) findViewById(R.id.registerlv);
        edt_email=(EditText) findViewById(R.id.email);
        edt_pwd=(EditText) findViewById(R.id.pwd_et);
        txt_reg=(TextView) findViewById(R.id.text);
        txt_otp=(Button) findViewById(R.id.otp);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        lnr_hide_pwd=(LinearLayout)findViewById(R.id.hide_pwd);
        img_submit=(ImageView) findViewById(R.id.submittv);
        progressBar.setVisibility(View.GONE);

        String textsignup = "<font color=#000000>Dont Have an Account?</font><font color=#E51C39> Register!</font>";
        txt_reg.setText(Html.fromHtml(textsignup));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");
        str_signupstatus=sharedPreferences.getString("signup", "");


        lnr_hide_pwd.setVisibility(View.GONE);

        if(str_signupstatus.equals("true"))
        {
            str_emailValue = sharedPreferences.getString("email", "");
            edt_email.setText(str_emailValue);
            edt_pwd.setEnabled(true);
            img_submit.setVisibility(View.VISIBLE);
            txt_otp.setVisibility(View.GONE);
            lnr_hide_pwd.setVisibility(View.VISIBLE);
            txt_otp.setFocusableInTouchMode(true);
            txt_otp.setFocusable(true);
            txt_otp.requestFocus();
            SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor edit = s_pref.edit();
            edit.putString("signup", "false");
            edit.commit();

        }


        img_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_get_email = edt_email.getText().toString();
                str_password = edt_pwd.getText().toString();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("check","login_success");
                editor.putString("mobileno",str_mobileno);
                editor.commit();

                if (Util.Operations.isOnline(Login.this)) {

                    if (!str_get_email.isEmpty() && !str_password.isEmpty()) {

                            new AsyncLogin(str_get_email, str_password).execute();

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Fields..", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        });


        lnr_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
                finish();
            }
        });

        txt_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_get_email = edt_email.getText().toString();
                if(edt_email.length()>0)
                {
                    new Otp().execute();
                }
                else
                {
                   Toast.makeText(Login.this,"Invalid Email",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    class AsyncLogin extends AsyncTask<String, Void, String> {

        String  email, password;

        public AsyncLogin(String email, String password) {
            String json = "", jsonStr = "";
            this.email = email;
            this.password = password;

        }

        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
            img_submit.setVisibility(View.GONE);

            super.onPreExecute();

        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email", email);
                jsonObject.accumulate("password", password);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest(URL_REGISTER, json);
            } catch (Exception e) {
            }
            return null;

        }

        @Override
        protected void onPostExecute(String jsonStr) {
            progressBar.setVisibility(View.GONE);
            img_submit.setVisibility(View.VISIBLE);
            super.onPostExecute(jsonStr);
            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                JSONArray ja=jo.getJSONArray("licence");


                    if(ja.length()>0){

                        for(int i =0;i<ja.length();i++){

                            JSONObject img_obj =ja.getJSONObject(i);

                            String pathnew = "http://104.197.7.143:3000/licence/" + img_obj.getString("filename");
                            s_pref = PreferenceManager.getDefaultSharedPreferences(Login.this);
                            editor = s_pref.edit();
                            editor.putString("file_generate", pathnew);
                            editor.commit();


                        }
                    }

                if (status.equals("true"))
                {

                    String id=jo.getString("id");
                    String token=jo.getString("token");
                    int s=ja.length();


                    SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = s_pref.edit();
                    edit.putString("id", id);
                    edit.putString("token", token);
                    edit.putInt("licence_activation",s);
                    edit.putString("login_status","true");
                    edit.commit();

                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),msg+"\n      Please generate OTP",Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }





    private class Otp extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
            img_submit.setVisibility(View.GONE);
            super.onPreExecute();

        }


        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email", str_get_email);
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
            progressBar.setVisibility(View.GONE);
            img_submit.setVisibility(View.VISIBLE);
            super.onPostExecute(jsonStr);

            edt_pwd.setEnabled(true);

            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");


                if (status.equals("true"))
                {

                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    txt_otp.setVisibility(View.GONE);
                    lnr_hide_pwd.setVisibility(View.VISIBLE);

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


    @Override
    public void onBackPressed() {

        showExit();
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


    private void showExit() {

        LayoutInflater layoutInflater = LayoutInflater.from(Login.this);
        View promptView = layoutInflater.inflate(R.layout.exitlogin, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(Login.this).create();
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
