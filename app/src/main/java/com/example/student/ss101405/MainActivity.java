package com.example.student.ss101405;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    public void click1(View view) {
        //檢查GPS是否沒開啟
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) )
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("你是否想開啟 GPS?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //到Android開啟GPS設定頁面
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.show();
        }

        //檢查Permission是否給予權限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            return;
        }
        else
        {
            turnOnGPS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                turnOnGPS();
            }
        }
    }

    private void turnOnGPS()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        //位置有變動時呼叫
        lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0, 0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d("LOC", location.getLatitude() + "," + location.getLongitude());
                        // 25.0338998,121.5648854
                        Location loc101 = new Location("MINE");
                        loc101.setLatitude(25.0338);
                        loc101.setLongitude(121.56488);
                        //計算距離
                        float f1 = location.distanceTo(loc101);
                        Log.d("LOC", "離 101 有：" + f1 + "公尺遠");
                        //從經緯度反查地地
                        Geocoder gc = new Geocoder(MainActivity.this, Locale.TRADITIONAL_CHINESE);
                        try {
                            List<Address> lstAddress = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            Log.d("LOC", lstAddress.get(0).getAddressLine(0));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                }
        );
    }
}
