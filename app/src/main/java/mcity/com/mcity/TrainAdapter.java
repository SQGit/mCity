package mcity.com.mcity;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 06-10-2016.
 */
public class TrainAdapter extends BaseAdapter{
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public TrainAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        final TextView train_name, departure,arrival;
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "mont.ttf");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.train_list, parent, false);
        resultp = data.get(position);

        train_name = (TextView) itemView.findViewById(R.id.train_name);
        departure=(TextView) itemView.findViewById(R.id.departure);
        arrival = (TextView) itemView.findViewById(R.id.arrival);

        train_name.setText(resultp.get(TrainSearch.name));
        departure.setText(resultp.get(TrainSearch.departuretime));
        arrival.setText(resultp.get(TrainSearch.arrivaltime));


        train_name.setTypeface(tf);
        departure.setTypeface(tf);
        arrival.setTypeface(tf);

        return itemView;
    }
}
