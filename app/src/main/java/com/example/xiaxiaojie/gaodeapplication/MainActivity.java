package com.example.xiaxiaojie.gaodeapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private MapView mapView;
    private AMap aMap;
    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mapView.onCreate(savedInstanceState);
        init();
        RadioButton rb=(RadioButton)findViewById(R.id.gps);
        rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300, 8, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            updatePosition(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            updatePosition(locationManager.getLastKnownLocation(provider));
                        }

                    });
                }
            }
        });
        Button bn = (Button)findViewById(R.id.loc);
        final TextView latTv=(TextView)findViewById(R.id.lat);
        final TextView lngTv=(TextView)findViewById(R.id.lng);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lng = lngTv.getEditableText().toString().trim();
                String lat = latTv.getEditableText().toString().trim();
                if (lng.equals("") || lat.equals("")) {
                    Toast.makeText(MainActivity.this, "请输入正确的经纬度", Toast.LENGTH_SHORT).show();

                } else {
                    ((RadioButton) findViewById(R.id.manual)).setChecked(true);
                    double dLng = Double.parseDouble(lng);
                    double dLat = Double.parseDouble(lat);
                    LatLng pos = new LatLng(dLat, dLng);
                    CameraUpdate cu = CameraUpdateFactory.changeLatLng(pos);
                    aMap.moveCamera(cu);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(pos);
                    markerOptions.title("沈阳师范大学");
                    markerOptions.snippet("沈阳师范大学");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    markerOptions.draggable(true);
                    Marker marker = aMap.addMarker(markerOptions);
                    marker.showInfoWindow();
                    MarkerOptions markerOptions1 = new MarkerOptions();
                    markerOptions1.position(new LatLng(dLat + 0.001, dLng)).title("北区食堂").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).draggable(true);
                    ArrayList<BitmapDescriptor> giflist = new ArrayList<>();
                    giflist.add(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    giflist.add(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    giflist.add(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    MarkerOptions markerOptions2 = new MarkerOptions().position(new LatLng(dLat - 0.001, dLng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("北区宿舍").draggable(true).period(10);
                    ArrayList<MarkerOptions> optionsList = new ArrayList<>();
                    optionsList.add(markerOptions1);
                    optionsList.add(markerOptions2);
                    aMap.addMarkers(optionsList, true);
                }
            }

        });
    }
    private void updatePosition(Location location)
    {
        LatLng pos= new LatLng(location.getLatitude(),location.getLongitude());
        CameraUpdate cu=CameraUpdateFactory.changeLatLng(pos);
        aMap.moveCamera(cu);
        aMap.clear();
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(pos);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background));
        markerOptions.draggable(true);
        Marker marker=aMap.addMarker(markerOptions);
    }
    private void init(){
        if (aMap==null){
            aMap=mapView.getMap();
            CameraUpdate cu=CameraUpdateFactory.zoomTo(15);
            aMap.moveCamera(cu);
            CameraUpdate tiltUpdate=CameraUpdateFactory.changeTilt(30);
            aMap.moveCamera(tiltUpdate);
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }
}
