package mcity.com.mcity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ANANDH on 22-12-2015.
 */
public class HouseAdapter extends BaseAdapter {


    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    Bitmap b;
    HashMap<String, String> resultp = new HashMap<String, String>();
    String enable_status,img1, img2, img3, img4;
    ArrayList<String> loading ;
    String str_cusno, str_owner_no,str_owner_mail,str_username;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    public HouseAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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
        TextView txt_description, txt_bedroom, txt_rentType, txt_furnishedType, txt_monthrent, txt_subtype, txt_bedtv, txt_renttv, txt_subtv, txt_furnishedtv, txt_make_call,
                txt_send_mail, txt_bedtypes, txt_loc, txt_rs, txt_mcity, txt_city;

        SharedPreferences sharedPreferences;
        SmsManager smsManager = SmsManager.getDefault();
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.search_properties_list_items, parent, false);
        resultp = data.get(position);



        Log.e("tag","11111"+resultp);
        loading = new ArrayList<>();

        img1 = resultp.get("path0");
        img2 = resultp.get("path1");
        img3 = resultp.get("path2");
        img4 = resultp.get("path3");

        loading.add(img1);
        loading.add(img2);
        loading.add(img3);
        loading.add(img4);

        txt_monthrent = (TextView) itemView.findViewById(R.id.rent);
        txt_subtype = (TextView) itemView.findViewById(R.id.subtype);
        txt_rentType = (TextView) itemView.findViewById(R.id.renttype);
        txt_furnishedType = (TextView) itemView.findViewById(R.id.furnishedtype);
        txt_loc = (TextView) itemView.findViewById(R.id.loc);
        txt_rs = (TextView) itemView.findViewById(R.id.rs);
        txt_mcity = (TextView) itemView.findViewById(R.id.mcity);
        txt_city = (TextView) itemView.findViewById(R.id.city);
        txt_bedtypes = (TextView) itemView.findViewById(R.id.bedtypes);
        txt_bedroom = (TextView) itemView.findViewById(R.id.bedroom);
        txt_description = (TextView) itemView.findViewById(R.id.description);
        txt_bedtv = (TextView) itemView.findViewById(R.id.bedroom_tv);
        txt_renttv = (TextView) itemView.findViewById(R.id.rent_tv);
        txt_subtv = (TextView) itemView.findViewById(R.id.subtype_tv);
        txt_furnishedtv = (TextView) itemView.findViewById(R.id.furnishedtype_tv);
        txt_make_call = (TextView) itemView.findViewById(R.id.make_call);
        txt_send_mail = (TextView) itemView.findViewById(R.id.send_mail);
        LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.view);
        final ImageView loadimage = (ImageView) itemView.findViewById(R.id.loadimg);


        Log.e("tag","1222222"+resultp.get("address"));
        Log.e("tag","333333"+resultp.get("bedroom"));
        txt_monthrent.setText(resultp.get("monthlyrent"));
        txt_bedtypes.setText(resultp.get("bedroom"));
        txt_loc.setText(resultp.get("location"));
        txt_bedroom.setText(resultp.get("bedroom"));
        txt_rentType.setText(resultp.get("renttype"));
        txt_subtype.setText(resultp.get("residential"));
        txt_furnishedType.setText(resultp.get("furnishedtype"));
        txt_description.setText(resultp.get("description"));

        str_owner_no = resultp.get("mobileno");
        str_owner_mail=resultp.get("email");
        str_username=resultp.get("username");


         enable_status=resultp.get("phone");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        str_cusno = sharedPreferences.getString("mobileno", "");






        Picasso.with(context)
                .load(resultp.get("path0"))
                .into(loadimage);
        Log.e("tag","image_checking"+resultp.get("path0"));


        txt_monthrent.setTypeface(tf);
        txt_rentType.setTypeface(tf);
        txt_description.setTypeface(tf);
        txt_bedtypes.setTypeface(tf);
        txt_loc.setTypeface(tf);
        txt_rs.setTypeface(tf);
        txt_mcity.setTypeface(tf);
        txt_city.setTypeface(tf);
        txt_furnishedType.setTypeface(tf);
        txt_bedroom.setTypeface(tf);
        txt_subtype.setTypeface(tf);
        txt_bedtv.setTypeface(tf);
        txt_renttv.setTypeface(tf);
        txt_furnishedtv.setTypeface(tf);
        txt_make_call.setTypeface(tf);
        txt_send_mail.setTypeface(tf);
        txt_subtv.setTypeface(tf);


        txt_make_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp = data.get(position);
                str_owner_no = resultp.get("mobileno");
                Log.e("tag", "check_phoneno" + str_owner_no);
                Log.e("tag", "check_enable" + enable_status);
                if(enable_status.equals("enabled")) {


                    int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    } else {

                        Log.e("tag","we"+str_owner_no);
                        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                        phoneIntent.setData(Uri.parse("tel:"+str_owner_no));

                        try {
                            v.getContext().startActivity(phoneIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(v.getContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                    else
                {
                    Toast.makeText(v.getContext(), "You cant able to make call. Please contact through mail...",Toast.LENGTH_LONG).show();
                }
            }
        });





        txt_send_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", str_owner_mail, null));

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MCity");
                emailIntent.putExtra(Intent.EXTRA_TEXT   , "Hi  "+str_username+"\n"+ "   I am interested in your Property. Please contact me.");
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(emailIntent);

            }
        });


        loadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = new ArrayList<>();
                resultp = data.get(position);

                for(int i =0;i<4;i++){
                    if(resultp.get("path"+i)!= null){
                        loading.add(resultp.get("path"+i));
                    }
                }

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
        });

        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.bg1));

        } else {
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.bg2));
        }

        return itemView;
    }
    }

