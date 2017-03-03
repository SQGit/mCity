package mcity.com.mcity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 15-11-2016.
 */
public class ShopAdapter extends BaseAdapter {

    Context context;

    String IMAGE_UPLOAD = Data_Service.URL_IMG + "shop_logo/";
    String IMAGE_UPLOAD1 = Data_Service.URL_IMG + "menu/";
    String str_token, str_uid,str_shop_id,str_shop_path,str_shop_view,str_phno,call_coupon_id;
    ArrayList<HashMap<String, String>> searchridelist;
    ArrayList<HashMap<String, String>> data;
    LayoutInflater inflater;
    TextView txt_tokencode;
    HashMap<String, String> resultp = new HashMap<String, String>();
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    ProgressBar progressBar;
    Dialog dialog2;


    public ShopAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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
        final TextView txt_description,txt_shopname,txt_address,txt_time,txt_timedes,txt_time_sun,txt_des_sun,txt_menu,txt_coupon;
        ImageView call;

        dialog2 = new Dialog(context);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader1);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);


        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.shop_adapter, parent, false);
        resultp = data.get(position);
        final ImageView img_shop_logo=(ImageView)itemView.findViewById(R.id.img_shop_logo) ;

        txt_description = (TextView) itemView.findViewById(R.id.txt_description);
        txt_shopname=(TextView) itemView.findViewById(R.id.txt_shopname);
        txt_address = (TextView) itemView.findViewById(R.id.txt_address);
        txt_time = (TextView) itemView.findViewById(R.id.txt_time);
        txt_menu= (TextView) itemView.findViewById(R.id.txt_menu);
        txt_timedes=(TextView)itemView.findViewById(R.id.txt_des) ;
        txt_time_sun=(TextView)itemView.findViewById(R.id.txt_time_sun) ;
        txt_des_sun=(TextView)itemView.findViewById(R.id.txt_des_sun) ;
        txt_coupon=(TextView) itemView.findViewById(R.id.txt_coupon);
        call=(ImageView)itemView.findViewById(R.id.call);

        str_shop_path=IMAGE_UPLOAD+resultp.get("shop_logo");
        Log.e("tag","testing..."+str_shop_path);



        str_phno=resultp.get("mobile_no");
        Log.e("tag","phone_no"+resultp.get("mobile_no"));


if(str_shop_path.equals("http://104.197.80.225:3000/mcity/shop_logo/"))
{
    Picasso.with(context)
            .load(R.drawable.no_image)
            .into(img_shop_logo);
}
        else
{
    Picasso.with(context)
            .load(str_shop_path)
            .into(img_shop_logo);
}



        txt_shopname.setTypeface(tf);
        txt_description.setTypeface(tf);
        txt_address.setTypeface(tf);
        txt_time.setTypeface(tf);
        txt_menu.setTypeface(tf);
        txt_coupon.setTypeface(tf);
        txt_timedes.setTypeface(tf);
        txt_time_sun.setTypeface(tf);
        txt_des_sun.setTypeface(tf);

        String str_shopname=resultp.get("shop_name");
        String str_description=resultp.get("shop_category");
        String str_address=resultp.get("shop_address");
        String str_openingtime=resultp.get("time_mon_sat");
        String str_sunday_time=resultp.get("time_sun");
        String str_check_sunday=resultp.get("is_sunday");
        Log.e("tag","sunday"+str_check_sunday);
        String leave="Holiday";

        Log.e("tag","checking_values"+str_shopname+str_description+str_address+str_openingtime);

        txt_shopname.setText(resultp.get("shop_name"));
        txt_address.setText(resultp.get("shop_address"));

        if(str_check_sunday.equals("true"))
        {
            Log.e("tag","r1");
            txt_time_sun.setText(resultp.get("time_sun"));
        }
        else if(str_check_sunday.equals("false"))
        {
            Log.e("tag","r2");
            txt_time_sun.setText(leave);
        }

        txt_time.setText(resultp.get("time_mon_sat"));
        txt_description.setText(resultp.get("shop_category"));

        txt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_shop_view=IMAGE_UPLOAD1+resultp.get("image");
                Log.e("tag","test..."+str_shop_view);
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.samp);
                dialog2.show();

                ImageView cross = (ImageView) dialog.findViewById(R.id.cross);
                ImageView menu_list = (ImageView) dialog.findViewById(R.id.menu_list);

                if(str_shop_view.equals("http://104.197.80.225:3000/mcity/menu/"))
                {
                    Picasso.with(context)
                            .load(R.drawable.no_image)
                            .into(menu_list);

                }
                else{
                    Picasso.with(context)
                            .load(str_shop_view)
                            .into(menu_list);
                }


                dialog2.dismiss();
                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        txt_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp = data.get(position);
                call_coupon_id=resultp.get("demo_shop_id");
                Log.e("tag","call..."+call_coupon_id);

                Intent t_coupon=new Intent(context.getApplicationContext(),CallCouponFromShop.class);
                t_coupon.putExtra("demo_shop_id",call_coupon_id);
                context.startActivity(t_coupon);
                ((Activity)context).finish();
            }
        });



        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp = data.get(position);
                str_phno=resultp.get("mobile_no");
                Log.e("tag","mobile number"+resultp.get("mobile_no"));
                int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                } else {
                    Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                    phoneIntent.setData(Uri.parse("tel:" + str_phno));
                    Log.e("tag","number"+str_phno);

                    try {
                        v.getContext().startActivity(phoneIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                    }
                }
            }

        });

        return itemView;
    }
}


