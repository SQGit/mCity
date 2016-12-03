package mcity.com.mcity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 01-11-2016.
 */
public class CouponAdapter extends BaseAdapter {

    Context context;
    //String URL = Data_Service.URL_API + "generatecoupon";
    String SHOW_COUPON = Data_Service.URL_API + "showcoupon";
    String IMAGE_UPLOAD = Data_Service.URL_IMG + "coupons/";
    String str_token, str_uid, str_shop_id, str_shop_path,c_code;
    ArrayList<HashMap<String, String>> searchridelist;
    ArrayList<HashMap<String, String>> data;
    LayoutInflater inflater;
    TextView txt_tokencode;
    HashMap<String, String> resultp = new HashMap<String, String>();


    public CouponAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");
        final TextView txt_expired, txt_expiredval, txt_shopname, txt_description, txt_tc;


        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.coupon_adapter, parent, false);
        resultp = data.get(position);


        LinearLayout img_background = (LinearLayout) itemView.findViewById(R.id.imagelin);
        LinearLayout dot_background = (LinearLayout) itemView.findViewById(R.id.dotlin);
        LinearLayout back_arrow = (LinearLayout) itemView.findViewById(R.id.back_arrow);
        final ImageView shop_logo = (ImageView) itemView.findViewById(R.id.shop_logo);

        txt_shopname = (TextView) itemView.findViewById(R.id.txt_shopname);
        txt_description = (TextView) itemView.findViewById(R.id.txt_description);
        txt_expired = (TextView) itemView.findViewById(R.id.txt_expired);
        txt_tc = (TextView) itemView.findViewById(R.id.tc);

        str_shop_path = IMAGE_UPLOAD + resultp.get("filename");
        Log.e("tag", "testing..." + str_shop_path);

        Picasso.with(context)
                .load(str_shop_path)
                .into(shop_logo);


        txt_shopname.setTypeface(tf);
        txt_description.setTypeface(tf);
        txt_tc.setTypeface(tf);

        txt_shopname.setText(resultp.get(MCoupon.shopname));
        txt_description.setText(resultp.get(MCoupon.description));
        str_shop_id = resultp.get(MCoupon._id);

        dot_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Under Processing",Toast.LENGTH_LONG).show();
                final Dialog dialog = new Dialog(context);
                resultp = data.get(position);
                str_shop_id=resultp.get("_id");
                str_shop_path=IMAGE_UPLOAD + resultp.get("filename");
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.coupon_generate);


                ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
                ImageView logo = (ImageView) dialog.findViewById(R.id.logo);
                TextView shop_name1 = (TextView) dialog.findViewById(R.id.shop_name);
                TextView description1 = (TextView) dialog.findViewById(R.id.description);
                txt_tokencode = (TextView) dialog.findViewById(R.id.token_code);
                TextView redeem = (TextView) dialog.findViewById(R.id.redeem);
                Button generate_code = (Button) dialog.findViewById(R.id.generate_code);


                shop_name1.setText(resultp.get(MCoupon.shopname));
                description1.setText(resultp.get(MCoupon.description));

                Picasso.with(context)
                        .load(str_shop_path)
                        .into(logo);


                new CodeGenerate().execute();

                Typeface tt = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
                shop_name1.setTypeface(tt);
                description1.setTypeface(tt);
                txt_tokencode.setTypeface(tt);
                redeem.setTypeface(tt);

                img_cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                generate_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "You have redeemed your coupon Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

            }
        });


        if (position % 2 == 0) {
            //img_background.setBackgroundColor(context.getResources().getColor(R.color.bg3));
            //dot_background.setBackgroundResource(R.drawable.dotted_red);


        } else {
            //img_background.setBackgroundColor(context.getResources().getColor(R.color.bg4));
            //dot_background.setBackgroundResource(R.drawable.doted_yellow);

        }
        return itemView;
    }

    private class CodeGenerate extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... params) {
            Log.e("tag", "1");
            String json = "", jsonStr = "";
            String id = "";
            try {


                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("couponid", str_shop_id);
                Log.e("tag", "2");
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(SHOW_COUPON, json, str_uid, str_token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String jsonStr) {

            super.onPostExecute(jsonStr);
            //progressBar.setVisibility(View.GONE);

            if (jsonStr.equals("")) {

                Toast.makeText(context, "Check Network Connection", Toast.LENGTH_SHORT).show();

            } else {
                try {

                    JSONObject jo = new JSONObject(jsonStr);
                    String status = jo.getString("status");
                    Log.e("tag", "1...." + status);

                    JSONObject message = jo.getJSONObject("message");
                    Log.e("tag", "2...." + message);

                    JSONArray coupon = message.getJSONArray("coupons");

                    Log.e("tag", "coupon" + coupon);


                    HashMap<String, String> map1 = new HashMap<String, String>();
                    for (int i = 0; i < coupon.length(); i++) {
                        Log.e("tag","111");
                        JSONObject ccc = coupon.getJSONObject(i);
                        map1.put("filename1", ccc.getString("code"));
                        Log.e("tag","222"+ccc.getString("code"));
                        c_code=ccc.getString("code");
                    }


                    txt_tokencode.setText(c_code);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("coupon_code", c_code);
                    editor.commit();


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }

    private class codeToServer extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);

        }


        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            String id = "";
            try {


                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id2", str_shop_id);
                //jsonObject.accumulate("code",coupon_code);

                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(SHOW_COUPON, json, str_uid, str_token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonStr) {

            super.onPostExecute(jsonStr);
            //progressBar.setVisibility(View.GONE);

            if (jsonStr.equals("")) {

                Toast.makeText(context, "Check Network Connection", Toast.LENGTH_SHORT).show();

            } else {
                try {

                    JSONObject jo = new JSONObject(jsonStr);
                    String status = jo.getString("status");
                    Log.e("tag", "1...." + status);

                    String message = jo.getString("message");
                    Log.e("tag", "2...." + message);

                    String code = jo.getString("code");
                    Log.e("tag", "3...." + code);

                    txt_tokencode.setText(code);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("coupon_code", code);
                    editor.commit();


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }
}

