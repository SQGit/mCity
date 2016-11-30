package mcity.com.mcity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * Created by sqindia on 25-11-2016.
 */

public class Loader extends Activity {
    Button bt_ldr;
    ProgressBar mprog,cir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_loader);


        mprog = (ProgressBar) findViewById(R.id.loading_spinner);
       // cir  = (ProgressBar) findViewById(R.id.circular_progress_bar);

       /* ObjectAnimator anim = ObjectAnimator.ofInt(cir, "progress", 0, 100);
        anim.setDuration(15000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();*/

    }
}
