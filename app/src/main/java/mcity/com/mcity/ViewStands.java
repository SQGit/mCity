package mcity.com.mcity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;


import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sloop.fonts.FontsManager;

import java.util.ArrayList;
import java.util.List;
import android.location.Location;
import android.location.LocationListener;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Admin on 04-11-2016.
 */


public class ViewStands extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    MapView mMapView;
    private GoogleMap googleMap,map;
    MarkerOptions marker;
    private static final LatLng MAHINDRA_WORLD_CITY = new LatLng(12.7308, 79.9839);
    private static final LatLng VANDALUR = new LatLng(12.8928, 80.0808);
    private static final LatLng TAMBARAM = new LatLng(12.9229, 80.1275);
    private static final LatLng INFOSYS = new LatLng(13.0604, 80.2496);
    TextView t1, t2, t3;
    LinearLayout lin_call;
    List<PlaceDetails> placedet;
    TextView txt_no1, txt_no2;
    DatabaseHandler db;
    LinearLayout lin_hide;
    String str_img1,str_img2,str_img3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION=2;
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION=3;

    TextView txt_drivername1,txt_drivername2,txt_drivername3,txt_driverno1,txt_driverno2,txt_driverno3;
    ImageView btn_call_driver1,btn_call_driver2,btn_call_driver3;

    public ViewStands() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_stands, container, false);


        FontsManager.initFormAssets(getActivity(), "mont.ttf");
        FontsManager.changeFonts(getActivity());

        t1 = (TextView) view.findViewById(R.id.txt_station);
        t2 = (TextView) view.findViewById(R.id.txt_description);
        t3 = (TextView) view.findViewById(R.id.txt_time);
        lin_call=(LinearLayout) view.findViewById(R.id.lin_call);
        lin_hide=(LinearLayout) view.findViewById(R.id.lin_hide);
        lin_hide.setVisibility(View.GONE);

        txt_no1 = (TextView) view.findViewById(R.id.txt_no1);
        //txt_no2=(TextView) view.findViewById(R.id.txt_no2);


        db = new DatabaseHandler(getActivity());
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "mont.ttf");
        t1.setTypeface(tf);
        t2.setTypeface(tf);
        t3.setTypeface(tf);

        txt_no1.setTypeface(tf);


        lin_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.auto_driver);

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String driver_name1 = sharedPreferences.getString("d1", "");
                String driver_name2 = sharedPreferences.getString("d2", "");
                String driver_name3 = sharedPreferences.getString("d3", "");

                String driver_phone1 = sharedPreferences.getString("ph1", "");
                String driver_phone2 = sharedPreferences.getString("ph2", "");
                String driver_phone3 = sharedPreferences.getString("ph3", "");

                //final ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
                final ImageView img_cross=(ImageView) dialog.findViewById(R.id.img_cross);


                txt_drivername1 = (TextView) dialog.findViewById(R.id.txt_drivername1);
                txt_driverno1 = (TextView) dialog.findViewById(R.id.txt_driverno1);
                btn_call_driver1 = (ImageView) dialog.findViewById(R.id.btn_call_driver1);

                txt_drivername2 = (TextView) dialog.findViewById(R.id.txt_drivername2);
                txt_driverno2 = (TextView) dialog.findViewById(R.id.txt_driverno2);
                 btn_call_driver2 = (ImageView) dialog.findViewById(R.id.btn_call_driver2);

                txt_drivername3 = (TextView) dialog.findViewById(R.id.txt_drivername3);
                txt_driverno3 = (TextView) dialog.findViewById(R.id.txt_driverno3);
                btn_call_driver3 = (ImageView) dialog.findViewById(R.id.btn_call_driver3);


               Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "mont.ttf");
                txt_drivername1.setTypeface(tf);
                txt_driverno1.setTypeface(tf);

                txt_drivername2.setTypeface(tf);
                txt_driverno2.setTypeface(tf);

                txt_drivername3.setTypeface(tf);
                txt_driverno3.setTypeface(tf);


                txt_drivername1.setText(driver_name1);
                txt_drivername2.setText(driver_name2);
                txt_drivername3.setText(driver_name3);

                txt_driverno1.setText(driver_phone1);
                txt_driverno2.setText(driver_phone2);
                txt_driverno3.setText(driver_phone3);


                img_cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_call_driver1.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {

                        str_img1=txt_driverno1.getText().toString();

                        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                        } else {

                            Log.e("tag","we"+str_img1);
                            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                            phoneIntent.setData(Uri.parse("tel:"+str_img1));
                            phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            try {
                                v.getContext().startActivity(phoneIntent);

                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(v.getContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
                            }
                        }

                        //
                    }
                });

                btn_call_driver2.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {


                        str_img2=txt_driverno2.getText().toString();

                        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                        } else {

                            Log.e("tag","we"+str_img2);
                            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                            phoneIntent.setData(Uri.parse("tel:"+str_img2));
                            phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            try {
                                v.getContext().startActivity(phoneIntent);

                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(v.getContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //
                    }
                });

                btn_call_driver3.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {


                        str_img3=txt_driverno3.getText().toString();

                        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                        } else {

                            Log.e("tag","we"+str_img3);
                            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                            phoneIntent.setData(Uri.parse("tel:"+str_img3));
                            phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            try {
                                v.getContext().startActivity(phoneIntent);

                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(v.getContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });



                dialog.show();

            }

        });

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        placedet = new ArrayList<PlaceDetails>();

        placedet = db.getAllContacts();
        googleMap = mMapView.getMap();
        int i = 0;
        Log.e("tag", "placedetails" + placedet);

        Log.e("tag", "placedetails" + placedet.size());
        for (final PlaceDetails cn : placedet) {


            final MarkerOptions markerOptions = new MarkerOptions();

            double lat = Double.parseDouble(cn.getLatitude());

            double lng = Double.parseDouble(cn.getLongitude());
            final String address = cn.getAddress();
            Log.d("tag", "TYPE" + cn.getPlaceName());
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
           markerOptions.snippet(""+i);
            //markerOptions.
            markerOptions.title(cn.getPlaceName());
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.auto_small));

            googleMap.addMarker(markerOptions);
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(12.730045, 79.9815063)));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            ++i;

          googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    try {

                        lin_hide.setVisibility(View.VISIBLE);
                        PlaceDetails cn = placedet.get(Integer.parseInt(marker.getSnippet()));
                        Log.e("tag","123"+cn);
                        String str_phn1 = cn.getPhone1();
                        String str_phn2 = cn.getPhone2();
                        String str_phn3 = cn.getPhone3();

                        String str_driver1 = cn.getname1();
                        String str_driver2 = cn.getname2();
                        String str_driver3 = cn.getname3();

                        Log.e("tag","phone"+str_phn1+str_phn2+str_phn3);
                        Log.e("tag","driver"+str_driver1+str_driver2+str_driver3);

                        LatLng a = marker.getPosition();
                        String pname = cn.getPlaceName();
                        String desc = cn.getAddress();
                        String time = cn.getTime();

                        t1.setText(pname);
                        t2.setText(desc);
                        t3.setText(time);



                      SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor edit = s_pref.edit();
                        edit.putString("d1", str_driver1);
                        edit.putString("d2", str_driver2);
                        edit.putString("d3", str_driver3);

                        edit.putString("ph1",str_phn1);
                        edit.putString("ph2",str_phn2);
                        edit.putString("ph3",str_phn3);
                        edit.commit();


                    } catch (Exception e) {

                    }
                    return false;
                }
            });
        }


       if(checkPermission())
        {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();


            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                onLocationChanged(location);
            }
            else
            {
                // showGPSDisabledAlertToUser();
            }
            locationManager.requestLocationUpdates(bestProvider, 20000, 0,this);
        }



        return view;
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(13f).build();

        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon)));


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



    }



    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);


        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED)) {
            Log.e("tag", "Permission is granted");
          //  DashboardTest.invokeCameraVideoPicker();
            return true;


        } else {
            Log.e("tag", "Permission is revoked");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return false;

        }
    }

}

