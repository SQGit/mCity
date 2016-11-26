package mcity.com.mcity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Admin on 27-09-2016.
 */
public class SplashScreen extends Activity {
    private static String TAG = SplashScreen.class.getName();
    private static long SLEEP_TIME = 5;	// Sleep for some time
    String str_check;
    DatabaseHandler db;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);	// Removes title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);	// Removes notification bar
        db = new DatabaseHandler(this);
        db.getWritableDatabase();
        setContentView(R.layout.splash_activity);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
        str_check = sharedPreferences.getString("login_status", "");
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
    }



    private class IntentLauncher extends Thread {
        @Override
        public void run() {
            try {
                // Sleeping
                Thread.sleep(SLEEP_TIME*1000);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }


            if(str_check.equals("true"))
            {

                Log.e("tag","111");
                Intent intent = new Intent(SplashScreen.this, Dashboard.class);
                SplashScreen.this.startActivity(intent);
                SplashScreen.this.finish();
            }
            else if(str_check.equals("false"))
            {
                Log.e("tag","222");

                Intent intent = new Intent(SplashScreen.this, Test_L.class);
                SplashScreen.this.startActivity(intent);
                SplashScreen.this.finish();

            }
            else
            {
                Log.e("tag","333");
                db.addContact(new PlaceDetails("Paranur Railway Station","12.729674", "79.985543",	"AutoStand, MahindraWorldCIty, Chengalpattu",	"8AM to 10PM pickups",	"8883070913","9788891120","9659108191","S.ARUL","N.SATHAM UZEN","V.LOGU"));
                db.addContact(new PlaceDetails("Canopy","12.731442", "80.000516",	"AutoStand, MahindraWorldCIty, Chengalpattu",	"8AM to 10PM pickups",	"9840357483","9940100332","9578616872","VINAYAKAM","MANI","SHANMUGAM"));
                db.addContact(new PlaceDetails("Capegemini","12.736314", "80.007350",	"AutoStand, MahindraWorldCIty, Chengalpattu",	"8AM to 10PM pickups",	"7094345321","9962183068","952404293","BALAJI","SEKAR","MAGESH"));
                db.addContact(new PlaceDetails("Aqualily","12.728183", "80.006341",	"AutoStand, MahindraWorldCIty, Chengalpattu",	"8AM to 10PM pickups",	"hidden","hidden","hidden","hidden","hidden","hidden"));
                db.addContact(new PlaceDetails("Zeropoint","12.742792", "80.0103423",	"AutoStand, MahindraWorldCIty, Chengalpattu",	"8AM to 10PM pickups",	"7094345321","9962183068","952404293","BALAJI","SEKAR","MAGESH"));
                Intent intent = new Intent(SplashScreen.this,   Test_L.class);
                SplashScreen.this.startActivity(intent);
                SplashScreen.this.finish();

            }
        }



    }
}

