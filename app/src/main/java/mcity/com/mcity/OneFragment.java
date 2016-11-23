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
    String SHOP_URL = Data_Service.URL_API + "getrestaurants";
    TextView tv, tv1, tv2;
    TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, txt_order;
    ImageView call, call1, call2;
    String str_token, str_uid,str_shop_path;
    ShopAdapter shopadapter;
    ListView shoplist;
    ArrayList<HashMap<String, String>> shoparraylist;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


    static String shopname = "shopname";
    static String address = "address";
    static String openingtime = "openingtime";
    static String _id="_id";
    static String description = "description";
    static String mobileno = "mobileno";



    public OneFragment() {
        // Required empty public constructor
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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");

        shoplist=(ListView)view.findViewById(R.id.shoplist);


        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "mont.ttf");


        shopingService();


        return view;
    }

    private void shopingService() {


        if (Util.Operations.isOnline(getActivity())) {

            new CouponSearch().execute();


        } else {
            Toast.makeText(getActivity(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
        }
    }

    private class CouponSearch extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            String id = "";
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
            //progressBar.setVisibility(View.GONE);

            if (jsonstr.equals("")) {

                Toast.makeText(getActivity(), "Check Network Connection", Toast.LENGTH_SHORT).show();

            } else {

                try {
                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("status");
                    Log.e("tag","status"+status);

                    JSONArray data1 = jo.getJSONArray("message");
                    {
                        for (int j = 0; j < data1.length(); j++)
                        {

                            Log.e("tag", "length" + data1.length());

                            JSONObject dataObj = data1.getJSONObject(j);
                            Log.e("tag", "dataobj" + dataObj);


                            HashMap<String, String> map1 = new HashMap<String, String>();
                            map1.put("_id", dataObj.getString("_id"));
                            map1.put("shopname", dataObj.getString("restaurantname"));
                            map1.put("address", dataObj.getString("address"));
                            map1.put("openingtime", dataObj.getString("openingtime"));
                            map1.put("description", dataObj.getString("description"));
                            map1.put("mobileno", dataObj.getString("mobileno"));
                            Log.e("tag", "mobile" +  dataObj.getString("mobileno"));



                            JSONArray data2 = dataObj.getJSONArray("logo");
                            for (int k = 0; k < data2.length(); k++) {

                                JSONObject path = data2.getJSONObject(k);
                                map1.put("filename", path.getString("filename"));
                            }


                            JSONArray data3 = dataObj.getJSONArray("viewmenu");
                            Log.e("tag","000");
                            for (int l = 0; l < data3.length(); l++) {
                                Log.e("tag","111");
                                JSONObject path = data3.getJSONObject(l);
                                map1.put("filename1", path.getString("filename"));
                                Log.e("tag","222"+path.getString("filename"));
                            }

                            shoparraylist.add(map1);
                        }
                    }
                    Log.e("tag","list"+shoparraylist);
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


