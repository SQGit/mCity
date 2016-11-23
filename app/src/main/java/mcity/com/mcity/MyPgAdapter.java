package mcity.com.mcity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
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

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 25-10-2016.
 */
public class MyPgAdapter extends BaseAdapter {

    String URL = Data_Service.URL_API + "removeroom";
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    Bitmap b;
    HashMap<String, String> resultp = new HashMap<String, String>();
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    String str_token,str_uid,str_main_id,str_sub_id;


    public MyPgAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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
        str_uid = sharedPreferences.getString("id","");

        TextView txt_loc,txt_roomTypeval,txt_address,txt_rent,txt_chennai,txt_mcity,txt_rs,txt_subtype, txt_roomtype, txt_description,txt_rentType,txt_gender,txt_furnishedType,txt_monthrent,txt_bedtv,txt_renttv,txt_subtv,txt_furnishedtv,txt_sendsms,txt_viewcontact;
        TextView txt_delete, txt_send_mail;
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");


        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.mypgadapter, parent, false);
        resultp = data.get(position);


        txt_loc = (TextView) itemView.findViewById(R.id.loc);
        txt_address = (TextView) itemView.findViewById(R.id.city);
        txt_rent=(TextView) itemView.findViewById(R.id.rents);
        txt_subtype=(TextView) itemView.findViewById(R.id.subtype);
        txt_chennai=(TextView) itemView.findViewById(R.id.chennai);
        txt_rs=(TextView) itemView.findViewById(R.id.rs);
        txt_mcity = (TextView) itemView.findViewById(R.id.mcity);
        txt_gender =(TextView)itemView.findViewById(R.id.gender);
        txt_delete = (TextView)itemView.findViewById(R.id.delete);
        txt_renttv = (TextView) itemView.findViewById(R.id.rent_tv);
        txt_subtv = (TextView) itemView.findViewById(R.id.subtype_tv);
        txt_furnishedtv = (TextView) itemView.findViewById(R.id.furnishedtype_tv);
        LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.view);
        txt_roomTypeval=(TextView) itemView.findViewById(R.id.roomTypeval);


        txt_loc.setText(resultp.get(SearchPGFilter.location));
        txt_rent.setText(resultp.get(SearchPGFilter.monthlyrent));
        txt_subtype.setText(resultp.get(SearchPGFilter.monthlyrent));
        txt_gender.setText(resultp.get(SearchPGFilter.gender));
        txt_roomTypeval.setText(resultp.get(SearchPGFilter.roomtype));
        String enable_status=resultp.get(SearchPGFilter.phone);


        txt_chennai.setTypeface(tf);
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

        txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp = data.get(position);
                str_main_id=resultp.get(PGHistory.main_id);
                str_sub_id=resultp.get(PGHistory.sub_id);
                deletepg();
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

    private void deletepg() {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final android.app.AlertDialog alertD = new android.app.AlertDialog.Builder(context).create();
        alertD.setCancelable(false);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.head1);
        final TextView head2 = (TextView) promptView.findViewById(R.id.head2);
        final ImageView no = (ImageView) promptView.findViewById(R.id.no);
        final ImageView yes = (ImageView) promptView.findViewById(R.id.yes);

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        head1.setTypeface(tf);
        head1.setText("Delete Post...");
        head2.setText("Do You want to delete this post?");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeletePgAsync().execute();
                alertD.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });
        alertD.setView(promptView);
        alertD.show();
    }



    private class DeletePgAsync extends AsyncTask<String, String, String> {
        @Override


        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            String id = "";
            try {

                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost();
                postMethod.addHeader("x-access-token", str_token);
                postMethod.addHeader("id", str_uid);
                postMethod.addHeader("Content-Type", "application/x-www-form-urlencoded");


                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id1", str_main_id);
                jsonObject.accumulate("id2", str_sub_id);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(URL, json, str_uid, str_token);
            } catch (Exception e) {

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


                if (status.equals("true")) {

                    Intent intent = new Intent(context,MPostHistory.class);
                    context.startActivity(intent);

                }

                else {

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}

