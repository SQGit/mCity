package mcity.com.mcity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 25-02-2017.
 */
public class CouponCall extends BaseAdapter{
    Context context1;
    String IMAGE_UPLOAD = Data_Service.URL_IMG + "coupons/";
    String str_token, str_uid,str_shop_id,str_shop_path;
    ArrayList<HashMap<String, String>> data;
    LayoutInflater inflater;
    TextView txt_tokencode;
    HashMap<String, String> resultp = new HashMap<String, String>();
    Activity activity;
    Dialog dialog1;
    Button generate_code;
    TextView shop_name1,description1;
    ImageView logo,img_cross;


    public CouponCall(Context context, ArrayList<HashMap<String, String>> arraylist, Activity activity) {
        this.context1 = context;
        data = arraylist;
        this.activity=activity;

    }



    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context1);
        str_token = sharedPreferences.getString("token", "");
        str_uid = sharedPreferences.getString("id", "");
        final TextView txt_expiredval1, txt_expiredval, txt_shopname, txt_description, txt_tc;

        Typeface tf = Typeface.createFromAsset(context1.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.coupon_adapter, viewGroup, false);
        resultp = data.get(position);

        LinearLayout dot_background = (LinearLayout) itemView.findViewById(R.id.dotlin);
        final ImageView shop_logo = (ImageView) itemView.findViewById(R.id.shop_logo);

        txt_shopname = (TextView) itemView.findViewById(R.id.txt_shopname);
        txt_description = (TextView) itemView.findViewById(R.id.txt_description);
        txt_tc = (TextView) itemView.findViewById(R.id.tc);
        txt_expiredval=(TextView)itemView.findViewById(R.id.txt_date);
        txt_expiredval1=(TextView)itemView.findViewById(R.id.txt_date1);

        str_shop_path = IMAGE_UPLOAD + resultp.get("shop_logo");


        Picasso.with(context1)
                .load(str_shop_path)
                .into(shop_logo);


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


        dialog1 = new Dialog(activity);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog1.setContentView(R.layout.coupon_generate);


        img_cross = (ImageView) dialog1.findViewById(R.id.img_cross);
        logo = (ImageView) dialog1.findViewById(R.id.logo);
        shop_name1 = (TextView) dialog1.findViewById(R.id.shop_name);
        description1 = (TextView) dialog1.findViewById(R.id.description);
        txt_tokencode = (TextView) dialog1.findViewById(R.id.token_code);
        generate_code = (Button) dialog1.findViewById(R.id.generate_code);

        Typeface tt = Typeface.createFromAsset(context1.getAssets(), "mont.ttf");
        shop_name1.setTypeface(tt);
        description1.setTypeface(tt);
        txt_tokencode.setTypeface(tt);



            dot_background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("tag","11");

                    dialog1.show();

                    resultp = data.get(position);
                    str_shop_id=resultp.get("_id");
                    str_shop_path=IMAGE_UPLOAD + resultp.get("shop_logo");


                    shop_name1.setText(resultp.get("shop_name"));
                    description1.setText(resultp.get("coupon_desc"));

                    Picasso.with(context1)
                            .load(str_shop_path)
                            .into(logo);



                   txt_tokencode.setText(resultp.get("coupon_code"));

                    img_cross.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();

                        }
                    });


                    generate_code.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent iii=new Intent(activity,MShop.class);
                            activity.startActivity(iii);

                        }
                    });
                    dialog1.dismiss();
                }
            });







        if (position % 2 == 0) {

        } else {
        }
        return itemView;
    }


}
