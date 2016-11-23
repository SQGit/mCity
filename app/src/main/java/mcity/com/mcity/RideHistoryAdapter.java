package mcity.com.mcity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
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
    String URL = Data_Service.URL_API + "removeride";
    //ProgressBar progress;


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
        Log.e("tag","@@@1"+token);
        uid = sharedPreferences.getString("id","");
        Log.e("tag","@@@2"+uid);


        final TextView fromaddress, toaddress,to, date, open_stmt, mail, time, amount, contact,midway_status,delete_post;
        final ImageView author_image;

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.search_ride_history, parent, false);
        resultp = data.get(position);

        //progress=(ProgressBar)itemView.findViewById(R.id.progressBar);
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
        delete_post=(TextView) itemView.findViewById(R.id.delete_post);


        str_date=resultp.get(MyRideHistory.date);
        mobileno=resultp.get(MyRideHistory.mobileno);
        String datestr=resultp.get(MyRideHistory.date);
        String iii=resultp.get("path");
        String enable_status=resultp.get(MyRideHistory.mobileno);
        Log.e("tag","please check"+enable_status);

        String[] splited = datestr.split(" ");


        fromaddress.setText(resultp.get(MyRideHistory.from));
        toaddress.setText(resultp.get(MyRideHistory.to));
        mail.setText(resultp.get(MyRideHistory.email));
        contact.setText(resultp.get(MyRideHistory.mobileno));
        midway_status.setText(resultp.get(MyRideHistory.midwaydrop));

        str_id_main = resultp.get(MyRideHistory.id_main);
        Log.e("tag","id_1  "+str_id_main);
        str_id_sub=resultp.get(MyRideHistory.id_sub);
        Log.e("tag","id_2  "+str_id_sub);



        date.setText(splited[0]);
        time.setText(splited[2]);


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


        delete_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    resultp = data.get(position);

                    str_id_main = resultp.get(MyRideHistory.id_main);
                    str_id_sub=resultp.get(MyRideHistory.id_sub);

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
                    head1.setText("Exit");
                    head2.setText("Do You want to Logout?");

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
                postMethod.addHeader("x-access-token", token);
                postMethod.addHeader("id", uid);
                postMethod.addHeader("Content-Type", "application/x-www-form-urlencoded");


                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id1", str_id_main);
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

                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();

                    Intent i=new Intent(context,RideSearch.class);

                    Log.e("tag", "success");


                } else {
                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                    Log.e("tag", "error");


                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}