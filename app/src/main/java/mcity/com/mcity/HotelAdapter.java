package mcity.com.mcity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ANANDH on 22-12-2015.
 */
public class HotelAdapter extends BaseAdapter {



    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    String str_owner_no,str_owner_mail;

    public HotelAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView txt_loc,txt_roomTypeval,txt_rent,txt_chennai,txt_mcity,txt_rs,txt_subtype, txt_address, txt_description,txt_gender,txt_renttv,txt_subtv,txt_furnishedtv,txt_make_call, txt_send_mail;

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.search_properties_list_item, parent, false);
        resultp = data.get(position);


        txt_loc = (TextView) itemView.findViewById(R.id.loc);
        txt_address = (TextView) itemView.findViewById(R.id.city);
        txt_rent=(TextView) itemView.findViewById(R.id.rents);
        txt_subtype=(TextView) itemView.findViewById(R.id.subtype);
        txt_chennai=(TextView) itemView.findViewById(R.id.chennai);
        txt_rs=(TextView) itemView.findViewById(R.id.rs);
        txt_mcity = (TextView) itemView.findViewById(R.id.mcity);
        txt_gender =(TextView)itemView.findViewById(R.id.gender);
        txt_description = (TextView) itemView.findViewById(R.id.description);
        txt_renttv = (TextView) itemView.findViewById(R.id.rent_tv);
        txt_subtv = (TextView) itemView.findViewById(R.id.subtype_tv);
        txt_furnishedtv = (TextView) itemView.findViewById(R.id.furnishedtype_tv);
        LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.view);
        txt_roomTypeval=(TextView) itemView.findViewById(R.id.roomTypeval);
        txt_make_call = (TextView) itemView.findViewById(R.id.make_call);
        txt_send_mail = (TextView) itemView.findViewById(R.id.send_mail);


        txt_loc.setText(resultp.get("location"));
        txt_rent.setText(resultp.get("monthlyrent"));
        txt_description.setText(resultp.get("description"));
        txt_subtype.setText(resultp.get("monthlyrent"));
        txt_gender.setText(resultp.get("gender"));
        txt_roomTypeval.setText(resultp.get("roomtype"));
        str_owner_no = resultp.get("mobileno");
        str_owner_mail=resultp.get("email");
        String enable_status=resultp.get("phone");
        final String username=resultp.get("username");



        txt_chennai.setTypeface(tf);
        txt_description.setTypeface(tf);
        txt_loc.setTypeface(tf);
        txt_rent.setTypeface(tf);
        txt_rs.setTypeface(tf);
        txt_mcity.setTypeface(tf);
        txt_subtype.setTypeface(tf);
        txt_renttv.setTypeface(tf);
        txt_furnishedtv.setTypeface(tf);
        txt_gender.setTypeface(tf);
        txt_subtv.setTypeface(tf);
        txt_roomTypeval.setTypeface(tf);


        if(enable_status.equals("enabled"))
        {
            txt_make_call.setText(resultp.get("mobileno"));

            txt_make_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    } else {

                        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                        phoneIntent.putExtra(Intent.EXTRA_SUBJECT, "Mcity");
                        phoneIntent.setData(Uri.parse("tel:"+str_owner_no));
                        phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        try {
                            v.getContext().startActivity(phoneIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(v.getContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });


        }else
        {
            txt_make_call.setText("hidden");
            txt_make_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"You cant able to make call. Please contact through mail...",Toast.LENGTH_SHORT).show();
                }
            });
        }



        txt_send_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", str_owner_mail, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MCity");
                emailIntent.putExtra(Intent.EXTRA_TEXT   , "Hi  "+username+"\n"+ "   Iam interested in your Property. Please contact me.");
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(emailIntent);
            }
        });


        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.bg1));

        } else {
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.bg2));
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return itemView;
    }
}