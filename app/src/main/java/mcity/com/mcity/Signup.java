package mcity.com.mcity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

import static mcity.com.mcity.R.attr.height;

/**
 * Created by Ramya on 03-09-2016.
 */

public class Signup extends AppCompatActivity {
    Typeface tf;
    ImageView submit;
    public static String URL_REGISTER = Data_Service.URL+"signup";
    String username, email, mobileno, password,repassword;
    EditText uname_et,pwd_et,repwd_et,phone_et,email_et;
    LinearLayout registerlv;
    TextView reg;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);
        submit = (ImageView) findViewById(R.id.submittv);
        registerlv = (LinearLayout) findViewById(R.id.registerlv);
        reg = (TextView) findViewById(R.id.text);
        uname_et = (EditText) findViewById(R.id.uname_et);
        //pwd_et = (EditText) findViewById(R.id.pwd_et);
        //repwd_et = (EditText) findViewById(R.id.cpwd_et);
        phone_et = (EditText) findViewById(R.id.phone_et);
        email_et = (EditText) findViewById(R.id.email_et);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        String textsignup = "<font color=#000000>Have an Account?</font><font color=#E51C39> Click to login!</font>";
        reg.setText(Html.fromHtml(textsignup));
        progressBar.setVisibility(View.GONE);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = uname_et.getText().toString();
                mobileno = phone_et.getText().toString();
                email = email_et.getText().toString();

                if (!(username.isEmpty())) {
                    if (!(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                        if (!(mobileno.isEmpty() || mobileno.length() < 9)) {
                            new MyActivityAsync(username,  mobileno, email).execute();
                            /*Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();*/

                            TranslateAnimation anim_btn_b2t = new TranslateAnimation(0, 0, height, 0);
                            anim_btn_b2t.setDuration(500);


                            username = uname_et.getText().toString().trim();
                            mobileno = phone_et.getText().toString().trim();
                            email = email_et.getText().toString().trim();


                        } else {
                            phone_et.setError("Enter valid phone number");
                            phone_et.requestFocus();
                        }
                    } else {
                        email_et.setError("Enter a valid email address!");
                        email_et.requestFocus();
                    }
                } else {
                    uname_et.setError("Enter a Name!");
                    uname_et.requestFocus();
                }
            }
        });



        registerlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();

            }
        });
    }

    class MyActivityAsync extends AsyncTask<String, Void, String> {

        String username, mobileno, email;

        public MyActivityAsync(String username,String mobileno,String email) {
            this.username = username;
            this.mobileno = mobileno;
            this.email = email;

        }

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                Log.e("tag", "<-----bck---->");
                jsonObject.accumulate("username", username);
                jsonObject.accumulate("mobileno", mobileno);
                jsonObject.accumulate("email", email);


                json = jsonObject.toString();

                Log.e("tag", "<-----jsonnnn---->" + jsonObject);

                return jsonStr = HttpUtils.makeRequest(URL_REGISTER, json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "<-----result---->" + jsonStr);
            progressBar.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
            super.onPostExecute(jsonStr);
            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.d("tag", "<-----Status----->" + status);
                if (status.equals("true"))
                {
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                    SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = s_pref.edit();
                    edit.putString("signup", "true");
                    edit.putString("email", email);
                    edit.commit();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Log.e("tag","bbb");
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


}
