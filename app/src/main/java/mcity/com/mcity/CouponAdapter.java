package mcity.com.mcity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
    String IMAGE_UPLOAD = Data_Service.URL_IMG + "shop_logo/";
    String str_token, str_uid, str_shop_id, str_shop_path;
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
        final TextView txt_expiredval1, txt_expiredval, txt_shopname, txt_description, txt_tc;


        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.coupon_adapter, parent, false);
        resultp = data.get(position);

        LinearLayout dot_background = (LinearLayout) itemView.findViewById(R.id.dotlin);
        final ImageView shop_logo = (ImageView) itemView.findViewById(R.id.shop_logo);

        txt_shopname = (TextView) itemView.findViewById(R.id.txt_shopname);
        txt_description = (TextView) itemView.findViewById(R.id.txt_description);
        txt_tc = (TextView) itemView.findViewById(R.id.tc);
        txt_expiredval=(TextView)itemView.findViewById(R.id.txt_date);
        txt_expiredval1=(TextView)itemView.findViewById(R.id.txt_date1);

        str_shop_path = IMAGE_UPLOAD + resultp.get("shop_logo");
        Log.e("tag","GUNA"+str_shop_path);

        if(str_shop_path.equals("http://104.197.80.225:3000/mcity/shop_logo/"))
        {
            Picasso.with(context)
                    .load(R.drawable.no_image)
                    .into(shop_logo);

        }
                else
        {
            Picasso.with(context)
                    .load(str_shop_path)
                    .into(shop_logo);

        }


        txt_shopname.setTypeface(tf);
        txt_description.setTypeface(tf);
        txt_tc.setTypeface(tf);
        txt_expiredval.setTypeface(tf);
        txt_expiredval1.setTypeface(tf);
        txt_shopname.setText(resultp.get("shop_name"));
        txt_description.setText(resultp.get("coupon_desc"));
        str_shop_id = resultp.get("_id");
        txt_expiredval.setText("Expired: ");
        txt_expiredval1.setText(resultp.get("coupon_expiry_date"));




        dot_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                resultp = data.get(position);
                str_shop_id=resultp.get("_id");
                str_shop_path=IMAGE_UPLOAD + resultp.get("shop_logo");
                Log.e("tag","testing........"+str_shop_path);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.setContentView(R.layout.coupon_generate);


                ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
                ImageView logo = (ImageView) dialog.findViewById(R.id.logo);
                TextView shop_name1 = (TextView) dialog.findViewById(R.id.shop_name);
                TextView description1 = (TextView) dialog.findViewById(R.id.description);
                txt_tokencode = (TextView) dialog.findViewById(R.id.token_code);
                Button generate_code = (Button) dialog.findViewById(R.id.generate_code);

                shop_name1.setText(resultp.get("shop_name"));
                description1.setText(resultp.get("coupon_desc"));


                if(str_shop_path.equals("http://104.197.80.225:3000/mcity/shop_logo/"))
                {
                    Picasso.with(context)
                            .load(R.drawable.no_image)
                            .into(logo);

                }
                else
                {
                    Picasso.with(context)
                            .load(str_shop_path)
                            .into(logo);

                }



                Typeface tt = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
                shop_name1.setTypeface(tt);
                description1.setTypeface(tt);
                txt_tokencode.setTypeface(tt);
                txt_tokencode.setText(resultp.get("coupon_code"));

                img_cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                generate_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                       /* Intent iii=new Intent(context,MCoupon.class);
                        context.startActivity(iii);*/



                    }
                });
                dialog.show();

            }
        });


        if (position % 2 == 0) {

        } else {
        }
        return itemView;
    }
}

