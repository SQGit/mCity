package mcity.com.mcity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sqindia on 23-11-2016.
 */

public class Test_L extends Activity {
    private ViewPager viewPager;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    ImageView iv_next;
    EditText et_email,et_otp;
    String str_email;
    TextView tv_otp,tv_register;
    int i=0,k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_l);
        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        iv_next = (ImageView) findViewById(R.id.submittv);
        tv_otp = (TextView) findViewById(R.id.text_Otp);
        tv_register = (TextView) findViewById(R.id.textView_Register);

        et_otp = (EditText) findViewById(R.id.pwd_et);
        layouts = new int[]{
                R.layout.test_login,
                R.layout.test_otp,};

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "Comming Soon !", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                Test_L.this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#000000")));
            }
        });

        if (i==0) {
            iv_next.setEnabled(false);
            tv_otp.setVisibility(View.GONE);
        }
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i==0) {
                    i=1;

                    viewPager.setCurrentItem(R.layout.test_otp);
                    tv_otp.setVisibility(View.VISIBLE);
                }
                else if (i==1){

                    i=0;
                }

            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

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

/*
                et_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        i=0;
                        str_email = et_email.getText().toString().trim();
                        if (!(str_email.isEmpty())) {
                           */
/* InputMethodManager inputManager = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.RESULT_HIDDEN);
                            //hideSoftKeyboard(Register.this);

                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);*//*

                            i=1;
                            if (i==1){
                                iv_next.setEnabled(true);
                                tv_otp.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                });
*/

                et_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if (actionId == EditorInfo.IME_ACTION_DONE){
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                            str_email = et_email.getText().toString().trim();

                            if (!(str_email.isEmpty())) {
                                InputMethodManager inputManager = (InputMethodManager)
                                        getSystemService(Context.INPUT_METHOD_SERVICE);

                                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                        InputMethodManager.RESULT_HIDDEN);
                                //hideSoftKeyboard(Register.this);

                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


                                    iv_next.setEnabled(true);


                            }
                        }
                        return true;
                    }
                });



            } else if (position == 1) {

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
/*

                FontsManager.initFormAssets(Test_L.this, "fonts/lato.ttf");       //initialization
                FontsManager.changeFonts(Test_L.this);
*/


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

}
