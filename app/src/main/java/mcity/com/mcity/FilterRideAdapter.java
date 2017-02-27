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
 * Created by Admin on 05-02-2017.
 */
public class FilterRideAdapter extends BaseAdapter{

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    ArrayList<String> loading = new ArrayList<>();
    String split, get_mailaddress,call_no,show_image;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    String str_from,str_to,str_date,mobileno,username,str_persons;
    TextView author_image;


    public FilterRideAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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
        final String lic_image,return_date,go_date;
        //final ImageView author_image;

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.search_ride_list, viewGroup, false);
        resultp = data.get(position);
        LinearLayout lnr_round_trip;


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
        author_image=(TextView) itemView.findViewById(R.id.license_image);
        lnr_round_trip=(LinearLayout)itemView.findViewById(R.id.lnr_round_trip);
        date1 = (TextView) itemView.findViewById(R.id.date1);
        date2 = (TextView) itemView.findViewById(R.id.date2);
        date_head = (TextView) itemView.findViewById(R.id.round_head);

        str_from = resultp.get(RideSearch.from);
        str_to=resultp.get(RideSearch.to);
        str_date=resultp.get(RideSearch.date);
        mobileno=resultp.get(RideSearch.mobileno);
        username=resultp.get(RideSearch.username);
        str_date=resultp.get(RideSearch.date);
        Log.e("tag","www"+str_date);
        lic_image=resultp.get("path");
        Log.e("tag","zooo"+lic_image);
        str_persons=resultp.get(RideSearch.noofpersons);

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
        fromaddress.setText(resultp.get(RideSearch.from));
        toaddress.setText(resultp.get(RideSearch.to));
        open_stmt.setText(str_persons+" Seat Available");


   /*Log.e("tag","img_path"+resultp.get("path"));
        Picasso.with(context)
                .load(resultp.get("path"))
                .into(author_image);*/



        date.setText(splited[0]);
        time.setText(splited[2]+splited[3]);

        get_mailaddress=resultp.get(RideSearch.email);
        call_no=resultp.get(RideSearch.mobileno);
        Log.e("tag","1234567"+get_mailaddress);
        amount.setText(resultp.get(RideSearch.price));
        midway_status.setText(resultp.get(RideSearch.midwaydrop));
        Log.e("tag","dropppppp"+resultp.get(RideSearch.midwaydrop));
        String enable_status=resultp.get(RideSearch.phone);


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
            //contact.setText(resultp.get("Make Call"));

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
                    Toast.makeText(v.getContext(),"You can't able to make call. Please contact through mail...",Toast.LENGTH_SHORT).show();
                }
            });
        }



        author_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag", "i233" + lic_image);
                final Dialog dialog = new Dialog(context);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.samp);


                ImageView cross = (ImageView) dialog.findViewById(R.id.cross);
                ImageView menu_list = (ImageView) dialog.findViewById(R.id.menu_list);


                Picasso.with(context)
                        .load(lic_image)
                        .into(menu_list);


                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                dialog.show();
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
