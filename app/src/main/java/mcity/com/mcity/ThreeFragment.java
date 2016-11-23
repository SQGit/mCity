package mcity.com.mcity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Admin on 03-11-2016.
 */
public class ThreeFragment extends Fragment{
    TextView tv;

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_three, container, false);
        tv=(TextView)view.findViewById(R.id.tv);


        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "mont.ttf");

        tv.setTypeface(tf);





        // Inflate the layout for this fragment
        return view;
    }

}
