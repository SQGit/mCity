package mcity.com.mcity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 27-09-2016.
 */
public class SplashScreen extends Activity {
    public static String URL_VERSION = Data_Service.URL + "checkversion";
    private static String TAG = SplashScreen.class.getName();
    private static long SLEEP_TIME = 5;	// Sleep for some time
    String str_check,version,str_version;
    DatabaseHandler db;
    SharedPreferences s_pref;
    SharedPreferences.Editor edit;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);	// Removes title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);	// Removes notification bar
        db = new DatabaseHandler(this);
        db.getWritableDatabase();
        setContentView(R.layout.splash_activity);


        version = BuildConfig.VERSION_NAME;
        Log.e("tag","gggg"+version);

        if (Util.Operations.isOnline(SplashScreen.this)) {

                new CheckVersion(version).execute();

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_SHORT).show();
        }


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
        str_check = sharedPreferences.getString("login_status", "");
        str_version= sharedPreferences.getString("version_status","");

        IntentLauncher launcher = new IntentLauncher();
        launcher.start();

    }

    private class IntentLauncher extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(SLEEP_TIME * 1000);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

                if (str_check.equals("true")) {
                    Log.e("tag", "111");
                    Intent intent = new Intent(SplashScreen.this, Dashboard.class);
                    SplashScreen.this.startActivity(intent);
                    SplashScreen.this.finish();
                } else if (str_check.equals("false")) {
                    Log.e("tag", "222");
                    Intent intent = new Intent(SplashScreen.this, Test_L.class);
                    SplashScreen.this.startActivity(intent);
                    SplashScreen.this.finish();

                } else {
                    Log.e("tag", "333");
                    db.addContact(new PlaceDetails("Paranur Railway Station", "12.729674", "79.985543", "AutoStand, MahindraWorldCity, Chengalpattu.", "8AM to 10PM pickups", "8883070913", "9788891120", "9659108191", "S.ARUL", "N.SATHAM UZEN", "V.LOGU"));
                    db.addContact(new PlaceDetails("Canopy", "12.731442", "80.000516", "AutoStand, MahindraWorldCity, Chengalpattu.", "8AM to 10PM pickups", "9840357483", "9940100332", "9578616872", "VINAYAKAM", "MANI", "SHANMUGAM"));
                    db.addContact(new PlaceDetails("Capegemini", "12.736314", "80.007350", "AutoStand, MahindraWorldCity, Chengalpattu.", "8AM to 10PM pickups", "7094345321", "9962183068", "952404293", "BALAJI", "SEKAR", "MAGESH"));
                    db.addContact(new PlaceDetails("Aqualily", "12.728183", "80.006341", "AutoStand, MahindraWorldCity, Chengalpattu.", "8AM to 10PM pickups", "hidden", "hidden", "hidden", "hidden", "hidden", "hidden"));
                    db.addContact(new PlaceDetails("Zeropoint", "12.742677", "72.992280", "AutoStand, MahindraWorldCity, Chengalpattu.", "8AM to 10PM pickups", "7094345321", "9962183068", "952404293", "BALAJI", "SEKAR", "MAGESH"));
                    Intent intent = new Intent(SplashScreen.this, Test_L.class);
                    SplashScreen.this.startActivity(intent);
                    SplashScreen.this.finish();
                }
            }





        }

        private class CheckVersion extends AsyncTask<String, String, String> {
            String version;

            public CheckVersion(String version) {

                this.version = version;
            }

            protected void onPreExecute() {

                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {
                String json = "", jsonStr = "";
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("version", version);

                    json = jsonObject.toString();

                    return jsonStr = HttpUtils.makeRequest(URL_VERSION, json);
                } catch (Exception e) {
                }
                return null;

            }


            @Override
            protected void onPostExecute(String jsonStr) {
                Log.e("tag", "<-----result---->" + jsonStr);
                super.onPostExecute(jsonStr);

                try {
                    JSONObject jo = new JSONObject(jsonStr);
                    String status = jo.getString("status");

                    s_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    edit = s_pref.edit();
                    edit.putString("version_status", status);
                    edit.commit();

                    if (status.equals("true")) {

                        //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();


                    } else {
                        //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


    }



