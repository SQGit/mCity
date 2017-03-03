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
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 17-11-2016.
 */
public class RetailAdapter extends BaseAdapter{

    Context context;
    String URL = Data_Service.URL_API + "generatecoupon";
    String IMAGE_UPLOAD = Data_Service.URL_IMG + "retail/";
    String str_token, str_uid,str_shop_id,str_shop_path,str_shop_view,str_phno;
    ArrayList<HashMap<String, String>> searchridelist;
    ArrayList<HashMap<String, String>> data;
    LayoutInflater inflater;
    TextView txt_tokencode;
    HashMap<String, String> resultp = new HashMap<String, String>();
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


    public RetailAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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
        final TextView txt_description,txt_shopname,txt_address,txt_time,txt_time_sun,txt_des_sun,txt_des;
        ImageView call;




        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.retail_adapter, parent, false);
        resultp = data.get(position);





        final ImageView img_shop_logo=(ImageView)itemView.findViewById(R.id.img_shop_logo) ;

        txt_description = (TextView) itemView.findViewById(R.id.txt_description);
        txt_shopname=(TextView) itemView.findViewById(R.id.txt_shopname);
        txt_address = (TextView) itemView.findViewById(R.id.txt_address);
        txt_time = (TextView) itemView.findViewById(R.id.txt_time);
        txt_time_sun= (TextView) itemView.findViewById(R.id.txt_time_sun);
        txt_des_sun= (TextView) itemView.findViewById(R.id.txt_des_sun);
       // txt_des= (TextView) itemView.findViewById(R.id.txt_des);
        call=(ImageView)itemView.findViewById(R.id.call);

        str_shop_path=IMAGE_UPLOAD+resultp.get("shop_logo");
        Log.e("tag","testing..."+str_shop_path);

        String str_check_sunday=resultp.get("is_sunday");
        str_phno=resultp.get("mobileno");
        Log.e("tag","mobile..."+str_phno);
        String leave="Holiday";



        if(str_shop_path.equals("http://104.197.80.225:3000/mcity/retail/"))
        {
            Log.e("tag","test1");
            Picasso.with(context)
                    .load(R.drawable.no_image)
                    .into(img_shop_logo);
        }
        else{
            Log.e("tag","test2");
            Picasso.with(context)
                    .load(str_shop_path)
                    .into(img_shop_logo);
        }


        txt_shopname.setTypeface(tf);
        txt_description.setTypeface(tf);
        txt_address.setTypeface(tf);
        txt_time.setTypeface(tf);
        txt_time_sun.setTypeface(tf);
        txt_des_sun.setTypeface(tf);


        String str_shopname=resultp.get("shop_name");
        String str_description=resultp.get("shop_sub_type");
        String str_address=resultp.get("shop_address");
        String str_openingtime=resultp.get("time_mon_sat")+"vbvhbgh";

        Log.e("tag","checking_values"+str_shopname+str_description+str_address+str_openingtime);


        txt_shopname.setText(resultp.get("shop_name"));
        txt_address.setText(resultp.get("shop_address"));
        txt_time.setText((resultp.get("time_mon_sat"))+"(Mon-Sat)");
        //txt_time.setText(");
        txt_description.setText(resultp.get("shop_sub_type"));
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


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

