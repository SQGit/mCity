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
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 21-01-2017.
 */
public class Myselladapter extends BaseAdapter {
    String URL = Data_Service.URL_API + "removesell";
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    String img1,img2,img3,img4,str_id_two,token,id;
    Bitmap b;
    HashMap<String, String> resultp_garage = new HashMap<String, String>();
    ArrayList<String> loading ;
    String str_owner_number,str_owner_mail,str_owner_name,enable_status;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


    public Myselladapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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
    public View getView(final int position, View view, final ViewGroup viewGroup) {
        final TextView txt_category, txt_model, txt_price, txt_color,  txt_kms,description;
        TextView txt_model_h,txt_price_h,txt_color_h,txt_kms_h;
        final String str_model,str_color,str_km,str_id2;
        TextView deletehouse,edithouse;



        SharedPreferences sharedPreferences;


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        token = sharedPreferences.getString("token", "");
        id = sharedPreferences.getString("id", "");


        SmsManager smsManager = SmsManager.getDefault();
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.mysell_history_adapter, viewGroup, false);
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
        deletehouse=(TextView)itemView.findViewById(R.id.deletehouse);
        //edithouse=(TextView)itemView.findViewById(R.id.edithouse);

        txt_model_h = (TextView) itemView.findViewById(R.id.txt_model_h);
        txt_price_h = (TextView) itemView.findViewById(R.id.txt_price_h);
        txt_color_h = (TextView) itemView.findViewById(R.id.txt_color_h);
        txt_kms_h = (TextView) itemView.findViewById(R.id.txt_kms_h);



        LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.view);
        final ImageView loadimage = (ImageView) itemView.findViewById(R.id.loadimg);
        str_id2=resultp_garage.get("_id2");
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
        deletehouse.setTypeface(tf);
        //edithouse.setTypeface(tf);
        description.setTypeface(tf);




        deletehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultp_garage = data.get(position);
                str_id_two=resultp_garage.get("_id");
                deleteAds();
            }
        });


       /* edithouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String get_str_categ=txt_category.getText().toString();
                String get_str_price=txt_price.getText().toString();
                String get_str_model=txt_model.getText().toString();
                String get_str_color=txt_color.getText().toString();
                String get_str_desc=description.getText().toString();
                String get_str_kms=txt_kms.getText().toString();

                Intent intent = new Intent(view.getContext(), MGarage_Edit_PostAds.class);

                intent.putExtra("key1",get_str_categ);
                intent.putExtra("key2",get_str_price);
                intent.putExtra("key3",get_str_model);
                intent.putExtra("key4",get_str_color);
                intent.putExtra("key5",get_str_desc);
                intent.putExtra("key6",get_str_kms);
                intent.putExtra("key7",str_id2);
                context.startActivity(intent);

                Log.e("tag","cccccc"+get_str_categ+get_str_price+get_str_model+get_str_color+get_str_desc);
            }
        });
*/

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

    private void deleteAds() {

        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alert_msg1 = new AlertDialog.Builder(this.context).create();
        alert_msg1.setCancelable(false);
        Window window = alert_msg1.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.head1);
        final TextView head2 = (TextView) promptView.findViewById(R.id.head2);
        final ImageView no = (ImageView) promptView.findViewById(R.id.no);
        final ImageView yes = (ImageView) promptView.findViewById(R.id.yes);

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        head1.setTypeface(tf);
        head2.setTypeface(tf);
        head1.setText("DELETE?");
        head2.setText("Do You want to Delete Post?");

        yes.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                new DeleteHouseAsync().execute();
                alert_msg1.dismiss();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert_msg1.dismiss();
            }
        });
        alert_msg1.setView(promptView);
        alert_msg1.show();
    }

    private class DeleteHouseAsync extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            super.onPreExecute();
            //progress.setVisibility(View.VISIBLE);
            Log.e("tag", "two");


        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";

            try {

                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost();
                postMethod.addHeader("x-access-token", token);
                postMethod.addHeader("id", id);
                postMethod.addHeader("Content-Type", "application/x-www-form-urlencoded");

                Log.e("tag","checking"+id);
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("id2", str_id_two);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(URL, json, id, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;

        }


        @Override
        protected void onPostExecute(String jsonstr) {
            super.onPostExecute(jsonstr);

            try {
                JSONObject jo = new JSONObject(jsonstr);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.e("tag", "<-----Status----->" + status);
                Log.e("tag", "<-----msg----->" + msg);

                if (status.equals("true")) {
                    Log.e("tag", "success");
                    Intent intent = new Intent(context,MyAddHistory.class);
                    context.startActivity(intent);
                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}

