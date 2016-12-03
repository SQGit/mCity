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
        final TextView txt_description,txt_shopname,txt_address,txt_time,txt_menu;
        ImageView call;




        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.retail_adapter, parent, false);
        resultp = data.get(position);





       /* LinearLayout img_background = (LinearLayout) itemView.findViewById(R.id.imagelin);
        LinearLayout dot_background = (LinearLayout) itemView.findViewById(R.id.dotlin);
        LinearLayout back_arrow = (LinearLayout) itemView.findViewById(R.id.back_arrow);*/
        final ImageView img_shop_logo=(ImageView)itemView.findViewById(R.id.img_shop_logo) ;

        txt_description = (TextView) itemView.findViewById(R.id.txt_description);
        txt_shopname=(TextView) itemView.findViewById(R.id.txt_shopname);
        txt_address = (TextView) itemView.findViewById(R.id.txt_address);
        txt_time = (TextView) itemView.findViewById(R.id.txt_time);
        txt_menu= (TextView) itemView.findViewById(R.id.txt_menu);
        call=(ImageView)itemView.findViewById(R.id.call);

        str_shop_path=IMAGE_UPLOAD+resultp.get("filename");
        Log.e("tag","testing..."+str_shop_path);

        /*str_shop_view=IMAGE_UPLOAD+resultp.get("filename1");
        Log.e("tag","testing1..."+str_shop_view);
*/
        str_phno=resultp.get("mobileno");
        Log.e("tag","mobile..."+str_phno);


        Picasso.with(context)
                .load(str_shop_path)
                .into(img_shop_logo);


        txt_shopname.setTypeface(tf);
        txt_description.setTypeface(tf);
        txt_address.setTypeface(tf);
        txt_time.setTypeface(tf);

        String str_shopname=resultp.get(OneFragment.shopname);
        String str_description=resultp.get(OneFragment.description);
        String str_address=resultp.get(OneFragment.address);
        String str_openingtime=resultp.get(OneFragment.openingtime);

        Log.e("tag","checking_values"+str_shopname+str_description+str_address+str_openingtime);


        txt_shopname.setText(resultp.get(TwoFragment.retailname));
        txt_address.setText(resultp.get(TwoFragment.description));
        txt_time.setText(resultp.get(TwoFragment.openingtime));
        txt_description.setText(resultp.get(TwoFragment.retaildescription));




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

