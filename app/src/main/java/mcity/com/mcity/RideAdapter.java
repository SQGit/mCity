package mcity.com.mcity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;


public class RideAdapter extends BaseAdapter {
    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    ArrayList<String> loading = new ArrayList<>();
    String split, get_mailaddress,call_no,show_image;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    String str_from,str_to,str_date,mobileno,username;
    TextView author_image;

    public RideAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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
        final TextView fromaddress, toaddress,to, date, open_stmt, mail, time, amount, contact,midway_status;
        //final ImageView author_image;

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.search_ride_list, parent, false);
        resultp = data.get(position);


        fromaddress = (TextView) itemView.findViewById(R.id.fromaddress);
        to=(TextView) itemView.findViewById(R.id.to);
        toaddress = (TextView) itemView.findViewById(R.id.toaddress);
        date = (TextView) itemView.findViewById(R.id.date);
        time = (TextView) itemView.findViewById(R.id.timer);
        open_stmt= (TextView) itemView.findViewById(R.id.open_stmt);
        amount= (TextView) itemView.findViewById(R.id.amount);
        mail = (TextView) itemView.findViewById(R.id.mail);
        contact = (TextView) itemView.findViewById(R.id.contact);
        midway_status=(TextView)itemView.findViewById(R.id.midway_status);
        author_image=(TextView) itemView.findViewById(R.id.author_image);

        str_from = resultp.get(RideSearch.from);
        str_to=resultp.get(RideSearch.to);
        str_date=resultp.get(RideSearch.date);
        mobileno=resultp.get(RideSearch.mobileno);
        username=resultp.get(RideSearch.username);
        String datestr=resultp.get(RideSearch.date);
        String iii=resultp.get("path");



        String[] splited = datestr.split(" ");


        fromaddress.setText(resultp.get(RideSearch.from));
        toaddress.setText(resultp.get(RideSearch.to));



       /* Log.e("tag","img_path"+resultp.get("path"));
        Picasso.with(context)
                .load(resultp.get("path"))
                .into(author_image);*/

       show_image=resultp.get("path");
        Log.e("tag","@@@"+show_image);


        date.setText(splited[0]);
        time.setText(splited[2]);
        //contact.setText(resultp.get(RideSearch.mobileno));
        //mail.setText(resultp.get(RideSearch.email));
        get_mailaddress=resultp.get(RideSearch.email);
        call_no=resultp.get(RideSearch.mobileno);
        Log.e("tag","1234567"+get_mailaddress);
        amount.setText(resultp.get(RideSearch.price));
        midway_status.setText(resultp.get(RideSearch.midwaydrop));
        Log.e("tag","dropppppp"+resultp.get(RideSearch.midwaydrop));
        String enable_status=resultp.get(RideSearch.phone);
        Log.e("tag","please check"+enable_status);

       // Log.e("tag","@@@@"+resultp.get(RideSearch.price));



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

        if(enable_status.equals("enabled"))
        {
            contact.setText(resultp.get("mobileno"));

            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    } else {

                        Log.e("tag","we"+call_no);
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
            contact.setText("hidden");
            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"You cant able to make call. Please contact through mail...",Toast.LENGTH_SHORT).show();
                }
            });
        }




        author_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(v.getRootView().getContext());
                View promptView = layoutInflater.inflate(R.layout.zoom_layout, null);
                final ImageView image = (ImageView) promptView.findViewById(R.id.image);
                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setCancelable(true);

                Picasso.with(context)
                        .load(resultp.get("path"))
                        .into(image);

                Log.e("tag","super"+resultp.get("path"));

               /* ViewPager mViewPager;
                CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(v.getRootView().getContext(),show_image);
                mViewPager = (ViewPager) promptView.findViewById(R.id.pager);
                mViewPager.setAdapter(mCustomPagerAdapter);
                mCustomPagerAdapter.notifyDataSetChanged();*/
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
                 emailIntent.putExtra(Intent.EXTRA_TEXT   , "Hi  "+username+"\n"+ "                   I would like to use your Ride.Please contact me.");
                 context.startActivity(emailIntent);
            }
        });



        return itemView;
    }






}