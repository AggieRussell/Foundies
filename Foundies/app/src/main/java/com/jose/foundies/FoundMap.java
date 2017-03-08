package com.jose.foundies;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class FoundMap extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onSearch(View view){
        EditText locationTextField = (EditText)findViewById(R.id.address);
        String locationText = locationTextField.getText().toString();
        List<Address> addressList = null;
        Address address = null;
        if(locationText != null && !locationText.equals("")){
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(locationText, 1);
                address = addressList.get(0);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("No results returned from location.");
            } catch (IndexOutOfBoundsException e){
                /*
                    Catch when there is no result returned from the address string
                 */
                e.printStackTrace();
                System.out.println("No results returned from location");
            }
            try {
                LatLng cStat = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(cStat).title(address.getFeatureName()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cStat, 15));
            }
            catch(NullPointerException e){
                /*
                    If there is no data in the address string.
                 */
                Context context = getApplicationContext();
                CharSequence text = "No Results!";
                int duration = Toast.LENGTH_SHORT;

                /*
                    Print out "No results!" to device
                 */
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                e.printStackTrace();
                System.out.println("No results");
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            //TODO: Need to figure out how to ask user for permission
        }
    }
}
