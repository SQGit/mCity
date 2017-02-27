package mcity.com.mcity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 03-11-2016.
 */
public class OneFragment extends Fragment {
    String SHOP_URL = Data_Service.URL_API + "getrestaurantsnew";
    TextView tv, tv1, tv2;
    ImageView call;
    String str_token, str_uid,str_shop_path;
    ShopAdapter shopadapter;
    ListView shoplist;
    ArrayList<HashMap<String, String>> shoparraylist;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    ProgressBar progressBar;
    Dialog dialog2;
    HashMap<String, String> map;

    static String shopname = "shopname";
    static String address = "address";
    static String openingtime = "openingtime";
    static String _id="_id";
    static String description = "description";
    static String mobileno = "mobileno";


    public OneFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one, container, false);
        FontsManager.initFormAssets(getActivity(), "mont.ttf");
        FontsManager.changeFonts(getActivity());
        shoparraylist=new ArrayList<>();
        dialog2 = new Dialog(getActivity());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");
        shoplist=(ListView)view.findViewById(R.id.shoplist);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "mont.ttf");
        Log.e("tag","check1");
        shopingService();
        return view;
    }

    private void shopingService() {
        if (Util.Operations.isOnline(getActivity())) {
            Log.e("tag","check2");
            new CouponSearch().execute();

        } else {
            Toast.makeText(getActivity(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
        }
    }

    private class CouponSearch extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            Log.e("tag","check3");
            dialog2.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("tag","check4");
            String json = "", jsonStr;

            try {


                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(SHOP_URL, json, str_uid, str_token);
            } catch (Exception e) {

            }
            return null;
        }


        @Override
        protected void onPostExecute(String jsonstr) {
            super.onPostExecute(jsonstr);
            dialog2.dismiss();
            //progressBar.setVisibility(View.GONE);

            if (jsonstr.equals("")) {

                //Toast.makeText(getApplicationContext(), "Check Network Connection", Toast.LENGTH_SHORT).show();

            } else {

                try {
                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("status");


                    JSONArray data1 = jo.getJSONArray("message");
                    {
                        for (int j = 0; j < data1.length(); j++) {

                            JSONObject dataObj = data1.getJSONObject(j);
                            map = new HashMap<String, String>();

                            map.put("_id", dataObj.getString("_id"));
                            Log.e("tag","1.."+dataObj.getString("_id"));
                            map.put("shop_category", dataObj.getString("shop_category"));
                            Log.e("tag","2.."+dataObj.getString("shop_category"));
                            map.put("shop_name", dataObj.getString("shop_name"));
                            Log.e("tag","3.."+dataObj.getString("shop_name"));
                            map.put("shop_address", dataObj.getString("shop_address"));
                            Log.e("tag","4.."+dataObj.getString("shop_address"));
                            map.put("time_mon_sat",dataObj.getString("time_mon_sat"));
                            Log.e("tag","5.."+dataObj.getString("time_mon_sat"));
                            map.put("shop_logo",dataObj.getString("shop_logo"));
                            Log.e("tag","7.."+dataObj.getString("shop_logo"));
                            map.put("time_sun",dataObj.getString("time_sun"));
                            Log.e("tag","8.."+dataObj.getString("time_sun"));
                            map.put("demo_shop_id",dataObj.getString("demo_shop_id"));
                            Log.e("tag","8.."+dataObj.getString("demo_shop_id"));


                            JSONArray data2= dataObj.getJSONArray("images");
                            for (int k = 0; k < data2.length(); k++) {

                                JSONObject path = data2.getJSONObject(k);
                                map.put("_id", path.getString("_id"));
                                Log.e("tag","9.."+path.getString("_id"));
                                map.put("image", path.getString("image"));
                                Log.e("tag","10.."+path.getString("image"));
                            }

                            shoparraylist.add(map);
                        }

                    }


                    shopadapter = new ShopAdapter(getActivity(), shoparraylist);
                    shoplist.setAdapter(shopadapter);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        }
    }



