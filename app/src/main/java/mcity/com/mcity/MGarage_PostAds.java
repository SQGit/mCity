package mcity.com.mcity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sloop.fonts.FontsManager;

/**
 * Created by Admin on 22-11-2016.
 */
public class MGarage_PostAds extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mgarage_postads);
        FontsManager.initFormAssets(this, "mont.ttf");
        FontsManager.changeFonts(this);

    }


    @Override
    public void onBackPressed() {
        Intent post = new Intent(MGarage_PostAds.this,Dashboard.class);
        startActivity(post);
        finish();
    }
}
