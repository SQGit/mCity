package mcity.com.mcity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 24-10-2016.
 */
public class RideHistoryAdapter extends BaseAdapter{
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    String str_id_main,str_id_sub,str_date,mobileno, token, uid;
    SharedPreferences prefs;
    String URL = Data_Service.URL_API + "removeridenew";
    ProgressBar progressBar;
    Dialog dialog2;


    public RideHistoryAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }


    @Override
    public int getCount(){return data.size();
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
        token = sharedPreferences.getString("token", "");
        uid = sharedPreferences.getString("id","");

        dialog2 = new Dialog(context);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.test_loader1);
        progressBar = (ProgressBar) dialog2.findViewById(R.id.loading_spinner);

        final TextView fromaddress, toaddress,to, date, open_stmt, time, amount,midway_status,delete_post,edit_post,date1,date2,date_head;
        final ImageView author_image;
        final LinearLayout lnr_round_trip;
        String str_persons,return_date,go_date;

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.search_ride_history, parent, false);
        resultp = data.get(position);


        fromaddress = (TextView) itemView.findViewById(R.id.fromaddress);
        to=(TextView) itemView.findViewById(R.id.to);
        toaddress = (TextView) itemView.findViewById(R.id.toaddress);
        date = (TextView) itemView.findViewById(R.id.date);
        time = (TextView) itemView.findViewById(R.id.timer);
        //edit_post=(TextView)itemView.findViewById(R.id.edit_post);
        open_stmt= (TextView) itemView.findViewById(R.id.open_stmt);
        amount= (TextView) itemView.findViewById(R.id.amount);
        midway_status=(TextView)itemView.findViewById(R.id.midway_status);
        delete_post=(TextView) itemView.findViewById(R.id.delete_post);
        date1 = (TextView) itemView.findViewById(R.id.date1);
        date2 = (TextView) itemView.findViewById(R.id.date2);
        date_head = (TextView) itemView.findViewById(R.id.round_head);
        lnr_round_trip=(LinearLayout)itemView.findViewById(R.id.lnr_round_trip);


        str_date=resultp.get(MyRideHistory.date);
        mobileno=resultp.get(MyRideHistory.mobileno);
        String datestr=resultp.get(MyRideHistory.date);
        String iii=resultp.get("path");
        String enable_status=resultp.get(MyRideHistory.mobileno);
        return_date=resultp.get(MyRideHistory.returndate);
        go_date=resultp.get(MyRideHistory.godate);


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

        fromaddress.setText(resultp.get(MyRideHistory.from));
        toaddress.setText(resultp.get(MyRideHistory.to));
        midway_status.setText(resultp.get(MyRideHistory.midwaydrop));
        amount.setText(resultp.get(MyRideHistory.price));
        str_persons=resultp.get(MyRideHistory.noofpersons);
        open_stmt.setText(str_persons+" Seat Availabale");
        str_id_sub=resultp.get("_id");
        date.setText(splited[0]);
        time.setText(splited[2]+splited[3]);

        fromaddress.setTypeface(tf);
        to.setTypeface(tf);
        toaddress.setTypeface(tf);
        date.setTypeface(tf);
        time.setTypeface(tf);
        open_stmt.setTypeface(tf);
        amount.setTypeface(tf);
        midway_status.setTypeface(tf);
        date1.setTypeface(tf);
        date2.setTypeface(tf);
        date_head.setTypeface(tf);

       /* edit_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/


        delete_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    resultp = data.get(position);
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    View promptView = layoutInflater.inflate(R.layout.exitdialog, null);
                    final AlertDialog alertD = new AlertDialog.Builder(context).create();
                    alertD.setCancelable(false);
                    Window window = alertD.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    final TextView head1 = (TextView) promptView.findViewById(R.id.head1);
                    final TextView head2 = (TextView) promptView.findViewById(R.id.head2);
                    final ImageView no = (ImageView) promptView.findViewById(R.id.no);
                    final ImageView yes = (ImageView) promptView.findViewById(R.id.yes);

                    Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
                    head1.setTypeface(tf);
                    head2.setTypeface(tf);

                    head1.setText("DELETE");
                    head2.setText("Do You want to Delete?");

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new DeleteRideAsync().execute();
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
            });
        return itemView;
    }


    private class DeleteRideAsync extends AsyncTask<String, String, String> {
        @Override

        protected void onPreExecute() {
            dialog2.show();
            super.onPreExecute();
        }
        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost();
                postMethod.addHeader("x-access-token", token);
                postMethod.addHeader("id", uid);
                postMethod.addHeader("Content-Type", "application/x-www-form-urlencoded");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id2", str_id_sub);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(URL, json, uid, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(String jsonstr) {
            dialog2.dismiss();
            super.onPostExecute(jsonstr);

            try {
                JSONObject jo = new JSONObject(jsonstr);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.e("tag", "<-----Status----->" + status);
                Log.e("tag", "<-----msg----->" + msg);

                if (status.equals("true")) {

                    Intent i = new Intent(context, MyRideHistory.class);
                    context.startActivity(i);

                } else {
                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}