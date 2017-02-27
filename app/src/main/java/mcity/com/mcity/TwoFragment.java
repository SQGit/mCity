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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 03-11-2016.
 */
public class TwoFragment extends Fragment{
    String SHOP_URL = Data_Service.URL_API + "getretailsnew";
    TextView tv, tv1, tv2;
    TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, txt_order;
    ImageView call, call1, call2;
    String str_token, str_uid,str_shop_path;
    RetailAdapter retailadapter;
    ListView shoplist;
    ArrayList<HashMap<String, String>> retailarraylist;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    HashMap<String, String> map;
    ProgressBar progressBar;
    Dialog dialog2;


    static String retaildescription= "retaildescription";
    static String retailname = "retailname";
    static String openingtime = "openingtime";
    static String _id="_id";
    static String description = "description";
    static String mobileno = "mobileno";
    public TwoFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_two, container, false);

        FontsManager.initFormAssets(getActivity(), "mont.ttf");
        FontsManager.changeFonts(getActivity());
        retailarraylist=new ArrayList<>();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");



        dialog2 = new Dialog(getActivity());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        shoplist=(ListView)view.findViewById(R.id.shoplist);


                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "mont.ttf");


        retailService();



        return view;
    }

    private void retailService() {


        if (Util.Operations.isOnline(getActivity())) {

            new RetailSearch().execute();


        } else {
            Toast.makeText(getActivity(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
        }
    }

    private class RetailSearch extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
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
                            map.put("shop_sub_type", dataObj.getString("shop_sub_type"));
                            Log.e("tag","2.."+dataObj.getString("shop_sub_type"));
                            map.put("shop_name", dataObj.getString("shop_name"));
                            Log.e("tag","3.."+dataObj.getString("shop_name"));
                            map.put("shop_address", dataObj.getString("shop_address"));
                            Log.e("tag","4.."+dataObj.getString("shop_address"));
                            map.put("time_mon_sat",dataObj.getString("time_mon_sat"));
                            Log.e("tag","5.."+dataObj.getString("time_mon_sat"));
                            map.put("shop_logo",dataObj.getString("shop_logo"));
                            Log.e("tag","7.."+dataObj.getString("shop_logo"));

                            JSONArray data2= dataObj.getJSONArray("images");
                            for (int k = 0; k < data2.length(); k++) {

                                JSONObject path = data2.getJSONObject(k);
                                map.put("_id", path.getString("_id"));
                                Log.e("tag","8.."+path.getString("_id"));
                                map.put("image", path.getString("image"));
                                Log.e("tag","9.."+path.getString("image"));
                            }

                            retailarraylist.add(map);
                        }

                    }


                    retailadapter = new RetailAdapter(getActivity(), retailarraylist);
                    shoplist.setAdapter(retailadapter);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }
}


