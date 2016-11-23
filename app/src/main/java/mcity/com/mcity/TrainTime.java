package mcity.com.mcity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.sloop.fonts.FontsManager;

/**
 * Created by Admin on 03-10-2016.
 */
public class TrainTime extends Activity {


    LinearLayout back_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.train_time);
        back_arrow=(LinearLayout)findViewById(R.id.back_arrow);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);


       back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),TrainSearch.class);
                startActivity(i);
                finish();
            }
        });


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
    public void onBackPressed() {

        Intent i = new Intent(TrainTime.this,TrainSearch.class);
        startActivity(i);
        finish();
        // super.onBackPressed();
    }
}