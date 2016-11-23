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


public class AdvancedRideAdapter extends BaseAdapter {


    String str_from,str_to,str_date,str_mobileno,str_username,str_get_mailaddress,str_call_no,str_show_image;
    TextView txt_author_image;
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    ArrayList<String> loading = new ArrayList<>();
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


    public AdvancedRideAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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

        final TextView txt_fromaddress, txt_toaddress,txt_to, txt_date, txt_open_stmt, txt_mail, txt_time, txt_amount, txt_contact,txt_midway_status;

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.search_ride_list, parent, false);
        resultp = data.get(position);

        txt_fromaddress = (TextView) itemView.findViewById(R.id.fromaddress);
        txt_to=(TextView) itemView.findViewById(R.id.to);
        txt_toaddress = (TextView) itemView.findViewById(R.id.toaddress);
        txt_date = (TextView) itemView.findViewById(R.id.date);
        txt_time = (TextView) itemView.findViewById(R.id.timer);
        txt_open_stmt= (TextView) itemView.findViewById(R.id.open_stmt);
        txt_amount= (TextView) itemView.findViewById(R.id.amount);
        txt_mail = (TextView) itemView.findViewById(R.id.mail);
        txt_contact = (TextView) itemView.findViewById(R.id.contact);
        txt_midway_status=(TextView)itemView.findViewById(R.id.midway_status);
        txt_author_image=(TextView) itemView.findViewById(R.id.author_image);


        str_from = resultp.get("from");
        str_to=resultp.get("to");
        str_date=resultp.get("date");
        str_mobileno=resultp.get("mobileno");
        String datestr=resultp.get("date");


        String[] splited = datestr.split(" ");
        txt_fromaddress.setText(resultp.get("from"));
        txt_toaddress.setText(resultp.get("to"));
        str_show_image=resultp.get("path");



        txt_date.setText(splited[0]);
        txt_time.setText(splited[2]);
        str_get_mailaddress=resultp.get(RideSearch.email);
        str_call_no=resultp.get(RideSearch.mobileno);
        txt_amount.setText(resultp.get(RideSearch.price));
        txt_midway_status.setText(resultp.get(RideSearch.midwaydrop));
        String enable_status=resultp.get(RideSearch.phone);


        txt_fromaddress.setTypeface(tf);
        txt_to.setTypeface(tf);
        txt_toaddress.setTypeface(tf);
        txt_date.setTypeface(tf);
        txt_time.setTypeface(tf);
        txt_open_stmt.setTypeface(tf);
        txt_amount.setTypeface(tf);
        txt_mail.setTypeface(tf);
        txt_contact.setTypeface(tf);
        txt_midway_status.setTypeface(tf);
        txt_author_image.setTypeface(tf);

            if(enable_status.equals("enabled"))
            {
                txt_contact.setText(resultp.get("mobileno"));

                txt_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                        }
                        else {
                            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                            phoneIntent.setData(Uri.parse("tel:"+str_call_no));

                            try {
                                v.getContext().startActivity(phoneIntent);
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(v.getContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        else
        {
            txt_contact.setText("hidden");
            txt_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"You cant able to make call. Please contact through mail...",Toast.LENGTH_SHORT).show();
                }
            });
        }


        txt_author_image.setOnClickListener(new View.OnClickListener() {
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
                alertbox.setView(promptView);
                alertbox.show();
            }
        });

        txt_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", str_get_mailaddress, null));
                 emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MCity"+ "  "+str_from+" to "+str_to+"  "+str_date);
                 emailIntent.putExtra(Intent.EXTRA_TEXT   , "Hi  "+str_username+"\n"+ "                   I would like to use your Ride.Please contact me.");
                 context.startActivity(emailIntent);
            }
        });
        return itemView;
    }
}