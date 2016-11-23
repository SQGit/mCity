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
import android.telephony.SmsManager;
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
public class MyHouseAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    Bitmap b;
    HashMap<String, String> resultp = new HashMap<String, String>();
    ArrayList<String> loading ;
    String str_main_id, str_sub_id,str_token,str_userid,str_tokenid,str_fieldid;
    String URL = Data_Service.URL_API + "removerent";

    public MyHouseAdapter(Context ctx, ArrayList<HashMap<String, String>> arraylist) {
        this.ctx = ctx;
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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        str_token = sharedPreferences.getString("token", "");
        str_userid = sharedPreferences.getString("id","");


        TextView txt_deletehouse, txt_bedroom, txt_rentType, txt_furnishedType, txt_address, txt_subtype, txt_bedtv, txt_renttv, txt_subtv, txt_furnishedtv, txt_make_call,
                txt_send_mail, bedtypes, loc, rs, mcity, city;


        Typeface tf = Typeface.createFromAsset(ctx.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.myhouse_adapter, parent, false);
        resultp = data.get(position);
        String str_sms;

        loading = new ArrayList<>();

        txt_deletehouse = (TextView) itemView.findViewById(R.id.deletehouse);
        txt_address = (TextView) itemView.findViewById(R.id.rent);
        txt_subtype = (TextView) itemView.findViewById(R.id.subtype);
        txt_rentType = (TextView) itemView.findViewById(R.id.renttype);
        txt_furnishedType = (TextView) itemView.findViewById(R.id.furnishedtype);
        loc = (TextView) itemView.findViewById(R.id.loc);
        rs = (TextView) itemView.findViewById(R.id.rs);
        mcity = (TextView) itemView.findViewById(R.id.mcity);
        city = (TextView) itemView.findViewById(R.id.city);
        bedtypes = (TextView) itemView.findViewById(R.id.bedtypes);
        txt_bedroom = (TextView) itemView.findViewById(R.id.bedroom);
        txt_bedtv = (TextView) itemView.findViewById(R.id.bedroom_tv);
        txt_renttv = (TextView) itemView.findViewById(R.id.rent_tv);
        txt_subtv = (TextView) itemView.findViewById(R.id.subtype_tv);
        txt_furnishedtv = (TextView) itemView.findViewById(R.id.furnishedtype_tv);
        LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.view);


        txt_address.setText(resultp.get(HouseHistory.monthlyrent));
        bedtypes.setText(resultp.get(HouseHistory.bedroom));
        loc.setText(resultp.get(HouseHistory.location));
        txt_bedroom.setText(resultp.get(HouseHistory.bedroom));
        txt_rentType.setText(resultp.get(HouseHistory.renttype));
        txt_subtype.setText(resultp.get(HouseHistory.furnishedtype));
        txt_furnishedType.setText(resultp.get(HouseHistory.residential));



        txt_address.setTypeface(tf);
        txt_rentType.setTypeface(tf);
        //description.setTypeface(tf);
        bedtypes.setTypeface(tf);
        loc.setTypeface(tf);
        rs.setTypeface(tf);
        mcity.setTypeface(tf);
        city.setTypeface(tf);
        txt_furnishedType.setTypeface(tf);
        txt_bedroom.setTypeface(tf);
        txt_subtype.setTypeface(tf);
        txt_bedtv.setTypeface(tf);
        txt_renttv.setTypeface(tf);
        txt_furnishedtv.setTypeface(tf);
        txt_subtv.setTypeface(tf);



        txt_deletehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp = data.get(position);

                str_tokenid=resultp.get(HouseHistory.id_main);
                str_fieldid=resultp.get(HouseHistory.id_sub);

                deletehome();

            }
        });




        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(ctx.getResources().getColor(R.color.bg1));

        } else {
            linearLayout.setBackgroundColor(ctx.getResources().getColor(R.color.bg2));
        }

        return itemView;
    }

    private void deletehome() {
        LayoutInflater layoutInflater = LayoutInflater.from(this.ctx);
        View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
        final AlertDialog alert_msg1 = new AlertDialog.Builder(this.ctx).create();
        alert_msg1.setCancelable(false);
        Window window = alert_msg1.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView head1 = (TextView) promptView.findViewById(R.id.head1);
        final TextView head2 = (TextView) promptView.findViewById(R.id.head2);
        final ImageView no = (ImageView) promptView.findViewById(R.id.no);
        final ImageView yes = (ImageView) promptView.findViewById(R.id.yes);

        Typeface tf = Typeface.createFromAsset(ctx.getAssets(), "mont.ttf");
        head1.setTypeface(tf);
        head1.setText("Exit");
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
            String id = "";
            try {

                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost();
                postMethod.addHeader("x-access-token", str_token);
                postMethod.addHeader("id", str_userid);
                postMethod.addHeader("Content-Type", "application/x-www-form-urlencoded");


                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id1", str_tokenid);
                jsonObject.accumulate("id2", str_fieldid);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(URL, json, str_userid, str_token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;

        }


        @Override
        protected void onPostExecute(String jsonstr) {
            Log.e("tag", "<-----result---->" + jsonstr);
            //progress.setVisibility(View.GONE);
            super.onPostExecute(jsonstr);

            try {
                JSONObject jo = new JSONObject(jsonstr);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.e("tag", "<-----Status----->" + status);
                Log.e("tag", "<-----msg----->" + msg);

                if (status.equals("true")) {

                    Log.e("tag", "success");
                    Intent intent = new Intent(ctx,MPostHistory.class);
                    ctx.startActivity(intent);

                    Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();


                }

                else {

                    Log.e("tag", "error");
                    Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}



