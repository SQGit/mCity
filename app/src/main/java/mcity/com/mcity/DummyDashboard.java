package mcity.com.mcity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sloop.fonts.FontsManager;

/**
 * Created by Admin on 21-11-2016.
 */
public class DummyDashboard extends Activity {
LinearLayout lin_mcoupon;
    ImageView imv_coupon;
    TextView txt_coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_dashboard);


        lin_mcoupon=(LinearLayout)findViewById(R.id.lin_mcoupon);
        imv_coupon=(ImageView)findViewById(R.id.imv_coupon);
        txt_coupon=(TextView)findViewById(R.id.txt_coupon);

        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        lin_mcoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_mcoupon.setBackgroundResource(R.drawable.dash_blue);
                txt_coupon.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.coupon_txt_color));
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),Dashboard.class);
        startActivity(i);
        finish();
    }
}
