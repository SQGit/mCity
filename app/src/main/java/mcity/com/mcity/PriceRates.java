package mcity.com.mcity;

import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sloop.fonts.FontsManager;


/**
 * Created by Admin on 04-11-2016.
 */
public class PriceRates extends Fragment {

    TextView txt_pickup, txt_drop, txt_rates,txt_extn,txt_person; //head
    TextView txt_paranur,txt_p1,txt_p2,txt_p3,txt_p4,txt_p5,txt_p6,txt_p7,txt_p8,txt_p9,txt_p10,
            txt_p11,txt_p12,txt_p13,txt_p14,txt_p15,txt_p16,txt_p17,txt_p18,txt_p19,txt_p20;

    TextView txt_canopy,txt_c1,txt_c2,txt_c3,txt_c4,txt_c5,txt_c6,txt_c7,txt_c8,txt_c9,txt_c10,
            txt_c11,txt_c12,txt_c13,txt_c14,txt_c15,txt_c16;


   TextView txt_aqualily,txt_a1,txt_a2,txt_a3,txt_a4,txt_a5,txt_a6,txt_a7,txt_a8,txt_a9,txt_a10,
            txt_a11,txt_a12,txt_a13,txt_a14,txt_a15,txt_a16;


    TextView txt_capegemini,txt_ca1,txt_ca2,txt_ca3,txt_ca4;




    public PriceRates() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FontsManager.initFormAssets(getActivity(), "mont.ttf");
        FontsManager.changeFonts(getActivity());



        View view = inflater.inflate(R.layout.price_rates, container, false);

        FontsManager.changeFonts(getActivity());
        FontsManager.initFormAssets(getActivity(), "mont.ttf");

       txt_pickup = (TextView) view.findViewById(R.id.txt_pickup);
        txt_drop = (TextView) view.findViewById(R.id.txt_drop);
        txt_rates = (TextView) view.findViewById(R.id.txt_rates);
        txt_extn = (TextView) view.findViewById(R.id.txt_extn);
        txt_person = (TextView) view.findViewById(R.id.txt_person);

//paranur
        txt_paranur= (TextView) view.findViewById(R.id.txt_paranur);
        txt_p1= (TextView) view.findViewById(R.id.txt_p1);
        txt_p2= (TextView) view.findViewById(R.id.txt_p2);
        txt_p3= (TextView) view.findViewById(R.id.txt_p3);
        txt_p4= (TextView) view.findViewById(R.id.txt_p4);
        txt_p5= (TextView) view.findViewById(R.id.txt_p5);
        txt_p6= (TextView) view.findViewById(R.id.txt_p6);
        txt_p7= (TextView) view.findViewById(R.id.txt_p7);
        txt_p8= (TextView) view.findViewById(R.id.txt_p8);
        txt_p9= (TextView) view.findViewById(R.id.txt_p9);
        txt_p10= (TextView) view.findViewById(R.id.txt_p10);
        txt_p11= (TextView) view.findViewById(R.id.txt_p11);
        txt_p12= (TextView) view.findViewById(R.id.txt_p12);
        txt_p13= (TextView) view.findViewById(R.id.txt_p13);
        txt_p14= (TextView) view.findViewById(R.id.txt_p14);
        txt_p15= (TextView) view.findViewById(R.id.txt_p15);
        txt_p16= (TextView) view.findViewById(R.id.txt_p16);
        txt_p17= (TextView) view.findViewById(R.id.txt_p17);
        txt_p18= (TextView) view.findViewById(R.id.txt_p18);
        txt_p19= (TextView) view.findViewById(R.id.txt_p19);
        txt_p20= (TextView) view.findViewById(R.id.txt_p20);


        txt_canopy= (TextView) view.findViewById(R.id.txt_canopy);
        txt_c1= (TextView) view.findViewById(R.id.txt_c1);
        txt_c2= (TextView) view.findViewById(R.id.txt_c2);
        txt_c3= (TextView) view.findViewById(R.id.txt_c3);
        txt_c4= (TextView) view.findViewById(R.id.txt_c4);
        txt_c5= (TextView) view.findViewById(R.id.txt_c5);
        txt_c6= (TextView) view.findViewById(R.id.txt_c6);
        txt_c7= (TextView) view.findViewById(R.id.txt_c7);
        txt_c8= (TextView) view.findViewById(R.id.txt_c8);
        txt_c9= (TextView) view.findViewById(R.id.txt_c9);
        txt_c10= (TextView) view.findViewById(R.id.txt_c10);
        txt_c11= (TextView) view.findViewById(R.id.txt_c11);
        txt_c12= (TextView) view.findViewById(R.id.txt_c12);
        txt_c13= (TextView) view.findViewById(R.id.txt_c13);
        txt_c14= (TextView) view.findViewById(R.id.txt_c14);
        txt_c15= (TextView) view.findViewById(R.id.txt_c15);
        txt_c16= (TextView) view.findViewById(R.id.txt_c16);



       txt_aqualily= (TextView) view.findViewById(R.id.txt_aqualily);
        txt_a1= (TextView) view.findViewById(R.id.txt_a1);
        txt_a2= (TextView) view.findViewById(R.id.txt_a2);
        txt_a3= (TextView) view.findViewById(R.id.txt_a3);
        txt_a4= (TextView) view.findViewById(R.id.txt_a4);
        txt_a5= (TextView) view.findViewById(R.id.txt_a5);
        txt_a6= (TextView) view.findViewById(R.id.txt_a6);
        txt_a7= (TextView) view.findViewById(R.id.txt_a7);
        txt_a8= (TextView) view.findViewById(R.id.txt_a8);
        txt_a9= (TextView) view.findViewById(R.id.txt_a9);
        txt_a10= (TextView) view.findViewById(R.id.txt_a10);
        txt_a11= (TextView) view.findViewById(R.id.txt_a11);
        txt_a12= (TextView) view.findViewById(R.id.txt_a12);
        txt_a13= (TextView) view.findViewById(R.id.txt_a13);
        txt_a14= (TextView) view.findViewById(R.id.txt_a14);
        txt_a15= (TextView) view.findViewById(R.id.txt_a15);
        txt_a16= (TextView) view.findViewById(R.id.txt_a16);


        txt_capegemini= (TextView) view.findViewById(R.id.txt_capegemini);
        txt_ca1= (TextView) view.findViewById(R.id.txt_ca1);
        txt_ca2= (TextView) view.findViewById(R.id.txt_ca2);
        txt_ca3= (TextView) view.findViewById(R.id.txt_ca3);
        txt_ca4= (TextView) view.findViewById(R.id.txt_ca4);



        //content = (TextView) view.findViewById(R.id.content);




        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "mont.ttf");
        txt_pickup.setTypeface(tf);
        txt_drop.setTypeface(tf);
        txt_rates.setTypeface(tf);
        txt_extn.setTypeface(tf);
        txt_person.setTypeface(tf);

        //content.setTypeface(tf);
        txt_paranur.setTypeface(tf);
        txt_p1.setTypeface(tf);
        txt_p2.setTypeface(tf);
        txt_p3.setTypeface(tf);
        txt_p4.setTypeface(tf);
        txt_p5.setTypeface(tf);
        txt_p6.setTypeface(tf);
        txt_p7.setTypeface(tf);
        txt_p8.setTypeface(tf);
        txt_p9.setTypeface(tf);
        txt_p10.setTypeface(tf);
        txt_p11.setTypeface(tf);
        txt_p12.setTypeface(tf);
        txt_p13.setTypeface(tf);
        txt_p14.setTypeface(tf);
        txt_p15.setTypeface(tf);
        txt_p16.setTypeface(tf);
        txt_p17.setTypeface(tf);
        txt_p18.setTypeface(tf);
        txt_p19.setTypeface(tf);
        txt_p20.setTypeface(tf);




        txt_canopy.setTypeface(tf);
        txt_c1.setTypeface(tf);
        txt_c2.setTypeface(tf);
        txt_c3.setTypeface(tf);
        txt_c4.setTypeface(tf);
        txt_c5.setTypeface(tf);
        txt_c6.setTypeface(tf);
        txt_c7.setTypeface(tf);
        txt_c8.setTypeface(tf);
        txt_c9.setTypeface(tf);
        txt_c10.setTypeface(tf);
        txt_c11.setTypeface(tf);
        txt_c12.setTypeface(tf);
        txt_c13.setTypeface(tf);
        txt_c14.setTypeface(tf);
        txt_c15.setTypeface(tf);
        txt_c16.setTypeface(tf);



        txt_aqualily.setTypeface(tf);
        txt_a1.setTypeface(tf);
        txt_a2.setTypeface(tf);
        txt_a3.setTypeface(tf);
        txt_a4.setTypeface(tf);
        txt_a5.setTypeface(tf);
        txt_a6.setTypeface(tf);
        txt_a7.setTypeface(tf);
        txt_a8.setTypeface(tf);
        txt_a9.setTypeface(tf);
        txt_a10.setTypeface(tf);
        txt_a11.setTypeface(tf);
        txt_a12.setTypeface(tf);
        txt_a13.setTypeface(tf);
        txt_a14.setTypeface(tf);
        txt_a15.setTypeface(tf);
        txt_a16.setTypeface(tf);


        txt_capegemini.setTypeface(tf);
        txt_ca1.setTypeface(tf);
        txt_ca2.setTypeface(tf);
        txt_ca3.setTypeface(tf);
        txt_ca4.setTypeface(tf);


        return view;
    }

    private class PriceRatesAsync extends AsyncTask<String,String,String>{


        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);

        }


        @Override
        protected String doInBackground(String... params) {



            return null;
        }
    }



}