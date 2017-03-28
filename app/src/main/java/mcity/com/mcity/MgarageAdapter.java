package mcity.com.mcity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 21-12-2016.
 */

public class MgarageAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    String img1,img2,img3,img4;
    Bitmap b;
    HashMap<String, String> resultp_garage = new HashMap<String, String>();
    ArrayList<String> loading ;
    String str_owner_number,str_owner_mail,str_owner_name,enable_status;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


    public MgarageAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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

        TextView txt_category, txt_model, txt_price, txt_color,  txt_kms,description;
        TextView txt_model_h,txt_price_h,txt_color_h,txt_kms_h;
        final TextView txt_send_mail,txt_makecall;
        String str_model,str_color,str_km;

        SharedPreferences sharedPreferences;
        SmsManager smsManager = SmsManager.getDefault();
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.mgarage_history_adapter, parent, false);
        resultp_garage = data.get(position);

        Log.e("tag","11111"+resultp_garage);
        loading = new ArrayList<>();

        img1 = resultp_garage.get("path0");
        img2 = resultp_garage.get("path1");
        img3 = resultp_garage.get("path2");
        img4 = resultp_garage.get("path3");

        loading.add(img1);
        loading.add(img2);
        loading.add(img3);
        loading.add(img4);

        txt_category = (TextView) itemView.findViewById(R.id.txt_category);
        txt_model = (TextView) itemView.findViewById(R.id.txt_model);
        txt_price = (TextView) itemView.findViewById(R.id.txt_price);
        txt_color = (TextView) itemView.findViewById(R.id.txt_color);
        txt_kms = (TextView) itemView.findViewById(R.id.txt_kms);
        description=(TextView)itemView.findViewById(R.id.description);

        txt_model_h = (TextView) itemView.findViewById(R.id.txt_model_h);
        txt_price_h = (TextView) itemView.findViewById(R.id.txt_price_h);
        txt_color_h = (TextView) itemView.findViewById(R.id.txt_color_h);
        txt_kms_h = (TextView) itemView.findViewById(R.id.txt_kms_h);

        txt_send_mail = (TextView) itemView.findViewById(R.id.txt_send_mail);
        txt_makecall = (TextView) itemView.findViewById(R.id.txt_makecall);

        LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.view);
        final ImageView loadimage = (ImageView) itemView.findViewById(R.id.loadimg);

        str_model=resultp_garage.get("field1");
        str_color=resultp_garage.get("field2");
        str_km=resultp_garage.get("field4");
        Log.e("tag","value"+resultp_garage.get("field1")+resultp_garage.get("field2")+resultp_garage.get("field4"));

        if(str_model.equals("not specified")&&str_color.equals("not specified")&&str_km.equals("not specified"))
        {

            txt_kms.setVisibility(View.INVISIBLE);
            //txt_color.setVisibility(View.INVISIBLE);
            //txt_model.setVisibility(View.INVISIBLE);
            txt_kms_h.setVisibility(View.INVISIBLE);
            txt_color.setText(resultp_garage.get("field2"));
            txt_model.setText(resultp_garage.get("field1"));
            txt_category.setText(resultp_garage.get("category"));
            txt_price.setText(resultp_garage.get("price"));
            description.setText(resultp_garage.get("description"));
            str_owner_number=resultp_garage.get("mobileno");
            str_owner_name=resultp_garage.get("username");
            str_owner_mail=resultp_garage.get("email");
            enable_status=resultp_garage.get("phone");
        }
        else
        {
            txt_kms.setText(str_km);
            txt_color.setText(str_color);
            txt_model.setText(str_model);
            txt_category.setText(resultp_garage.get("category"));
            txt_price.setText(resultp_garage.get("price"));
            description.setText(resultp_garage.get("description"));
            str_owner_number=resultp_garage.get("mobileno");
            str_owner_name=resultp_garage.get("username");
            str_owner_mail=resultp_garage.get("email");
            enable_status=resultp_garage.get("phone");
        }


        Picasso.with(context)
                .load(resultp_garage.get("path0"))
                .into(loadimage);
        Log.e("tag","image_checking"+resultp_garage.get("path0"));

        txt_category.setTypeface(tf);
        txt_model.setTypeface(tf);
        txt_price.setTypeface(tf);
        txt_color.setTypeface(tf);
        txt_kms.setTypeface(tf);
        txt_model_h.setTypeface(tf);
        txt_price_h.setTypeface(tf);
        txt_color_h.setTypeface(tf);
        txt_kms_h.setTypeface(tf);
        description.setTypeface(tf);
        txt_send_mail.setTypeface(tf);
        txt_makecall.setTypeface(tf);


        txt_send_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", str_owner_mail, null));


                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MCity");
                emailIntent.putExtra(Intent.EXTRA_TEXT   , "Hi  "+str_owner_name+"\n"+ "   I am interested in your Property. Please contact me.");
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(emailIntent);
            }
        });



        txt_makecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultp_garage = data.get(position);
                str_owner_number=resultp_garage.get("mobileno");
                enable_status=resultp_garage.get("phone");
                Log.e("tag","@@@"+str_owner_number+enable_status);

                if(enable_status.equals("enabled"))
                {
                    int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    } else {

                        Log.e("tag","we"+str_owner_number);
                        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                        phoneIntent.setData(Uri.parse("tel:"+str_owner_number));

                        try {
                            view.getContext().startActivity(phoneIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(view.getContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                else if(enable_status.equals("Hidden Contact"))
                {
                    txt_makecall.setText("hidden");
                    txt_makecall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(v.getContext(),"You can't able to make call. Please contact through mail...",Toast.LENGTH_SHORT).show();
                        }
                    });
                }




            }
        });


       /* txt_makecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          if(enable_status.equals("enabled")) {
              resultp_garage = data.get(position);
              str_owner_number = resultp_garage.get("mobileno");

              int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
              if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                  ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
              } else {

                  Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                  phoneIntent.setData(Uri.parse("tel:" + str_owner_number));
                  phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                  try {
                      v.getContext().startActivity(phoneIntent);

                  } catch (android.content.ActivityNotFoundException ex) {
                      Toast.makeText(v.getContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
                  }
              }
          }
            }
        });*/
       loadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = new ArrayList<>();
                resultp_garage = data.get(position);


                for(int i =0;i<4;i++){
                    if(resultp_garage.get("path"+i)!= null){
                        loading.add(resultp_garage.get("path"+i));
                    }
                }

                if (loading.size() > 0) {

                    LayoutInflater layoutInflater = LayoutInflater.from(v.getRootView().getContext());
                    View promptView = layoutInflater.inflate(R.layout.zoom_layout1, null);

                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                    alertbox.setCancelable(true);

                    ViewPager mViewPager;
                    CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(v.getRootView().getContext(), loading);
                    mViewPager = (ViewPager) promptView.findViewById(R.id.pager);
                    mViewPager.setAdapter(mCustomPagerAdapter);
                    mCustomPagerAdapter.notifyDataSetChanged();
                    alertbox.setView(promptView);
                    alertbox.show();
                }
                else{
                    Toast.makeText(v.getContext(), "no images found", Toast.LENGTH_LONG).show();
                }
            }
        });
        return itemView;
    }

}
