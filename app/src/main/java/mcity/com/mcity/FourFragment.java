package mcity.com.mcity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Admin on 03-11-2016.
 */
public class FourFragment extends Fragment{
TextView tv;
    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_four, container, false);
        tv=(TextView)view.findViewById(R.id.txt_expired);


        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "mont.ttf");

        tv.setTypeface(tf);

        // Inflate the layout for this fragment
        return view;
    }

}
