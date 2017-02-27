package mcity.com.mcity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 10-02-2017.
 */
public class RideAdapter2 extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    ArrayList<String> loading = new ArrayList<>();
    String split, get_mailaddress,call_no,show_image;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    String str_from,str_to,str_date,mobileno,username;
    TextView author_image;

    public RideAdapter2(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
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
        final TextView fromaddress, toaddress,to, date, open_stmt, mail, time, amount, contact,midway_status,date1,date2,date_head;
        final String str_persons,lic_image,return_date,go_date;
        final LinearLayout lnr_round_trip;

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.search_ride_list, viewGroup, false);
        resultp = data.get(position);


        fromaddress = (TextView) itemView.findViewById(R.id.fromaddress);
        to=(TextView) itemView.findViewById(R.id.to);
        toaddress = (TextView) itemView.findViewById(R.id.toaddress);
        date = (TextView) itemView.findViewById(R.id.date);
        time = (TextView) itemView.findViewById(R.id.timer);
        open_stmt= (TextView) itemView.findViewById(R.id.no_of_persons);
        amount= (TextView) itemView.findViewById(R.id.amount);
        mail = (TextView) itemView.findViewById(R.id.mail);
        contact = (TextView) itemView.findViewById(R.id.contact);
        midway_status=(TextView)itemView.findViewById(R.id.midway_status);
        author_image=(TextView)itemView.findViewById(R.id.license_image);
        date1 = (TextView) itemView.findViewById(R.id.date1);
        date2 = (TextView) itemView.findViewById(R.id.date2);
        date_head = (TextView) itemView.findViewById(R.id.round_head);
        lnr_round_trip=(LinearLayout)itemView.findViewById(R.id.lnr_round_trip);


        lic_image=resultp.get("path");


        str_from = resultp.get(MorePOst.from);
        str_to=resultp.get(MorePOst.to);
        str_date=resultp.get(MorePOst.date);
        username=resultp.get(MorePOst.username);
        call_no=resultp.get(MorePOst.mobileno);
        String datestr=resultp.get(MorePOst.date);
        String iii=resultp.get("path");

        return_date=resultp.get(RideSearch.returndate);
        go_date=resultp.get(RideSearch.godate);


        if(return_date.equals("empty")&&go_date.equals("empty"))
        {
            lnr_round_trip.setVisibility(View.GONE);
        }
        else
        {
            lnr_round_trip.setVisibility(View.VISIBLE);
            date1.setText(go_date);
            date2.setText(return_date);
        }

        String[] splited = str_date.split(" ");


        fromaddress.setText(resultp.get(MorePOst.from));
        toaddress.setText(resultp.get(MorePOst.to));

        date.setText(splited[0]);
        time.setText(splited[2]+splited[3]);

        get_mailaddress=resultp.get(MorePOst.email);
        amount.setText(resultp.get(MorePOst.price));
        midway_status.setText(resultp.get(MorePOst.midwaydrop));
        String enable_status=resultp.get(MorePOst.phone);
        str_persons=resultp.get(MorePOst.noofpersons);
        Log.e("tag","test_person"+str_persons);

        open_stmt.setText(str_persons+" Seat Availabale");

        fromaddress.setTypeface(tf);
        to.setTypeface(tf);
        toaddress.setTypeface(tf);
        date.setTypeface(tf);
        time.setTypeface(tf);
        open_stmt.setTypeface(tf);
        amount.setTypeface(tf);
        mail.setTypeface(tf);
        contact.setTypeface(tf);
        midway_status.setTypeface(tf);
        author_image.setTypeface(tf);
        date1.setTypeface(tf);
        date2.setTypeface(tf);
        date_head.setTypeface(tf);



        if(enable_status.equals("enabled"))
        {
            contact.setText("Make Call");
            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    } else
                    {
                        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                        phoneIntent.setData(Uri.parse("tel:"+call_no));

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
            contact.setText("Make Call");
            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"You cant able to make call. Please contact through Email...",Toast.LENGTH_SHORT).show();
                }
            });
        }



        author_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = new ArrayList<>();
                resultp = data.get(position);

                        loading.add(resultp.get("path"));

                LayoutInflater layoutInflater = LayoutInflater.from(v.getRootView().getContext());
                View promptView = layoutInflater.inflate(R.layout.zoom_layout1, null);
                ImageView close_iv;
                close_iv=(ImageView)v.findViewById(R.id.close_iv);
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


        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", get_mailaddress, null));

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MCity"+ "  "+str_from+" to "+str_to+"  "+str_date);
                emailIntent.putExtra(Intent.EXTRA_TEXT   , "Hai  "+username+"\n"+ "                   I would like to use your Ride. Please contact me.");
                context.startActivity(emailIntent);
            }
        });

        return itemView;
    }
}
