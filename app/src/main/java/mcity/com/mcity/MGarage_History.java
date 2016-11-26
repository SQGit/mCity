package mcity.com.mcity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.sloop.fonts.FontsManager;

/**
 * Created by Admin on 22-11-2016.
 */
public class MGarage_History  extends Activity {
    LinearLayout btn_postAd,btn_searchAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mgarage_history);
        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

        btn_postAd = (LinearLayout) findViewById(R.id.layout_postads);
        btn_searchAd = (LinearLayout) findViewById(R.id.layout_searchad);

        btn_searchAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post = new Intent(MGarage_History.this,Test_L.class);
                startActivity(post);
            }
        });

        btn_postAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post = new Intent(MGarage_History.this,Loader.class);
                startActivity(post);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent post = new Intent(MGarage_History.this,Dashboard.class);
        startActivity(post);
        finish();
    }
}
