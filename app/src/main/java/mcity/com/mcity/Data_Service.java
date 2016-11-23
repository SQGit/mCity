package mcity.com.mcity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Salman on 6/21/2016.
 */
public class Data_Service {



 /*public static String URL= "http://104.197.80.225:3000/";
    public static String URL_API= "http://104.197.80.225:3000/api/";
    public static String URL_IMG="http://104.197.80.225:3000/";*/


   public static String URL= "http://104.197.80.225:3010/";
    public static String URL_API= "http://104.197.80.225:3010/api/";
    public static String URL_IMG="http://104.197.80.225:3010/";




    public static boolean isNetworkAvailable(Context c1) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c1.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
